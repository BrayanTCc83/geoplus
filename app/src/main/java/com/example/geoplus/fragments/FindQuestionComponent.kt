package com.example.geoplus.fragments

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoplus.QuestionActivity
import com.example.geoplus.R
import com.example.geoplus.ResumeActivity
import com.example.geoplus.global.GlobalState
import com.example.geoplus.models.FindByDescriptionQuestion
import com.example.geoplus.models.FindByImageQuestion
import com.example.geoplus.models.FindYourselfModel
import com.example.geoplus.models.ResultModel

val ImagesIndexed: Array<Int> = arrayOf(
    R.drawable.edo_cdmx,    // 0
    R.drawable.playa_del_carmen,    // 1
    R.drawable.acapulco,    // 2
    R.drawable.los_cabos,    // 3
    R.drawable.pachuca_de_soto,    // 4
    R.drawable.cdmx,    // 5
    R.drawable.tampico,    // 6
    R.drawable.tepic,    // 7
    R.drawable.ciudad_juarez,    // 8
    R.drawable.edo_tamaulipas,
    R.drawable.reynosa,
    R.drawable.jalapa,
    R.drawable.pico_de_orizaba,
    R.drawable.popocatepetl,
    R.drawable.chilpancingo,
    R.drawable.morelos,
    R.drawable.toluca,
    R.drawable.cholula,
    R.drawable.chichenizta,
    R.drawable.morelia,
    R.drawable.reserva_biosfera,
    R.drawable.xcaret,
    R.drawable.palacio_republica,
    R.drawable.congreso_nacion,
    R.drawable.monumento_revolucion,
    R.drawable.castillo_chapultepec,
    R.drawable.guadalajara,
    R.drawable.puerto_escondido,
    R.drawable.islas_marianas,
    R.drawable.merida,
    R.drawable.atlantes_de_tula,
    R.drawable.tlaxcala,
    R.drawable.teotihuacan,
    R.drawable.tical,
    R.drawable.c_3_zapotes,
)

fun nextQuestionFind(score: Float, questions: FindYourselfModel, questionId: Int, onEnd: () -> Unit) {
    val globalState: GlobalState = GlobalState.getInstance()
    if(globalState.ctx == null)
        return

    if(questions.questions.size > questionId) {
        val q = questions.questions[questionId]
        globalState.addResult(
            ResultModel(
                id = questionId + 1,
                score = if(score > 0) score else 0f,
                question = q.question,
                answer = when(q) {
                    is FindByImageQuestion -> q.options[q.answer]
                    is FindByDescriptionQuestion -> "Opción ${q.options[q.answer]}."
                    else -> ""
                },
                points = q.points
            )
        )
        onEnd()
    }

    val intent: Intent
    if(questions.questions.size > questionId + 1) {
        intent = Intent(globalState.ctx, QuestionActivity::class.java)
        globalState.questionId++
    } else
        intent = Intent(globalState.ctx, ResumeActivity::class.java)
    globalState.ctx!!.startActivity(intent)
    onEnd()
}

@Composable
fun FindQuestionComponent(questions: FindYourselfModel, questionId: Int, onEnd: () -> Unit) {
    val question = questions.questions[questionId]
    Log.d("LOG_GEOPLUS", "Rendering [$questionId]: $question")
    when(question) {
        is FindByImageQuestion -> FindOnceComponent(question as FindByImageQuestion, fun(score: Float) {
            nextQuestionFind(score, questions, questionId, onEnd)
        }, onEnd)
        is FindByDescriptionQuestion -> FindObjectComponent(question as FindByDescriptionQuestion, fun(score: Float) {
            nextQuestionFind(score, questions, questionId, onEnd)
        }, onEnd)
        else -> Column {}
    }
}

@Composable
fun FindOnceComponent(q: FindByImageQuestion, nextQuestion: (Float) -> Unit, onEnd: () -> Unit) {
    Log.d("LOG_GEOPLUS", "Radio Question rendering")
    val ctx: Context? = GlobalState.getInstance().ctx

    var selected by remember { mutableStateOf<Int>(-1) }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = q.question,
                color = colorResource(id = R.color.principal_color),
                fontSize = 14.sp
            )
        }
        item {
            val id = (ctx?.resources?.getIdentifier(q.image ?: "", "drawable", ctx.packageName))?:0
            Log.d("LOG_GEOPLUS", "Imagen ${q.image} -> $id")
            Image(
                painter = painterResource(id = id),
                contentDescription = null,
                modifier = Modifier.width(400.dp).height(400.dp),
                contentScale = ContentScale.Fit
            )
        }
        itemsIndexed(q.options) {
                idx: Int, option: String -> Row(
            modifier = Modifier.clickable { selected = idx },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected == idx,
                colors = RadioButtonColors(
                    selectedColor = colorResource(id = R.color.principal_color),
                    unselectedColor = colorResource(id = R.color.white),
                    disabledSelectedColor = colorResource(id = R.color.white),
                    disabledUnselectedColor = colorResource(id = R.color.white)
                ),
                onClick = { selected = idx }
            )
            Text(
                text = option,
                color = colorResource(id = R.color.white),
                fontSize = 14.sp
            )
        }
        }
        item {
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .background(
                        color = colorResource(id = R.color.principal_color),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 3.dp,
                        color = colorResource(id = R.color.secondary_color),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(16.dp)
                    .clickable { nextQuestion((10.0f / if (q.answer == selected) 1 else 2)) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.button_check),
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun FindObjectComponent(q: FindByDescriptionQuestion, nextQuestion: (Float) -> Unit, onEnd: () -> Unit) {
    Log.d("LOG_GEOPLUS", "Radio Question rendering")
    val ctx: Context? = GlobalState.getInstance().ctx

    var selected by remember { mutableStateOf<Int>(-1) }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = q.question,
                color = colorResource(id = R.color.principal_color),
                fontSize = 14.sp
            )
        }
        itemsIndexed(q.options) {
            idx: Int, option: Int -> Row(
                modifier = Modifier.clickable { selected = idx },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == idx,
                    colors = RadioButtonColors(
                        selectedColor = colorResource(id = R.color.principal_color),
                        unselectedColor = colorResource(id = R.color.white),
                        disabledSelectedColor = colorResource(id = R.color.white),
                        disabledUnselectedColor = colorResource(id = R.color.white)
                    ),
                    onClick = { selected = idx }
                )
                Image(
                    painter = painterResource(id = ImagesIndexed[option]),
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
        item {
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .background(
                        color = colorResource(id = R.color.principal_color),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 3.dp,
                        color = colorResource(id = R.color.secondary_color),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(16.dp)
                    .clickable {
                        nextQuestion((10.0f / if (q.answer == selected) 1 else 2))
                   },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.button_check),
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp
                )
            }
        }
    }
}
