package fr.iglee42.evolvedmekanism.tiles;

import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.AbstractTieredPersonalStorageItemInventory;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageManager;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.security.ISecurityUtils;
import mekanism.api.security.SecurityMode;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.lib.inventory.personalstorage.AbstractPersonalStorageItemInventory;
import mekanism.common.lib.inventory.personalstorage.PersonalStorageManager;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.tile.TileEntityPersonalStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.BiPredicate;

public abstract class TileEntityTieredPersonalStorage extends TileEntityPersonalStorage {

    private final PersonalStorageTier tier;

    protected TileEntityTieredPersonalStorage(Holder<Block> blockProvider, BlockPos pos, BlockState state, PersonalStorageTier tier) {
        super(blockProvider, pos, state);
        this.tier = tier;
    }

    public PersonalStorageTier getTier() {
        return tier;
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canInteract = (stack, automationType) ->
                automationType == AutomationType.MANUAL || ISecurityUtils.INSTANCE.getEffectiveSecurityMode(this, isRemote()) == SecurityMode.PUBLIC;
        BlockTieredPersonalStorage<?,?> block = (BlockTieredPersonalStorage<?, ?>) getBlockState().getBlock();
        TieredPersonalStorageManager.createSlots(builder::addSlot, canInteract, listener,block.getTier());
        return builder.build();
    }


    @Override
    protected void applyImplicitComponents(@NotNull BlockEntity.DataComponentInput input) {
        super.applyImplicitComponents(input);
        if (!isRemote()) {
            UUID owner = input.get(MekanismDataComponents.OWNER);
            if (owner != null) {
                AbstractTieredPersonalStorageItemInventory storageItemInventory = TieredPersonalStorageManager.getInventoryForUnchecked(
                        getTier(),input.get(MekanismDataComponents.PERSONAL_STORAGE_ID), owner).orElse(null);
                if (storageItemInventory != null) {
                    List<IInventorySlot> inventorySlots = storageItemInventory.getInventorySlots(null);
                    for (int i = 0; i < inventorySlots.size(); i++) {
                        setStackInSlot(i, inventorySlots.get(i).getStack().copy());
                    }
                }
            }
        }
    }

}