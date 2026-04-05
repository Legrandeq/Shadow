package com.twojmod.shadow.module.impl.combat

import com.twojmod.shadow.module.Category
import com.twojmod.shadow.module.Module
import com.twojmod.shadow.setting.NumberSetting

class Velocity : Module("Velocity", "Modifies knockback received.", Category.COMBAT) {
    val horizontal = NumberSetting("Horizontal", 0.0, 0.0, 100.0, 1.0)
    val vertical = NumberSetting("Vertical", 0.0, 0.0, 100.0, 1.0)

    init {
        addSetting(horizontal)
        addSetting(vertical)
    }
}
