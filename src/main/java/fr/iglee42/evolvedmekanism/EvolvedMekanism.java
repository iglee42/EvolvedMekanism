package fr.iglee42.evolvedmekanism;

import com.mojang.logging.LogUtils;
import fr.iglee42.emgenerators.client.EMGenClientRegistration;
import fr.iglee42.emgenerators.registries.*;
import fr.iglee42.emtools.client.EMToolsClientRegistration;
import fr.iglee42.emtools.config.EvolvedMekanismToolsConfig;
import fr.iglee42.emtools.registries.EMToolsArmorMaterials;
import fr.iglee42.emtools.registries.EMToolsItems;
import fr.iglee42.emtools.registries.EMToolsTags;
import fr.iglee42.emtools.utils.EMMobEquipmentHelper;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.interfaces.InitializableEnum;
import fr.iglee42.evolvedmekanism.inventory.personalstorage.TieredPersonalStorageManager;
import fr.iglee42.evolvedmekanism.multiblock.EMBuilders;
import fr.iglee42.evolvedmekanism.multiblock.apt.APTCache;
import fr.iglee42.evolvedmekanism.multiblock.apt.APTMultiblockData;
import fr.iglee42.evolvedmekanism.multiblock.apt.APTValidator;
import fr.iglee42.evolvedmekanism.network.EMPacketHandler;
import fr.iglee42.evolvedmekanism.registries.*;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import mekanism.api.MekanismIMC;
import mekanism.api.text.EnumColor;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.MekanismLang;
import mekanism.common.command.builders.BuildCommand;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.lib.Version;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tier.*;
import mekanism.common.util.MekanismUtils;
import mekanism.tools.client.ShieldTextures;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.slf4j.Logger;

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

    public EvolvedMekanism(IEventBus modEventBus, ModContainer container) {
        logger.info("Evolved Mekanism Launched");
        instance = this;
        EMConfig.registerConfigs(container);
        if (ModsCompats.MEKANISMTOOLS.isLoaded()) EvolvedMekanismToolsConfig.registerConfigs(container);

        initEnums();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueIMC);

        EMBlocks.BLOCKS.register(modEventBus);
        EMItems.ITEMS.register(modEventBus);
        EMCreativeTabs.CREATIVE_TABS.register(modEventBus);
        EMTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        EMChemicals.CHEMICALS.register(modEventBus);
        EMContainerTypes.CONTAINER_TYPES.register(modEventBus);
        EMLootFunctions.REGISTER.register(modEventBus);
        EMRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        EMModules.MODULES.register(modEventBus);
        EMFluids.FLUIDS.register(modEventBus);
        if (ModsCompats.MEKANISMTOOLS.isLoaded())EMToolsArmorMaterials.ARMOR_MATERIALS.register(modEventBus);

        registerCompats(modEventBus);


        //NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.addListener(this::serverStopped);

        modEventBus.addListener(EMConfig::onConfigLoad);
        if (ModsCompats.MEKANISMTOOLS.isLoaded())modEventBus.addListener(EvolvedMekanismToolsConfig::onConfigLoad);


        if (ModsCompats.MEKANISMTOOLS.isLoaded()) NeoForge.EVENT_BUS.addListener(EMMobEquipmentHelper::onLivingSpecialSpawn);

        versionNumber = new Version(container);
        packetHandler = new EMPacketHandler(modEventBus);
    }

    private void registerCompats(IEventBus modEventBus) {

        if (ModsCompats.MEKANISMGENERATORS.isLoaded()) {
           EMGenItems.register(modEventBus);
           EMGenBlocks.register(modEventBus);
           EMGenTileEntityTypes.register(modEventBus);
           EMGenContainerTypes.register(modEventBus);
           EMGenBlockTypes.register();
           modEventBus.register(new EMGenClientRegistration());
        }

        if (ModsCompats.MEKANISMTOOLS.isLoaded()) {
            EMToolsItems.register(modEventBus);
            modEventBus.register(new EMToolsClientRegistration());
        }
    }

    private void serverStopped(ServerStoppedEvent event) {
        TieredPersonalStorageManager.reset();
    }

    public static EMPacketHandler packetHandler() {
        return instance.packetHandler;
    }

    private void initEnums() {
        logger.info("Initializing Enum Injections");
        ((InitializableEnum)(Object)BaseTier.BASIC).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)AlloyTier.INFUSED).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)FactoryTier.BASIC).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)QIODriveTier.BASE).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)FactoryType.COMBINING).evolvedmekanism$initNewValues();

        ((InitializableEnum)(Object)CableTier.BASIC).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)ConductorTier.BASIC).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)PipeTier.BASIC).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)TubeTier.BASIC).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)TransporterTier.BASIC).evolvedmekanism$initNewValues();

        ((InitializableEnum)(Object)BinTier.BASIC).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)InductionCellTier.BASIC).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)InductionProviderTier.BASIC).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)EnergyCubeTier.BASIC).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)ChemicalTankTier.BASIC).evolvedmekanism$initNewValues();
        ((InitializableEnum)(Object)FluidTankTier.BASIC).evolvedmekanism$initNewValues();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        initEnums();
        event.enqueueWork(() -> {
            EMTags.init();
            if (ModsCompats.MEKANISMTOOLS.isLoaded()) EMToolsTags.init();
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

    public static Component logFormat(Object message) {
        return logFormat(EnumColor.GRAY,message);
    }

    public static Component logFormat(EnumColor messageColor, Object message) {
        return MekanismLang.LOG_FORMAT.translateColored(EnumColor.DARK_GREEN, EvolvedMekanismLang.MEKANISM_EVOLVED, messageColor, message);
    }
}
