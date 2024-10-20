package com.example.geoplus.models

data class ResultModel(
    val id: Int,
    val score: Float,
    val question: String,
    val answer: Comparable<*>,
    val points: Int
)