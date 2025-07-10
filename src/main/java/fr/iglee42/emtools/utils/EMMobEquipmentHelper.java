package fr.iglee42.emtools.utils;

import fr.iglee42.emtools.config.EMToolsMaterialConfig;
import fr.iglee42.emtools.config.EMToolsConfig.ArmorSpawnChanceConfig;
import fr.iglee42.emtools.config.EvolvedMekanismToolsConfig;
import fr.iglee42.emtools.registries.EMToolsItems;
import mekanism.common.config.value.CachedFloatValue;
import mekanism.tools.common.config.MekanismToolsConfig;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

public class EMMobEquipmentHelper {

    private static final GearType BETTER_GOLD = new GearType(EMToolsItems.BETTER_GOLD_SWORD, EMToolsItems.BETTER_GOLD_SHOVEL,
          EMToolsItems.BETTER_GOLD_HELMET, EMToolsItems.BETTER_GOLD_CHESTPLATE, EMToolsItems.BETTER_GOLD_LEGGINGS, EMToolsItems.BETTER_GOLD_BOOTS,
          EvolvedMekanismToolsConfig.tools.betterGoldSpawnRate);
    private static final GearType PLASLITHERITE = new GearType(EMToolsItems.PLASLITHERITE_SWORD, EMToolsItems.PLASLITHERITE_SHOVEL, EMToolsItems.PLASLITHERITE_HELMET,
          EMToolsItems.PLASLITHERITE_CHESTPLATE, EMToolsItems.PLASLITHERITE_LEGGINGS, EMToolsItems.PLASLITHERITE_BOOTS, EvolvedMekanismToolsConfig.tools.plaslitheriteSpawnRate);
    private static final GearType REFINED_REDSTONE = new GearType(EMToolsItems.REFINED_REDSTONE_SWORD, EMToolsItems.PLASLITHERITE_SHOVEL, EMToolsItems.PLASLITHERITE_HELMET,
            EMToolsItems.PLASLITHERITE_CHESTPLATE, EMToolsItems.PLASLITHERITE_LEGGINGS, EMToolsItems.PLASLITHERITE_BOOTS, EvolvedMekanismToolsConfig.tools.plaslitheriteSpawnRate);

    private static boolean isZombie(LivingEntity entity) {
        //Ignore the specific subclasses that can't spawn with armor in vanilla
        return entity instanceof Zombie && !(entity instanceof Drowned) && !(entity instanceof ZombifiedPiglin);
    }

    public static void onLivingSpecialSpawn(FinalizeSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        boolean isZombie = isZombie(entity);
        if (isZombie || entity instanceof Skeleton || entity instanceof Stray || entity instanceof Piglin) {
            //Don't bother calculating random numbers unless the instanceof checks pass
            RandomSource random = event.getLevel().getRandom();
            DifficultyInstance difficulty = event.getDifficulty();
            boolean isHard = difficulty.getDifficulty() == Difficulty.HARD;
            float difficultyMultiplier = difficulty.getSpecialMultiplier();
            GearType gearType = null;
            if (random.nextFloat() < MekanismToolsConfig.tools.armorSpawnChance.get() * difficultyMultiplier) {
                //We can only spawn refined glowstone armor on piglins
                gearType = getGearType(entity instanceof Piglin ? 0 : random.nextInt(3));
                setEntityArmorWithChance(random, entity, isHard, difficulty, gearType);
            }
            if (isZombie) {
                CachedFloatValue spawnChance = isHard ? MekanismToolsConfig.tools.weaponSpawnChanceHard : MekanismToolsConfig.tools.weaponSpawnChance;
                if (random.nextFloat() < spawnChance.get()) {
                    if (gearType == null) {
                        gearType = getGearType(random.nextInt(3));
                    }
                    if (gearType.spawnChance.canSpawnWeapon.get()) {
                        Holder<Item> weapon = random.nextFloat() < gearType.spawnChance.swordWeight.get() ? gearType.sword : gearType.shovel;
                        setStackIfEmpty(entity, random, gearType.spawnChance.weaponEnchantmentChance.get(), difficulty, EquipmentSlot.MAINHAND, weapon);
                    }
                }
            }
        }
    }

    private static GearType getGearType(int type) {
        return switch (type) {
            default -> BETTER_GOLD;
            case 1 -> PLASLITHERITE;
            case 2 -> REFINED_REDSTONE;
        };
    }

    private static void setEntityArmorWithChance(RandomSource random, LivingEntity entity, boolean isHard, DifficultyInstance difficulty, GearType gearType) {
        ArmorSpawnChanceConfig chanceConfig = gearType.spawnChance();
        float stopChance = isHard ? chanceConfig.multiplePieceChanceHard.get() : chanceConfig.multiplePieceChance.get();
        if (random.nextFloat() < chanceConfig.bootsChance.get()) {
            setStackIfEmpty(entity, random, chanceConfig.armorEnchantmentChance.get(), difficulty, EquipmentSlot.FEET, gearType.boots);
            if (random.nextFloat() < stopChance) {
                return;
            }
        }
        if (random.nextFloat() < chanceConfig.leggingsChance.get()) {
            setStackIfEmpty(entity, random, chanceConfig.armorEnchantmentChance.get(), difficulty, EquipmentSlot.LEGS, gearType.leggings);
            if (random.nextFloat() < stopChance) {
                return;
            }
        }
        if (random.nextFloat() < chanceConfig.chestplateChance.get()) {
            setStackIfEmpty(entity, random, chanceConfig.armorEnchantmentChance.get(), difficulty, EquipmentSlot.CHEST, gearType.chestplate);
            if (random.nextFloat() < stopChance) {
                return;
            }
        }
        if (random.nextFloat() < chanceConfig.helmetChance.get()) {
            setStackIfEmpty(entity, random, chanceConfig.armorEnchantmentChance.get(), difficulty, EquipmentSlot.HEAD, gearType.helmet);
        }
    }

    private static void setStackIfEmpty(LivingEntity entity, RandomSource random, float baseChance, DifficultyInstance difficulty, EquipmentSlot slot, Holder<Item> item) {
        if (entity.getItemBySlot(slot).isEmpty()) {
            ItemStack stack = new ItemStack(item);
            if (random.nextFloat() < baseChance * difficulty.getSpecialMultiplier()) {
                //Copy of vanilla's enchant item level logic
                EnchantmentHelper.enchantItemFromProvider(stack, entity.level().registryAccess(), VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT, difficulty, random);
            }
            entity.setItemSlot(slot, stack);
        }
    }

    private record GearType(Holder<Item> sword, Holder<Item> shovel, Holder<Item> helmet, Holder<Item> chestplate, Holder<Item> leggings, Holder<Item> boots,
                            ArmorSpawnChanceConfig spawnChance) {
    }
}