package fr.iglee42.evolvedmekanism.jei.categories;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.client.bars.GuiCustomDynamicHorizontalRateBar;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.multiblock.apt.APTMultiblockData;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.chemical.ItemStackChemicalToItemStackRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiDynamicHorizontalRateBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.lib.Color;
import mekanism.common.registries.MekanismItems;
import mekanism.common.util.text.EnergyDisplay;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class APTRecipeCategory extends BaseRecipeCategory<ItemStackGasToItemStackRecipe> {

    private final GuiGauge<?> inputGas;
    private final GuiSlot input;
    private final GuiSlot output;
    private final GuiCustomDynamicHorizontalRateBar bar;

    public APTRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<ItemStackGasToItemStackRecipe> recipeType) {
        super(helper, recipeType, EvolvedMekanismLang.APT.translate(), createIcon(helper, EMItems.BETTER_GOLD_INGOT), 3, 12, 168, 74);
        inputGas = addElement(GuiGasGauge.getDummy(GaugeType.STANDARD, this, 6, 17));
        input = addSlot(SlotType.INPUT, 27, 40);
        output = addSlot(SlotType.OUTPUT, 132, 40);
        addElement(new GuiInnerScreen(this, 47, 17, 82, 60, () -> {
            List<Component> list = new ArrayList<>();
            list.add(MekanismLang.STATUS.translate(MekanismLang.ACTIVE));
            list.add(MekanismLang.USING.translate(EnergyDisplay.of(EMConfig.general.aptEnergyConsumption.getOrDefault())));
            return list;
        }));
        addElement(new GuiEnergyGauge(new GuiEnergyGauge.IEnergyInfoHandler() {
            @Override
            public FloatingLong getEnergy() {
                return FloatingLong.ONE;
            }

            @Override
            public FloatingLong getMaxEnergy() {
                return FloatingLong.ONE;
            }
        },GaugeType.STANDARD,this, 151, 17));
        bar = addElement(new GuiCustomDynamicHorizontalRateBar(this, getBarProgressTimer(), 6, 79, 160,
                c->Color.WHITE));

    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, ItemStackGasToItemStackRecipe recipe, @NotNull IFocusGroup focusGroup) {
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.INPUT, inputGas, recipe.getChemicalInput().getRepresentations());
        initItem(builder, RecipeIngredientRole.INPUT, input, recipe.getItemInput().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, output, recipe.getOutputDefinition());
        bar.setColorFunction(p->Color.rgb(recipe.getChemicalInput().getRepresentations().get(0).getChemicalTint()));
    }
}