package fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes;

import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.iglee42.evolvedmekanism.recipes.FluidCombiningRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.recipe_viewer.emi.MekanismEmiRecipeCategory;
import mekanism.client.recipe_viewer.emi.recipe.MekanismEmiHolderRecipe;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.world.item.crafting.RecipeHolder;

public class FluidCombiningEmiRecipe extends MekanismEmiHolderRecipe<FluidCombiningRecipe> {

    public FluidCombiningEmiRecipe(MekanismEmiRecipeCategory category, RecipeHolder<FluidCombiningRecipe> recipeHolder) {
        super(category, recipeHolder);
        addInputDefinition(recipe.getMainInput());
        addInputDefinition(recipe.getExtraInput());
        addFluidOutputDefinition(recipe.getOutputDefinition());
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        initTank(widgetHolder, GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 18, 13), still(recipe.getMainInput()));
        initTank(widgetHolder, GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT_2), this, 40, 13), still(recipe.getExtraInput()));
        initTank(widgetHolder, GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), this, 140, 13), output(0)).recipeContext(this);
        addSimpleProgress(widgetHolder, ProgressType.LARGE_RIGHT, 72, 40, 100);
    }

    static EmiIngredient still(FluidStackIngredient ingredient) {
        return EmiIngredient.of(FluidCombiningRecipe.sourceFluids(ingredient.getRepresentations()).stream().map(NeoForgeEmiStack::of).toList());
    }
}
