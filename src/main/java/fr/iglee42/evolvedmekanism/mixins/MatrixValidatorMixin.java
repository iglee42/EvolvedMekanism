package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.registries.EMBlockTypes;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.content.matrix.MatrixMultiblockData;
import mekanism.common.content.matrix.MatrixValidator;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.registries.MekanismBlockTypes;
import mekanism.common.tile.multiblock.TileEntityInductionCell;
import mekanism.common.tile.multiblock.TileEntityInductionProvider;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = MatrixValidator.class,remap = false)
public class MatrixValidatorMixin  extends CuboidStructureValidator<MatrixMultiblockData> {

    @Shadow @Final private List<TileEntityInductionCell> cells;

    @Shadow @Final private List<TileEntityInductionProvider> providers;

    @Inject(method = "validateInner",at = @At(value = "INVOKE", target = "Lmekanism/common/content/blocktype/BlockType;is(Lnet/minecraft/world/level/block/Block;[Lmekanism/common/content/blocktype/BlockType;)Z"),cancellable = true)
    private void evolvedmekanism$acceptCustomBlocks(BlockState state, Long2ObjectMap<ChunkAccess> chunkMap, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        if (BlockType.is(state.getBlock(), EMBlockTypes.OVERCLOCKED_INDUCTION_CELL, EMBlockTypes.QUANTUM_INDUCTION_CELL,
                EMBlockTypes.DENSE_INDUCTION_CELL, EMBlockTypes.MULTIVERSAL_INDUCTION_CELL, EMBlockTypes.OVERCLOCKED_INDUCTION_PROVIDER,
                EMBlockTypes.QUANTUM_INDUCTION_PROVIDER, EMBlockTypes.DENSE_INDUCTION_PROVIDER, EMBlockTypes.MULTIVERSAL_INDUCTION_PROVIDER,EMBlockTypes.CREATIVE_INDUCTION_PROVIDER,EMBlockTypes.CREATIVE_INDUCTION_CELL)) {
            //Compare blocks against the type before bothering to look up the tile
            BlockEntity tile = WorldUtils.getTileEntity(world, chunkMap, pos);
            if (tile instanceof TileEntityInductionCell cell) {
                cells.add(cell);
                cir.setReturnValue(true);
            } else if (tile instanceof TileEntityInductionProvider provider) {
                providers.add(provider);
                cir.setReturnValue(true);
            }
            //Else something went wrong
        }
    }

    @Unique
    protected FormationProtocol.CasingType getCasingType(BlockState state) {
        Block block = state.getBlock();
        if (BlockType.is(block, MekanismBlockTypes.INDUCTION_CASING)) {
            return FormationProtocol.CasingType.FRAME;
        } else if (BlockType.is(block, MekanismBlockTypes.INDUCTION_PORT)) {
            return FormationProtocol.CasingType.VALVE;
        }
        return FormationProtocol.CasingType.INVALID;
    }
}
