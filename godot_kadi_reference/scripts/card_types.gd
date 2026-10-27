# File: res://scripts/card_types.gd
class_name CardTypes
extends RefCounted

enum Suits {
	HEARTS = 0,
	DIAMONDS = 1,
	CLUBS = 2,
	SPADES = 3
}

enum Ranks {
	TWO = 2, THREE = 3, FOUR = 4, FIVE = 5, SIX = 6, SEVEN = 7,
	EIGHT = 8, NINE = 9, TEN = 10,
	JACK = 11, QUEEN = 12, KING = 13, ACE = 14, JOKER = 15
}

enum CardKind {
	ANSWER,
	QUESTION,
	JUMP,
	KICKBACK,
	PENALTY,
	WILD_ACE
}
