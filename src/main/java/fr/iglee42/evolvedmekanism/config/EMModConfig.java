package fr.iglee42.evolvedmekanism.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import java.nio.file.Path;
import java.util.function.Function;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.config.IMekanismConfig;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ConfigFileTypeHandler;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.FMLPaths;

/**
 * Custom {@link ModConfig} implementation that allows for rerouting the server config from being in the world's folder to being in the normal config folder. This allows
 * for us to use the builtin sync support, without the extra hassle of having to explain to people where the config file is, or require people in single player to edit
 * the config each time they make a new world.
 */
public class EMModConfig extends ModConfig {

    private static final EMConfigFileTypeHandler MEK_TOML = new EMConfigFileTypeHandler();

    private final IMekanismConfig mekanismConfig;

    public EMModConfig(ModContainer container, IMekanismConfig config) {
        super(config.getConfigType(), config.getConfigSpec(), container, EvolvedMekanism.MOD_NAME + "/" + config.getFileName() + ".toml");
        this.mekanismConfig = config;
    }

    @Override
    public ConfigFileTypeHandler getHandler() {
        return MEK_TOML;
    }

    public void clearCache(ModConfigEvent event) {
        mekanismConfig.clearCache(event instanceof ModConfigEvent.Unloading);
    }

    private static class EMConfigFileTypeHandler extends ConfigFileTypeHandler {

        private static Path getPath(Path configBasePath) {
            //Intercept server config path reading for Mekanism configs and reroute it to the normal config directory
            if (configBasePath.endsWith("serverconfig")) {
                return FMLPaths.CONFIGDIR.get();
            }
            return configBasePath;
        }

        @Override
        public Function<ModConfig, CommentedFileConfig> reader(Path configBasePath) {
            return super.reader(getPath(configBasePath));
        }

        @Override
        public void unload(Path configBasePath, ModConfig config) {
            super.unload(getPath(configBasePath), config);
        }
    }
}