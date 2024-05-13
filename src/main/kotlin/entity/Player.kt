package entity

/**
 * Represents a player in a game, capturing details such as their name and current score.
 *
 * @property name The name of the player.
 * @property currentScore The current score of the player, initialized to `0` by default.
 */

class Player(
    var name: String,
    var currentScore: Int = 0
) {

    var passed = false

    /**
     * Provides a string representation of the player, displaying their name followed by their current score.
     *@return A string in the format "PlayerName: Score - CurrentScore".
     */
    fun getScore(): Int {
        return currentScore
    }

    override fun toString(): String = "$name: Score - $currentScore"
}