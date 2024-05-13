package entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * A test class for the [Pyramid] class.
 */
class PyramidTest {

    private lateinit var pyramid: Pyramid

    /**
     * Sets up the test environment before each test.
     * Initializes a Pyramid instance with a full set of Cards.
     * The cards are arranged in a simple, non-randomized order for testing.
     */
    @BeforeEach
    fun setUp() {
        pyramid = Pyramid(Array(7) { row ->
            Array(row + 1) { col ->
                Card(CardSuit.values()[col % 4], CardValue.values()[col % 13], col % 2 == 0)
            }
        })
    }

    /**
     * Tests the initial configuration of the Pyramid.
     * Verifies that the cards are not null, and the number of cards
     * in each row matches the expected layout.
     */
    @Test
    fun testInitialConfiguration() {
        assertNotNull(pyramid.cards, "Pyramid cards should not be null")
        assertEquals(7, pyramid.cards.size, "Pyramid should have 7 rows")

        for (i in pyramid.cards.indices) {
            assertEquals(i + 1, pyramid.cards[i].size, "Row ${i + 1} should have ${i + 1} cards")
        }
    }

    /**
     * Tests the functionality of removing a card from the Pyramid.
     * Simulates the removal of a specific card and verifies that
     * the card's position in the pyramid is null after removal.
     */
    @Test
    fun testCardRemoval() {
        val removedCard = pyramid.cards[3][2]  // Example: Remove a card from row 4, column 3
        pyramid.cards[3][2] = null

        assertNotNull(removedCard, "Removed card should not be null before removal")
        assertNull(pyramid.cards[3][2], "Removed card position should be null after removal")
    }

    /**
     * Tests the visibility of cards in the Pyramid.
     * Verifies that the cards are revealed or hidden as expected based on their initial state.
     */
    @Test
    fun testCardVisibility() {
        assertTrue(pyramid.cards[0][0]?.isRevealed ?: false, "First card should be revealed")
        assertFalse(pyramid.cards[1][1]?.isRevealed ?: true, "Second card in second row should not be revealed")
    }

}
