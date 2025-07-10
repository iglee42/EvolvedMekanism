package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.registries.EMFactoryType;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeFactoryType;
import mekanism.common.block.prefab.BlockFactoryMachine;
import mekanism.common.item.block.machine.ItemBlockFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemBlockFactory.class,remap = false)
public class ItemBlockFactoryMixin {

    @Inject(method = "getSideConfig",at = @At("HEAD"),cancellable = true)
    private static void em$addOthers(BlockFactoryMachine.BlockFactory<?> block, CallbackInfoReturnable<AttachedSideConfig> cir){
        if (Attribute.getOrThrow(block.builtInRegistryHolder(), AttributeFactoryType.class).getFactoryType().equals(EMFactoryType.ALLOYING)){
            cir.setReturnValue(AttachedSideConfig.EXTRA_MACHINE);
            return;
        }
    }
}
