package com.example.geoplus.fragments

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geoplus.R
import com.example.geoplus.ResumeActivity
import com.example.geoplus.global.GlobalState

@Composable
fun ResumeFragment() {
    val globalState: GlobalState = GlobalState.getInstance()
    var res: Float = 0.0f

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        globalState.getResults().forEach { resultModel ->
            Row {
                var totpoints = 0
                globalState.getResults().forEach { model ->
                    run {
                        res += (model.score * model.points)
                        totpoints += model.points
                    }
                }
                res /= totpoints

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
        GetStars(score = res.toDouble())
        Text(
            text = "Puntuacion final: %.2f".format(res),
            color = colorResource(id = R.color.white),
            fontSize = 14.sp
        )
    }
}

@Composable
fun ResumeFragmentCardGame() {
    val globalState: GlobalState = GlobalState.getInstance()
    val result = globalState.getCardResult()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
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
                    text = "Puntuacion: ${result?.score}",
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp
                )
            }
        }
        GetStars(score = result?.score?.toDouble()?:0.0 )
        Text(
            text = "Puntuacion final: %.2f".format(result?.score?:0.0f),
            color = colorResource(id = R.color.white),
            fontSize = 14.sp
        )
    }
}