package fr.iglee42.evolvedmekanism.blocks;

import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalBarrel;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.tile.TileEntityPersonalBarrel;
import mekanism.common.tile.TileEntityPersonalChest;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BlockTieredPersonnalBarrel extends BlockTieredPersonalStorage<TileEntityTieredPersonalBarrel,BlockTypeTile<TileEntityTieredPersonalBarrel>>{
    public BlockTieredPersonnalBarrel(BlockTypeTile<TileEntityTieredPersonalBarrel> type, PersonalStorageTier tier) {
        super(type, tier);
    }

    @Override
    @Deprecated
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.tick(state, level, pos, random);
        TileEntityPersonalBarrel barrel = WorldUtils.getTileEntity(TileEntityPersonalBarrel.class, level, pos);
        if (barrel != null) {
            barrel.recheckOpen();
        }
    }
}
