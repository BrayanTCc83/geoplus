package com.example.geoplus.fragments

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoplus.QuestionActivity
import com.example.geoplus.global.GlobalState
import com.example.geoplus.models.ComplexRutaQuestion
import com.example.geoplus.models.RutasModel
import com.example.geoplus.models.SimpleRutaQuestion
import com.example.geoplus.R
import com.example.geoplus.ResumeActivity
import com.example.geoplus.models.ResultModel
import kotlin.math.abs

val IndexedStates: Array<String> = arrayOf(
    "Baja California Sur",      // 0
    "Baja California Norte",    // 1
    "Sonora",                   // 2
    "Chihuahua",                // 3
    "Coahuila",                 // 4
    "Nuevo Leon",               // 5
    "Tamaulipas",               // 6
    "Sinaloa",                  // 7
    "Durango",                  // 8
    "Zacatecas",                // 9
    "San Luis Potosi",          // 10
    "Aguascalientes",           // 11
    "Nayarit",                  // 12
    "Jalisco",                  // 13
    "Guanajuato",               // 14
    "Queretaro",                // 15
    "Hidalgo",                  // 16
    "Colima",                   // 17
    "Michoacan",                // 18
    "Estado de Mexico",         // 19
    "Ciudad de Mexico",         // 20
    "Tlaxcala",                 // 21
    "Puebla",                   // 22
    "Veracruz",                 // 23
    "Morelos",                  // 24
    "Guerrero",                 // 25
    "Oaxaca",                   // 26
    "Chiapas",                  // 27
    "Tabasco",                  // 28
    "Campeche",                 // 29
    "Yucatan",                  // 30
    "Quintana Roo"              // 31
)

val botonesEstados: Array<Pair<Int, Int>> = arrayOf(
    Pair(175, 150), Pair(130, 60),  Pair(215, 70),  Pair(300, 90),
    Pair(380, 140), Pair(420, 180), Pair(445, 200), Pair(270, 200),
    Pair(320, 190), Pair(360, 220), Pair(410, 245), Pair(320, 250),
    Pair(340, 290), Pair(370, 250), Pair(395, 280), Pair(425, 290),
    Pair(445, 280), Pair(330, 320), Pair(380, 315), Pair(430, 315),
    Pair(450, 315), Pair(470, 315), Pair(470, 340), Pair(500, 320),
    Pair(450, 335), Pair(420, 360), Pair(510, 380), Pair(590, 380),
    Pair(590, 350), Pair(640, 325), Pair(660, 270), Pair(675, 310)
)

@Composable
fun FixedButton(x: Int, y: Int, isSelected: Boolean, clickFun: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .offset(y = y.dp, x = x.dp)
            .height(20.dp)
            .width(20.dp)
            .background(
                color = if (isSelected)
                    colorResource(id = R.color.principal_color)
                else
                    colorResource(id = R.color.white),
                shape = CircleShape
            )
            .border(
                width = 3.dp,
                color = colorResource(id = R.color.secondary_color),
                shape = CircleShape
            )
            .clickable {
                clickFun()
            }
    )
}

