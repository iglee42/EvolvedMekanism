package fr.iglee42.emtools.registries;

import fr.iglee42.emtools.config.EMToolsConfig;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import mekanism.api.Upgrade;
import mekanism.api.text.TextComponentUtil;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.item.ItemAlloy;
import mekanism.common.item.ItemQIODrive;
import mekanism.common.item.ItemTierInstaller;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.resource.IResource;
import mekanism.common.resource.ResourceType;
import mekanism.common.tier.QIODriveTier;
import mekanism.tools.common.config.MekanismToolsConfig;
import mekanism.tools.common.item.*;
import mekanism.tools.common.material.BaseMekanismMaterial;
import mekanism.tools.common.material.MaterialCreator;
import mekanism.tools.common.material.VanillaPaxelMaterialCreator;
import mekanism.tools.common.registries.ToolsItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.BiFunction;

public class EMToolsItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(EvolvedMekanism.MODID);

    public static final ItemRegistryObject<ItemMekanismPickaxe> BETTER_GOLD_PICKAXE = registerPickaxe(EMToolsConfig.tools.betterGold);
    public static final ItemRegistryObject<ItemMekanismAxe> BETTER_GOLD_AXE = registerAxe(EMToolsConfig.tools.betterGold);
    public static final ItemRegistryObject<ItemMekanismShovel> BETTER_GOLD_SHOVEL = registerShovel(EMToolsConfig.tools.betterGold);
    public static final ItemRegistryObject<ItemMekanismHoe> BETTER_GOLD_HOE = registerHoe(EMToolsConfig.tools.betterGold);
    public static final ItemRegistryObject<ItemMekanismSword> BETTER_GOLD_SWORD = registerSword(EMToolsConfig.tools.betterGold);
    public static final ItemRegistryObject<ItemMekanismPaxel> BETTER_GOLD_PAXEL = registerPaxel(EMToolsConfig.tools.betterGold);
    public static final ItemRegistryObject<ItemMekanismArmor> BETTER_GOLD_HELMET = registerArmor(EMToolsConfig.tools.betterGold, ArmorItem.Type.HELMET);
    public static final ItemRegistryObject<ItemMekanismArmor> BETTER_GOLD_CHESTPLATE = registerArmor(EMToolsConfig.tools.betterGold, ArmorItem.Type.CHESTPLATE);
    public static final ItemRegistryObject<ItemMekanismArmor> BETTER_GOLD_LEGGINGS = registerArmor(EMToolsConfig.tools.betterGold, ArmorItem.Type.LEGGINGS);
    public static final ItemRegistryObject<ItemMekanismArmor> BETTER_GOLD_BOOTS = registerArmor(EMToolsConfig.tools.betterGold, ArmorItem.Type.BOOTS);
    public static final ItemRegistryObject<ItemMekanismShield> BETTER_GOLD_SHIELD = registerShield(EMToolsConfig.tools.betterGold);

    public static final ItemRegistryObject<ItemMekanismPickaxe> PLASLITHERITE_PICKAXE = registerPickaxe(EMToolsConfig.tools.plaslitherite);
    public static final ItemRegistryObject<ItemMekanismAxe> PLASLITHERITE_AXE = registerAxe(EMToolsConfig.tools.plaslitherite);
    public static final ItemRegistryObject<ItemMekanismShovel> PLASLITHERITE_SHOVEL = registerShovel(EMToolsConfig.tools.plaslitherite);
    public static final ItemRegistryObject<ItemMekanismHoe> PLASLITHERITE_HOE = registerHoe(EMToolsConfig.tools.plaslitherite);
    public static final ItemRegistryObject<ItemMekanismSword> PLASLITHERITE_SWORD = registerSword(EMToolsConfig.tools.plaslitherite);
    public static final ItemRegistryObject<ItemMekanismPaxel> PLASLITHERITE_PAXEL = registerPaxel(EMToolsConfig.tools.plaslitherite);
    public static final ItemRegistryObject<ItemMekanismArmor> PLASLITHERITE_HELMET = registerArmor(EMToolsConfig.tools.plaslitherite, ArmorItem.Type.HELMET);
    public static final ItemRegistryObject<ItemMekanismArmor> PLASLITHERITE_CHESTPLATE = registerArmor(EMToolsConfig.tools.plaslitherite, ArmorItem.Type.CHESTPLATE);
    public static final ItemRegistryObject<ItemMekanismArmor> PLASLITHERITE_LEGGINGS = registerArmor(EMToolsConfig.tools.plaslitherite, ArmorItem.Type.LEGGINGS);
    public static final ItemRegistryObject<ItemMekanismArmor> PLASLITHERITE_BOOTS = registerArmor(EMToolsConfig.tools.plaslitherite, ArmorItem.Type.BOOTS);
    public static final ItemRegistryObject<ItemMekanismShield> PLASLITHERITE_SHIELD = registerShield(EMToolsConfig.tools.plaslitherite);

    public static final ItemRegistryObject<ItemMekanismPickaxe> REFINED_REDSTONE_PICKAXE = registerPickaxe(EMToolsConfig.tools.refinedRedstone);
    public static final ItemRegistryObject<ItemMekanismAxe> REFINED_REDSTONE_AXE = registerAxe(EMToolsConfig.tools.refinedRedstone);
    public static final ItemRegistryObject<ItemMekanismShovel> REFINED_REDSTONE_SHOVEL = registerShovel(EMToolsConfig.tools.refinedRedstone);
    public static final ItemRegistryObject<ItemMekanismHoe> REFINED_REDSTONE_HOE = registerHoe(EMToolsConfig.tools.refinedRedstone);
    public static final ItemRegistryObject<ItemMekanismSword> REFINED_REDSTONE_SWORD = registerSword(EMToolsConfig.tools.refinedRedstone);
    public static final ItemRegistryObject<ItemMekanismPaxel> REFINED_REDSTONE_PAXEL = registerPaxel(EMToolsConfig.tools.refinedRedstone);
    public static final ItemRegistryObject<ItemMekanismArmor> REFINED_REDSTONE_HELMET = registerArmor(EMToolsConfig.tools.refinedRedstone, ArmorItem.Type.HELMET);
    public static final ItemRegistryObject<ItemMekanismArmor> REFINED_REDSTONE_CHESTPLATE = registerArmor(EMToolsConfig.tools.refinedRedstone, ArmorItem.Type.CHESTPLATE);
    public static final ItemRegistryObject<ItemMekanismArmor> REFINED_REDSTONE_LEGGINGS = registerArmor(EMToolsConfig.tools.refinedRedstone, ArmorItem.Type.LEGGINGS);
    public static final ItemRegistryObject<ItemMekanismArmor> REFINED_REDSTONE_BOOTS = registerArmor(EMToolsConfig.tools.refinedRedstone, ArmorItem.Type.BOOTS);
    public static final ItemRegistryObject<ItemMekanismShield> REFINED_REDSTONE_SHIELD = registerShield(EMToolsConfig.tools.refinedRedstone);


    private static ItemRegistryObject<ItemMekanismShield> registerShield(MaterialCreator material) {
        return register(ItemMekanismShield::new, "_shield", material);
    }

    private static ItemRegistryObject<ItemMekanismPickaxe> registerPickaxe(MaterialCreator material) {
        return register(ItemMekanismPickaxe::new, "_pickaxe", material);
    }

    private static ItemRegistryObject<ItemMekanismAxe> registerAxe(MaterialCreator material) {
        return register(ItemMekanismAxe::new, "_axe", material);
    }

    private static ItemRegistryObject<ItemMekanismShovel> registerShovel(MaterialCreator material) {
        return register(ItemMekanismShovel::new, "_shovel", material);
    }

    private static ItemRegistryObject<ItemMekanismHoe> registerHoe(MaterialCreator material) {
        return register(ItemMekanismHoe::new, "_hoe", material);
    }

    private static ItemRegistryObject<ItemMekanismSword> registerSword(MaterialCreator material) {
        return register(ItemMekanismSword::new, "_sword", material);
    }

    private static ItemRegistryObject<ItemMekanismPaxel> registerPaxel(MaterialCreator material) {
        return register(ItemMekanismPaxel::new, "_paxel", material);
    }

    private static ItemRegistryObject<ItemMekanismPaxel> registerPaxel(VanillaPaxelMaterialCreator material) {
        if (material.getVanillaTier() == Tiers.NETHERITE) {
            return ITEMS.registerUnburnable(material.getRegistryPrefix() + "_paxel", properties -> new ItemMekanismPaxel(material, properties));
        }
        return ITEMS.register(material.getRegistryPrefix() + "_paxel", properties -> new ItemMekanismPaxel(material, properties));
    }

    private static ItemRegistryObject<ItemMekanismArmor> registerArmor(MaterialCreator material, ArmorItem.Type armorType) {
        return registerArmor(material, armorType, ItemMekanismArmor::new);
    }

    private static ItemRegistryObject<ItemMekanismArmor> registerArmor(MaterialCreator material, ArmorItem.Type armorType, ArmorCreator armorCreator) {
        return ITEMS.register(material.getRegistryPrefix() + "_" + armorType.getName(), () -> armorCreator.create(material, armorType, getBaseProperties(material)));
    }

    private static <ITEM extends Item> ItemRegistryObject<ITEM> register(BiFunction<MaterialCreator, Item.Properties, ITEM> itemCreator, String suffix,
                                                                         MaterialCreator material) {
        return ITEMS.register(material.getRegistryPrefix() + suffix, () -> itemCreator.apply(material, getBaseProperties(material)));
    }

    private static Item.Properties getBaseProperties(BaseMekanismMaterial material) {
        Item.Properties properties = new Item.Properties();
        if (!material.burnsInFire()) {
            properties = properties.fireResistant();
        }
        return properties;
    }

    @FunctionalInterface
    private interface ArmorCreator {

        ItemMekanismArmor create(MaterialCreator material, ArmorItem.Type armorType, Item.Properties properties);
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
