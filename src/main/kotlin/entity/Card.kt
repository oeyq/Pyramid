package entity

/**
 * Data class for the single typ of game elements that the game "Pyramid" knows: cards.
 *
 *  It is characterized by a [CardSuit] and a [CardValue]
 *  @property isRevealed A nullable Boolean indicating the visibility state of the card.
 *  `true` implies the card is revealed, `false` implies it is hidden,
 *  and `null` might imply an undetermined state.
 */
data class Card(
    val suit: CardSuit, val value: CardValue,
    var isRevealed: Boolean = false
) {

    /**
     * Returns a string representation of the card,
     * consisting of its suit followed by its value.
     * @return A string in the format "SuitValue".
     */
    override fun toString() = "$suit$value"

    /**
     * compares two [Card]s according to the [Enum.ordinal] value of their [CardSuit]
     * (i.e., the order in which the suits are declared in the enum class)
     */
    operator fun compareTo(other: Card) = this.value.ordinal - other.value.ordinal


}