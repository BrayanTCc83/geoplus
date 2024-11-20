package com.example.geoplus

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoplus.databinding.ActivityLevelsBinding
import com.example.geoplus.databinding.ActivityResumeBinding
import com.example.geoplus.fragments.GetStars
import com.example.geoplus.fragments.ResumeFragment
import com.example.geoplus.fragments.ResumeFragmentCardGame
import com.example.geoplus.global.Database
import com.example.geoplus.global.GlobalState
import com.example.geoplus.models.PlayerProgressModel
import kotlin.math.sign

class ResumeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResumeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val globalState: GlobalState = GlobalState.getInstance()
        val title = globalState.questionary?.title?: "Quiz: resumen"
        binding.title.text = title.uppercase()

        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            if(globalState.type == "Memorama")
                ResumeFragmentCardGame()
            else
                ResumeFragment()
        }

        val buttonNext = findViewById<Button>(R.id.btn_next);
        buttonNext.setOnClickListener {
            if(globalState.type == "Memorama") {
                val progress = Database.getInstance().getProgress(GlobalState.getInstance().type)
                var completed = progress.completed
                var scores: MutableList<Float>? = progress.puntuations?.toMutableList()
                if(scores == null) scores = mutableListOf()

                val result = globalState.getCardResult()
                val id = (result?.id ?: 0)

                if(completed <= (result?.id ?: 0)) {
                    completed = id + 1
                    scores.add(result?.score?:0.0f)
                } else {
                    scores[id] = result?.score?:0.0f
                }

                if(Database.getInstance().saveProgress(GlobalState.getInstance().type, PlayerProgressModel(completed, scores.toFloatArray()))) {
                    GlobalState.getInstance().clearCardResult()
                    finish()
                }
                return@setOnClickListener
            }

            try {
                var res = 0.0f
                var totpoints = 0
                globalState.results.forEach { model ->
                    run {
                        res += (model.score * model.points)
                        totpoints += model.points
                    }
                }
                res /= totpoints

                val progress = Database.getInstance().getProgress(GlobalState.getInstance().type)
                var completed = progress.completed
                var scores: MutableList<Float>? = progress.puntuations?.toMutableList()
                if(scores == null) scores = mutableListOf()
                Log.d("LOG_GEOPLUS", "Questionary ${GlobalState.getInstance().questionary}")

                if(completed < (GlobalState.getInstance().questionary?.id ?: 0)){
                    completed = GlobalState.getInstance().questionary?.id?:0
                    Log.d("LOG_GEOPLUS", "Nivel completado $completed")
                    if(scores.size < completed)
                        scores.add(res)
                    else scores[completed - 1] = res
                } else if(completed >= (GlobalState.getInstance().questionary?.id ?: 0))
                    scores[completed - 1] = res

                if(Database.getInstance().saveProgress(GlobalState.getInstance().type, PlayerProgressModel(completed, scores.toFloatArray()))) {
                    GlobalState.getInstance().clearResult()
                    finish()
                }
            } catch(e: Exception) {
                Log.d("LOG_GEOPLUS", e.message?:"")
            }
        }

        val buttonBack = findViewById<Button>(R.id.btn_back);
        buttonBack.setOnClickListener {
            GlobalState.getInstance().clearResult()
            GlobalState.getInstance().clearCardResult()
            finish()
        }
    }
}