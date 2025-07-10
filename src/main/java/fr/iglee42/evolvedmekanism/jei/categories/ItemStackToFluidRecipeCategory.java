package fr.iglee42.evolvedmekanism.jei.categories;

import com.mojang.serialization.Codec;

import java.util.List;

import fr.iglee42.evolvedmekanism.impl.BasicItemStackToFluidRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.jei.HolderRecipeCategory;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.client.recipe_viewer.type.RVRecipeTypeWrapper;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class ItemStackToFluidRecipeCategory extends HolderRecipeCategory<ItemStackToFluidRecipe> {

    private final GuiGauge<?> outputTank;
    private final GuiSlot input;

    public ItemStackToFluidRecipeCategory(IGuiHelper helper, RVRecipeTypeWrapper<?, ItemStackToFluidRecipe,?> recipeType, boolean isConversion) {
        super(helper, recipeType);
        input = addSlot(SlotType.INPUT, 26, 36);
        outputTank = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), this, 131, 13));
        addElement(new GuiProgress(isConversion ? () -> 1 : getSimpleProgressTimer(), ProgressType.LARGE_RIGHT, this, 54, 40));
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, RecipeHolder<ItemStackToFluidRecipe> recipe, @NotNull IFocusGroup focusGroup) {
        initItem(builder, RecipeIngredientRole.INPUT, input, recipe.value().getInput().getRepresentations());
        List<FluidStack> outputDefinition = recipe.value().getOutputDefinition();
        initFluid(builder, RecipeIngredientRole.OUTPUT, outputTank, outputDefinition);
    }



    @NotNull
    @Override
    public Codec<RecipeHolder<ItemStackToFluidRecipe>> getCodec(@NotNull ICodecHelper codecHelper, @NotNull IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }
}