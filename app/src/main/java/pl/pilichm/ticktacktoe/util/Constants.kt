package pl.pilichm.ticktacktoe.util

class Constants {
    companion object {
        const val NO_PLAYER_ID = 0
        const val FIRST_PLAYER_ID = 1
        const val SECOND_PLAYER_ID = 2

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
    }
}