package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.tiers.PersonalStorageTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.MekanismLang;
import mekanism.common.block.BlockPersonalStorage;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.attribute.AttributeUpgradeable;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.registries.MekanismContainerTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(value = BlockType.BlockTypeBuilder.class,remap = false)
public class BlockTypeBuilderMixin<BLOCK extends BlockType, T extends BlockType.BlockTypeBuilder<BLOCK, T>> {

    @Shadow @Final protected BLOCK holder;

    @Inject(method = "with",at = @At("HEAD"))
    private void evolvedmekanism$addUpgradesToBarrels(Attribute[] attrs, CallbackInfoReturnable<T> cir){
        if (Arrays.asList(attrs).contains(BlockPersonalStorage.PERSONAL_STORAGE_INVENTORY)){
            if (holder.getDescription().equals(MekanismLang.DESCRIPTION_PERSONAL_CHEST)){
                holder.add(new AttributeTier<>(PersonalStorageTier.BASIC), new AttributeUpgradeable(()-> EMBlocks.ADVANCED_PERSONAL_CHEST));
            } else {
                holder.add(new AttributeTier<>(PersonalStorageTier.BASIC), new AttributeUpgradeable(()-> EMBlocks.ADVANCED_PERSONAL_BARREL));
            }
        }
    }

}
