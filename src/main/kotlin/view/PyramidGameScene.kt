package view

import entity.Card
import entity.CardStack
import entity.Player
import service.CardImageLoader
import service.RootService
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * `PyramidGameScene` class represents the visual and interactive aspects of a pyramid card game.
 * It extends `BoardGameScene` and implements `Refreshable`. This class is responsible for
 * managing and displaying the game's visual components such as card stacks, player labels, and
 * score displays. It handles user interactions, updates card movements, and dynamically reflects
 * changes in the game state.
 */
class PyramidGameScene(private val rootService: RootService) : BoardGameScene(1920, 1210), Refreshable {


    private val drawStack = LabeledStackView(posX = 200, posY = 510, "Draw Stack").apply {
        onMouseClicked = {

            for (cardView in selectedCards) {
                cardView.opacity = 1.0
            }
            selectedCards.clear()

            rootService.currentGame?.let { game ->
                if (game.currentPlayer == 1) {
                    rootService.playerActionService.revealCard(game.player1)
                } else {
                    rootService.playerActionService.revealCard(game.player2)
                }


            }
        }
    }

    private val reserveStack = LabeledStackView(posX = 1500, posY = 510, "Reserve Stack")

    /**
     * Initializes a stack view with card views based on the provided CardStack.
     *
     * @param stack The CardStack object representing a stack of cards.
     * @param stackView The LabeledStackView to be populated with card views.
     * @param cardImageLoader A CardImageLoader instance for loading card images.
     */
    private fun initializeStackView(stack: CardStack, stackView: LabeledStackView, cardImageLoader: CardImageLoader) {
        stackView.clear()
        stack.peekAll().reversed().forEach { card ->
            val cardView = CardView(
                height = 170,
                width = 110,
                front = ImageVisual(cardImageLoader.frontImageFor(card.suit, card.value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            stackView.add(cardView)
            cardMap.add(card to cardView)

        }
    }

    /**
     * Moves a CardView to a different LabeledStackView and optionally flips the card.
     *
     * @param cardView The CardView to be moved.
     * @param toStack The destination LabeledStackView.
     * @param flip A boolean indicating whether to flip the card during the move.
     */
    private fun moveCardView(cardView: CardView, toStack: LabeledStackView, flip: Boolean = false) {
        if (flip) {
            when (cardView.currentSide) {
                CardView.CardSide.BACK -> cardView.showFront()
                CardView.CardSide.FRONT -> cardView.showBack()
            }
        }
        cardView.removeFromParent()
        toStack.add(cardView)
    }


    private val labelWidth = 450
    private val labelHeight = 50
    private val sideMargin = 110 // Adjust this margin to move labels closer to or further from the sides

    // Player name labels positioned at the sides
    private val player1NameLabel = Label(
        posX = sideMargin, // X position from the left edge
        posY = 50,
        width = labelWidth,
        height = labelHeight,
        text = "Player 1"
    )
    private val player2NameLabel = Label(
        posX = 1920 - labelWidth - sideMargin, // X position from the right edge
        posY = 50,
        width = labelWidth,
        height = labelHeight,
        text = "Player 2"
    )

    private val passButton = Button(
        width = 200, height = 60,
        posX = 1625, posY = 1085,
        text = "PASS",
        font = Font(size = 20, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("buttonBG.png")
    ).apply {
        onMouseClicked = {
            rootService.playerActionService.pass()
        }
    }

    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    private val selectedCards: MutableList<CardView> = mutableListOf()


    private val player1ScoreLabel = Label(
        posX = 235, // Adjust as per your layout
        posY = 120, // Adjust as per your layout
        width = 200,
        height = 50,
        text = "0", // Initial score
        font = Font(size = 50, color = Color.BLACK)
    )

    private val player2ScoreLabel = Label(
        posX = 1490, // Adjust as per your layout
        posY = 120, // Adjust as per your layout
        width = 200,
        height = 50,
        text = "0", // Initial score
        font = Font(size = 50, color = Color.BLACK)
    )

    /**
     * Updates the score labels based on the current game state.
     */
    override fun refreshScores() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        // Update score labels
        player1ScoreLabel.text = "${game.player1.currentScore}"
        player2ScoreLabel.text = "${game.player2.currentScore}"
    }


    init {

        background = ColorVisual(57, 70, 59)

        player1NameLabel.apply {
            font = Font(size = 67, color = Color.BLACK, family = "Cooper Black")
            // More styling options can be set here
        }
        player2NameLabel.apply {
            font = Font(size = 67, color = Color.BLACK, family = "Cooper Black")
        }
        // Add the player name labels to the scene
        addComponents(
            drawStack, reserveStack,
            player1NameLabel, player2NameLabel,
            player1ScoreLabel, player2ScoreLabel,
            passButton,
        )
    }

    /**
     * Initializes the pyramid layout with card views based on the provided 2D array of Cards.
     *
     * @param pyramidCards A 2D array of Card objects representing the pyramid structure.
     */
    private fun initializePyramid(pyramidCards: Array<Array<Card?>>) {
        // Clear previous pyramid by removing all CardViews from their parent.
        cardMap.getCoDomain().forEach { cardView -> cardView.removeFromParent() }
        cardMap.clear()

        // Constants for card size and spacing
        val cardWidth = 100
        val cardHeight = 150
        val cardSpacingHorizontal = 18
        val cardSpacingVertical = 18
        val numRows = pyramidCards.size  // Total number of rows in the pyramid

        // Adjust this value to move the pyramid down
        val verticalAdjustment = 70  // Increase this number to move the pyramid further down

        // Calculate the starting X and Y positions for the first row, centered in the scene
        val totalPyramidWidth = (numRows - 1) * (cardWidth + cardSpacingHorizontal)
        val startPosX = (1920 - totalPyramidWidth) / 2.3
        val startPosY = ((1080 - numRows * (cardHeight + cardSpacingVertical)) / 2.0) + verticalAdjustment

        // Iterate over each row and card in the pyramid
        for (rowIndex in 0 until numRows) {
            val numCardsInRow = pyramidCards[rowIndex].size
            val rowWidth = (numCardsInRow - 1) * (cardWidth + cardSpacingHorizontal)
            val rowStartX = startPosX + (totalPyramidWidth - rowWidth) / 2
            val rowY = startPosY + rowIndex * (cardHeight + cardSpacingVertical)

            for (cardIndex in 0 until numCardsInRow) {
                pyramidCards[rowIndex][cardIndex]?.let { card ->
                    // Determine if the card is an edge card, except for the last row
                    val isEdgeCard = rowIndex == 0 ||
                            cardIndex == 0 ||
                            cardIndex == numCardsInRow - 1 ||
                            (rowIndex < numRows - 1 && (cardIndex == 0 || cardIndex == numCardsInRow - 1))

                    // For the last row, only the first and last cards are edge cards
                    val isLastRowEdgeCard =
                        rowIndex == numRows - 1 && (cardIndex == 0 || cardIndex == numCardsInRow - 1)
                    val cardView = initializeCardView(card, isEdgeCard || isLastRowEdgeCard)

                    // Position this CardView
                    cardView.posX = rowStartX + cardIndex * (cardWidth + cardSpacingHorizontal)
                    cardView.posY = rowY

                    // Add CardView to the scene
                    addComponents(cardView)  // Assuming 'addComponent' is the correct method

                    // Add to cardMap for future reference
                    cardMap.add(card to cardView)
                }
            }
        }
    }

    /**
     * Creates a CardView for a given Card and sets its visibility based on its position (edge or not).
     *
     * @param card The Card to create a view for.
     * @param isEdgeCard A boolean indicating whether the card is an edge card.
     * @return CardView The created CardView for the specified card.
     */
    private fun initializeCardView(card: Card, isEdgeCard: Boolean): CardView {
        val cardImageLoader = CardImageLoader()
        val cardView = CardView(
            width = 110,
            height = 160,
            front = ImageVisual(cardImageLoader.frontImageFor(card.suit, card.value)),
            back = ImageVisual(cardImageLoader.backImage)
        )
        cardView.onMouseClicked = {
            println("Card clicked: $card")
            // Check if the card is clickable (either an edge card or the top card in the reserve pile)
            if (rootService.currentGame?.reserveStack?.empty == false) {
                if (isEdgeCard || card == rootService.currentGame?.reserveStack?.peek()) {
                    handleCardSelection(cardView)
                }
            } else {
                if (isEdgeCard) {
                    handleCardSelection(cardView)
                }
            }
        }
        // Show the front for edge cards, back for non-edge cards
        if (isEdgeCard) {
            cardView.showFront()
        } else {
            cardView.showBack()
        }
        // Event handling for when a card is clicked
        return cardView
    }

    /**
     * Handles the selection and deselection of cards.
     *
     * @param cardView The CardView that was selected or deselected.
     */
    private fun handleCardSelection(cardView: CardView) {
        // Toggle selection
        if (selectedCards.contains(cardView)) {
            selectedCards.remove(cardView)
            // Optionally, visually deselect the card
            cardView.opacity = 1.0
        } else {
            selectedCards.add(cardView)
            cardView.opacity = 0.5
        }

        // Check if two cards are selected
        if (selectedCards.size == 2) {
            val card1 = cardMap.backward(selectedCards[0])
            val card2 = cardMap.backward(selectedCards[1])

            // Call PlayerActionService to handle pair removal
            rootService.playerActionService.removePair(card1, card2)
        }
    }

    /**
     * Highlights the label of the current player to indicate whose turn it is.
     */
    private fun highlightCurrentPlayer() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        // Reset styles for both player labels to default
        player1NameLabel.font = Font(size = 67, color = Color.BLACK, family = "Cooper Black")
        player2NameLabel.font = Font(size = 67, color = Color.BLACK, family = "Cooper Black")

        // Highlight the label of the current player
        if (game.currentPlayer == 1) {
            player1NameLabel.font = Font(size = 67, color = Color(61, 12, 17), family = "Cooper Black")
        } else {
            player2NameLabel.font = Font(size = 67, color = Color(61, 12, 17), family = "Cooper Black")
        }
    }

    /**
     * Refreshes the pyramid view based on the current game state, revealing cards as needed.
     */
    private fun refreshPyramidView() {
        val game = rootService.currentGame ?: return

        game.pyramid.cards.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, card ->
                if (card != null && card.isRevealed) {
                    cardMap.forward(card).showFront()
                    cardMap.forward(card).onMouseClicked = {

                        handleCardSelection(cardMap.forward(card))

                    }
                }
            }
        }
        highlightCurrentPlayer()
    }

