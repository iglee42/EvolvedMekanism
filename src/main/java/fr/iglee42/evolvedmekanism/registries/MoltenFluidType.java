package fr.iglee42.evolvedmekanism.registries;

import java.util.function.Consumer;

import fr.iglee42.evolvedmekanism.client.MoltenFluidClient;
import mekanism.common.registration.impl.FluidDeferredRegister;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

/**
 * Molten metals use lava movement and damage. Mekanism's default fluid type follows water,
 * which pins entities at the end of a current and never burns them.
 */
public class MoltenFluidType extends FluidDeferredRegister.MekanismFluidType {

    public final int color;

    public MoltenFluidType(FluidType.Properties properties, FluidDeferredRegister.FluidTypeRenderProperties renderProperties, int color) {
        super(properties, renderProperties);
        this.color = color;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new MoltenFluidClient(this));
    }

    @Override
    public double motionScale(Entity entity) {
        return entity.level().dimensionType().ultraWarm() ? 0.007D : 0.0023333333333333335D;
    }

    @Override
    public void setItemMovement(ItemEntity entity) {
        Vec3 motion = entity.getDeltaMovement();
        entity.setDeltaMovement(motion.x * 0.95F, motion.y + (motion.y < 0.06F ? 5.0E-4F : 0.0F), motion.z * 0.95F);
    }

    @Override
    public boolean move(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity) {
        boolean falling = entity.getDeltaMovement().y <= 0.0D;
        double y = entity.getY();
        entity.moveRelative(0.02F, movementVector);
        entity.move(MoverType.SELF, entity.getDeltaMovement());
        if (entity.getFluidTypeHeight(this) <= entity.getFluidJumpThreshold()) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5D, 0.8D, 0.5D));
            entity.setDeltaMovement(entity.getFluidFallingAdjustedMovement(gravity, falling, entity.getDeltaMovement()));
        } else {
            entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5D));
        }
        if (gravity != 0.0D) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, -gravity / 4.0D, 0.0D));
        }
        Vec3 motion = entity.getDeltaMovement();
        if (entity.horizontalCollision && entity.isFree(motion.x, motion.y + 0.6D - entity.getY() + y, motion.z)) {
            entity.setDeltaMovement(motion.x, 0.3D, motion.z);
        }
        return true;
    }

    public static void afterEntityTick(Entity entity) {
        if (!entity.isAlive() || entity.fireImmune() || entity.isSpectator()) {
            return;
        }
        if (!entity.isInFluidType((type, height) -> height > 0.0D && type instanceof MoltenFluidType)) {
            return;
        }
        // Creative caps fire ticks at 1, and the entity tick clears the flame flag before this runs.
        if (entity instanceof Player player && player.getAbilities().invulnerable) {
            player.setRemainingFireTicks(1);
        } else {
            entity.lavaHurt();
            // 300 is divisible by 20, and molten is not vanilla lava, so the fire tick would add damage every tick.
            entity.setRemainingFireTicks(299);
        }
        entity.setSharedFlagOnFire(true);
    }
}
