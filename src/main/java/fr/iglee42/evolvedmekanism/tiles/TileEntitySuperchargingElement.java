package fr.iglee42.evolvedmekanism.tiles;

import java.awt.*;
import java.util.Random;
import java.util.UUID;

import fr.iglee42.evolvedmekanism.multiblock.apt.APTMultiblockData;
import fr.iglee42.evolvedmekanism.particles.ColoredRisingBubleOptions;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMParticleTypes;
import mekanism.api.chemical.ChemicalStack;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.content.boiler.BoilerMultiblockData;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismRecipeSerializers;
import mekanism.common.tile.prefab.TileEntityInternalMultiblock;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntitySuperchargingElement extends TileEntityInternalMultiblock {

    private Color chemicalColor;
    private Color oChemicalColor;

    public TileEntitySuperchargingElement(BlockPos pos, BlockState state) {
        super(EMBlocks.SUPERCHARGING_ELEMENT, pos, state);
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        setActive(getMultiblock() != null && getMultiblock().isFormed() && getMultiblock() instanceof APTMultiblockData data && data.getScaledProgress() > 0);
        if (!(getMultiblock() instanceof APTMultiblockData data)) return;
        ChemicalStack<?> stack = data.inputTank.getStack();
        chemicalColor = new Color(stack.getChemicalTint());
        if (!chemicalColor.equals(oChemicalColor)) {
            oChemicalColor = chemicalColor;
            setChanged();
        }
    }

    @Override
    protected void onUpdateClient() {
        super.onUpdateClient();
        if (getActive()){
            if (chemicalColor == null) return;
            if (level != null){
                double x = getTilePos().getX();
                double y = getTilePos().getY()  + 1;
                double z = getTilePos().getZ();
                if (level.random.nextFloat() > 0.5)level.addAlwaysVisibleParticle(new ColoredRisingBubleOptions(chemicalColor.getRed() / 255f,chemicalColor.getGreen() /255f,chemicalColor.getBlue()/255f), x + (new Random().nextFloat()), y /*+ 3.25d*/, z + (new Random().nextFloat()), (double)0.0F, (double)0.1, (double)0.0F);


            }
        }
    }

    @Override
    public @NotNull CompoundTag getReducedUpdateTag() {
        CompoundTag tag = super.getReducedUpdateTag();
        if (chemicalColor != null)tag.putInt("chemicalColor", chemicalColor.getRGB());
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag) {
        super.handleUpdateTag(tag);
        if (tag.contains("chemicalColor")) chemicalColor = new Color(tag.getInt("chemicalColor"));
    }
}