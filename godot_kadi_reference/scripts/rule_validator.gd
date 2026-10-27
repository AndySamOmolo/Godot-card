# File: res://scripts/rule_validator.gd
class_name RuleValidator
extends RefCounted

static func is_question_card(rank: int) -> bool:
	return rank == CardTypes.Ranks.QUEEN or rank == CardTypes.Ranks.EIGHT

static func is_answer_card(rank: int) -> bool:
	return rank in [4, 5, 6, 7, 9, 10, CardTypes.Ranks.ACE]

static func can_counter_penalty(card: CardData, top_penalty: CardData) -> bool:
	if top_penalty == null:
		return false
	if card.rank == CardTypes.Ranks.ACE:
		return true
	return card.is_penalty() and card.rank == top_penalty.rank

static func is_single_card_playable(card: CardData, top: CardData, penalty_stack: int = 0, declared_suit: int = -1) -> bool:
	if top == null:
		return true
	if penalty_stack > 0:
		return can_counter_penalty(card, top)
	if declared_suit != -1:
		return card.suit == declared_suit or card.rank == CardTypes.Ranks.ACE or card.rank == CardTypes.Ranks.JOKER
	if card.rank == CardTypes.Ranks.ACE or card.rank == CardTypes.Ranks.JOKER:
		return true
	return card.suit == top.suit or card.rank == top.rank

static func validate_question_chain(cards: Array) -> bool:
	for i in range(cards.size()):
		var card: CardData = cards[i]
		if not is_question_card(card.rank):
			continue
		if i + 1 >= cards.size():
			return false
		var next_card: CardData = cards[i + 1]
		if is_question_card(next_card.rank):
			continue
		if not is_answer_card(next_card.rank):
			return false
		if next_card.suit != card.suit and next_card.rank != CardTypes.Ranks.ACE:
			return false
	return true

static func validate_move(cards: Array, top: CardData, penalty_stack: int, declared_suit: int) -> bool:
	if cards.is_empty():
		return false
	if not is_single_card_playable(cards[0], top, penalty_stack, declared_suit):
		return false
	if not validate_question_chain(cards):
		return false
	for i in range(cards.size() - 1):
		var cur: CardData = cards[i]
		var nxt: CardData = cards[i + 1]
		if not is_question_card(cur.rank) and cur.rank != nxt.rank:
			return false
	return true

static func is_winning_move_cards(cards: Array) -> bool:
	if cards.is_empty():
		return false
	for c in cards:
		if not (is_answer_card(c.rank) or is_question_card(c.rank)):
			return false
	return validate_question_chain(cards)

static func has_won(player: int, played_cards: Array = []) -> bool:
	var hand_empty: bool = (GameState.hands[player].size() == 0)
	var declared_niko: bool = GameState.announced_kadi[player]
	var final_play_legal: bool = GameState.last_play_was_legal
	var valid_cards: bool = true if played_cards.is_empty() else is_winning_move_cards(played_cards)
	var no_opponent_cardless: bool = true
	for other in range(GameState.hands.size()):
		if other != player and GameState.hands[other].size() == 0:
			no_opponent_cardless = false
	return hand_empty and declared_niko and final_play_legal and valid_cards and no_opponent_cardless
