package fr.iglee42.emtools.config;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.config.EMConfigHelper;
import mekanism.common.config.IMekanismConfig;
import mekanism.common.config.MekanismConfigHelper;
import mekanism.tools.common.MekanismTools;
import mekanism.tools.common.config.ToolsClientConfig;
import mekanism.tools.common.config.ToolsConfig;
import mekanism.tools.common.config.ToolsMaterialConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.event.config.ModConfigEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EvolvedMekanismToolsConfig {

    private EvolvedMekanismToolsConfig() {
    }

    private static final Map<IConfigSpec, IMekanismConfig> KNOWN_CONFIGS = new HashMap<>();
    //Note: Materials has to be registered above tools, so that we can reference the materials in the other config
    public static final EMToolsMaterialConfig materials = new EMToolsMaterialConfig();
    public static final EMToolsConfig tools = new EMToolsConfig();

    public static void registerConfigs(ModContainer modContainer) {
        EMConfigHelper.registerConfig(KNOWN_CONFIGS, modContainer, materials);
        EMConfigHelper.registerConfig(KNOWN_CONFIGS, modContainer, tools);
    }

    public static void onConfigLoad(ModConfigEvent configEvent) {
        EMConfigHelper.onConfigLoad(configEvent, EvolvedMekanism.MODID, KNOWN_CONFIGS);
    }

    public static Collection<IMekanismConfig> getConfigs() {
        return Collections.unmodifiableCollection(KNOWN_CONFIGS.values());
    }
}