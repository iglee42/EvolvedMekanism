package fr.iglee42.evolvedmekanism.inventory.personalstorage;

import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import fr.iglee42.evolvedmekanism.utils.EMDataHandlerUtils;
import mekanism.api.AutomationType;
import mekanism.api.DataHandlerUtils;
import mekanism.api.IContentsListener;
import mekanism.api.NBTConstants;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.lib.MekanismSavedData;
import mekanism.common.lib.inventory.personalstorage.AbstractPersonalStorageItemInventory;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.SecurityUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNotNullByDefault
public class TieredPersonalStorageManager {
    private static final Map<UUID, TieredPersonalStorageData> STORAGE_BY_PLAYER_UUID = new HashMap<>();

    private static Optional<TieredPersonalStorageData> forOwner(UUID playerUUID) {
        if (EffectiveSide.get().isClient()) {
            return Optional.empty();
        }
        return Optional.of(STORAGE_BY_PLAYER_UUID.computeIfAbsent(playerUUID, uuid -> MekanismSavedData.createSavedData(TieredPersonalStorageData::new, "tiered_personal_storage" + File.separator + uuid)));
    }

    /**
     * Only call on the server. Gets or creates an inventory for the supplied stack
     *
     * @param stack Personal storage ItemStack (type not checked) - will be modified if it didn't have an inventory id
     * @return the existing or new inventory
     */
    public static Optional<AbstractTieredPersonalStorageItemInventory> getInventoryFor(ItemStack stack) {
        UUID owner = SecurityUtils.get().getOwnerUUID(stack);
        if (owner == null) {
            throw new IllegalStateException("Stack has no owner!");
        }
        UUID invId = getInventoryId(stack);
        if (!(stack.getItem() instanceof ItemBlockTieredPersonalStorage<?> item)) throw new IllegalStateException("Item isn't ItemBlockTieredPersonalStorage");
        return forOwner(owner).map(data -> {
            AbstractTieredPersonalStorageItemInventory storageItemInventory = data.getOrAddInventory(invId,item.getTier());
            //TODO - After 1.20: Remove legacy loading
            ListTag legacyData = ItemDataUtils.getList(stack, NBTConstants.ITEMS);
            if (!legacyData.isEmpty()) {
                DataHandlerUtils.readContainers(storageItemInventory.getInventorySlots(null), legacyData);
                ItemDataUtils.removeData(stack, NBTConstants.ITEMS);
            }

            return storageItemInventory;
        });

    }

    public static boolean createInventoryFor(PersonalStorageTier tier,ItemStack stack, List<IInventorySlot> contents) {
        UUID owner = SecurityUtils.get().getOwnerUUID(stack);
        if (owner == null || contents.size() != tier.getSlotCount()) {
            //No owner or wrong number of slots, something went wrong
            return false;
        }
        //Get a new inventory id
        forOwner(owner).ifPresent(inv -> inv.addInventory(getInventoryId(stack), contents,tier));
        return true;
    }

    /**
     * Only call on the server
     * <p>
     * Version of {@link #getInventoryFor(ItemStack)} which will NOT create an inventory if none exists already.
     * The stack will only be modified if it contained a legacy inventory
     *
     * @param stack Personal storage ItemStack
     * @return the existing or converted inventory, or an empty optional if none exists in saved data nor legacy data
     */
    public static Optional<AbstractTieredPersonalStorageItemInventory> getInventoryIfPresent(ItemStack stack) {
        UUID owner = SecurityUtils.get().getOwnerUUID(stack);
        UUID invId = getInventoryIdNullable(stack);
        //TODO - After 1.20: Remove legacy loading
        boolean hasLegacyData = ItemDataUtils.hasData(stack, NBTConstants.ITEMS, Tag.TAG_LIST);
        return owner != null && (invId != null || hasLegacyData) ? getInventoryFor(stack) : Optional.empty();
    }

    public static void deleteInventory(ItemStack stack) {
        UUID owner = SecurityUtils.get().getOwnerUUID(stack);
        UUID invId = getInventoryIdNullable(stack);
        if (owner != null && invId != null) {
            forOwner(owner).ifPresent(inv->inv.removeInventory(invId));
        }
    }

    @NotNull
    private static UUID getInventoryId(ItemStack stack) {
        UUID invId = getInventoryIdNullable(stack);
        if (invId == null) {
            invId = UUID.randomUUID();
            ItemDataUtils.setUUID(stack, NBTConstants.PERSONAL_STORAGE_ID, invId);
        }
        return invId;
    }

    @Nullable
    private static UUID getInventoryIdNullable(ItemStack stack) {
        return ItemDataUtils.getUniqueID(stack, NBTConstants.PERSONAL_STORAGE_ID);
    }

    public static void reset() {
        STORAGE_BY_PLAYER_UUID.clear();
    }

    public static void createSlots(Consumer<IInventorySlot> slotConsumer, BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canInteract, @Nullable IContentsListener listener, PersonalStorageTier tier) {
        for (int slotY = 0; slotY < tier.rows; slotY++) {
            for (int slotX = 0; slotX < tier.columns; slotX++) {
                slotConsumer.accept(BasicInventorySlot.at(canInteract, canInteract, listener, 8 + slotX * 18, 18 + slotY * 18));
            }
        }
    }

}