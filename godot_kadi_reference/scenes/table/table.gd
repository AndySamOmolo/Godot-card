# File: res://scenes/table/table.gd
extends Node2D

const CardScene: PackedScene = preload("res://scenes/card/card_scene.tscn")

@onready var discard_slot: Control = $DiscardSlot
@onready var player_hand_container: HBoxContainer = $PlayerHandContainer
@onready var status_label: Label = $HUD/StatusLabel
@onready var feedback_label: Label = $HUD/FeedbackLabel
@onready var niko_label: Label = $HUD/NikoKadiLabel
@onready var play_button: Button = $HUD/PlaySelectedButton
@onready var draw_button: Button = $HUD/DrawButton
@onready var niko_button: Button = $HUD/NikoKadiButton
@onready var declare_suit_dialog: DeclareSuitDialog = $HUD/DeclareSuitDialog
@onready var ai_turn_timer: Timer = $AITurnTimer

var staged_cards: Array[CardData] = []
var match_over: bool = false

func _ready() -> void:
	if OS.is_debug_build():
		KadiSelfTest.run_all_tests()
	ai_turn_timer.one_shot = true
	ai_turn_timer.timeout.connect(_on_ai_turn_timer_timeout)
	play_button.pressed.connect(_on_play_selected_pressed)
	draw_button.pressed.connect(_on_draw_pressed)
	niko_button.pressed.connect(_on_niko_pressed)
	declare_suit_dialog.suit_declared.connect(_on_suit_declared)
	TurnManager.turn_changed.connect(_on_turn_changed)
	GameState.state_updated.connect(refresh_table_ui)
	start_match()

func start_match() -> void:
	match_over = false
	staged_cards.clear()
	niko_label.text = ""
	feedback_label.text = "Karibu Kadi! Select matching card(s) and click Play Selected."
	GameState.start_new_match(4, 4)
	TurnManager.reset_turns(4)

func refresh_table_ui() -> void:
	var top: CardData = GameState.top_discard()
	if top != null:
		var top_node: Control = _ensure_card_child(discard_slot, 0)
		top_node.visible = true
		top_node.setup(top)
		top_node.set_selected(false)
	for extra_idx in range(1, discard_slot.get_child_count()):
		discard_slot.get_child(extra_idx).visible = false

	var human_hand: Array = GameState.hands[0] if not GameState.hands.is_empty() else []
	for i in range(human_hand.size()):
		var card_data: CardData = human_hand[i]
		var card_node: Control = _ensure_card_child(player_hand_container, i)
		card_node.visible = true
		card_node.setup(card_data)
		card_node.set_selected(card_data in staged_cards)
		if not card_node.card_clicked.is_connected(_on_hand_card_clicked):
			card_node.card_clicked.connect(_on_hand_card_clicked)
	for i in range(human_hand.size(), player_hand_container.get_child_count()):
		var unused_node: Control = player_hand_container.get_child(i)
		unused_node.visible = false
		unused_node.set_selected(false)

	var announced_seats: Array[String] = []
	for seat_idx in range(GameState.announced_kadi.size()):
		if GameState.announced_kadi[seat_idx]:
			announced_seats.append("You" if seat_idx == 0 else "P%d" % (seat_idx + 1))
	niko_label.text = "" if announced_seats.is_empty() else "📢 NIKO KADI: %s" % ", ".join(announced_seats)

	var suit_demand: String = CardData.SUIT_NAMES.get(GameState.declared_suit, "None")
	var dir_str: String = "↻ CW" if TurnManager.direction == 1 else "↺ CCW"
	status_label.text = "Turn: P%d (%s) | Deck: %d | Penalty: +%d | Demand: %s | Opponents: P2(%d) P3(%d) P4(%d)" % [
		TurnManager.current_player_index + 1, dir_str, GameState.deck.size(),
		GameState.penalty_stack, suit_demand,
		GameState.hands[1].size(), GameState.hands[2].size(), GameState.hands[3].size()
	]
	draw_button.text = "Accept Penalty (+%d)" % GameState.penalty_stack if GameState.penalty_stack > 0 else "Draw Card"
	play_button.text = "Play Selected (%d)" % staged_cards.size()

func _ensure_card_child(container: Control, index: int) -> Control:
	if index < container.get_child_count():
		return container.get_child(index) as Control
	var created: Control = CardScene.instantiate()
	container.add_child(created)
	return created

func _on_hand_card_clicked(card_node: Control) -> void:
	if match_over or TurnManager.current_player_index != 0:
		return
	var c: CardData = card_node.data
	if c in staged_cards:
		staged_cards.erase(c)
		card_node.set_selected(false)
	else:
		staged_cards.append(c)
		card_node.set_selected(true)
	play_button.text = "Play Selected (%d)" % staged_cards.size()

func _on_play_selected_pressed() -> void:
	if match_over or TurnManager.current_player_index != 0 or staged_cards.is_empty():
		return
	var move: Array[CardData] = staged_cards.duplicate()
	staged_cards.clear()
	_execute_move(0, move)

func _on_draw_pressed() -> void:
	if match_over or TurnManager.current_player_index != 0:
		return
	staged_cards.clear()
	_draw_or_accept_penalty(0)
	TurnManager.advance_turn()

func _on_niko_pressed() -> void:
	if match_over or TurnManager.current_player_index != 0:
		return
	GameState.announced_kadi[0] = true
	refresh_table_ui()

