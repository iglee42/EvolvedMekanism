package fr.iglee42.evolvedmekanism.jei.categories;

import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.registries.MekanismBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import org.jetbrains.annotations.NotNull;

public class AlloyerRecipeCategory extends BaseRecipeCategory<AlloyerRecipe> {

    private final GuiSlot input;
    private final GuiSlot extra;
    private final GuiSlot secondExtra;
    private final GuiSlot output;

    public AlloyerRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<AlloyerRecipe> recipeType) {
        super(helper, recipeType, EMBlocks.ALLOYER, 28, 16, 144, 54);
        addElement(new GuiUpArrow(this, 68, 38));
        input = addSlot(SlotType.INPUT, 64, 17);
        extra = addSlot(SlotType.EXTRA, 55, 53);
        secondExtra = addSlot(SlotType.EXTRA, 75, 53);
        output = addSlot(SlotType.OUTPUT, 116, 35);
        addSlot(SlotType.POWER, 39, 35).with(SlotOverlay.POWER);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        addSimpleProgress(ProgressType.BAR, 86, 38);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, AlloyerRecipe recipe, @NotNull IFocusGroup focusGroup) {
        initItem(builder, RecipeIngredientRole.INPUT, input, recipe.getMainInput().getRepresentations());
        initItem(builder, RecipeIngredientRole.INPUT, extra, recipe.getExtraInput().getRepresentations());
        initItem(builder, RecipeIngredientRole.INPUT, secondExtra, recipe.getTertiaryExtraInput().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, output, recipe.getOutputDefinition());
    }
}