package com.twojmod.shadow.setting

class BooleanSetting(name: String, value: Boolean) : Setting<Boolean>(name, value) {
    fun toggle() {
        value = !value
    }
}
