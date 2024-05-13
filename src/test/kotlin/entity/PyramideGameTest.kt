package entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Test class for PyramideGame.
 * This class contains unit tests to verify the correct initialization and behavior
 * of the PyramideGame class.
 */
class PyramideGameTest {

    private lateinit var game: PyramideGame
    private lateinit var player1: Player
    private lateinit var player2: Player
    private lateinit var pyramid: Pyramid
    private lateinit var drawStack: CardStack
    private lateinit var reserveStack: CardStack

    /**
     * Sets up the test environment before each test.
     * Initializes the players, pyramid, draw stack, reserve stack, and the game instance.
     */
    @BeforeEach
    fun setUp() {
        // Initialize players
        player1 = Player("Alice")
        player2 = Player("Bob")

        // Initialize Pyramid (mock setup)
        pyramid = Pyramid(Array(7) { row ->
            Array(row + 1) { Card(CardSuit.CLUBS, CardValue.ACE, false) }
        })

        // Initialize the draw and reserve stacks
        drawStack = CardStack()
        reserveStack = CardStack()

        // Initialize the game
        game = PyramideGame(player1, player2, 0, false, pyramid, drawStack, reserveStack)
    }

    /**
     * Tests if the players are correctly initialized in the game.
     */
    @Test
    fun testPlayerInitialization() {
        assertEquals(player1, game.player1, "Player 1 should be correctly initialized")
        assertEquals(player2, game.player2, "Player 2 should be correctly initialized")
    }

    /**
     * Tests if the current player is correctly initialized and can be updated.
     */
    @Test
    fun testCurrentPlayer() {
        assertEquals(0, game.currentPlayer, "Initial current player should be 0 (player1)")
        game.currentPlayer = 1
        assertEquals(1, game.currentPlayer, "Current player should be able to change to 1 (player2)")
    }

    /**
     * Tests the initial and updated states of the 'passed' property in the game.
     */
    @Test
    fun testPassedInitialization() {
        assertEquals(false, game.passed, "Initially, 'passed' should be false")

        game.passed = true
        assertEquals(true, game.passed, "'passed' should be able to change to true")
    }


    /**
     * Tests if the pyramid is correctly initialized in the game.
     */
    @Test
    fun testPyramidInitialization() {
        assertNotNull(game.pyramid, "Pyramid should be initialized")
        assertEquals(7, game.pyramid.cards.size, "Pyramid should have 7 rows")
    }

    /**
     * Tests if the draw stack is correctly initialized and initially empty.
     */
    @Test
    fun testDrawStackInitialization() {
        assertNotNull(game.drawStack, "Draw stack should be initialized")
        assertTrue(game.drawStack.empty, "Draw stack should be empty initially")
    }

    /**
     * Tests if the reserve stack is correctly initialized and initially empty.
     */
    @Test
    fun testReserveStackInitialization() {
        assertNotNull(game.reserveStack, "Reserve stack should be initialized")
        assertTrue(game.reserveStack.empty, "Reserve stack should be empty initially")
    }
}
