package entity


/**
 * Represents a stack of cards.
 *
 * This class provides methods to manipulate and query the card stack,
 * such as adding a card to the stack, drawing a card from the stack,
 * and getting the size of the stack.
 */
class CardStack {
    /** A private mutable list that holds the cards in the stack. */
    var cards: MutableList<Card> = mutableListOf()

    /**
     * the amount of cards in this stack
     */
    val size: Int get() = cards.size

    /**
     * Returns `true` if the stack is empty, `false` otherwise.
     */
    val empty: Boolean get() = cards.isEmpty()


    /**
     * Draws [amount] cards from this stack.
     *
     * @param amount the number of cards to draw; defaults to 1 if omitted.
     *
     * @throws IllegalArgumentException if not enough cards on stack to draw the desired amount.
     */
    fun draw(amount: Int = 1): List<Card> {
        require(amount in 1..cards.size) { "can't draw $amount cards from $cards" }
        return List(amount) { cards.removeFirst() }
    }

    /**
     * Draws all cards from this stack. Convenience wrapper for [draw] with
     * amount parameter equal to [size]
     */
    fun drawAll(): List<Card> = draw(size)

    /**
     * returns the top card from the stack *without removing* it from the stack.
     * Use [draw] if you want the card also to be removed.
     */
    fun peek(): Card = cards.first()

    /**
     * provides a view of the full stack contents without changing it. Use [draw]
     * for actually drawing cards from this stack.
     */
    fun peekAll(): List<Card> = cards.toList()


    override fun toString(): String = cards.toString()
}
