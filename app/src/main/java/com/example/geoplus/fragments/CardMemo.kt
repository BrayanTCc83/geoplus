package com.example.geoplus.fragments

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoplus.R
import com.example.geoplus.models.CardMemo
import kotlinx.coroutines.delay

@Composable
fun CardMemoComponent(index: Int, card: CardMemo, ctx: Context, text: Boolean, completes: MutableList<Int>, opens: MutableSet<Pair<Int,Int>>, onClick: (Int, CardMemo) -> Unit, onClose: (Int, CardMemo) -> Unit) {
    val resImage = remember { mutableStateOf(R.drawable.question_mark) }
    val resTexto = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = resImage.value, resTexto.value) {
        delay(2500L)
        if (resImage.value != R.drawable.question_mark && card.id !in completes) {
            resImage.value = R.drawable.question_mark
            onClose(index, card)
        } else if(resTexto.value && card.id !in completes) {
            resTexto.value = false
            onClose(index, card)
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .background(
                    colorResource(id = R.color.semitransparent),
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    1.dp,
                    colorResource(id = R.color.secondary_color),
                    shape = RoundedCornerShape(10.dp)
                )
                .width(100.dp)
                .height(125.dp)
                .clickable {
                    if (opens.size == 2 || card.id in completes)
                        return@clickable
                    onClick(index, card)
                    if (text) {
                        resTexto.value = true
                    } else {
                        resImage.value =
                            ctx.resources.getIdentifier(
                                card.image ?: "",
                                "drawable",
                                ctx.packageName
                            )
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if(resTexto.value) {
                Text(
                    card.value?:"",
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Image(
                    painterResource(id = resImage.value),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(2.dp)
                        .width(60.dp)
                        .height(60.dp)
                )
            }
        }
    }
}