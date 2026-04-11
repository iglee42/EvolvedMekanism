package fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes;

import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import mekanism.api.text.EnumColor;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.emi.MekanismEmiRecipeCategory;
import mekanism.client.recipe_viewer.emi.recipe.MekanismEmiHolderRecipe;
import mekanism.common.tile.component.config.DataType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

import static mekanism.client.recipe_viewer.RecipeViewerUtils.FULL_BAR;

public class SolidificationEMIRecipe extends MekanismEmiHolderRecipe<SolidificationRecipe> {

    public SolidificationEMIRecipe(MekanismEmiRecipeCategory category, RecipeHolder<SolidificationRecipe> recipeHolder) {
        super(category, recipeHolder);
        addInputDefinition(recipe.getInputSolid());
        addInputDefinition(recipe.getInputFluid());
        addInputDefinition(recipe.getFluidInputExtra());
        addItemOutputDefinition(recipe.getOutputDefinition());
    }


    @Override
    public void addWidgets(WidgetHolder widgets) {
        addSlot(widgets, SlotType.INPUT, 54,35, input(0)).appendTooltip((tooltip) -> recipe.shouldKeepItem() ? EmiTooltipComponents.of(EvolvedMekanismLang.TOOLTIP_NO_CONSUMED.translateColored(EnumColor.YELLOW)) : EmiTooltipComponents.of(Component.empty()));
        initTank(widgets,GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 28, 10),input(1));
        initTank(widgets,GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 5, 10),input(2));
        addSlot(widgets, SlotType.OUTPUT, 116,35, output(0)).recipeContext(this);
        addElement(widgets,new GuiVerticalPowerBar(this,FULL_BAR,164,15));
        addSimpleProgress(widgets, ProgressType.RIGHT, 77,38,100);
    }
}