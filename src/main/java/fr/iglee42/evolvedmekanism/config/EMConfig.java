package fr.iglee42.evolvedmekanism.config;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class EMConfig {

    private EMConfig() {
    }

    public static final EMGeneralConfig general = new EMGeneralConfig();
    public static final EMWorldConfig world = new EMWorldConfig();
    public static final EMGeneratorsConfig generators = new EMGeneratorsConfig();
    public static final EMTierConfig tiers = new EMTierConfig();

    public static void registerConfigs(ModLoadingContext modLoadingContext) {
        ModContainer modContainer = modLoadingContext.getActiveContainer();
        EMConfigHelper.registerConfig(modContainer, general);
        EMConfigHelper.registerConfig(modContainer, world);
        EMConfigHelper.registerConfig(modContainer, generators);
        EMConfigHelper.registerConfig(modContainer, tiers);
    }
}