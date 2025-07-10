package fr.iglee42.evolvedmekanism.multiblock;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import mekanism.common.command.builders.StructureBuilder;
import mekanism.common.registries.MekanismBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class EMBuilders {

    public static class APTBuilder extends StructureBuilder {

        public APTBuilder() {
            super(7, 5, 7);
        }

        @Override
        protected void build(Level world, BlockPos start, boolean empty) {
            buildPartialFrame(world, start, 1);
            buildWalls(world, start);
            buildInteriorLayers(world, start, 1, 3, Blocks.AIR.defaultBlockState());
            for (int x = -2; x < 2; ++x) {
                for (int y = -2; y < 2; ++y) {
                    for (int z = -2; z < 2; ++z) {
                        // Check whether one or all three vars ar 0 or -1.
                        // Something that checks whether its exactly one would be better, but that seems very hard.
                        if ((x == -1) == (y == -1) == (z == -1) == (x == 0) == (y == 0) != (z == 0)) {
                            // Check that not all three vars are 0 or -1.
                            if (!(x == -1 || x == 0) || !(y == -1 || y == 0) || !(z == -1 || z == 0)) {
                                world.setBlockAndUpdate(start.offset(x < 0 ? sizeX + x : x, y < 0 ? sizeY + y : y,
                                        z < 0 ? sizeZ + z : z), getCasing());
                            }
                        }
                    }
                }
            }
        }

        @Override
        protected BlockState getCasing() {
            return EMBlocks.APT_CASING.defaultState();
        }
    }
}
