package fr.iglee42.evolvedmekanism.jei.categories;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import mekanism.api.text.EnumColor;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
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

public class SolidificationRecipeCategory extends HolderRecipeCategory<SolidificationRecipe> {


    private final GuiGauge<?> inputExtraFluid;
    private final GuiGauge<?> inputFluid;
    private final GuiSlot inputItem;
    private final GuiSlot outputItem;

    public SolidificationRecipeCategory(IGuiHelper helper, RVRecipeTypeWrapper<?,SolidificationRecipe,?> recipeType) {
        super(helper, recipeType);
        //Note: This previously had a lang key for a shorter string. Though ideally especially due to translations
        // we will eventually instead just make the text scale
        inputItem = addSlot(SlotType.INPUT, 54, 35);
        outputItem = addSlot(SlotType.OUTPUT, 116, 35);
        addSlot(SlotType.POWER, 141, 17).with(SlotOverlay.POWER);
        inputFluid = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 28, 10));
        inputExtraFluid = addElement(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 5, 10));
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        addSimpleProgress(ProgressType.RIGHT, 77, 38);
    }


    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, RecipeHolder<SolidificationRecipe> recipe, @NotNull IFocusGroup focusGroup) {
        initItem(builder, RecipeIngredientRole.INPUT, inputItem, recipe.value().getInputSolid().getRepresentations()).addRichTooltipCallback((recipeSlotView, tooltip) -> {
            if (recipe.value().shouldKeepItem())tooltip.add(EvolvedMekanismLang.TOOLTIP_NO_CONSUMED.translateColored(EnumColor.YELLOW));
        });
        initFluid(builder, RecipeIngredientRole.INPUT, inputFluid, recipe.value().getInputFluid().getRepresentations());
        initFluid(builder, RecipeIngredientRole.INPUT, inputExtraFluid, recipe.value().getFluidInputExtra().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, outputItem, recipe.value().getOutputDefinition());
    }
}