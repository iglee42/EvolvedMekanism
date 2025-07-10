package fr.iglee42.evolvedmekanism.loot;

import fr.iglee42.evolvedmekanism.inventory.personalstorage.AbstractTieredPersonalStorageItemInventory;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.ClientSideTieredPersonalStorageInventory;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageItemInventory;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageManager;
import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.registries.EMLootFunctions;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.lib.inventory.personalstorage.ClientSidePersonalStorageInventory;
import mekanism.common.lib.inventory.personalstorage.PersonalStorageManager;
import mekanism.common.tile.TileEntityPersonalStorage;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.fml.util.thread.EffectiveSide;

import java.util.List;
import java.util.Set;

/**
 * Loot function which copies the Personal Storage inventory to the saved data and adds an inv id to the stack
 */
@MethodsReturnNonnullByDefault
@ParametersAreNotNullByDefault
public class PersonalTieredStorageContentsLootFunction implements LootItemFunction {

    public static final PersonalTieredStorageContentsLootFunction INSTANCE = new PersonalTieredStorageContentsLootFunction();
    private static final Set<LootContextParam<?>> REFERENCED_PARAMS = Set.of(LootContextParams.BLOCK_ENTITY);

    private PersonalTieredStorageContentsLootFunction() {
    }

    public static LootItemFunction.Builder builder() {
        return ()->INSTANCE;
    }

    @Override
    public LootItemFunctionType<?> getType() {
        return EMLootFunctions.TIERED_PERSONAL_STORAGE_LOOT_FUNC.get();
    }

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
        BlockEntity blockEntity = lootContext.getParam(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof TileEntityPersonalStorage personalStorage && !personalStorage.isInventoryEmpty()) {
            List<IInventorySlot> tileSlots = personalStorage.getInventorySlots(null);
            if (!(itemStack.getItem() instanceof ItemBlockTieredPersonalStorage<?> item)) throw new IllegalStateException("Item isn't ItemBlockTieredPersonalStorage");

            AbstractTieredPersonalStorageItemInventory destInv;
            if (EffectiveSide.get().isClient()) {
                destInv = new ClientSideTieredPersonalStorageInventory(item.getTier());
            } else {
                destInv = TieredPersonalStorageManager.getInventoryFor(itemStack).orElseThrow(() -> new IllegalStateException("Inventory not available?!"));
            }
            for (int i = 0; i < tileSlots.size(); i++) {
                IInventorySlot tileSlot = tileSlots.get(i);
                if (!tileSlot.isEmpty()) {
                    destInv.setStackInSlot(i, tileSlot.getStack().copy());
                }
            }
        }
        return itemStack;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return REFERENCED_PARAMS;
    }

}