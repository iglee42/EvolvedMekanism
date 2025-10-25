package fr.iglee42.evolvedmekanism.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.List;
import java.util.Set;

import fr.iglee42.evolvedmekanism.inventory.personalstorage.AbstractTieredPersonalStorageItemInventory;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.ClientSideTieredPersonalStorageInventory;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageItemInventory;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageManager;
import fr.iglee42.evolvedmekanism.registries.EMLootFunctions;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalStorage;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.lib.inventory.personalstorage.ClientSidePersonalStorageInventory;
import mekanism.common.lib.inventory.personalstorage.PersonalStorageManager;
import mekanism.common.tile.TileEntityPersonalStorage;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.fml.util.thread.EffectiveSide;

/**
 * Loot function which copies the Personal Storage inventory to the saved data and adds an inv id to the stack
 */
@MethodsReturnNonnullByDefault
@ParametersAreNotNullByDefault
public class PersonalTieredStorageContentsLootFunction implements LootItemFunction {

    private static final PersonalTieredStorageContentsLootFunction INSTANCE = new PersonalTieredStorageContentsLootFunction();
    private static final Set<LootContextParam<?>> REFERENCED_PARAMS = Set.of(LootContextParams.BLOCK_ENTITY);

    private PersonalTieredStorageContentsLootFunction() {
    }

    public static LootItemFunction.Builder builder() {
        return ()->INSTANCE;
    }

    @Override
    public LootItemFunctionType getType() {
        return EMLootFunctions.TIERED_PERSONAL_STORAGE_LOOT_FUNC.get();
    }

    @Override
    public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
        BlockEntity blockEntity = lootContext.getParam(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof TileEntityTieredPersonalStorage personalStorage && !personalStorage.isInventoryEmpty()) {
            List<IInventorySlot> tileSlots = personalStorage.getInventorySlots(null);
            AbstractTieredPersonalStorageItemInventory destInv;
            if (EffectiveSide.get().isClient()) {
                destInv = new ClientSideTieredPersonalStorageInventory(personalStorage.getTier());
            } else {
                destInv = TieredPersonalStorageManager.getInventoryFor(itemStack).orElseThrow(()->new IllegalStateException("Inventory not available?!"));
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

    public static class PersonalStorageLootFunctionSerializer implements Serializer<PersonalTieredStorageContentsLootFunction> {

        @Override
        public void serialize(JsonObject pJson, PersonalTieredStorageContentsLootFunction pValue, JsonSerializationContext pSerializationContext) {
            //no-op
        }

        @Override
        public PersonalTieredStorageContentsLootFunction deserialize(JsonObject pJson, JsonDeserializationContext pSerializationContext) {
            return INSTANCE;
        }
    }
}