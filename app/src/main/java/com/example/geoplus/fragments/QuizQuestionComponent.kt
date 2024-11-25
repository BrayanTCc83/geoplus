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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoplus.QuestionActivity
import com.example.geoplus.models.QuizCheckboxQuestionModel
import com.example.geoplus.models.QuizModel
import com.example.geoplus.models.QuizOpenQuestionModel
import com.example.geoplus.models.QuizRadioQuestionModel
import com.example.geoplus.R
import com.example.geoplus.ResumeActivity
import com.example.geoplus.global.GlobalState
import com.example.geoplus.models.ResultModel

val selected = mutableStateListOf<Int>()
@Composable
fun QuestionCheckbox(question: QuizCheckboxQuestionModel, nextQuestion: (score: Float) -> Any, onEnd: () -> Unit) {
    val ctx: Context? = GlobalState.getInstance().ctx

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Image(
                painterResource(id = (ctx?.resources?.getIdentifier(
                    question.image ?: "",
                    "drawable",
                    ctx.packageName
                ))?:0),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(300.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
            )
        }
        item {
            Text(
                text = question.question,
                color = colorResource(id = R.color.principal_color),
                fontSize = 14.sp
            )
        }
        itemsIndexed(question.options) {
            idx: Int, option: String -> Row(
                Modifier.clickable {
                    if (selected.contains(idx))
                        selected.remove(idx)
                    else
                        selected.add(idx)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selected.contains(idx),
                    colors = CheckboxColors(
                        checkedBoxColor = colorResource(id = R.color.principal_color),
                        checkedBorderColor = colorResource(id = R.color.secondary_color),
                        checkedCheckmarkColor = colorResource(id = R.color.white),
                        uncheckedBorderColor = colorResource(id = R.color.white),
                        uncheckedBoxColor = colorResource(id = R.color.dark_principal),
                        uncheckedCheckmarkColor = colorResource(id = R.color.dark_principal),
                        disabledCheckedBoxColor = colorResource(id = R.color.white),
                        disabledBorderColor = colorResource(id = R.color.white),
                        disabledIndeterminateBorderColor = colorResource(id = R.color.white),
                        disabledIndeterminateBoxColor = colorResource(id = R.color.white),
                        disabledUncheckedBorderColor = colorResource(id = R.color.white),
                        disabledUncheckedBoxColor = colorResource(id = R.color.white),
                    ),
                    onCheckedChange = {}
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
                    .width(300.dp)
                    .background(
                        color = colorResource(id = R.color.principal_color),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.secondary_color),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)
                    .clickable {
                        val errors = question.answer.filter { a -> !selected.contains(a) }.size
                        val corrects = question.answer.filter { a -> selected.contains(a) }.size
                        val sc: Float = ((corrects - errors).toFloat() / question.answer.size.toFloat()) * 10.0f
                        selected.clear()
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

@Composable
fun QuestionRadio(question: QuizRadioQuestionModel, nextQuestion: (score: Float) -> Any, onEnd: () -> Unit) {
    val ctx: Context? = GlobalState.getInstance().ctx

    var selected by remember { mutableStateOf<Int>(-1) }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Image(
                painterResource(id = (ctx?.resources?.getIdentifier(
                    question.image ?: "",
                    "drawable",
                    ctx.packageName
                ))?:0),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(300.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
        item {
            Text(
                text = question.question,
                color = colorResource(id = R.color.principal_color),
                fontSize = 14.sp
            )
        }
        itemsIndexed(question.options) {
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
                    .width(300.dp)
                    .background(
                        color = colorResource(id = R.color.principal_color),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.secondary_color),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)
                    .clickable { nextQuestion((10.0f / if (question.answer == selected) 1 else 2)) },
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

val tries = mutableStateListOf<String>()
@Composable
fun QuestionOpen(question: QuizOpenQuestionModel, nextQuestion: (score: Float) -> Any, onEnd: () -> Unit) {
    val ctx: Context? = GlobalState.getInstance().ctx
    var currentValue by remember { mutableStateOf(value = "") }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Image(
                painterResource(id = (ctx?.resources?.getIdentifier(
                    question.image ?: "",
                    "drawable",
                    ctx.packageName
                ))?:0),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(300.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
            )
        }
        item {
            Text(
                modifier = Modifier.width(350.dp),
                text = question.question,
                color = colorResource(id = R.color.principal_color),
                fontSize = 14.sp
            )
        }
        item {
            TextField(
                value = currentValue,
                onValueChange = { v -> currentValue = v },
                modifier = Modifier
                    .padding(8.dp)
                    .width(350.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = colorResource(id = R.color.principal_color),
                    focusedTextColor = colorResource(id = R.color.white),
                    unfocusedLabelColor = colorResource(id = R.color.principal_color),
                    unfocusedContainerColor = colorResource(id = R.color.dark_secondary),
                    focusedContainerColor = colorResource(id = R.color.dark_secondary)
                )
            )
        }
        items(tries) {
            t -> Row {
                Image(
                    painterResource(id = R.drawable.close_icon),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(2.dp)
                        .width(20.dp)
                        .height(20.dp)
                )
                Text(
                    text = t,
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp
                )
            }
        }
        item {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .background(
                        color = colorResource(id = R.color.principal_color),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(
                        width = 3.dp,
                        color = colorResource(id = R.color.secondary_color),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)
                    .clickable {
                        if (currentValue.isNotEmpty() && currentValue.isNotBlank()) {
                            if (question.answer.lowercase() == currentValue.lowercase()) {
                                val score = (10 / (tries.size + 1)).toFloat()
                                tries.clear()
                                nextQuestion(score)
                            } else if (tries.size == question.tries) {
                                tries.clear()
                                nextQuestion(0.0f)
                            } else {
                                tries.add(currentValue)
                                print(tries)
                                currentValue = ""
                            }
                        }
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

fun nextQuestion(score: Float, questions: QuizModel, questionId: Int, onEnd: () -> Unit) {
    val globalState: GlobalState = GlobalState.getInstance()
    if(globalState.ctx == null)
        return

    if(questions.questions.size > questionId) {
        val q = questions.questions[questionId]
        globalState.addResult(ResultModel(
            id = questionId + 1,
            score = if(score > 0) score else 0f,
            question = q.question,
            answer = when(q) {
                is QuizOpenQuestionModel -> q.answer
                is QuizRadioQuestionModel -> q.options[q.answer]
                is QuizCheckboxQuestionModel -> q.options.filterIndexed { i, a -> q.answer.contains(i) }.joinToString()
                else -> ""
            },
            points = q.points
        ))
        onEnd()
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
fun QuizQuestionComponent(questions: QuizModel, questionId: Int, onEnd: () -> Unit) {
    val question = questions.questions[questionId]
    when(question) {
        is QuizOpenQuestionModel -> QuestionOpen(question as QuizOpenQuestionModel, fun(score: Float) {
            nextQuestion(score, questions, questionId, onEnd)
        }, onEnd)
        is QuizRadioQuestionModel -> QuestionRadio(question as QuizRadioQuestionModel, fun(score: Float) {
            nextQuestion(score, questions, questionId, onEnd)
        }, onEnd)
        is QuizCheckboxQuestionModel -> QuestionCheckbox(question as QuizCheckboxQuestionModel, fun(score: Float) {
            nextQuestion(score, questions, questionId, onEnd)
        }, onEnd)
        else -> Column {}
    }
}