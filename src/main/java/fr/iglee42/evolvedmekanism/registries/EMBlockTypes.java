package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTPort;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElement;
import mekanism.common.block.attribute.*;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import mekanism.common.registries.MekanismSounds;

public class EMBlockTypes {

    // APT Casing
    public static final BlockTypeTile<TileEntityAPTCasing> APT_CASING = BlockTileBuilder
            .createBlock(() -> EMTileEntityTypes.APT_CASING, EvolvedMekanismLang.DESCRIPTION_APT_CASING)
            .withGui(() -> EMContainerTypes.APT, EvolvedMekanismLang.APT)
            .withSound(MekanismSounds.SPS)
            .externalMultiblock()
            .build();
    // APT Port
    public static final BlockTypeTile<TileEntityAPTPort> APT_PORT = BlockTileBuilder
            .createBlock(() -> EMTileEntityTypes.APT_PORT, EvolvedMekanismLang.DESCRIPTION_APT_PORT)
            .withGui(() -> EMContainerTypes.APT, EvolvedMekanismLang.APT)
            .withSound(MekanismSounds.SPS)
            .with(Attributes.ACTIVE, Attributes.COMPARATOR)
            .externalMultiblock()
            .withComputerSupport("aptPort")
            .build();

    public static final BlockTypeTile<TileEntitySuperchargingElement> SUPERCHARGING_ELEMENT = BlockTileBuilder
            .createBlock(() -> EMTileEntityTypes.SUPERCHARGING_ELEMENT, EvolvedMekanismLang.DESCRIPTION_SUPERCHARGING_ELEMENT)
            .with(Attributes.ACTIVE_LIGHT)
            .internalMultiblock()
            .build();
}
