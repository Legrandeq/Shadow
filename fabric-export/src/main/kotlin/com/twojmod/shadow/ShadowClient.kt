package com.twojmod.shadow

import com.twojmod.shadow.gui.ClickGUIScreen
import com.twojmod.shadow.module.ModuleManager
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

class ShadowClient : ClientModInitializer {
    private lateinit var guiKeybind: KeyBinding

    override fun onInitializeClient() {
        println("[Shadow] Initializing Client...")
        
        // Inicjalizacja modułów
        ModuleManager.init()

        // Rejestracja prawego shifta
        guiKeybind = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "Open ClickGUI",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "Shadow Client"
            )
        )

        // Sprawdzanie kliknięcia co tick
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client ->
            while (guiKeybind.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(ClickGUIScreen())
                }
            }
        })
    }
}
