package fr.iglee42.evolvedmekanism.tiles;

import java.util.Random;
import java.util.UUID;

import fr.iglee42.evolvedmekanism.multiblock.apt.APTMultiblockData;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.common.content.boiler.BoilerMultiblockData;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.prefab.TileEntityInternalMultiblock;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySuperchargingElement extends TileEntityInternalMultiblock {

    public TileEntitySuperchargingElement(BlockPos pos, BlockState state) {
        super(EMBlocks.SUPERCHARGING_ELEMENT, pos, state);
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        setActive(getMultiblock() != null && getMultiblock().isFormed() && getMultiblock() instanceof APTMultiblockData data && data.getScaledProgress() > 0);
    }

    @Override
    protected void onUpdateClient() {
        super.onUpdateClient();
        if (getActive()){
            if (level != null){
                double x = getTilePos().getX();
                double y = getTilePos().getY()  + 1;
                double z = getTilePos().getZ();

                level.addAlwaysVisibleParticle(ParticleTypes.NAUTILUS, x + (new Random().nextFloat()), y + 3.25d, z + (new Random().nextFloat()), (double)0.0F, (double)-3.25F, (double)0.0F);


            }
        }
    }
}