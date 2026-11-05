package com.riftarmory;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModCreativeTabs {

    public static final RegistryKey<ItemGroup> RIFT_TAB_KEY = RegistryKey.of(
            Registries.ITEM_GROUP.getKey(), Identifier.of("riftarmory", "rift_armory"));

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, RIFT_TAB_KEY, FabricItemGroup.builder()
                .icon(() -> new ItemStack(ModItems.RIFT_MACE))
                .displayName(Text.literal("Rift Armory"))
                .build());

        ItemGroupEvents.modifyEntriesEvent(RIFT_TAB_KEY).register(entries -> {
            entries.add(ModItems.RIFT_MACE);
            entries.add(ModItems.DRAGON_MACE);
            entries.add(ModItems.LIGHTNING_MACE);
            entries.add(ModItems.GHOST_MACE);
            entries.add(ModItems.RIFT_SPEAR);
            entries.add(ModItems.DRAGON_SPEAR);
            entries.add(ModItems.GHOST_SPEAR);
            entries.add(ModItems.ANNIHILATION_STICK);
            entries.add(ModItems.RIFT_CHEST);
            entries.add(ModItems.RIFT_ROD);
            entries.add(ModItems.DRAGON_ROD);
            entries.add(ModItems.LIGHTNING_ROD);
            entries.add(ModItems.GHOST_ROD);
            entries.add(ModItems.RIFT_HEART);
            entries.add(ModItems.GHOST_HEART);
        });
    }
}
