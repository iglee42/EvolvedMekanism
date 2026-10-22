package fr.iglee42.evolvedmekanism.modules;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IHUDElement;
import mekanism.api.gear.IModule;
import mekanism.api.gear.IModuleHelper;
import mekanism.api.gear.config.IModuleConfigItem;
import mekanism.api.gear.config.ModuleBooleanData;
import mekanism.api.gear.config.ModuleColorData;
import mekanism.api.gear.config.ModuleConfigItemCreator;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.lib.Color;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

@ParametersAreNotNullByDefault
public class ModuleAdvancedVisionEnhancementUnit implements ICustomModule<ModuleAdvancedVisionEnhancementUnit> {

    private static final ResourceLocation ICON = MekanismUtils.getResource(ResourceType.GUI_HUD, "vision_enhancement_unit.png");

    private IModuleConfigItem<Boolean> fogEnabled;
    private IModuleConfigItem<Integer> fogColor;

    @Override
    public void init(IModule<ModuleAdvancedVisionEnhancementUnit> module, ModuleConfigItemCreator creator) {
        fogEnabled = creator.createConfigItem("fog", EvolvedMekanismLang.MODULE_FOG, new ModuleBooleanData(false));
        fogColor = creator.createConfigItem("fog_color", EvolvedMekanismLang.MODULE_FOG_COLOR, ModuleColorData.rgb(0x66CC66));
    }

    public boolean fogEnabled() {
        return fogEnabled != null && fogEnabled.get();
    }

    public Color fogColor() {
        return Color.rgb(fogColor == null ? 0x66CC66 : fogColor.get());
    }

    @Override
    public void tickServer(IModule<ModuleAdvancedVisionEnhancementUnit> module, Player player) {
        module.useEnergy(player, MekanismConfig.gear.mekaSuitEnergyUsageVisionEnhancement.get());
    }

    @Override
    public void addHUDElements(IModule<ModuleAdvancedVisionEnhancementUnit> module, Player player, Consumer<IHUDElement> hudElementAdder) {
        hudElementAdder.accept(IModuleHelper.INSTANCE.hudElementEnabled(ICON, module.isEnabled()));
    }

    @Override
    public boolean canChangeModeWhenDisabled(IModule<ModuleAdvancedVisionEnhancementUnit> module) {
        return true;
    }

    @Override
    public void changeMode(IModule<ModuleAdvancedVisionEnhancementUnit> module, Player player, ItemStack stack, int shift, boolean displayChangeMessage) {
        module.toggleEnabled(player, MekanismLang.MODULE_VISION_ENHANCEMENT.translate());
    }
}
