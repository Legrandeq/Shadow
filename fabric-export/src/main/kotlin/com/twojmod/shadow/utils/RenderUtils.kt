package com.twojmod.shadow.utils

import net.minecraft.client.gui.DrawContext

object RenderUtils {
    fun drawRect(context: DrawContext, x: Int, y: Int, width: Int, height: Int, color: Int) {
        context.fill(x, y, x + width, y + height, color)
    }
}
