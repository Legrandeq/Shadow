package com.twojmod.shadow.gui

import com.twojmod.shadow.module.Category
import com.twojmod.shadow.module.ModuleManager
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW

class ClickGUIScreen : Screen(Text.literal("Shadow Client GUI")) {
    private val windowWidth = 450
    private val windowHeight = 280
    private var x = 0
    private var y = 0

    private var activeCategory = Category.COMBAT

    private val COLOR_BG = 0xEE1A1A1F.toInt()
    private val COLOR_SIDEBAR = 0xFF12121A.toInt()
    private val COLOR_ACCENT = 0xFF00D9FF.toInt()
    private val COLOR_ACCENT_BG = 0x2200D9FF.toInt()
    private val COLOR_TEXT = 0xFFFFFFFF.toInt()
    private val COLOR_TEXT_MUTED = 0xFFAAAAAA.toInt()
    private val COLOR_BORDER = 0x33FFFFFF.toInt()

    override fun init() {
        super.init()
        this.x = (this.width - this.windowWidth) / 2
        this.y = (this.height - this.windowHeight) / 2
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderBackground(context, mouseX, mouseY, delta)

        context.fill(x, y, x + windowWidth, y + windowHeight, COLOR_BG)
        context.drawBorder(x - 1, y - 1, windowWidth + 2, windowHeight + 2, COLOR_BORDER)

        val sidebarWidth = 110
        context.fill(x, y, x + sidebarWidth, y + windowHeight, COLOR_SIDEBAR)
        context.fill(x + sidebarWidth, y, x + sidebarWidth + 1, y + windowHeight, COLOR_BORDER)

        context.drawTextWithShadow(this.textRenderer, "SHADOW", x + 12, y + 15, COLOR_ACCENT)
        context.drawText(this.textRenderer, "v2.4.1", x + 60, y + 15, COLOR_TEXT_MUTED, false)

        var catY = y + 45
        context.drawText(this.textRenderer, "CATEGORIES", x + 12, catY - 15, 0xFF555555.toInt(), false)

        for (category in Category.entries) {
            val isActive = category == activeCategory
            
            if (isActive) {
                context.fill(x, catY, x + sidebarWidth, catY + 22, COLOR_ACCENT_BG)
                context.fill(x, catY, x + 3, catY + 22, COLOR_ACCENT)
                context.drawTextWithShadow(this.textRenderer, category.displayName, x + 15, catY + 7, COLOR_ACCENT)
            } else {
                val isHovered = mouseX in x..(x + sidebarWidth) && mouseY in catY..(catY + 22)
                val textColor = if (isHovered) COLOR_TEXT else COLOR_TEXT_MUTED
                
                if (isHovered) context.fill(x, catY, x + sidebarWidth, catY + 22, 0x11FFFFFF.toInt())
                
                context.drawTextWithShadow(this.textRenderer, category.displayName, x + 15, catY + 7, textColor)
            }
            catY += 26
        }

        val modX = x + sidebarWidth + 15
        var modY = y + 15
        
        context.drawTextWithShadow(this.textRenderer, activeCategory.displayName, modX, modY, COLOR_TEXT)
        context.drawText(this.textRenderer, "Configure modules", modX, modY + 12, COLOR_TEXT_MUTED, false)

        modY += 35
        val modules = ModuleManager.getModulesByCategory(activeCategory)
        for (module in modules) {
            renderModuleCard(context, module.name, modX, modY, module.isEnabled)
            modY += 50
        }

        super.render(context, mouseX, mouseY, delta)
    }

    private fun renderModuleCard(context: DrawContext, name: String, mX: Int, mY: Int, enabled: Boolean) {
        val cardWidth = windowWidth - 110 - 30
        val cardHeight = 40

        context.fill(mX, mY, mX + cardWidth, mY + cardHeight, 0x801A1A1F.toInt())
        context.drawBorder(mX, mY, cardWidth, cardHeight, if (enabled) 0x5500D9FF.toInt() else COLOR_BORDER)

        context.drawTextWithShadow(this.textRenderer, name, mX + 15, mY + 16, if (enabled) COLOR_TEXT else COLOR_TEXT_MUTED)

        val toggleX = mX + cardWidth - 45
        val toggleY = mY + 12
        
        context.fill(toggleX, toggleY, toggleX + 30, toggleY + 16, if (enabled) 0xCC00D9FF.toInt() else 0xFF333333.toInt())
        val knobX = if (enabled) toggleX + 16 else toggleX + 2
        context.fill(knobX, toggleY + 2, knobX + 12, toggleY + 14, COLOR_TEXT)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val sidebarWidth = 110
        if (mouseX >= x && mouseX <= x + sidebarWidth) {
            var catY = y + 45
            for (category in Category.entries) {
                if (mouseY >= catY && mouseY <= catY + 22) {
                    activeCategory = category
                    return true
                }
                catY += 26
            }
        }

        // Obsługa kliknięć w moduły (włączanie/wyłączanie)
        val modX = x + sidebarWidth + 15
        var modY = y + 50
        val cardWidth = windowWidth - 110 - 30
        val modules = ModuleManager.getModulesByCategory(activeCategory)
        
        for (module in modules) {
            val toggleX = modX + cardWidth - 45
            val toggleY = modY + 12
            
            if (mouseX >= toggleX && mouseX <= toggleX + 30 && mouseY >= toggleY && mouseY <= toggleY + 16) {
                module.toggle()
                return true
            }
            modY += 50
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT || keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close()
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun shouldPause(): Boolean {
        return false
    }
}
