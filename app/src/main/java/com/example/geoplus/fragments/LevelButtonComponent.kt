package com.example.geoplus.fragments

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoplus.QuestionActivity
import com.example.geoplus.R
import com.example.geoplus.models.GameCardModel

fun OpenLevel(ctx: Context, file: String, type: String, id: Int) {
    val intent = Intent(ctx, QuestionActivity::class.java)
    intent.putExtra("file", file)
    intent.putExtra("type", type)
    intent.putExtra("question_id", id)
    ctx.startActivity(intent)
}

@Composable
fun LevelsButtons(ctx: Context, game: GameCardModel) {
    val textMod = Modifier.padding(10.dp)
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(game.levels) { i: Int ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            colorResource(
                                id = (if ((game.progress?.completed ?: 0) >= i) {
                                    R.color.principal_color
                                } else {
                                    R.color.dark_secondary
                                })
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            1.dp,
                            colorResource(id = R.color.secondary_color),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .width(70.dp)
                        .height(70.dp)
                        .clickable {
                            if ((game.progress?.completed ?: 0) < i) {
                                return@clickable
                            }

                            OpenLevel(
                                ctx = ctx,
                                file = game.reference
                                    .plus(i)
                                    .plus(".json"),
                                type = game.title,
                                id = i
                            )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painterResource(id = (if((game.progress?.completed?:0) > i) {
                            R.drawable.star_solid
                        } else if((game.progress?.completed?:0) == i) {
                            R.drawable.star_regular
                        } else {
                            R.drawable.lock
                        })),

                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(2.dp)
                            .width(16.dp)
                            .height(16.dp)
                    )
                    Text(
                        text = "${if((game.progress?.completed?:0) > i) {
                            game.progress!!.puntuations?.get(i)
                        } else if((game.progress?.completed?:0) == i) {
                            0.0
                        } else {
                            "Locked"
                        }}",
                        modifier = textMod,
                        color = colorResource(id = R.color.white),
                        fontSize = 14.sp,
                    )
                }
                Text(
                    text = "${i+1}",
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp,
                    modifier = textMod
                )
            }
        }
    }
}