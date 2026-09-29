package fr.iglee42.emgenerators.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fr.iglee42.emgenerators.tile.TileEntityTieredWindGenerator;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.tileentity.IWireFrameRenderer;
import mekanism.client.render.tileentity.ModelTileEntityRenderer;
import mekanism.generators.client.model.ModelWindGenerator;
import mekanism.generators.common.GeneratorsProfilerConstants;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public class RenderTieredWindGenerator extends ModelTileEntityRenderer<TileEntityTieredWindGenerator, ModelWindGenerator> implements IWireFrameRenderer {

    public RenderTieredWindGenerator(BlockEntityRendererProvider.Context context) {
        super(context, ModelWindGenerator::new);
    }

    @Override
    protected void render(TileEntityTieredWindGenerator tile, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {
        float angle = setupRenderer(tile, partialTick, matrix);
        WindModelTexture.set(WindModelTexture.forTier(tile.getTier()));
        try {
            model.render(matrix, renderer, angle, light, overlayLight, false);
        } finally {
            WindModelTexture.clear();
            matrix.popPose();
        }
    }

    @Override
    protected String getProfilerSection() {
        return GeneratorsProfilerConstants.WIND_GENERATOR;
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityTieredWindGenerator tile) {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(TileEntityTieredWindGenerator tile) {
        BlockPos pos = tile.getBlockPos();
        return AABB.encapsulatingFullBlocks(pos.offset(-2, 0, -2), pos.offset(2, 6, 2));
    }

    @Override
    public void renderWireFrame(BlockEntity tile, float partialTick, PoseStack matrix, VertexConsumer buffer) {
        if (tile instanceof TileEntityTieredWindGenerator windGenerator) {
            float angle = setupRenderer(windGenerator, partialTick, matrix);
            model.renderWireFrame(matrix, buffer, angle);
            matrix.popPose();
        }
    }

    private float setupRenderer(TileEntityTieredWindGenerator tile, float partialTick, PoseStack matrix) {
        matrix.pushPose();
        matrix.translate(0.5, 1.5, 0.5);
        MekanismRenderer.rotate(matrix, tile.getDirection(), 0, 180, 90, 270);
        matrix.mulPose(Axis.ZP.rotationDegrees(180));
        float angle = tile.getAngle();
        if (tile.getActive() && partialTick > 0) {
            angle = (angle + tile.getHeightSpeedRatio() * partialTick) % 360;
        }
        return angle;
    }
}
