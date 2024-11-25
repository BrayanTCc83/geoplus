package com.example.geoplus

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.ComposeView
import com.example.geoplus.databinding.ActivityLevelsBinding
import com.example.geoplus.fragments.GetStars
import com.example.geoplus.models.GameCardModel
import androidx.compose.ui.Modifier
import com.example.geoplus.fragments.LevelsButtons
import com.example.geoplus.global.Database
import com.example.geoplus.global.GlobalState

class LevelsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLevelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLevelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("id", 0)
        val title = intent.getStringExtra("title")
        val image = intent.getStringExtra("image")
        val reference = intent.getStringExtra("reference")
        val levels = intent.getIntExtra("levels", 0)
        val score = intent.getDoubleExtra("score", 0.0)
        binding.gameTitle.text = title.toString().uppercase()

        val progress =  Database.getInstance().getProgress(title?:"")

        val context = this
        val starsComposeView = findViewById<ComposeView>(R.id.description_view)
        val levelsComposeView = findViewById<ComposeView>(R.id.compose_view)
        starsComposeView.setContent {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GetStars(score = score)
            }
        }
        levelsComposeView.setContent {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LevelsButtons(
                    ctx = context,
                    game = GameCardModel(id, image?:"", title?:"", levels, reference?:"", progress = progress)
                )
            }
        }

        GlobalState.getInstance().reset()
        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
    }
}