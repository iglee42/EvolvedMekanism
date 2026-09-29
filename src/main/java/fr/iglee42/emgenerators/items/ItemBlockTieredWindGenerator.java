package fr.iglee42.emgenerators.items;

import java.util.function.Consumer;

import fr.iglee42.emgenerators.client.RenderTieredWindGeneratorItem;
import fr.iglee42.emgenerators.tiers.AdvancedWindGeneratorTier;
import fr.iglee42.emgenerators.tile.TileEntityTieredWindGenerator;
import mekanism.client.render.RenderPropertiesProvider.MekRenderProperties;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.generators.common.content.blocktype.Generator;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class ItemBlockTieredWindGenerator extends ItemBlockTooltip<BlockTile.BlockTileModel<TileEntityTieredWindGenerator, Generator<TileEntityTieredWindGenerator>>> {

    public ItemBlockTieredWindGenerator(BlockTile.BlockTileModel<TileEntityTieredWindGenerator, Generator<TileEntityTieredWindGenerator>> block) {
        super(block);
    }

    @NotNull
    @Override
    public AdvancedWindGeneratorTier getTier() {
        return Attribute.getTier(getBlock(), AdvancedWindGeneratorTier.class);
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new MekRenderProperties(RenderTieredWindGeneratorItem.RENDERER));
    }
}
