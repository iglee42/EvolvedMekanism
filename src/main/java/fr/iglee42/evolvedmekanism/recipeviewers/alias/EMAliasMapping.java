package fr.iglee42.evolvedmekanism.recipeviewers.alias;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMFactoryType;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import fr.iglee42.evolvedmekanism.utils.EMVanillaMekanism;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.client.recipe_viewer.alias.IAliasMapping;
import mekanism.client.recipe_viewer.alias.MekanismAliases;
import mekanism.client.recipe_viewer.alias.RVAliasHelper;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.tier.FactoryTier;
import net.minecraft.Util;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NothingNullByDefault
public final class EMAliasMapping implements IAliasMapping {

    @Override
    public <ITEM, FLUID, CHEMICAL> void addAliases(RVAliasHelper<ITEM, FLUID, CHEMICAL> rv) {
        addFactoryAliases(rv);
        addStorageAliases(rv);
        addTransferAliases(rv);
        addUpgradeAliases(rv);
        rv.addItemHolderAliases(List.of(EMItems.PORTABLE_HAZMAT_SUIT), () -> Util.makeDescriptionId("alias", EvolvedMekanism.rl("hazmat")));
    }

    private static List<FactoryTier> extraFactoryTiers() {
        return List.of(
                EMFactoryTier.OVERCLOCKED,
                EMFactoryTier.QUANTUM,
                EMFactoryTier.DENSE,
                EMFactoryTier.MULTIVERSAL,
                EMFactoryTier.CREATIVE
        );
    }

    private <ITEM, FLUID, CHEMICAL> void addFactoryAliases(RVAliasHelper<ITEM, FLUID, CHEMICAL> rv) {
        List<FactoryTier> extraTiers = extraFactoryTiers();
        for (FactoryType factoryType : EMVanillaMekanism.FACTORY_TYPES) {
            List<ItemLike> extraFactories = factories(extraTiers, factoryType);
            if (!extraFactories.isEmpty()) {
                rv.addAliases(extraFactories, factoryType.getBaseBlock());
            }
        }
        List<FactoryTier> alloyingTiers = new ArrayList<>(List.of(EMVanillaMekanism.FACTORY_TIERS));
        alloyingTiers.addAll(extraTiers);
        List<ItemLike> alloyingFactories = factories(alloyingTiers, EMFactoryType.ALLOYING);
        if (!alloyingFactories.isEmpty()) {
            rv.addAliases(alloyingFactories, EMBlocks.ALLOYER);
        }
        rv.addAliases(EMBlocks.ALLOYER, () -> Util.makeDescriptionId("alias", EvolvedMekanism.rl("alloying")));
    }

    private static List<ItemLike> factories(List<FactoryTier> tiers, FactoryType type) {
        if (type == null) {
            return List.of();
        }
        return tiers.stream()
                .map(tier -> EMBlocks.getFactory(tier, type))
                .filter(Objects::nonNull)
                .map(factory -> (ItemLike) factory)
                .toList();
    }

