package fr.iglee42.evolvedmekanism.multiblock.apt;

import java.util.EnumSet;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.registries.EMBlockTypes;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElement;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElementMk2;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.math.voxel.VoxelCuboid.CuboidSide;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.FormationProtocol.CasingType;
import mekanism.common.lib.multiblock.FormationProtocol.StructureRequirement;
import mekanism.common.lib.multiblock.Structure;
import mekanism.common.lib.multiblock.StructureHelper;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

public class APTValidator extends CuboidStructureValidator<APTMultiblockData> {

    private static final VoxelCuboid BOUNDS = new VoxelCuboid(7, 5, 7);
    private static final byte[][] ALLOWED_GRID = {
            {0, 0, 1, 1, 1, 0, 0},
            {0, 1, 2, 2, 2, 1, 0},
            {1, 2, 2, 2, 2, 2, 1},
            {1, 2, 2, 2, 2, 2, 1},
            {1, 2, 2, 2, 2, 2, 1},
            {0, 1, 2, 2, 2, 1, 0},
            {0, 0, 1, 1, 1, 0, 0}
    };

    @Override
    protected StructureRequirement getStructureRequirement(BlockPos pos) {
        VoxelCuboid.WallRelative relative = cuboid.getWallRelative(pos);
        if (relative.isWall()) {
            Structure.Axis axis = Structure.Axis.get(cuboid.getSide(pos));
            Structure.Axis h = axis.horizontal(), v = axis.vertical();
            //Note: This ends up becoming immutable by doing this but that is fine and doesn't really matter
            Direction side = cuboid.getSide(pos);
            pos = pos.subtract(cuboid.getMinPos());
            return side.getAxis().isHorizontal() && pos.getY() >= 3?
                    StructureRequirement.REQUIREMENTS[ALLOWED_GRID[h.getCoord(pos)][v.getCoord(pos) + 2]] :
                            StructureRequirement.REQUIREMENTS[ALLOWED_GRID[h.getCoord(pos)][v.getCoord(pos)]];
        }
        return super.getStructureRequirement(pos);
    }

    @Override
    protected CasingType getCasingType(BlockState state) {
        Block block = state.getBlock();
        if (BlockType.is(block, EMBlockTypes.APT_CASING)) {
            return CasingType.FRAME;
        } else if (BlockType.is(block, EMBlockTypes.APT_PORT)) {
            return CasingType.VALVE;
        }
        return CasingType.INVALID;
    }

    @Override
    protected boolean validateInner(BlockState state, Long2ObjectMap<ChunkAccess> chunkMap, BlockPos pos) {
        if (super.validateInner(state, chunkMap, pos)) return true;
        pos = pos.subtract(cuboid.getMinPos());
        return pos.getY() == 1 && (BlockType.is(state.getBlock(),EMBlockTypes.SUPERCHARGING_ELEMENT) || BlockType.is(state.getBlock(),EMBlockTypes.SUPERCHARGING_ELEMENT_MK2));
    }

    @Override
    public boolean precheck() {
        // 144 = (24 missing blocks possible on each face) * (6 sides)
        cuboid = StructureHelper.fetchCuboid(structure, BOUNDS, BOUNDS, EnumSet.allOf(CuboidSide.class), 72);
        return cuboid != null;
    }

    @Override
    public FormationProtocol.FormationResult postcheck(APTMultiblockData structure, Long2ObjectMap<ChunkAccess> chunkMap) {
        float total = 0f;
        for (BlockPos pos : structure.internalLocations) {
            BlockEntity tile = WorldUtils.getTileEntity(world, chunkMap, pos);
            if (tile instanceof TileEntitySuperchargingElement) {
                if (pos.subtract(cuboid.getMinPos()).getY() != 1) return FormationProtocol.FormationResult.fail(EvolvedMekanismLang.APT_INVALID_SUPERCHARGING);
                // MK1 contribution comes from config (percent -> fraction)
                total += EMConfig.general.aptMk1Percent.get() / 100f;
            } else if (tile instanceof TileEntitySuperchargingElementMk2) {
                if (pos.subtract(cuboid.getMinPos()).getY() != 1) return FormationProtocol.FormationResult.fail(EvolvedMekanismLang.APT_INVALID_SUPERCHARGING);
                // MK2 contribution comes from config (percent -> fraction)
                total += EMConfig.general.aptMk2Percent.get() / 100f;
            }
        }
        structure.superchargingElements = total;

        return FormationProtocol.FormationResult.SUCCESS;
    }
}
