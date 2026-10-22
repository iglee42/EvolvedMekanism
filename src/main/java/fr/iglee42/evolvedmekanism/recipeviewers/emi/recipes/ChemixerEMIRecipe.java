package fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes;

import dev.emi.emi.api.widget.WidgetHolder;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.emi.MekanismEmiRecipeCategory;
import mekanism.client.recipe_viewer.emi.recipe.MekanismEmiHolderRecipe;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.world.item.crafting.RecipeHolder;

import static mekanism.client.recipe_viewer.RecipeViewerUtils.FULL_BAR;

public class ChemixerEMIRecipe extends MekanismEmiHolderRecipe<ChemixerRecipe> {

    public ChemixerEMIRecipe(MekanismEmiRecipeCategory category, RecipeHolder<ChemixerRecipe> recipeHolder) {
        super(category, recipeHolder);
        addInputDefinition(recipe.getInputMain());
        addInputDefinition(recipe.getInputExtra());
        addInputDefinition(recipe.getInputGas());
        addItemOutputDefinition(recipe.getOutputDefinition());
    }


    @Override
    public void addWidgets(WidgetHolder widgets) {
        addElement(widgets,new GuiUpArrow(this, 68, 38));
        addSlot(widgets, SlotType.INPUT, 64,17, input(0));
        initTank(widgets,GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 28, 13),input(2));
        addSlot(widgets, SlotType.EXTRA, 64,53, input(1));
        addSlot(widgets, SlotType.OUTPUT, 116,35, output(0)).recipeContext(this);
        addElement(widgets,new GuiVerticalPowerBar(this,FULL_BAR,164,15));
        addSimpleProgress(widgets, ProgressType.BAR, 86,38,100);
    }
}