package fr.iglee42.evolvedmekanism.config;

import fr.iglee42.emgenerators.tiers.AdvancedLunarPanelTier;
import fr.iglee42.emgenerators.tiers.AdvancedSolarPanelTier;
import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedConfigValue;
import mekanism.common.config.value.CachedEnumValue;
import mekanism.common.config.value.CachedIntValue;
import mekanism.common.config.value.CachedLongValue;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class EMGeneralConfig extends BaseMekanismConfig {

    private static final String APT_CATEGORY = "apt";
    private static final String ITEMS_CATEGORY = "items";
    private static final String ADVANCED_SOLAR_GENERATOR_CATEGORY = "advancedSolarGenerator";
    private static final String ADVANCED_LUNAR_GENERATOR_CATEGORY = "advancedLunarGenerator";

    private final ModConfigSpec configSpec;

    //APT
    public final CachedIntValue aptInputStorage;
    public final CachedIntValue aptDefaultDuration;
    public final CachedLongValue aptEnergyStorage;
    public final CachedLongValue aptEnergyConsumption;

    //ADVANCED SOLAR/LUNAR GENERATORS
    private final Map<AdvancedSolarPanelTier, CachedIntValue> advancedSolarGeneratorMultipliers = new EnumMap<>(AdvancedSolarPanelTier.class);
    private final Map<AdvancedLunarPanelTier, CachedIntValue> advancedLunarGeneratorMultipliers = new EnumMap<>(AdvancedLunarPanelTier.class);

    //OTHER
    public final CachedConfigValue<BaseTier> maxInstallerTier;

    EMGeneralConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("General Config. This config is synced from server to client.").push("general");
        builder.comment("Items Settings").push(ITEMS_CATEGORY);
        maxInstallerTier = CachedEnumValue.wrap(this,builder.comment("Defines the machine tier up to which the maximum tier installer should go")
                .defineEnum("maxInstallerTier", EMBaseTier.MULTIVERSAL,
                        BaseTier.BASIC,BaseTier.ADVANCED,BaseTier.ELITE,BaseTier.ULTIMATE,BaseTier.CREATIVE,EMBaseTier.OVERCLOCKED,EMBaseTier.QUANTUM,EMBaseTier.DENSE,EMBaseTier.MULTIVERSAL));
        builder.pop();
        builder.comment("APT Settings").push(APT_CATEGORY);
        aptInputStorage = CachedIntValue.wrap(this, builder.comment("How much gas (in mB) can the input tank hold.")
                .defineInRange("inputPerAntimatter", 5_000, 1, Integer.MAX_VALUE));
        aptDefaultDuration = CachedIntValue.wrap(this, builder.comment("Duration of a recipe (in ticks). Formula: defaultDuration * (gasInputAmount / 100)")
              .defineInRange("defaultDuration", 200, 1, Integer.MAX_VALUE));
        aptEnergyStorage = CachedLongValue.wrap(this, builder.comment("Amount of energy (in Joules) which can be stored in the APT.").define(
              "energyStorage", 10_000_000L));
        aptEnergyConsumption = CachedLongValue.wrap(this, builder.comment("Energy needed (in Joules) per tick to process the recipe.").define(
              "energyPerInput", 100_000L));
        builder.pop();

        builder.comment("Advanced Solar Generator Settings").push(ADVANCED_SOLAR_GENERATOR_CATEGORY);
        for (AdvancedSolarPanelTier tier : AdvancedSolarPanelTier.values()) {
            advancedSolarGeneratorMultipliers.put(tier, CachedIntValue.wrap(this, builder
                    .comment("Energy production multiplier for the " + tier.name() + " advanced solar generator.")
                    .defineInRange(tier.name().toLowerCase(Locale.ROOT) + "Multiplier", tier.getDefaultMultiplier(), 1, Integer.MAX_VALUE)));
        }
        builder.pop();

        builder.comment("Advanced Lunar Generator Settings").push(ADVANCED_LUNAR_GENERATOR_CATEGORY);
        for (AdvancedLunarPanelTier tier : AdvancedLunarPanelTier.values()) {
            advancedLunarGeneratorMultipliers.put(tier, CachedIntValue.wrap(this, builder
                    .comment("Energy production multiplier for the " + tier.name() + " advanced lunar generator.")
                    .defineInRange(tier.name().toLowerCase(Locale.ROOT) + "Multiplier", tier.getDefaultMultiplier(), 1, Integer.MAX_VALUE)));
        }
        builder.pop();

        builder.pop();
        configSpec = builder.build();
    }

    public int getAdvancedSolarGeneratorMultiplier(AdvancedSolarPanelTier tier) {
        return advancedSolarGeneratorMultipliers.get(tier).get();
    }

    public int getAdvancedLunarGeneratorMultiplier(AdvancedLunarPanelTier tier) {
        return advancedLunarGeneratorMultipliers.get(tier).get();
    }

    @Override
    public String getFileName() {
        return "general";
    }

    @Override
    public String getTranslation() {
        return "";
    }

    @Override
    public ModConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }
}
