package pl.pilichm.ticktacktoe.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import pl.pilichm.ticktacktoe.R
import pl.pilichm.ticktacktoe.util.Constants

class MainActivity : AppCompatActivity() {
    private var mCurrentPlayer: Int = Constants.FIRST_PLAYER_ID
    private var mBoardState = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addListenersToGridElements()
        resetBoard()
    }

    /**
     * Adds on click listener to all game board elements, adding circle or cross
     * if selected field is empty. Displays toast if selected field is not empty.
     * */
    private fun addListenersToGridElements(){
        for (i in 0..8){
            val view = llMain.findViewWithTag<ImageView>(i.toString())
            view.setOnClickListener {
                if (mBoardState[i]==0){
                    mBoardState[i] = mCurrentPlayer
                    if (mCurrentPlayer==Constants.FIRST_PLAYER_ID){
                        tvWhichPlayer.text = resources.getString(R.string.second_player_move)
                        view.setImageResource(R.drawable.shape_cross)
                        mCurrentPlayer = Constants.SECOND_PLAYER_ID
                    } else {
                        tvWhichPlayer.text = resources.getString(R.string.first_player_move)
                        view.setImageResource(R.drawable.shape_circle)
                        mCurrentPlayer = Constants.FIRST_PLAYER_ID
                    }
                    checkResult()
                } else {
                    Toast.makeText(
                        applicationContext,
                        resources.getString(R.string.field_already_selected),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * Checks if someone has won or game has ended in draw.
     * */
    private fun checkResult(){
        /**
         * Check if someone has winning configuration.
         * */
        for (positions in Constants.winningLocations){
            if (mBoardState[positions[0]]==mBoardState[positions[1]]
                &&mBoardState[positions[0]]==mBoardState[positions[2]]
                &&mBoardState[positions[0]]!=Constants.NO_PLAYER_ID) {
                colorWinningPositions(positions)

                when (mBoardState[positions[0]]){
                    Constants.FIRST_PLAYER_ID -> {
                        tvWhichPlayer.text = resources.getString(R.string.first_player_wins)
                        return
                    }
                    Constants.SECOND_PLAYER_ID -> {
                        tvWhichPlayer.text = resources.getString(R.string.second_player_wins)
                        return
                    }
                }
            }
        }

        /**
         * Check if board contains at least one empty field.
         * If not, current game has ended in draw.
         * */
        var emptyElementFound = false
        for (element in mBoardState){
            if (element==Constants.NO_PLAYER_ID){
                emptyElementFound = true
                break
            }
        }

        if (!emptyElementFound){
            tvWhichPlayer.text = resources.getString(R.string.game_ends_draw)
        }
    }

    /**
     * Color fields from winning line.
     * */
    private fun colorWinningPositions(positions: Array<Int>){
        val winningPosColor = ContextCompat.getColor(this, R.color.winning_positions_color)

        for (position in positions){
            val view = llMain.findViewWithTag<ImageView>(position.toString())
            view.setBackgroundColor(winningPosColor)
        }
    }

    /**
     * Resets game board and current player.
     * Player is set to first player.
     * Background colors of all fields are set to gray.
     * Text encouragement is set to first player.
     * */
    private fun resetBoard(){
        val gridLightColor = ContextCompat.getColor(this, R.color.grid_element_light)
        val gridDarkColor = ContextCompat.getColor(this, R.color.grid_element_dark)
        mCurrentPlayer = Constants.FIRST_PLAYER_ID
        tvWhichPlayer.text = resources.getString(R.string.first_player_move)

        for (i in 0..8){
            val view = llMain.findViewWithTag<ImageView>(i.toString())
            view.setImageResource(0)

            if (i%2==0){
                view.setBackgroundColor(gridLightColor)
            } else {
                view.setBackgroundColor(gridDarkColor)
            }
        }
    }
}