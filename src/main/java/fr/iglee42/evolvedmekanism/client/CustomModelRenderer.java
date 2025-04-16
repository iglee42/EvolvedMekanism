package fr.iglee42.evolvedmekanism.client;

import fr.iglee42.evolvedmekanism.client.renderers.datas.CustomRenderData;
import it.unimi.dsi.fastutil.floats.Float2ObjectMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;

import mekanism.client.render.MekanismRenderer.Model3D;
import mekanism.common.util.MekanismUtils;
import net.minecraftforge.fluids.FluidStack;

public final class CustomModelRenderer {

    private CustomModelRenderer() {
    }

    private static final int BLOCK_STAGES = 1_000;

    private static final Map<CustomRenderData, Float2ObjectMap<Model3D>> cachedCenterData = new Object2ObjectOpenHashMap<>();

    public static int getStage(FluidStack stack, int stages, double scale) {
        return getStage(MekanismUtils.lighterThanAirGas(stack), stages, scale);
    }

    public static int getStage(boolean gaseous, int stages, double scale) {
        if (gaseous) {
            return stages - 1;
        }
        return Math.min(stages - 1, (int) (scale * (stages - 1)));
    }

    /**
     * @apiNote If the data is gaseous then scale is ignored
     */
    public static Model3D getModel(CustomRenderData data, double scale) {
        float maxStages = Math.max(data.height * BLOCK_STAGES, 1);
        float stage;
        if (data.height == 0) {
            //If there is no height set it to 1 for the stage as max stages is going to be one as well
            stage = 1;
        } else if (data.isGaseous()) {
            stage = maxStages;
        } else {
            stage = Math.min(maxStages, (int) (scale * maxStages));
        }
        return cachedCenterData.computeIfAbsent(data, d -> new Float2ObjectOpenHashMap<>())
              .computeIfAbsent(stage, s -> new Model3D()
                    .setTexture(data.getTexture())
                    .xBounds(0.01F, data.length - 0.02F)
                    .yBounds(0.01F, data.height * (s / (float) maxStages) - 0.02F)
                    .zBounds(0.01F, data.width - 0.02F)
              );
    }

    //Undoes the z-fighting height shift from the model
    public static float getActualHeight(Model3D model) {
        return model.maxY + 0.02F;
    }


    public static void resetCachedModels() {
        cachedCenterData.clear();
    }
}