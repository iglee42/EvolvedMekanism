package fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes;

import dev.emi.emi.api.widget.WidgetHolder;
import fr.iglee42.evolvedmekanism.recipes.FluidAlloyingRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.EMEmiRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.EMEmiRecipeCategory;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.EMEmiUtils;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeInfo;
import mekanism.client.gui.element.gauge.GaugeOverlay;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;

public class FluidAlloyingEmiRecipe extends EMEmiRecipe<FluidAlloyingRecipe> {

    public FluidAlloyingEmiRecipe(EMEmiRecipeCategory category, FluidAlloyingRecipe recipe) {
        super(category, recipe);
        addFluidInputDefinition(recipe.getMainInputDefinition());
        addFluidInputDefinition(recipe.getExtraInputDefinition());
        addFluidInputDefinition(recipe.getTertiaryInputDefinition());
        addFluidOutputDefinition(recipe.getOutputDefinition());
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        initTank(widgets, GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 7, 16), input(0));
        initTank(widgets, GuiFluidGauge.getDummy(GaugeType.get(GaugeInfo.YELLOW, GaugeOverlay.STANDARD), this, 29, 16), input(1));
        initTank(widgets, GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT_2), this, 51, 16), input(2));
        initTank(widgets, GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), this, 131, 16), output(0)).recipeContext(this);
        addSlot(widgets, SlotType.INPUT, 8, 79).with(SlotOverlay.MINUS);
        addSlot(widgets, SlotType.EXTRA, 30, 79).with(SlotOverlay.MINUS);
        addSlot(widgets, SlotType.INPUT, 52, 79).with(SlotOverlay.MINUS);
        addSlot(widgets, SlotType.OUTPUT, 132, 79).with(SlotOverlay.PLUS);
        addSlot(widgets, SlotType.POWER, 153, 79).with(SlotOverlay.POWER);
        addElement(widgets, new GuiVerticalPowerBar(this, EMEmiUtils.FULL_BAR, 164, 15));
        addSimpleProgress(widgets, ProgressType.LARGE_RIGHT, 76, 42, 100);
    }
}
