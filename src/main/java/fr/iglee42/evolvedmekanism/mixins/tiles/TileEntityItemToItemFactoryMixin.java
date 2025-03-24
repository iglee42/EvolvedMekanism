package fr.iglee42.evolvedmekanism.mixins.tiles;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.FactoryInputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.factory.TileEntityFactory;
import mekanism.common.tile.factory.TileEntityItemToItemFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Set;

@Mixin(value = TileEntityItemToItemFactory.class,remap = false)
public class TileEntityItemToItemFactoryMixin<RECIPE extends MekanismRecipe> extends TileEntityFactory<RECIPE> {

    @Shadow protected IInputHandler<@NotNull ItemStack>[] inputHandlers;

    @Shadow protected IOutputHandler<@NotNull ItemStack>[] outputHandlers;

    protected TileEntityItemToItemFactoryMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state, List<CachedRecipe.OperationTracker.RecipeError> errorTypes, Set<CachedRecipe.OperationTracker.RecipeError> globalErrorTypes) {
        super(blockProvider, pos, state, errorTypes, globalErrorTypes);
    }

    @Inject(method = "addSlots",at = @At("HEAD"), cancellable = true)
    private void evolvedmekanism$moveSlot(InventorySlotHelper builder, IContentsListener listener, IContentsListener updateSortingListener, CallbackInfo ci){
        if (EvolvedMekanism.isEvolvedMekanismTier(tier.getBaseTier())){
            inputHandlers = new IInputHandler[tier.processes];
            outputHandlers = new IOutputHandler[tier.processes];
            processInfoSlots = new TileEntityFactory.ProcessInfo[tier.processes];
            int baseX = 9;
            int baseXMult = 19;
            for (int i = 0; i < tier.processes; i++) {
                int xPos = baseX + (i * baseXMult);
                OutputInventorySlot outputSlot = OutputInventorySlot.at(updateSortingListener, xPos, 57);
                //Note: As we are an item factory that has comparator's based on items we can just use the monitor as a listener directly
                FactoryInputInventorySlot inputSlot = FactoryInputInventorySlot.create(this, i, outputSlot, recipeCacheLookupMonitors[i], xPos, 13);
                int index = i;
                builder.addSlot(inputSlot).tracksWarnings(slot -> slot.warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT, index)));
                builder.addSlot(outputSlot).tracksWarnings(slot -> slot.warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, index)));
                inputHandlers[i] = InputHelper.getInputHandler(inputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
                outputHandlers[i] = OutputHelper.getOutputHandler(outputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
                processInfoSlots[i] = new TileEntityFactory.ProcessInfo(i, inputSlot, outputSlot, null);
            }
            ci.cancel();
        }
    }

    @Unique
    protected void addSlots(InventorySlotHelper builder, IContentsListener listener, IContentsListener updateSortingListener) { }

    @Unique
    protected boolean isCachedRecipeValid(@Nullable CachedRecipe<RECIPE> cached, @NotNull ItemStack stack) {
        return false;
    }

    @Unique
    protected @Nullable RECIPE findRecipe(int process, @NotNull ItemStack fallbackInput, @NotNull IInventorySlot outputSlot, @Nullable IInventorySlot secondaryOutputSlot) {
        return null;
    }

    @Unique
    protected int getNeededInput(RECIPE recipe, ItemStack inputStack) {
        return 0;
    }

    @Unique
    public boolean isValidInputItem(@NotNull ItemStack stack) {
        return false;
    }

    @Unique
    public @NotNull IMekanismRecipeTypeProvider<RECIPE, ?> getRecipeType() {
        return null;
    }

    @Unique
    public @Nullable RECIPE getRecipe(int cacheIndex) {
        return null;
    }

    @Unique
    public @NotNull CachedRecipe<RECIPE> createNewCachedRecipe(@NotNull RECIPE recipe, int cacheIndex) {
        return null;
    }
}
