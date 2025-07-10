package fr.iglee42.evolvedmekanism.jei.categories;

import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.jei.HolderRecipeCategory;
import mekanism.client.recipe_viewer.type.RVRecipeTypeWrapper;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import static mekanism.client.recipe_viewer.RecipeViewerUtils.FULL_BAR;

public class ChemixerRecipeCategory extends HolderRecipeCategory<ChemixerRecipe> {

    private final GuiSlot input;
    private final GuiGauge<?> inputGas;
    private final GuiSlot extra;
    private final GuiSlot output;

    public ChemixerRecipeCategory(IGuiHelper helper, RVRecipeTypeWrapper<?,ChemixerRecipe,?> recipeType) {
        super(helper, recipeType);
        addElement(new GuiUpArrow(this, 68, 38));
        input = addSlot(SlotType.INPUT, 64, 17);
        inputGas = addElement(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 28, 13));
        extra = addSlot(SlotType.EXTRA, 64, 53);
        output = addSlot(SlotType.OUTPUT, 116, 35);
        addSlot(SlotType.POWER, 141, 35).with(SlotOverlay.POWER);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        addSimpleProgress(ProgressType.BAR, 86, 38);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, RecipeHolder<ChemixerRecipe> recipe, @NotNull IFocusGroup focusGroup) {
        initChemical(builder, RecipeIngredientRole.INPUT, inputGas, recipe.value().getInputGas().getRepresentations());
        initItem(builder, RecipeIngredientRole.INPUT, input, recipe.value().getInputMain().getRepresentations());
        initItem(builder, RecipeIngredientRole.INPUT, extra, recipe.value().getInputExtra().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, output, recipe.value().getOutputDefinition());
    }
}