    private <ITEM, FLUID, CHEMICAL> void addStorageAliases(RVAliasHelper<ITEM, FLUID, CHEMICAL> rv) {
        rv.addAliases(List.of(
                EMBlocks.OVERCLOCKED_BIN,
                EMBlocks.QUANTUM_BIN,
                EMBlocks.DENSE_BIN,
                EMBlocks.MULTIVERSAL_BIN
        ), MekanismAliases.BIN_DRAWER, MekanismAliases.ITEM_STORAGE);

        rv.addAliases(List.of(
                EMBlocks.OVERCLOCKED_FLUID_TANK,
                EMBlocks.QUANTUM_FLUID_TANK,
                EMBlocks.DENSE_FLUID_TANK,
                EMBlocks.MULTIVERSAL_FLUID_TANK
        ), MekanismAliases.FLUID_STORAGE, MekanismAliases.STORAGE_PORTABLE, Items.BUCKET::getDescriptionId);

        rv.addAliases(List.of(
                EMBlocks.OVERCLOCKED_CHEMICAL_TANK,
                EMBlocks.QUANTUM_CHEMICAL_TANK,
                EMBlocks.DENSE_CHEMICAL_TANK,
                EMBlocks.MULTIVERSAL_CHEMICAL_TANK
        ),
                MekanismAliases.CHEMICAL_STORAGE,
                MekanismAliases.GAS_STORAGE,
                MekanismAliases.INFUSE_TYPE_STORAGE,
                MekanismAliases.INFUSION_STORAGE,
                MekanismAliases.PIGMENT_STORAGE,
                MekanismAliases.SLURRY_STORAGE
        );

        rv.addAliases(List.of(
                EMBlocks.OVERCLOCKED_ENERGY_CUBE,
                EMBlocks.QUANTUM_ENERGY_CUBE,
                EMBlocks.DENSE_ENERGY_CUBE,
                EMBlocks.MULTIVERSAL_ENERGY_CUBE
        ), MekanismAliases.ENERGY_STORAGE, MekanismAliases.ENERGY_STORAGE_BATTERY, MekanismAliases.ITEM_CHARGER);

        rv.addAliases(List.of(
                EMBlocks.OVERCLOCKED_INDUCTION_CELL,
                EMBlocks.QUANTUM_INDUCTION_CELL,
                EMBlocks.DENSE_INDUCTION_CELL,
                EMBlocks.MULTIVERSAL_INDUCTION_CELL,
                EMBlocks.CREATIVE_INDUCTION_CELL,
                EMBlocks.OVERCLOCKED_INDUCTION_PROVIDER,
                EMBlocks.QUANTUM_INDUCTION_PROVIDER,
                EMBlocks.DENSE_INDUCTION_PROVIDER,
                EMBlocks.MULTIVERSAL_INDUCTION_PROVIDER,
                EMBlocks.CREATIVE_INDUCTION_PROVIDER
        ), MekanismAliases.MATRIX_COMPONENT);
        rv.addAliases(List.of(
                EMBlocks.OVERCLOCKED_INDUCTION_CELL,
                EMBlocks.QUANTUM_INDUCTION_CELL,
                EMBlocks.DENSE_INDUCTION_CELL,
                EMBlocks.MULTIVERSAL_INDUCTION_CELL,
                EMBlocks.CREATIVE_INDUCTION_CELL
        ), MekanismAliases.ENERGY_STORAGE, MekanismAliases.ENERGY_STORAGE_BATTERY, MekanismAliases.ITEM_CHARGER);
        rv.addAliases(List.of(
                EMBlocks.OVERCLOCKED_INDUCTION_PROVIDER,
                EMBlocks.QUANTUM_INDUCTION_PROVIDER,
                EMBlocks.DENSE_INDUCTION_PROVIDER,
                EMBlocks.MULTIVERSAL_INDUCTION_PROVIDER,
                EMBlocks.CREATIVE_INDUCTION_PROVIDER
        ), MekanismAliases.ENERGY_TRANSFER, MekanismAliases.ENERGY_THROUGHPUT, MekanismAliases.ITEM_CHARGER);

        rv.addItemHolderAliases(List.of(
                EMItems.BOOSTED_QIO_DRIVE,
                EMItems.SINGULARITY_QIO_DRIVE,
                EMItems.HYPRA_SOLIDIFIED_QIO_DRIVE,
                EMItems.BLACK_HOLE_QIO_DRIVE,
                EMItems.CREATIVE_QIO_DRIVE
        ), MekanismAliases.QIO_FULL, MekanismAliases.QIO_DRIVE_CELL, MekanismAliases.QIO_DRIVE_DISK, MekanismAliases.ITEM_STORAGE);
    }

