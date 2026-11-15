package fr.iglee42.evolvedmekanism.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.evolvedmekanism.registries.EMParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ColoredMoltenOptions implements ParticleOptions {

    public static final MapCodec<ColoredMoltenOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("r").forGetter(ColoredMoltenOptions::red),
            Codec.FLOAT.fieldOf("g").forGetter(ColoredMoltenOptions::green),
            Codec.FLOAT.fieldOf("b").forGetter(ColoredMoltenOptions::blue)
    ).apply(instance, ColoredMoltenOptions::new));

    public static final StreamCodec<FriendlyByteBuf, ColoredMoltenOptions> STREAM_CODEC = StreamCodec.of(
            (buf, options) -> {
                buf.writeFloat(options.r);
                buf.writeFloat(options.g);
                buf.writeFloat(options.b);
            },
            buf -> new ColoredMoltenOptions(buf.readFloat(), buf.readFloat(), buf.readFloat())
    );

    private final float r;
    private final float g;
    private final float b;

    public ColoredMoltenOptions(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static ColoredMoltenOptions fromArgb(int argb) {
        return new ColoredMoltenOptions(
                ((argb >> 16) & 0xFF) / 255.0F,
                ((argb >> 8) & 0xFF) / 255.0F,
                (argb & 0xFF) / 255.0F
        );
    }

    public float red() {
        return r;
    }

    public float green() {
        return g;
    }

    public float blue() {
        return b;
    }

    @Override
    public ParticleType<?> getType() {
        return EMParticleTypes.MOLTEN_POP.get();
    }
}
