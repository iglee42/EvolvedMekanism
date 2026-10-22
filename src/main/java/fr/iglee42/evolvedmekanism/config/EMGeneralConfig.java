package fr.iglee42.evolvedmekanism.config;

import mekanism.api.math.FloatingLong;
import mekanism.api.tier.BaseTier;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.MekanismConfig;
import mekanism.common.config.value.CachedBooleanValue;
import mekanism.common.config.value.CachedConfigValue;
import mekanism.common.config.value.CachedFloatValue;
import mekanism.common.config.value.CachedFloatingLongValue;
import mekanism.common.config.value.CachedIntValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class EMGeneralConfig extends BaseMekanismConfig {

    private static final String APT_CATEGORY = "apt";
    private static final String ITEMS_CATEGORY = "items";
    private static final String MACHINES_CATEGORY = "machines";
    private static final String INTEGRATIONS_CATEGORY = "integrations";

    private final ForgeConfigSpec configSpec;

    //APT
    public final CachedIntValue aptInputStorage;
    public final CachedIntValue aptDefaultDuration;
    public final CachedFloatingLongValue aptEnergyStorage;
    public final CachedFloatingLongValue aptEnergyConsumption;

    public final CachedFloatingLongValue alloyerEnergyUsage;
    public final CachedFloatingLongValue alloyerEnergyStorage;
    public final CachedFloatingLongValue fluidCombinerEnergyUsage;
    public final CachedFloatingLongValue fluidCombinerEnergyStorage;
    public final CachedFloatingLongValue fluidAlloyerEnergyUsage;
    public final CachedFloatingLongValue fluidAlloyerEnergyStorage;
    public final CachedFloatingLongValue chemixerEnergyUsage;
    public final CachedFloatingLongValue chemixerEnergyStorage;
    public final CachedFloatingLongValue melterEnergyUsage;
    public final CachedFloatingLongValue melterEnergyStorage;
    public final CachedFloatingLongValue solidifierEnergyUsage;
    public final CachedFloatingLongValue solidifierEnergyStorage;
    public final CachedFloatingLongValue laserDisenchanterEnergyStorage;
    public final CachedFloatingLongValue enchantingStationEnergyUsage;
    public final CachedFloatingLongValue enchantingStationEnergyStorage;
    public final CachedFloatValue lunarNeutronActivatorProduction;
    public final CachedFloatValue melterHeatDissipation;
    public final CachedBooleanValue enableDecayTimers;

    //OTHER
    public final CachedConfigValue<MaxInstallerTier> maxInstallerTier;
    public final CachedBooleanValue allowAlloyBlocksInOredictionificator;

    EMGeneralConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
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
        aptEnergyStorage = CachedFloatingLongValue.define(this, builder,"Amount of energy (in Joules) which can be stored in the APT.",
              "energyStorage", FloatingLong.createConst(10_000_000));
        aptEnergyConsumption = CachedFloatingLongValue.define(this, builder, "Energy needed (in Joules) per tick to process the recipe.",
              "energyPerInput", FloatingLong.createConst(100_000));
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
        melterEnergyUsage = energyValue(builder, "melterEnergyUsage", MekanismConfig.usage.oxidationChamber.getOrDefault(), "Melter energy use per tick.");
        melterEnergyStorage = energyValue(builder, "melterEnergyStorage", MekanismConfig.storage.oxidationChamber.getOrDefault(), "Melter energy capacity.");
        solidifierEnergyUsage = energyValue(builder, "solidifierEnergyUsage", MekanismConfig.usage.pressurizedReactionBase.getOrDefault(), "Solidifier energy use per tick.");
        solidifierEnergyStorage = energyValue(builder, "solidifierEnergyStorage", MekanismConfig.storage.pressurizedReactionBase.getOrDefault(), "Solidifier energy capacity.");
        laserDisenchanterEnergyStorage = energyValue(builder, "laserDisenchanterEnergyStorage", MekanismConfig.storage.laserTractorBeam.getOrDefault(), "Laser disenchanter energy capacity.");
        enchantingStationEnergyUsage = energyValue(builder, "enchantingStationEnergyUsage", MekanismConfig.usage.modificationStation.getOrDefault(), "Enchanting station energy use per tick.");
        enchantingStationEnergyStorage = energyValue(builder, "enchantingStationEnergyStorage", MekanismConfig.storage.modificationStation.getOrDefault(), "Enchanting station energy capacity.");
        lunarNeutronActivatorProduction = CachedFloatValue.wrap(this, builder.comment("Lunar neutron activator peak production rate.")
              .defineInRange("lunarNeutronActivatorProduction", (double) MekanismConfig.general.maxSolarNeutronActivatorRate.getOrDefault(), 0, Double.MAX_VALUE));
        melterHeatDissipation = CachedFloatValue.wrap(this, builder.comment("Melter heat dissipation per tick.")
              .defineInRange("melterHeatDissipation", MekanismConfig.general.evaporationHeatDissipation.getOrDefault(), 0, Double.MAX_VALUE));
        builder.pop();

        builder.comment("Integration Settings").push(INTEGRATIONS_CATEGORY);
        enableDecayTimers = CachedBooleanValue.wrap(this, builder.comment("Enable radiation decay timer integration with curios.")
              .define("enableDecayTimers", MekanismConfig.common.enableDecayTimers.getOrDefault()));
        builder.pop();

        builder.pop();
        configSpec = builder.build();
    }

    private CachedFloatingLongValue energyValue(ForgeConfigSpec.Builder builder, String name, FloatingLong defaultValue, String comment) {
        return CachedFloatingLongValue.define(this, builder, comment, name, defaultValue);
    }

    @Override
    public String getFileName() {
        return "general";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public Type getConfigType() {
        return Type.SERVER;
    }

    public BaseTier getMaxInstallerTier() {
        MaxInstallerTier configured = maxInstallerTier.getOrDefault();
        BaseTier tier = configured == null ? null : configured.toBaseTier();
        return tier != null ? tier : BaseTier.CREATIVE;
    }
}
