package com.twojmod.shadow.module.impl.player

import com.twojmod.shadow.module.Category
import com.twojmod.shadow.module.Module

class AutoSprint : Module("AutoSprint", "Automatically sprints for you.", Category.PLAYER) {
    override fun onTick() {
        if (mc.player != null && mc.player!!.forwardSpeed > 0 && !mc.player!!.isSneaking && !mc.player!!.horizontalCollision) {
            mc.player!!.isSprinting = true
        }
    }
}
