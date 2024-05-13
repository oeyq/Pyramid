package view

import service.RootService
import tools.aqua.bgw.core.MenuScene

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import entity.PyramideGame
import entity.Player
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * [MenuScene] that is displayed when the game is finished. It shows the final result of the game
 * as well as the score. Also, there are two buttons: one for starting a new game and one for
 * quitting the program.
 */
class GameEndedMenuScene(private val rootService: RootService) : MenuScene(400, 600), Refreshable {

    /**
     * Label for displaying the "Game Over" headline.
     */
    private val headlineLabel = Label(
        width = 300, height = 50, posX = 50, posY = 150,
        text = "Game Over",
        font = Font(size = 25, color = Color.BLACK, family = "Cooper Black")
    )

    private val p2Score = Label(width = 300, height = 35, posX = 50, posY = 250)

    private val p1Score = Label(width = 300, height = 35, posX = 50, posY = 290)

    /**
     * Label for displaying the game result.
     */
    private val gameResult = Label(width = 300, height = 35, posX = 50, posY = 330).apply {
    }

    /**
     * Button to quit the program.
     */
    val quitButton = Button(width = 140, height = 35, posX = 50, posY = 430, text = "Quit").apply {
        visual = ColorVisual(Color(125, 0, 0))
    }

    /**
     * Button to start a new game.
     */
    val newGameButton = Button(width = 140, height = 35, posX = 210, posY = 430, text = "New Game").apply {
        visual = ColorVisual(Color(116, 146, 102))
    }

    init {
        opacity = 0.5
        addComponents(headlineLabel, p1Score, p2Score, gameResult, newGameButton, quitButton)
    }

    /**
     * Extension function for the Player class to get a formatted string of the player's score.
     *
     * @return String representing the player's name and current score.
     */
    private fun Player.scoreString(): String = "${this.name} scored ${this.currentScore} points."

    /**
     * Determines the game result string based on the scores of both players in the PyramideGame.
     *
     * @return String indicating which player has won or if it's a draw.
     */
    private fun PyramideGame.gameResultString(): String {
        val p1Score = player1.currentScore
        val p2Score = player2.currentScore
        return when {
            p1Score - p2Score > 0 -> "${player1.name} has won!"
            p1Score - p2Score < 0 -> "${player2.name} has won!"
            else -> "it's a Draw."
        }
    }

    /**
     * Updates the scene with the final scores and game result when the game ends.
     */
    override fun refreshAfterEndGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }
        p1Score.text = game.player1.scoreString()
        p2Score.text = game.player2.scoreString()
        gameResult.text = game.gameResultString()
    }
}