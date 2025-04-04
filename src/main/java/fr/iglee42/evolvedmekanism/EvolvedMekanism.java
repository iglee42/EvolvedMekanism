package fr.iglee42.evolvedmekanism;

import com.mojang.logging.LogUtils;
import fr.iglee42.evolvedmekanism.network.EMPacketHandler;
import fr.iglee42.evolvedmekanism.registries.*;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.Mekanism;
import mekanism.common.lib.Version;
import mekanism.common.network.PacketHandler;
import mekanism.common.tier.*;
import mekanism.common.util.MekanismUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(EvolvedMekanism.MODID)
public class EvolvedMekanism {

    public static final String MODID = "evolvedmekanism";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static EvolvedMekanism instance;

    private final EMPacketHandler packetHandler;

    public final Version versionNumber;


    public EvolvedMekanism() {
        instance = this;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        initEnums();

        EMBlocks.BLOCKS.register(modEventBus);
        EMItems.ITEMS.register(modEventBus);
        EMCreativeTabs.CREATIVE_TABS.register(modEventBus);
        EMBlockEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        EMInfuseTypes.INFUSE_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);


        versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
        packetHandler = new EMPacketHandler();
    }


    public static EMPacketHandler packetHandler() {
        return instance.packetHandler;
    }


    private void initEnums(){
        BaseTier ignored1 = BaseTier.BASIC;
        AlloyTier ignored2 = AlloyTier.ATOMIC;
        FactoryTier ignored3 = FactoryTier.BASIC;

        CableTier cable1 = CableTier.BASIC;
        ConductorTier cable2 = ConductorTier.BASIC;
        PipeTier cable3 = PipeTier.BASIC;
        TubeTier cable4 = TubeTier.BASIC;
        TransporterTier cable5 = TransporterTier.BASIC;

        BinTier storage1 = BinTier.BASIC;
        InductionCellTier storage2 = InductionCellTier.BASIC;
        InductionProviderTier storage3 = InductionProviderTier.BASIC;
        EnergyCubeTier storage4 = EnergyCubeTier.BASIC;
        ChemicalTankTier storage5 = ChemicalTankTier.BASIC;
        FluidTankTier storage6 = FluidTankTier.BASIC;
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        packetHandler.initialize();
    }

    public static ResourceLocation rl(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,path);
    }

    public static ResourceLocation getResource(MekanismUtils.ResourceType type, String name) {
        return rl(type.getPrefix() + name);
    }

    public static boolean isEvolvedMekanismTier(BaseTier tier){
        return tier.equals(EMBaseTier.OVERCLOCKED) || tier.equals(EMBaseTier.QUANTUM) || tier.equals(EMBaseTier.DENSE) || tier.equals(EMBaseTier.MULTIVERSAL);
    }

}
