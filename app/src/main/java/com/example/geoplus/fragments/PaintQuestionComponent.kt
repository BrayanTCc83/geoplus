package com.example.geoplus.fragments

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoplus.QuestionActivity
import com.example.geoplus.R
import com.example.geoplus.ResumeActivity
import com.example.geoplus.global.GlobalState
import com.example.geoplus.models.PaintObjectQuestion
import com.example.geoplus.models.PaintOnceQuestion
import com.example.geoplus.models.PintaEstadosModel
import com.example.geoplus.models.ResultModel
import kotlin.math.roundToInt

val IndexedColors: Array<Color> = arrayOf(
    Color.Red /* 0 */, Color.Cyan /* 1 */, Color.Blue /* 2 */, Color.Black /* 3 */,
    Color.Green /* 4 */, Color.Yellow /* 5 */, Color.Magenta /* 6 */
)

data class Country(val img: Int, val w: Int, val h: Int, val x: Int, val y: Int)
val PaintCountries: Array<Country> = arrayOf(
    Country(R.drawable.edo_baja_sur, 105, 105, 200, 70),// "Baja California Sur",      // 0
    Country(R.drawable.edo_baja_norte, 90, 90, 162, 0),// "Baja California Norte",    // 1
    Country(R.drawable.edo_sonora, 128, 128, 190, 0),// "Sonora",                   // 2
    Country(R.drawable.edo_chihuahua, 145, 145, 260, 20),// "Chihuahua",                // 3
    Country(R.drawable.edo_coahuila, 128, 128, 335, 49),// "Coahuila",                 // 4
    Country(R.drawable.edo_nuevo_leon, 111, 111, 381, 91),// "Nuevo Leon",               // 5
    Country(R.drawable.edo_tamaulipas, 124, 124, 396, 93),// "Tamaulipas",               // 6
    Country(R.drawable.edo_sinaloa, 100, 100, 261, 100),// "Sinaloa",                  // 7
    Country(R.drawable.edo_durango, 113, 113, 293, 104),// "Durango",                  // 8
    Country(R.drawable.edo_zacatecas, 96, 96, 339, 143),// "Zacatecas",                // 9
    Country(R.drawable.edo_san_luis, 76, 76, 388, 163),// "San Luis Potosi",          // 10
    Country(R.drawable.edo_aguascalientes, 20, 20, 385, 200),// "Aguascalientes",           // 11
    Country(R.drawable.edo_nayarit, 45, 45, 329, 189),// "Nayarit",                  // 12
    Country(R.drawable.edo_jalisco, 76, 76, 330, 195),// "Jalisco",                  // 13
    Country(R.drawable.edo_guanajuato, 40, 40, 395, 210),// "Guanajuato",               // 14
    Country(R.drawable.edo_queretaro, 40, 40, 410, 212),// "Queretaro",                // 15
    Country(R.drawable.edo_hidalgo, 50, 50, 423, 212),// "Hidalgo",                  // 16
    Country(R.drawable.edo_colima, 40, 40, 342, 247),// "Colima",                   // 17
    Country(R.drawable.edo_michoacan, 70, 70, 365, 230),// "Michoacan",                // 18
    Country(R.drawable.edo_mexico, 50, 50, 407, 240),// "Estado de Mexico",         // 19
    Country(R.drawable.edo_cdmx, 20, 20, 435, 255),// "Ciudad de Mexico",         // 20
    Country(R.drawable.edo_tlaxcala, 28, 28, 455, 241),// "Tlaxcala",                 // 21
    Country(R.drawable.edo_puebla, 70, 70, 435, 220),// "Puebla",                   // 22
    Country(R.drawable.edo_verazcruz, 140, 140, 425, 175),// "Veracruz",                 // 23
    Country(R.drawable.edo_morelos, 20, 20, 434, 270),// "Morelos",                  // 24
    Country(R.drawable.edo_guerrero, 80, 80, 380, 260),// "Guerrero",                 // 25
    Country(R.drawable.edo_oaxaca, 80, 80, 450, 260),// "Oaxaca",                   // 26
    Country(R.drawable.edo_chiapas, 70, 70, 510, 280),// "Chiapas",                  // 27
    Country(R.drawable.edo_tabasco, 60, 60, 508, 253),// "Tabasco",                  // 28
    Country(R.drawable.edo_campeche, 60, 60, 535, 230),// "Campeche",                 // 29
    Country(R.drawable.edo_yucatan, 55, 55, 560, 215),// "Yucatan",                  // 30
    Country(R.drawable.edo_quintana_roo, 60, 60, 575, 230),// "Quintana Roo"              // 31
)

