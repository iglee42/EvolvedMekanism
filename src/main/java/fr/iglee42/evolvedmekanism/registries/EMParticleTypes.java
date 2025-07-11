package fr.iglee42.evolvedmekanism.registries;

import com.mojang.serialization.Codec;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.particles.ColoredRisingBubleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EMParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
        DeferredRegister.create(Registries.PARTICLE_TYPE, EvolvedMekanism.MODID);

    public static final RegistryObject<ParticleType<ColoredRisingBubleOptions>> RISING_BUBBLE =
        PARTICLES.register("rising_bubble", ()-> new ParticleType<>(false, ColoredRisingBubleOptions.DESERIALIZER) {
            @Override
            public Codec<ColoredRisingBubleOptions> codec() {
                return ColoredRisingBubleOptions.CODEC;
            }
        });
}
