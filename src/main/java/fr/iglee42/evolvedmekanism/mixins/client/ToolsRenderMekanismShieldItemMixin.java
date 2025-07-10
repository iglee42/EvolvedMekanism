package fr.iglee42.evolvedmekanism.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fr.iglee42.emtools.client.EMShieldTextures;
import fr.iglee42.emtools.registries.EMToolsItems;
import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import mekanism.tools.client.ShieldTextures;
import mekanism.tools.client.render.item.RenderMekanismShieldItem;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = RenderMekanismShieldItem.class,remap = false)
public class ToolsRenderMekanismShieldItemMixin {


    @Shadow private ShieldModel shieldModel;

    @Inject(method = "renderByItem", at = @At("HEAD"),cancellable = true)
    private void em$changeForEmShields(ItemStack stack, ItemDisplayContext displayContext, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, CallbackInfo ci){
        if (stack.is(EMToolsItems.PLASLITHERITE_SHIELD.asItem()) || stack.is(EMToolsItems.BETTER_GOLD_SHIELD.asItem())|| stack.is(EMToolsItems.REFINED_REDSTONE_SHIELD.asItem())){
            Item item = stack.getItem();
            ((InitializableEnum)(Object)ShieldTextures.OSMIUM).evolvedmekanism$initNewValues();
            ShieldTextures textures;
            if (item == EMToolsItems.PLASLITHERITE_SHIELD.asItem()) {
                textures = EMShieldTextures.PLASLITHERITE;
            } else if (item == EMToolsItems.BETTER_GOLD_SHIELD.asItem()) {
                textures = EMShieldTextures.BETTER_GOLD;
            } else if (item == EMToolsItems.REFINED_REDSTONE_SHIELD.asItem()) {
                textures = EMShieldTextures.REFINED_REDSTONE;
            } else {
                return;
            }
            Material material = textures.getBase();
            matrix.pushPose();
            matrix.scale(1, -1, -1);
            VertexConsumer buffer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(renderer, shieldModel.renderType(material.atlasLocation()), true, stack.hasFoil()));
            BannerPatternLayers bannerPattern = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
            DyeColor color = stack.get(DataComponents.BASE_COLOR);
            if (!bannerPattern.layers().isEmpty() || color != null) {
                shieldModel.handle().render(matrix, buffer, light, overlayLight, 0xFFFFFFFF);
                BannerRenderer.renderPatterns(matrix, renderer, light, overlayLight, shieldModel.plate(), material, false,
                        Objects.requireNonNullElse(color, DyeColor.WHITE), bannerPattern, stack.hasFoil());
            } else {
                shieldModel.renderToBuffer(matrix, buffer, light, overlayLight, 0xFFFFFFFF);
            }
            matrix.popPose();
            ci.cancel();
        }
    }
}
