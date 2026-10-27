# File: res://scripts/kadi_self_test.gd
class_name KadiSelfTest
extends RefCounted

static func run_all_tests() -> void:
	test_reshuffle_mid_penalty()
	test_no_win_while_opponent_cardless()
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
