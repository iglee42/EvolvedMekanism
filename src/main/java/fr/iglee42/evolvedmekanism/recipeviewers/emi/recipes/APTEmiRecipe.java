package fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes;

import java.util.ArrayList;
import java.util.List;

import dev.emi.emi.api.widget.WidgetHolder;
import fr.iglee42.evolvedmekanism.client.bars.GuiCustomDynamicHorizontalRateBar;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.EMEmiRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.EMEmiRecipeCategory;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.EMEmiUtils;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.common.MekanismLang;
import mekanism.common.lib.Color;
import mekanism.common.util.text.EnergyDisplay;
import net.minecraft.network.chat.Component;

public class APTEmiRecipe extends EMEmiRecipe<ItemStackGasToItemStackRecipe> {

    public APTEmiRecipe(EMEmiRecipeCategory category, ItemStackGasToItemStackRecipe recipe) {
        super(category, recipe);
        addInputDefinition(recipe.getChemicalInput());
        addInputDefinition(recipe.getItemInput());
        addItemOutputDefinition(recipe.getOutputDefinition());
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        addSlot(widgets, SlotType.INPUT, 27, 40, input(1));
        initTank(widgets, GuiGasGauge.getDummy(GaugeType.STANDARD, this, 6, 17), input(0));
        addSlot(widgets, SlotType.OUTPUT, 132, 40, output(0)).recipeContext(this);
        addElement(widgets, new GuiInnerScreen(this, 47, 17, 82, 60, () -> {
            List<Component> list = new ArrayList<>();
            list.add(MekanismLang.STATUS.translate(MekanismLang.ACTIVE));
            list.add(MekanismLang.USING.translate(EnergyDisplay.of(EMConfig.general.aptEnergyConsumption.getOrDefault())));
            return list;
        }));
        addElement(widgets, new GuiEnergyGauge(new GuiEnergyGauge.IEnergyInfoHandler() {
            @Override
            public FloatingLong getEnergy() {
                return FloatingLong.ONE;
            }

            @Override
            public FloatingLong getMaxEnergy() {
                return FloatingLong.ONE;
            }
        }, GaugeType.STANDARD, this, 151, 17));
        addElement(widgets, new GuiCustomDynamicHorizontalRateBar(this, EMEmiUtils.barProgressHandler(20), 6, 79, 160,
                c -> Color.rgb(recipe.getChemicalInput().getRepresentations().get(0).getChemicalTint())));
    }
}
