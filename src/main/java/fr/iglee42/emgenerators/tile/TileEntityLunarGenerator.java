package fr.iglee42.emgenerators.tile;

import fr.iglee42.evolvedmekanism.mixins.tiles.TileEntitySolarGeneratorAccessor;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.math.FloatingLong;
import mekanism.api.math.FloatingLongSupplier;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableBoolean;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.WorldUtils;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityLunarGenerator extends TileEntitySolarGenerator {

    private boolean seesMoon;

    @Nullable
    protected LunarCheck lunarCheck;

    public TileEntityLunarGenerator(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        this(blockProvider, pos, state, MekanismGeneratorsConfig.generators.solarGeneration);
    }

    protected TileEntityLunarGenerator(IBlockProvider blockProvider, BlockPos pos, BlockState state, @NotNull FloatingLongSupplier maxOutput) {
        super(blockProvider, pos, state, maxOutput);
    }

    @ComputerMethod(nameOverride = "canSeeMoon")
    @Override
    public boolean canSeeSun() {
        return seesMoon;
    }

    @Override
    protected void onUpdateServer() {
        solarCheck = null;
        super.onUpdateServer();
        if (lunarCheck == null) {
            recheckSettings();
        }
        seesMoon = checkCanSeeMoon();
        if (seesMoon && MekanismUtils.canFunction(this) && getEnergyContainer().getNeeded().greaterThan(FloatingLong.ZERO)) {
            setActive(true);
            FloatingLong production = getProduction();
            ((TileEntitySolarGeneratorAccessor) this).setLastProductionAmount(production.subtract(getEnergyContainer().insert(production, Action.EXECUTE, AutomationType.INTERNAL)));
        } else {
            setActive(false);
            ((TileEntitySolarGeneratorAccessor) this).setLastProductionAmount(FloatingLong.ZERO);
        }
    }

    protected void recheckSettings() {
        if (level == null) {
            return;
        }
        lunarCheck = new LunarCheck(level, worldPosition);
        updateMaxOutputRaw(getConfiguredMax().multiply(lunarCheck.getPeakMultiplier()));
    }

    protected boolean checkCanSeeMoon() {
        if (lunarCheck == null) {
            return false;
        }
        lunarCheck.recheckCanSeeMoon();
        return lunarCheck.canSeeMoon();
    }

    public FloatingLong getProduction() {
        if (level == null || lunarCheck == null) {
            return FloatingLong.ZERO;
        }
        float brightness = getBrightnessMultiplier(level);
        return getConfiguredMax().multiply(brightness * lunarCheck.getGenerationMultiplier());
    }

    protected float getBrightnessMultiplier(@NotNull Level world) {
        return 1.0F - WorldUtils.getSunBrightness(world, 1.0F);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableBoolean.create(this::canSeeSun, value -> seesMoon = value));
    }

    @Override
    protected FloatingLong getConfiguredMax() {
        int modifier = 1 + (upgradeComponent != null ? upgradeComponent.getUpgrades(EMUpgrades.LUNAR_UPGRADE) : 0);
        return super.getConfiguredMax().multiply(modifier);
    }

    protected static class LunarCheck {

        private final boolean needsRainCheck;
        private final float peakMultiplier;
        protected final BlockPos pos;
        protected final Level world;
        protected boolean canSeeMoon;

        public LunarCheck(Level world, BlockPos pos) {
            this.world = world;
            this.pos = pos;
            Biome biome = this.world.getBiomeManager().getBiome(this.pos).value();
            needsRainCheck = biome.getPrecipitationAt(this.pos) != Biome.Precipitation.NONE;
            float tempEff = 0.3F * (0.8F - biome.getBaseTemperature());
            float humidityEff = needsRainCheck ? -0.3F * biome.getModifiedClimateSettings().downfall() : 0;
            peakMultiplier = 1.0F + tempEff + humidityEff;
        }

        public void recheckCanSeeMoon() {
            canSeeMoon = world != null && world.dimensionType().hasSkyLight() && world.canSeeSky(pos.above()) && world.isNight();
        }

        public boolean canSeeMoon() {
            return canSeeMoon;
        }

        public float getPeakMultiplier() {
            return peakMultiplier;
        }

        public float getGenerationMultiplier() {
            if (!canSeeMoon) {
                return 0;
            }
            if (needsRainCheck && (this.world.isRaining() || this.world.isThundering())) {
                return peakMultiplier * 0.2F;
            }
            return peakMultiplier;
        }
    }
}
