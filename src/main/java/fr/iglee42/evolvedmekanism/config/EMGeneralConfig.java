package fr.iglee42.evolvedmekanism.config;

import fr.iglee42.emgenerators.tiers.AdvancedSolarPanelTier;
import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.math.FloatingLong;
import mekanism.api.tier.BaseTier;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedConfigValue;
import mekanism.common.config.value.CachedEnumValue;
import mekanism.common.config.value.CachedFloatingLongValue;
import mekanism.common.config.value.CachedIntValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class EMGeneralConfig extends BaseMekanismConfig {

    private static final String APT_CATEGORY = "apt";
    private static final String ITEMS_CATEGORY = "items";
    private static final String ADVANCED_SOLAR_GENERATOR_CATEGORY = "advancedSolarGenerator";

    private final ForgeConfigSpec configSpec;

    //APT
    public final CachedIntValue aptInputStorage;
    public final CachedIntValue aptDefaultDuration;
    public final CachedFloatingLongValue aptEnergyStorage;
    public final CachedFloatingLongValue aptEnergyConsumption;

    //ADVANCED SOLAR GENERATORS
    private final Map<AdvancedSolarPanelTier, CachedIntValue> advancedSolarGeneratorMultipliers = new EnumMap<>(AdvancedSolarPanelTier.class);

    //OTHER
    public final CachedConfigValue<BaseTier> maxInstallerTier;

    EMGeneralConfig() {

        ((InitializableEnum)(Object)BaseTier.BASIC).evolvedmekanism$initNewValues();
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
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
        aptEnergyStorage = CachedFloatingLongValue.define(this, builder,"Amount of energy (in Joules) which can be stored in the APT.",
              "energyStorage", FloatingLong.createConst(10_000_000));
        aptEnergyConsumption = CachedFloatingLongValue.define(this, builder, "Energy needed (in Joules) per tick to process the recipe.",
              "energyPerInput", FloatingLong.createConst(100_000));
        builder.pop();

        builder.comment("Advanced Solar Generator Settings").push(ADVANCED_SOLAR_GENERATOR_CATEGORY);
        for (AdvancedSolarPanelTier tier : AdvancedSolarPanelTier.values()) {
            advancedSolarGeneratorMultipliers.put(tier, CachedIntValue.wrap(this, builder
                    .comment("Energy production multiplier for the " + tier.name() + " advanced solar generator.")
                    .defineInRange(tier.name().toLowerCase(Locale.ROOT) + "Multiplier", tier.getDefaultMultiplier(), 1, Integer.MAX_VALUE)));
        }
        builder.pop();

        builder.pop();
        configSpec = builder.build();
    }

    public int getAdvancedSolarGeneratorMultiplier(AdvancedSolarPanelTier tier) {
        return advancedSolarGeneratorMultipliers.get(tier).get();
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
