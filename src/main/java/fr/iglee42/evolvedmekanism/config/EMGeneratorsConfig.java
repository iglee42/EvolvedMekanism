package fr.iglee42.evolvedmekanism.config;

import fr.iglee42.emgenerators.tiers.AdvancedLunarPanelTier;
import fr.iglee42.emgenerators.tiers.AdvancedSolarPanelTier;
import fr.iglee42.emgenerators.tiers.AdvancedWindGeneratorTier;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedIntValue;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class EMGeneratorsConfig extends BaseMekanismConfig {

    private final ModConfigSpec configSpec;

    private final CachedIntValue solarAdvanced;
    private final CachedIntValue solarElite;
    private final CachedIntValue solarUltimate;
    private final CachedIntValue solarOverclocked;
    private final CachedIntValue solarQuantum;
    private final CachedIntValue solarDense;
    private final CachedIntValue solarMultiversal;
    private final CachedIntValue solarCreative;

    private final CachedIntValue lunarBasic;
    private final CachedIntValue lunarAdvanced;
    private final CachedIntValue lunarElite;
    private final CachedIntValue lunarUltimate;
    private final CachedIntValue lunarOverclocked;
    private final CachedIntValue lunarQuantum;
    private final CachedIntValue lunarDense;
    private final CachedIntValue lunarMultiversal;
    private final CachedIntValue lunarCreative;

    private final CachedIntValue windAdvanced;
    private final CachedIntValue windElite;
    private final CachedIntValue windUltimate;
    private final CachedIntValue windOverclocked;
    private final CachedIntValue windQuantum;
    private final CachedIntValue windDense;
    private final CachedIntValue windMultiversal;
    private final CachedIntValue windCreative;

    EMGeneratorsConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("""
                Generator production multipliers. This config is synced from server to client.
                Each value multiplies the matching Mekanism Generators rate, so Mekanism's own generator config still applies.
                Solar and lunar tiers multiply advancedSolarGeneration. The basic lunar multiblock uses that same rate at multiplier 1.
                Wind tiers multiply the height-based output between generationMin and generationMax.
                Internal buffer scales with the same multiplier. Defaults go up by x4 each tier, starting at x8 of the Mekanism generator used in the recipe.""").push("generators");

        builder.comment("Evolved solar generators. Multiplier of Mekanism's advancedSolarGeneration.").push("solar");
        solarAdvanced = multiplier(builder, "advanced", 8);
        solarElite = multiplier(builder, "elite", 32);
        solarUltimate = multiplier(builder, "ultimate", 128);
        solarOverclocked = multiplier(builder, "overclocked", 512);
        solarQuantum = multiplier(builder, "quantum", 2_048);
        solarDense = multiplier(builder, "dense", 8_192);
        solarMultiversal = multiplier(builder, "multiversal", 32_768);
        solarCreative = multiplier(builder, "creative", 131_072);
        builder.pop();

        builder.comment("Lunar generators. Tiered machines multiply Mekanism's advancedSolarGeneration. The single-block lunar generator keeps using solarGeneration.").push("lunar");
        lunarBasic = multiplier(builder, "basic", 1);
        lunarAdvanced = multiplier(builder, "advanced", 8);
        lunarElite = multiplier(builder, "elite", 32);
        lunarUltimate = multiplier(builder, "ultimate", 128);
        lunarOverclocked = multiplier(builder, "overclocked", 512);
        lunarQuantum = multiplier(builder, "quantum", 2_048);
        lunarDense = multiplier(builder, "dense", 8_192);
        lunarMultiversal = multiplier(builder, "multiversal", 32_768);
        lunarCreative = multiplier(builder, "creative", 131_072);
        builder.pop();

        builder.comment("Evolved wind generators. Multiplier of Mekanism's height-based wind output.").push("wind");
        windAdvanced = multiplier(builder, "advanced", 8);
        windElite = multiplier(builder, "elite", 32);
        windUltimate = multiplier(builder, "ultimate", 128);
        windOverclocked = multiplier(builder, "overclocked", 512);
        windQuantum = multiplier(builder, "quantum", 2_048);
        windDense = multiplier(builder, "dense", 8_192);
        windMultiversal = multiplier(builder, "multiversal", 32_768);
        windCreative = multiplier(builder, "creative", 131_072);
        builder.pop();

        builder.pop();
        configSpec = builder.build();
    }

    private CachedIntValue multiplier(ModConfigSpec.Builder builder, String name, int defaultValue) {
        return CachedIntValue.wrap(this, builder.comment("Production multiplier for the " + name + " tier.")
                .defineInRange(name, defaultValue, 1, Integer.MAX_VALUE));
    }

    public int solarMultiplier(AdvancedSolarPanelTier tier) {
        return switch (tier) {
            case ADVANCED -> solarAdvanced.getOrDefault();
            case ELITE -> solarElite.getOrDefault();
            case ULTIMATE -> solarUltimate.getOrDefault();
            case OVERCLOCKED -> solarOverclocked.getOrDefault();
            case QUANTUM -> solarQuantum.getOrDefault();
            case DENSE -> solarDense.getOrDefault();
            case MULTIVERSAL -> solarMultiversal.getOrDefault();
            case CREATIVE -> solarCreative.getOrDefault();
        };
    }

    public int lunarMultiplier(AdvancedLunarPanelTier tier) {
        return switch (tier) {
            case BASIC -> lunarBasic.getOrDefault();
            case ADVANCED -> lunarAdvanced.getOrDefault();
            case ELITE -> lunarElite.getOrDefault();
            case ULTIMATE -> lunarUltimate.getOrDefault();
            case OVERCLOCKED -> lunarOverclocked.getOrDefault();
            case QUANTUM -> lunarQuantum.getOrDefault();
            case DENSE -> lunarDense.getOrDefault();
            case MULTIVERSAL -> lunarMultiversal.getOrDefault();
            case CREATIVE -> lunarCreative.getOrDefault();
        };
    }

    public int windMultiplier(AdvancedWindGeneratorTier tier) {
        return switch (tier) {
            case ADVANCED -> windAdvanced.getOrDefault();
            case ELITE -> windElite.getOrDefault();
            case ULTIMATE -> windUltimate.getOrDefault();
            case OVERCLOCKED -> windOverclocked.getOrDefault();
            case QUANTUM -> windQuantum.getOrDefault();
            case DENSE -> windDense.getOrDefault();
            case MULTIVERSAL -> windMultiversal.getOrDefault();
            case CREATIVE -> windCreative.getOrDefault();
        };
    }

    @Override
    public String getFileName() {
        return "generators";
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
