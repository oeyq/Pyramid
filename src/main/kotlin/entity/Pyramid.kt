package entity

/**
 * Represents the arrangement of cards on the table in a pyramid-like structure.
 * Typically used in card games where cards are set up in a layered pyramid layout.
 *
 * @property cards A 2D array capturing the rows and positions of cards in the pyramid.
 *                Each inner array represents a row, with the cards in that row.
 */
class Pyramid(val cards: Array<Array<Card?>>)