package pl.pilichm.ticktacktoe.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main_menu.*
import pl.pilichm.ticktacktoe.R
import pl.pilichm.ticktacktoe.util.Constants
import kotlin.system.exitProcess

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        setUpListeners()
    }

    private fun setUpListeners(){
        /**
         * Start game for one player.
         * */
        btnManiMenuOnePlayer.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra(Constants.PROPERTY_NUMBER_OF_PLAYERS, 1)
            startActivity(intent)
        }

        /**
         * Start game for two players.
         * */
        btnManiMenuTwoPlayers.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra(Constants.PROPERTY_NUMBER_OF_PLAYERS, 2)
            startActivity(intent)
        }

        /**
         * Go to settings.
         * */
        btnManiMenuSettings.setOnClickListener {
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
        }

        /**
         * Exit app.
         * */
        btnManiMenuExit.setOnClickListener {
            finish();
            exitProcess(0);
        }
    }
}