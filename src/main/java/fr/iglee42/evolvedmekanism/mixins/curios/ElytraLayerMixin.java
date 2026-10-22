package fr.iglee42.evolvedmekanism.mixins.curios;

import fr.iglee42.evolvedmekanism.curios.CuriosHelper;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ElytraLayer.class, remap = false)
public class ElytraLayerMixin {

    @Redirect(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;")
    )
    private ItemStack evolvedmekanism$renderFromCurios(LivingEntity entity, EquipmentSlot slot) {
        ItemStack chest = entity.getItemBySlot(slot);
        if (slot == EquipmentSlot.CHEST && !chest.canElytraFly(entity)) {
            ItemStack curios = CuriosHelper.findElytra(entity);
            if (!curios.isEmpty()) {
                return curios;
            }
        }
        return chest;
    }
}
