package fr.iglee42.evolvedmekanism.mixins.tiles;

import fr.iglee42.emgenerators.tile.TileEntityLunarGenerator;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = TileEntitySolarGenerator.class)
public interface TileEntitySolarGeneratorAccessor {

    @Accessor("lastProductionAmount")
    void setLastProductionAmount(long lastProductionAmount);

}
