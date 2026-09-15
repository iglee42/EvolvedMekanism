package fr.iglee42.evolvedmekanism.datagen.loot;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.loot.PersonalTieredStorageContentsLootFunction;
import mekanism.common.block.BlockEnergyCube;
import mekanism.common.block.BlockOre;
import mekanism.common.block.basic.BlockBin;
import mekanism.common.block.basic.BlockFluidTank;
import mekanism.common.block.prefab.BlockFactoryMachine.BlockFactory;
import mekanism.common.registries.MekanismItems;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ResourceType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EMBlockLoot extends BlockLootSubProvider {

    public EMBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> blocks = new ArrayList<>();
        for (Block block : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
            if (id != null && EvolvedMekanism.MODID.equals(id.getNamespace()) && !(block instanceof LiquidBlock)) {
                blocks.add(block);
            }
        }
        return blocks;
    }

    @Override
    protected void generate() {
        for (Block block : getKnownBlocks()) {
            addBlock(block);
        }
    }

    private void addBlock(Block block) {
        String path = BuiltInRegistries.BLOCK.getKey(block).getPath();
        if (block instanceof BlockOre) {
            addDimOre(block, path);
        } else if (block instanceof BlockBin) {
            addCopy(block, false, nbt -> nbt.copy("Items", "mekData.Items"));
        } else if (block instanceof BlockEnergyCube) {
            addCopy(block, true, nbt -> nbt
                    .copy("componentSecurity.owner", "mekData.owner")
                    .copy("componentSecurity.securityMode", "mekData.securityMode")
                    .copy("componentConfig", "mekData.componentConfig")
                    .copy("componentEjector", "mekData.componentEjector")
                    .copy("controlType", "mekData.controlType")
                    .copy("EnergyContainers", "mekData.EnergyContainers")
                    .copy("Items", "mekData.Items"));
        } else if (block instanceof BlockFluidTank) {
            addCopy(block, true, nbt -> nbt
                    .copy("componentSecurity.owner", "mekData.owner")
                    .copy("componentSecurity.securityMode", "mekData.securityMode")
                    .copy("editMode", "mekData.editMode")
                    .copy("FluidTanks", "mekData.FluidTanks")
                    .copy("Items", "mekData.Items"));
        } else if (block instanceof BlockFactory<?>) {
            addCopy(block, true, nbt -> nbt
                    .copy("componentSecurity.owner", "mekData.owner")
                    .copy("componentSecurity.securityMode", "mekData.securityMode")
                    .copy("componentUpgrade", "mekData.componentUpgrade")
                    .copy("componentConfig", "mekData.componentConfig")
                    .copy("componentEjector", "mekData.componentEjector")
                    .copy("sorting", "mekData.sorting")
                    .copy("controlType", "mekData.controlType")
                    .copy("EnergyContainers", "mekData.EnergyContainers")
                    .copy("Items", "mekData.Items"));
        } else if (block instanceof BlockTieredPersonalStorage<?, ?>) {
            addPersonalStorage(block);
        } else if (path.endsWith("_universal_cable") || path.endsWith("_mechanical_pipe") || path.endsWith("_pressurized_tube")
                || path.endsWith("_logistical_transporter") || path.endsWith("_thermodynamic_conductor")
                || path.endsWith("_induction_provider") || path.equals("apt_casing") || path.equals("supercharging_element")
                || path.startsWith("block_")) {
            dropSelf(block);
        } else if (path.endsWith("_induction_cell") || path.equals("apt_port")) {
            add(block, LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(block).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                            .copy("EnergyContainers", "mekData.EnergyContainers"))))));
        } else if (path.contains("solar_generator") || path.contains("lunar_generator")) {
            addCopy(block, true, nbt -> nbt
                    .copy("componentSecurity.owner", "mekData.owner")
                    .copy("componentSecurity.securityMode", "mekData.securityMode")
                    .copy("componentUpgrade", "mekData.componentUpgrade")
                    .copy("controlType", "mekData.controlType")
                    .copy("EnergyContainers", "mekData.EnergyContainers")
                    .copy("Items", "mekData.Items"));
        } else if (path.endsWith("_chemical_tank")) {
            addCopy(block, true, nbt -> nbt
                    .copy("componentSecurity.owner", "mekData.owner")
                    .copy("componentSecurity.securityMode", "mekData.securityMode")
                    .copy("componentConfig", "mekData.componentConfig")
                    .copy("componentEjector", "mekData.componentEjector")
                    .copy("dumping", "mekData.dumping")
                    .copy("controlType", "mekData.controlType")
                    .copy("GasTanks", "mekData.GasTanks")
                    .copy("InfusionTanks", "mekData.InfusionTanks")
                    .copy("PigmentTanks", "mekData.PigmentTanks")
                    .copy("SlurryTanks", "mekData.SlurryTanks")
                    .copy("Items", "mekData.Items"));
        } else {
            addCopy(block, true, nbt -> nbt
                    .copy("componentSecurity.owner", "mekData.owner")
                    .copy("componentSecurity.securityMode", "mekData.securityMode")
                    .copy("componentUpgrade", "mekData.componentUpgrade")
                    .copy("componentConfig", "mekData.componentConfig")
                    .copy("componentEjector", "mekData.componentEjector")
                    .copy("controlType", "mekData.controlType")
                    .copy("EnergyContainers", "mekData.EnergyContainers")
                    .copy("Items", "mekData.Items"));
        }
    }

    private void addCopy(Block block, boolean copyName, java.util.function.UnaryOperator<CopyNbtFunction.Builder> nbt) {
        var entry = LootItem.lootTableItem(block).apply(nbt.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)));
        if (copyName) {
            entry.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY));
        }
        add(block, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)));
    }

    private void addPersonalStorage(Block block) {
        add(block, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(
                LootItem.lootTableItem(block)
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("componentSecurity.owner", "mekData.owner")
                                .copy("componentSecurity.securityMode", "mekData.securityMode"))
                        .apply(PersonalTieredStorageContentsLootFunction.builder())
        )));
    }

    private void addDimOre(Block block, String path) {
        Item drop;
        if (path.contains("fluorite")) {
            add(block, createSilkTouchDispatchTable(block, applyExplosionDecay(block,
                    LootItem.lootTableItem(MekanismItems.FLUORITE_GEM)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
                            .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)))));
            return;
        } else if (path.contains("osmium")) {
            drop = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.RAW, PrimaryResource.OSMIUM).get();
        } else if (path.contains("tin")) {
            drop = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.RAW, PrimaryResource.TIN).get();
        } else if (path.contains("lead")) {
            drop = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.RAW, PrimaryResource.LEAD).get();
        } else if (path.contains("uranium")) {
            drop = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.RAW, PrimaryResource.URANIUM).get();
        } else {
            dropSelf(block);
            return;
        }
        add(block, createOreDrop(block, drop));
    }
}
