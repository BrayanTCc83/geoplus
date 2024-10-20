package com.example.geoplus.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val nick: String,
    val name: String,
    val lastname1: String,
    val lastname2: String,
    val age: String,
    val email: String,
    val password: String
)