package service

import entity.Player
import org.junit.jupiter.api.Test
import view.Refreshable
import kotlin.test.assertTrue

/**
 * Test implementation of the Refreshable interface for testing different game state updates.
 */
class RefreshableTest: Refreshable {

    private var refreshAfterStartGameCalled: Boolean = false
    private var refreshAfterPassCalled: Boolean = false
    private var refreshAfterRemovePairCalled: Boolean = false
    private var refreshAfterRevealCardCalled: Boolean = false
    private var refreshAfterChangePlayerCalled: Boolean = false
    private var refreshAfterEndGameCalled: Boolean = false

    /**
     * Called when a new game starts.
     * Sets the flag for game start refresh to true.
     */
    @Test
    override fun refreshAfterStartGame() {
        refreshAfterStartGameCalled = true
    }

    /**
     * Called when a player passes their turn.
     * Sets the flag for pass refresh to true.
     */
    @Test
    override fun refreshAfterPass() {
        refreshAfterPassCalled = true
    }


    /**
     * Called when the player's turn changes.
     * Sets the flag for change player refresh to true.
     */
    @Test
    override fun refreshAfterChangePlayer() {
        refreshAfterChangePlayerCalled = true
    }

    /**
     * Called when the game ends.
     * Sets the flag for end game refresh to true.
     */
    @Test
    override fun refreshAfterEndGame() {
        refreshAfterEndGameCalled = true
    }
    /**
     * Resets all flags to false.
     * Useful for reusing the same TestRefreshable across multiple tests.
     */
    @Test
    fun reset() {
        refreshAfterStartGameCalled = false
        refreshAfterPassCalled = false
        refreshAfterRemovePairCalled = false
        refreshAfterRevealCardCalled = false
        refreshAfterEndGameCalled = false
    }
}