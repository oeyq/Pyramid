package entity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Handles test class for CardValue
 */
class CardValueTest {

    /**
     * Test to verify if the CardValue enumeration values are correctly
     * converted to their respective string representations.
     */
    @Test
    fun testCardValueToString() {
        assertEquals("2", CardValue.TWO.toString())
        assertEquals("3", CardValue.THREE.toString())
        assertEquals("4", CardValue.FOUR.toString())
        assertEquals("5", CardValue.FIVE.toString())
        assertEquals("6", CardValue.SIX.toString())
        assertEquals("7", CardValue.SEVEN.toString())
        assertEquals("8", CardValue.EIGHT.toString())
        assertEquals("9", CardValue.NINE.toString())
        assertEquals("10", CardValue.TEN.toString())
        assertEquals("J", CardValue.JACK.toString())
        assertEquals("Q", CardValue.QUEEN.toString())
        assertEquals("K", CardValue.KING.toString())
        assertEquals("A", CardValue.ACE.toString())
    }

    /**
     * Test to verify if the `shortDeck` function of the CardValue
     * enumeration correctly returns a list of values representing
     * a short deck of cards.
     */
    @Test
    fun testShortDeck() {
        val shortDeckValues = CardValue.shortDeck()
        assertTrue(shortDeckValues.containsAll(listOf(
            CardValue.ACE, CardValue.SEVEN, CardValue.EIGHT, CardValue.NINE,
            CardValue.TEN, CardValue.JACK, CardValue.QUEEN, CardValue.KING)))
    }
}