    /**
     * Prepares and refreshes the game scene when a new game starts.
     */
    override fun refreshAfterStartGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        clearComponents()
        addComponents(
            drawStack, reserveStack,
            player1NameLabel, player2NameLabel,
            player1ScoreLabel, player2ScoreLabel,
            passButton,
        )
        player1NameLabel.text = game.player1.name
        player2NameLabel.text = game.player2.name
        player1ScoreLabel.text = "${game.player1.currentScore}"
        player2ScoreLabel.text = "${game.player2.currentScore}"
        selectedCards.clear()
        cardMap.clear()
        val cardImageLoader = CardImageLoader()
        initializePyramid(game.pyramid.cards)
        initializeStackView(game.reserveStack, reserveStack, cardImageLoader)
        initializeStackView(game.drawStack, drawStack, cardImageLoader)
        highlightCurrentPlayer()
    }

    /**
     * Updates the game view after a pair of cards is removed, handling invalid pairs as well.
     *
     * @param isValid A boolean indicating whether the removed pair was valid.
     */
    override fun refreshAfterRemovePair(isValid: Boolean) {
        val game = rootService.currentGame
        checkNotNull(game)
        if (!isValid) {
            for (cardView in selectedCards) {
                cardView.opacity = 1.0
            }
            selectedCards.clear()
        } else {

            selectedCards.forEach { cardView ->
                if (reserveStack.isNotEmpty() && cardView == reserveStack.peek()) {
                    reserveStack.pop()
                } else removeComponents(cardView)
            }

            selectedCards.clear()
        }

        refreshPyramidView()
        highlightCurrentPlayer()

        //-----------
        println(game.pyramid.cards.flatten())
        //-----------

        rootService.gameService.endGame()
    }

    /**
     * Updates the game view after a player passes their turn.
     */
    override fun refreshAfterPass() {
        highlightCurrentPlayer()
    }

    /**
     * Updates the game view after a card is revealed by a player.
     *
     * @param player The Player who revealed
     */
    override fun refreshAfterRevealCard(player: Player) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        when (player) {
            game.player1 -> moveCardView(cardMap.forward(game.reserveStack.cards.last()), reserveStack, true)
            game.player2 -> moveCardView(cardMap.forward(game.reserveStack.cards.last()), reserveStack, true)
        }

        if (reserveStack.isNotEmpty()) {

            reserveStack.peek().apply {
                onMouseClicked = {
                    handleCardSelection(reserveStack.peek())
                }

            }
        }


        if (game.drawStack.cards.isEmpty()) {
            drawStack.clear()
            drawStack.onMouseClicked = null
        }

        highlightCurrentPlayer()
    }

}