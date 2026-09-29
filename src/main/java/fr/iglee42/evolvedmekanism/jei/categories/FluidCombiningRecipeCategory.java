package fr.iglee42.evolvedmekanism.jei.categories;

import fr.iglee42.evolvedmekanism.recipes.FluidCombiningRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import org.jetbrains.annotations.NotNull;

public class FluidCombiningRecipeCategory extends BaseRecipeCategory<FluidCombiningRecipe> {

    private final GuiGauge<?> input;
    private final GuiGauge<?> extra;
    private final GuiGauge<?> output;

    public FluidCombiningRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<FluidCombiningRecipe> recipeType) {
        super(helper, recipeType, EMBlocks.FLUID_COMBINER, 5, 8, 164, 78);
        input = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 7, 16));
        extra = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT_2), this, 29, 16));
        output = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), this, 131, 16));
        addSlot(SlotType.INPUT, 8, 79).with(SlotOverlay.MINUS);
        addSlot(SlotType.EXTRA, 30, 79).with(SlotOverlay.MINUS);
        addSlot(SlotType.OUTPUT, 132, 79).with(SlotOverlay.PLUS);
        addSlot(SlotType.POWER, 153, 79).with(SlotOverlay.POWER);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        addSimpleProgress(ProgressType.LARGE_RIGHT, 65, 42);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, FluidCombiningRecipe recipe, @NotNull IFocusGroup focusGroup) {
        initFluid(builder, RecipeIngredientRole.INPUT, input, recipe.getMainInputDefinition());
        initFluid(builder, RecipeIngredientRole.INPUT, extra, recipe.getExtraInputDefinition());
        initFluid(builder, RecipeIngredientRole.OUTPUT, output, recipe.getOutputDefinition());
    }
}
