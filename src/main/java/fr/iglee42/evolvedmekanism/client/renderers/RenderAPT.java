package fr.iglee42.evolvedmekanism.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.iglee42.evolvedmekanism.client.renderers.datas.CustomRenderData;
import fr.iglee42.evolvedmekanism.client.renderers.datas.MultipleCustomRenderData;
import fr.iglee42.evolvedmekanism.multiblock.apt.APTMultiblockData;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.render.data.FluidRenderData;
import mekanism.client.render.data.RenderData;
import mekanism.client.render.tileentity.MultiblockTileEntityRenderer;
import mekanism.common.registries.MekanismFluids;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
        Lazy<VertexConsumer> buffer = Lazy.of(() -> renderer.getBuffer(Sheets.translucentCullBlockSheet()));
        if (!multiblock.inputTank.isEmpty()) {
            MultipleCustomRenderData renderData = new MultipleCustomRenderData();

            float scaleY = (multiblock.height() - 2) * multiblock.prevGasScale;
            String middleData = renderData.add("middle",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                    .location(Objects.requireNonNull(multiblock.renderLocation).offset(3,0,0)).dimensions(9,scaleY,3).build()
            ,Direction.WEST,Direction.EAST);
            renderData.setCustomFunc(middleData, model->model.bounds(model.minX,model.minY,model.minZ+0.01f,model.maxX,model.maxY,model.maxZ-0.01f));
            // WEST MID
            String westMid = renderData.add("westMid",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                    .location(Objects.requireNonNull(multiblock.renderLocation).offset(2,0,2)).dimensions(5,scaleY,1).build()
            , Direction.EAST,Direction.NORTH,Direction.SOUTH,Direction.WEST);
            renderData.setCustomFunc(westMid, model->model.bounds(model.minX,model.minY,model.minZ,model.maxX + 0.03f,model.maxY,model.maxZ));
            String westMidN = renderData.add("westMidN",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                            .location(Objects.requireNonNull(multiblock.renderLocation).offset(2,0,1)).dimensions(1,scaleY,1).build()
                    , Direction.EAST,Direction.SOUTH);
            renderData.setCustomFunc(westMidN, model->model.bounds(model.minX + 0.001f,model.minY,model.minZ + 0.001f,model.maxX + 0.03f,model.maxY,model.maxZ + 0.03f));
            String westMidS = renderData.add("westMidS",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                            .location(Objects.requireNonNull(multiblock.renderLocation).offset(2,0,7)).dimensions(1,scaleY,1).build()
                    , Direction.EAST,Direction.NORTH);
            renderData.setCustomFunc(westMidS, model->model.bounds(model.minX + 0.001f,model.minY,model.minZ - 0.03f,model.maxX + 0.03f,model.maxY,model.maxZ));
            //WEST END
            String westEnd = renderData.add("westEnd",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                            .location(Objects.requireNonNull(multiblock.renderLocation).offset(0,0,3)).dimensions(3,scaleY,2).build()
                    , Direction.EAST,Direction.NORTH,Direction.SOUTH);
            renderData.setCustomFunc(westEnd, model->model.bounds(model.minX + 0.001f,model.minY,model.minZ,model.maxX + 0.03f,model.maxY,model.maxZ));
            String westEndN = renderData.add("westEndN",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                            .location(Objects.requireNonNull(multiblock.renderLocation).offset(1,0,2)).dimensions(1,scaleY,1).build()
                    , Direction.EAST,Direction.SOUTH);
            renderData.setCustomFunc(westEndN, model->model.bounds(model.minX + 0.001f,model.minY,model.minZ + 0.001f,model.maxX + 0.03f,model.maxY,model.maxZ + 0.03f));
            String westEndS = renderData.add("westEndS",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                            .location(Objects.requireNonNull(multiblock.renderLocation).offset(1,0,6)).dimensions(1,scaleY,1).build()
                    , Direction.EAST,Direction.NORTH);
            renderData.setCustomFunc(westEndS, model->model.bounds(model.minX + 0.001f,model.minY,model.minZ - 0.03f,model.maxX + 0.03f,model.maxY,model.maxZ));

            // EAST MID
            String eastMid = renderData.add("eastMid",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                            .location(Objects.requireNonNull(multiblock.renderLocation).offset(6,0,2)).dimensions(5,scaleY,1).build()
                    , Direction.EAST,Direction.NORTH,Direction.SOUTH,Direction.WEST);
            renderData.setCustomFunc(eastMid, model->model.bounds(model.minX  - 0.03f,model.minY,model.minZ,model.maxX,model.maxY,model.maxZ));
            String eastMidN = renderData.add("eastMidN",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                            .location(Objects.requireNonNull(multiblock.renderLocation).offset(6,0,1)).dimensions(1,scaleY,1).build()
                    , Direction.WEST,Direction.SOUTH);
            renderData.setCustomFunc(eastMidN, model->model.bounds(model.minX - 0.03f,model.minY,model.minZ + 0.001f,model.maxX,model.maxY,model.maxZ + 0.03f));
            String eastMidS = renderData.add("eastMidS",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                            .location(Objects.requireNonNull(multiblock.renderLocation).offset(6,0,7)).dimensions(1,scaleY,1).build()
                    , Direction.WEST,Direction.NORTH);
            renderData.setCustomFunc(eastMidS, model->model.bounds(model.minX - 0.03f,model.minY,model.minZ - 0.03f,model.maxX ,model.maxY,model.maxZ));

            //EAST END
            String eastEnd = renderData.add("eastEnd",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                            .location(Objects.requireNonNull(multiblock.renderLocation).offset(7,0,3)).dimensions(3,scaleY,2).build()
                    , Direction.WEST,Direction.NORTH,Direction.SOUTH);
            renderData.setCustomFunc(eastEnd, model->model.bounds(model.minX - 0.03f,model.minY,model.minZ,model.maxX - 0.001f,model.maxY,model.maxZ));
            String eastEndN = renderData.add("eastEndN",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                            .location(Objects.requireNonNull(multiblock.renderLocation).offset(7,0,2)).dimensions(1,scaleY,1).build()
                    , Direction.WEST,Direction.SOUTH);
            renderData.setCustomFunc(eastEndN, model->model.bounds(model.minX - 0.03f,model.minY,model.minZ + 0.001f,model.maxX,model.maxY,model.maxZ + 0.03f));
            String eastEndS = renderData.add("eastEndS",CustomRenderData.Builder.create(multiblock.inputTank.getStack())
                            .location(Objects.requireNonNull(multiblock.renderLocation).offset(7,0,6)).dimensions(1,scaleY,1).build()
                    , Direction.WEST,Direction.NORTH);
            renderData.setCustomFunc(eastEndS, model->model.bounds(model.minX - 0.03f,model.minY,model.minZ - 0.03f,model.maxX ,model.maxY,model.maxZ));


            renderData.renderAllDatas(getCamera(),pos,matrix,buffer.get(),overlayLight,0.75f,multiblock);
        }
    }

    @Override
    protected String getProfilerSection() {
        return "APT";
    }
}