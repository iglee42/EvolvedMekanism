package fr.iglee42.evolvedmekanism.recipeviewers.jei.categories;

import java.util.List;

import com.mojang.serialization.Codec;
import fr.iglee42.evolvedmekanism.recipes.FluidCombiningRecipe;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.jei.HolderRecipeCategory;
import mekanism.client.recipe_viewer.type.RVRecipeTypeWrapper;
import mekanism.common.inventory.container.slot.SlotOverlay;
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

import static mekanism.client.recipe_viewer.RecipeViewerUtils.FULL_BAR;

public class FluidCombiningJEIRecipeCategory extends HolderRecipeCategory<FluidCombiningRecipe> {

    private final GuiGauge<?> mainTank;
    private final GuiGauge<?> extraTank;
    private final GuiGauge<?> outputTank;

    public FluidCombiningJEIRecipeCategory(IGuiHelper helper, RVRecipeTypeWrapper<?, FluidCombiningRecipe, ?> recipeType) {
        super(helper, recipeType);
        mainTank = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 18, 13));
        extraTank = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT_2), this, 40, 13));
        outputTank = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), this, 140, 13));
        addElement(new GuiProgress(getSimpleProgressTimer(), ProgressType.LARGE_RIGHT, this, 72, 40));
        addSlot(SlotType.INPUT,19,76).with(SlotOverlay.MINUS);
        addSlot(SlotType.INPUT_2,41,76).with(SlotOverlay.MINUS);
        addSlot(SlotType.OUTPUT,141,76).with(SlotOverlay.PLUS);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, RecipeHolder<FluidCombiningRecipe> recipe, @NotNull IFocusGroup focusGroup) {
        initFluid(builder, RecipeIngredientRole.INPUT, mainTank, FluidCombiningRecipe.sourceFluids(recipe.value().getMainInput().getRepresentations()));
        initFluid(builder, RecipeIngredientRole.INPUT, extraTank, FluidCombiningRecipe.sourceFluids(recipe.value().getExtraInput().getRepresentations()));
        initFluid(builder, RecipeIngredientRole.OUTPUT, outputTank, FluidCombiningRecipe.sourceFluids(recipe.value().getOutputDefinition()));
    }

    @NotNull
    @Override
    public Codec<RecipeHolder<FluidCombiningRecipe>> getCodec(@NotNull ICodecHelper codecHelper, @NotNull IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }
}
