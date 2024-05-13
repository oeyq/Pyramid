package entity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * A test class for the [Player] class.
 */
class PlayerTest {

    /**
     * Tests whether the name and score of a player are initialized correctly.
     */
    @Test
    fun testNameAndScoreInitialization() {
        val player = Player("John", 100)
        assertEquals("John", player.name)
        assertEquals(100, player.currentScore)
    }

    /**
     * Tests whether the default score for a player is set to zero when not explicitly provided.
     */
    @Test
    fun testDefaultScoreInitialization() {
        val player = Player("Jane")
        assertEquals(0, player.currentScore)
    }

    /**
     * Tests the string representation of the [Player] class to ensure it's formatted as expected.
     */
    @Test
    fun testToStringRepresentation() {
        val player = Player("John", 100)
        assertEquals("John: Score - 100", player.toString())
    }
}
