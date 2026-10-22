package fr.iglee42.emgenerators.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fr.iglee42.emgenerators.tile.TileEntityTieredWindGenerator;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.tileentity.IWireFrameRenderer;
import mekanism.client.render.tileentity.ModelTileEntityRenderer;
import mekanism.generators.client.model.ModelWindGenerator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RenderTieredWindGenerator extends ModelTileEntityRenderer<TileEntityTieredWindGenerator, ModelWindGenerator> implements IWireFrameRenderer {

    public RenderTieredWindGenerator(BlockEntityRendererProvider.Context context) {
        super(context, ModelWindGenerator::new);
    }

    @Override
    protected void render(TileEntityTieredWindGenerator tile, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {
        double angle = setupRenderer(tile, partialTick, matrix);
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
        return "windGenerator";
    }

    @Override
    public boolean shouldRenderOffScreen(TileEntityTieredWindGenerator tile) {
        return true;
    }

    @Override
    public void renderWireFrame(BlockEntity tile, float partialTick, PoseStack matrix, VertexConsumer buffer, int red, int green, int blue, int alpha) {
        if (tile instanceof TileEntityTieredWindGenerator windGenerator) {
            double angle = setupRenderer(windGenerator, partialTick, matrix);
            model.renderWireFrame(matrix, buffer, angle, red, green, blue, alpha);
            matrix.popPose();
        }
    }

    private double setupRenderer(TileEntityTieredWindGenerator tile, float partialTick, PoseStack matrix) {
        matrix.pushPose();
        matrix.translate(0.5, 1.5, 0.5);
        MekanismRenderer.rotate(matrix, tile.getDirection(), 0, 180, 90, 270);
        matrix.mulPose(Axis.ZP.rotationDegrees(180));
        double angle = tile.getAngle();
        if (tile.getActive()) {
            float heightRatio = (tile.getBlockPos().getY() + 4) / 8F;
            angle = (angle + heightRatio * partialTick) % 360;
        }
        return angle;
    }
}
