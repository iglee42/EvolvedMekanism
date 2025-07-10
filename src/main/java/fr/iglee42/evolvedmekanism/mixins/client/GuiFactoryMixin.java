package fr.iglee42.evolvedmekanism.mixins.client;

import fr.iglee42.evolvedmekanism.client.buttons.GuiSmallerDumpButton;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.element.GuiElement;
import mekanism.client.gui.element.bar.GuiChemicalBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.gui.element.tab.GuiSortingTab;
import mekanism.client.gui.machine.GuiFactory;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.ISupportsWarning;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.factory.TileEntityItemStackChemicalToItemStackFactory;
import mekanism.common.tile.factory.TileEntitySawingFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(value = GuiFactory.class,remap = false)
public abstract class GuiFactoryMixin {

    //@Shadow protected abstract GuiProgress addProgress(GuiProgress progressBar);

    @Unique
    private TileEntityFactory<?> evolvedMekanism$be;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void evolvedmekanism$makeGuiBigger(MekanismTileContainer container, Inventory inv, Component title, CallbackInfo ci) {
        Object obj = this;
        GuiFactory gui = (GuiFactory) obj;
        evolvedMekanism$be = (TileEntityFactory<?>) container.getTileEntity();
        if (((TileEntityFactory<?>)container.getTileEntity()).tier.ordinal() >= EMFactoryTier.OVERCLOCKED.ordinal()) {
            if (evolvedMekanism$be.hasSecondaryResourceBar() && !(evolvedMekanism$be instanceof TileEntitySawingFactory)) {
                gui.imageHeight -= 11;
                gui.inventoryLabelY = 75;
            }

            gui.imageWidth +=(38 *( ((TileEntityFactory<?>) container.getTileEntity()).tier.ordinal() - EMFactoryTier.OVERCLOCKED.ordinal() + 1)) + 9;
            gui.inventoryLabelX = gui.imageWidth / 2 - Minecraft.getInstance().font.width(gui.playerInventoryTitle) / 2;

        }
    }

    @Inject(method = "addGuiElements", at = @At(value = "INVOKE", target = "Lmekanism/client/gui/GuiConfigurableTile;addGuiElements()V",shift = At.Shift.AFTER), cancellable = true)
    private void evolvedmekanism$changeElementPoses(CallbackInfo ci) {
        if (evolvedMekanism$be.tier.ordinal() >= EMFactoryTier.OVERCLOCKED.ordinal()){
            Object obj = this;
            GuiFactory gui = (GuiFactory) obj;
            AtomicInteger energySlotX = new AtomicInteger();
            gui.getMenu().getInventoryContainerSlots().stream().filter(slot->slot.getSlotType().equals(ContainerSlotType.POWER)).findFirst().ifPresent(s-> energySlotX.set(s.x));
            int energyBarOffset = 5;
            evolvedMekanism$addElement(gui,new GuiVerticalPowerBar(gui, evolvedMekanism$be.getEnergyContainer(), energySlotX.get() + energyBarOffset, gui.inventoryLabelY + 9, 52))
                    .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, evolvedMekanism$be.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY, 0));
            evolvedMekanism$addElement(gui,new GuiSortingTab(gui, evolvedMekanism$be));
            evolvedMekanism$addElement(gui,new GuiEnergyTab(gui, evolvedMekanism$be.getEnergyContainer(), evolvedMekanism$be::getLastUsage));
            if (evolvedMekanism$be.hasSecondaryResourceBar()) {
                ISupportsWarning<?> secondaryBar = null;
                AtomicInteger extraSlotX = new AtomicInteger();
                int imageWidth = 176 +(38 *( evolvedMekanism$be.tier.ordinal() - EMFactoryTier.OVERCLOCKED.ordinal() + 1)) + 9;
                int inventorySize = 9 * 20;
                int endInventory = (imageWidth / 2 + inventorySize / 2) - 10;
                extraSlotX.set(endInventory + 4);
                int extraBarOffset = 5;
             if (evolvedMekanism$be instanceof TileEntityItemStackChemicalToItemStackFactory factory) {
                    secondaryBar = evolvedMekanism$addElement(gui,new GuiChemicalBar(gui, GuiChemicalBar.getProvider(factory.getChemicalTank(), evolvedMekanism$be.getChemicalTanks(null)),
                            extraSlotX.get() + extraBarOffset, gui.inventoryLabelY + 9, 4, 52, false));
                    evolvedMekanism$addElement(gui,new GuiSmallerDumpButton<>(gui, factory, extraSlotX.get() - 2, gui.inventoryLabelY));
                }
                if (secondaryBar != null) {
                    secondaryBar.warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, evolvedMekanism$be.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT, 0));
                }
            }


            int baseX = 9;
            int baseXMult = 19;
            for (int i = 0; i < evolvedMekanism$be.tier.processes; i++) {
                int cacheIndex = i;
                evolvedMekanism$addElement(gui,new GuiProgress(() -> evolvedMekanism$be.getScaledProgress(1, cacheIndex), ProgressType.DOWN, gui, 4 + baseX + (i * baseXMult), 33))
                        .recipeViewerCategory(evolvedMekanism$be)
                        //Only can happen if recipes change because inputs are sanitized in the factory based on the output
                        .warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, evolvedMekanism$be.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT, cacheIndex));
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

    /*@Inject(method = "addProgress", at = @At("HEAD"), cancellable = true)
    private void evolvedmekanism$fixProgressBar(GuiProgress progressBar, CallbackInfoReturnable<GuiProgress> cir) {
        Object obj = this;
        GuiFactory gui = (GuiFactory) obj;
        if (evolvedMekanism$be.getFactoryType().equals(EMFactoryType.ALLOYING)){
            cir.setReturnValue(evolvedMekanism$addElement(gui,progressBar.jeiCategories(EMJEI.ALLOYING)));
        }
    }*/


}
