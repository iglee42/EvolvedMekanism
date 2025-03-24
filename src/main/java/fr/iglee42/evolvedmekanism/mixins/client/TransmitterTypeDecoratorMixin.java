package fr.iglee42.evolvedmekanism.mixins.client;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.api.providers.IBlockProvider;
import mekanism.client.render.item.TransmitterTypeDecorator;
import mekanism.common.util.MekanismUtils;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TransmitterTypeDecorator.class)
public class TransmitterTypeDecoratorMixin {

    @Mutable
    @Shadow @Final private ResourceLocation texture;

    @Inject(method = "<init>",at =@At("TAIL"))
    private void evolvedmekanism$changeRlToEM(IBlockProvider block, CallbackInfo ci){
         if (block.getRegistryName().getNamespace().equals(EvolvedMekanism.MODID)){
             this.texture = EvolvedMekanism.rl(MekanismUtils.ResourceType.GUI_ICONS.getPrefix() + block.getRegistryName().getPath() + ".png");
         }
     }

}
