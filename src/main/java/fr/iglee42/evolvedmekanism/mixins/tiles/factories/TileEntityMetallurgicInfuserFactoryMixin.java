package fr.iglee42.evolvedmekanism.mixins.tiles.factories;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.MetallurgicInfuserRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.slot.chemical.InfusionInventorySlot;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.IDoubleRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache;
import mekanism.common.tile.factory.TileEntityCombiningFactory;
import mekanism.common.tile.factory.TileEntityItemToItemFactory;
import mekanism.common.tile.factory.TileEntityMetallurgicInfuserFactory;
import mekanism.common.tile.interfaces.IHasDumpButton;
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

@Mixin(value = TileEntityMetallurgicInfuserFactory.class,remap = false)
public class TileEntityMetallurgicInfuserFactoryMixin extends TileEntityItemToItemFactory<MetallurgicInfuserRecipe> implements IHasDumpButton,
        IDoubleRecipeLookupHandler.ItemChemicalRecipeLookupHandler<InfuseType, InfusionStack, MetallurgicInfuserRecipe> {

    @Shadow private InfusionInventorySlot extraSlot;

    @Shadow private IInfusionTank infusionTank;

    protected TileEntityMetallurgicInfuserFactoryMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state, List<CachedRecipe.OperationTracker.RecipeError> errorTypes, Set<CachedRecipe.OperationTracker.RecipeError> globalErrorTypes) {
        super(blockProvider, pos, state, errorTypes, globalErrorTypes);
    }

    @Inject(method = "addSlots",at = @At(value = "INVOKE", target = "Lmekanism/common/tile/factory/TileEntityItemToItemFactory;addSlots(Lmekanism/common/capabilities/holder/slot/InventorySlotHelper;Lmekanism/api/IContentsListener;Lmekanism/api/IContentsListener;)V",shift = At.Shift.AFTER), cancellable = true)
    private void evolvedmekanism$moveSlot(InventorySlotHelper builder, IContentsListener listener, IContentsListener updateSortingListener, CallbackInfo ci){
        TileEntityMetallurgicInfuserFactory be = (TileEntityMetallurgicInfuserFactory) ((Object)this);
        if (EvolvedMekanism.isEvolvedMekanismTier(be.tier.getBaseTier())){
            int imageWidth = 176 +(38 *( be.tier.ordinal() - EMFactoryTier.OVERCLOCKED.ordinal() + 1)) + 9;
            int inventorySize = 9 * 20;
            int endInventory = (imageWidth / 2 + inventorySize / 2) - 10;
            builder.addSlot(extraSlot = InfusionInventorySlot.fillOrConvert(infusionTank, be::getLevel, listener, endInventory + 4, 143));

            ci.cancel();
        }
    }

    @Unique
    public void dump() {
        
    }

    @Unique
    protected boolean isCachedRecipeValid(@Nullable CachedRecipe<MetallurgicInfuserRecipe> cached, @NotNull ItemStack stack) {
        return false;
    }

    @Unique
    protected @Nullable MetallurgicInfuserRecipe findRecipe(int process, @NotNull ItemStack fallbackInput, @NotNull IInventorySlot outputSlot, @Nullable IInventorySlot secondaryOutputSlot) {
        return null;
    }

    @Unique
    protected int getNeededInput(MetallurgicInfuserRecipe recipe, ItemStack inputStack) {
        return 0;
    }

    @Unique
    public boolean isValidInputItem(@NotNull ItemStack stack) {
        return false;
    }

    @Unique
    public @NotNull IMekanismRecipeTypeProvider<MetallurgicInfuserRecipe, InputRecipeCache.ItemChemical<InfuseType, InfusionStack, MetallurgicInfuserRecipe>> getRecipeType() {
        return null;
    }

    @Unique
    public @Nullable MetallurgicInfuserRecipe getRecipe(int cacheIndex) {
        return null;
    }

    @Unique
    public @NotNull CachedRecipe<MetallurgicInfuserRecipe> createNewCachedRecipe(@NotNull MetallurgicInfuserRecipe recipe, int cacheIndex) {
        return null;
    }
}
