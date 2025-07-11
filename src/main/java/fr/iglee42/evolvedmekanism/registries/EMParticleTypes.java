package fr.iglee42.evolvedmekanism.registries;

import com.mojang.serialization.MapCodec;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.particles.ColoredRisingBubbleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EMParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
        DeferredRegister.create(Registries.PARTICLE_TYPE, EvolvedMekanism.MODID);

    public static final DeferredHolder<ParticleType<?>,ParticleType<ColoredRisingBubbleOptions>> RISING_BUBBLE =
        PARTICLES.register("rising_bubble", ()-> new ParticleType<>(false) {
            @Override
            public MapCodec<ColoredRisingBubbleOptions> codec() {
                return ColoredRisingBubbleOptions.CODEC;
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, ColoredRisingBubbleOptions> streamCodec() {
                return ColoredRisingBubbleOptions.STREAM_CODEC;
            }
        });
}
