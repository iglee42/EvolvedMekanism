package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;
import mekanism.common.registries.MekanismCreativeTabs;

public class EMCreativeTabs {

    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(EvolvedMekanism.MODID);

    public static final CreativeTabRegistryObject EVOLVED_MEKANISM = CREATIVE_TABS.registerMain(EvolvedMekanismLang.MEKANISM_EVOLVED, EMItems.EXOVERSAL_ALLOY, builder ->
          builder.withSearchBar(70)//Allow our tabs to be searchable for convenience purposes
                  .withBackgroundLocation(EvolvedMekanism.rl("textures/gui/creative_tab.png"))
                  .withTabsBefore(MekanismCreativeTabs.MEKANISM.key())
                .displayItems((displayParameters, output) -> {
                    CreativeTabDeferredRegister.addToDisplay(EMItems.ITEMS, output);
                    CreativeTabDeferredRegister.addToDisplay(EMBlocks.BLOCKS, output);
                })
    );

}