package fr.iglee42.evolvedmekanism.inventory.personalstorage;

import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.security.IItemSecurityUtils;
import mekanism.common.Mekanism;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.lib.MekanismSavedData;
import mekanism.common.lib.inventory.personalstorage.AbstractPersonalStorageItemInventory;
import mekanism.common.registries.MekanismDataComponents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.util.thread.EffectiveSide;
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
        return Optional.of(STORAGE_BY_PLAYER_UUID.computeIfAbsent(playerUUID, uuid -> MekanismSavedData.createSavedData(TieredPersonalStorageData::new, "personal_storage" + File.separator + uuid)));
    }

    /**
     * Only call on the server. Gets or creates an inventory for the supplied stack
     *
     * @param stack Personal storage ItemStack (type not checked) - will be modified if it didn't have an inventory id
     *
     * @return the existing or new inventory
     */
    public static Optional<AbstractTieredPersonalStorageItemInventory> getInventoryFor(ItemStack stack) {
        UUID owner = IItemSecurityUtils.INSTANCE.getOwnerUUID(stack);
        if (owner == null) {
            Mekanism.logger.error("Storage inventory asked for but stack has no owner! {}", stack, new Exception());
            return Optional.empty();
        }
        return getInventoryFor(stack, owner);
    }

    /**
     * Only call on the server. Gets or creates an inventory for the supplied stack
     *
     * @param stack Personal storage ItemStack (type not checked) - will be modified if it didn't have an inventory id
     * @param owner The owner of the stack
     *
     * @return the existing or new inventory
     */
    public static Optional<AbstractTieredPersonalStorageItemInventory> getInventoryFor(ItemStack stack, @NotNull UUID owner) {
        UUID invId = getInventoryId(stack);
        if (!(stack.getItem() instanceof ItemBlockTieredPersonalStorage<?> item)) throw new IllegalStateException("Item isn't ItemBlockTieredPersonalStorage");
        return getInventoryForUnchecked(item.getTier(),invId, owner);
    }

    /**
     * Only call on the server. Gets an inventory for the supplied stack
     *
     * @param tier
     * @param inventoryId Personal storage inventory id
     * @param owner       The owner of the stack
     * @return the existing or new inventory
     */
    public static Optional<AbstractTieredPersonalStorageItemInventory> getInventoryForUnchecked(PersonalStorageTier tier, @Nullable UUID inventoryId, @NotNull UUID owner) {
        if (inventoryId == null) {
            return Optional.empty();
        }
        Optional<TieredPersonalStorageData> data = forOwner(owner);
        //noinspection OptionalIsPresent - Capturing lambda
        if (data.isPresent()) {
            return Optional.of(data.get().getOrAddInventory(inventoryId,tier));
        }
        return Optional.empty();
    }

    public static boolean createInventoryFor(PersonalStorageTier tier,HolderLookup.Provider provider, ItemStack stack, List<IInventorySlot> contents) {
        UUID owner = IItemSecurityUtils.INSTANCE.getOwnerUUID(stack);
        if (owner == null || contents.size() != 54) {
            //No owner or wrong number of slots, something went wrong
            return false;
        }
        //Get a new inventory id
        Optional<TieredPersonalStorageData> data = forOwner(owner);
        //noinspection OptionalIsPresent - Capturing lambda
        if (data.isPresent()) {
            data.get().addInventory(provider, getInventoryId(stack), contents,tier);
        }
        return true;
    }

    /**
     * Only call on the server
     * <p>
     * Version of {@link #getInventoryFor(ItemStack)} which will NOT create an inventory if none exists already. The stack will only be modified if it contained a legacy
     * inventory
     *
     * @param stack Personal storage ItemStack
     *
     * @return the existing or converted inventory, or an empty optional if none exists in saved data nor legacy data
     */
    public static Optional<AbstractTieredPersonalStorageItemInventory> getInventoryIfPresent(ItemStack stack) {
        UUID owner = IItemSecurityUtils.INSTANCE.getOwnerUUID(stack);
        return owner != null && stack.has(MekanismDataComponents.PERSONAL_STORAGE_ID) ? getInventoryFor(stack, owner) : Optional.empty();
    }

    public static void deleteInventory(ItemStack stack) {
        UUID owner = IItemSecurityUtils.INSTANCE.getOwnerUUID(stack);
        if (owner != null) {
            UUID storageId = stack.remove(MekanismDataComponents.PERSONAL_STORAGE_ID);
            if (storageId != null) {
                //If there actually was an id stored then remove the corresponding inventory
                Optional<TieredPersonalStorageData> data = forOwner(owner);
                //noinspection OptionalIsPresent - Capturing lambda
                if (data.isPresent()) {
                    data.get().removeInventory(storageId);
                }
            }
        }
    }

    @NotNull
    private static UUID getInventoryId(ItemStack stack) {
        UUID invId = stack.get(MekanismDataComponents.PERSONAL_STORAGE_ID);
        if (invId == null) {
            invId = UUID.randomUUID();
            stack.set(MekanismDataComponents.PERSONAL_STORAGE_ID, invId);
        }
        return invId;
    }

    public static void reset() {
        STORAGE_BY_PLAYER_UUID.clear();
    }

    public static void createSlots(Consumer<IInventorySlot> slotConsumer, BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canInteract, IContentsListener listener, PersonalStorageTier tier) {
        for (int slotY = 0; slotY < tier.rows; slotY++) {
            for (int slotX = 0; slotX < tier.columns; slotX++) {
                slotConsumer.accept(BasicInventorySlot.at(canInteract, canInteract, listener, 8 + slotX * 18, 18 + slotY * 18));
            }
        }
    }

    public static void transferFromBasic(AbstractPersonalStorageItemInventory oldInventory, ItemStack stack) {
        getInventoryFor(stack).ifPresent(inventory->{
            for (int i = 0; i < oldInventory.getSlots(); i++) {
                inventory.setStackInSlot(i,oldInventory.getStackInSlot(i));
            }
        });

    }

    public static void transferToNew(ItemStack oldItem, ItemStack stack) {
         getInventoryFor(oldItem).ifPresent(oldInventory->{
             getInventoryFor(stack).ifPresent(inventory->{
                 for (int i = 0; i < oldInventory.getSlots(); i++) {
                     inventory.setStackInSlot(i,oldInventory.getStackInSlot(i));
                 }
             });
         });
    }
}