package fr.iglee42.evolvedmekanism.datagen.loot;

import fr.iglee42.emgenerators.registries.EMGenBlocks;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.blocks.BlockNoctisRozuliOre;
import fr.iglee42.evolvedmekanism.blocks.BlockTieredPersonalStorage;
import fr.iglee42.evolvedmekanism.loot.PersonalTieredStorageContentsLootFunction;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMOreType;
import fr.iglee42.evolvedmekanism.utils.ModsCompats;
import mekanism.common.block.BlockEnergyCube;
import mekanism.common.block.BlockOre;
import mekanism.common.block.basic.BlockBin;
import mekanism.common.block.basic.BlockFluidTank;
import mekanism.common.block.prefab.BlockFactoryMachine.BlockFactory;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.registries.MekanismItems;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ResourceType;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EMBlockLoot extends BlockLootSubProvider {

    public EMBlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> blocks = new ArrayList<>();
        EMBlocks.BLOCKS.getPrimaryEntries().forEach(holder -> {
            Block block = holder.get();
            if (!(block instanceof LiquidBlock)) {
                blocks.add(block);
            }
        });
        EMBlocks.BLOCKS_NO_ITEMS.getEntries().forEach(holder -> {
            Block block = holder.get();
            if (!(block instanceof LiquidBlock)) {
                blocks.add(block);
            }
        });
        if (ModsCompats.MEKANISMGENERATORS.isLoaded()) {
            EMGenBlocks.BLOCKS.getPrimaryEntries().forEach(holder -> blocks.add(holder.get()));
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
        if (block instanceof BlockNoctisRozuliOre) {
            addNaturalNoctis(block, path.contains("deepslate"));
        } else if (path.equals("noctis_rozuli_ore") || path.equals("deepslate_noctis_rozuli_ore")) {
            addNoctisOre(block);
        } else if (block instanceof BlockOre) {
            addDimOre(block, path);
        } else if (block instanceof BlockBin) {
            addCopy(block, false, MekanismDataComponents.LOCK.get(), MekanismDataComponents.ATTACHED_ITEMS.get());
        } else if (block instanceof BlockEnergyCube) {
            addCopy(block, true,
                    MekanismDataComponents.EJECTOR.get(), MekanismDataComponents.OWNER.get(), MekanismDataComponents.REDSTONE_CONTROL.get(),
                    MekanismDataComponents.SECURITY.get(), MekanismDataComponents.SIDE_CONFIG.get(), MekanismDataComponents.ATTACHED_ENERGY.get(),
                    MekanismDataComponents.ATTACHED_ITEMS.get());
        } else if (block instanceof BlockFluidTank) {
            addCopy(block, true,
                    MekanismDataComponents.EDIT_MODE.get(), MekanismDataComponents.OWNER.get(), MekanismDataComponents.SECURITY.get(),
                    MekanismDataComponents.ATTACHED_ITEMS.get(), MekanismDataComponents.ATTACHED_FLUIDS.get());
        } else if (block instanceof BlockFactory<?>) {
            addCopy(block, true,
                    MekanismDataComponents.EJECTOR.get(), MekanismDataComponents.OWNER.get(), MekanismDataComponents.REDSTONE_CONTROL.get(),
                    MekanismDataComponents.SECURITY.get(), MekanismDataComponents.SIDE_CONFIG.get(), MekanismDataComponents.SORTING.get(),
                    MekanismDataComponents.UPGRADES.get(), MekanismDataComponents.ATTACHED_ENERGY.get(), MekanismDataComponents.ATTACHED_ITEMS.get());
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
                    .add(LootItem.lootTableItem(block).apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                            .include(MekanismDataComponents.ATTACHED_ENERGY.get()))))));
        } else if (path.contains("solar_generator") || path.contains("lunar_generator")) {
            addCopy(block, true,
                    MekanismDataComponents.OWNER.get(), MekanismDataComponents.REDSTONE_CONTROL.get(), MekanismDataComponents.SECURITY.get(),
                    MekanismDataComponents.UPGRADES.get(), MekanismDataComponents.ATTACHED_ENERGY.get(), MekanismDataComponents.ATTACHED_ITEMS.get());
        } else if (path.equals("lunar_neutron_activator")) {
            addCopy(block, true,
                    MekanismDataComponents.EJECTOR.get(), MekanismDataComponents.OWNER.get(), MekanismDataComponents.REDSTONE_CONTROL.get(),
                    MekanismDataComponents.SECURITY.get(), MekanismDataComponents.SIDE_CONFIG.get(),
                    MekanismDataComponents.ATTACHED_ITEMS.get(), MekanismDataComponents.ATTACHED_CHEMICALS.get());
        } else if (path.endsWith("_chemical_tank")) {
            addCopy(block, true,
                    MekanismDataComponents.DUMP_MODE.get(), MekanismDataComponents.EJECTOR.get(), MekanismDataComponents.OWNER.get(),
                    MekanismDataComponents.REDSTONE_CONTROL.get(), MekanismDataComponents.SECURITY.get(), MekanismDataComponents.SIDE_CONFIG.get(),
                    MekanismDataComponents.ATTACHED_ITEMS.get(), MekanismDataComponents.ATTACHED_CHEMICALS.get());
        } else {
            addCopy(block, true,
                    MekanismDataComponents.EJECTOR.get(), MekanismDataComponents.OWNER.get(), MekanismDataComponents.REDSTONE_CONTROL.get(),
                    MekanismDataComponents.SECURITY.get(), MekanismDataComponents.SIDE_CONFIG.get(), MekanismDataComponents.UPGRADES.get(),
                    MekanismDataComponents.ATTACHED_ENERGY.get(), MekanismDataComponents.ATTACHED_ITEMS.get());
        }
    }

    private void addCopy(Block block, boolean copyName, DataComponentType<?>... components) {
        CopyComponentsFunction.Builder copy = CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY);
        for (DataComponentType<?> component : components) {
            copy.include(component);
        }
        var entry = LootItem.lootTableItem(block).apply(copy);
        if (copyName) {
            entry.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY));
        }
        add(block, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)));
    }

    private void addPersonalStorage(Block block) {
        add(block, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(
                LootItem.lootTableItem(block)
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                .include(MekanismDataComponents.OWNER.get())
                                .include(MekanismDataComponents.SECURITY.get()))
                        .apply(PersonalTieredStorageContentsLootFunction.builder())
        )));
    }

    private void addDimOre(Block block, String path) {
        Item drop;
        if (path.contains("fluorite")) {
            var enchants = registries.lookupOrThrow(Registries.ENCHANTMENT);
            add(block, createSilkTouchDispatchTable(block, applyExplosionDecay(block,
                    LootItem.lootTableItem(MekanismItems.FLUORITE_GEM)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
                            .apply(ApplyBonusCount.addOreBonusCount(enchants.getOrThrow(Enchantments.FORTUNE))))));
            return;
        } else if (path.contains("osmium")) {
            drop = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.RAW, PrimaryResource.OSMIUM).asItem();
        } else if (path.contains("tin")) {
            drop = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.RAW, PrimaryResource.TIN).asItem();
        } else if (path.contains("lead")) {
            drop = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.RAW, PrimaryResource.LEAD).asItem();
        } else if (path.contains("uranium")) {
            drop = MekanismItems.PROCESSED_RESOURCES.get(ResourceType.RAW, PrimaryResource.URANIUM).asItem();
        } else {
            dropSelf(block);
            return;
        }
        add(block, createOreDrop(block, drop));
    }

    private void addNoctisOre(Block block) {
        var enchants = registries.lookupOrThrow(Registries.ENCHANTMENT);
        add(block, createSilkTouchDispatchTable(block, applyExplosionDecay(block,
                LootItem.lootTableItem(EMItems.NOCTIS_ROZULI)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9)))
                        .apply(ApplyBonusCount.addOreBonusCount(enchants.getOrThrow(Enchantments.FORTUNE))))));
    }

    private void addNaturalNoctis(Block block, boolean deepslate) {
        var enchants = registries.lookupOrThrow(Registries.ENCHANTMENT);
        Item coveredSilk = deepslate ? Items.DEEPSLATE_LAPIS_ORE : Items.LAPIS_ORE;
        Item uncoveredSilk = deepslate
                ? EMBlocks.ORES.get(EMOreType.NOCTIS_ROZULI).deepslate().asItem()
                : EMBlocks.ORES.get(EMOreType.NOCTIS_ROZULI).stone().asItem();
        add(block, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(
                AlternativesEntry.alternatives(
                        AlternativesEntry.alternatives(
                                LootItem.lootTableItem(coveredSilk).when(hasSilkTouch()),
                                applyExplosionDecay(block, LootItem.lootTableItem(Items.LAPIS_LAZULI)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9)))
                                        .apply(ApplyBonusCount.addOreBonusCount(enchants.getOrThrow(Enchantments.FORTUNE))))
                        ).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(BlockNoctisRozuliOre.UNCOVERED, false))),
                        AlternativesEntry.alternatives(
                                LootItem.lootTableItem(uncoveredSilk).when(hasSilkTouch()),
                                applyExplosionDecay(block, LootItem.lootTableItem(EMItems.NOCTIS_ROZULI)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9)))
                                        .apply(ApplyBonusCount.addOreBonusCount(enchants.getOrThrow(Enchantments.FORTUNE))))
                        )
                )
        )));
    }
}
