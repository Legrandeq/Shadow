package com.twojmod.shadow.module.impl.combat

import com.twojmod.shadow.module.Category
import com.twojmod.shadow.module.Module
import com.twojmod.shadow.setting.BooleanSetting
import com.twojmod.shadow.setting.NumberSetting

class KillAura : Module("KillAura", "Attacks entities around you automatically.", Category.COMBAT) {
    val range = NumberSetting("Range", 4.0, 3.0, 6.0, 0.1)
    val playersOnly = BooleanSetting("Players Only", true)

    init {
        addSetting(range)
        addSetting(playersOnly)
    }

    override fun onTick() {
        // Logika atakowania graczy
    }
}
