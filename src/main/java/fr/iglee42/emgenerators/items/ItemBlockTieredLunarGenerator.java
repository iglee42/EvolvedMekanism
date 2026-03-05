package fr.iglee42.emgenerators.items;

import fr.iglee42.emgenerators.tiers.AdvancedLunarPanelTier;
import fr.iglee42.emgenerators.tiers.AdvancedSolarPanelTier;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedLunarGenerator;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.generators.common.content.blocktype.Generator;
import org.jetbrains.annotations.NotNull;

public class ItemBlockTieredLunarGenerator extends ItemBlockTooltip<BlockTile.BlockTileModel<TileEntityTieredAdvancedLunarGenerator, Generator<TileEntityTieredAdvancedLunarGenerator>>> {

    public ItemBlockTieredLunarGenerator(BlockTile.BlockTileModel<TileEntityTieredAdvancedLunarGenerator, Generator<TileEntityTieredAdvancedLunarGenerator>> block, Properties props) {
        super(block,props);
    }

    @NotNull
    @Override
    public AdvancedLunarPanelTier getTier() {
        return Attribute.getTier(getBlock(), AdvancedLunarPanelTier.class);
    }

}
