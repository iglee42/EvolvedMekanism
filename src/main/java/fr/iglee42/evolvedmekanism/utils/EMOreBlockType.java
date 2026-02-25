package fr.iglee42.evolvedmekanism.utils;

import fr.iglee42.evolvedmekanism.blocks.EMBlockOre;
import mekanism.common.block.BlockOre;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockRegistryObject;

public record EMOreBlockType(BlockRegistryObject<EMBlockOre, ItemBlockTooltip<EMBlockOre>> stone,
                             BlockRegistryObject<EMBlockOre, ItemBlockTooltip<EMBlockOre>> deepslate) {
}