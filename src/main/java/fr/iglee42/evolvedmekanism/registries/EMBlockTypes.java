package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTCasing;
import fr.iglee42.evolvedmekanism.multiblock.apt.TileEntityAPTPort;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElement;
import fr.iglee42.evolvedmekanism.tiles.TileEntitySuperchargingElementMk2;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityAlloyer;
import fr.iglee42.evolvedmekanism.tiles.machine.TileEntityChemixer;
import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.*;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.content.blocktype.Machine.MachineBuilder;
import mekanism.common.registries.MekanismSounds;

import java.util.EnumSet;

public class EMBlockTypes {

    public static final Machine.FactoryMachine<TileEntityAlloyer> ALLOYER = MachineBuilder
            .createFactoryMachine(() -> EMTileEntityTypes.ALLOYER, EvolvedMekanismLang.DESCRIPTION_ALLOYER, EMFactoryType.ALLOYING)
            .withGui(() -> EMContainerTypes.ALLOYER)
            .withSound(MekanismSounds.COMBINER)
            .withEnergyConfig(MekanismConfig.usage.combiner, MekanismConfig.storage.combiner)
            .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING))
            .build();

    public static final Machine<TileEntityChemixer> CHEMIXER = MachineBuilder
            .createMachine(() -> EMTileEntityTypes.CHEMIXER, MekanismLang.DESCRIPTION_PRESSURIZED_REACTION_CHAMBER)
            .withGui(() -> EMContainerTypes.CHEMIXER)
            .withSound(MekanismSounds.PRESSURIZED_REACTION_CHAMBER)
            .withEnergyConfig(MekanismConfig.usage.combiner, MekanismConfig.storage.combiner)
            .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING))
            .withComputerSupport("chemixer")
            .build();

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
    // Supercharging Element
    public static final BlockTypeTile<TileEntitySuperchargingElement> SUPERCHARGING_ELEMENT = BlockTileBuilder
            .createBlock(() -> EMTileEntityTypes.SUPERCHARGING_ELEMENT, EvolvedMekanismLang.DESCRIPTION_SUPERCHARGING_ELEMENT)
            .with(Attributes.ACTIVE_LIGHT)
            .internalMultiblock()
            .build();
    // Supercharging Element MK2
    public static final BlockTypeTile<TileEntitySuperchargingElementMk2> SUPERCHARGING_ELEMENT_MK2 = BlockTileBuilder
            .createBlock(() -> EMTileEntityTypes.SUPERCHARGING_ELEMENT_MK2, EvolvedMekanismLang.DESCRIPTION_SUPERCHARGING_ELEMENT_MK2)
            .with(Attributes.ACTIVE_LIGHT)
            .internalMultiblock()
            .build();
}
