package fr.iglee42.emgenerators.registries;

import fr.iglee42.emgenerators.tiers.AdvancedSolarPanelTier;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMTileEntityTypes;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;
import net.neoforged.bus.api.IEventBus;

public class EMGenTileEntityTypes {

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(EvolvedMekanism.MODID);

    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedSolarGenerator> ADVANCED_SOLAR_PANEL = registerTieredSolarPanel(EMGenBlocks.ADVANCED_SOLAR_GENERATOR, AdvancedSolarPanelTier.ADVANCED);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedSolarGenerator> ELITE_SOLAR_PANEL = registerTieredSolarPanel(EMGenBlocks.ELITE_SOLAR_GENERATOR, AdvancedSolarPanelTier.ELITE);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedSolarGenerator> ULTIMATE_SOLAR_PANEL = registerTieredSolarPanel(EMGenBlocks.ULTIMATE_SOLAR_GENERATOR, AdvancedSolarPanelTier.ULTIMATE);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedSolarGenerator> OVERCLOCKED_SOLAR_PANEL = registerTieredSolarPanel(EMGenBlocks.OVERCLOCKED_SOLAR_GENERATOR, AdvancedSolarPanelTier.OVERCLOCKED);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedSolarGenerator> QUANTUM_SOLAR_PANEL = registerTieredSolarPanel(EMGenBlocks.QUANTUM_SOLAR_GENERATOR, AdvancedSolarPanelTier.QUANTUM);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedSolarGenerator> DENSE_SOLAR_PANEL = registerTieredSolarPanel(EMGenBlocks.DENSE_SOLAR_GENERATOR, AdvancedSolarPanelTier.DENSE);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedSolarGenerator> MULTIVERSAL_SOLAR_PANEL = registerTieredSolarPanel(EMGenBlocks.MULTIVERSAL_SOLAR_GENERATOR, AdvancedSolarPanelTier.MULTIVERSAL);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedSolarGenerator> CREATIVE_SOLAR_PANEL = registerTieredSolarPanel(EMGenBlocks.CREATIVE_SOLAR_GENERATOR, AdvancedSolarPanelTier.CREATIVE);

    public static TileEntityTypeRegistryObject<TileEntityTieredAdvancedSolarGenerator> registerTieredSolarPanel(BlockRegistryObject<?,?> block, AdvancedSolarPanelTier tier) {
        return TILE_ENTITY_TYPES.mekBuilder(block, (pos,state)->new TileEntityTieredAdvancedSolarGenerator(block,pos, state, tier))
                .clientTicker(TileEntityMekanism::tickClient)
                .serverTicker(TileEntityMekanism::tickServer)
                .withSimple(Capabilities.CONFIG_CARD)
                .withSimple(Capabilities.EVAPORATION_SOLAR)
                .build();
    }

    public static void register(IEventBus bus) {
        TILE_ENTITY_TYPES.register(bus);
    }
}
