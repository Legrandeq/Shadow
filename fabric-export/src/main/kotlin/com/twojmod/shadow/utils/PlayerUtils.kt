package com.twojmod.shadow.utils

import net.minecraft.client.MinecraftClient

object PlayerUtils {
    fun isMoving(): Boolean {
        val player = MinecraftClient.getInstance().player ?: return false
        return player.forwardSpeed != 0f || player.sidewaysSpeed != 0f
    }
}