fun nextQuestionPaint(score: Float, questions: PintaEstadosModel, questionId: Int) {
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
                    is PaintOnceQuestion -> "${IndexedColors[q.color].toString()}: ${q.answer.joinToString { i -> IndexedStates[i] }}"
                    is PaintObjectQuestion -> q.answer.entries.joinToString { p ->
                        "${p.key}: ${
                            p.value.joinToString { i -> IndexedStates[i] }
                        }"
                    }
                    else -> ""
                },
                points = q.points
            )
        )
    }

    val intent: Intent
    if(questions.questions.size > questionId + 1) {
        intent = Intent(globalState.ctx, QuestionActivity::class.java)
        globalState.questionId++
    } else
        intent = Intent(globalState.ctx, ResumeActivity::class.java)
    globalState.ctx!!.startActivity(intent)
}

@Composable
fun PaintQuestionComponent(questions: PintaEstadosModel, questionId: Int) {
    val question = questions.questions[questionId]
    Log.d("LOG_GEOPLUS", "Rendering [$questionId]: $question")
    when(question) {
        is PaintOnceQuestion -> PaintOnceComponent(question as PaintOnceQuestion, fun(score: Float) {
            nextQuestionPaint(score, questions, questionId)
        })
        is PaintObjectQuestion -> PaintObjectComponent(question as PaintObjectQuestion, fun(score: Float) {
            nextQuestionPaint(score, questions, questionId)
        })
        else -> Column {}
    }
}

val paints = mutableStateListOf<Pair<Int, Int>>()
@Composable
fun PaintCountries() {
    PaintCountries.forEach {
            country ->
        val c: Color = Color.White
        val colorMatrix = floatArrayOf(
            c.red, 0f, 0f, 0f, 0f,
            0f, c.green, 0f, 0f, 0f,
            0f, 0f, c.blue, 0f, 0f,
            0f, 0f, 0f, c.alpha, 0f
        )
        Image(
            painter = painterResource(id = country.img),
            contentDescription = "",
            modifier = Modifier
                .width(country.w.dp)
                .height(country.h.dp)
                .offset(x = country.x.dp, y = country.y.dp),
            colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
        )
    }
    paints.forEach {
            pair ->
        val country: Country = PaintCountries[pair.second]
        val c: Color = IndexedColors[pair.first]
        val colorMatrix = floatArrayOf(
            c.red, 0f, 0f, 0f, 0f,
            0f, c.green, 0f, 0f, 0f,
            0f, 0f, c.blue, 0f, 0f,
            0f, 0f, 0f, c.alpha, 0f
        )
        val k = 4
        Image(
            painter = painterResource(id = country.img),
            contentDescription = "",
            modifier = Modifier
                .width((country.w*k).dp)
                .height((country.h*k).dp)
                .offset(x = (country.x*k).dp, y = (country.y*k).dp),
            colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
        )
    }
}

