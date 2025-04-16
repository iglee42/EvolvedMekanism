package fr.iglee42.evolvedmekanism.client.renderers.datas;

import java.util.Objects;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.client.render.MekanismRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public abstract class CustomChemicalRenderData<CHEMICAL extends Chemical<CHEMICAL>> extends CustomRenderData {

    public final CHEMICAL chemical;

    protected CustomChemicalRenderData(BlockPos renderLocation, float width, float height, float length, CHEMICAL chemical) {
        super(renderLocation, width, height, length);
        this.chemical = chemical;
    }

    @Override
    public int getColorARGB(float scale) {
        return MekanismRenderer.getColorARGB(chemical, scale, isGaseous());
    }

    @Override
    public TextureAtlasSprite getTexture() {
        return MekanismRenderer.getChemicalTexture(chemical);
    }

    @Override
    public boolean isGaseous() {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), chemical);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == this) {
            return true;
        } else if (o == null || getClass() != o.getClass() || !super.equals(o)) {
            return false;
        }
        return chemical == ((CustomChemicalRenderData<?>) o).chemical;
    }

    public static class GasRenderData extends CustomChemicalRenderData<Gas> {

        public GasRenderData(BlockPos renderLocation, float width, float height, float length, Gas gas) {
            super(renderLocation, width, height, length, gas);
        }

        @Override
        public boolean isGaseous() {
            return true;
        }
    }

    public static class InfusionRenderData extends CustomChemicalRenderData<InfuseType> {

        public InfusionRenderData(BlockPos renderLocation, float width, float height, float length, InfuseType infuseType) {
            super(renderLocation, width, height, length, infuseType);
        }
    }

    public static class PigmentRenderData extends CustomChemicalRenderData<Pigment> {

        public PigmentRenderData(BlockPos renderLocation, float width, float height, float length, Pigment pigment) {
            super(renderLocation, width, height, length, pigment);
        }
    }

    public static class SlurryRenderData extends CustomChemicalRenderData<Slurry> {

        public SlurryRenderData(BlockPos renderLocation, float width, float height, float length, Slurry slurry) {
            super(renderLocation, width, height, length, slurry);
        }
    }
}