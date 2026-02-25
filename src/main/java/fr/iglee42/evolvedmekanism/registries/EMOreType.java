package fr.iglee42.evolvedmekanism.registries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import mekanism.api.SerializationConstants;
import mekanism.common.resource.IResource;
import mekanism.common.resource.ore.BaseOreConfig;
import mekanism.common.resource.ore.OreAnchor;
import mekanism.common.world.height.HeightShape;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum EMOreType implements StringRepresentable {
    NOCTIS_ROZULI(EMResources.NOCTIS_ROZULI, 2, 8,
          new BaseOreConfig("normal", 2, 0, 7, HeightShape.TRAPEZOID, OreAnchor.absolute(-32), OreAnchor.absolute(32)),
          new BaseOreConfig("buried", 4, 0.5f, 7, HeightShape.TRAPEZOID, OreAnchor.aboveBottom(0), OreAnchor.absolute(64))
    );

    public static Codec<EMOreType> CODEC = StringRepresentable.fromEnum(EMOreType::values);

    private final List<BaseOreConfig> baseConfigs;
    private final IResource resource;
    private final int minExp;
    private final int maxExp;

    EMOreType(IResource resource, BaseOreConfig... configs) {
        this(resource, 0, configs);
    }

    EMOreType(IResource resource, int exp, BaseOreConfig... configs) {
        this(resource, exp, exp, configs);
    }

    EMOreType(IResource resource, int minExp, int maxExp, BaseOreConfig... configs) {
        this.resource = resource;
        this.minExp = minExp;
        this.maxExp = maxExp;
        this.baseConfigs = List.of(configs);
    }

    public IResource getResource() {
        return resource;
    }

    public List<BaseOreConfig> getBaseConfigs() {
        return baseConfigs;
    }

    public int getMinExp() {
        return minExp;
    }

    public int getMaxExp() {
        return maxExp;
    }

    public static EMOreType get(IResource resource) {
        for (EMOreType ore : values()) {
            if (resource == ore.resource) {
                return ore;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public String getSerializedName() {
        return resource.getRegistrySuffix();
    }

    public record OreVeinType(EMOreType type, int index) {

        public static final Codec<OreVeinType> CODEC = RecordCodecBuilder.create(builder -> builder.group(
              EMOreType.CODEC.fieldOf(SerializationConstants.TYPE).forGetter(config -> config.type),
              Codec.INT.fieldOf(SerializationConstants.INDEX).forGetter(config -> config.index)
        ).apply(builder, OreVeinType::new));

        public OreVeinType {
            if (index < 0 || index >= type.getBaseConfigs().size()) {
                throw new IndexOutOfBoundsException("Vein Type index out of range: " + index);
            }
        }

        public String name() {
            return "ore_" + type.getResource().getRegistrySuffix() + "_" + type.getBaseConfigs().get(index).name();
        }
    }
}