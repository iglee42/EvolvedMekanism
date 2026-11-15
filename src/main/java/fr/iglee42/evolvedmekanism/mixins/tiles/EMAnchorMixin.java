package fr.iglee42.evolvedmekanism.mixins.tiles;

import java.util.Collections;
import java.util.Set;

import fr.iglee42.emgenerators.tile.TileEntityLunarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredWindGenerator;
import fr.iglee42.evolvedmekanism.tiles.factory.TileEntityAlloyingFactory;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityAlloyer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityChemixer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityFluidAlloyer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityFluidCombiner;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityMelter;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntitySolidifier;
import mekanism.common.lib.chunkloading.IChunkLoader;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.TileComponentChunkLoader;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {
        TileEntityAlloyer.class,
        TileEntityFluidCombiner.class,
        TileEntityFluidAlloyer.class,
        TileEntityChemixer.class,
        TileEntityMelter.class,
        TileEntitySolidifier.class,
        TileEntityAlloyingFactory.class,
        TileEntityLunarGenerator.class,
        TileEntityTieredAdvancedSolarGenerator.class,
        TileEntityTieredWindGenerator.class
}, remap = false)
public class EMAnchorMixin implements IChunkLoader {

    @Unique
    private TileComponentChunkLoader<?> evolvedmekanism$chunkLoader;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void evolvedmekanism$initAnchor(CallbackInfo ci) {
        evolvedmekanism$chunkLoader = new TileComponentChunkLoader<>(evolvedmekanism$asChunkLoader());
    }

    @Unique
    @SuppressWarnings("unchecked")
    private <T extends TileEntityMekanism & IChunkLoader> T evolvedmekanism$asChunkLoader() {
        return (T) (Object) this;
    }

    @Override
    public TileComponentChunkLoader<?> getChunkLoader() {
        return evolvedmekanism$chunkLoader;
    }

    @Override
    public Set<ChunkPos> getChunkSet() {
        return Collections.singleton(new ChunkPos(((TileEntityMekanism) (Object) this).getBlockPos()));
    }
}
