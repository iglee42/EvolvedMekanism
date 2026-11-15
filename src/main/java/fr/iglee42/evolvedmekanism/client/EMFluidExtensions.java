package fr.iglee42.evolvedmekanism.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import fr.iglee42.evolvedmekanism.registries.MoltenFluidType;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.registration.impl.FluidDeferredRegister.MekanismFluidType;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

/**
 * Molten fluids keep Mekanism's tinted textures, but the in-fluid fog matches lava's distances
 * and uses the molten color. Other fluids stay on Mekanism's water fog.
 */
public final class EMFluidExtensions {

    private EMFluidExtensions() {
    }

    public static void register(RegisterClientExtensionsEvent event) {
        for (var entry : EMFluids.FLUIDS.getFluidTypeEntries()) {
            if (entry.value() instanceof MekanismFluidType fluidType) {
                event.registerFluidType(fluidType instanceof MoltenFluidType ? molten(fluidType) : chemical(fluidType), fluidType);
            }
        }
    }

    private static IClientFluidTypeExtensions molten(MekanismFluidType fluidType) {
        return new IClientFluidTypeExtensions() {
            @NotNull
            @Override
            public ResourceLocation getStillTexture() {
                return fluidType.stillTexture;
            }

            @NotNull
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
            public Vector3f modifyFogColor(@NotNull Camera camera, float partialTick, @NotNull ClientLevel level, int renderDistance, float darkenWorldAmount, @NotNull Vector3f fluidFogColor) {
                int color = getTintColor();
                return new Vector3f(MekanismRenderer.getRed(color), MekanismRenderer.getGreen(color), MekanismRenderer.getBlue(color));
            }

            @Override
            public void modifyFogRender(@NotNull Camera camera, @NotNull FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, @NotNull FogShape shape) {
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
        };
    }

    private static IClientFluidTypeExtensions chemical(MekanismFluidType fluidType) {
        return new IClientFluidTypeExtensions() {
            @NotNull
            @Override
            public ResourceLocation getStillTexture() {
                return fluidType.stillTexture;
            }

            @NotNull
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
                return fluidType.renderOverlayTexture;
            }

            @Override
            public int getTintColor() {
                return fluidType.color;
            }

            @NotNull
            @Override
            public Vector3f modifyFogColor(@NotNull Camera camera, float partialTick, @NotNull ClientLevel level, int renderDistance, float darkenWorldAmount, @NotNull Vector3f fluidFogColor) {
                int color = getTintColor();
                return new Vector3f(MekanismRenderer.getRed(color), MekanismRenderer.getGreen(color), MekanismRenderer.getBlue(color));
            }

            @Override
            public void modifyFogRender(@NotNull Camera camera, @NotNull FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, @NotNull FogShape shape) {
                farDistance = 24F;
                if (farDistance > renderDistance) {
                    farDistance = renderDistance;
                    shape = FogShape.CYLINDER;
                }
                RenderSystem.setShaderFogStart(-8);
                RenderSystem.setShaderFogEnd(farDistance);
                RenderSystem.setShaderFogShape(shape);
            }
        };
    }
}
