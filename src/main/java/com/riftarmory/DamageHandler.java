package com.riftarmory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DamageHandler {

    public static void explode(ServerWorld world, Entity source, double x, double y, double z, float power) {
        world.createExplosion(source, x, y, z, power, World.ExplosionSourceType.MOB);
    }

    public static void chainLightning(ServerWorld world, ServerPlayerEntity user, LivingEntity first, int maxTargets, float damagePerHit) {
        strikeAt(world, first.getX(), first.getY(), first.getZ());
        first.damage(world, user.getDamageSources().lightningBolt(), damagePerHit);

        int hits = 1;
        Box area = Box.of(new Vec3d(first.getX(), first.getY(), first.getZ()), 10.0, 10.0, 10.0);
        for (Entity e : world.getOtherEntities(user, area)) {
            if (hits >= maxTargets) {
                break;
            }
            if (e instanceof LivingEntity target) {
                hits++;
                strikeAt(world, target.getX(), target.getY(), target.getZ());
                target.damage(world, user.getDamageSources().lightningBolt(), damagePerHit);
            }
        }
    }

    public static void rainSelfStrike(ServerWorld world, ServerPlayerEntity player) {
        BlockPos pos = player.getBlockPos();
        if (world.isRaining() && world.hasRain(pos)) {
            player.damage(world, world.getDamageSources().lightningBolt(), 4.0f);
        }
    }

    private static void strikeAt(ServerWorld world, double x, double y, double z) {
        LightningEntity bolt = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        bolt.setPosition(x, y, z);
        world.spawnEntity(bolt);
    }

    public static void broadcast(ServerWorld world, String message) {
        world.getServer().getPlayerManager().broadcast(Text.literal(message), false);
    }
}
