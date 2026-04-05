package com.twojmod.shadow.gui

import com.twojmod.shadow.module.Category
import com.twojmod.shadow.module.Module
import com.twojmod.shadow.module.ModuleManager
import com.twojmod.shadow.setting.BooleanSetting
import com.twojmod.shadow.setting.ModeSetting
import com.twojmod.shadow.setting.NumberSetting
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW
import kotlin.math.round

class ClickGUIScreen : Screen(Text.literal("Shadow Client GUI")) {
    private val windowWidth = 450
    private val windowHeight = 280
    private var x = 0
    private var y = 0

    private var activeCategory = Category.COMBAT
    private var expandedModule: Module? = null
    private var draggingSlider: NumberSetting? = null
    private var scrollY = 0.0

    companion object Theme {
        var bg = 0xEE0F0F13.toInt()
        var sidebar = 0xFF09090B.toInt()
        var accent = 0xFF6366F1.toInt() // Indigo 500
        var accentBg = 0x226366F1.toInt()
        var accentHover = 0x336366F1.toInt()
        var accentToggle = 0xAA6366F1.toInt()
        var text = 0xFFF9FAFB.toInt()
        var textMuted = 0xFF9CA3AF.toInt()
        var border = 0x1AFFFFFF.toInt()
        var cardBg = 0x8018181B.toInt()
        var cardHover = 0x9927272A.toInt()
        var toggleBg = 0xFF27272A.toInt()
    }

    override fun init() {
        super.init()
        this.x = (this.width - this.windowWidth) / 2
        this.y = (this.height - this.windowHeight) / 2
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        this.renderBackground(context, mouseX, mouseY, delta)

        // Main Window Background
        context.fill(x, y, x + windowWidth, y + windowHeight, Theme.bg)
        context.drawBorder(x - 1, y - 1, windowWidth + 2, windowHeight + 2, Theme.border)

        // Sidebar
        val sidebarWidth = 110
        context.fill(x, y, x + sidebarWidth, y + windowHeight, Theme.sidebar)
        context.fill(x + sidebarWidth, y, x + sidebarWidth + 1, y + windowHeight, Theme.border)

        // Title
        context.drawTextWithShadow(this.textRenderer, "SHADOW", x + 12, y + 15, Theme.accent)
        context.drawText(this.textRenderer, "v2.4.1", x + 60, y + 15, Theme.textMuted, false)

        // Categories
        var catY = y + 45
        context.drawText(this.textRenderer, "CATEGORIES", x + 12, catY - 15, 0xFF555555.toInt(), false)

        for (category in Category.entries) {
            val isActive = category == activeCategory
            val isHovered = mouseX in x..(x + sidebarWidth) && mouseY in catY..(catY + 22)
            
            if (isActive) {
                val bgColor = if (isHovered) Theme.accentHover else Theme.accentBg
                context.fill(x, catY, x + sidebarWidth, catY + 22, bgColor)
                context.fill(x, catY, x + 3, catY + 22, Theme.accent)
                context.drawTextWithShadow(this.textRenderer, category.displayName, x + 15, catY + 7, Theme.accent)
            } else {
                val textColor = if (isHovered) Theme.text else Theme.textMuted
                
                if (isHovered) context.fill(x, catY, x + sidebarWidth, catY + 22, 0x1AFFFFFF.toInt())
                
                context.drawTextWithShadow(this.textRenderer, category.displayName, x + 15, catY + 7, textColor)
            }
            catY += 26
        }

        // Modules Area (Right Side)
        val modX = x + sidebarWidth + 15
        
        // Enable Scissor to clip scrolling modules inside the window
        // Note: Minecraft's enableScissor uses window coordinates, so we use the exact window bounds
        context.enableScissor(x + sidebarWidth + 1, y + 1, x + windowWidth - 1, y + windowHeight - 1)
        
        var modY = y + 15 + scrollY.toInt()
        
        context.drawTextWithShadow(this.textRenderer, activeCategory.displayName, modX, modY, Theme.text)
        context.drawText(this.textRenderer, "Configure modules", modX, modY + 12, Theme.textMuted, false)

        modY += 35
        val modules = ModuleManager.getModulesByCategory(activeCategory)
        for (module in modules) {
            val cardHeight = renderModuleCard(context, module, modX, modY, mouseX, mouseY)
            modY += cardHeight + 10
        }

        context.disableScissor()

        super.render(context, mouseX, mouseY, delta)
    }

