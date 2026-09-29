package fr.iglee42.evolvedmekanism.mixins.tiles;

import fr.iglee42.emgenerators.tile.TileEntityTieredWindGenerator;
import fr.iglee42.emgenerators.tile.WindGeneratorBlockOverride;
import mekanism.api.math.FloatingLong;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mekanism.generators.common.tile.TileEntityWindGenerator;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TileEntityWindGenerator.class, remap = false)
public class TileEntityWindGeneratorMixin {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lmekanism/generators/common/registries/GeneratorsBlocks;WIND_GENERATOR:Lmekanism/common/registration/impl/BlockRegistryObject;"))
    private static BlockRegistryObject evolvedmekanism$block() {
        BlockRegistryObject override = WindGeneratorBlockOverride.current();
        return override != null ? override : GeneratorsBlocks.WIND_GENERATOR;
    }

    @Inject(method = "getProductionRate", at = @At("RETURN"), cancellable = true)
    private void evolvedmekanism$tierProduction(CallbackInfoReturnable<FloatingLong> cir) {
        if ((Object) this instanceof TileEntityTieredWindGenerator tile) {
            cir.setReturnValue(cir.getReturnValue().multiply(tile.getTier().getMultiplier()));
        }
    }

    @Redirect(method = "onUpdateServer", at = @At(value = "INVOKE", target = "Lmekanism/api/math/FloatingLong;multiply(Lmekanism/api/math/FloatingLong;)Lmekanism/api/math/FloatingLong;"))
    private FloatingLong evolvedmekanism$tierInsert(FloatingLong value, FloatingLong multiplier) {
        FloatingLong result = value.multiply(multiplier);
        if ((Object) this instanceof TileEntityTieredWindGenerator tile) {
            return result.multiply(tile.getTier().getMultiplier());
        }
        return result;
    }
}
