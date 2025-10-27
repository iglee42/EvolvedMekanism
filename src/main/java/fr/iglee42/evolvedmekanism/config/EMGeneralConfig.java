package fr.iglee42.evolvedmekanism.config;

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

public class EMGeneralConfig extends BaseMekanismConfig {

    private static final String APT_CATEGORY = "apt";
    private static final String ITEMS_CATEGORY = "items";

    private final ModConfigSpec configSpec;

    //APT
    public final CachedIntValue aptInputStorage;
    public final CachedIntValue aptDefaultDuration;
    public final CachedLongValue aptEnergyStorage;
    public final CachedLongValue aptEnergyConsumption;

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

        builder.pop();
        configSpec = builder.build();
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
