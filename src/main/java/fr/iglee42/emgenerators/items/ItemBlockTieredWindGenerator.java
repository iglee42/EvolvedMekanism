package fr.iglee42.emgenerators.items;

import fr.iglee42.emgenerators.tiers.AdvancedWindGeneratorTier;
import fr.iglee42.emgenerators.tile.TileEntityTieredWindGenerator;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.generators.common.content.blocktype.Generator;
import org.jetbrains.annotations.NotNull;

public class ItemBlockTieredWindGenerator extends ItemBlockTooltip<BlockTile.BlockTileModel<TileEntityTieredWindGenerator, Generator<TileEntityTieredWindGenerator>>> {

    public ItemBlockTieredWindGenerator(BlockTile.BlockTileModel<TileEntityTieredWindGenerator, Generator<TileEntityTieredWindGenerator>> block, Properties props) {
        super(block, props);
    }

    @NotNull
    @Override
    public AdvancedWindGeneratorTier getTier() {
        return Attribute.getTier(getBlock(), AdvancedWindGeneratorTier.class);
    }
}
