package service

import entity.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/**
 * GameServiceTest manages the game test for a pyramid card game.
 */
class ServiceTest {

    private lateinit var gameService: GameService
    private lateinit var rootService: RootService

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = GameService(rootService)
        gameService.startGame("Alice", "Bob")
    }

    /**
     * Tests the initialization of a new game, ensuring correct setup of players, pyramid, and card stacks.
     */
    @Test
    fun testStartGame() {
        val game = rootService.currentGame
        assertNotNull(game)
        assertEquals("Alice", game!!.player1.name)
        assertEquals("Bob", game.player2.name)
        assertEquals(1, game.currentPlayer)
        assertFalse(game.passed)
        assertEquals(28, game.pyramid.cards.sumOf { it.count { card -> card != null } })
        assertEquals(24, game.drawStack.size) // 52 - 28 = 24
    }

    /**
     * Tests the functionality of switching player turns, verifying that the current player is correctly updated.
     */
    @Test
    fun testChangePlayer() {
        val initialPlayer = rootService.currentGame!!.currentPlayer
        gameService.changePlayer()
        val newPlayer = rootService.currentGame!!.currentPlayer
        assertNotEquals(initialPlayer, newPlayer)
    }

    /**
     * Tests the card choice validation logic, ensuring that valid and invalid pairs of cards are correctly identified.
     */
    @Test
    fun testCheckCardChoice() {
        val card1 = Card(CardSuit.HEARTS, CardValue.FIVE)
        val card2 = Card(CardSuit.SPADES, CardValue.TEN)
        assertTrue(gameService.checkCardChoice(card1, card2))

        val invalidCard1 = Card(CardSuit.CLUBS, CardValue.ACE)
        val invalidCard2 = Card(CardSuit.DIAMONDS, CardValue.ACE)
        assertFalse(gameService.checkCardChoice(invalidCard1, invalidCard2))
    }

    /**
     * Tests the end game conditions to verify if the game correctly concludes under defined circumstances.
     */
    @Test
    fun testEndGame() {
        gameService.endGame()
    }

    /**
     * Tests the logic for determining if a card has adjacent cards, both to the left and right, in the pyramid.
     */
    @Test
    fun testHasAdjacentCards() {
        assertFalse(gameService.hasAdjacentLinkCards(0, 0))
        assertFalse(gameService.hasAdjacentRechtsCards(0, 0))
    }

    /**
     * Tests the functionality of flipping adjacent cards.
     * This ensures that cards with no other adjacent cards are correctly flipped.
     */
    @Test
    fun testFlipAdjacentCards() {
        val game = rootService.currentGame!!


        // We remove a card from the second row, first column (index 1, 0)
        val row = 1
        val col = 0
        game.pyramid.cards[row][col] = null

        // Flip adjacent cards
        gameService.flipAdjacentCards(row, col)

        if (col - 1 >= 0 && gameService.hasAdjacentLinkCards(row, col - 1).not()) {
            assertTrue(game.pyramid.cards[row][col - 1]?.isRevealed ?: false)
        }
        if (col + 1 < game.pyramid.cards[row].size && gameService.hasAdjacentRechtsCards(row, col + 1).not()) {
            assertTrue(game.pyramid.cards[row][col + 1]?.isRevealed ?: false)
        }
    }

    /**
     * Tests the method for finding the position of a card in the pyramid or the reserve stack.
     */
    @Test
    fun testFindCardPosition() {
        val game = rootService.currentGame!!
        val card = game.pyramid.cards[0][0]!!
        val position = gameService.findCardPosition(card)
        assertEquals(Pair(0, 0), position)
    }

    /**
     * Tests the construction of the pyramid with the first 28 cards, ensuring correct layout and card placement.
     */
    @Test
    fun testCreatePyramid() {
        val first28Cards = gameService.defaultRandomCardList().take(28)
        val pyramid = gameService.createPyramid(first28Cards)

        assertEquals(7, pyramid.cards.size)

        for (row in pyramid.cards.indices) {
            assertEquals(row + 1, pyramid.cards[row].size)

            // Check the reveal status of the outer cards in each row
            assertTrue(pyramid.cards[row].first()?.isRevealed ?: false)
            assertTrue(pyramid.cards[row].last()?.isRevealed ?: false)

            // Check the hidden status of inner cards (if any)
            if (pyramid.cards[row].size > 2) {
                pyramid.cards[row].slice(1 until pyramid.cards[row].lastIndex).forEach {
                    assertFalse(it?.isRevealed ?: true)
                }
            }
        }
    }

}
