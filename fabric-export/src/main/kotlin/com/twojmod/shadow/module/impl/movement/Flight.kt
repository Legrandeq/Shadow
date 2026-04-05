package com.twojmod.shadow.module.impl.movement

import com.twojmod.shadow.module.Category
import com.twojmod.shadow.module.Module
import com.twojmod.shadow.setting.NumberSetting

class Flight : Module("Flight", "Allows you to fly like in creative mode.", Category.MOVEMENT) {
    val speed = NumberSetting("Speed", 1.0, 0.1, 5.0, 0.1)

    init {
        addSetting(speed)
    }

    override fun onTick() {
        mc.player?.abilities?.flying = true
    }

    override fun onDisable() {
        if (mc.player != null && !mc.player!!.isCreative) {
            mc.player?.abilities?.flying = false
        }
    }
}
