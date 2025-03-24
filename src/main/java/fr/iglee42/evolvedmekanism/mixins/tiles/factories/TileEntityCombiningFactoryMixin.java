package fr.iglee42.evolvedmekanism.mixins.tiles.factories;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.CombinerRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.slot.FactoryInputInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.factory.TileEntityCombiningFactory;
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

@Mixin(value = TileEntityCombiningFactory.class,remap = false)
public class TileEntityCombiningFactoryMixin extends TileEntityItemToItemFactory<CombinerRecipe> implements IDoubleRecipeLookupHandler.DoubleItemRecipeLookupHandler<CombinerRecipe> {

    @Shadow private InputInventorySlot extraSlot;

    protected TileEntityCombiningFactoryMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state, List<CachedRecipe.OperationTracker.RecipeError> errorTypes, Set<CachedRecipe.OperationTracker.RecipeError> globalErrorTypes) {
        super(blockProvider, pos, state, errorTypes, globalErrorTypes);
    }

    @Inject(method = "addSlots",at = @At(value = "INVOKE", target = "Lmekanism/common/tile/factory/TileEntityItemToItemFactory;addSlots(Lmekanism/common/capabilities/holder/slot/InventorySlotHelper;Lmekanism/api/IContentsListener;Lmekanism/api/IContentsListener;)V",shift = At.Shift.AFTER), cancellable = true)
    private void evolvedmekanism$moveSlot(InventorySlotHelper builder, IContentsListener listener, IContentsListener updateSortingListener, CallbackInfo ci){
        TileEntityCombiningFactory be = (TileEntityCombiningFactory) ((Object)this);
        if (EvolvedMekanism.isEvolvedMekanismTier(be.tier.getBaseTier())){
            builder.addSlot(extraSlot = InputInventorySlot.at(be::containsRecipeB, markAllMonitorsChanged(listener), 192, 143));
            extraSlot.setSlotType(ContainerSlotType.EXTRA);
            ci.cancel();
        }
    }

    @Unique
    public @NotNull IMekanismRecipeTypeProvider<CombinerRecipe, InputRecipeCache.DoubleItem<CombinerRecipe>> getRecipeType() {
        return null;
    }

    @Unique
    public @Nullable CombinerRecipe getRecipe(int cacheIndex) {
        return null;
    }

    @Unique
    public @NotNull CachedRecipe<CombinerRecipe> createNewCachedRecipe(@NotNull CombinerRecipe recipe, int cacheIndex) {
        return null;
    }

    @Unique
    public void onContentsChanged() {

    }

    @Unique
    protected boolean isCachedRecipeValid(@Nullable CachedRecipe<CombinerRecipe> cached, @NotNull ItemStack stack) {
        return false;
    }

    @Unique
    protected @Nullable CombinerRecipe findRecipe(int process, @NotNull ItemStack fallbackInput, @NotNull IInventorySlot outputSlot, @Nullable IInventorySlot secondaryOutputSlot) {
        return null;
    }

    @Unique
    protected int getNeededInput(CombinerRecipe recipe, ItemStack inputStack) {
        return 0;
    }

    @Unique
    public boolean isValidInputItem(@NotNull ItemStack stack) {
        return false;
    }
}
