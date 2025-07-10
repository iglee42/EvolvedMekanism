package fr.iglee42.emtools.registries;

import fr.iglee42.emtools.config.EvolvedMekanismToolsConfig;
import fr.iglee42.emtools.registries.EMToolsArmorMaterials;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.tools.common.item.*;
import mekanism.tools.common.material.BaseMekanismMaterial;
import mekanism.tools.common.material.MaterialCreator;
import mekanism.tools.common.material.VanillaPaxelMaterialCreator;
import net.minecraft.core.Holder;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;

import java.util.function.BiFunction;

public class EMToolsItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(EvolvedMekanism.MODID);

    public static final ItemRegistryObject<ItemMekanismPickaxe> BETTER_GOLD_PICKAXE = registerPickaxe(EvolvedMekanismToolsConfig.materials.betterGold);
    public static final ItemRegistryObject<ItemMekanismAxe> BETTER_GOLD_AXE = registerAxe(EvolvedMekanismToolsConfig.materials.betterGold);
    public static final ItemRegistryObject<ItemMekanismShovel> BETTER_GOLD_SHOVEL = registerShovel(EvolvedMekanismToolsConfig.materials.betterGold);
    public static final ItemRegistryObject<ItemMekanismHoe> BETTER_GOLD_HOE = registerHoe(EvolvedMekanismToolsConfig.materials.betterGold);
    public static final ItemRegistryObject<ItemMekanismSword> BETTER_GOLD_SWORD = registerSword(EvolvedMekanismToolsConfig.materials.betterGold);
    public static final ItemRegistryObject<ItemMekanismPaxel> BETTER_GOLD_PAXEL = registerPaxel(EvolvedMekanismToolsConfig.materials.betterGold);
    public static final ItemRegistryObject<ItemMekanismArmor> BETTER_GOLD_HELMET = registerArmor(EMToolsArmorMaterials.BETTER_GOLD, EvolvedMekanismToolsConfig.materials.betterGold, ArmorItem.Type.HELMET);
    public static final ItemRegistryObject<ItemMekanismArmor> BETTER_GOLD_CHESTPLATE = registerArmor(EMToolsArmorMaterials.BETTER_GOLD, EvolvedMekanismToolsConfig.materials.betterGold, ArmorItem.Type.CHESTPLATE);
    public static final ItemRegistryObject<ItemMekanismArmor> BETTER_GOLD_LEGGINGS = registerArmor(EMToolsArmorMaterials.BETTER_GOLD, EvolvedMekanismToolsConfig.materials.betterGold, ArmorItem.Type.LEGGINGS);
    public static final ItemRegistryObject<ItemMekanismArmor> BETTER_GOLD_BOOTS = registerArmor(EMToolsArmorMaterials.BETTER_GOLD, EvolvedMekanismToolsConfig.materials.betterGold, ArmorItem.Type.BOOTS);
    public static final ItemRegistryObject<ItemMekanismShield> BETTER_GOLD_SHIELD = registerShield(EvolvedMekanismToolsConfig.materials.betterGold);

    public static final ItemRegistryObject<ItemMekanismPickaxe> PLASLITHERITE_PICKAXE = registerPickaxe(EvolvedMekanismToolsConfig.materials.plaslitherite);
    public static final ItemRegistryObject<ItemMekanismAxe> PLASLITHERITE_AXE = registerAxe(EvolvedMekanismToolsConfig.materials.plaslitherite);
    public static final ItemRegistryObject<ItemMekanismShovel> PLASLITHERITE_SHOVEL = registerShovel(EvolvedMekanismToolsConfig.materials.plaslitherite);
    public static final ItemRegistryObject<ItemMekanismHoe> PLASLITHERITE_HOE = registerHoe(EvolvedMekanismToolsConfig.materials.plaslitherite);
    public static final ItemRegistryObject<ItemMekanismSword> PLASLITHERITE_SWORD = registerSword(EvolvedMekanismToolsConfig.materials.plaslitherite);
    public static final ItemRegistryObject<ItemMekanismPaxel> PLASLITHERITE_PAXEL = registerPaxel(EvolvedMekanismToolsConfig.materials.plaslitherite);
    public static final ItemRegistryObject<ItemMekanismArmor> PLASLITHERITE_HELMET = registerArmor(EMToolsArmorMaterials.PLASLITHERITE, EvolvedMekanismToolsConfig.materials.plaslitherite, ArmorItem.Type.HELMET);
    public static final ItemRegistryObject<ItemMekanismArmor> PLASLITHERITE_CHESTPLATE = registerArmor(EMToolsArmorMaterials.PLASLITHERITE, EvolvedMekanismToolsConfig.materials.plaslitherite, ArmorItem.Type.CHESTPLATE);
    public static final ItemRegistryObject<ItemMekanismArmor> PLASLITHERITE_LEGGINGS = registerArmor(EMToolsArmorMaterials.PLASLITHERITE, EvolvedMekanismToolsConfig.materials.plaslitherite, ArmorItem.Type.LEGGINGS);
    public static final ItemRegistryObject<ItemMekanismArmor> PLASLITHERITE_BOOTS = registerArmor(EMToolsArmorMaterials.PLASLITHERITE, EvolvedMekanismToolsConfig.materials.plaslitherite, ArmorItem.Type.BOOTS);
    public static final ItemRegistryObject<ItemMekanismShield> PLASLITHERITE_SHIELD = registerShield(EvolvedMekanismToolsConfig.materials.plaslitherite);

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
        return ITEMS.registerItem(material.getRegistryPrefix() + "_paxel", properties -> new ItemMekanismPaxel(material, properties));
    }

    private static ItemRegistryObject<ItemMekanismArmor> registerArmor(Holder<ArmorMaterial> armorMaterial, MaterialCreator material, ArmorItem.Type armorType) {
        return registerArmor(armorMaterial, material, armorType, ItemMekanismArmor::new);
    }

    private static ItemRegistryObject<ItemMekanismArmor> registerArmor(Holder<ArmorMaterial> armorMaterial, MaterialCreator material, ArmorItem.Type armorType, ArmorCreator armorCreator) {
        return ITEMS.register(material.getRegistryPrefix() + "_" + armorType.getName(), () -> armorCreator.create(armorMaterial, armorType, getBaseProperties(material)
                .durability(material.getDurabilityForType(armorType))));
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

        ItemMekanismArmor create(Holder<ArmorMaterial> material, ArmorItem.Type armorType, Item.Properties properties);
    }
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
