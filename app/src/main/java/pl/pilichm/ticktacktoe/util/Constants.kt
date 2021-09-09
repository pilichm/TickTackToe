package pl.pilichm.ticktacktoe.util

class Constants {
    companion object {
        const val NO_PLAYER_ID = 0
        const val FIRST_PLAYER_ID = 1
        const val SECOND_PLAYER_ID = 2
        const val PREFIX_TOP_IMAGE = "top"
        const val PREFIX_BOTTOM_IMAGE = "bottom"
        const val PROPERTY_NUMBER_OF_PLAYERS = "number_of_players"
        const val DIFFICULTY_EASY = "Easy"
        const val DIFFICULTY_MEDIUM = "Medium"
        const val DIFFICULTY_HARD = "Hard"

        /**
         * Const values for storing settings in shared preferences.
         * */
        const val SHARED_PREFERENCES_NAME = "tic_tac_settings"
        const val SP_KEY_DIFFICULTY_LEVEL = "difficulty_level"
        const val SP_KEY_FIRST_PLAYER_SIGN = "first_player_sign"
        const val SP_KEY_SECOND_PLAYER_SIGN = "second_player_sign"
        const val FIRST_SIGN = 0
        const val SECOND_SIGN = 1

        val winningLocations = arrayOf(
            arrayOf(0, 1, 2),
            arrayOf(3, 4, 5),
            arrayOf(6, 7, 8),
            arrayOf(0, 3, 6),
            arrayOf(1, 4, 7),
            arrayOf(2, 5, 8),
            arrayOf(0, 4, 8),
            arrayOf(2, 4, 6)
        )

        /**
         * Two first positions must be set to the same player, third must be empty.
         * */
        val winningINNextMovePositions = arrayOf(
            // Rows.
            arrayOf(1, 2, 3),
            arrayOf(3, 4, 5),
            arrayOf(6, 7, 8),
            arrayOf(0, 2, 1),
            arrayOf(3, 5, 4),
            arrayOf(6, 8, 7),
            arrayOf(1, 2, 0),
            arrayOf(4, 5, 3),
            arrayOf(7, 8, 6),
            // Columns.
            arrayOf(0, 3, 6),
            arrayOf(1, 4, 7),
            arrayOf(2, 5, 8),
            arrayOf(0, 6, 3),
            arrayOf(1, 7, 4),
            arrayOf(2, 8, 5),
            arrayOf(3, 6, 0),
            arrayOf(4, 7, 1),
            arrayOf(5, 8, 2),
            // Diagonals.
            arrayOf(0, 4, 8),
            arrayOf(0, 8, 4),
            arrayOf(4, 8, 0),
            arrayOf(2, 4, 6),
            arrayOf(4, 6, 2),
            arrayOf(2, 6, 4)
        )
    }
}