package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.world.EMConfigurableConstantInt;
import mekanism.common.Mekanism;
import mekanism.common.world.ConfigurableConstantInt;
import mekanism.common.world.ConfigurableUniformInt;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.IntProviderType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EMIntProviderTypes {

    private EMIntProviderTypes() {
    }

    public static final DeferredRegister<IntProviderType<?>> INT_PROVIDER_TYPES = DeferredRegister.create(Registries.INT_PROVIDER_TYPE, EvolvedMekanism.MODID);

    public static final DeferredHolder<IntProviderType<?>, IntProviderType<EMConfigurableConstantInt>> CONFIGURABLE_CONSTANT = INT_PROVIDER_TYPES.register("configurable_constant", () -> () -> EMConfigurableConstantInt.CODEC);
}