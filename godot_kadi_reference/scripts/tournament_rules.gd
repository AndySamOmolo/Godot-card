# File: res://scripts/tournament_rules.gd
class_name TournamentRules
extends RefCounted

const ELIMINATION_POINTS: int = 100

static func card_points(card: CardData) -> int:
	if card == null:
		return 0
	match card.rank:
		CardTypes.Ranks.JOKER: return 50
		CardTypes.Ranks.THREE: return 30
		CardTypes.Ranks.TWO, CardTypes.Ranks.ACE: return 20
		CardTypes.Ranks.JACK, CardTypes.Ranks.QUEEN, CardTypes.Ranks.KING: return 10
		_: return card.rank

static func compute_hand_points(hand: Array) -> int:
	var total: int = 0
	for c in hand:
		total += card_points(c)
	return total

## Heuristic scorer so smart AI sheds high-risk cards while saving 1 Ace for defense:
static func score_ai_move(cards: Array, penalty_stack: int, hand_size: int) -> int:
	if cards.is_empty():
		return -999
	var score: int = 0
	for c in cards:
		score += card_points(c)
		if c.rank == CardTypes.Ranks.ACE and penalty_stack == 0 and hand_size > 2:
			score -= 35
	return score
