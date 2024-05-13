package view

import service.AbstractRefreshingService
import entity.Player

/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the view classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * UI classes only need to react to events relevant to them.
 *
 * @see AbstractRefreshingService
 *
 */
interface Refreshable {

    /**
     * perform refreshes that are necessary after a new game started
     */
    fun refreshAfterStartGame() {}

    /**
     * perform refreshes that are necessary after the players passes
     */
    fun refreshAfterPass() {}

    /**
     * perform refreshes that are necessary after the pairs are removed
     */
    fun refreshAfterRemovePair(isValid: Boolean) {}

    /**
     * perform refreshes that are necessary after cards are revealed
     */
    fun refreshAfterRevealCard(player: Player) {}

    /**
     * perform refreshes that are necessary after the player has been changed
     */
    fun refreshAfterChangePlayer() {}

    /**
     * perform refreshes that are necessary after the scores
     */
    fun refreshScores() {}

    /**
     * perform refreshes that are necessary after the last round was played
     */
    fun refreshAfterEndGame() {}

}