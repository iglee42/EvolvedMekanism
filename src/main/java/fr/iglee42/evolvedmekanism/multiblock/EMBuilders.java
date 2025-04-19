package fr.iglee42.evolvedmekanism.multiblock;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import mekanism.common.command.builders.StructureBuilder;
import mekanism.common.registries.MekanismBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class EMBuilders {

    public static class APTBuilder extends StructureBuilder {

        public APTBuilder() {
            super(9, 5, 9);
        }

        @Override
        protected void build(Level world, BlockPos start, boolean empty) {
            Map<int[], BlockState> states = new HashMap<>();
            states.put(new int[]{0, 0, 0},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{1, 0, 0},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{2, 0, 0},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{3, 0, 0},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{4, 0, 0},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{1, 0, 1},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{1, 0, 2},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{1, 0, 3},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{1, 0, 4},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{2, 0, 1},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{2, 0, 2},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{2, 0, 3},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{3, 0, 1},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{3, 0, 2},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{4, 0, 1},EMBlocks.APT_CASING.getBlock().defaultBlockState());

            for (int i = 1; i < 4; i++){
                states.put(new int[]{0, i, 4}, MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
                states.put(new int[]{1, i, 4},EMBlocks.APT_CASING.getBlock().defaultBlockState());
                states.put(new int[]{2, i, 3},MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
                states.put(new int[]{3, i, 2},MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
                states.put(new int[]{4, i, 1},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            }

            states.put(new int[]{0, 4, 0},MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
            states.put(new int[]{1, 4, 0},MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
            states.put(new int[]{2, 4, 0},MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
            states.put(new int[]{3, 4, 0},MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
            states.put(new int[]{4, 4, 0},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{1, 4, 1},MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
            states.put(new int[]{1, 4, 2},MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
            states.put(new int[]{1, 4, 3},MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
            states.put(new int[]{1, 4, 4},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{2, 4, 1},MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
            states.put(new int[]{2, 4, 2},MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
            states.put(new int[]{2, 4, 3},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{3, 4, 1},MekanismBlocks.STRUCTURAL_GLASS.getBlock().defaultBlockState());
            states.put(new int[]{3, 4, 2},EMBlocks.APT_CASING.getBlock().defaultBlockState());
            states.put(new int[]{4, 4, 1},EMBlocks.APT_CASING.getBlock().defaultBlockState());


            states.forEach((modif,block)->{
                BlockPos added = new BlockPos(modif[0],modif[1],modif[2]);
                BlockPos added90 = added.rotate(Rotation.CLOCKWISE_90);
                BlockPos added180 = added.rotate(Rotation.CLOCKWISE_180);
                BlockPos added270 = added.rotate(Rotation.COUNTERCLOCKWISE_90);

                world.setBlockAndUpdate(start.offset(added),block);
                world.setBlockAndUpdate(start.offset(added90),block);
                world.setBlockAndUpdate(start.offset(added180),block);
                world.setBlockAndUpdate(start.offset(added270),block);
            });
        }

        @Override
        protected Block getCasing() {
            return EMBlocks.APT_CASING.getBlock();
        }
    }
}
