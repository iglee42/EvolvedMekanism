package fr.iglee42.emgenerators.tile;

import fr.iglee42.emgenerators.tiers.AdvancedWindGeneratorTier;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.tile.TileEntityWindGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityTieredWindGenerator extends TileEntityWindGenerator {

    private final AdvancedWindGeneratorTier tier;

    public TileEntityTieredWindGenerator(BlockPos pos, BlockState state, AdvancedWindGeneratorTier tier) {
        super(pos, state);
        this.tier = tier;
    }

    public AdvancedWindGeneratorTier getTier() {
        return tier;
    }

    @Override
    public long getCurrentGeneration() {
        return super.getCurrentGeneration() * tier.getMultiplier();
    }

    @Override
    protected boolean onUpdateServer() {
        updateMaxOutputRaw(MekanismGeneratorsConfig.generators.windGenerationMax.get() * tier.getMultiplier());
        return super.onUpdateServer();
    }
}
