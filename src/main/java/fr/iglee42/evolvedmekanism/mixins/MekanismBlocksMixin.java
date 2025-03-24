package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.api.tier.ITier;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registries.MekanismBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;
import java.util.function.Supplier;

@Mixin(value = MekanismBlocks.class,remap = false)
public class MekanismBlocksMixin {


    @Inject(method = "registerTieredBlock(Lmekanism/api/tier/ITier;Ljava/lang/String;Ljava/util/function/Supplier;Ljava/util/function/Function;)Lmekanism/common/registration/impl/BlockRegistryObject;",at = @At(value = "RETURN",shift = At.Shift.BEFORE), cancellable = true)
    private static <BLOCK extends Block, ITEM extends BlockItem> void evolveddraconic$changeModid(ITier tier, String suffix, Supplier<? extends BLOCK> blockSupplier, Function<BLOCK, ITEM> itemCreator, CallbackInfoReturnable<BlockRegistryObject<BLOCK, ITEM>> cir){
        if (EvolvedMekanism.isEvolvedMekanismTier(tier.getBaseTier())){
            cir.setReturnValue(EMBlocks.BLOCKS.register(tier.getBaseTier().getLowerName() + suffix, blockSupplier, itemCreator));
        }
    }

}
