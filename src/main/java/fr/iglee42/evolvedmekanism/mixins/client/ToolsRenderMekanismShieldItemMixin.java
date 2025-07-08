package fr.iglee42.evolvedmekanism.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import fr.iglee42.emtools.client.EMShieldTextures;
import fr.iglee42.emtools.registries.EMToolsItems;
import mekanism.api.NBTConstants;
import mekanism.common.Mekanism;
import mekanism.common.util.RegistryUtils;
import mekanism.tools.client.ShieldTextures;
import mekanism.tools.client.render.item.RenderMekanismShieldItem;
import mekanism.tools.common.registries.ToolsItems;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = RenderMekanismShieldItem.class,remap = false)
public class ToolsRenderMekanismShieldItemMixin {


    @Shadow private ShieldModel shieldModel;

    @Inject(method = "renderByItem", at = @At("HEAD"),cancellable = true)
    private void em$changeForEmShields(ItemStack stack, ItemDisplayContext displayContext, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, CallbackInfo ci){
        if (stack.is(EMToolsItems.PLASLITHERITE_SHIELD.asItem()) || stack.is(EMToolsItems.BETTER_GOLD_SHIELD.asItem())){
            Item item = stack.getItem();
            ShieldTextures textures;
            if (item == EMToolsItems.PLASLITHERITE_SHIELD.asItem()) {
                textures = EMShieldTextures.PLASLITHERITE;
            } else if (item == EMToolsItems.BETTER_GOLD_SHIELD.asItem()) {
                textures = EMShieldTextures.BETTER_GOLD;
            } else {
                return;
            }
            Material material = textures.getBase();
            matrix.pushPose();
            matrix.scale(1, -1, -1);
            VertexConsumer buffer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(renderer, shieldModel.renderType(material.atlasLocation()), true, stack.hasFoil()));
            if (stack.getTagElement(NBTConstants.BLOCK_ENTITY_TAG) != null) {
                shieldModel.handle().render(matrix, buffer, light, overlayLight, 1, 1, 1, 1);
                List<Pair<Holder<BannerPattern>, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(stack), BannerBlockEntity.getItemPatterns(stack));
                BannerRenderer.renderPatterns(matrix, renderer, light, overlayLight, shieldModel.plate(), material, false, list);
            } else {
                shieldModel.renderToBuffer(matrix, buffer, light, overlayLight, 1, 1, 1, 1);
            }
            matrix.popPose();
            ci.cancel();
        }
    }
}
