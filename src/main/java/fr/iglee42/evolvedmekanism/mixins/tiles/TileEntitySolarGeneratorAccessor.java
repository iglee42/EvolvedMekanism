package fr.iglee42.evolvedmekanism.mixins.tiles;

import mekanism.api.math.FloatingLong;
import mekanism.generators.common.tile.TileEntitySolarGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = TileEntitySolarGenerator.class, remap = false)
public interface TileEntitySolarGeneratorAccessor {

    @Accessor("lastProductionAmount")
    void setLastProductionAmount(FloatingLong value);
}
