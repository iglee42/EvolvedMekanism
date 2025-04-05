package fr.iglee42.evolvedmekanism.blocks;

import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageManager;
import fr.iglee42.evolvedmekanism.loot.PersonalTieredStorageContentsLootFunction;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalStorage;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.block.BlockPersonalStorage;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.loot.PersonalStorageContentsLootFunction;
import mekanism.common.lib.inventory.personalstorage.PersonalStorageManager;
import mekanism.common.tile.TileEntityPersonalChest;
import mekanism.common.tile.TileEntityPersonalStorage;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockTieredPersonalStorage<TILE extends TileEntityTieredPersonalStorage, BLOCK extends BlockTypeTile<TILE>> extends BlockPersonalStorage<TILE,BLOCK> {

    public static final Attribute TIERED_PERSONAL_STORAGE_INVENTORY = new Attributes.AttributeInventory<>(((lootBuilder, nbtBuilder) -> {
        lootBuilder.apply(PersonalTieredStorageContentsLootFunction.builder());
        return true;
    }));

    private final PersonalStorageTier tier;

    public BlockTieredPersonalStorage(BLOCK type, PersonalStorageTier tier) {
        super(type, properties -> properties.mapColor(MapColor.COLOR_GRAY));
        this.tier = tier;
    }

    @Override
    public void setPlacedBy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if (!world.isClientSide) {
            TieredPersonalStorageManager.getInventoryIfPresent(stack).ifPresent(storageItemInventory -> {
                TileEntityTieredPersonalStorage tile = WorldUtils.getTileEntity(TileEntityTieredPersonalStorage.class, world, pos);
                if (tile == null) {
                    return;
                }
                List<IInventorySlot> inventorySlots = storageItemInventory.getInventorySlots(null);
                for (int i = 0; i < inventorySlots.size(); i++) {
                    IInventorySlot itemSlot = inventorySlots.get(i);
                    tile.setStackInSlot(i, itemSlot.getStack().copy());
                }
                if (stack.getCount() == 1 && (!(placer instanceof Player player) || !player.getAbilities().instabuild)) {
                    //itemstack will be deleted, remove the stored inventory
                    TieredPersonalStorageManager.deleteInventory(stack);
                }
            });
        }
    }

    public PersonalStorageTier getTier() {
        return tier;
    }


}
