package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.registries.EMFactoryType;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.tier.FactoryTier;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockShapes.class,remap = false)
public class BlockShapesMixin {

    @Shadow @Final public static VoxelShape[] ENRICHING_FACTORY;

    @Inject(method = "getShape",at = @At("HEAD"),cancellable = true)
    private static void evolvedmekanism$getCustomShape(FactoryTier tier, FactoryType type, CallbackInfoReturnable<VoxelShape[]> cir){
        if (type.equals(EMFactoryType.ALLOYING)){
            cir.setReturnValue(ENRICHING_FACTORY);
        }
    }
}