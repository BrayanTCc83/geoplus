package com.example.geoplus.models

import kotlinx.serialization.Serializable

@Serializable
public data class PlayerProgressModel(var completed: Int, var puntuations: FloatArray?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerProgressModel

        if (completed != other.completed) return false
        if (!puntuations.contentEquals(other.puntuations)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = completed
        result = 31 * result + puntuations.contentHashCode()
        return result
    }
};