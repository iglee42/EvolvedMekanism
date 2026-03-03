package fr.iglee42.emgenerators.tile;

import fr.iglee42.emgenerators.tiers.AdvancedLunarPanelTier;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import mekanism.api.IEvaporationSolar;
import mekanism.api.RelativeSide;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityTieredAdvancedLunarGenerator extends TileEntityLunarGenerator
        implements IBoundingBlock, IEvaporationSolar {

    private final AdvancedLunarPanelTier tier;
    private final LunarCheck[] lunarChecks = new LunarCheck[8];

    public TileEntityTieredAdvancedLunarGenerator(Holder<Block> blockProvider, BlockPos pos, BlockState state, AdvancedLunarPanelTier tier) {
        super(blockProvider, pos, state,
                ()->MekanismGeneratorsConfig.generators.advancedSolarGeneration.get() * tier.getMultiplier());
        this.tier = tier;
    }

    
    public AdvancedLunarPanelTier getTier() {
        return tier;
    }


    @Override
    protected RelativeSide[] getEnergySides() {
        return new RelativeSide[] { RelativeSide.FRONT, RelativeSide.BOTTOM };
    }

    @Override
    protected long getConfiguredMax() {
        int modifier = 1 + (upgradeComponent != null ? upgradeComponent.getUpgrades(EMUpgrades.LUNAR_UPGRADE) : 0);
        return MekanismGeneratorsConfig.generators.advancedSolarGeneration.get() * tier.getMultiplier() * modifier;
    }

    @Override
    protected void recheckSettings() {
        if (level == null) {
            return;
        }
        BlockPos topPos = worldPosition.above(2);
        lunarCheck = new AdvancedLunarCheck(level, topPos);
        float totalPeak = lunarCheck.getPeakMultiplier();
        for (int i = 0; i < lunarChecks.length; i++) {
            if (i < 3) {
                lunarChecks[i] = new AdvancedLunarCheck(level, topPos.offset(-1, 0, i - 1));
            } else if (i == 3) {
                lunarChecks[i] = new AdvancedLunarCheck(level, topPos.offset(0, 0, -1));
            } else if (i == 4) {
                lunarChecks[i] = new AdvancedLunarCheck(level, topPos.offset(0, 0, 1));
            } else {
                lunarChecks[i] = new AdvancedLunarCheck(level, topPos.offset(1, 0, i - 6));
            }
            totalPeak += lunarChecks[i].getPeakMultiplier();
        }
        updateMaxOutputRaw((long) (getConfiguredMax() *(totalPeak / 9) * (1 + (upgradeComponent != null ? upgradeComponent.getUpgrades(EMUpgrades.LUNAR_UPGRADE) : 0))));
    }


    @Override
    protected boolean checkCanSeeMoon() {
        if (lunarCheck == null) {
            // Note: We assume if lunarCheck is null then solarChecks will be filled with
            // null, and if it isn't
            // then it won't be as they get initialized at the same time
            return false;
        }
        // Allow attempting to recheck each position, and mark that we can see the sun
        // if at least one position can
        lunarCheck.recheckCanSeeMoon();
        byte count = lunarCheck.canSeeMoon() ? (byte) 1 : 0;
        for (LunarCheck check : lunarChecks) {
            check.recheckCanSeeMoon();
            if (check.canSeeMoon()) {
                count++;
            }
        }
        // Mark that our solar generator can "see" the sun if at least five of the nine
        // positions
        // are able to see the sun
        return count > 4;
    }

    @Override
    public long getProduction() {
        if (level == null || lunarCheck == null) {
            // Note: We assume if lunarCheck is null then solarChecks will be filled with
            // null, and if it isn't
            // then it won't be as they get initialized at the same time
            return 0;
        }
        float brightness = getBrightnessMultiplier(level);
        // Calculate the generation multiplier of all the solar panels together
        // any part that can't see the sun will contribute zero to the multiplier,
        // and then we take the average across all to see how much to multiply by
        float generationMultiplier = lunarCheck.getGenerationMultiplier();
        for (LunarCheck check : lunarChecks) {
            generationMultiplier += check.getGenerationMultiplier();
        }
        generationMultiplier /= lunarChecks.length + 1;
        // Production is a function of the peak possible output in this biome and sun's
        // current brightness
        return (long) (getConfiguredMax() * (brightness * generationMultiplier));
    }

    private static class AdvancedLunarCheck extends LunarCheck {

        private final int recheckFrequency;
        private long lastCheckedMoon;

        public AdvancedLunarCheck(Level world, BlockPos pos) {
            super(world, pos);
            // Recheck between every 10-30 ticks, to not end up checking each position each
            // tick
            recheckFrequency = Mth.nextInt(world.random, 10, 30);
        }

        @Override
        public void recheckCanSeeMoon() {
            if (!world.dimensionType().hasSkyLight() || !world.isNight()) {
                // Inline of most of WorldUtils#canSeeSun so that we can exit early if it is not
                // day or there is no skylight
                // We start with the basic dimension checks and always run those, as they are
                // simple and quick checks, and
                // we want to be able to stop quickly when it gets too dark
                canSeeMoon = false;
                return;
            }
            long time = world.getGameTime();
            if (time < lastCheckedMoon + recheckFrequency) {
                // If we have checked for blocks above the solar panel in the past
                // recheckFrequency
                // number of ticks, skip checking for now for performance reasons
                return;
            }
            // otherwise, mark that we checked and actually check
            lastCheckedMoon = time;
            if (world.getFluidState(pos).isEmpty()) {
                // If the top isn't fluid logged we can just quickly check if the top can see
                // the sun
                canSeeMoon = world.dimensionType().hasSkyLight() && world.canSeeSky(pos.above()) && world.isNight();
            } else {
                BlockPos above = pos.above();
                if ( world.dimensionType().hasSkyLight() && world.canSeeSky(above) && world.isNight()) {
                    // If the spot above can see the sun, check to make sure we can see through the
                    // block there
                    BlockState state = world.getBlockState(above);
                    canSeeMoon = !state.liquid() && state.getLightBlock(world, above) <= 0;
                } else {
                    canSeeMoon = false;
                }
            }
        }
    }

    @Override
    public @Nullable IUpgradeData getUpgradeData(HolderLookup.Provider provider) {
        return new IUpgradeData() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        };
    }

    @Override
    public void parseUpgradeData(HolderLookup.Provider provider, @NotNull IUpgradeData data) {
    }
}