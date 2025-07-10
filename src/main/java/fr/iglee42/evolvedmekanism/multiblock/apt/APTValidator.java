package fr.iglee42.evolvedmekanism.multiblock.apt;

import java.util.EnumSet;
import java.util.Set;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.registries.EMBlockTypes;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElement;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.math.voxel.VoxelCuboid.CuboidSide;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.FormationProtocol.CasingType;
import mekanism.common.lib.multiblock.FormationProtocol.StructureRequirement;
import mekanism.common.lib.multiblock.Structure;
import mekanism.common.lib.multiblock.StructureHelper;
import mekanism.common.tile.multiblock.TileEntitySuperheatingElement;
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
/*    private static final byte[][] ALLOWED_TOP_BOTTOM = {
          {0, 0, 0, 1, 1, 1, 0, 0, 0},
          {0, 0, 1, 2, 2, 2, 1, 0, 0},
          {0, 1, 2, 2, 2, 2, 2, 1, 0},
          {1, 2, 2, 2, 2, 2, 2, 2, 1},
          {1, 2, 2, 2, 2, 2, 2, 2, 1},
          {1, 2, 2, 2, 2, 2, 2, 2, 1},
          {0, 1, 2, 2, 2, 2, 2, 1, 0},
          {0, 0, 1, 2, 2, 2, 1, 0, 0},
          {0, 0, 0, 1, 1, 1, 0, 0, 0},
    };
    private static final byte[][] ALLOWED_LAYERS = {
            {0, 0, 0, 1, 2, 1, 0, 0, 0},
            {0, 0, 2, 3, 3, 3, 2, 0, 0},
            {0, 2, 3, 3, 3, 3, 3, 2, 0},
            {1, 3, 3, 3, 3, 3, 3, 3, 1},
            {2, 3, 3, 3, 3, 3, 3, 3, 2},
            {1, 3, 3, 3, 3, 3, 3, 3, 1},
            {0, 2, 3, 3, 3, 3, 3, 2, 0},
            {0, 0, 2, 3, 3, 3, 2, 0, 0},
            {0, 0, 0, 1, 2, 1, 0, 0, 0},
    };*/

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
        return pos.getY() == 1 && BlockType.is(state.getBlock(),EMBlockTypes.SUPERCHARGING_ELEMENT);
    }

    @Override
    public boolean precheck() {
        // 144 = (24 missing blocks possible on each face) * (6 sides)
        cuboid = StructureHelper.fetchCuboid(structure, BOUNDS, BOUNDS, EnumSet.allOf(CuboidSide.class), 72);
        return cuboid != null;
    }

    @Override
    public FormationProtocol.FormationResult postcheck(APTMultiblockData structure, Long2ObjectMap<ChunkAccess> chunkMap) {
        Set<BlockPos> elements = new ObjectOpenHashSet<>();
        for (BlockPos pos : structure.internalLocations) {
            BlockEntity tile = WorldUtils.getTileEntity(world, chunkMap, pos);
            if (tile instanceof TileEntitySuperchargingElement) {
                elements.add(pos);
            }
        }

        if (!elements.isEmpty())
            structure.superchargingElements = FormationProtocol.explore(elements.iterator().next(), coord ->
                coord.subtract(cuboid.getMinPos()).getY() == 1 && WorldUtils.getTileEntity(TileEntitySuperchargingElement.class, world, chunkMap, coord) != null);

        if (elements.size() > structure.superchargingElements) {
            return FormationProtocol.FormationResult.fail(EvolvedMekanismLang.APT_INVALID_SUPERCHARGING);
        }

        return FormationProtocol.FormationResult.SUCCESS;
    }
}
