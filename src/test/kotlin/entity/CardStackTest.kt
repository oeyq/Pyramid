package entity

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Handles test class for CardStack
 */
class CardStackTest {

    private lateinit var cardStack: CardStack
    private lateinit var testCard: Card

    /**
     * Sets up the test environment before each test.
     * Initializes a new CardStack and a test card.
     */
    @BeforeEach
    fun setUp() {
        cardStack = CardStack()
        testCard = Card(CardSuit.HEARTS, CardValue.ACE)
    }

    /**
     * Tests the addition of a single card to the card stack.
     * Ensures the card stack is not empty and has the correct size after addition.
     */
    @Test
    fun testAddCard() {
        cardStack.cards.add(testCard)
        assertFalse(cardStack.empty)
        assertEquals(1, cardStack.size)
    }

    /**
     * Tests drawing a single card from the card stack.
     * Verifies the correct card is drawn and the stack is updated accordingly.
     */
    @Test
    fun testDrawCard() {
        cardStack.cards.add(testCard)
        val drawnCard = cardStack.draw().first()
        assertEquals(testCard, drawnCard)
        assertTrue(cardStack.empty)
    }

    /**
     * Tests drawing multiple cards from the card stack.
     * Checks if the correct number of cards are drawn and the stack is updated.
     */
    @Test
    fun testDrawMultipleCards() {
        val testCard2 = Card(CardSuit.SPADES, CardValue.KING)
        cardStack.cards.addAll(listOf(testCard, testCard2))
        val drawnCards = cardStack.draw(2)
        assertEquals(2, drawnCards.size)
        assertTrue(drawnCards.containsAll(listOf(testCard, testCard2)))
        assertTrue(cardStack.empty)
    }

    /**
     * Tests the behavior of drawing cards with an invalid amount.
     * An IllegalArgumentException is expected when the amount exceeds the stack size.
     */
    @Test
    fun testDrawWithInvalidAmount() {
        assertThrows(IllegalArgumentException::class.java) {
            cardStack.draw(1)
        }
    }

    /**
     * Tests peeking at the top card of the stack without removing it.
     * Ensures the correct card is returned and the stack remains unchanged.
     */
    @Test
    fun testPeekCard() {
        cardStack.cards.add(testCard)
        val peekedCard = cardStack.peek()
        assertEquals(testCard, peekedCard)
        assertFalse(cardStack.empty)
    }

    /**
     * Tests if the empty property of the stack is correctly identified.
     * Ensures the stack is identified as empty when it has no cards.
     */
    @Test
    fun testEmptyStack() {
        assertTrue(cardStack.empty)
    }

    /**
     * Tests the size property of the card stack.
     * Ensures that the size is correctly reported as the number of cards in the stack.
     */
    @Test
    fun testSize() {
        assertEquals(0, cardStack.size)
        cardStack.cards.add(testCard)
        assertEquals(1, cardStack.size)
    }

    /**
     * Tests the functionality of peeking at all cards in the stack without removing them.
     * Verifies that all cards are returned and the stack remains unchanged.
     */
    @Test
    fun testPeekAll() {
        val testCard2 = Card(CardSuit.SPADES, CardValue.KING)
        cardStack.cards.addAll(listOf(testCard, testCard2))
        val allCards = cardStack.peekAll()
        assertEquals(2, allCards.size)
        assertTrue(allCards.containsAll(listOf(testCard, testCard2)))
    }
}

