package com.twojmod.shadow.setting

class NumberSetting(
    name: String,
    value: Double,
    val min: Double,
    val max: Double,
    val increment: Double
) : Setting<Double>(name, value)
