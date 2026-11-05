package com.riftarmory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class Riftarmory implements ModInitializer {

    public void onInitialize() {
        ModItems.register();
        ModCreativeTabs.register();
        WeaponActions.register();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList()) {
                PlayerState.tick(p);
            }
        });
    }
}
