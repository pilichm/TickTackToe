package pl.pilichm.ticktacktoe.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import pl.pilichm.ticktacktoe.R
import pl.pilichm.ticktacktoe.databinding.ActivitySettingsBinding
import pl.pilichm.ticktacktoe.util.Constants

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private var mDifficultyLevel = Constants.DIFFICULTY_EASY
    private var mFirstPlayerSign = Constants.FIRST_SIGN
    private var mSecondPlayerSign = Constants.FIRST_SIGN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSettingsFromSharedPreferences()
        setUpDifficultySpinner(getSpinnerPositionForDifficulty(mDifficultyLevel))
        setDifficultyLevelDescription()
        setUpListenersForRadioButtons()
        setUpInitialRadioButtonValues()
        setUpResetButton()
    }

    /**
     * Resets setting values to default in shared preferences and ui elements.
     * */
    private fun setUpResetButton(){
        binding.btnSettingsResetToDefault.setOnClickListener {
            saveSetting(Constants.SP_KEY_DIFFICULTY_LEVEL, Constants.DIFFICULTY_EASY)
            saveSetting(Constants.SP_KEY_FIRST_PLAYER_SIGN, Constants.FIRST_SIGN)
            saveSetting(Constants.SP_KEY_SECOND_PLAYER_SIGN, Constants.FIRST_SIGN)
            mDifficultyLevel = Constants.DIFFICULTY_EASY
            mFirstPlayerSign = Constants.FIRST_SIGN
            mSecondPlayerSign = Constants.FIRST_SIGN
            setUpDifficultySpinner(getSpinnerPositionForDifficulty(mDifficultyLevel))
            setDifficultyLevelDescription()
            setUpInitialRadioButtonValues()
        }
    }

    /**
     * Sets up radio button so that they match values stored in shared preferences.
     * */
    private fun setUpInitialRadioButtonValues(){
        when (mFirstPlayerSign){
            Constants.FIRST_SIGN -> {
                binding.firstPlayerCross.isChecked = true
                binding.firstPlayerRedCircle.isChecked = false
            }
            Constants.SECOND_SIGN -> {
                binding.firstPlayerCross.isChecked = false
                binding.firstPlayerRedCircle.isChecked = true
            }
        }

        when (mSecondPlayerSign){
            Constants.FIRST_SIGN -> {
                binding.secondPlayerCircle.isChecked = true
                binding.secondPlayerGreenCircle.isChecked = false
            }
            Constants.SECOND_SIGN -> {
                binding.secondPlayerCircle.isChecked = false
                binding.secondPlayerGreenCircle.isChecked = true
            }
        }
    }

    /**
     * Adds listeners to radio buttons responsible for changing first and second player sign.
     * Changed values are saved in shared preferences.
     * */
    private fun setUpListenersForRadioButtons(){
        binding.firstPlayerCross.setOnClickListener {
            saveSetting(Constants.SP_KEY_FIRST_PLAYER_SIGN, Constants.FIRST_SIGN)
        }

        binding.firstPlayerRedCircle.setOnClickListener {
            saveSetting(Constants.SP_KEY_FIRST_PLAYER_SIGN, Constants.SECOND_SIGN)
        }

        binding.secondPlayerCircle.setOnClickListener {
            saveSetting(Constants.SP_KEY_SECOND_PLAYER_SIGN, Constants.FIRST_SIGN)
        }

        binding.secondPlayerGreenCircle.setOnClickListener {
            saveSetting(Constants.SP_KEY_SECOND_PLAYER_SIGN, Constants.SECOND_SIGN)
        }
    }

    /**
     * Reads settings saved in shared preferences.
     * */
    private fun getSettingsFromSharedPreferences(){
        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        mDifficultyLevel = sharedPreferences.getString(Constants.SP_KEY_DIFFICULTY_LEVEL, Constants.DIFFICULTY_EASY)!!
        mFirstPlayerSign = sharedPreferences.getInt(Constants.SP_KEY_FIRST_PLAYER_SIGN, 0)
        mSecondPlayerSign = sharedPreferences.getInt(Constants.SP_KEY_SECOND_PLAYER_SIGN, 0)
    }


    /**
     * Adds listener to difficulty level selection spinner, so that displayed description
     * matches selected value.
     * */
    private fun setUpDifficultySpinner(selectedPosition: Int){
        binding.spinnerDifficulty.setSelection(selectedPosition)
        binding.spinnerDifficulty.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setDifficultyLevelDescription()

                /**
                 * Save changed difficulty level in shared preferences.
                 * */
                when (position) {
                    0 -> saveSetting(Constants.SP_KEY_DIFFICULTY_LEVEL, Constants.DIFFICULTY_EASY)
                    1 -> saveSetting(Constants.SP_KEY_DIFFICULTY_LEVEL, Constants.DIFFICULTY_MEDIUM)
                    2 -> saveSetting(Constants.SP_KEY_DIFFICULTY_LEVEL, Constants.DIFFICULTY_HARD)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    /**
     * Refreshed description text so that it matches difficulty level selected in spinner.
     * */
    private fun setDifficultyLevelDescription(){
        when (binding.spinnerDifficulty.selectedItem.toString()){
            Constants.DIFFICULTY_EASY -> {
                binding.tvSettingsDifficultyDesc.text = resources.getString(R.string.easy_level_description)
            }
            Constants.DIFFICULTY_MEDIUM -> {
                binding.tvSettingsDifficultyDesc.text = resources.getString(R.string.medium_level_description)
            }
            Constants.DIFFICULTY_HARD -> {
                binding.tvSettingsDifficultyDesc.text = resources.getString(R.string.hard_level_description)
            }
        }
    }

    /**
     * Returns spinner position for selected Difficulty level.
     * */
    private fun getSpinnerPositionForDifficulty(difficultyName: String): Int {
        return when (difficultyName){
            Constants.DIFFICULTY_EASY -> 0
            Constants.DIFFICULTY_MEDIUM -> 1
            Constants.DIFFICULTY_HARD -> 2
            else -> 0
        }
    }

    /**
     * Saves passed value under passed key in shared preferences.
     * Works only with String and Int values.
     * */
    private fun saveSetting(settingName: String, settingValue: Any){
        val sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if (settingValue is String){
            editor.putString(settingName, settingValue)
        }

        if (settingValue is Int){
            editor.putInt(settingName, settingValue)
        }

        editor.apply()
    }
}