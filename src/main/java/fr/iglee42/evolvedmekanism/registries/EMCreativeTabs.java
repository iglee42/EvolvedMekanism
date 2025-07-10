package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.emgenerators.registries.EMGenBlocks;
import fr.iglee42.emgenerators.registries.EMGenItems;
import fr.iglee42.emtools.registries.EMToolsItems;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import mekanism.common.registration.MekanismDeferredHolder;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registries.MekanismCreativeTabs;
import net.minecraft.world.item.CreativeModeTab;

public class EMCreativeTabs {

    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(EvolvedMekanism.MODID);

    public static final MekanismDeferredHolder<CreativeModeTab,CreativeModeTab> EVOLVED_MEKANISM = CREATIVE_TABS.registerMain(EvolvedMekanismLang.MEKANISM_EVOLVED, EMItems.EXOVERSAL_ALLOY, builder ->
          builder.withSearchBar(70)//Allow our tabs to be searchable for convenience purposes
                  .backgroundTexture(EvolvedMekanism.rl("textures/gui/creative_tab.png"))
                  .withTabsBefore(MekanismCreativeTabs.MEKANISM.getKey())
                .displayItems((displayParameters, output) -> {
                    CreativeTabDeferredRegister.addToDisplay(EMItems.ITEMS, output);
                    if (ModsCompats.MEKANISMGENERATORS.isLoaded())CreativeTabDeferredRegister.addToDisplay(EMGenItems.ITEMS, output);
                    if (ModsCompats.MEKANISMTOOLS.isLoaded())CreativeTabDeferredRegister.addToDisplay(EMToolsItems.ITEMS, output);
                    CreativeTabDeferredRegister.addToDisplay(EMBlocks.BLOCKS, output);
                    if (ModsCompats.MEKANISMGENERATORS.isLoaded())CreativeTabDeferredRegister.addToDisplay(EMGenBlocks.BLOCKS, output);
                    CreativeTabDeferredRegister.addToDisplay(EMFluids.FLUIDS, output);
                    //addFilledTanks(output, true);
                })
    );

}