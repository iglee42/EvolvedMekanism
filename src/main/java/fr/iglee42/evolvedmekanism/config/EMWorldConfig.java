package fr.iglee42.evolvedmekanism.config;

import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.MekanismConfig;
import mekanism.common.config.value.CachedBooleanValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class EMWorldConfig extends BaseMekanismConfig {

    private final ForgeConfigSpec configSpec;
    public final CachedBooleanValue enableRegeneration;

    EMWorldConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        enableRegeneration = CachedBooleanValue.wrap(this, builder.comment("Allow Evolved Mekanism ore features to generate during regeneration.")
              .define("enableRegeneration", MekanismConfig.world.enableRegeneration.getOrDefault()));
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "world";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }
}
