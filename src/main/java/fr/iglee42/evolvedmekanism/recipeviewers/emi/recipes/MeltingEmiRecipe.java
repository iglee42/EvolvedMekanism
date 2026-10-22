package fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes;

import dev.emi.emi.api.widget.WidgetHolder;
import fr.iglee42.evolvedmekanism.recipes.MeltingRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.EMEmiRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.EMEmiRecipeCategory;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.common.tile.component.config.DataType;

public class MeltingEmiRecipe extends EMEmiRecipe<MeltingRecipe> {

    public MeltingEmiRecipe(EMEmiRecipeCategory category, MeltingRecipe recipe) {
        super(category, recipe);
        addInputDefinition(recipe.getInput());
        addFluidOutputDefinition(recipe.getOutputDefinition());
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        initTank(widgets, GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), this, 131, 13), output(0)).recipeContext(this);
        addSlot(widgets, SlotType.INPUT, 26, 36, input(0));
        addSimpleProgress(widgets, ProgressType.LARGE_RIGHT, 64, 40, 100);
    }
}
