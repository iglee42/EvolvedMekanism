package fr.iglee42.evolvedmekanism.blocks;

import fr.iglee42.evolvedmekanism.items.ItemBlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalChest;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.registration.impl.BlockRegistryObject;
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

    public static BlockRegistryObject<BlockTieredPersonnalChest, ItemBlockTieredPersonalStorage<BlockTieredPersonnalChest>> getUpgrade(PersonalStorageTier tier){
        return switch (tier){
            case BASIC -> EMBlocks.ADVANCED_PERSONAL_CHEST;
            case ADVANCED -> EMBlocks.ELITE_PERSONAL_CHEST;
            case ELITE -> EMBlocks.ULTIMATE_PERSONAL_CHEST;
            case ULTIMATE -> EMBlocks.OVERCLOCKED_PERSONAL_CHEST;
            case OVERCLOCKED -> EMBlocks.QUANTUM_PERSONAL_CHEST;
            case QUANTUM -> EMBlocks.DENSE_PERSONAL_CHEST;
            case DENSE -> EMBlocks.MULTIVERSAL_PERSONAL_CHEST;
            case MULTIVERSAL -> null;
        };
    }
}