    private fun renderModuleCard(context: DrawContext, module: Module, mX: Int, mY: Int, mouseX: Int, mouseY: Int): Int {
        val cardWidth = windowWidth - 110 - 30
        var cardHeight = 40
        val isExpanded = expandedModule == module

        if (isExpanded) {
            cardHeight += module.settings.size * 25 + 5
        }

        val isHovered = mouseX in mX..(mX + cardWidth) && mouseY in mY..(mY + cardHeight)

        // Card Background
        val bgColor = if (isHovered) Theme.cardHover else Theme.cardBg
        context.fill(mX, mY, mX + cardWidth, mY + cardHeight, bgColor)
        
        val borderColor = if (module.isEnabled) Theme.accent else if (isHovered) 0x55FFFFFF.toInt() else Theme.border
        context.drawBorder(mX, mY, cardWidth, cardHeight, borderColor)

        // Module Name
        context.drawTextWithShadow(this.textRenderer, module.name, mX + 15, mY + 16, if (module.isEnabled) Theme.text else Theme.textMuted)

        // Toggle Switch
        val toggleX = mX + cardWidth - 45
        val toggleY = mY + 12
        context.fill(toggleX, toggleY, toggleX + 30, toggleY + 16, if (module.isEnabled) Theme.accent else Theme.toggleBg)
        val knobX = if (module.isEnabled) toggleX + 16 else toggleX + 2
        context.fill(knobX, toggleY + 2, knobX + 12, toggleY + 14, Theme.text)

        // Render Settings if expanded
        if (isExpanded) {
            var setY = mY + 45
            for (setting in module.settings) {
                when (setting) {
                    is BooleanSetting -> {
                        context.drawTextWithShadow(this.textRenderer, setting.name, mX + 15, setY + 4, Theme.textMuted)
                        
                        // Distinct Boolean Toggle
                        val setToggleWidth = 24
                        val setToggleHeight = 12
                        val setToggleX = mX + cardWidth - setToggleWidth - 15
                        val setToggleY = setY + 3
                        
                        val setBgColor = if (setting.value) Theme.accentToggle else Theme.toggleBg
                        context.fill(setToggleX, setToggleY, setToggleX + setToggleWidth, setToggleY + setToggleHeight, setBgColor)
                        
                        val setKnobX = if (setting.value) setToggleX + setToggleWidth - 10 else setToggleX + 2
                        context.fill(setKnobX, setToggleY + 2, setKnobX + 8, setToggleY + setToggleHeight - 2, Theme.text)
                    }
                    is NumberSetting -> {
                        context.drawTextWithShadow(this.textRenderer, setting.name, mX + 15, setY + 2, Theme.textMuted)
                        val valueStr = String.format("%.2f", setting.value)
                        context.drawTextWithShadow(this.textRenderer, valueStr, mX + cardWidth - 15 - this.textRenderer.getWidth(valueStr), setY + 2, Theme.text)
                        
                        val sliderX = mX + 15
                        val sliderY = setY + 16
                        val sliderWidth = cardWidth - 30
                        val sliderHeight = 4
                        
                        // Handle Dragging
                        if (draggingSlider == setting) {
                            val mousePercent = (mouseX - sliderX).toDouble() / sliderWidth
                            val clamped = mousePercent.coerceIn(0.0, 1.0)
                            val rawValue = setting.min + clamped * (setting.max - setting.min)
                            setting.value = round(rawValue / setting.increment) * setting.increment
                        }

                        // Draw Slider Background
                        context.fill(sliderX, sliderY, sliderX + sliderWidth, sliderY + sliderHeight, Theme.toggleBg)
                        
                        // Draw Slider Fill
                        val percent = (setting.value - setting.min) / (setting.max - setting.min)
                        val fillWidth = (percent * sliderWidth).toInt()
                        context.fill(sliderX, sliderY, sliderX + fillWidth, sliderY + sliderHeight, Theme.accent)
                        
                        // Draw Slider Knob
                        context.fill(sliderX + fillWidth - 3, sliderY - 2, sliderX + fillWidth + 3, sliderY + sliderHeight + 2, Theme.text)
                    }
                    is ModeSetting -> {
                        context.drawTextWithShadow(this.textRenderer, setting.name, mX + 15, setY + 4, Theme.textMuted)
                        val modeStr = setting.value
                        
                        val modeWidth = this.textRenderer.getWidth(modeStr)
                        val modeX = mX + cardWidth - 15 - modeWidth
                        
                        // Mode Background Box
                        context.fill(modeX - 4, setY + 2, modeX + modeWidth + 4, setY + 14, Theme.toggleBg)
                        context.drawBorder(modeX - 4, setY + 2, modeWidth + 8, 12, Theme.border)
                        
                        context.drawTextWithShadow(this.textRenderer, modeStr, modeX, setY + 4, Theme.accent)
                    }
                }
                setY += 25
            }
        }

        return cardHeight
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val sidebarWidth = 110
        
        // Sidebar Category Clicks
        if (mouseX >= x && mouseX <= x + sidebarWidth) {
            var catY = y + 45
            for (category in Category.entries) {
                if (mouseY >= catY && mouseY <= catY + 22) {
                    activeCategory = category
                    scrollY = 0.0 // Reset scroll when changing category
                    expandedModule = null
                    return true
                }
                catY += 26
            }
        }

        // Module & Settings Clicks
        val modX = x + sidebarWidth + 15
        var modY = y + 15 + scrollY.toInt()
        modY += 35 // offset for title
        
        val cardWidth = windowWidth - 110 - 30
        val modules = ModuleManager.getModulesByCategory(activeCategory)
        
        for (module in modules) {
            var cardHeight = 40
            val isExpanded = expandedModule == module
            if (isExpanded) {
                cardHeight += module.settings.size * 25 + 5
            }
            
            // Check if click is inside the module bounds
            if (mouseX >= modX && mouseX <= modX + cardWidth && mouseY >= modY && mouseY <= modY + cardHeight) {
                
                // Toggle Click
                val toggleX = modX + cardWidth - 45
                val toggleY = modY + 12
                if (mouseX >= toggleX && mouseX <= toggleX + 30 && mouseY >= toggleY && mouseY <= toggleY + 16) {
                    module.toggle()
                    return true
                }
                
                // Header Click (Expand/Collapse)
                if (mouseY <= modY + 40) {
                    expandedModule = if (isExpanded) null else module
                    return true
                }
                
                // Settings Clicks
                if (isExpanded) {
                    var setY = modY + 45
                    for (setting in module.settings) {
                        when (setting) {
                            is BooleanSetting -> {
                                if (mouseY >= setY && mouseY <= setY + 20) {
                                    setting.toggle()
                                    return true
                                }
                            }
                            is NumberSetting -> {
                                if (mouseY >= setY && mouseY <= setY + 25) {
                                    draggingSlider = setting
                                    return true
                                }
                            }
                            is ModeSetting -> {
                                if (mouseY >= setY && mouseY <= setY + 20) {
                                    setting.cycle()
                                    return true
                                }
                            }
                        }
                        setY += 25
                    }
                }
            }
            modY += cardHeight + 10
        }

        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        draggingSlider = null
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        scrollY += verticalAmount * 20
        if (scrollY > 0) scrollY = 0.0
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
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
