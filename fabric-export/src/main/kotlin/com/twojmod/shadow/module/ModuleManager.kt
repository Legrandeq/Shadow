package com.twojmod.shadow.module

import com.twojmod.shadow.module.impl.combat.KillAura
import com.twojmod.shadow.module.impl.combat.Velocity
import com.twojmod.shadow.module.impl.movement.Flight
import com.twojmod.shadow.module.impl.render.Fullbright

object ModuleManager {
    val modules = mutableListOf<Module>()

    fun init() {
        modules.add(KillAura())
        modules.add(Velocity())
        modules.add(Flight())
        modules.add(Fullbright())
    }

    fun getModulesByCategory(category: Category): List<Module> {
        return modules.filter { it.category == category }
    }

    fun onTick() {
        for (module in modules) {
            if (module.isEnabled) {
                module.onTick()
            }
        }
    }

    fun onKeyPressed(key: Int) {
        for (module in modules) {
            if (module.keybind == key) {
                module.toggle()
            }
        }
    }
}
