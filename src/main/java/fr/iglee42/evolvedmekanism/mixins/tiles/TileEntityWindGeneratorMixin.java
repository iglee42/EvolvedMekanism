package fr.iglee42.evolvedmekanism.mixins.tiles;

import fr.iglee42.emgenerators.tile.WindGeneratorBlockOverride;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mekanism.generators.common.tile.TileEntityWindGenerator;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityWindGenerator.class, remap = false)
public class TileEntityWindGeneratorMixin {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lmekanism/generators/common/registries/GeneratorsBlocks;WIND_GENERATOR:Lmekanism/common/registration/impl/BlockRegistryObject;"))
    private static BlockRegistryObject evolvedmekanism$block() {
        BlockRegistryObject override = WindGeneratorBlockOverride.current();
        return override != null ? override : GeneratorsBlocks.WIND_GENERATOR;
    }
}
