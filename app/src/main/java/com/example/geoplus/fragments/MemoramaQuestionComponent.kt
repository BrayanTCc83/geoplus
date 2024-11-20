package com.example.geoplus.fragments

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoplus.R
import com.example.geoplus.ResumeActivity
import com.example.geoplus.global.GlobalState
import com.example.geoplus.models.CardMemo
import com.example.geoplus.models.MemoramaModel
import com.example.geoplus.models.ResultGameCard

fun generateUniqueRandomNumbers(): MutableList<Int> {
    return (0 until 32).toList().shuffled().take(9).toMutableList()
}

data class CardContext(
    val card: CardMemo,
    val ctx: Context,
    val isText: Boolean,
    val isCompleted: Boolean
)

@Composable
fun MemoramaQuestionComponent(questions: MemoramaModel, ctx: Context, onEnd: () -> Unit) {
    Log.d("LOG_GEOPLUS", "Rendering memorama ${questions.title}")
    val selected = remember { generateUniqueRandomNumbers() }
    val itemList = remember { mutableListOf<CardContext>() }
    val opens = remember { mutableSetOf<Pair<Int, Int>>() }
    val completes = remember { mutableListOf<Int>() }
    val tries = remember { mutableStateOf(0) }

    if(itemList.size == 0) {
        Log.d("LOG_GEOPLUS", "Agregando cartas")
        selected.forEach { i ->
            itemList.add(CardContext(questions.cards[i], ctx, false, false))
            itemList.add(CardContext(questions.cards[i], ctx, true, false))
        }
        itemList.shuffle()
    }

    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "Intentos: ${tries.value}",
            color = colorResource(id = R.color.principal_color),
            fontSize = 14.sp
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(itemList.size) { index: Int ->
                val item = itemList[index]
                CardMemoComponent(
                    index,
                    item.card,
                    item.ctx,
                    item.isText,
                    completes,
                    opens,
                    onClick = { i: Int, card: CardMemo ->
                        if(item.isCompleted || opens.size == 2)
                            return@CardMemoComponent

                        Log.d("LOG_GEOPLUS", "Open Card [$i]: Card with id = ${card.id}")

                        if(opens.indexOf(Pair(i, card.id)) == -1)
                            opens.add(Pair(i, card.id))
                        Log.d("LOG_GEOPLUS", "Opened cards: ${opens.toString()}")
                        if(opens.first().first != i && opens.first().second == card.id) {
                            completes.add(card.id)
                            opens.clear()
                            if(completes.size == itemList.size/2) {
                                Log.d("LOG_GEOPLUS", "Juego terminado, Intentos: ${tries.value} ${completes.size} ${itemList.size}")

                                val globalState = GlobalState.getInstance()
                                globalState.clearResult()
                                globalState.setCardResult(ResultGameCard(
                                    id = questions.id,
                                    score = 10f/tries.value * 18f,
                                    points = 10
                                ))

                                val intent = Intent(globalState.ctx, ResumeActivity::class.java)
                                globalState.ctx!!.startActivity(intent)
                                onEnd()
                            }
                        }
                    },
                    onClose = { i: Int, card: CardMemo ->
                        Log.d("LOG_GEOPLUS", "Eliminando carta $i ${card.id}")
                        opens.remove(Pair(i, card.id))
                        tries.value += 1
                    }
                )
            }
        }
    }
}