package fr.iglee42.emgenerators.client;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.iglee42.emgenerators.items.ItemBlockTieredWindGenerator;
import mekanism.client.render.item.MekanismISTER;
import mekanism.generators.client.model.ModelWindGenerator;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RenderTieredWindGeneratorItem extends MekanismISTER {

    public static final RenderTieredWindGeneratorItem RENDERER = new RenderTieredWindGeneratorItem();
    private static float lastTicksUpdated;
    private static int angle;
    private ModelWindGenerator windGenerator;

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        windGenerator = new ModelWindGenerator(getEntityModels());
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack matrix, @NotNull MultiBufferSource renderer, int light, int overlayLight) {
        float partialTick = Minecraft.getInstance().getFrameTime();
        if (lastTicksUpdated != partialTick) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                List<ResourceLocation> blacklist = MekanismGeneratorsConfig.generators.windGenerationDimBlacklist.get();
                if (blacklist.isEmpty() || !blacklist.contains(minecraft.level.dimension().location())) {
                    angle = (angle + 2) % 360;
                }
            }
            lastTicksUpdated = partialTick;
        }
        matrix.pushPose();
        matrix.translate(0.5, 0.5, 0.5);
        matrix.mulPose(Axis.ZP.rotationDegrees(180));
        if (stack.getItem() instanceof ItemBlockTieredWindGenerator item) {
            WindModelTexture.set(WindModelTexture.forTier(item.getTier()));
        }
        try {
            windGenerator.render(matrix, renderer, angle, light, overlayLight, stack.hasFoil());
        } finally {
            WindModelTexture.clear();
            matrix.popPose();
        }
    }
}
