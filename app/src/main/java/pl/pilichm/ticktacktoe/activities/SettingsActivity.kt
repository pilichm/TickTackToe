package pl.pilichm.ticktacktoe.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_settings.*
import pl.pilichm.ticktacktoe.R
import pl.pilichm.ticktacktoe.util.Constants

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setUpDifficultySpinner(0)
        setDifficultyLevelDescription()
    }

    /**
     * Adds listener to difficulty level selection spinner, so that displayed description
     * matches selected value.
     * */
    private fun setUpDifficultySpinner(selectedPosition: Int){
        spinnerDifficulty.setSelection(selectedPosition)
        spinnerDifficulty.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setDifficultyLevelDescription()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    /**
     * Refreshed description text so that it matches difficulty level selected in spinner.
     * */
    private fun setDifficultyLevelDescription(){
        when (spinnerDifficulty.selectedItem.toString()){
            Constants.DIFFICULTY_EASY -> {
                tvSettingsDifficultyDesc.text = resources.getString(R.string.easy_level_description)
            }
            Constants.DIFFICULTY_MEDIUM -> {
                tvSettingsDifficultyDesc.text = resources.getString(R.string.medium_level_description)
            }
            Constants.DIFFICULTY_HARD -> {
                tvSettingsDifficultyDesc.text = resources.getString(R.string.hard_level_description)
            }
        }
    }
}