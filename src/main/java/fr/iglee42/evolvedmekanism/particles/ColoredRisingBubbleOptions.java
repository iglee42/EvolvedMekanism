package fr.iglee42.evolvedmekanism.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.evolvedmekanism.registries.EMParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Locale;

public class ColoredRisingBubbleOptions implements
        ParticleOptions {


    public static final MapCodec<ColoredRisingBubbleOptions> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("r").forGetter(p -> p.r),
            Codec.FLOAT.fieldOf("g").forGetter(p -> p.g),
            Codec.FLOAT.fieldOf("b").forGetter(p -> p.b)
    ).apply(instance, ColoredRisingBubbleOptions::new));
    public static final StreamCodec<FriendlyByteBuf,ColoredRisingBubbleOptions> STREAM_CODEC = StreamCodec.of(
            (buf,o)->{
                buf.writeFloat(o.r);
                buf.writeFloat(o.g);
                buf.writeFloat(o.b);
            },buf-> new ColoredRisingBubbleOptions(buf.readFloat(),buf.readFloat(),buf.readFloat())
    );


    private final float r, g, b;

    public ColoredRisingBubbleOptions(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public float red() { return r; }
    public float green() { return g; }
    public float blue() { return b; }

    @Override
    public ParticleType<?> getType() {
        return EMParticleTypes.RISING_BUBBLE.get();
    }

}
