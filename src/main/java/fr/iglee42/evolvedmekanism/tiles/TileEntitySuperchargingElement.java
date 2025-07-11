package fr.iglee42.evolvedmekanism.tiles;

import java.awt.*;
import java.util.Random;

import fr.iglee42.evolvedmekanism.multiblock.apt.APTMultiblockData;
import fr.iglee42.evolvedmekanism.particles.ColoredRisingBubleOptions;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.api.chemical.ChemicalStack;
import mekanism.common.tile.prefab.TileEntityInternalMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
    protected boolean onUpdateServer() {
        boolean send = super.onUpdateServer();
        setActive(getMultiblock() != null && getMultiblock().isFormed() && getMultiblock() instanceof APTMultiblockData data && data.getScaledProgress() > 0);
        if (!(getMultiblock() instanceof APTMultiblockData data)) return;
        ChemicalStack stack = data.inputTank.getStack();
        chemicalColor = new Color(stack.getChemicalTint());
        if (!chemicalColor.equals(oChemicalColor)) {
            oChemicalColor = chemicalColor;
            send = true;
        }
        return send;
    }

    @Override
    protected void onUpdateClient() {
        super.onUpdateClient();
        if (getActive()){
            if (chemicalColor == null) return;
            if (level != null){
                double x = getBlockPos().getX();
                double y = getBlockPos().getY()  + 1;
                double z = getBlockPos().getZ();
                if (level.random.nextFloat() > 0.5)level.addAlwaysVisibleParticle(new ColoredRisingBubleOptions(chemicalColor.getRed() / 255f,chemicalColor.getGreen() /255f,chemicalColor.getBlue()/255f), x + (new Random().nextFloat()), y /*+ 3.25d*/, z + (new Random().nextFloat()), (double)0.0F, (double)0.1, (double)0.0F);


            }
        }
    }

    @Override
    public @NotNull CompoundTag getReducedUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getReducedUpdateTag(provider);
        if (chemicalColor != null)tag.putInt("chemicalColor", chemicalColor.getRGB());
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag,HolderLookup.Provider provider) {
        super.handleUpdateTag(tag,provider);
        if (tag.contains("chemicalColor")) chemicalColor = new Color(tag.getInt("chemicalColor"));
    }
}