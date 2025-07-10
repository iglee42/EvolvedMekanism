package fr.iglee42.emgenerators.items;

import fr.iglee42.emgenerators.tiers.AdvancedSolarPanelTier;
import fr.iglee42.emgenerators.tile.TileEntityTieredAdvancedSolarGenerator;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.generators.common.content.blocktype.Generator;
import org.jetbrains.annotations.NotNull;

public class ItemBlockTieredSolarGenerator extends ItemBlockTooltip<BlockTile.BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>>> {

    public ItemBlockTieredSolarGenerator(BlockTile.BlockTileModel<TileEntityTieredAdvancedSolarGenerator, Generator<TileEntityTieredAdvancedSolarGenerator>> block,Properties props) {
        super(block,props);
    }

    @NotNull
    @Override
    public AdvancedSolarPanelTier getTier() {
        return Attribute.getTier(getBlock(), AdvancedSolarPanelTier.class);
    }

}
