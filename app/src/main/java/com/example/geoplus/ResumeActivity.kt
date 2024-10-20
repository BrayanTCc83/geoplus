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
        binding.title.setText(title.uppercase())

        var res: Float = 0.0f
        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                globalState.results.forEach {
                    resultModel -> Row {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcer_foreground),
                            contentDescription = "",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .padding(4.dp)
                                .width(50.dp)
                                .height(50.dp)
                        )
                        Column {
                            Text(
                                text = "Pregunta ${resultModel.id}: ${resultModel.question}",
                                color = colorResource(id = R.color.principal_color),
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Respuesta: ${resultModel.answer}",
                                color = colorResource(id = R.color.white),
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Puntuacion: ${resultModel.score}",
                                color = colorResource(id = R.color.white),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                var totpoints = 0
                globalState.results.forEach {
                    model -> run {
                        res += (model.score * model.points)
                        totpoints += model.points
                    }
                }
                res /= totpoints
                GetStars(score = res.toDouble())
                Text(
                    text = "Puntuacion final: %.2f".format(res),
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp
                )
            }
        }

        val context = this
        val buttonNext = findViewById<Button>(R.id.btn_next);
        buttonNext.setOnClickListener {
            try {
                val progress = Database.getInstance().getProgress(GlobalState.getInstance().type)
                var completed = progress.completed
                var scores: MutableList<Float>? = progress.puntuations?.toMutableList()
                if(scores == null) scores = mutableListOf()
                Log.d("LOG_GEOPLUS", "Questionary ${GlobalState.getInstance().questionary}")
                if(completed < (GlobalState.getInstance().questionary?.id ?: 0)){
                    completed = GlobalState.getInstance().questionary?.id?:0
                    Log.d("LOG_GEOPLUS", "Levels complete $completed")
                    if(scores.size < completed)
                        scores.add(res)
                    else scores[completed - 1] = res
                } else if(completed >= (GlobalState.getInstance().questionary?.id ?: 0))
                    scores[completed - 1] = res

                if(Database.getInstance().saveProgress(GlobalState.getInstance().type, PlayerProgressModel(completed, scores.toFloatArray()))) {
                    GlobalState.getInstance().results.clear()
                    val intent = Intent(context, HomeActivity::class.java)
                    startActivity(intent)
                }
            } catch(e: Exception) {
                Log.d("LOG_GEOPLUS", e.message?:"")
            }
        }

        val buttonBack = findViewById<Button>(R.id.btn_back);
        buttonBack.setOnClickListener {
            GlobalState.getInstance().results.clear()
            val intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}