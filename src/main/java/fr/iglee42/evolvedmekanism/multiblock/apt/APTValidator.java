package fr.iglee42.evolvedmekanism.multiblock.apt;

import java.util.EnumSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

import fr.iglee42.evolvedmekanism.registries.EMBlockTypes;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.content.sps.SPSMultiblockData;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.math.voxel.VoxelCuboid.CuboidSide;
import mekanism.common.lib.math.voxel.VoxelPlane;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol.CasingType;
import mekanism.common.lib.multiblock.FormationProtocol.StructureRequirement;
import mekanism.common.lib.multiblock.Structure;
import mekanism.common.lib.multiblock.StructureHelper;
import mekanism.common.registries.MekanismBlockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class APTValidator extends CuboidStructureValidator<APTMultiblockData> {

    private static final VoxelCuboid BOUNDS = new VoxelCuboid(9, 5, 9);
    private static final byte[][] ALLOWED_TOP_BOTTOM = {
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
            {0, 0, 1, 3, 3, 3, 2, 0, 0},
            {0, 1, 3, 3, 3, 3, 3, 2, 0},
            {1, 3, 3, 3, 3, 3, 3, 3, 1},
            {2, 3, 3, 3, 3, 3, 3, 3, 2},
            {1, 3, 3, 3, 3, 3, 3, 3, 1},
            {0, 2, 3, 3, 3, 3, 3, 2, 0},
            {0, 0, 2, 3, 3, 3, 2, 0, 0},
            {0, 0, 0, 1, 2, 1, 0, 0, 0},
    };

    @Override
    protected StructureRequirement getStructureRequirement(BlockPos pos) {
        pos = pos.subtract(cuboid.getMinPos());
        if (pos.getY() == 0 || pos.getY() == 4){
            return StructureRequirement.REQUIREMENTS[ALLOWED_TOP_BOTTOM[pos.getX()][pos.getZ()]];
        }
        else {
            return StructureRequirement.REQUIREMENTS[ALLOWED_LAYERS[pos.getX()][pos.getZ()]];
        }
    }

    @Override
    protected CasingType getCasingType(BlockState state) {
        Block block = state.getBlock();
        if (BlockType.is(block, EMBlockTypes.APT_CASING)) {
            return CasingType.FRAME;
        } else if (BlockType.is(block, EMBlockTypes.APT_PORT)) {
            return CasingType.VALVE;
        } else if (BlockType.is(block,MekanismBlockTypes.STRUCTURAL_GLASS)){
            return CasingType.FRAME;
        }
        return CasingType.INVALID;
    }

    @Override
    public boolean precheck() {
        // 144 = (24 missing blocks possible on each face) * (6 sides)
        cuboid = fetchCuboid(structure, BOUNDS, BOUNDS,EnumSet.allOf(CuboidSide.class),168);
        return cuboid != null;
    }

    public static VoxelCuboid fetchCuboid(Structure structure, VoxelCuboid minBounds, VoxelCuboid maxBounds, Set<CuboidSide> sides, int tolerance) {
        // make sure we have enough sides to create cuboidal dimensions
        if (sides.size() < 2) {
            return null;
        }
        int missing = 0;
        VoxelCuboid.CuboidBuilder builder = new VoxelCuboid.CuboidBuilder();
        for (CuboidSide side : sides) {
            Structure.Axis axis = side.getAxis(), horizontal = axis.horizontal(), vertical = axis.vertical();
            NavigableMap<Integer, VoxelPlane> majorAxisMap = structure.getMajorAxisMap(axis);
            Map.Entry<Integer, VoxelPlane> majorEntry = side.getFace().isPositive() ? majorAxisMap.lastEntry() : majorAxisMap.firstEntry();
            // fail fast if the plane doesn't exist
            if (majorEntry == null) {
                return null;
            }
            VoxelPlane plane = majorEntry.getValue();
            // handle missing blocks based on tolerance value
            missing += plane.getMissing();
            if (missing > tolerance) {
                return null;
            }
            int majorKey = majorEntry.getKey();
            // set bounds from dimension of plane's axis
            builder.set(side, majorKey);
            // update cuboidal dimensions from each corner of the plane
            builder.trySet(CuboidSide.get(CuboidSide.Face.NEGATIVE, horizontal), plane.getMinCol());
            builder.trySet(CuboidSide.get(CuboidSide.Face.POSITIVE, horizontal), plane.getMaxCol());
            builder.trySet(CuboidSide.get(CuboidSide.Face.NEGATIVE, vertical), plane.getMinRow());
            builder.trySet(CuboidSide.get(CuboidSide.Face.POSITIVE, vertical), plane.getMaxRow());
            // check to make sure that we don't have any framed minor planes sticking out of our plane
            NavigableMap<Integer, VoxelPlane> minorAxisMap = structure.getMinorAxisMap(axis);
            if (!minorAxisMap.isEmpty()) {
                if (side.getFace().isPositive()) {
                    if (hasOutOfBoundsPositiveMinor(minorAxisMap, majorKey)) {
                        return null;
                    }
                } else if (hasOutOfBoundsNegativeMinor(minorAxisMap, majorKey)) {
                    return null;
                }
            }
        }
        VoxelCuboid ret = builder.build();
        // make sure the cuboid has the correct bounds
        if (!ret.greaterOrEqual(minBounds) || !maxBounds.greaterOrEqual(ret)) {
            return null;
        }
        return ret;
    }
    private static boolean hasOutOfBoundsNegativeMinor(NavigableMap<Integer, VoxelPlane> minorAxisMap, int majorKey) {
        Map.Entry<Integer, VoxelPlane> minorEntry = minorAxisMap.firstEntry();
        while (minorEntry != null) {
            int minorKey = minorEntry.getKey();
            if (minorKey >= majorKey) {
                //If our outer minor plane is in the bounds of our major plane
                // then just exit as the other minor planes will be as well
                break;
            } else if (minorEntry.getValue().hasFrame()) {
                //Otherwise, if it isn't in the bounds, and it has a frame, fail out
                return true;
            }
            //If we don't have a frame and are out of bounds, see if we have any minor entries that
            // are "smaller" that may be invalid
            minorEntry = minorAxisMap.higherEntry(minorKey);
        }
        return false;
    }

    private static boolean hasOutOfBoundsPositiveMinor(NavigableMap<Integer, VoxelPlane> minorAxisMap, int majorKey) {
        Map.Entry<Integer, VoxelPlane> minorEntry = minorAxisMap.lastEntry();
        while (minorEntry != null) {
            int minorKey = minorEntry.getKey();
            if (minorKey <= majorKey) {
                //If our outer minor plane is in the bounds of our major plane
                // then just exit as the other minor planes will be as well
                break;
            } else if (minorEntry.getValue().hasFrame()) {
                //Otherwise, if it isn't in the bounds, and it has a frame, fail out
                return true;
            }
            //If we don't have a frame and are out of bounds, see if we have any minor entries that
            // are "smaller" that may be invalid
            minorEntry = minorAxisMap.lowerEntry(minorKey);
        }
        return false;
    }


}
