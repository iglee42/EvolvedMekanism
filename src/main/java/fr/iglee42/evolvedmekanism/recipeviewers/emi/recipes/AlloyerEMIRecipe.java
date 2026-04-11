package fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes;

import dev.emi.emi.api.widget.WidgetHolder;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.emi.MekanismEmiRecipeCategory;
import mekanism.client.recipe_viewer.emi.recipe.MekanismEmiHolderRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import static mekanism.client.recipe_viewer.RecipeViewerUtils.FULL_BAR;

public class AlloyerEMIRecipe extends MekanismEmiHolderRecipe<AlloyerRecipe> {

    public AlloyerEMIRecipe(MekanismEmiRecipeCategory category, RecipeHolder<AlloyerRecipe> recipeHolder) {
        super(category, recipeHolder);
        addInputDefinition(recipe.getMainInput());
        addInputDefinition(recipe.getExtraInput());
        addInputDefinition(recipe.getTertiaryExtraInput());
        addItemOutputDefinition(recipe.getOutputDefinition());
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        addElement(widgets,new GuiUpArrow(this, 68, 38));
        addSlot(widgets, SlotType.INPUT, 64,17, input(0));
        addSlot(widgets, SlotType.EXTRA, 55,53, input(1));
        addSlot(widgets, SlotType.EXTRA, 75,53, input(2));
        addSlot(widgets, SlotType.OUTPUT, 116,35, output(0)).recipeContext(this);
        addElement(widgets,new GuiVerticalPowerBar(this,FULL_BAR,164,15));
        addSimpleProgress(widgets, ProgressType.BAR, 86,38,100);
    }
}