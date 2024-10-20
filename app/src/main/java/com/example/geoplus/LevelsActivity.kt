package com.example.geoplus

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.snapshots.AutoboxingStateValueProperty
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.example.geoplus.databinding.ActivityLevelsBinding
import com.example.geoplus.fragments.GameCardComponent
import com.example.geoplus.fragments.GetStars
import com.example.geoplus.models.GameCardModel
import androidx.compose.ui.Modifier
import com.example.geoplus.fragments.LevelsButtons

class LevelsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLevelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLevelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val reference = intent.getStringExtra("reference")
        val levels = intent.getIntExtra("levels", 0)
        val score = intent.getDoubleExtra("score", 0.0)
        binding.gameTitle.setText(title)

        val context = this
        val starsComposeView = findViewById<ComposeView>(R.id.compose_view)
        starsComposeView.setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GetStars(score = score)
                LevelsButtons(
                    ctx = context,
                    game = GameCardModel(0, title?:"", levels, reference?:"")
                )
            }
        }
    }
}