package fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes;

import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.EMEmiRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.EMEmiRecipeCategory;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.EMEmiUtils;
import mekanism.api.text.EnumColor;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.common.tile.component.config.DataType;

public class SolidificationEmiRecipe extends EMEmiRecipe<SolidificationRecipe> {

    public SolidificationEmiRecipe(EMEmiRecipeCategory category, SolidificationRecipe recipe) {
        super(category, recipe);
        addInputDefinition(recipe.getInputSolid());
        addInputDefinition(recipe.getInputFluid());
        addInputDefinition(recipe.getFluidInputExtra());
        addItemOutputDefinition(recipe.getOutputDefinition());
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        SlotWidget inputSlot = addSlot(widgets, SlotType.INPUT, 54, 35, input(0));
        if (recipe.shouldKeepItem()) {
            inputSlot.appendTooltip(EvolvedMekanismLang.TOOLTIP_NO_CONSUMED.translateColored(EnumColor.YELLOW));
        }
        initTank(widgets, GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 28, 10), input(1));
        initTank(widgets, GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 5, 10), input(2));
        addSlot(widgets, SlotType.OUTPUT, 116, 35, output(0)).recipeContext(this);
        addElement(widgets, new GuiVerticalPowerBar(this, EMEmiUtils.FULL_BAR, 164, 15));
        addSimpleProgress(widgets, ProgressType.RIGHT, 77, 38, 100);
    }
}
