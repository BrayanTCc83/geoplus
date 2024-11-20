package com.example.geoplus.models;

import kotlinx.serialization.Serializable;

@Serializable
public data class GameCardModel(
        val id: Int,
        val image: String,
        val title: String,
        val levels: Int,
        val reference: String,
        var progress: PlayerProgressModel? = null
);