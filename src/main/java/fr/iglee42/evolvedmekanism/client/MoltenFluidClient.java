package fr.iglee42.evolvedmekanism.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.iglee42.evolvedmekanism.registries.MoltenFluidType;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

/**
 * In-fluid view uses lava's fog distances and the molten color. Lava has no screen texture;
 * the flame overlay comes from the player being on fire.
 */
public class MoltenFluidClient implements IClientFluidTypeExtensions {

    private final MoltenFluidType fluidType;

    public MoltenFluidClient(MoltenFluidType fluidType) {
        this.fluidType = fluidType;
    }

    @Override
    public ResourceLocation getStillTexture() {
        return fluidType.stillTexture;
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        return fluidType.flowingTexture;
    }

    @Override
    public ResourceLocation getOverlayTexture() {
        return fluidType.overlayTexture;
    }

    @Nullable
    @Override
    public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
        return null;
    }

    @Override
    public int getTintColor() {
        return fluidType.color;
    }

    @NotNull
    @Override
    public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        int color = fluidType.color;
        return new Vector3f(((color >> 16) & 0xFF) / 255.0F, ((color >> 8) & 0xFF) / 255.0F, (color & 0xFF) / 255.0F);
    }

    @Override
    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
        Entity entity = camera.getEntity();
        float start = 0.25F;
        float end = 1.0F;
        if (entity.isSpectator()) {
            start = -8.0F;
            end = renderDistance * 0.5F;
        } else if (entity instanceof LivingEntity living && living.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            start = 0.0F;
            end = 3.0F;
        }
        if (end > renderDistance) {
            end = renderDistance;
            shape = FogShape.CYLINDER;
        }
        RenderSystem.setShaderFogStart(start);
        RenderSystem.setShaderFogEnd(end);
        RenderSystem.setShaderFogShape(shape);
    }
}
