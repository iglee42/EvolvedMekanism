package fr.iglee42.emtools.config;

import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.IMekanismConfig;
import mekanism.common.config.value.CachedBooleanValue;
import mekanism.common.config.value.CachedFloatValue;
import mekanism.tools.common.config.MekanismToolsConfig;
import mekanism.tools.common.config.ToolsConfigTranslations;
import mekanism.tools.common.config.ToolsConfigTranslations.ArmorSpawnChanceTranslations;
import mekanism.tools.common.material.MaterialCreator;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.neoforge.common.ModConfigSpec;

public class EMToolsConfig extends BaseMekanismConfig {

    private final ModConfigSpec configSpec;
    public final ArmorSpawnChanceConfig betterGoldSpawnRate;
    public final ArmorSpawnChanceConfig plaslitheriteSpawnRate;
    public final ArmorSpawnChanceConfig refinedRedstoneSpawnRate;

    EMToolsConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        ToolsConfigTranslations.SERVER_GEAR_SPAWN_CHANCE.applyToBuilder(builder).push("mobGearSpawnRate");
        betterGoldSpawnRate = new ArmorSpawnChanceConfig(this, builder, MekanismToolsConfig.materials.bronze);
        plaslitheriteSpawnRate = new ArmorSpawnChanceConfig(this, builder, MekanismToolsConfig.materials.lapisLazuli);
        refinedRedstoneSpawnRate = new ArmorSpawnChanceConfig(this, builder, MekanismToolsConfig.materials.lapisLazuli);
        builder.pop();

        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "tools";
    }

    @Override
    public String getTranslation() {
        return "General Config";
    }

    @Override
    public ModConfigSpec getConfigSpec() {
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

        private ArmorSpawnChanceConfig(IMekanismConfig config, ModConfigSpec.Builder builder, MaterialCreator material) {
            this(config, builder, material.getRegistryPrefix(), 0.33, 1, 1, 1, 1, 0.25, 0.5);
        }

        private ArmorSpawnChanceConfig(IMekanismConfig config, ModConfigSpec.Builder builder, String key, double swordChance, double helmetChance,
              double chestplateChance, double leggingsChance, double bootsChance, double weaponEnchantmentChance, double armorEnchantmentChance) {
            ArmorSpawnChanceTranslations translations = ArmorSpawnChanceTranslations.create(key);
            translations.topLevel().applyToBuilder(builder).push(key);
            this.canSpawnWeapon = CachedBooleanValue.wrap(config, translations.canSpawnWeapon().applyToBuilder(builder)
                  .define("canSpawnWeapon", true));
            this.swordWeight = CachedFloatValue.wrap(config, translations.swordWeight().applyToBuilder(builder)
                  .defineInRange("swordWeight", swordChance, 0, 1));
            this.helmetChance = CachedFloatValue.wrap(config, translations.helmetChance().applyToBuilder(builder)
                  .defineInRange("helmetChance", helmetChance, 0, 1));
            this.chestplateChance = CachedFloatValue.wrap(config, translations.chestplateChance().applyToBuilder(builder)
                  .defineInRange("chestplateChance", chestplateChance, 0, 1));
            this.leggingsChance = CachedFloatValue.wrap(config, translations.leggingsChance().applyToBuilder(builder)
                  .defineInRange("leggingsChance", leggingsChance, 0, 1));
            this.bootsChance = CachedFloatValue.wrap(config, translations.bootsChance().applyToBuilder(builder)
                  .defineInRange("bootsChance", bootsChance, 0, 1));

            this.multiplePieceChance = CachedFloatValue.wrap(config, translations.multiplePieceChance().applyToBuilder(builder)
                  .defineInRange("multiplePieceChance", 0.25, 0, 1));
            this.multiplePieceChanceHard = CachedFloatValue.wrap(config, translations.multiplePieceChanceHard().applyToBuilder(builder)
                  .defineInRange("multiplePieceChanceHard", 0.1, 0, 1));

            this.weaponEnchantmentChance = CachedFloatValue.wrap(config, translations.weaponEnchantmentChance().applyToBuilder(builder)
                  .defineInRange("weaponEnchantmentChance", weaponEnchantmentChance, 0, 1));
            this.armorEnchantmentChance = CachedFloatValue.wrap(config, translations.armorEnchantmentChance().applyToBuilder(builder)
                  .defineInRange("armorEnchantmentChance", armorEnchantmentChance, 0, 1));
            builder.pop();
        }
    }
}