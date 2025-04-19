package fr.iglee42.evolvedmekanism.config;

import java.nio.file.Path;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.Mekanism;
import mekanism.common.config.IMekanismConfig;
import mekanism.common.config.MekanismModConfig;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.loading.FMLPaths;

public class EMConfigHelper {

    private EMConfigHelper() {
    }

    public static final Path CONFIG_DIR = FMLPaths.getOrCreateGameRelativePath(FMLPaths.CONFIGDIR.get().resolve(EvolvedMekanism.MOD_NAME));

    /**
     * Creates a mod config so that {@link net.minecraftforge.fml.config.ConfigTracker} will track it and sync server configs from server to client.
     */
    public static void registerConfig(ModContainer modContainer, IMekanismConfig config) {
        EMModConfig modConfig = new EMModConfig(modContainer, config);
        if (config.addToContainer()) {
            modContainer.addConfig(modConfig);
        }
    }
}