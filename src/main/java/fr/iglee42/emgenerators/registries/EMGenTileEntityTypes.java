package fr.iglee42.emgenerators.registries;

import fr.iglee42.emgenerators.tiers.AdvancedLunarPanelTier;
import fr.iglee42.emgenerators.tiers.AdvancedSolarPanelTier;
import fr.iglee42.emgenerators.tile.TileEntityLunarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedLunarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;

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

    public static final TileEntityTypeRegistryObject<TileEntityLunarGenerator> LUNAR_GENERATOR = TILE_ENTITY_TYPES.register(EMGenBlocks.LUNAR_GENERATOR,
            (pos, state) -> new TileEntityLunarGenerator(EMGenBlocks.LUNAR_GENERATOR, pos, state));
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedLunarGenerator> BASIC_ADVANCED_LUNAR_PANEL = registerTieredLunarPanel(EMGenBlocks.BASIC_ADVANCED_LUNAR_GENERATOR, AdvancedLunarPanelTier.BASIC);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedLunarGenerator> ADVANCED_LUNAR_PANEL = registerTieredLunarPanel(EMGenBlocks.ADVANCED_LUNAR_GENERATOR, AdvancedLunarPanelTier.ADVANCED);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedLunarGenerator> ELITE_LUNAR_PANEL = registerTieredLunarPanel(EMGenBlocks.ELITE_LUNAR_GENERATOR, AdvancedLunarPanelTier.ELITE);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedLunarGenerator> ULTIMATE_LUNAR_PANEL = registerTieredLunarPanel(EMGenBlocks.ULTIMATE_LUNAR_GENERATOR, AdvancedLunarPanelTier.ULTIMATE);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedLunarGenerator> OVERCLOCKED_LUNAR_PANEL = registerTieredLunarPanel(EMGenBlocks.OVERCLOCKED_LUNAR_GENERATOR, AdvancedLunarPanelTier.OVERCLOCKED);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedLunarGenerator> QUANTUM_LUNAR_PANEL = registerTieredLunarPanel(EMGenBlocks.QUANTUM_LUNAR_GENERATOR, AdvancedLunarPanelTier.QUANTUM);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedLunarGenerator> DENSE_LUNAR_PANEL = registerTieredLunarPanel(EMGenBlocks.DENSE_LUNAR_GENERATOR, AdvancedLunarPanelTier.DENSE);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedLunarGenerator> MULTIVERSAL_LUNAR_PANEL = registerTieredLunarPanel(EMGenBlocks.MULTIVERSAL_LUNAR_GENERATOR, AdvancedLunarPanelTier.MULTIVERSAL);
    public static final TileEntityTypeRegistryObject<TileEntityTieredAdvancedLunarGenerator> CREATIVE_LUNAR_PANEL = registerTieredLunarPanel(EMGenBlocks.CREATIVE_LUNAR_GENERATOR, AdvancedLunarPanelTier.CREATIVE);

    public static TileEntityTypeRegistryObject<TileEntityTieredAdvancedSolarGenerator> registerTieredSolarPanel(BlockRegistryObject<?,?> block, AdvancedSolarPanelTier tier) {
        return TILE_ENTITY_TYPES.register(block, (pos,state)->new TileEntityTieredAdvancedSolarGenerator(block,pos, state, tier));
    }

    public static TileEntityTypeRegistryObject<TileEntityTieredAdvancedLunarGenerator> registerTieredLunarPanel(BlockRegistryObject<?,?> block, AdvancedLunarPanelTier tier) {
        return TILE_ENTITY_TYPES.register(block, (pos,state)->new TileEntityTieredAdvancedLunarGenerator(block,pos, state, tier));
    }

    public static void register(IEventBus bus) {
        TILE_ENTITY_TYPES.register(bus);
    }
}
