package service

import entity.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Handles test class for Player action service in a pyramid card game,
 * including passing turns, revealing cards, and removing card pairs.
 */

class PlayerActionServiceTest {

    private lateinit var rootService: RootService
    private lateinit var playerActionService: PlayerActionService
    private lateinit var pyramidGame: PyramideGame
    private lateinit var player1: Player
    private lateinit var player2: Player

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    fun setUp() {
        rootService = RootService()
        playerActionService = PlayerActionService(rootService)
        player1 = Player("Alice")
        player2 = Player("Bob")
        pyramidGame = PyramideGame(player1, player2, 1, false, Pyramid(arrayOf()), CardStack(), CardStack())
        rootService.currentGame = pyramidGame
    }

    /**
     * Test the pass functionality of the PlayerActionService.
     * Ensures that when a player passes, their pass state is set to true.
     */
    @Test
    fun testPass() {
        println("Starting testPass")
        pyramidGame.currentPlayer = 1
        playerActionService.pass()
        assertTrue(player1.passed)
        assertFalse(player2.passed)
        println("testPass Completed")
    }

    /**
     * Test the revealCard functionality of the PlayerActionService.
     * Verifies that a card is correctly moved from the draw stack to the reserve stack.
     */
    @Test
    fun testRevealCard() {
        println("Starting testRevealCard")
        val card = Card(CardSuit.HEARTS, CardValue.ACE)
        pyramidGame.drawStack.cards.add(card)
        playerActionService.revealCard(player1)
        assertEquals(0, pyramidGame.drawStack.cards.size)
        assertEquals(1, pyramidGame.reserveStack.cards.size)
        assertTrue(pyramidGame.reserveStack.cards.contains(card))
        println("testRevealCard Completed")
    }

}
