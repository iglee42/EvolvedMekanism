package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.particles.ColoredMoltenOptions;
import fr.iglee42.evolvedmekanism.registries.MoltenFluidType;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Same pops and ambient loop as lava, with a flame particle tinted by the molten color.
 */
@Mixin(value = Fluid.class)
public class MoltenFluidAnimateMixin {

    @Inject(method = "animateTick", at = @At("HEAD"))
    private void evolvedmekanism$lavaAmbience(Level level, BlockPos pos, FluidState state, RandomSource random, CallbackInfo ci) {
        if (!(((Fluid) (Object) this).getFluidType() instanceof MoltenFluidType molten)) {
            return;
        }
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        if (!aboveState.isAir() || aboveState.isSolidRender(level, above)) {
            return;
        }
        if (random.nextInt(100) == 0) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + 1.0D;
            double z = pos.getZ() + random.nextDouble();
            level.addParticle(ColoredMoltenOptions.fromArgb(molten.color), x, y, z, 0.0D, 0.0D, 0.0D);
            level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
        }
        if (random.nextInt(200) == 0) {
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
        }
    }
}
