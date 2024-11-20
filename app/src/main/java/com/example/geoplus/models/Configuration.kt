package com.example.geoplus.models

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    var darkmode: Boolean
)

public val DefaultConfiguration: Configuration = Configuration(
    true
)