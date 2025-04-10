package fr.iglee42.evolvedmekanism.mixins.tiles;

import fr.iglee42.evolvedmekanism.tiles.upgrade.TieredStorageUpgradeData;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.tile.TileEntityPersonalStorage;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.common.util.SecurityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(value = TileEntityPersonalStorage.class,remap = false)
public class TileEntityPersonalStorageMixin extends TileEntityMekanism {

    public TileEntityPersonalStorageMixin(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    public @Nullable IUpgradeData getUpgradeData() {
        if (isInventoryEmpty(null)) return new TieredStorageUpgradeData(null, SecurityUtils.get().getOwnerUUID(this));
        List<IInventorySlot> tileSlots = getInventorySlots(null);
        return new TieredStorageUpgradeData(tileSlots, SecurityUtils.get().getOwnerUUID(this));
    }

    @Override
    public void parseUpgradeData(@NotNull IUpgradeData upgradeData) {
        if (upgradeData instanceof TieredStorageUpgradeData data) {
            if (data.inventory() == null) return;
            if (data.owner() != null){
                setOwnerUUID(data.owner());
            }
            for (int i = 0; i < data.inventory().size(); i++) {
                setStackInSlot(i,data.inventory().get(i).getStack());
            }
        } else {
            super.parseUpgradeData(upgradeData);
        }
    }
}
