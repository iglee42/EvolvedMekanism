package fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes;

import dev.emi.emi.api.widget.WidgetHolder;
import fr.iglee42.evolvedmekanism.client.bars.GuiCustomDynamicHorizontalRateBar;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import mekanism.api.recipes.ItemStackChemicalToItemStackRecipe;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.client.recipe_viewer.emi.MekanismEmiRecipeCategory;
import mekanism.client.recipe_viewer.emi.recipe.MekanismEmiHolderRecipe;
import mekanism.common.MekanismLang;
import mekanism.common.lib.Color;
import mekanism.common.util.text.EnergyDisplay;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;

public class APTEMIRecipe extends MekanismEmiHolderRecipe<ItemStackChemicalToItemStackRecipe> {

    public APTEMIRecipe(MekanismEmiRecipeCategory category, RecipeHolder<ItemStackChemicalToItemStackRecipe> recipeHolder) {
        super(category, recipeHolder);
        addInputDefinition(recipe.getChemicalInput());
        addInputDefinition(recipe.getItemInput());
        addItemOutputDefinition(recipe.getOutputDefinition());
    }


    @Override
    public void addWidgets(WidgetHolder widgets) {
        addSlot(widgets, SlotType.INPUT, 27, 40, input(1));
        initTank(widgets,GuiChemicalGauge.getDummy(GaugeType.STANDARD, this, 6, 17),input(0));
        addSlot(widgets, SlotType.OUTPUT, 132, 40, output(0)).recipeContext(this);
        addElement(widgets, new GuiInnerScreen(this, 47, 17, 82, 60, () -> {
            List<Component> list = new ArrayList<>();
            list.add(MekanismLang.STATUS.translate(MekanismLang.ACTIVE));
            list.add(MekanismLang.USING.translate(EnergyDisplay.of(EMConfig.general.aptEnergyConsumption.getOrDefault())));
            return list;
        }));
        addElement(widgets, new GuiEnergyGauge(new GuiEnergyGauge.IEnergyInfoHandler() {
            @Override
            public long getEnergy() {
                return 1;
            }

            @Override
            public long getMaxEnergy() {
                return 1;
            }
        },GaugeType.STANDARD,this, 151, 17));
        addElement(widgets, new GuiCustomDynamicHorizontalRateBar(this, RecipeViewerUtils.barProgressHandler(SharedConstants.TICKS_PER_SECOND), 6, 79, 160,
                c->Color.rgb(recipe.getChemicalInput().getRepresentations().get(0).getChemicalTint())));
    }
}