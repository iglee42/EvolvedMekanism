package fr.iglee42.evolvedmekanism.inventory.personalstorage;

import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;

/**
 * Dummy inventory for client side to sync to
 */
public class ClientSideTieredPersonalStorageInventory extends AbstractTieredPersonalStorageItemInventory {

    public ClientSideTieredPersonalStorageInventory(PersonalStorageTier tier) {
        super(tier);
    }

    @Override
    public void onContentsChanged() {
        //no op
    }
}
