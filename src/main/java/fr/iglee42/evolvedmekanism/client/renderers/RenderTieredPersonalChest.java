package fr.iglee42.evolvedmekanism.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.tiles.TileEntityTieredPersonalChest;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.tileentity.MekanismTileEntityRenderer;
import mekanism.common.base.ProfilerConstants;
import mekanism.common.tile.TileEntityPersonalChest;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;

@NothingNullByDefault
public class RenderTieredPersonalChest extends MekanismTileEntityRenderer<TileEntityTieredPersonalChest> {

    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;

    public RenderTieredPersonalChest(BlockEntityRendererProvider.Context context) {
        super(context);
        ModelPart modelpart = context.bakeLayer(ModelLayers.CHEST);
        this.bottom = modelpart.getChild("bottom");
        this.lid = modelpart.getChild("lid");
        this.lock = modelpart.getChild("lock");
    }

    @Override
    protected void render(TileEntityTieredPersonalChest tile, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {
        matrix.pushPose();
        if (!tile.isRemoved()) {
            matrix.translate(0.5D, 0.5D, 0.5D);
            matrix.mulPose(Axis.YP.rotationDegrees(-tile.getDirection().toYRot()));
            matrix.translate(-0.5D, -0.5D, -0.5D);
        }
        ResourceLocation texture = EvolvedMekanism.getResource(ResourceType.TEXTURE_BLOCKS, "models/"+tile.getTier().getBaseTier().getSerializedName()+"_personal_chest.png");
        float lidAngle = 1.0F - tile.getOpenNess(partialTick);
        lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
        VertexConsumer builder = renderer.getBuffer(RenderType.entityCutout(texture));
        lid.xRot = -(lidAngle * ((float) Math.PI / 2F));
        lock.xRot = lid.xRot;
        lid.render(matrix, builder, light, overlayLight);
        lock.render(matrix, builder, light, overlayLight);
        bottom.render(matrix, builder, light, overlayLight);
        matrix.popPose();
    }

    @Override
    protected String getProfilerSection() {
        return "tieredPersonalChest";
    }
}