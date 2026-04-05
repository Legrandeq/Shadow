package com.twojmod.shadow.module.impl.world

import com.twojmod.shadow.module.Category
import com.twojmod.shadow.module.Module
import com.twojmod.shadow.setting.NumberSetting

class FastPlace : Module("FastPlace", "Allows you to place blocks faster.", Category.WORLD) {
    val delay = NumberSetting("Delay", 0.0, 0.0, 4.0, 1.0)

    init {
        addSetting(delay)
    }
}
