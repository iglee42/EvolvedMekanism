package fr.iglee42.evolvedmekanism.client.renderers.datas;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.iglee42.evolvedmekanism.client.CustomModelRenderer;
import fr.iglee42.evolvedmekanism.multiblock.apt.APTMultiblockData;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.ModelRenderer;
import mekanism.client.render.RenderResizableCuboid.FaceDisplay;
import mekanism.common.lib.multiblock.MultiblockData;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class MultipleCustomRenderData {

    private static final Map<String, MekanismRenderer.Model3D> cachedModels = new Object2ObjectOpenHashMap<>();
    private final Map<String,CustomRenderData> datas = new HashMap<>();
    private final Map<String, Set<Direction>> ignoredFaces = new Object2ObjectOpenHashMap<>();
    private final Map<String, Function<MekanismRenderer.Model3D, MekanismRenderer.Model3D>> customFunc = new Object2ObjectOpenHashMap<>();

    public MultipleCustomRenderData() {
    }

    public Map<String, CustomRenderData> getDatas() {
        return datas;
    }

    public String add(String id,CustomRenderData data,Direction... ignoredFaces){
        this.datas.put(id,data);
        this.ignoredFaces.put(id,Set.of(ignoredFaces));
        return id;
    }

    public void setCustomFunc(String data, Function<MekanismRenderer.Model3D, MekanismRenderer.Model3D> axis){
        this.customFunc.put(data,axis);
    }

    public void renderAllDatas(Camera camera, BlockPos rendererPos, PoseStack matrix, VertexConsumer buffer, int overlay, float scale, MultiblockData multiblock){
        datas.forEach((id,data)->{
            MekanismRenderer.Model3D gasModel = cachedModels.computeIfAbsent(id, d ->{
                MekanismRenderer.Model3D model = CustomModelRenderer.getModel(data, 1).copy().setSideRender(dir -> !ignoredFaces.getOrDefault(id, new HashSet<>()).contains(dir));
                model = customFunc.getOrDefault(id,m-> m).apply(model);
                return model;
            });

            if (multiblock instanceof APTMultiblockData apt)gasModel = gasModel.bounds(gasModel.minX, gasModel.minY,gasModel.minZ,gasModel.maxX,(apt.height() - 2) * apt.prevGasScale,gasModel.maxZ);

            renderObject(camera,data,rendererPos,gasModel,matrix,buffer,overlay,scale);
        });
    }

    private void renderObject(Camera camera, CustomRenderData data, BlockPos rendererPos, MekanismRenderer.Model3D object, @NotNull PoseStack matrix, VertexConsumer buffer, int overlay, float scale) {
        int glow = data.calculateGlowLight(LightTexture.FULL_SKY);
        matrix.pushPose();
        matrix.translate(data.location.getX() - rendererPos.getX(), data.location.getY() - rendererPos.getY(), data.location.getZ() - rendererPos.getZ());
        MekanismRenderer.renderObject(object, matrix, buffer, data.getColorARGB(scale), 0xF000F0, overlay, getCustomFaceDisplay(camera, data, object), camera, data.location);
        matrix.popPose();
    }

    private FaceDisplay getCustomFaceDisplay(Camera camera, CustomRenderData data, MekanismRenderer.Model3D model) {
        return isInsideBounds(camera, data.location.getX(), data.location.getY(), data.location.getZ(),
                data.location.getX() + data.length, data.location.getY() + ModelRenderer.getActualHeight(model), data.location.getZ() + data.width)
                ? FaceDisplay.BACK : FaceDisplay.FRONT;
    }

    private boolean isInsideBounds(Camera camera, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        Vec3 projectedView = camera.getPosition();
        return minX <= projectedView.x && projectedView.x <= maxX &&
                minY <= projectedView.y && projectedView.y <= maxY &&
                minZ <= projectedView.z && projectedView.z <= maxZ;
    }

    public static void clearCaches(){
        cachedModels.clear();
    }

}
