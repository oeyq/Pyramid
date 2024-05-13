package view


import tools.aqua.bgw.core.BoardGameApplication
import service.RootService

/**
 * Implementation of the BGW [BoardGameApplication] for the example card game "Pyramid"
 */
class PyramidApplication : BoardGameApplication("Pyramid Game"), Refreshable {

    private val rootService = RootService()
    private val gameScene = PyramidGameScene(rootService)

    private val newGameMenuScene = NewGameMenuScene(rootService).apply {
        quitButton.onMouseClicked = {
            exit()
        }
    }

    private val gameEndedMenuScene = GameEndedMenuScene(rootService).apply {
        quitButton.onMouseClicked = {
            exit()
        }
        newGameButton.onMouseClicked = { showMenuScene(newGameMenuScene, 0) }
    }

    init {
        rootService.addRefreshables(
            this,
            gameScene,
            newGameMenuScene,
            gameEndedMenuScene,
        )

        this.showGameScene(gameScene)
        this.showMenuScene(newGameMenuScene, 0)

    }

    override fun refreshAfterStartGame() {
        this.hideMenuScene()
    }

    override fun refreshAfterEndGame() {
        this.showMenuScene(gameEndedMenuScene)
    }

}

