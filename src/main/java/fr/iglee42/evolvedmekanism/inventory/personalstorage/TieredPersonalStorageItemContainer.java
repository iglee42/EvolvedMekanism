package fr.iglee42.evolvedmekanism.inventory.personalstorage;

import java.util.List;

import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.registries.EMContainerTypes;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.inventory.container.item.MekanismItemContainer;
import mekanism.common.inventory.container.slot.HotBarSlot;
import mekanism.common.lib.inventory.personalstorage.AbstractPersonalStorageItemInventory;
import mekanism.common.lib.inventory.personalstorage.ClientSidePersonalStorageInventory;
import mekanism.common.lib.inventory.personalstorage.PersonalStorageManager;
import mekanism.common.registries.MekanismContainerTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TieredPersonalStorageItemContainer extends MekanismItemContainer {

    private final AbstractTieredPersonalStorageItemInventory itemInventory;

    public TieredPersonalStorageItemContainer(int id, Inventory inv, InteractionHand hand, ItemStack stack, boolean isRemote) {
        super(EMContainerTypes.TIERED_PERSONAL_STORAGE_ITEM, id, inv, hand, stack);
        //We have to initialize this before actually adding the slots
        ItemBlockTieredPersonalStorage<?> item = (ItemBlockTieredPersonalStorage<?>) stack.getItem();
        itemInventory = !isRemote ? TieredPersonalStorageManager.getInventoryFor(stack) : new ClientSideTieredPersonalStorageInventory(item.getTier());
        super.addSlotsAndOpen();
    }

    @Override
    protected void addSlotsAndOpen() {
        //no-op, we call super in constructor, as we need the isRemote
    }

    @Override
    protected void addSlots() {
        super.addSlots();
        //Get all the inventory slots the tile has
        List<IInventorySlot> inventorySlots = itemInventory.getInventorySlots(null);
        for (IInventorySlot inventorySlot : inventorySlots) {
            Slot containerSlot = inventorySlot.createContainerSlot();
            if (containerSlot != null) {
                addSlot(containerSlot);
            }
        }
    }

    public InteractionHand getHand() {
        return hand;
    }

    @Override
    protected int getInventoryYOffset() {
        int moreRows = ((ItemBlockTieredPersonalStorage<?>)getStack().getItem()).getTier().rows - 6;
        return 140 + moreRows * 18;
    }

    @Override
    protected int getInventoryXOffset() {
        return super.getInventoryXOffset() + (((ItemBlockTieredPersonalStorage<?>)getStack().getItem()).getTier().columns - 9) / 2 * 18;
    }

    @Override
    protected HotBarSlot createHotBarSlot(@NotNull Inventory inv, int index, int x, int y) {
        // special handling to prevent removing the personal chest from the player's inventory slot
        if (index == inv.selected && hand == InteractionHand.MAIN_HAND) {
            return new HotBarSlot(inv, index, x, y) {
                @Override
                public boolean mayPickup(@NotNull Player player) {
                    return false;
                }
            };
        }
        return super.createHotBarSlot(inv, index, x, y);
    }

    @Override
    public void clicked(int slotId, int dragType, @NotNull ClickType clickType, @NotNull Player player) {
        if (clickType == ClickType.SWAP) {
            if (hand == InteractionHand.OFF_HAND && dragType == 40) {
                //Block pressing f to swap it when it is in the offhand
                return;
            } else if (hand == InteractionHand.MAIN_HAND && dragType >= 0 && dragType < Inventory.getSelectionSize()) {
                //Block taking out of the selected slot (we don't validate we have a hotbar slot as we always should for this container)
                if (!hotBarSlots.get(dragType).mayPickup(player)) {
                    return;
                }
            }
        }
        super.clicked(slotId, dragType, clickType, player);
    }

    public ItemStack getStack() {
        return stack;
    }

}
