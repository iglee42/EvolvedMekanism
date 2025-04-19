package fr.iglee42.evolvedmekanism.config;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class EMConfig {

    private EMConfig() {
    }

    public static final EMGeneralConfig general = new EMGeneralConfig();

    public static void registerConfigs(ModLoadingContext modLoadingContext) {
        ModContainer modContainer = modLoadingContext.getActiveContainer();
        EMConfigHelper.registerConfig(modContainer, general);
    }
}