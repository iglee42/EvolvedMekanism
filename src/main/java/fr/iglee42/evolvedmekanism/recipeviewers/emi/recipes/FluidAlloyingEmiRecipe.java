package fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes;

import dev.emi.emi.api.widget.WidgetHolder;
import fr.iglee42.evolvedmekanism.recipes.FluidAlloyingRecipe;
import mekanism.client.gui.element.gauge.GaugeInfo;
import mekanism.client.gui.element.gauge.GaugeOverlay;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.recipe_viewer.emi.MekanismEmiRecipeCategory;
import mekanism.client.recipe_viewer.emi.recipe.MekanismEmiHolderRecipe;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.world.item.crafting.RecipeHolder;

public class FluidAlloyingEmiRecipe extends MekanismEmiHolderRecipe<FluidAlloyingRecipe> {

    public FluidAlloyingEmiRecipe(MekanismEmiRecipeCategory category, RecipeHolder<FluidAlloyingRecipe> recipeHolder) {
        super(category, recipeHolder);
        addInputDefinition(recipe.getMainInput());
        addInputDefinition(recipe.getExtraInput());
        addInputDefinition(recipe.getTertiaryExtraInput());
        addFluidOutputDefinition(recipe.getOutputDefinition());
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        initTank(widgetHolder, GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 18, 13), FluidCombiningEmiRecipe.still(recipe.getMainInput()));
        initTank(widgetHolder, GuiFluidGauge.getDummy(GaugeType.get(GaugeInfo.YELLOW, GaugeOverlay.STANDARD), this, 40, 13), FluidCombiningEmiRecipe.still(recipe.getExtraInput()));
        initTank(widgetHolder, GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT_2), this, 62, 13), FluidCombiningEmiRecipe.still(recipe.getTertiaryExtraInput()));
        initTank(widgetHolder, GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), this, 140, 13), output(0)).recipeContext(this);
        addSimpleProgress(widgetHolder, ProgressType.LARGE_RIGHT, 86, 40, 100);
    }
}