@Composable
fun SimpleRoute(q: SimpleRutaQuestion, nextQuestion: (score: Float) -> Any) {
    var start by remember { mutableStateOf<Int>(-1) }
    var end by remember { mutableStateOf<Int>(-1) }
    Column {
        Text(
            modifier = Modifier.width(350.dp).padding(16.dp),
            text = q.question,
            color = colorResource(id = R.color.principal_color),
            fontSize = 14.sp
        )
        LazyRow {
            item {
                Box(modifier = Modifier
                    .padding(10.dp)
                    .width(800.dp)
                    .height(800.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.mexico_pais),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(650.dp)
                            .height(650.dp)
                            .offset(x = 100.dp)
                    )
                    botonesEstados.forEachIndexed {
                        i, pair -> FixedButton(x = pair.first+30, y = pair.second + 70, i == start || i == end, fun() {
                            if(start == -1) {
                                start = i
                            } else if(start == i) {
                                start = -1
                            } else {
                                if(end == -1) {
                                    end = i
                                } else if(end == i) {
                                    end = -1
                                }
                            }
                        })
                    }
                    if(start != -1 && end != -1) {
                        val begin = botonesEstados[start]
                        val last = botonesEstados[end]
                        var px = begin.first + 10
                        var py = begin.second + 10
                        val w = abs(last.first - begin.first)
                        val h = abs(last.second - begin.second)
                        if(begin.first > last.first ) px = last.first + 10
                        if(begin.second > last.second) py = last.second + 10
                        Canvas(
                            modifier = Modifier
                                .offset(x = px.dp, y = py.dp)
                                .width(w.dp)
                                .height(h.dp)
                        ) {
                            var x1 = 0f
                            var y1 = 0f
                            var x2 = size.width
                            var y2 = size.height
                            if( begin.first > last.first ) {
                                x1 = size.width
                                x2 = 0f
                            }
                            if(begin.second > last.second) {
                                y1 = size.height
                                y2 = 0f
                            }
                            drawLine(
                                color = Color.Black,
                                start = Offset(x = x1, y = y1),
                                end = Offset(x = x2, y = y2),
                                strokeWidth = 20.0f,
                                cap = StrokeCap.Round
                            )
                        }
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
                                nextQuestion(
                                    if(start == q.start && end == q.end) 10.0f
                                    else if(start == q.start || end == q.end) 5.0f
                                    else 0.0f
                                )
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

val nodes = mutableStateListOf<Int>()
@Composable
fun ComplexRoute(q: ComplexRutaQuestion, nextQuestion: (score: Float) -> Any) {
    Column {
        Text(
            modifier = Modifier.width(350.dp).padding(16.dp),
            text = q.question,
            color = colorResource(id = R.color.principal_color),
            fontSize = 14.sp
        )
        LazyRow {
            item {
                Box(modifier = Modifier
                    .padding(10.dp)
                    .width(800.dp)
                    .height(800.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.mexico_pais),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(800.dp)
                            .height(800.dp)
                    )
                    botonesEstados.forEachIndexed {
                        i, pair -> FixedButton(x = pair.first, y = pair.second, nodes.contains(i), fun() {
                            if(nodes.contains(i)) nodes.remove(i)
                            else nodes.add(i)
                        })
                    }
                    if(nodes.size >= 2) {
                        for (j in 1..(nodes.size - 1)) {
                            val begin = botonesEstados[nodes[j-1]]
                            val last = botonesEstados[nodes[j]]
                            var px = begin.first + 10
                            var py = begin.second + 10
                            val w = abs(last.first - begin.first)
                            val h = abs(last.second - begin.second)
                            if(begin.first > last.first ) px = last.first + 10
                            if(begin.second > last.second) py = last.second + 10
                            Canvas(
                                modifier = Modifier
                                    .offset(x = px.dp, y = py.dp)
                                    .width(w.dp)
                                    .height(h.dp)
                            ) {
                                var x1 = 0f
                                var y1 = 0f
                                var x2 = size.width
                                var y2 = size.height
                                if( begin.first > last.first ) {
                                    x1 = size.width
                                    x2 = 0f
                                }
                                if(begin.second > last.second) {
                                    y1 = size.height
                                    y2 = 0f
                                }
                                drawLine(
                                    color = Color.Black,
                                    start = Offset(x = x1, y = y1),
                                    end = Offset(x = x2, y = y2),
                                    strokeWidth = 20.0f,
                                    cap = StrokeCap.Round
                                )
                            }
                        }
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
                                val errors = nodes.filter { a -> !q.nodes.contains(a) }.size
                                val corrects = nodes.filter { a -> q.nodes.contains(a) }.size
                                val sc: Float = ((corrects - errors).toFloat() / q.nodes.size.toFloat()) * 10.0f
                                nodes.clear()
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

fun nextQuestionRoute(score: Float, questions: RutasModel, questionId: Int) {
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
                    is SimpleRutaQuestion -> "Inicio: ${IndexedStates[q.start]}, fin: ${IndexedStates[q.end]}"
                    is ComplexRutaQuestion -> "Ruta: ${q.nodes.map { i -> IndexedStates[i] }.joinToString()}"
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
fun RouteQuestionComponent(questions: RutasModel, questionId: Int) {
    val question = questions.questions[questionId]
    when(question) {
        is SimpleRutaQuestion -> SimpleRoute(question as SimpleRutaQuestion, fun(score: Float) {
            nextQuestionRoute(score, questions, questionId)
        })
        is ComplexRutaQuestion -> ComplexRoute(question as ComplexRutaQuestion, fun(score: Float) {
            nextQuestionRoute(score, questions, questionId)
        })
        else -> Column {}
    }
}