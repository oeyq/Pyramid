package entity
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Handles test class for CardSuit
 */
class CardSuitTest {
    /**
     * Test to verify if the CardSuit enumeration values are correctly
     * converted to their respective string representations.
     */
    @Test
    fun testCardSuitToString() {
        assertEquals("♣", CardSuit.CLUBS.toString())
        assertEquals("♠", CardSuit.SPADES.toString())
        assertEquals("♥", CardSuit.HEARTS.toString())
        assertEquals("♦", CardSuit.DIAMONDS.toString())
    }

}