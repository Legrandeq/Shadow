package com.twojmod.shadow.module.impl.misc

import com.twojmod.shadow.module.Category
import com.twojmod.shadow.module.Module

class AutoRespawn : Module("AutoRespawn", "Automatically respawns you after dying.", Category.MISC) {
    override fun onTick() {
        if (mc.player != null && mc.player!!.isDead) {
            mc.player!!.requestRespawn()
        }
    }
}
