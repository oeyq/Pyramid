package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

/**
 * `NewGameMenuScene` class represents the menu scene for setting up a new game.
 * It extends `MenuScene` and implements `Refreshable`. This class is responsible for
 * managing and displaying UI components such as player input fields and control buttons.
 * It allows players to enter their names and start or quit the game.
 *
 * @property rootService An instance of RootService to interact with game logic.
 */
class NewGameMenuScene(private val rootService: RootService) :
    MenuScene(width = 950, height = 600, background = ImageVisual("pyramid.png")), Refreshable {

    /**
     * TextField for player 1's name input. Randomly selects a default name from a predefined list.
     * Disables the start button if either player's name field is blank.
     */
    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p1Input: TextField = TextField(
        width = 139,
        height = 26,
        posX = 273,
        posY = 100,
        text = listOf("Gloria", "Phil", "Cam", "Claire", "Jay", "Mitchell").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = this.text.isBlank() || p2Input.text.isBlank()
        }
    }

    /**
     * TextField for player 2's name input. Randomly selects a default name from a predefined list.
     * Disables the start button if either player's name field is blank.
     */
    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p2Input: TextField = TextField(
        width = 139,
        height = 26,
        posX = 548,
        posY = 100,
        text = listOf("Phoebe", "Rachel", "Chandler", "Joey", "Monica", "Ross").random()
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    /**
     * Button to start the game. Initiates the game with the entered player names.
     * Is disabled if either player's name field is blank.
     */
    private val startButton = Button(
        width = 100,
        height = 50,
        posX = 800,
        posY = 490,
        text = "Start"
    ).apply {
        visual = ColorVisual(116, 146, 102)
        onMouseClicked = {
            rootService.gameService.startGame(
                p1Input.text.trim(),
                p2Input.text.trim()
            )
        }
    }

    /**
     * Button to quit the game. It is always enabled and can be used to exit the game or return to a previous menu.
     */
    val quitButton = Button(
        width = 100,
        height = 50,
        posX = 60,
        posY = 490,
        text = "Quit"
    ).apply {
        visual = ColorVisual(125, 0, 0)
    }

    init {
        opacity = 1.0
        addComponents(
            p1Input,
            p2Input,
            startButton,
            quitButton
        )
    }
}