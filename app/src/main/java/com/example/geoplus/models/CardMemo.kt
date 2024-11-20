package com.example.geoplus.models

import kotlinx.serialization.Serializable

@Serializable
data class CardMemo(
    val id: Int,
    val image: String? = null,
    val value: String? = null
)