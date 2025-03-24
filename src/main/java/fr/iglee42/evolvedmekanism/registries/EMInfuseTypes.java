package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.common.registration.impl.InfuseTypeDeferredRegister;
import mekanism.common.registration.impl.InfuseTypeRegistryObject;

public class EMInfuseTypes {

    public static final InfuseTypeDeferredRegister INFUSE_TYPES = new InfuseTypeDeferredRegister(EvolvedMekanism.MODID);

    public static final InfuseTypeRegistryObject<InfuseType> URANIUM = INFUSE_TYPES.register("uranium",0x9BE199);
    public static final InfuseTypeRegistryObject<InfuseType> BETTER_GOLD = INFUSE_TYPES.register("better_gold",EvolvedMekanism.rl("infuse_type/better_gold"),0xF9C918);
}
