package fr.iglee42.evolvedmekanism.registries;

import com.mojang.serialization.Codec;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.particles.ColoredMoltenOptions;
import fr.iglee42.evolvedmekanism.particles.ColoredRisingBubbleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EMParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
        DeferredRegister.create(Registries.PARTICLE_TYPE, EvolvedMekanism.MODID);

    public static final RegistryObject<ParticleType<ColoredRisingBubbleOptions>> RISING_BUBBLE =
        PARTICLES.register("rising_bubble", ()-> new ParticleType<>(false, ColoredRisingBubbleOptions.DESERIALIZER) {
            @Override
            public Codec<ColoredRisingBubbleOptions> codec() {
                return ColoredRisingBubbleOptions.CODEC;
            }
        });

    public static final RegistryObject<ParticleType<ColoredMoltenOptions>> MOLTEN_POP =
        PARTICLES.register("molten_pop", () -> new ParticleType<>(false, ColoredMoltenOptions.DESERIALIZER) {
            @Override
            public Codec<ColoredMoltenOptions> codec() {
                return ColoredMoltenOptions.CODEC;
            }
        });
}
