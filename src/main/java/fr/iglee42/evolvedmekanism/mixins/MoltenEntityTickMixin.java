package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.registries.MoltenFluidType;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Runs after the entity tick, once vanilla has finished updating the fire flag.
 */
@Mixin(value = Entity.class, remap = false)
public class MoltenEntityTickMixin {

    @Inject(method = "tick", at = @At("RETURN"))
    private void evolvedmekanism$moltenFire(CallbackInfo ci) {
        MoltenFluidType.afterEntityTick((Entity) (Object) this);
    }
}