@Composable
fun PaintOnceComponent(q: PaintOnceQuestion, nextQuestion: (Float) -> Unit) {
    var color by remember { mutableStateOf<Int>(-1) }
    Column {
        Text(
            modifier = Modifier
                .width(350.dp)
                .padding(16.dp),
            text = q.question,
            color = colorResource(id = R.color.principal_color),
            fontSize = 14.sp
        )
        LazyRow {
            items(7) { i ->
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(50.dp)
                        .width(50.dp)
                        .background(
                            color = if (color == i)
                                colorResource(id = R.color.principal_color)
                            else
                                IndexedColors[i],
                            shape = CircleShape
                        )
                        .border(
                            width = 3.dp,
                            color = colorResource(id = R.color.secondary_color),
                            shape = CircleShape
                        )
                        .clickable {
                            Log.d("LOG_GEOPLUS", "Click a color: ${IndexedColors[i]}")
                            color = i
                        }
                )
            }
        }
        LazyRow {
            item {
                Box(modifier = Modifier
                    .padding(10.dp)
                    .width(800.dp)
                    .height(800.dp)) {
                    PaintCountries()
                    val kx = 1.36f
                    val ky = 1.25f
                    botonesEstados.forEachIndexed {
                        i, pair -> FixedButton(x = (pair.first/kx).roundToInt()+110, y = (pair.second/ky).roundToInt(), paints.any { p -> p.second == i }, fun() {
                            if(color == -1) {
                                Toast.makeText(GlobalState.getInstance().ctx, "No has seleccionado ningun color.", Toast.LENGTH_LONG).show()
                                return
                            }
                            Log.d("LOG_GEOPLUS", "Click a estado: ${IndexedStates[i]}")
                            if(paints.any { p -> p.second == i })
                                paints.remove(paints.find { p -> p.second == i })
                            else
                                paints.add(Pair(color, i))
                        })
                    }
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
                                val errors = paints.filter { pair -> !q.answer.contains(pair.second) || q.color != pair.first }.size
                                val corrects = paints.filter { pair -> q.answer.contains(pair.second) && q.color == pair.first }.size
                                val sc: Float = ((corrects - errors).toFloat() / q.answer.size.toFloat()) * 10.0f
                                Log.d("LOG_GEOPLUS", "Aciertos: $corrects, Errores: $errors, Requeridos: ${q.answer.size}, Calificacion: ${sc}")
                                paints.clear()
                                nextQuestion(sc)
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
    }
}

@Composable
fun PaintObjectComponent(q: PaintObjectQuestion, nextQuestion: (Float) -> Unit) {
    var color by remember { mutableStateOf<Int>(-1) }
    Column {
        Text(
            modifier = Modifier
                .width(350.dp)
                .padding(16.dp),
            text = q.question,
            color = colorResource(id = R.color.principal_color),
            fontSize = 14.sp
        )
        LazyRow {
            items(7) { i ->
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(50.dp)
                        .width(50.dp)
                        .background(
                            color = if (color == i)
                                colorResource(id = R.color.principal_color)
                            else
                                IndexedColors[i],
                            shape = CircleShape
                        )
                        .border(
                            width = 3.dp,
                            color = colorResource(id = R.color.secondary_color),
                            shape = CircleShape
                        )
                        .clickable {
                            Log.d("LOG_GEOPLUS", "Click a color: ${IndexedColors[i]}")
                            color = i
                        }
                )
            }
        }
        LazyRow {
            item {
                Box(modifier = Modifier
                    .padding(10.dp)
                    .width(800.dp)
                    .height(800.dp)) {
                    PaintCountries()
                    botonesEstados.forEachIndexed {
                        i, pair -> FixedButton(x = pair.first, y = pair.second, paints.any { p -> p.second == i }, fun() {
                            if(color == -1) {
                                Toast.makeText(GlobalState.getInstance().ctx, "No has seleccionado ningun color.", Toast.LENGTH_LONG).show()
                                return
                            }
                            Log.d("LOG_GEOPLUS", "Click a estado: ${IndexedStates[i]}")
                            if(paints.any { p -> p.second == i })
                                paints.remove(paints.find { p -> p.second == i })
                            else
                                paints.add(Pair(color, i))
                        })
                    }
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
                                val errors = paints.filter { pair -> (q.answer["${pair.first}"]?.contains(pair.second))?:true }.size
                                val corrects = paints.filter { pair -> (q.answer["${pair.first}"]?.contains(pair.second))?:false }.size
                                val sc: Float = ((corrects - errors).toFloat() / q.answer.size.toFloat()) * 10.0f
                                Log.d("LOG_GEOPLUS", "Aciertos: $corrects, Errores: $errors, Requeridos: ${q.answer.size}, Calificacion: ${sc}")
                                paints.clear()
                                nextQuestion(sc)
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
    }
}
