package com.riftarmory;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerState {

    private static final Map<UUID, Integer> STABILITY = new HashMap<>();
    private static final Map<UUID, Integer> GHOST_PHASE = new HashMap<>();

    public static int getStability(ServerPlayerEntity p) {
        return STABILITY.getOrDefault(p.getUuid(), 100);
    }

    public static void addStability(ServerPlayerEntity p, int delta) {
        int value = getStability(p) + delta;
        if (value < 0) value = 0;
        if (value > 100) value = 100;
        STABILITY.put(p.getUuid(), value);
    }

    public static boolean hasStability(ServerPlayerEntity p, int cost) {
        return getStability(p) >= cost;
    }

    public static void setGhostPhase(ServerPlayerEntity p, int ticks) {
        GHOST_PHASE.put(p.getUuid(), ticks);
    }

    public static void tick(ServerPlayerEntity p) {
        if (p.age % 200 == 0) {
            addStability(p, 1);
        }
        Integer phase = GHOST_PHASE.get(p.getUuid());
        if (phase != null && phase > 0) {
            phase--;
            if (phase == 0) {
                p.setInvisible(false);
                GHOST_PHASE.remove(p.getUuid());
            } else {
                GHOST_PHASE.put(p.getUuid(), phase);
            }
        }
    }
}
