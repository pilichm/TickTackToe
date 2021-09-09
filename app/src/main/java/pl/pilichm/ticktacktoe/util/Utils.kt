package pl.pilichm.ticktacktoe.util

class Utils {
    companion object {

        /**
         * Returns random empty position from game board.
         * */
        fun makeRandomMove(boardState: IntArray): Int {
            while (true){
                val position = (boardState.indices).random()
                if (boardState[position]==Constants.NO_PLAYER_ID){
                    return position
                }
            }
        }

        /**
         * Returns best possible position for next move.
         * */
        fun makeBestPossibleMove(boardState: IntArray): Int {
            /**
             * Check if can win in this move.
             * */
            for (positions in Constants.winningLocations){
                if (positions[0]==Constants.SECOND_PLAYER_ID
                    &&positions[1]==Constants.SECOND_PLAYER_ID
                    &&positions[2]==Constants.NO_PLAYER_ID){
                    return positions[2]
                }
            }

            /**
             * Check if can block human player in next move.
             * */
            for (positions in Constants.winningLocations){
                if (positions[0]==Constants.FIRST_PLAYER_ID
                    &&positions[1]==Constants.FIRST_PLAYER_ID
                    &&positions[2]==Constants.NO_PLAYER_ID){
                    return positions[2]
                }
            }

            /**
             * Return middle position if empty.
             * */
            if (boardState[4]==Constants.NO_PLAYER_ID){
                return 4
            }

            /**
             * If no condition is met return random empty position.
             * */
            return makeRandomMove(boardState)
        }
    }
}