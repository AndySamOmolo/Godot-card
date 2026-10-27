# File: res://scripts/game_state.gd
# Autoload Singleton: GameState
extends Node

signal state_updated()
signal penalty_updated(new_total: int)
signal pile_recycled(new_deck_size: int)

var deck: Array[CardData] = []
var discard_pile: Array[CardData] = []
var hands: Array = [[], [], [], []]
var penalty_stack: int = 0
var declared_suit: int = -1
var announced_kadi: Array[bool] = [false, false, false, false]
var last_play_was_legal: bool = true

func _ready() -> void:
	deck = build_deck()

func build_deck() -> Array[CardData]:
	var new_deck: Array[CardData] = []
	for suit in [CardTypes.Suits.HEARTS, CardTypes.Suits.DIAMONDS, CardTypes.Suits.CLUBS, CardTypes.Suits.SPADES]:
		for rank in range(2, 15):
			new_deck.append(CardData.make(suit, rank))
	new_deck.append(CardData.make(CardTypes.Suits.HEARTS, CardTypes.Ranks.JOKER))
	new_deck.append(CardData.make(CardTypes.Suits.SPADES, CardTypes.Ranks.JOKER))
	new_deck.shuffle()
	return new_deck

func start_new_match(player_count: int = 4, cards_each: int = 4) -> void:
	deck = build_deck()
	discard_pile.clear()
	hands.clear()
	announced_kadi.clear()
	penalty_stack = 0
	declared_suit = -1
	last_play_was_legal = true
	for _p in range(player_count):
		var hand: Array[CardData] = []
		for _c in range(cards_each):
			hand.append(deck.pop_back())
		hands.append(hand)
		announced_kadi.append(false)
	discard_pile.append(draw_starting_card())
	state_updated.emit()

func is_valid_starter(card: CardData) -> bool:
	return card.can_start_discard()

func draw_starting_card() -> CardData:
	var card: CardData = deck.pop_back()
	while not is_valid_starter(card):
		deck.push_front(card)
		deck.shuffle()
		card = deck.pop_back()
	return card

func top_discard() -> CardData:
	return discard_pile.back() if not discard_pile.is_empty() else null

func draw_cards(player: int, count: int = 1) -> Array[CardData]:
	var drawn: Array[CardData] = []
	for _i in range(count):
		reshuffle_if_empty()
		if not deck.is_empty():
			var c: CardData = deck.pop_back()
			hands[player].append(c)
			drawn.append(c)
	announced_kadi[player] = false
	state_updated.emit()
	return drawn

func apply_penalty_card(card: CardData) -> void:
	penalty_stack += card.penalty_value()
	penalty_updated.emit(penalty_stack)

func consume_penalty_stack(player: int) -> int:
	var count: int = penalty_stack
	penalty_stack = 0
	draw_cards(player, count)
	penalty_updated.emit(0)
	return count

func reshuffle_if_empty() -> void:
	if deck.size() > 0 or discard_pile.size() <= 1:
		return
	var top_card: CardData = discard_pile.pop_back()
	deck = discard_pile.duplicate()
	deck.shuffle()
	discard_pile = [top_card]
	randomize()
	pile_recycled.emit(deck.size())

func choose_best_suit_for_ai(hand: Array) -> int:
	var counts: Dictionary = {0: 0, 1: 0, 2: 0, 3: 0}
	for c in hand:
		counts[c.suit] = counts.get(c.suit, 0) + 1
	var best_suit: int = CardTypes.Suits.HEARTS
	for s in [0, 1, 2, 3]:
		if counts[s] > counts[best_suit]:
			best_suit = s
	return best_suit
