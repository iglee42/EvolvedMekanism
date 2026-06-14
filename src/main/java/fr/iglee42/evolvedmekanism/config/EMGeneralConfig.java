package fr.iglee42.evolvedmekanism.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import mekanism.common.config.value.CachedIntValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

public class EMGeneralConfig extends BaseMekanismConfig {

    private static final String APT_CATEGORY = "apt";

    private final ForgeConfigSpec configSpec;

    //APT
    public final CachedIntValue aptInputStorage;
    public final CachedIntValue aptDefaultDuration;
    public final CachedFloatingLongValue aptEnergyStorage;
    public final CachedFloatingLongValue aptEnergyConsumption;
    // Supercharging element percents
    public final CachedIntValue aptMk1Percent;
    public final CachedIntValue aptMk2Percent;

    EMGeneralConfig() {

        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("General Config. This config is synced from server to client.").push("general");
        builder.comment("APT Settings").push(APT_CATEGORY);
        aptInputStorage = CachedIntValue.wrap(this, builder.comment("How much gas (in mB) can the input tank hold.")
                .defineInRange("inputPerAntimatter", 5_000, 1, Integer.MAX_VALUE));
        aptDefaultDuration = CachedIntValue.wrap(this, builder.comment("Duration of a recipe (in ticks). Formula: defaultDuration * (gasInputAmount / 100)")
              .defineInRange("defaultDuration", 200, 1, Integer.MAX_VALUE));
        aptEnergyStorage = CachedFloatingLongValue.define(this, builder,"Amount of energy (in Joules) which can be stored in the APT.",
              "energyStorage", FloatingLong.createConst(10_000_000));
        aptEnergyConsumption = CachedFloatingLongValue.define(this, builder, "Energy needed (in Joules) per tick to process the recipe.",
              "energyPerInput", FloatingLong.createConst(100_000));
        // Contribution per supercharging element in percent (e.g. 25 means +25%)
        aptMk1Percent = CachedIntValue.wrap(this, builder.comment("MK1 contribution in percent (e.g. 25 equals 25% per MK1 element).")
                .defineInRange("mk1Percent", 25, 0, 100000));
        aptMk2Percent = CachedIntValue.wrap(this, builder.comment("MK2 contribution in percent (e.g. 250 equals 250% per MK2 element).")
                .defineInRange("mk2Percent", 250, 0, 100000));
        builder.pop();

        builder.pop();
        configSpec = builder.build();
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
}
