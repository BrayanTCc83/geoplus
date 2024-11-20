package com.example.geoplus.fragments

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.example.geoplus.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoplus.LevelsActivity
import com.example.geoplus.models.GameCardModel
import kotlin.math.roundToInt

fun openGameLevels(ctx: Context, game: GameCardModel, score: Double) {
    val intent = Intent(ctx, LevelsActivity::class.java)
    intent.putExtra("title", game.title)
    intent.putExtra("reference", game.reference)
    intent.putExtra("levels", game.levels)
    intent.putExtra("score", score)
    ctx.startActivity(intent)
}

@Composable
fun PaintImage(score: Double, s: Double) {
    val imageMod = Modifier
        .padding(2.dp)
        .width(16.dp)
        .height(16.dp)
    val icon = if(score >= s)
        R.drawable.star_solid
    else if((s - score) < 2 && (s - score) > 0.5)
        R.drawable.star_half
    else
        R.drawable.star_regular
    Image(painterResource(id = icon), contentDescription = null, contentScale = ContentScale.Fit, modifier = imageMod)
}

@Composable
fun GetStars(score: Double) {
    Row {
        PaintImage(score = score, s = 2.0)
        PaintImage(score = score, s = 4.0)
        PaintImage(score = score, s = 6.0)
        PaintImage(score = score, s = 8.0)
        PaintImage(score = score, s = 10.0)
    }
}

@Composable
fun GameCardComponent(gameCard: GameCardModel, ctx: Context) {
    val textMod = Modifier
        .padding(top = 10.dp, bottom = 10.dp)
    val score = if(gameCard.progress?.puntuations != null) {
        gameCard.progress!!.puntuations!!.average()
    } else {
        0.0
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .background(
                colorResource(id = R.color.principal_color),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                3.dp,
                colorResource(id = R.color.secondary_color),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
               openGameLevels(ctx = ctx, game = gameCard, score = score)
            },
    ) {
        Image(
            painterResource(id = ctx.resources.getIdentifier(gameCard.image, "drawable", ctx.packageName)),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(5.dp)
                .width(100.dp)
                .height(100.dp)
        )
        Text(
            text = gameCard.title.uppercase(),
            color = colorResource(id = R.color.white),
            fontSize = 16.sp,
            modifier = textMod
        )
        GetStars(
            score = score
        )
        Text(
            text = "${gameCard.progress?.completed?:0}/${gameCard.levels}",
            modifier = textMod,
            color = colorResource(id = R.color.white),
            fontSize = 14.sp,
        )
    }
}