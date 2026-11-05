package com.riftarmory;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item RIFT_MACE = reg("rift_mace", new RiftMaceItem(new Item.Settings().sword(ToolMaterial.NETHERITE, 5, -2.4f, 0.0f)));
    public static final Item DRAGON_MACE = reg("dragon_mace", new DragonMaceItem(new Item.Settings().sword(ToolMaterial.NETHERITE, 5, -2.6f, 0.0f)));
    public static final Item LIGHTNING_MACE = reg("lightning_mace", new LightningMaceItem(new Item.Settings().sword(ToolMaterial.NETHERITE, 4, -2.4f, 0.0f)));
    public static final Item GHOST_MACE = reg("ghost_mace", new GhostMaceItem(new Item.Settings().sword(ToolMaterial.NETHERITE, 4, -2.4f, 0.0f)));
    public static final Item RIFT_SPEAR = reg("rift_spear", new RiftSpearItem(new Item.Settings().sword(ToolMaterial.NETHERITE, 4, -2.0f, 0.0f)));
    public static final Item DRAGON_SPEAR = reg("dragon_spear", new DragonSpearItem(new Item.Settings().sword(ToolMaterial.NETHERITE, 4, -2.2f, 0.0f)));
    public static final Item GHOST_SPEAR = reg("ghost_spear", new GhostSpearItem(new Item.Settings().sword(ToolMaterial.NETHERITE, 4, -2.0f, 0.0f)));
    public static final Item ANNIHILATION_STICK = reg("annihilation_stick", new AnnihilationStickItem(new Item.Settings().maxCount(1)));
    public static final Item RIFT_CHEST = reg("rift_chest", new RiftChestItem(new Item.Settings().maxCount(16)));
    public static final Item RIFT_ROD = reg("rift_rod", new Item(new Item.Settings()));
    public static final Item DRAGON_ROD = reg("dragon_rod", new Item(new Item.Settings()));
    public static final Item LIGHTNING_ROD = reg("lightning_rod", new Item(new Item.Settings()));
    public static final Item GHOST_ROD = reg("ghost_rod", new Item(new Item.Settings()));
    public static final Item RIFT_HEART = reg("rift_heart", new Item(new Item.Settings()));
    public static final Item GHOST_HEART = reg("ghost_heart", new Item(new Item.Settings()));

    private static Item reg(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of("riftarmory", name), item);
    }

    public static void register() {
    }
}
