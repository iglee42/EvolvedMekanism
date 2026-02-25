package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.world.height.EMConfigurableHeightProvider;
import mekanism.common.Mekanism;
import mekanism.common.world.height.ConfigurableHeightProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EMHeightProviderTypes {

    private EMHeightProviderTypes() {
    }

    public static final DeferredRegister<HeightProviderType<?>> HEIGHT_PROVIDER_TYPES = DeferredRegister.create(Registries.HEIGHT_PROVIDER_TYPE, EvolvedMekanism.MODID);

    public static final DeferredHolder<HeightProviderType<?>, HeightProviderType<EMConfigurableHeightProvider>> CONFIGURABLE = HEIGHT_PROVIDER_TYPES.register("configurable", () -> () -> EMConfigurableHeightProvider.CODEC);
}