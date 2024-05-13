package service

import entity.*

/**
 * Handles player actions in a pyramid card game, including passing turns, revealing cards, and removing card pairs.
 * @property rootService Provides access to the overall game state.
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    private val pyramid: Pyramid = rootService.gameService.pyramid
    private val drawStack: CardStack = rootService.gameService.drawStack

    /**
     * Allows a player to pass their turn. If both players have passed, the game ends.
     *
     * This function changes the current player and checks if both players have passed their turns.
     * If so, it triggers the end of the game.
     */
    fun pass() {
        val game = rootService.currentGame ?: return

        val activePlayer = if (game.currentPlayer == 1) game.player1 else game.player2
        activePlayer.passed = true

        if (game.player1.passed && game.player2.passed) {
            game.passed = true
            rootService.gameService.endGame()
        }

        // Change player and reset the passed state of the other player
        //val nextPlayer = if (game.currentPlayer == 1) game.player2 else game.player1
        //nextPlayer.passed = false
        rootService.gameService.changePlayer()
        onAllRefreshables { refreshAfterPass() }
    }


    /**
     * Reveals the top card from the draw stack and adds it to the reserve stack.
     *
     * @param player The player performing the action.
     * This function takes the last card from the draw stack, reveals it, and moves it to the reserve stack.
     */

    fun revealCard(player: Player) {
        val game = rootService.currentGame ?: return
        if (game.drawStack.empty) {
            println("Draw stack is empty.")
            return
        }
        val cardToMove = game.drawStack.cards.removeLastOrNull()
        cardToMove?.let {
            it.isRevealed = true
            game.reserveStack.cards.add(it)
        }

        player.passed = false
        rootService.gameService.changePlayer()
        onAllRefreshables { refreshAfterRevealCard(player) }
    }

    /**
     * Removes a pair of matching cards from the game.
     *
     * @param card1 The first card in the pair to be removed.
     * @param card2 The second card in the pair to be removed.
     *
     * This function checks if the selected pair of cards is valid according to the game rules.
     * If valid, it removes the cards from their respective positions (pyramid or reserve stack)
     * and flips any adjacent cards if necessary.
     */

    fun removePair(card1: Card, card2: Card) {

        val gameService = rootService.gameService
        val game = rootService.currentGame ?: return

        // Check if the pair is valid
        if (!gameService.checkCardChoice(card1, card2)) {
            println("Invalid card choice.")
            onAllRefreshables { refreshAfterRemovePair(isValid = false) }
            return
        }

        // Remove the cards from the pyramid or reserve stack
        val card1Position = gameService.findCardPosition(card1)
        val card2Position = gameService.findCardPosition(card2)



        if (card1Position != null) {
            removeCardFromGame(card1Position)
        }
        if (card2Position != null) {
            removeCardFromGame(card2Position)
        }
        if (card1Position != null && card1Position != Pair(-1, -1)) {
            gameService.flipAdjacentCards(card1Position.first, card1Position.second)
        }
        if (card2Position != null && card2Position != Pair(-1, -1)) {
            gameService.flipAdjacentCards(card2Position.first, card2Position.second)
        }
        // Update the player's score
        updatePlayerScore(game, card1, card2)
        // Change player turn and refresh UI
        gameService.changePlayer()
        onAllRefreshables { refreshAfterRemovePair(isValid = true) }
    }

    private fun updatePlayerScore(game: PyramideGame, card1: Card, card2: Card) {
        val currentPlayer = if (game.currentPlayer == 1) game.player1 else game.player2

        println("Current player before score update: ${currentPlayer.name}, Score: ${currentPlayer.currentScore}")

        val scoreIncrement = when {
            card1.value == CardValue.ACE || card2.value == CardValue.ACE -> 1
            else -> 2
        }

        currentPlayer.currentScore += scoreIncrement

        println("Score updated. New Score: ${currentPlayer.currentScore}")
        onAllRefreshables { refreshScores() }
    }


    private fun removeCardFromGame(position: Pair<Int, Int>) {
        val gameService = rootService.gameService
        val game = rootService.currentGame ?: return

        if (position.first >= 0 && position.second >= 0) {
            game.pyramid.cards[position.first][position.second] = null
            gameService.flipAdjacentCards(position.first, position.second)
        } else if (position == Pair(-1, -1)) {
            game.reserveStack.cards.removeLastOrNull()
        }
    }
}