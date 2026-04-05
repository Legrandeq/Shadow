package com.twojmod.shadow.utils

object MathUtils {
    fun clamp(value: Double, min: Double, max: Double): Double {
        return if (value < min) min else if (value > max) max else value
    }
}
