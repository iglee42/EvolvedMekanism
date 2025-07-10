package fr.iglee42.emtools.config;

import fr.iglee42.emtools.materials.BetterGoldMaterialDefaults;
import fr.iglee42.emtools.materials.PlaslitheriteMaterialDefaults;
import fr.iglee42.evolvedmekanism.config.EMConfigHelper;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.tools.common.config.ToolsConfigTranslations;
import mekanism.tools.common.material.MaterialCreator;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class EMToolsMaterialConfig extends BaseMekanismConfig {


    private final ModConfigSpec configSpec;


    public final MaterialCreator betterGold;
    public final MaterialCreator plaslitherite;



    EMToolsMaterialConfig() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        ToolsConfigTranslations.STARTUP_MATERIALS.applyToBuilder(builder).push("materials");
        betterGold = new MaterialCreator(this, builder, new BetterGoldMaterialDefaults());
        plaslitherite = new MaterialCreator(this, builder, new PlaslitheriteMaterialDefaults());
        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "tools-materials-startup";
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
        return ModConfig.Type.STARTUP;
    }


}