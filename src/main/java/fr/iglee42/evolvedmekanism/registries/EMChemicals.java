package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalBuilder;
import mekanism.common.registration.impl.ChemicalDeferredRegister;
import mekanism.common.registration.impl.DeferredChemical;
import net.minecraft.resources.ResourceLocation;

public class EMChemicals {

    public static final ChemicalDeferredRegister CHEMICALS = new ChemicalDeferredRegister(EvolvedMekanism.MODID);

    public static final DeferredChemical<Chemical> URANIUM = CHEMICALS.registerInfuse("uranium",0x9BE199);
    public static final DeferredChemical<Chemical> BETTER_GOLD = createInfuse("better_gold",EvolvedMekanism.rl("infuse_type/better_gold"),0xF9C918);
    public static final DeferredChemical<Chemical> PLASLITHERITE = createInfuse("plaslitherite",EvolvedMekanism.rl("infuse_type/plaslitherite"),0x666667);

    private static DeferredChemical<Chemical> createInfuse(String id, ResourceLocation texture, int color){
        return CHEMICALS.register(id,() -> new Chemical(ChemicalBuilder.builder(texture).tint(color)));
    }
}
