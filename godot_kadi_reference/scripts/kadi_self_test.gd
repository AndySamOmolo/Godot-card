# File: res://scripts/kadi_self_test.gd
class_name KadiSelfTest
extends RefCounted

static func run_all_tests() -> void:
	var saved_deck: Array[CardData] = GameState.deck.duplicate()
	var saved_discard: Array[CardData] = GameState.discard_pile.duplicate()
	var saved_hands: Array = GameState.hands.duplicate(true)
	var saved_kadi: Array[bool] = GameState.announced_kadi.duplicate()
	var saved_penalty: int = GameState.penalty_stack
	var saved_suit: int = GameState.declared_suit

	test_reshuffle_mid_penalty()
	test_no_win_while_opponent_cardless()
	test_question_chain_suit_and_rank_rules()

	GameState.deck = saved_deck
	GameState.discard_pile = saved_discard
	GameState.hands = saved_hands
	GameState.announced_kadi = saved_kadi
	GameState.penalty_stack = saved_penalty
	GameState.declared_suit = saved_suit
	print("✅ All Kadi rule self-tests PASSED!")

static func test_reshuffle_mid_penalty() -> void:
	GameState.hands = [[], [], [], []]
	GameState.announced_kadi = [false, false, false, false]
	GameState.deck = [
		CardData.make(CardTypes.Suits.CLUBS, 9),
		CardData.make(CardTypes.Suits.HEARTS, 4)
	]
	GameState.discard_pile = [
		CardData.make(CardTypes.Suits.SPADES, 5),
		CardData.make(CardTypes.Suits.DIAMONDS, 6),
		CardData.make(CardTypes.Suits.CLUBS, 7),
		CardData.make(CardTypes.Suits.HEARTS, 9),
		CardData.make(CardTypes.Suits.SPADES, 10),
		CardData.make(CardTypes.Suits.HEARTS, CardTypes.Ranks.JOKER)
	]
	var expected_top: CardData = GameState.discard_pile.back()
	GameState.draw_cards(0, 5)
	assert(GameState.hands[0].size() == 5, "all 5 penalty cards must be drawn")
	assert(GameState.discard_pile.size() == 1, "discard must keep exactly the top card")
	assert(GameState.discard_pile.back() == expected_top, "top card must survive reshuffle")

static func test_no_win_while_opponent_cardless() -> void:
	GameState.hands = [[], [], [CardData.make(CardTypes.Suits.CLUBS, 4)], [CardData.make(CardTypes.Suits.SPADES, 6)]]
	GameState.announced_kadi = [true, false, false, false]
	GameState.last_play_was_legal = true
	assert(not RuleValidator.has_won(0), "cannot win while Seat 1 is also cardless")

static func test_question_chain_suit_and_rank_rules() -> void:
	var q_hearts := CardData.make(CardTypes.Suits.HEARTS, CardTypes.Ranks.QUEEN)
	var eight_spades := CardData.make(CardTypes.Suits.SPADES, CardTypes.Ranks.EIGHT)
	var four_spades := CardData.make(CardTypes.Suits.SPADES, 4)
	assert(
		not RuleValidator.validate_question_chain([q_hearts, eight_spades, four_spades]),
		"unrelated suit/rank Question cards (Q♥ -> 8♠) must be rejected"
	)
	var eight_hearts := CardData.make(CardTypes.Suits.HEARTS, CardTypes.Ranks.EIGHT)
	var seven_hearts := CardData.make(CardTypes.Suits.HEARTS, 7)
	assert(
		RuleValidator.validate_question_chain([q_hearts, eight_hearts, seven_hearts]),
		"same-suit Question chain (Q♥ -> 8♥ -> 7♥) must be accepted"
	)
