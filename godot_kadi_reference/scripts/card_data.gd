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

const ASSET_RANK_WORDS: Dictionary = {
	11: "jack", 12: "queen", 13: "king", 14: "ace"
}

@export var suit: CardTypes.Suits = CardTypes.Suits.HEARTS
@export var rank: int = 2
@export var display_name: String = ""
@export var custom_texture: Texture2D = null

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

## Resolves a downloaded card texture from res://assets/cards/ automatically
## supporting the 3 most common downloaded card asset pack naming conventions:
## 1) suit_rank.png      (e.g. hearts_7.png, spades_14.png, joker_red.png)
## 2) rank_of_suit.png   (e.g. 7_of_hearts.png, ace_of_spades.png, red_joker.png)
## 3) Kenney card pack   (e.g. cardHearts7.png, cardSpadesA.png, cardJoker.png)
func resolve_downloaded_texture() -> Texture2D:
	if custom_texture != null:
		return custom_texture
	for candidate in get_candidate_texture_paths():
		if ResourceLoader.exists(candidate):
			var tex = load(candidate)
			if tex is Texture2D:
				return tex
	return null

func get_candidate_texture_paths() -> Array[String]:
	if rank == CardTypes.Ranks.JOKER:
		var color_tag: String = "red" if is_red_suit() else "black"
		return [
			"res://assets/cards/joker_%s.png" % color_tag,
			"res://assets/cards/%s_joker.png" % color_tag,
			"res://assets/cards/cardJoker.png",
			"res://assets/cards/joker.png"
		]
	var suit_cap: String = SUIT_NAMES.get(suit, "Hearts")
	var suit_low: String = suit_cap.to_lower()
	var r_word: String = ASSET_RANK_WORDS.get(rank, str(rank))
	var r_short: String = rank_label()
	return [
		"res://assets/cards/%s_%d.png" % [suit_low, rank],
		"res://assets/cards/%s_%02d.png" % [suit_low, rank],
		"res://assets/cards/%s_of_%s.png" % [r_word, suit_low],
		"res://assets/cards/card%s%s.png" % [suit_cap, r_short],
		"res://assets/cards/%s_%s.png" % [suit_low, r_short.to_lower()]
	]
