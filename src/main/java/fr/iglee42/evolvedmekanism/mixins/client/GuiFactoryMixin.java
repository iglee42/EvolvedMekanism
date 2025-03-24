package fr.iglee42.evolvedmekanism.mixins.client;

import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.element.GuiDumpButton;
import mekanism.client.gui.element.GuiElement;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.gui.element.tab.GuiSortingTab;
import mekanism.client.gui.machine.GuiFactory;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.ISupportsWarning;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.factory.TileEntityItemStackGasToItemStackFactory;
import mekanism.common.tile.factory.TileEntityMetallurgicInfuserFactory;
import mekanism.common.tile.factory.TileEntitySawingFactory;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = GuiFactory.class,remap = false)
public abstract class GuiFactoryMixin {

    @Shadow protected abstract GuiProgress addProgress(GuiProgress progressBar);

    @Unique
    private TileEntityFactory<?> be;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void evolvedmekanism$makeGuiBigger(MekanismTileContainer container, Inventory inv, Component title, CallbackInfo ci) {
        Object obj = this;
        GuiFactory gui = (GuiFactory) obj;
        be = (TileEntityFactory<?>) container.getTileEntity();
        if (((TileEntityFactory<?>)container.getTileEntity()).tier.ordinal() >= EMFactoryTier.OVERCLOCKED.ordinal()) {
            if (be.hasSecondaryResourceBar() && !(be instanceof TileEntitySawingFactory)) {
                gui.imageHeight -= 11;
                gui.inventoryLabelY = 75;
            }

            gui.imageWidth +=(38 *( ((TileEntityFactory<?>) container.getTileEntity()).tier.ordinal() - EMFactoryTier.OVERCLOCKED.ordinal() + 1)) + 9;

        }
    }

    @Inject(method = "addGuiElements", at = @At(value = "INVOKE", target = "Lmekanism/client/gui/GuiConfigurableTile;addGuiElements()V",shift = At.Shift.AFTER), cancellable = true)
    private void evolvedmekanism$changeElementPoses(CallbackInfo ci) {
        if (be.tier.ordinal() >= EMFactoryTier.OVERCLOCKED.ordinal()){
            Object obj = this;
            GuiFactory gui = (GuiFactory) obj;
            evolvedMekanism$addElement(gui,new GuiVerticalPowerBar(gui, be.getEnergyContainer(), 176, gui.inventoryLabelY + 9, 52))
                    .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, be.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY, 0));
            evolvedMekanism$addElement(gui,new GuiSortingTab(gui, be));
            evolvedMekanism$addElement(gui,new GuiEnergyTab(gui, be.getEnergyContainer(), be::getLastUsage));
            if (be.hasSecondaryResourceBar()) {
                ISupportsWarning<?> secondaryBar = null;
                if (be instanceof TileEntityMetallurgicInfuserFactory factory) {
                    secondaryBar = evolvedMekanism$addElement(gui,new GuiChemicalBar<>(gui, GuiChemicalBar.getProvider(factory.getInfusionTank(), be.getInfusionTanks(null)),
                            198, gui.inventoryLabelY + 9, 4, 52, false));
                    evolvedMekanism$addElement(gui,new GuiDumpButton<>(gui, factory, 190, gui.inventoryLabelY - 1));
                } else if (be instanceof TileEntityItemStackGasToItemStackFactory factory) {
                    secondaryBar = evolvedMekanism$addElement(gui,new GuiChemicalBar<>(gui, GuiChemicalBar.getProvider(factory.getGasTank(), be.getGasTanks(null)),
                            198, gui.inventoryLabelY + 9, 4, 52, false));
                    evolvedMekanism$addElement(gui,new GuiDumpButton<>(gui, factory, 190, gui.inventoryLabelY - 1));
                }
                if (secondaryBar != null) {
                    secondaryBar.warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, be.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
                }
            }


            int baseX = 9;
            int baseXMult = 19;
            for (int i = 0; i < be.tier.processes; i++) {
                int cacheIndex = i;
                addProgress(new GuiProgress(() -> be.getScaledProgress(1, cacheIndex), ProgressType.DOWN, gui, 4 + baseX + (i * baseXMult), 33))
                        .warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, be.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT, cacheIndex));
            }
            ci.cancel();
        }
    }


    @Unique
    private static <T extends GuiElement> T evolvedMekanism$addElement(GuiFactory factory, T element) {
        factory.renderables.add(element);
        ((List<GuiEventListener>) factory.children()).add(element);
        return element;
    }



}
