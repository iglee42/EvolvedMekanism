package fr.iglee42.evolvedmekanism.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

public class EMRenderTypes {

    public static final RenderType TRANSLUCENT_NO_DEPTH = RenderType.create("translucent_no_depth",
        DefaultVertexFormat.BLOCK,
        VertexFormat.Mode.QUADS,
        256,
        true,
        true,
        RenderType.CompositeState.builder()
            .setShaderState(RenderType.RENDERTYPE_TRANSLUCENT_SHADER)
            .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
            .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
            .setWriteMaskState(RenderType.COLOR_WRITE)
            .setCullState(RenderType.CULL)
            .setLightmapState(RenderType.LIGHTMAP)
            .setOverlayState(RenderType.OVERLAY)
            .createCompositeState(true)
    );
}