func _draw_or_accept_penalty(player: int) -> void:
	if GameState.penalty_stack > 0:
		var count: int = GameState.consume_penalty_stack(player)
		feedback_label.text = "Player %d picked up +%d penalty cards." % [player + 1, count]
	else:
		GameState.draw_cards(player, 1)
		feedback_label.text = "Player %d drew 1 card." % (player + 1)

func _execute_move(player: int, cards: Array[CardData]) -> void:
	var top: CardData = GameState.top_discard()
	if not RuleValidator.validate_move(cards, top, GameState.penalty_stack, GameState.declared_suit):
		GameState.last_play_was_legal = false
		GameState.draw_cards(player, 1)
		feedback_label.text = "Invalid play by Player %d! +1 penalty card drawn." % (player + 1)
		TurnManager.advance_turn()
		return

	GameState.last_play_was_legal = true
	var had_active_penalty: bool = (GameState.penalty_stack > 0)
	var prior_active_suit: int = GameState.declared_suit if GameState.declared_suit != -1 else (top.suit if top != null else -1)
	var skips: int = 0
	var names: Array[String] = []
	for c in cards:
		names.append(c.display_name)
		GameState.hands[player].erase(c)
		GameState.discard_pile.append(c)
		if GameState.declared_suit != -1 and c.suit == GameState.declared_suit and c.rank != CardTypes.Ranks.ACE and c.rank != CardTypes.Ranks.JOKER:
			GameState.declared_suit = -1
		if c.rank == CardTypes.Ranks.JACK:
			skips += 1
		elif c.rank == CardTypes.Ranks.KING:
			TurnManager.reverse_direction()
		elif c.is_penalty():
			if c.rank == CardTypes.Ranks.JOKER and prior_active_suit != -1:
				c.suit = prior_active_suit as CardTypes.Suits
			GameState.apply_penalty_card(c)
		elif c.rank == CardTypes.Ranks.ACE and had_active_penalty:
			GameState.penalty_stack = 0
			GameState.declared_suit = prior_active_suit
			GameState.penalty_updated.emit(0)

	feedback_label.text = "Player %d played: %s" % [player + 1, ", ".join(names)]
	GameState.state_updated.emit()

	if RuleValidator.has_won(player, cards):
		match_over = true
		SaveManager.record_match_result(player == 0)
		feedback_label.text = "🏆 PLAYER %d WINS THE KADI MATCH! (%s)" % [player + 1, SaveManager.get_summary_text()]
		return

	var last_card: CardData = cards.back()
	if last_card.rank == CardTypes.Ranks.ACE and not had_active_penalty:
		if player == 0:
			declare_suit_dialog.show()
			return
		else:
			GameState.declared_suit = GameState.choose_best_suit_for_ai(GameState.hands[player])

	TurnManager.advance_turn(skips)

func _on_suit_declared(suit: int) -> void:
	GameState.declared_suit = suit
	feedback_label.text = "You declared suit: %s" % CardData.SUIT_NAMES.get(suit, "Any Suit (No Demand)")
	refresh_table_ui()
	TurnManager.advance_turn()

func _on_turn_changed(player_index: int) -> void:
	var is_human: bool = (player_index == 0)
	play_button.disabled = not is_human
	draw_button.disabled = not is_human
	niko_button.disabled = not is_human
	refresh_table_ui()
	if not is_human and not match_over:
		ai_turn_timer.start(0.75)

func _on_ai_turn_timer_timeout() -> void:
	var seat: int = TurnManager.current_player_index
	if seat != 0 and not match_over:
		_execute_ai_turn(seat)

func _execute_ai_turn(ai_seat: int) -> void:
	var top: CardData = GameState.top_discard()
	var hand: Array = GameState.hands[ai_seat]
	var candidate_moves: Array = []

	for c in hand:
		if RuleValidator.is_question_card(c.rank):
			if RuleValidator.is_single_card_playable(c, top, GameState.penalty_stack, GameState.declared_suit):
				for ans in hand:
					if ans != c and RuleValidator.is_answer_card(ans.rank) and (ans.suit == c.suit or ans.rank == CardTypes.Ranks.ACE):
						var qa_move: Array[CardData] = [c, ans]
						if RuleValidator.validate_move(qa_move, top, GameState.penalty_stack, GameState.declared_suit):
							candidate_moves.append(qa_move)
		elif RuleValidator.is_single_card_playable(c, top, GameState.penalty_stack, GameState.declared_suit):
			var single_move: Array[CardData] = [c]
			candidate_moves.append(single_move)

	if not candidate_moves.is_empty():
		var best_move: Array[CardData] = candidate_moves[0]
		var best_score: int = TournamentRules.score_ai_move(best_move, GameState.penalty_stack, hand.size())
		for m in candidate_moves:
			var sc: int = TournamentRules.score_ai_move(m, GameState.penalty_stack, hand.size())
			if sc > best_score:
				best_score = sc
				best_move = m
		if hand.size() - best_move.size() == 1:
			for rem in hand:
				if rem not in best_move and RuleValidator.is_answer_card(rem.rank):
					GameState.announced_kadi[ai_seat] = true
		_execute_move(ai_seat, best_move)
		return

	_draw_or_accept_penalty(ai_seat)
	TurnManager.advance_turn()
