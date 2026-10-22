package fr.iglee42.evolvedmekanism.config;

import mekanism.api.tier.BaseTier;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.MekanismConfig;
import mekanism.common.config.value.CachedBooleanValue;
import mekanism.common.config.value.CachedConfigValue;
import mekanism.common.config.value.CachedFloatValue;
import mekanism.common.config.value.CachedIntValue;
import mekanism.common.config.value.CachedLongValue;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class EMGeneralConfig extends BaseMekanismConfig {

    private static final String APT_CATEGORY = "apt";
    private static final String ITEMS_CATEGORY = "items";
    private static final String MACHINES_CATEGORY = "machines";
    private static final String INTEGRATIONS_CATEGORY = "integrations";

    private final ModConfigSpec configSpec;

    //APT
    public final CachedIntValue aptInputStorage;
    public final CachedIntValue aptDefaultDuration;
    public final CachedLongValue aptEnergyStorage;
    public final CachedLongValue aptEnergyConsumption;

    //Machine settings
    public final CachedLongValue alloyerEnergyUsage;
    public final CachedLongValue alloyerEnergyStorage;
    public final CachedLongValue fluidCombinerEnergyUsage;
    public final CachedLongValue fluidCombinerEnergyStorage;
    public final CachedLongValue fluidAlloyerEnergyUsage;
    public final CachedLongValue fluidAlloyerEnergyStorage;
    public final CachedLongValue chemixerEnergyUsage;
    public final CachedLongValue chemixerEnergyStorage;
    public final CachedLongValue melterEnergyUsage;
    public final CachedLongValue melterEnergyStorage;
    public final CachedLongValue solidifierEnergyUsage;
    public final CachedLongValue solidifierEnergyStorage;
    public final CachedLongValue laserDisenchanterEnergyStorage;
    public final CachedLongValue enchantingStationEnergyUsage;
    public final CachedLongValue enchantingStationEnergyStorage;
    public final CachedFloatValue lunarNeutronActivatorProduction;
    public final CachedFloatValue melterHeatDissipation;

    //Integrations
    public final CachedBooleanValue enableDecayTimers;

    //OTHER
    public final CachedConfigValue<MaxInstallerTier> maxInstallerTier;
    public final CachedBooleanValue allowAlloyBlocksInOredictionificator;

    EMGeneralConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("General Config. This config is synced from server to client.").push("general");
        builder.comment("Items Settings").push(ITEMS_CATEGORY);
        maxInstallerTier = CachedConfigValue.wrap(this, builder.comment("Defines the machine tier up to which the maximum tier installer should go")
                .defineEnum("maxInstallerTier", MaxInstallerTier.MULTIVERSAL));
        allowAlloyBlocksInOredictionificator = CachedBooleanValue.wrap(this, builder.comment("Allow alloy blocks to be changed in the oredictionificator").define("allowAlloyBlocksInOredictionificator", false));
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

        builder.comment("Machine Settings").push(MACHINES_CATEGORY);
        alloyerEnergyUsage = energyValue(builder, "alloyerEnergyUsage", MekanismConfig.usage.combiner.getOrDefault(), "Alloyer energy use per tick.");
        alloyerEnergyStorage = energyValue(builder, "alloyerEnergyStorage", MekanismConfig.storage.combiner.getOrDefault(), "Alloyer energy capacity.");
        fluidCombinerEnergyUsage = energyValue(builder, "fluidCombinerEnergyUsage", MekanismConfig.usage.combiner.getOrDefault(), "Fluid combiner energy use per tick.");
        fluidCombinerEnergyStorage = energyValue(builder, "fluidCombinerEnergyStorage", MekanismConfig.storage.combiner.getOrDefault(), "Fluid combiner energy capacity.");
        fluidAlloyerEnergyUsage = energyValue(builder, "fluidAlloyerEnergyUsage", MekanismConfig.usage.combiner.getOrDefault(), "Fluid alloyer energy use per tick.");
        fluidAlloyerEnergyStorage = energyValue(builder, "fluidAlloyerEnergyStorage", MekanismConfig.storage.combiner.getOrDefault(), "Fluid alloyer energy capacity.");
        chemixerEnergyUsage = energyValue(builder, "chemixerEnergyUsage", MekanismConfig.usage.combiner.getOrDefault(), "Chemixer energy use per tick.");
        chemixerEnergyStorage = energyValue(builder, "chemixerEnergyStorage", MekanismConfig.storage.combiner.getOrDefault(), "Chemixer energy capacity.");
        melterEnergyUsage = energyValue(builder, "melterEnergyUsage", MekanismConfig.usage.chemicalOxidizer.getOrDefault(), "Melter energy use per tick.");
        melterEnergyStorage = energyValue(builder, "melterEnergyStorage", MekanismConfig.storage.chemicalOxidizer.getOrDefault(), "Melter energy capacity.");
        solidifierEnergyUsage = energyValue(builder, "solidifierEnergyUsage", MekanismConfig.usage.pressurizedReactionBase.getOrDefault(), "Solidifier energy use per tick.");
        solidifierEnergyStorage = energyValue(builder, "solidifierEnergyStorage", MekanismConfig.storage.pressurizedReactionBase.getOrDefault(), "Solidifier energy capacity.");
        laserDisenchanterEnergyStorage = energyValue(builder, "laserDisenchanterEnergyStorage", MekanismConfig.storage.laserTractorBeam.getOrDefault(), "Laser disenchanter energy capacity.");
        enchantingStationEnergyUsage = energyValue(builder, "enchantingStationEnergyUsage", MekanismConfig.usage.modificationStation.getOrDefault(), "Enchanting station energy use per tick.");
        enchantingStationEnergyStorage = energyValue(builder, "enchantingStationEnergyStorage", MekanismConfig.storage.modificationStation.getOrDefault(), "Enchanting station energy capacity.");
        lunarNeutronActivatorProduction = CachedFloatValue.wrap(this, builder.comment("Lunar neutron activator peak production rate.")
              .defineInRange("lunarNeutronActivatorProduction", (double) MekanismConfig.general.maxSolarNeutronActivatorRate.getOrDefault(), 0, Double.MAX_VALUE));
        melterHeatDissipation = CachedFloatValue.wrap(this, builder.comment("Melter heat dissipation per tick.")
              .defineInRange("melterHeatDissipation", (double) MekanismConfig.general.evaporationHeatDissipation.getOrDefault(), 0, Double.MAX_VALUE));
        builder.pop();

        builder.comment("Integration Settings").push(INTEGRATIONS_CATEGORY);
        enableDecayTimers = CachedBooleanValue.wrap(this, builder.comment("Enable radiation decay timer integration with curios.")
              .define("enableDecayTimers", MekanismConfig.common.enableDecayTimers.getOrDefault()));
        builder.pop();

        builder.pop();
        configSpec = builder.build();
    }

    private CachedLongValue energyValue(ModConfigSpec.Builder builder, String name, long defaultValue, String comment) {
        return CachedLongValue.wrap(this, builder.comment(comment).defineInRange(name, defaultValue, 1, Long.MAX_VALUE));
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

    public BaseTier getMaxInstallerTier() {
        MaxInstallerTier configured = maxInstallerTier.getOrDefault();
        BaseTier tier = configured == null ? null : configured.toBaseTier();
        return tier != null ? tier : BaseTier.CREATIVE;
    }
}
