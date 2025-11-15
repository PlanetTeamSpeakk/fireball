package com.ptsmods.fireball.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.ptsmods.fireball.FireballMod;

@Mod(FireballMod.MOD_ID)
public final class FireballModForge {
    public FireballModForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(FireballMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        FireballMod.init();
    }
}
