package fr.iglee42.emgenerators.tile;

import fr.iglee42.emgenerators.tiers.AdvancedLunarPanelTier;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import mekanism.api.IEvaporationSolar;
import mekanism.api.RelativeSide;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.upgrade.IUpgradeData;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityTieredAdvancedLunarGenerator extends TileEntityLunarGenerator
        implements IBoundingBlock, IEvaporationSolar {

    private final AdvancedLunarPanelTier tier;
    private final LunarCheck[] lunarChecks = new LunarCheck[8];

    public TileEntityTieredAdvancedLunarGenerator(IBlockProvider blockProvider, BlockPos pos, BlockState state, AdvancedLunarPanelTier tier) {
        super(blockProvider, pos, state, () -> MekanismGeneratorsConfig.generators.advancedSolarGeneration.get().multiply(tier.getMultiplier()));
        this.tier = tier;
        addCapabilityResolver(mekanism.common.capabilities.resolver.BasicCapabilityResolver.constant(mekanism.common.capabilities.Capabilities.EVAPORATION_SOLAR, this));
    }

    public AdvancedLunarPanelTier getTier() {
        return tier;
    }

    @Override
    protected RelativeSide[] getEnergySides() {
        return new RelativeSide[]{RelativeSide.FRONT, RelativeSide.BOTTOM};
    }

    @Override
    protected FloatingLong getConfiguredMax() {
        int modifier = 1 + (upgradeComponent != null ? upgradeComponent.getUpgrades(EMUpgrades.LUNAR_UPGRADE) : 0);
        return MekanismGeneratorsConfig.generators.advancedSolarGeneration.get().multiply(tier.getMultiplier()).multiply(modifier);
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
        updateMaxOutputRaw(getConfiguredMax().multiply(totalPeak / 9));
    }

    @Override
    protected boolean checkCanSeeMoon() {
        if (lunarCheck == null) {
            return false;
        }
        lunarCheck.recheckCanSeeMoon();
        byte count = lunarCheck.canSeeMoon() ? (byte) 1 : 0;
        for (LunarCheck check : lunarChecks) {
            check.recheckCanSeeMoon();
            if (check.canSeeMoon()) {
                count++;
            }
        }
        return count > 4;
    }

    @Override
    public FloatingLong getProduction() {
        if (level == null || lunarCheck == null) {
            return FloatingLong.ZERO;
        }
        float brightness = getBrightnessMultiplier(level);
        float generationMultiplier = lunarCheck.getGenerationMultiplier();
        for (LunarCheck check : lunarChecks) {
            generationMultiplier += check.getGenerationMultiplier();
        }
        generationMultiplier /= lunarChecks.length + 1;
        return getConfiguredMax().multiply(brightness * generationMultiplier);
    }

    private static class AdvancedLunarCheck extends LunarCheck {

        private final int recheckFrequency;
        private long lastCheckedMoon;

        public AdvancedLunarCheck(Level world, BlockPos pos) {
            super(world, pos);
            recheckFrequency = Mth.nextInt(world.random, 10, 30);
        }

        @Override
        public void recheckCanSeeMoon() {
            if (!world.dimensionType().hasSkyLight() || !world.isNight()) {
                canSeeMoon = false;
                return;
            }
            long time = world.getGameTime();
            if (time < lastCheckedMoon + recheckFrequency) {
                return;
            }
            lastCheckedMoon = time;
            if (world.getFluidState(pos).isEmpty()) {
                canSeeMoon = world.canSeeSky(pos.above()) && world.isNight();
            } else {
                BlockPos above = pos.above();
                if (world.canSeeSky(above) && world.isNight()) {
                    BlockState state = world.getBlockState(above);
                    canSeeMoon = !state.liquid() && state.getLightBlock(world, above) <= 0;
                } else {
                    canSeeMoon = false;
                }
            }
        }
    }

    @Override
    public @Nullable IUpgradeData getUpgradeData() {
        return new IUpgradeData() {
        };
    }

    @Override
    public void parseUpgradeData(@NotNull IUpgradeData data) {
    }
}
