package fr.iglee42.emtools.config;

import fr.iglee42.emtools.materials.BetterGoldMaterialDefaults;
import fr.iglee42.emtools.materials.PlaslitheriteMaterialDefaults;
import fr.iglee42.evolvedmekanism.config.EMConfigHelper;
import fr.iglee42.evolvedmekanism.config.EMGeneralConfig;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.IMekanismConfig;
import mekanism.common.config.value.CachedBooleanValue;
import mekanism.common.config.value.CachedFloatValue;
import mekanism.tools.common.material.MaterialCreator;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class EMToolsConfig extends BaseMekanismConfig {


    public static final EMToolsConfig tools = new EMToolsConfig();

    public static void registerConfigs(ModLoadingContext modLoadingContext) {
        ModContainer modContainer = modLoadingContext.getActiveContainer();
        EMConfigHelper.registerConfig(modContainer, tools);

    }
    private final ForgeConfigSpec configSpec;

    public final ArmorSpawnChanceConfig betterGoldSpawnRate;
    public final ArmorSpawnChanceConfig plaslitheriteSpawnRate;
    public final MaterialCreator betterGold;
    public final MaterialCreator plaslitherite;



    EMToolsConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Evolved Mekanism Tools Config. This config is synced from server to client.").push("tools");

        builder.push("mobArmorSpawnRate");
        betterGoldSpawnRate = new ArmorSpawnChanceConfig(this, builder, "better_gold", "better_gold");
        plaslitheriteSpawnRate = new ArmorSpawnChanceConfig(this, builder, "plaslitherite", "plaslitherite");
        builder.pop();
        betterGold = new MaterialCreator(this, builder, new BetterGoldMaterialDefaults());
        plaslitherite = new MaterialCreator(this, builder, new PlaslitheriteMaterialDefaults());
        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "tools";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public Type getConfigType() {
        return Type.SERVER;
    }

    public static class ArmorSpawnChanceConfig {

        public final CachedBooleanValue canSpawnWeapon;
        public final CachedFloatValue swordWeight;
        public final CachedFloatValue helmetChance;
        public final CachedFloatValue chestplateChance;
        public final CachedFloatValue leggingsChance;
        public final CachedFloatValue bootsChance;

        public final CachedFloatValue multiplePieceChance;
        public final CachedFloatValue multiplePieceChanceHard;

        public final CachedFloatValue weaponEnchantmentChance;
        public final CachedFloatValue armorEnchantmentChance;

        private ArmorSpawnChanceConfig(IMekanismConfig config, ForgeConfigSpec.Builder builder, String armorKey, String armor) {
            this(config, builder, armorKey, armor, 0.33, 1, 1, 1, 1, 0.25, 0.5);
        }

        private ArmorSpawnChanceConfig(IMekanismConfig config, ForgeConfigSpec.Builder builder, String armorKey, String armor, double swordChance, double helmetChance,
              double chestplateChance, double leggingsChance, double bootsChance, double weaponEnchantmentChance, double armorEnchantmentChance) {
            builder.comment("Spawn chances for pieces of " + armor + " gear. Note: These values are after the general mobArmorSpawnRate (or corresponding weapon rate) has been checked, "
                            + "and after an even split between material types has been done.").push(armorKey);
            this.canSpawnWeapon = CachedBooleanValue.wrap(config, builder.comment("Whether mobs can spawn with " + armor + " Weapons.")
                  .define("canSpawnWeapon", true));
            this.swordWeight = CachedFloatValue.wrap(config, builder.comment("The chance that mobs will spawn with " + armor + " Swords rather than " + armor + " Shovels.")
                  .defineInRange("swordWeight", swordChance, 0, 1));
            this.helmetChance = CachedFloatValue.wrap(config, builder.comment("The chance that mobs can spawn with " + armor + " Helmets.")
                  .defineInRange("helmetChance", helmetChance, 0, 1));
            this.chestplateChance = CachedFloatValue.wrap(config, builder.comment("The chance that mobs can spawn with " + armor + " Chestplates.")
                  .defineInRange("chestplateChance", chestplateChance, 0, 1));
            this.leggingsChance = CachedFloatValue.wrap(config, builder.comment("The chance that mobs can spawn with " + armor + " Leggings.")
                  .defineInRange("leggingsChance", leggingsChance, 0, 1));
            this.bootsChance = CachedFloatValue.wrap(config, builder.comment("The chance that mobs can spawn with " + armor + " Boots.")
                  .defineInRange("bootsChance", bootsChance, 0, 1));

            this.multiplePieceChance = CachedFloatValue.wrap(config, builder.comment("The chance that after each piece of " + armor + " Armor a mob spawns with that no more pieces will be added. Order of pieces tried is boots, leggings, chestplate, helmet.")
                  .defineInRange("multiplePieceChance", 0.25, 0, 1));
            this.multiplePieceChanceHard = CachedFloatValue.wrap(config, builder.comment("The chance on hard mode that after each piece of " + armor + " Armor a mob spawns with that no more pieces will be added. Order of pieces tried is boots, leggings, chestplate, helmet.")
                  .defineInRange("multiplePieceChanceHard", 0.1, 0, 1));

            this.weaponEnchantmentChance = CachedFloatValue.wrap(config, builder.comment("The chance that if a mob spawns with " + armor + " Weapons that it will be enchanted. This is multiplied modified by the chunk's difficulty modifier.")
                  .defineInRange("weaponEnchantmentChance", weaponEnchantmentChance, 0, 1));
            this.armorEnchantmentChance = CachedFloatValue.wrap(config, builder.comment("The chance that if a mob spawns with " + armor + " Armor that they will be enchanted. This is multiplied modified by the chunk's difficulty modifier.")
                  .defineInRange("armorEnchantmentChance", armorEnchantmentChance, 0, 1));
            builder.pop();
        }
    }
}