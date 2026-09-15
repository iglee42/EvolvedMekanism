package fr.iglee42.evolvedmekanism.registries;

import com.mojang.serialization.Codec;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EMDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, EvolvedMekanism.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CURIO_ACTIVE =
            DATA_COMPONENTS.register("curio_active", () -> DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build());

    private EMDataComponents() {
    }

    public static boolean isActive(net.minecraft.world.item.ItemStack stack) {
        return stack.getOrDefault(CURIO_ACTIVE.get(), Boolean.TRUE);
    }

    public static boolean toggle(net.minecraft.world.item.ItemStack stack) {
        boolean next = !isActive(stack);
        stack.set(CURIO_ACTIVE.get(), next);
        return next;
    }
}
