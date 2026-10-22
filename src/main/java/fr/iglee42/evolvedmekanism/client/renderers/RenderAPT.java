package fr.iglee42.evolvedmekanism.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.iglee42.evolvedmekanism.client.EMRenderTypes;
import fr.iglee42.evolvedmekanism.client.renderers.datas.CustomRenderData;
import fr.iglee42.evolvedmekanism.client.renderers.datas.MultipleCustomRenderData;
import fr.iglee42.evolvedmekanism.multiblock.apt.APTMultiblockData;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.tileentity.MultiblockTileEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.util.Lazy;

import java.util.Objects;

@NothingNullByDefault
public class RenderAPT extends MultiblockTileEntityRenderer<APTMultiblockData, TileEntityAPTCasing> {

    public RenderAPT(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void render(TileEntityAPTCasing tile, APTMultiblockData multiblock, float partialTick, PoseStack matrix, MultiBufferSource renderer,
          int light, int overlayLight, ProfilerFiller profiler) {
        BlockPos pos = tile.getBlockPos();
        Lazy<VertexConsumer> buffer = Lazy.of(() -> renderer.getBuffer(EMRenderTypes.TRANSLUCENT_NO_DEPTH));
        if (!multiblock.inputTank.isEmpty()) {
            MultipleCustomRenderData renderData = new MultipleCustomRenderData();

            float scaleY = (multiblock.height() - 2) * multiblock.prevGasScale;
            String middleData = renderData.add("middle", CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                    .location(Objects.requireNonNull(multiblock.renderLocation).offset(1, 0, 1)).dimensions(5, scaleY, 5).build()
            );
            renderData.setCustomFunc(middleData, model -> model.bounds(model.minX, model.minY, model.minZ + 0.001f, model.maxX, model.maxY, model.maxZ + 0.001f));
            renderData.renderAllDatas(getCamera(), pos, matrix, buffer.get(), overlayLight, 0.75f, multiblock);
        }
    }

    @Override
    protected String getProfilerSection() {
        return "APT";
    }
}
