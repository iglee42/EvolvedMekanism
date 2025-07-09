package fr.iglee42.evolvedmekanism.mixins.tiles;

import fr.iglee42.evolvedmekanism.utils.EMDataHandlerUtils;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = TileEntityMekanism.class)
public class TileEntityMekanismMixin {

    @Redirect(method = "load",at = @At(value = "INVOKE", target = "Lmekanism/api/DataHandlerUtils;readContainers(Ljava/util/List;Lnet/minecraft/nbt/ListTag;)V"))
    private void em$loadInventoryWithIntSlots(List<? extends INBTSerializable<CompoundTag>> containers, ListTag storedContainers){
        EMDataHandlerUtils.readContainers(containers,storedContainers);
    }

    @Redirect(method = "saveAdditional",at = @At(value = "INVOKE", target = "Lmekanism/api/DataHandlerUtils;writeContainers(Ljava/util/List;)Lnet/minecraft/nbt/ListTag;"))
    private ListTag em$writeInventoryWithIntSlots(List<? extends INBTSerializable<CompoundTag>> containers){
        return EMDataHandlerUtils.writeContainers(containers);
    }

}
