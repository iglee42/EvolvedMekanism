package fr.iglee42.evolvedmekanism.config;


import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.Mekanism;
import mekanism.common.config.IMekanismConfig;
import mekanism.common.config.MekanismConfigHelper;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EMConfig {

    private EMConfig() {
    }


    private static final Map<IConfigSpec, IMekanismConfig> KNOWN_CONFIGS = new HashMap<>();
    public static final EMGeneralConfig general = new EMGeneralConfig();

    public static void registerConfigs(ModContainer container) {
        EMConfigHelper.registerConfig(KNOWN_CONFIGS,container, general);
    }


    public static void onConfigLoad(ModConfigEvent configEvent) {
        MekanismConfigHelper.onConfigLoad(configEvent, EvolvedMekanism.MODID, KNOWN_CONFIGS);
    }

    public static Collection<IMekanismConfig> getConfigs() {
        return Collections.unmodifiableCollection(KNOWN_CONFIGS.values());
    }
}