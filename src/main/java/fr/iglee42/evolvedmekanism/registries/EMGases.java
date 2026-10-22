package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.api.chemical.gas.Gas;
import mekanism.common.registration.impl.GasDeferredRegister;
import mekanism.common.registration.impl.GasRegistryObject;

public class EMGases {

    public static final GasDeferredRegister GASES = new GasDeferredRegister(EvolvedMekanism.MODID);

    public static final GasRegistryObject<Gas> NITROGEN = GASES.register(EMChemicalConstants.NITROGEN);
    public static final GasRegistryObject<Gas> CRYONOCTIS = GASES.register(EMChemicalConstants.CRYONOCTIS);
}
