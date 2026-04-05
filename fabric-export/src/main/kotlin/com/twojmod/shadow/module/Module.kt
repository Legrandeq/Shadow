package com.twojmod.shadow.module

import com.twojmod.shadow.setting.Setting
import net.minecraft.client.MinecraftClient

abstract class Module(val name: String, val description: String, val category: Category) {
    var isEnabled = false
    var keybind: Int = -1
    val settings = mutableListOf<Setting<*>>()
    protected val mc: MinecraftClient = MinecraftClient.getInstance()

    open fun onEnable() {}
    open fun onDisable() {}
    open fun onTick() {}

    fun toggle() {
        isEnabled = !isEnabled
        if (isEnabled) {
            onEnable()
        } else {
            onDisable()
        }
    }

    protected fun addSetting(setting: Setting<*>) {
        settings.add(setting)
    }
}
