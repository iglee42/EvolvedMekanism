package fr.iglee42.evolvedmekanism.recipeviewers.emi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.widget.MekanismEmiWidget;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.widget.MekanismTankEmiWidget;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiElement;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public abstract class EMEmiRecipe<RECIPE extends MekanismRecipe> extends AbstractContainerEventHandler implements EmiRecipe, IGuiWrapper {

    private final List<EmiIngredient> inputs = new ArrayList<>();
    private final List<EmiStack> outputs = new ArrayList<>();
    private final List<EmiIngredient> renderOutputs = new ArrayList<>();
    private final EMEmiRecipeCategory category;
    private final ResourceLocation id;
    protected final RECIPE recipe;
    private final int xOffset;
    private final int yOffset;
    private final int width;
    private final int height;

    protected EMEmiRecipe(EMEmiRecipeCategory category, RECIPE recipe) {
        this.category = category;
        this.recipe = recipe;
        this.id = recipe.getId();
        this.xOffset = category.xOffset();
        this.yOffset = category.yOffset();
        this.width = category.width();
        this.height = category.height();
    }

    protected EmiIngredient input(int index) {
        return inputs.get(index);
    }

    protected EmiIngredient output(int index) {
        return renderOutputs.get(index);
    }

    protected void addInputDefinition(ItemStackIngredient ingredient) {
        inputs.add(EMEmiIngredients.items(ingredient));
    }

    protected void addInputDefinition(FluidStackIngredient ingredient) {
        inputs.add(EMEmiIngredients.fluids(ingredient));
    }

    protected void addInputDefinition(GasStackIngredient ingredient) {
        inputs.add(EMEmiIngredients.gases(ingredient));
    }

    protected void addItemOutputDefinition(List<ItemStack> definition) {
        addOutputDefinition(definition.stream().map(EmiStack::of).toList());
    }

    protected void addFluidOutputDefinition(List<FluidStack> definition) {
        addOutputDefinition(definition.stream().map(EMEmiIngredients::fluidOutput).toList());
    }

    protected void addOutputDefinition(List<EmiStack> stacks) {
        if (stacks.isEmpty()) {
            outputs.add(EmiStack.EMPTY);
            renderOutputs.add(EmiStack.EMPTY);
        } else {
            outputs.addAll(stacks);
            renderOutputs.add(EmiIngredient.of(stacks));
        }
    }

    @Override
    public int getLeft() {
        return xOffset;
    }

    @Override
    public int getTop() {
        return yOffset;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Font getFont() {
        return Minecraft.getInstance().font;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Nullable
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }

    @Override
    public int getDisplayWidth() {
        return getWidth();
    }

    @Override
    public int getDisplayHeight() {
        return getHeight();
    }

    @Override
    public boolean supportsRecipeTree() {
        return true;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }

    protected SlotWidget addSlot(WidgetHolder widgetHolder, SlotType type, int x, int y, EmiIngredient ingredient) {
        GuiSlot slot = addSlot(widgetHolder, type, x, y);
        return initItem(widgetHolder, slot.getX(), slot.getY(), ingredient);
    }

    protected GuiSlot addSlot(WidgetHolder widgetHolder, SlotType type, int x, int y) {
        return addElement(widgetHolder, new GuiSlot(type, this, x - 1, y - 1));
    }

    protected GuiProgress addSimpleProgress(WidgetHolder widgetHolder, ProgressType type, int x, int y, int processTime) {
        return addElement(widgetHolder, new GuiProgress(EMEmiUtils.progressHandler(processTime), type, this, x, y));
    }

    protected <ELEMENT extends GuiElement> ELEMENT addElement(WidgetHolder widgetHolder, ELEMENT element) {
        widgetHolder.add(new MekanismEmiWidget(element, false));
        return element;
    }

    protected SlotWidget initItem(WidgetHolder widgetHolder, int x, int y, EmiIngredient ingredient) {
        return widgetHolder.addSlot(ingredient, x, y).drawBack(false);
    }

    protected SlotWidget initTank(WidgetHolder widgetHolder, GuiElement element, EmiIngredient ingredient) {
        addElement(widgetHolder, element);
        return widgetHolder.add(new MekanismTankEmiWidget(ingredient, element, Math.max(1, ingredient.getAmount()))).drawBack(false);
    }
}
