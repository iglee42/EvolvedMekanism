package fr.iglee42.evolvedmekanism.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.function.IntSupplier;

import fr.iglee42.evolvedmekanism.config.EMConfig;
import mekanism.api.SerializationConstants;
import mekanism.api.functions.FloatSupplier;
import mekanism.common.config.MekanismConfig;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration.TargetBlockState;
import fr.iglee42.evolvedmekanism.registries.EMOreType.OreVeinType;
import fr.iglee42.evolvedmekanism.config.EMWorldConfig.OreVeinConfig;

public record EMResizableOreFeatureConfig(List<TargetBlockState> targetStates, OreVeinType oreVeinType, IntSupplier size,
                                          FloatSupplier discardChanceOnAirExposure) implements FeatureConfiguration {

    public static final Codec<EMResizableOreFeatureConfig> CODEC = RecordCodecBuilder.create(builder -> builder.group(
          Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf(SerializationConstants.TARGETS).forGetter(config -> config.targetStates),
          OreVeinType.CODEC.fieldOf(SerializationConstants.ORE_TYPE).forGetter(config -> config.oreVeinType)
    ).apply(builder, (targetStates, oreVeinType) -> {
        OreVeinConfig veinConfig = EMConfig.world.getVeinConfig(oreVeinType);
        return new EMResizableOreFeatureConfig(targetStates, oreVeinType, veinConfig.maxVeinSize(), veinConfig.discardChanceOnAirExposure());
    }));
}