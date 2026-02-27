package fr.iglee42.emgenerators.tile;

import fr.iglee42.emgenerators.registries.EMGenBlocks;
import fr.iglee42.evolvedmekanism.mixins.tiles.TileEntitySolarGeneratorAccessor;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.math.MathUtils;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableBoolean;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.util.WorldUtils;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.LongSupplier;

public class TileEntityLunarGenerator extends TileEntitySolarGenerator {

    private boolean seesMoon;

    @Nullable
    protected LunarCheck lunarCheck;


    public TileEntityLunarGenerator(BlockPos pos, BlockState state) {
        this(EMGenBlocks.LUNAR_GENERATOR, pos, state, MekanismGeneratorsConfig.generators.solarGeneration);
    }

    protected TileEntityLunarGenerator(Holder<Block> blockProvider, BlockPos pos, BlockState state, @NotNull LongSupplier maxOutput) {
        super(blockProvider, pos, state, maxOutput);
    }

    @ComputerMethod(nameOverride = "canSeeMoon")
    @Override
    public boolean canSeeSun() {
        return seesMoon;
    }

    @Override
    protected boolean onUpdateServer() {
        solarCheck = null;
        boolean sendUpdatePacket = super.onUpdateServer();
        if (lunarCheck == null) {
            recheckSettings();
        }
        seesMoon = checkCanSeeMoon();
        if (seesMoon && canFunction() && getEnergyContainer().getNeeded() > 0L) {
            setActive(true);
            long production = getProduction();
            ((TileEntitySolarGeneratorAccessor)this).setLastProductionAmount(production - getEnergyContainer().insert(production, Action.EXECUTE, AutomationType.INTERNAL));
        } else {
            setActive(false);
            ((TileEntitySolarGeneratorAccessor)this).setLastProductionAmount(0L);
        }
        return sendUpdatePacket;
    }

    protected void recheckSettings() {
        if (level == null) {
            return;
        }
        lunarCheck = new LunarCheck(level, worldPosition);
        updateMaxOutputRaw(MathUtils.clampToLong(getConfiguredMax() * lunarCheck.getPeakMultiplier()));
    }

    protected boolean checkCanSeeMoon() {
        if (lunarCheck == null) {
            return false;
        }
        lunarCheck.recheckCanSeeMoon();
        return lunarCheck.canSeeMoon();
    }

    public long getProduction() {
        if (level == null || lunarCheck == null) {
            return 0L;
        }
        float brightness = getBrightnessMultiplier(level);
        return MathUtils.clampToLong(getConfiguredMax() * (brightness * lunarCheck.getGenerationMultiplier()));
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
    protected long getConfiguredMax() {
        int modifier = 1 + (upgradeComponent != null ? upgradeComponent.getUpgrades(EMUpgrades.LUNAR_UPGRADE) : 0);
        return super.getConfiguredMax() * modifier;
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
            Biome b = this.world.getBiomeManager().getBiome(this.pos).value();
            needsRainCheck = b.getPrecipitationAt(this.pos) != Biome.Precipitation.NONE;
            // Consider the best temperature to be 0.8; biomes that are higher than that
            // will suffer an efficiency loss (semiconductors don't like heat); biomes that are cooler
            // get a boost. We scale the efficiency to around 30% so that it doesn't totally dominate
            float tempEff = 0.3F * (0.8F - b.getTemperature(this.pos));

            // Treat rainfall as a proxy for humidity; any humidity works as a drag on overall efficiency.
            // As with temperature, we scale it so that it doesn't overwhelm production. Note the signedness
            // on the scaling factor. Also note that we only use rainfall as a proxy if it CAN rain; some dimensions
            // (like the End) have rainfall set, but can't actually support rain.
            float humidityEff = needsRainCheck ? -0.3F * b.getModifiedClimateSettings().downfall() : 0;
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
                //If the generator is in a biome where it can rain, and it's raining penalize production by 80%
                return peakMultiplier * 0.2F;
            }
            return peakMultiplier;
        }
    }
}
