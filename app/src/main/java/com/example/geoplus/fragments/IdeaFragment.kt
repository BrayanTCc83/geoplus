package com.example.geoplus.fragments

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.geoplus.R
import com.example.geoplus.models.Place

@Composable
fun RenderizeIdea(places: Array<Place>, ctx: Context) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
    ) {
        items(places) { place ->
            Log.d("LOG_GEOPLUS", "Lugar: ${place.description}, Imagen: ${place.image} -> ${ctx.resources.getIdentifier(place.image, "drawable", ctx.packageName)}")
            Image(
                painter = painterResource(ctx.resources.getIdentifier(place.image, "drawable", ctx.packageName)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(3/4f)
            )
            Text(
                text = place.description,
                color = colorResource(id = R.color.white),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )
        }
    }
}