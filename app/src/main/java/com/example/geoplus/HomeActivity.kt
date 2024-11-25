package com.example.geoplus

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.items
import com.example.geoplus.databinding.ActivityHomeBinding
import com.example.geoplus.models.GameCardModel
import com.example.geoplus.utils.ReadJSONFromAssets
import com.google.gson.Gson
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.ComposeView
import com.example.geoplus.fragments.GameCardComponent
import com.example.geoplus.global.Database
import com.example.geoplus.models.DefaultUser
import com.example.geoplus.models.User
import java.io.File

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sesion: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            sesion = Database.getInstance().session ?: DefaultUser
        } catch(e: Exception) {
            Log.d("LOG_GEOPLUS", e.message?:"Error")
        }

        binding.nombre.text = "${sesion.name} ${sesion.lastname1} ${sesion.lastname2}" ?: "NoUsuario"

        binding.btnLogout.setOnClickListener {
            if(Database.getInstance().logout()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val jsonString = ReadJSONFromAssets(baseContext, "Games.json")
        val cards: MutableList<GameCardModel> = Gson().fromJson(jsonString, Array<GameCardModel>::class.java).toMutableList()
        cards.forEachIndexed { index, game ->
            if(Database.getInstance().progressExists(game.title)) {
                val progress = Database.getInstance().getProgress(game.title)
                cards[index].progress = progress
            }
        }

        val context = this
        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cards) {
                    card -> GameCardComponent(gameCard = card, ctx = context)
                }
            }
        }

        val configurationBtn = findViewById<ImageButton>(R.id.btnConfiguracion)
        configurationBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val btnIdea = findViewById<ImageButton>(R.id.btnGeografia)
        btnIdea.setOnClickListener {
            val intent = Intent(this, IdeaActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}