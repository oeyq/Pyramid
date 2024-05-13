package entity

/**
 * Represents the state of a Pyramide game or round, detailing the players involved and
 * the current game dynamics.
 *
 * @property player1 The first player in the Pyramide game.
 * @property player2 The second player in the Pyramide game.
 * @property passed A Boolean indicating if the opponent has passed their turn.
 * Defaults to `false`.
 * @property currentPlayer An integer indicating the current player's turn.
 */
class PyramideGame
    (
    val player1: Player,
    val player2: Player,
    var currentPlayer: Int,
    var passed: Boolean = false,
    var pyramid: Pyramid,
    var drawStack: CardStack,
    var reserveStack: CardStack
)
