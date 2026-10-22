package fr.iglee42.evolvedmekanism.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.registries.EMPlacementModifiers;
import mekanism.common.config.MekanismConfig;
import mekanism.common.resource.ore.OreType.OreVeinType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BooleanSupplier;

public class EMDisableableFeaturePlacement extends PlacementFilter {

    public static final Codec<EMDisableableFeaturePlacement> CODEC = RecordCodecBuilder.create(builder -> builder.group(
          OreVeinType.CODEC.optionalFieldOf("oreVeinType").forGetter(config -> Optional.ofNullable(config.oreVeinType)),
          Codec.BOOL.fieldOf("retroGen").forGetter(config -> config.retroGen)
    ).apply(builder, (oreType, retroGen) -> {
        if (oreType.isPresent()) {
            OreVeinType type = oreType.get();
            return new EMDisableableFeaturePlacement(type, MekanismConfig.world.getVeinConfig(type).shouldGenerate(), retroGen);
        }
        return new EMDisableableFeaturePlacement(null, () -> true, retroGen);
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
            return !retroGen || EMConfig.world.enableRegeneration.get();
        }
        return false;
    }

    @NotNull
    @Override
    public PlacementModifierType<?> type() {
        return EMPlacementModifiers.DISABLEABLE.get();
    }
}
