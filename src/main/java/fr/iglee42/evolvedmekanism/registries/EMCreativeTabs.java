package fr.iglee42.evolvedmekanism.registries;

import java.util.function.BooleanSupplier;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.providers.IItemProvider;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.Attributes.AttributeComparator;
import mekanism.common.block.prefab.BlockBase;
import mekanism.common.block.transmitter.BlockMechanicalPipe;
import mekanism.common.block.transmitter.BlockSmallTransmitter;
import mekanism.common.block.transmitter.BlockTransmitter;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;
import mekanism.common.registries.MekanismCreativeTabs;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ore.OreBlockType;
import mekanism.common.tier.ChemicalTankTier;
import mekanism.common.tier.FluidTankTier;
import mekanism.common.util.ChemicalUtil;
import mekanism.common.util.EnumUtils;
import mekanism.common.util.FluidUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class EMCreativeTabs {

    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(EvolvedMekanism.MODID);

    public static final CreativeTabRegistryObject EVOLVED_MEKANISM = CREATIVE_TABS.registerMain(EvolvedMekanismLang.MEKANISM_EVOLVED, EMItems.EXOVERSAL_ALLOY, builder ->
          builder.withSearchBar(70)//Allow our tabs to be searchable for convenience purposes
                  .withBackgroundLocation(EvolvedMekanism.rl("textures/gui/creative_tab.png"))
                  .withTabsBefore(MekanismCreativeTabs.MEKANISM.key())
                .displayItems((displayParameters, output) -> {
                    CreativeTabDeferredRegister.addToDisplay(EMItems.ITEMS, output);
                    CreativeTabDeferredRegister.addToDisplay(EMBlocks.BLOCKS, output);
                    //CreativeTabDeferredRegister.addToDisplay(MekanismFluids.FLUIDS, output);
                    //addFilledTanks(output, true);
                })
    );

}