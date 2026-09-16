package fr.iglee42.evolvedmekanism.mixins.curios;

import fr.iglee42.evolvedmekanism.curios.CuriosHelper;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = HumanoidArmorLayer.class, remap = false)
public class HumanoidArmorLayerMixin {

    @Redirect(
            method = "renderArmorPiece(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;")
    )
    private ItemStack evolvedmekanism$replaceWithHazmat(LivingEntity entity, EquipmentSlot slot) {
        if (CuriosHelper.shouldReplaceArmorRender(entity)) {
            ItemStack replacement = CuriosHelper.getHazmatArmorPiece(slot);
            if (!replacement.isEmpty()) {
                return replacement;
            }
        }
        return entity.getItemBySlot(slot);
    }
}
