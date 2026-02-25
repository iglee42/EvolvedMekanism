package fr.iglee42.evolvedmekanism.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.BooleanSupplier;

import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.registries.EMPlacementModifiers;
import mekanism.api.SerializationConstants;
import mekanism.common.config.MekanismConfig;
import mekanism.common.registries.MekanismPlacementModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import fr.iglee42.evolvedmekanism.registries.EMOreType.OreVeinType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EMDisableableFeaturePlacement extends PlacementFilter {

    public static final MapCodec<EMDisableableFeaturePlacement> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
          OreVeinType.CODEC.optionalFieldOf(SerializationConstants.ORE_TYPE).forGetter(config -> Optional.ofNullable(config.oreVeinType)),
          Codec.BOOL.fieldOf(SerializationConstants.RETRO_GEN).forGetter(config -> config.retroGen)
    ).apply(builder, (oreType, retroGen) -> {
        if (oreType.isPresent()) {
            OreVeinType type = oreType.get();
            return new EMDisableableFeaturePlacement(type, EMConfig.world.getVeinConfig(type).shouldGenerate(), retroGen);
        }
        return new EMDisableableFeaturePlacement(null, null, retroGen);
    }));

    private final BooleanSupplier enabledSupplier;
    @Nullable
    private final OreVeinType oreVeinType;
    private final boolean retroGen;

    public EMDisableableFeaturePlacement(@Nullable OreVeinType oreVeinType, BooleanSupplier enabledSupplier, boolean retroGen) {
        this.oreVeinType = oreVeinType;
        this.enabledSupplier = enabledSupplier;
        this.retroGen = retroGen;
    }

    @Override
    protected boolean shouldPlace(@NotNull PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos pos) {
        if (enabledSupplier.getAsBoolean()) {
            //If we are enabled, and we are either not a retrogen feature or retrogen is enabled, generate
            return !retroGen || MekanismConfig.world.enableRegeneration.get();
        }
        return false;
    }

    @NotNull
    @Override
    public PlacementModifierType<?> type() {
        return EMPlacementModifiers.DISABLEABLE.get();
    }
}