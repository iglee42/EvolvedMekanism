package fr.iglee42.evolvedmekanism;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import fr.iglee42.emgenerators.registries.EMGenBlockTypes;
import fr.iglee42.emgenerators.registries.EMGenBlocks;
import fr.iglee42.emgenerators.registries.EMGenContainerTypes;
import fr.iglee42.emgenerators.registries.EMGenTileEntityTypes;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageManager;
import fr.iglee42.evolvedmekanism.multiblock.EMBuilders;
import fr.iglee42.evolvedmekanism.multiblock.apt.APTCache;
import fr.iglee42.evolvedmekanism.multiblock.apt.APTMultiblockData;
import fr.iglee42.evolvedmekanism.multiblock.apt.APTValidator;
import fr.iglee42.evolvedmekanism.network.EMPacketHandler;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMContainerTypes;
import fr.iglee42.evolvedmekanism.registries.EMCreativeTabs;
import fr.iglee42.evolvedmekanism.registries.EMInfuseTypes;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMLootFunctions;
import fr.iglee42.evolvedmekanism.registries.EMModules;
import fr.iglee42.evolvedmekanism.registries.EMRecipeSerializers;
import fr.iglee42.evolvedmekanism.registries.EMTileEntityTypes;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import mekanism.api.MekanismIMC;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.MekanismLang;
import mekanism.common.command.builders.BuildCommand;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.lib.Version;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tier.BinTier;
import mekanism.common.tier.CableTier;
import mekanism.common.tier.ChemicalTankTier;
import mekanism.common.tier.ConductorTier;
import mekanism.common.tier.EnergyCubeTier;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tier.FluidTankTier;
import mekanism.common.tier.InductionCellTier;
import mekanism.common.tier.InductionProviderTier;
import mekanism.common.tier.PipeTier;
import mekanism.common.tier.QIODriveTier;
import mekanism.common.tier.TransporterTier;
import mekanism.common.tier.TubeTier;
import mekanism.common.util.MekanismUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EvolvedMekanism.MODID)
public class EvolvedMekanism {

    public static final String MODID = "evolvedmekanism";
    public static final Logger logger = LogUtils.getLogger();
    public static final String MOD_NAME = "EvolvedMekanism";

    public static EvolvedMekanism instance;

    private final EMPacketHandler packetHandler;

    public final Version versionNumber;

    public static final MultiblockManager<APTMultiblockData> aptManager = new MultiblockManager<>("apt", APTCache::new,
            APTValidator::new);

    public EvolvedMekanism() {
        instance = this;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EMConfig.registerConfigs(FMLJavaModLoadingContext.get());

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueIMC);
        initEnums();

        EMBlocks.BLOCKS.register(modEventBus);
        EMItems.ITEMS.register(modEventBus);
        EMCreativeTabs.CREATIVE_TABS.register(modEventBus);
        EMTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        EMInfuseTypes.INFUSE_TYPES.register(modEventBus);
        EMContainerTypes.CONTAINER_TYPES.register(modEventBus);
        EMLootFunctions.REGISTER.register(modEventBus);
        EMRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        EMModules.MODULES.createAndRegister(modEventBus);

        registerCompats();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::serverStopped);

        versionNumber = new Version(ModLoadingContext.get().getActiveContainer());
        packetHandler = new EMPacketHandler();
    }

    private void registerCompats() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        if (ModsCompats.MEKANISMGENERATORS.isLoaded()) {
            EMGenBlocks.register(modEventBus);
            EMGenTileEntityTypes.register(modEventBus);
            EMGenContainerTypes.register(modEventBus);
            EMGenBlockTypes.register();
        }
    }

    private void serverStopped(ServerStoppedEvent event) {
        TieredPersonalStorageManager.reset();
    }

    public static EMPacketHandler packetHandler() {
        return instance.packetHandler;
    }

    private void initEnums() {
        MekanismLang ignoredLType = MekanismLang.MEKANISM;
        BaseTier ignored1 = BaseTier.BASIC;
        AlloyTier ignored2 = AlloyTier.ATOMIC;
        FactoryTier ignored3 = FactoryTier.BASIC;
        FactoryType ignoredFType = FactoryType.COMBINING;
        QIODriveTier ignoredQIO = QIODriveTier.BASE;

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
        event.enqueueWork(() -> {
            BuildCommand.register("apt", EvolvedMekanismLang.APT, new EMBuilders.APTBuilder());
        });
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation getResource(MekanismUtils.ResourceType type, String name) {
        return rl(type.getPrefix() + name);
    }

    public static boolean isEvolvedMekanismTier(BaseTier tier) {
        return tier.equals(EMBaseTier.OVERCLOCKED) || tier.equals(EMBaseTier.QUANTUM) || tier.equals(EMBaseTier.DENSE)
                || tier.equals(EMBaseTier.MULTIVERSAL) || tier.equals(BaseTier.CREATIVE);
    }

    public static ItemRegistryObject<Item> getCircuitByTier(BaseTier tier) {
        if (tier.equals(BaseTier.BASIC))
            return MekanismItems.BASIC_CONTROL_CIRCUIT;
        if (tier.equals(BaseTier.ADVANCED))
            return MekanismItems.ADVANCED_CONTROL_CIRCUIT;
        if (tier.equals(BaseTier.ELITE))
            return MekanismItems.ELITE_CONTROL_CIRCUIT;
        if (tier.equals(BaseTier.ULTIMATE))
            return MekanismItems.ULTIMATE_CONTROL_CIRCUIT;
        if (tier.equals(EMBaseTier.OVERCLOCKED))
            return EMItems.OVERCLOCKED_CONTROL_CIRCUIT;
        if (tier.equals(EMBaseTier.QUANTUM))
            return EMItems.QUANTUM_CONTROL_CIRCUIT;
        if (tier.equals(EMBaseTier.DENSE))
            return EMItems.DENSE_CONTROL_CIRCUIT;
        if (tier.equals(EMBaseTier.MULTIVERSAL))
            return EMItems.MULTIVERSAL_CONTROL_CIRCUIT;
        if (tier.equals(BaseTier.CREATIVE))
            return EMItems.CREATIVE_CONTROL_CIRCUIT;
        return null;
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        MekanismIMC.addMekaSuitBootsModules(EMModules.AIR_AFFINITY);
        MekanismIMC.addMekaSuitHelmetModules(EMModules.AQUA_AFFINITY);
        MekanismIMC.addMekaSuitPantsModules(EMModules.LUCK);
        MekanismIMC.addMekaToolModules(EMModules.CAPTURING);
    }

}
