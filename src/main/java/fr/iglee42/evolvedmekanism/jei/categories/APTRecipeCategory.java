package fr.iglee42.evolvedmekanism.jei.categories;

import com.mojang.serialization.Codec;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.client.bars.GuiCustomDynamicHorizontalRateBar;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import mekanism.api.recipes.ItemStackChemicalToItemStackRecipe;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.jei.BaseRecipeCategory;
import mekanism.client.recipe_viewer.jei.MekanismJEI;
import mekanism.client.recipe_viewer.type.RVRecipeTypeWrapper;
import mekanism.common.MekanismLang;
import mekanism.common.lib.Color;
import mekanism.common.util.text.EnergyDisplay;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class APTRecipeCategory extends BaseRecipeCategory<RecipeHolder<ItemStackChemicalToItemStackRecipe>> {

    private final GuiGauge<?> inputGas;
    private final GuiSlot input;
    private final GuiSlot output;
    private final GuiCustomDynamicHorizontalRateBar bar;

    public APTRecipeCategory(IGuiHelper helper, RVRecipeTypeWrapper<?,ItemStackChemicalToItemStackRecipe,?> recipeType) {
        super(helper, MekanismJEI.holderRecipeType(recipeType), EvolvedMekanismLang.APT.translate(),createIcon(helper, recipeType), recipeType.xOffset(), recipeType.yOffset(), recipeType.width(), recipeType.height());
        inputGas = addElement(GuiChemicalGauge.getDummy(GaugeType.STANDARD, this, 6, 17));
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
            public long getEnergy() {
                return 1;
            }

            @Override
            public long getMaxEnergy() {
                return 1;
            }
        },GaugeType.STANDARD,this, 151, 17));
        bar = addElement(new GuiCustomDynamicHorizontalRateBar(this, getBarProgressTimer(), 6, 79, 160,
                c->Color.WHITE));

    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, RecipeHolder<ItemStackChemicalToItemStackRecipe> recipe, @NotNull IFocusGroup focusGroup) {
        initChemical(builder, RecipeIngredientRole.INPUT, inputGas, recipe.value().getChemicalInput().getRepresentations());
        initItem(builder, RecipeIngredientRole.INPUT, input, recipe.value().getItemInput().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, output, recipe.value().getOutputDefinition());
        bar.setColorFunction(p->Color.rgb(recipe.value().getChemicalInput().getRepresentations().get(0).getChemicalTint()));
    }

    @NotNull
    @Override
    public ResourceLocation getRegistryName(RecipeHolder<ItemStackChemicalToItemStackRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public Codec<RecipeHolder<ItemStackChemicalToItemStackRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }
}