package fr.iglee42.evolvedmekanism.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.registries.EMIntProviderTypes;
import mekanism.api.SerializationConstants;
import mekanism.common.config.MekanismConfig;
import mekanism.common.config.value.CachedIntValue;
import mekanism.common.registries.MekanismIntProviderTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviderType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import fr.iglee42.evolvedmekanism.registries.EMOreType.OreVeinType;

public class EMConfigurableConstantInt extends IntProvider {

    public static final MapCodec<EMConfigurableConstantInt> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            OreVeinType.CODEC.optionalFieldOf(SerializationConstants.ORE_TYPE).forGetter(config -> Optional.ofNullable(config.oreVeinType))
    ).apply(builder, oreType -> {
        if (oreType.isPresent()) {
            OreVeinType type = oreType.get();
            return new EMConfigurableConstantInt(type, EMConfig.world.getVeinConfig(type).perChunk());
        }
        return new EMConfigurableConstantInt(null, null);
    }));

    @Nullable
    private final OreVeinType oreVeinType;
    private final CachedIntValue value;

    public EMConfigurableConstantInt(@Nullable OreVeinType oreVeinType, CachedIntValue value) {
        this.oreVeinType = oreVeinType;
        this.value = value;
    }

    public int getValue() {
        //Needs to be getOrDefault so that when IntProvider's range codec validates things in CountPlacement,
        // even though how it gets that value doesn't matter for syncing. Our actual value here doesn't really
        // matter because we limit our config values at the ranges of CountPlacement
        return this.value.getOrDefault();
    }

    @Override
    public int sample(@NotNull RandomSource random) {
        return getValue();
    }

    @Override
    public int getMinValue() {
        return getValue();
    }

    @Override
    public int getMaxValue() {
        return getValue();
    }

    @NotNull
    @Override
    public IntProviderType<?> getType() {
        return EMIntProviderTypes.CONFIGURABLE_CONSTANT.get();
    }

    @Override
    public String toString() {
        return Integer.toString(getValue());
    }
}