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

public class ColoredRisingBubleOptions implements
        ParticleOptions {

    public static final Deserializer<ColoredRisingBubleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public ColoredRisingBubleOptions fromCommand(ParticleType<ColoredRisingBubleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            return new ColoredRisingBubleOptions(r, g, b);
        }

        @Override
        public ColoredRisingBubleOptions fromNetwork(ParticleType<ColoredRisingBubleOptions> type, FriendlyByteBuf buf) {
            return new ColoredRisingBubleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
    };

    public static final Codec<ColoredRisingBubleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("r").forGetter(p -> p.r),
            Codec.FLOAT.fieldOf("g").forGetter(p -> p.g),
            Codec.FLOAT.fieldOf("b").forGetter(p -> p.b)
    ).apply(instance, ColoredRisingBubleOptions::new));

    private final float r, g, b;

    public ColoredRisingBubleOptions(float r, float g, float b) {
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
