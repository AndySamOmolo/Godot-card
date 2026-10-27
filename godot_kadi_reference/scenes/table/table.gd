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
	ai_turn_timer.one_shot = true
	ai_turn_timer.timeout.connect(_on_ai_turn_timer_timeout)
	play_button.pressed.connect(_on_play_selected_pressed)
	draw_button.pressed.connect(_on_draw_pressed)
	niko_button.pressed.connect(_on_niko_pressed)
	declare_suit_dialog.suit_declared.connect(_on_suit_declared)
	TurnManager.turn_changed.connect(_on_turn_changed)
	GameState.state_updated.connect(refresh_table_ui)
	if OS.is_debug_build():
		KadiSelfTest.run_all_tests()
	start_match()

func start_match() -> void:
	match_over = false
	staged_cards.clear()
	niko_label.text = ""
	feedback_label.text = "Karibu Kadi! Select matching card(s) and click Play Selected."
	GameState.start_new_match(4, 4)
	TurnManager.reset_turns(4)

func refresh_table_ui() -> void:
	_clear_children(discard_slot)
	_clear_children(player_hand_container)

	var top: CardData = GameState.top_discard()
	if top != null:
		var top_node: Control = CardScene.instantiate()
		discard_slot.add_child(top_node)
		top_node.setup(top)

	var human_hand: Array = GameState.hands[0]
	for card_data in human_hand:
		var card_node: Control = CardScene.instantiate()
		player_hand_container.add_child(card_node)
		card_node.setup(card_data)
		card_node.set_selected(card_data in staged_cards)
		card_node.card_clicked.connect(_on_hand_card_clicked)

	var suit_demand: String = CardData.SUIT_NAMES.get(GameState.declared_suit, "None")
	var dir_str: String = "↻ CW" if TurnManager.direction == 1 else "↺ CCW"
	status_label.text = "Turn: P%d (%s) | Deck: %d | Penalty: +%d | Demand: %s | Opponents: P2(%d) P3(%d) P4(%d)" % [
		TurnManager.current_player_index + 1, dir_str, GameState.deck.size(),
		GameState.penalty_stack, suit_demand,
		GameState.hands[1].size(), GameState.hands[2].size(), GameState.hands[3].size()
	]
	draw_button.text = "Accept Penalty (+%d)" % GameState.penalty_stack if GameState.penalty_stack > 0 else "Draw Card"
	play_button.text = "Play Selected (%d)" % staged_cards.size()

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
	niko_label.text = "📢 You announced: NIKO KADI!"

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
	var skips: int = 0
	var names: Array[String] = []
	for c in cards:
		names.append(c.display_name)
		GameState.hands[player].erase(c)
		GameState.discard_pile.append(c)
		if GameState.declared_suit != -1 and c.suit == GameState.declared_suit and c.rank != CardTypes.Ranks.ACE:
			GameState.declared_suit = -1
		if c.rank == CardTypes.Ranks.JACK:
			skips += 1
		elif c.rank == CardTypes.Ranks.KING:
			TurnManager.reverse_direction()
		elif c.is_penalty():
			GameState.apply_penalty_card(c)
		elif c.rank == CardTypes.Ranks.ACE and had_active_penalty:
			GameState.penalty_stack = 0

	feedback_label.text = "Player %d played: %s" % [player + 1, ", ".join(names)]
	GameState.state_updated.emit()

	if RuleValidator.has_won(player, cards):
		match_over = true
		feedback_label.text = "🏆 PLAYER %d WINS THE KADI MATCH!" % (player + 1)
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
	feedback_label.text = "You declared suit: %s" % CardData.SUIT_NAMES.get(suit, "Any")
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

	for c in hand:
		if not RuleValidator.is_question_card(c.rank) and RuleValidator.is_single_card_playable(c, top, GameState.penalty_stack, GameState.declared_suit):
			var move: Array[CardData] = [c]
			if hand.size() - 1 == 1:
				for rem in hand:
					if rem != c and RuleValidator.is_answer_card(rem.rank):
						GameState.announced_kadi[ai_seat] = true
						niko_label.text = "📢 Player %d says: NIKO KADI!" % (ai_seat + 1)
			_execute_move(ai_seat, move)
			return

	_draw_or_accept_penalty(ai_seat)
	TurnManager.advance_turn()

func _clear_children(container: Node) -> void:
	for child in container.get_children():
		child.queue_free()
