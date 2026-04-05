package com.twojmod.shadow.setting

class ModeSetting(name: String, val modes: List<String>, value: String) : Setting<String>(name, value) {
    fun cycle() {
        val index = modes.indexOf(value)
        value = modes[(index + 1) % modes.size]
    }
}
