package com.twojmod.shadow.gui.hud

import com.twojmod.shadow.module.ModuleManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

object HudRenderer {
    fun render(context: DrawContext, tickDelta: Float) {
        val mc = MinecraftClient.getInstance()
        if (mc.options.hudHidden) return

        Watermark.render(context)

        // Renderowanie ArrayListy (aktywnych modułów)
        var y = 2
        val activeModules = ModuleManager.modules
            .filter { it.isEnabled }
            .sortedByDescending { mc.textRenderer.getWidth(it.name) }
            
        val screenWidth = context.scaledWindowWidth

        for (module in activeModules) {
            val text = module.name
            val textWidth = mc.textRenderer.getWidth(text)
            val x = screenWidth - textWidth - 4

            // Tło pod tekstem
            context.fill(x - 2, y - 1, screenWidth, y + 10, 0x80000000.toInt())
            // Tekst modułu
            context.drawTextWithShadow(mc.textRenderer, text, x, y, 0x00D9FF)
            y += 11
        }
    }
}
