package fr.iglee42.evolvedmekanism.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.evolvedmekanism.registries.EMParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Locale;

public class ColoredRisingBubbleOptions implements
        ParticleOptions {

    public static final Deserializer<ColoredRisingBubbleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public ColoredRisingBubbleOptions fromCommand(ParticleType<ColoredRisingBubbleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            return new ColoredRisingBubbleOptions(r, g, b);
        }

        @Override
        public ColoredRisingBubbleOptions fromNetwork(ParticleType<ColoredRisingBubbleOptions> type, FriendlyByteBuf buf) {
            return new ColoredRisingBubbleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
    };

    public static final Codec<ColoredRisingBubbleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("r").forGetter(p -> p.r),
            Codec.FLOAT.fieldOf("g").forGetter(p -> p.g),
            Codec.FLOAT.fieldOf("b").forGetter(p -> p.b)
    ).apply(instance, ColoredRisingBubbleOptions::new));

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

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(r);
        buf.writeFloat(g);
        buf.writeFloat(b);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%f %f %f", r, g, b);
    }
}
