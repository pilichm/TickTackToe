package pl.pilichm.ticktacktoe.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.pilichm.ticktacktoe.databinding.ActivityMainMenuBinding
import pl.pilichm.ticktacktoe.util.Constants
import kotlin.system.exitProcess

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListeners()
    }

    private fun setUpListeners(){
        /**
         * Start game for one player.
         * */
        binding.btnManiMenuOnePlayer.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra(Constants.PROPERTY_NUMBER_OF_PLAYERS, 1)
            startActivity(intent)
        }

        /**
         * Start game for two players.
         * */
        binding.btnManiMenuTwoPlayers.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra(Constants.PROPERTY_NUMBER_OF_PLAYERS, 2)
            startActivity(intent)
        }

        /**
         * Go to settings.
         * */
        binding.btnManiMenuSettings.setOnClickListener {
            startActivity(Intent(applicationContext, SettingsActivity::class.java))
        }

        /**
         * Exit app.
         * */
        binding.btnManiMenuExit.setOnClickListener {
            finish();
            exitProcess(0);
        }
    }
}