package service

import entity.*

/**
 * GameService manages the game state and operations for a pyramid card game.
 * @property rootService The root service for managing overall game state.
 */
class GameService(private val rootService: RootService) : AbstractRefreshingService() {
    val drawStack = CardStack()
    val reserveStack = CardStack()
    val pyramid = Pyramid(arrayOf())

    /**
     * Initializes and starts a new game of Pyramid for two players.
     *
     * @param player1Name The name of the first player.
     * @param player2Name The name of the second player.
     */
    fun startGame(player1Name: String, player2Name: String) {
        val player1 = Player(player1Name)
        val player2 = Player(player2Name)
        val allCards = defaultRandomCardList().shuffled()
        val first28Cards = allCards.take(28) // Take the first 28 cards
        val remainingCards = allCards.drop(28) // Drop the first 28 cards and take the rest
        val pyramid = createPyramid(first28Cards)
        val drawStack = createDrawStack(remainingCards)
        val game = PyramideGame(player1, player2, 1, false, pyramid, drawStack, reserveStack)

        // Reset players' scores
        player1.currentScore = 0
        player2.currentScore = 0

        rootService.currentGame = game
        // Reset the draw and reserve stacks
        drawStack.cards.clear()
        drawStack.cards.addAll(remainingCards)

        reserveStack.cards.clear()

        onAllRefreshables { refreshAfterStartGame() }
    }

    /**
     * Switches the current player turn in the game.
     */
    fun changePlayer() {
        val game = rootService.currentGame
        checkNotNull(game)
        if (game.currentPlayer == 1) {
            game.currentPlayer = 2
        } else {
            game.currentPlayer = 1
        }

        rootService.currentGame = game
        onAllRefreshables { refreshAfterChangePlayer() }
    }

    /**
     * Checks if the selected pair of cards is a valid choice according to the game rules.
     *
     * @param card1 The first card selected.
     * @param card2 The second card selected.
     * @return Boolean indicating if the pair is valid (true) or not (false).
     */
    fun checkCardChoice(card1: Card, card2: Card): Boolean {
        val cardValues = mapOf(
            CardValue.ACE to 999,
            CardValue.TWO to 2,
            CardValue.THREE to 3,
            CardValue.FOUR to 4,
            CardValue.FIVE to 5,
            CardValue.SIX to 6,
            CardValue.SEVEN to 7,
            CardValue.EIGHT to 8,
            CardValue.NINE to 9,
            CardValue.TEN to 10,
            CardValue.JACK to 11,
            CardValue.QUEEN to 12,
            CardValue.KING to 13
        )
        val value1 = cardValues[card1.value] ?: 0
        val value2 = cardValues[card2.value] ?: 0
        return (value1 + value2 == 15 || card1.value == CardValue.ACE || card2.value == CardValue.ACE) && !(card1.value == CardValue.ACE && card2.value == CardValue.ACE)
    }

    /**
     * Ends the current game and triggers end game actions.
     */
    fun endGame() {

        val game = rootService.currentGame
        checkNotNull(game)


        if (pyramidIsEmpty() || game.passed) {
            onAllRefreshables { refreshAfterEndGame() }
        }

    }

    private fun pyramidIsEmpty(): Boolean{
        val pyramid = rootService.currentGame?.pyramid?.cards
        checkNotNull(pyramid)

        for(level in pyramid){
            for(index in level){
                println(index != null)
                if(index != null) return false
            }
        }
        return true
    }


    /**
     * Creates a draw stack from the remaining cards not used in the pyramid.
     *
     * @param remainingCards The list of cards not used in the pyramid.
     * @return The created CardStack object representing the draw stack.
     */
    fun createDrawStack(remainingCards: List<Card>): CardStack {
        val drawStack = CardStack()
        drawStack.cards.addAll(remainingCards)
        return drawStack
    }

    /**
     * Checks if a card at the specified position has adjacent cards in the pyramid.
     *
     * @param row The row index of the card in the pyramid.
     * @param col The column index of the card in the pyramid.
     * @return Boolean indicating if the card has adjacent cards (true) or not (false).
     */
    fun hasAdjacentRechtsCards(row: Int, col: Int): Boolean {
        val pyramid = rootService.currentGame?.pyramid ?: return false
        val hasRight = col + 1 < pyramid.cards[row].size && pyramid.cards[row].getOrNull(col + 1) != null

        return hasRight
    }

    fun hasAdjacentLinkCards(row: Int, col: Int): Boolean {
        val pyramid = rootService.currentGame?.pyramid ?: return false

        val hasLeft = col - 1 >= 0 && pyramid.cards[row].getOrNull(col - 1) != null

        return hasLeft
    }

    /**
     * Flips adjacent cards to the specified position if they have no other adjacent cards.
     *
     * @param row The row index of the target card in the pyramid.
     * @param col The column index of the target card in the pyramid.
     */
    fun flipAdjacentCards(row: Int, col: Int) {
        val pyramid = rootService.currentGame?.pyramid ?: return

        // Check and flip the card to the left if it exists and has no other adjacent card
        if (col - 1 >= 0 && !hasAdjacentRechtsCards(row, col - 1)) {
            pyramid.cards[row][col - 1]?.isRevealed = true
        }

        // Check and flip the card to the right if it exists and has no other adjacent card
        if (col + 1 < pyramid.cards[row].size && !hasAdjacentLinkCards(row, col + 1)) {
            pyramid.cards[row][col + 1]?.isRevealed = true
        }

        rootService.currentGame!!.pyramid = pyramid
    }

    /**
     * Finds the position of a given card in the pyramid.
     *
     * @param card The card to find.
     * @return A Pair of Integers representing the row and column of the card, or null if not found.
     */
    fun findCardPosition(card: Card): Pair<Int, Int>? {

        val game = rootService.currentGame
        checkNotNull(game)

        val pyramidCards = game.pyramid.cards

        for (row in pyramidCards.indices) {
            for (col in pyramidCards[row].indices) {  // Ensure correct iteration over columns
                if (pyramidCards[row][col] == card) {
                    return Pair(row, col)
                }
            }
        }

        if (card == game.reserveStack.cards.last()) {
            return Pair(-1, -1)
        }

        return null

    }

    /**
     * Creates a default list of cards in random order.
     *
     * @return A shuffled list of all cards.
     */
    fun defaultRandomCardList(): List<Card> {
        val suits = CardSuit.values()
        val values = CardValue.values()
        val cards = mutableListOf<Card>()

        for (suit in suits) {
            for (value in values) {
                cards.add(Card(suit, value))
            }
        }

        return cards.shuffled()
    }

    /**
     * Constructs a pyramid layout with the first 28 cards.
     *
     * @param first28Cards The first 28 cards to use for the pyramid.
     * @return The Pyramid object representing the initial state of the game's pyramid.
     */
    fun createPyramid(first28Cards: List<Card>): Pyramid {
        val pyramidArray: Array<Array<Card?>> = Array(7) { row ->
            Array<Card?>(row + 1) { null }
        }

        var cardIndex = 0
        for (row in pyramidArray.indices) {
            for (col in pyramidArray[row].indices) {
                val card = first28Cards[cardIndex++]

                // Reveal the card if it's on the outer edge of the pyramid
                if (col == 0 || col == pyramidArray[row].lastIndex) {
                    card.isRevealed = true
                }

                pyramidArray[row][col] = card
            }
        }
        println(first28Cards)

        return Pyramid(pyramidArray)
    }
}