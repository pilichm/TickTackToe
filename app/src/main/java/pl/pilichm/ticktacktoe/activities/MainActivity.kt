package pl.pilichm.ticktacktoe.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import pl.pilichm.ticktacktoe.R
import pl.pilichm.ticktacktoe.util.Constants

class MainActivity : AppCompatActivity() {
    private var mCurrentPlayer: Int = Constants.FIRST_PLAYER_ID
    private var mBoardState = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var mSomeoneWon: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpActionBar()
        addListenersToGridElements()
        resetBoard()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Reset game board after click on menu item.
     * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menuMainActivityReset -> {
                resetBoard()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    /**
     * Sets up custom action bar with no app title.
     * */
    private fun setUpActionBar(){
        setSupportActionBar(toolbarMainActivity)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    /**
     * Adds on click listener to all game board elements, adding circle or cross
     * if selected field is empty. Displays toast if selected field is not empty.
     * */
    private fun addListenersToGridElements(){
        for (i in 0..8){
            val view = llMain.findViewWithTag<RelativeLayout>(i.toString())
            view.setOnClickListener {
                if (mBoardState[i]==0&&!mSomeoneWon){
                    mBoardState[i] = mCurrentPlayer
                    val viewTop = llMain.findViewWithTag<ImageView>("${Constants.PREFIX_TOP_IMAGE}$i")

                    if (mCurrentPlayer==Constants.FIRST_PLAYER_ID){
                        tvWhichPlayer.text = resources.getString(R.string.second_player_move)
                        addImageWithFadeInAnimation(viewTop, R.drawable.shape_cross)
                        mCurrentPlayer = Constants.SECOND_PLAYER_ID
                    } else {
                        tvWhichPlayer.text = resources.getString(R.string.first_player_move)
                        addImageWithFadeInAnimation(viewTop, R.drawable.shape_circle)
                        mCurrentPlayer = Constants.FIRST_PLAYER_ID
                    }
                    checkResult()
                } else {
                    val message =  if (mSomeoneWon){
                        resources.getString(R.string.someone_already_won)
                    } else {
                        resources.getString(R.string.field_already_selected)
                    }

                    Toast.makeText(
                        applicationContext,
                        message,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Adds passed resource as background for ImageView with fade in animation.
     * */
    private fun addImageWithFadeInAnimation(view: ImageView, resourceId: Int){
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.startOffset = 10
        fadeIn.duration = 1000
        view.setImageResource(resourceId)
        view.startAnimation(fadeIn)
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

                mSomeoneWon = true
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
            val viewBottom = llMain.findViewWithTag<ImageView>("${Constants.PREFIX_BOTTOM_IMAGE}$position")
            viewBottom.setBackgroundColor(winningPosColor)
        }
    }

    /**
     * Resets game board and current player.
     * Player is set to first player.
     * Background colors of all fields are set to gray.
     * Text encouragement is set to first player.
     * State of all fields is set to empty.
     * */
    private fun resetBoard(){
        val gridLightColor = ContextCompat.getColor(this, R.color.grid_element_light)
        val gridDarkColor = ContextCompat.getColor(this, R.color.grid_element_dark)
        mCurrentPlayer = Constants.FIRST_PLAYER_ID
        tvWhichPlayer.text = resources.getString(R.string.first_player_move)
        mSomeoneWon = false

        for (i in 0..8){
            mBoardState[i] = Constants.NO_PLAYER_ID
            val viewBottom = llMain.findViewWithTag<ImageView>("${Constants.PREFIX_BOTTOM_IMAGE}$i")
            val viewTop = llMain.findViewWithTag<ImageView>("${Constants.PREFIX_TOP_IMAGE}$i")

            viewTop.setImageResource(0)
            viewBottom.setImageResource(0)

            if (i%2==0){
                viewBottom.setBackgroundColor(gridLightColor)
            } else {
                viewBottom.setBackgroundColor(gridDarkColor)
            }
        }
    }
}