package fr.iglee42.evolvedmekanism.blocks;

import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalChest;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BlockTieredPersonnalChest extends BlockTieredPersonalStorage<TileEntityTieredPersonalChest, BlockTypeTile<TileEntityTieredPersonalChest>>{
    public BlockTieredPersonnalChest(BlockTypeTile<TileEntityTieredPersonalChest> type, PersonalStorageTier tier) {
        super(type, tier);
    }

    @Override
    @Deprecated
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.tick(state, level, pos, random);
        TileEntityTieredPersonalChest chest = WorldUtils.getTileEntity(TileEntityTieredPersonalChest.class, level, pos);
        if (chest != null) {
            chest.recheckOpen();
        }
    }
}
