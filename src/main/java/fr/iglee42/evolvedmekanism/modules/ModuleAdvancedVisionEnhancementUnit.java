package fr.iglee42.evolvedmekanism.modules;

import java.util.function.Consumer;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.gear.ICustomModule;
import mekanism.api.gear.IHUDElement;
import mekanism.api.gear.IModule;
import mekanism.api.gear.IModuleContainer;
import mekanism.api.gear.IModuleHelper;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.lib.Color;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@ParametersAreNotNullByDefault
public record ModuleAdvancedVisionEnhancementUnit(boolean fogEnabled, Color fogColor) implements ICustomModule<ModuleAdvancedVisionEnhancementUnit> {

    private static final ResourceLocation icon = MekanismUtils.getResource(ResourceType.GUI_HUD, "vision_enhancement_unit.png");

    public static final ResourceLocation FOG = EvolvedMekanism.rl("fog");
    public static final ResourceLocation FOG_COLOR = EvolvedMekanism.rl("fog_color");

    public ModuleAdvancedVisionEnhancementUnit(IModule<ModuleAdvancedVisionEnhancementUnit> module) {
        this(module.getBooleanConfigOrFalse(FOG), Color.rgb(module.<Integer>getConfigOrThrow(FOG_COLOR).get()));
    }

    @Override
    public void tickServer(IModule<ModuleAdvancedVisionEnhancementUnit> module, IModuleContainer moduleContainer, ItemStack stack, Player player) {
        module.useEnergy(player, stack, MekanismConfig.gear.mekaSuitEnergyUsageVisionEnhancement.get());
    }

    @Override
    public void addHUDElements(IModule<ModuleAdvancedVisionEnhancementUnit> module, IModuleContainer moduleContainer, ItemStack stack, Player player, Consumer<IHUDElement> hudElementAdder) {
        hudElementAdder.accept(IModuleHelper.INSTANCE.hudElementEnabled(icon, module.isEnabled()));
    }

    @Override
    public boolean canChangeModeWhenDisabled(IModule<ModuleAdvancedVisionEnhancementUnit> module) {
        return true;
    }

    @Override
    public void changeMode(IModule<ModuleAdvancedVisionEnhancementUnit> module, Player player, IModuleContainer moduleContainer, ItemStack stack, int shift, boolean displayChangeMessage) {
        module.toggleEnabled(moduleContainer, stack, player, MekanismLang.MODULE_VISION_ENHANCEMENT.translate());
    }


}