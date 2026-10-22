package fr.iglee42.emgenerators.tile;

import fr.iglee42.emgenerators.tiers.AdvancedWindGeneratorTier;
import mekanism.api.math.FloatingLong;
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

    public FloatingLong getGeneration() {
        if (!getActive()) {
            return FloatingLong.ZERO;
        }
        return MekanismGeneratorsConfig.generators.windGenerationMin.get()
                .multiply(getCurrentMultiplier())
                .multiply(tier.getMultiplier());
    }

    @Override
    protected void onUpdateServer() {
        updateMaxOutputRaw(MekanismGeneratorsConfig.generators.windGenerationMax.get().multiply(tier.getMultiplier()));
        super.onUpdateServer();
    }
}
