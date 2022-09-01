package pl.pilichm.ticktacktoe.activities

import android.content.Context
import android.media.MediaPlayer
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
import pl.pilichm.ticktacktoe.R
import pl.pilichm.ticktacktoe.databinding.ActivityMainBinding
import pl.pilichm.ticktacktoe.util.Constants
import pl.pilichm.ticktacktoe.util.Utils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mCurrentPlayer: Int = Constants.FIRST_PLAYER_ID
    private var mBoardState = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var mSomeoneWon: Boolean = false
    private var mFirstPlayerSign = Constants.FIRST_SIGN
    private var mSecondPlayerSign = Constants.FIRST_SIGN
    private var mNumOfPlayers = 2
    private var mDifficultyLevel = Constants.DIFFICULTY_EASY
    private var mComputerPlayerMoveCount = 0
    private var mVolumeOn: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.PROPERTY_NUMBER_OF_PLAYERS)){
            mNumOfPlayers = intent.getIntExtra(Constants.PROPERTY_NUMBER_OF_PLAYERS, 2)
        }

        setUpActionBar()
        addListenersToGridElements()
        getSettingsFromSharedPreferences()
        resetBoard()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Reads settings saved in shared preferences.
     * */
    private fun getSettingsFromSharedPreferences(){
        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        mFirstPlayerSign = sharedPreferences.getInt(Constants.SP_KEY_FIRST_PLAYER_SIGN, 0)
        mSecondPlayerSign = sharedPreferences.getInt(Constants.SP_KEY_SECOND_PLAYER_SIGN, 0)
        mDifficultyLevel = sharedPreferences.getString(Constants.SP_KEY_DIFFICULTY_LEVEL, Constants.DIFFICULTY_EASY)!!
    }

    /**
     * Returns id of image resource which will be displayed in fields clicked by first player
     * if true is passed, or second player if false is passed.
     * */
    private fun getSignIdForPlayer(isFirstPlayer: Boolean): Int{
        return if (isFirstPlayer){
            when (mFirstPlayerSign){
                Constants.FIRST_SIGN -> R.drawable.shape_cross
                Constants.SECOND_SIGN -> R.drawable.circle_red
                else -> R.drawable.shape_cross
            }
        } else {
            when (mSecondPlayerSign){
                Constants.FIRST_SIGN -> R.drawable.shape_circle
                Constants.SECOND_SIGN -> R.drawable.circle_green
                else -> R.drawable.shape_circle
            }
        }
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
        setSupportActionBar(binding.toolbarMainActivity)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    /**
     * Adds on click listener to all game board elements, adding circle or cross
     * if selected field is empty. Displays toast if selected field is not empty.
     * */
    private fun addListenersToGridElements(){
        for (i in 0..8){
            val view = binding.llMain.findViewWithTag<RelativeLayout>(i.toString())
            view.setOnClickListener {
                if (mBoardState[i]==0&&!mSomeoneWon){
                    mBoardState[i] = mCurrentPlayer
                    val viewTop = binding.llMain.findViewWithTag<ImageView>("${Constants.PREFIX_TOP_IMAGE}$i")

                    if (mCurrentPlayer==Constants.FIRST_PLAYER_ID){
                        checkResult()
                        binding.tvWhichPlayer.text = resources.getString(R.string.second_player_move)
                        addImageWithFadeInAnimation(viewTop, getSignIdForPlayer(true))
                        mCurrentPlayer = Constants.SECOND_PLAYER_ID

                        /**
                         * In one player mode cpu will make moves for second player.
                         */
                        if (mNumOfPlayers==1&&Utils.checkIfBoardContainsEmptyField(mBoardState)){
                            val selectedPosition = when (mDifficultyLevel) {
                                Constants.DIFFICULTY_EASY -> {
                                    Utils.makeRandomMove(mBoardState)
                                }
                                Constants.DIFFICULTY_MEDIUM -> {
                                    if (mComputerPlayerMoveCount%2==0){
                                        Utils.makeRandomMove(mBoardState)
                                    } else {
                                        Utils.makeBestPossibleMove(mBoardState)
                                    }
                                }
                                Constants.DIFFICULTY_HARD -> {
                                    Utils.makeBestPossibleMove(mBoardState)
                                }
                                else -> {
                                    Utils.makeRandomMove(mBoardState)
                                }
                            }

                            val selectedView = binding.llMain.findViewWithTag<ImageView>("${Constants.PREFIX_TOP_IMAGE}$selectedPosition")

                            mBoardState[selectedPosition] = Constants.SECOND_PLAYER_ID
                            binding.tvWhichPlayer.text = resources.getString(R.string.first_player_move)
                            addImageWithFadeInAnimation(selectedView, getSignIdForPlayer(false))
                            mCurrentPlayer = Constants.FIRST_PLAYER_ID
                            mComputerPlayerMoveCount++
                            checkResult()
                        }

                    } else {
                        binding.tvWhichPlayer.text = resources.getString(R.string.first_player_move)
                        addImageWithFadeInAnimation(viewTop, getSignIdForPlayer(false))
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

        /**
         * Adds listener to floating action button for enabling and disabling game sounds.
         * */
        binding.fabVolumeUp.setOnClickListener {
            if (mVolumeOn){
                binding.fabVolumeUp.setImageResource(R.drawable.ic_baseline_volume_off_24)
            } else {
                binding.fabVolumeUp.setImageResource(R.drawable.ic_baseline_volume_up_24)
            }
            mVolumeOn = !mVolumeOn
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
                        binding.tvWhichPlayer.text = resources.getString(R.string.first_player_wins)
                        playSoundIfEnabled(R.raw.game_result_victory)
                        return
                    }
                    Constants.SECOND_PLAYER_ID -> {
                        if (mNumOfPlayers==2){
                            binding.tvWhichPlayer.text = resources.getString(R.string.second_player_wins)
                            playSoundIfEnabled(R.raw.game_result_victory)
                        } else {
                            binding.tvWhichPlayer.text = resources.getString(R.string.cpu_player_wins)
                            playSoundIfEnabled(R.raw.game_result_lose)
                        }

                        return
                    }
                }
            }
        }

        /**
         * Check if board contains at least one empty field.
         * If not, current game has ended in draw.
         * */

        if (!Utils.checkIfBoardContainsEmptyField(mBoardState)){
            binding.tvWhichPlayer.text = resources.getString(R.string.game_ends_draw)
            playSoundIfEnabled(R.raw.game_result_draw)
        }
    }

    /**
     * Color fields from winning line.
     * */
    private fun colorWinningPositions(positions: Array<Int>){
        val winningPosColor = ContextCompat.getColor(this, R.color.winning_positions_color)

        for (position in positions){
            val viewBottom = binding.llMain.findViewWithTag<ImageView>("${Constants.PREFIX_BOTTOM_IMAGE}$position")
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
        binding.tvWhichPlayer.text = resources.getString(R.string.first_player_move)
        mSomeoneWon = false

        for (i in 0..8){
            mBoardState[i] = Constants.NO_PLAYER_ID
            val viewBottom = binding.llMain.findViewWithTag<ImageView>("${Constants.PREFIX_BOTTOM_IMAGE}$i")
            val viewTop = binding.llMain.findViewWithTag<ImageView>("${Constants.PREFIX_TOP_IMAGE}$i")

            viewTop.setImageResource(0)
            viewBottom.setImageResource(0)

            if (i%2==0){
                viewBottom.setBackgroundColor(gridLightColor)
            } else {
                viewBottom.setBackgroundColor(gridDarkColor)
            }
        }
    }

    /**
     * Plays passed audio resource if game sounds are enabled.
     * */
    private fun playSoundIfEnabled(soundId: Int){
        if (mVolumeOn){
            val mMediaPlayer = MediaPlayer.create(applicationContext, soundId)
            mMediaPlayer?.start()
        }
    }
}