package com.twojmod.shadow.module

import com.twojmod.shadow.module.impl.combat.KillAura
import com.twojmod.shadow.module.impl.combat.Velocity
import com.twojmod.shadow.module.impl.exploits.Disabler
import com.twojmod.shadow.module.impl.misc.AutoRespawn
import com.twojmod.shadow.module.impl.movement.Flight
import com.twojmod.shadow.module.impl.player.AutoSprint
import com.twojmod.shadow.module.impl.render.Fullbright
import com.twojmod.shadow.module.impl.world.FastPlace

object ModuleManager {
    val modules = mutableListOf<Module>()

    fun init() {
        // Combat
        modules.add(KillAura())
        modules.add(Velocity())
        
        // Movement
        modules.add(Flight())
        
        // Render
        modules.add(Fullbright())
        
        // World
        modules.add(FastPlace())
        
        // Player
        modules.add(AutoSprint())
        
        // Misc
        modules.add(AutoRespawn())
        
        // Exploits
        modules.add(Disabler())
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