    private <ITEM, FLUID, CHEMICAL> void addTransferAliases(RVAliasHelper<ITEM, FLUID, CHEMICAL> rv) {
        rv.addAliases(List.of(
                EMBlocks.OVERCLOCKED_UNIVERSAL_CABLE,
                EMBlocks.QUANTUM_UNIVERSAL_CABLE,
                EMBlocks.DENSE_UNIVERSAL_CABLE,
                EMBlocks.MULTIVERSAL_UNIVERSAL_CABLE,
                EMBlocks.CREATIVE_UNIVERSAL_CABLE
        ),
                MekanismAliases.ENERGY_TRANSFER,
                MekanismAliases.TRANSMITTER,
                MekanismAliases.TRANSMITTER_CONDUIT,
                MekanismAliases.TRANSMITTER_PIPE,
                MekanismAliases.TRANSMITTER_TUBE
        );
        rv.addAliases(List.of(
                EMBlocks.OVERCLOCKED_THERMODYNAMIC_CONDUCTOR,
                EMBlocks.QUANTUM_THERMODYNAMIC_CONDUCTOR,
                EMBlocks.DENSE_THERMODYNAMIC_CONDUCTOR,
                EMBlocks.MULTIVERSAL_THERMODYNAMIC_CONDUCTOR,
                EMBlocks.CREATIVE_THERMODYNAMIC_CONDUCTOR
        ),
                MekanismAliases.HEAT_TRANSFER,
                MekanismAliases.TRANSMITTER,
                MekanismAliases.TRANSMITTER_CONDUIT,
                MekanismAliases.TRANSMITTER_PIPE,
                MekanismAliases.TRANSMITTER_TUBE
        );
        rv.addAliases(List.of(
                EMBlocks.OVERCLOCKED_MECHANICAL_PIPE,
                EMBlocks.QUANTUM_MECHANICAL_PIPE,
                EMBlocks.DENSE_MECHANICAL_PIPE,
                EMBlocks.MULTIVERSAL_MECHANICAL_PIPE,
                EMBlocks.CREATIVE_MECHANICAL_PIPE
        ), MekanismAliases.FLUID_TRANSFER, MekanismAliases.TRANSMITTER, MekanismAliases.TRANSMITTER_CONDUIT, MekanismAliases.TRANSMITTER_TUBE);
        rv.addAliases(List.of(
                EMBlocks.OVERCLOCKED_LOGISTICAL_TRANSPORTER,
                EMBlocks.QUANTUM_LOGISTICAL_TRANSPORTER,
                EMBlocks.DENSE_LOGISTICAL_TRANSPORTER,
                EMBlocks.MULTIVERSAL_LOGISTICAL_TRANSPORTER,
                EMBlocks.CREATIVE_LOGISTICAL_TRANSPORTER
        ),
                MekanismAliases.ITEM_TRANSFER,
                MekanismAliases.TRANSMITTER,
                MekanismAliases.TRANSMITTER_CONDUIT,
                MekanismAliases.TRANSMITTER_PIPE,
                MekanismAliases.TRANSMITTER_TUBE
        );
        rv.addAliases(List.of(
                EMBlocks.OVERCLOCKED_PRESSURIZED_TUBE,
                EMBlocks.QUANTUM_PRESSURIZED_TUBE,
                EMBlocks.DENSE_PRESSURIZED_TUBE,
                EMBlocks.MULTIVERSAL_PRESSURIZED_TUBE,
                EMBlocks.CREATIVE_PRESSURIZED_TUBE
        ),
                MekanismAliases.CHEMICAL_TRANSFER,
                MekanismAliases.GAS_TRANSFER,
                MekanismAliases.INFUSE_TYPE_TRANSFER,
                MekanismAliases.INFUSION_TRANSFER,
                MekanismAliases.PIGMENT_TRANSFER,
                MekanismAliases.SLURRY_TRANSFER,
                MekanismAliases.TRANSMITTER,
                MekanismAliases.TRANSMITTER_CONDUIT,
                MekanismAliases.TRANSMITTER_PIPE
        );
    }

    private <ITEM, FLUID, CHEMICAL> void addUpgradeAliases(RVAliasHelper<ITEM, FLUID, CHEMICAL> rv) {
        rv.addItemHolderAliases(List.of(
                EMItems.OVERCLOCKED_TIER_INSTALLER,
                EMItems.QUANTUM_TIER_INSTALLER,
                EMItems.DENSE_TIER_INSTALLER,
                EMItems.MULTIVERSAL_TIER_INSTALLER,
                EMItems.CREATIVE_TIER_INSTALLER,
                EMItems.MAX_TIER_INSTALLER
        ), MekanismAliases.INSTALLER_FACTORY, MekanismAliases.INSTALLER_UPGRADE);
    }
}
