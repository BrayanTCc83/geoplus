package com.example.geoplus.models

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val image: String,
    val description: String
)

@Serializable
data class Idea(
    val country: String,
    val places: Array<Place>
)