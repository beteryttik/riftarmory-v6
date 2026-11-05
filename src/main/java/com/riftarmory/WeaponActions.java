package com.riftarmory;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;

public class WeaponActions {

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            if (!(world instanceof ServerWorld serverWorld)) {
                return TypedActionResult.pass(stack);
            }
            if (!(player instanceof ServerPlayerEntity user)) {
                return TypedActionResult.pass(stack);
            }

            if (stack.isOf(ModItems.RIFT_MACE)) {
                riftMace(serverWorld, user);
                return TypedActionResult.success(stack, true);
            }
            if (stack.isOf(ModItems.DRAGON_SPEAR)) {
                boolean ok = dragonDash(serverWorld, user);
                return TypedActionResult.success(stack, ok);
            }
            if (stack.isOf(ModItems.GHOST_MACE)) {
                ghostMace(serverWorld, user);
                return TypedActionResult.success(stack, true);
            }
            if (stack.isOf(ModItems.LIGHTNING_MACE)) {
                lightningMace(serverWorld, user);
                return TypedActionResult.success(stack, true);
            }
            if (stack.isOf(ModItems.RIFT_SPEAR)) {
                riftSpear(serverWorld, user);
                return TypedActionResult.success(stack, true);
            }
            if (stack.isOf(ModItems.GHOST_SPEAR)) {
                ghostSpear(serverWorld, user);
                return TypedActionResult.success(stack, true);
            }
            if (stack.isOf(ModItems.ANNIHILATION_STICK)) {
                annihilation(serverWorld, user);
                return TypedActionResult.success(stack, true);
            }
            if (stack.isOf(ModItems.RIFT_CHEST)) {
                openRiftChest(serverWorld, user, stack);
                return TypedActionResult.success(stack, true);
            }
            return TypedActionResult.pass(stack);
        });
    }

    private static Vec3d aimPoint(ServerPlayerEntity user, double distance) {
        Vec3d look = user.getRotationVector();
        return new Vec3d(user.getX(), user.getY() + 1.0, user.getZ()).add(look.multiply(distance));
    }

    private static void riftMace(ServerWorld world, ServerPlayerEntity user) {
        Vec3d dest = aimPoint(user, 8.0);
        DamageHandler.explode(world, user, dest.x, dest.y, dest.z, 7.0f);
        world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1.0f, 0.8f);
    }

    private static boolean dragonDash(ServerWorld world, ServerPlayerEntity user) {
        if (!PlayerState.hasStability(user, 20)) {
            return false;
        }
        Vec3d look = user.getRotationVector().normalize();
        double sx = user.getX();
        double sy = user.getY() + 1.0;
        double sz = user.getZ();
        double dx = sx + look.x * 16.0;
        double dy = sy + look.y * 16.0;
        double dz = sz + look.z * 16.0;

        for (int i = 1; i <= 16; i++) {
            BlockPos bp = BlockPos.ofFloored(sx + look.x * i, sy + look.y * i, sz + look.z * i);
            if (!world.getBlockState(bp).isAir()) {
                return false;
            }
            if (!world.getFluidState(bp).isEmpty()) {
                return false;
            }
        }
        BlockPos dest = BlockPos.ofFloored(dx, dy, dz);
        if (!world.getBlockState(dest).isAir()) {
            return false;
        }
        if (!world.getFluidState(dest).isEmpty()) {
            return false;
        }
        if (dy < world.getBottomY() + 1) {
            return false;
        }

        PlayerState.addStability(user, -20);
        user.setPosition(dx, dy, dz);
        world.playSound(null, dest, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.2f);
        return true;
    }

    private static void ghostMace(ServerWorld world, ServerPlayerEntity user) {
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 200, 0));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 1));
        PlayerState.setGhostPhase(user, 200);
        world.playSound(null, user.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE.value(), SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    private static void lightningMace(ServerWorld world, ServerPlayerEntity user) {
        Vec3d center = aimPoint(user, 10.0);
        List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class,
                Box.of(new Vec3d(center.x, center.y, center.z), 12.0, 12.0, 12.0),
                e -> e != user && e.isAlive());
        if (targets.isEmpty()) {
            return;
        }
        targets.sort(Comparator.comparingDouble(t -> t.squaredDistanceTo(user)));
        DamageHandler.chainLightning(world, user, targets.get(0), 3, 8.0f);
        DamageHandler.rainSelfStrike(world, user);
    }

    private static void riftSpear(ServerWorld world, ServerPlayerEntity user) {
        Vec3d dest = aimPoint(user, 16.0);
        DamageHandler.explode(world, user, dest.x, dest.y, dest.z, 3.0f);
        world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1.0f, 1.4f);
    }

    private static void ghostSpear(ServerWorld world, ServerPlayerEntity user) {
        Vec3d center = new Vec3d(user.getX(), user.getY() + 1.0, user.getZ());
        List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class,
                Box.of(center, 24.0, 24.0, 24.0),
                e -> e != user && e.isAlive());
        if (targets.isEmpty()) {
            return;
        }
        targets.sort(Comparator.comparingDouble(t -> t.squaredDistanceTo(user)));
        LivingEntity closest = targets.get(0);
        closest.damage(world, user.getDamageSources().playerAttack(user), 12.0f);
        world.playSound(null, user.getBlockPos(), SoundEvents.PARTICLE_SOUL_ESCAPE.value(), SoundCategory.PLAYERS, 1.0f, 0.6f);
    }

    private static void annihilation(ServerWorld world, ServerPlayerEntity user) {
        Vec3d dest = aimPoint(user, 12.0);
        DamageHandler.explode(world, user, dest.x, dest.y, dest.z, 17.0f);
        DamageHandler.broadcast(world, "Палка Уничтожения приведена в действие!");
        world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1.0f, 0.5f);
    }

    private static void openRiftChest(ServerWorld world, ServerPlayerEntity user, ItemStack stack) {
        String[] loot = {
                "minecraft:diamond",
                "minecraft:amethyst_shard",
                "minecraft:emerald",
                "minecraft:blaze_rod",
                "minecraft:ender_pearl"
        };
        String pick = loot[world.random.nextInt(loot.length)];
        world.dropItem(new ItemStack(Registries.ITEM.get(Identifier.of(pick))), user.getX(), user.getY() + 1.0, user.getZ());
        stack.decrement(1);
        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }
}
