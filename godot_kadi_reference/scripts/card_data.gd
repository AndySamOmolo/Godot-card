# File: res://scripts/card_data.gd
class_name CardData
extends Resource

const SUIT_SYMBOLS: Dictionary = {
	CardTypes.Suits.HEARTS: "♥",
	CardTypes.Suits.DIAMONDS: "♦",
	CardTypes.Suits.CLUBS: "♣",
	CardTypes.Suits.SPADES: "♠"
}

const SUIT_NAMES: Dictionary = {
	CardTypes.Suits.HEARTS: "Hearts",
	CardTypes.Suits.DIAMONDS: "Diamonds",
	CardTypes.Suits.CLUBS: "Clubs",
	CardTypes.Suits.SPADES: "Spades"
}

const RANK_LABELS: Dictionary = {
	11: "J", 12: "Q", 13: "K", 14: "A", 15: "JOK"
}

@export var suit: CardTypes.Suits = CardTypes.Suits.HEARTS
@export var rank: int = 2
@export var display_name: String = ""

static func make(s: CardTypes.Suits, r: int) -> CardData:
	var c := CardData.new()
	c.suit = s
	c.rank = r
	c.display_name = "JOK" if r == CardTypes.Ranks.JOKER else "%s%s" % [c.rank_label(), c.suit_symbol()]
	return c

func rank_label() -> String:
	return RANK_LABELS.get(rank, str(rank))

func suit_symbol() -> String:
	return "★" if rank == CardTypes.Ranks.JOKER else SUIT_SYMBOLS.get(suit, "?")

func is_red_suit() -> bool:
	return suit == CardTypes.Suits.HEARTS or suit == CardTypes.Suits.DIAMONDS

func is_penalty() -> bool:
	return rank == CardTypes.Ranks.TWO or rank == CardTypes.Ranks.THREE or rank == CardTypes.Ranks.JOKER

func penalty_value() -> int:
	match rank:
		CardTypes.Ranks.TWO: return 2
		CardTypes.Ranks.THREE: return 3
		CardTypes.Ranks.JOKER: return 5
		_: return 0

func is_question() -> bool:
	return rank == CardTypes.Ranks.QUEEN or rank == CardTypes.Ranks.EIGHT

func is_answer() -> bool:
	return rank in [4, 5, 6, 7, 9, 10, CardTypes.Ranks.ACE]

func can_start_discard() -> bool:
	return rank in [4, 5, 6, 7, 9, 10]
