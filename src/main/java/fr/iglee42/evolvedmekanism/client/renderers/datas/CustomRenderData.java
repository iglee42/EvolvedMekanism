package fr.iglee42.evolvedmekanism.client.renderers.datas;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.ChemicalStack;
import mekanism.common.lib.multiblock.MultiblockData;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@NothingNullByDefault
public abstract class CustomRenderData {

    public final BlockPos location;
    public final float height;
    public final float length;
    public final float width;

    protected CustomRenderData(BlockPos renderLocation, float width, float height, float length) {
        this.location = renderLocation;
        this.width = width;
        this.height = height;
        this.length = length;
    }

    public abstract TextureAtlasSprite getTexture();

    public abstract boolean isGaseous();

    public abstract int getColorARGB(float scale);

    public int calculateGlowLight(int light) {
        return light;
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, length, width);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof CustomRenderData data && data.height == height && data.length == length && data.width == width;
    }

    public static class Builder<DATA_TYPE extends CustomRenderData> {

        @Nullable
        private final ChemicalStack chemical;
        private final FluidStack fluid;
        @Nullable
        private BlockPos location;
        private float height;
        private float length;
        private float width;

        private Builder(@Nullable ChemicalStack chemical, FluidStack fluid) {
            this.chemical = chemical;
            this.fluid = fluid;
        }

        public static  Builder<CustomChemicalRenderData> create(ChemicalStack chemical) {
            if (chemical.isEmpty()) {
                throw new IllegalArgumentException("Chemical may not be empty");
            }
            return new Builder<>(chemical, FluidStack.EMPTY);
        }

        public static Builder<FluidRenderData> create(FluidStack fluid) {
            if (fluid.isEmpty()) {
                throw new IllegalArgumentException("Fluid may not be empty");
            }
            return new Builder<>(ChemicalStack.EMPTY, fluid);
        }

        public Builder<DATA_TYPE> location(BlockPos renderLocation) {
            this.location = renderLocation;
            return this;
        }

        public Builder<DATA_TYPE> height(float height) {
            this.height = height;
            return this;
        }

        public Builder<DATA_TYPE> length(float length) {
            this.length = length;
            return this;
        }

        public Builder<DATA_TYPE> width(float width) {
            this.width = width;
            return this;
        }

        public Builder<DATA_TYPE> dimensions(float width, float height, float length) {
            return width(width).height(height).length(length);
        }

        public Builder<DATA_TYPE> of(MultiblockData multiblock) {
            return location(Objects.requireNonNull(multiblock.renderLocation, "Render location may not be null."))
                  .height(multiblock.height() - 2)
                  .length(multiblock.length())
                  .width(multiblock.width());
        }

        public DATA_TYPE build() {
            if (location == null) {
                throw new IllegalStateException("Incomplete render data builder, no render location set.");
            }
            CustomRenderData data;
            if (!fluid.isEmpty()) {
                data = new FluidRenderData(location, width, height, length, fluid);
            } else if (!chemical.isEmpty()) {
                data = new CustomChemicalRenderData(location, width, height, length, chemical);
            } else {
                throw new IllegalStateException("Incomplete render data builder, missing or unknown chemical or fluid.");
            }
            //noinspection unchecked
            return (DATA_TYPE) data;
        }
    }
}