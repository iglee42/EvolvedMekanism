package fr.iglee42.evolvedmekanism.jei.categories;

import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.jei.HolderRecipeCategory;
import mekanism.client.recipe_viewer.type.RVRecipeTypeWrapper;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import static mekanism.client.recipe_viewer.RecipeViewerUtils.FULL_BAR;

public class AlloyerRecipeCategory extends HolderRecipeCategory<AlloyerRecipe> {

    private final GuiSlot input;
    private final GuiSlot extra;
    private final GuiSlot secondExtra;
    private final GuiSlot output;

    public AlloyerRecipeCategory(IGuiHelper helper, RVRecipeTypeWrapper<?,AlloyerRecipe,?> recipeType) {
        super(helper, recipeType);
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, RecipeHolder<AlloyerRecipe> recipe, @NotNull IFocusGroup focusGroup) {
        initItem(builder, RecipeIngredientRole.INPUT, input, recipe.value().getMainInput().getRepresentations());
        initItem(builder, RecipeIngredientRole.INPUT, extra, recipe.value().getExtraInput().getRepresentations());
        initItem(builder, RecipeIngredientRole.INPUT, secondExtra, recipe.value().getTertiaryExtraInput().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, output, recipe.value().getOutputDefinition());
    }

}