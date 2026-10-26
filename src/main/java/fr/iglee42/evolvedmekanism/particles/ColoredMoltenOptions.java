package fr.iglee42.evolvedmekanism.particles;

import java.util.Locale;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.evolvedmekanism.registries.EMParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class ColoredMoltenOptions implements ParticleOptions {

    public static final Deserializer<ColoredMoltenOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public ColoredMoltenOptions fromCommand(ParticleType<ColoredMoltenOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float b = reader.readFloat();
            return new ColoredMoltenOptions(r, g, b);
        }

        @Override
        public ColoredMoltenOptions fromNetwork(ParticleType<ColoredMoltenOptions> type, FriendlyByteBuf buf) {
            return new ColoredMoltenOptions(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
    };

    public static final Codec<ColoredMoltenOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("r").forGetter(ColoredMoltenOptions::red),
            Codec.FLOAT.fieldOf("g").forGetter(ColoredMoltenOptions::green),
            Codec.FLOAT.fieldOf("b").forGetter(ColoredMoltenOptions::blue)
    ).apply(instance, ColoredMoltenOptions::new));

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
