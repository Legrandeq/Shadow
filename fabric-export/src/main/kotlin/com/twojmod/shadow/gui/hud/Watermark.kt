package com.twojmod.shadow.gui.hud

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

object Watermark {
    fun render(context: DrawContext) {
        val mc = MinecraftClient.getInstance()
        context.drawTextWithShadow(mc.textRenderer, "Shadow v2.4.1", 4, 4, 0x00D9FF)
    }
}
