package fr.iglee42.evolvedmekanism.tiles.enchantment;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMChemicals;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mekanism.api.*;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.LaserEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableEnum;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.tile.interfaces.IHasMode;
import mekanism.common.tile.laser.TileEntityBasicLaser;
import mekanism.common.tile.laser.TileEntityLaserAmplifier;
import mekanism.common.tile.laser.TileEntityLaserReceptor;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BooleanSupplier;

public class TileEntityLaserDisenchanter extends TileEntityLaserReceptor implements IHasMode {

    public static final long MAX_GAS = 10000;

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class, methodNames = {"getChemical", "getChemicalCapacity", "getChemicalNeeded",
            "getChemicalFilledPercentage"}, docPlaceholder = "chemical")
    public IChemicalTank chemicalTank;

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getGasItemInput", docPlaceholder = "gas item input slot")
    ChemicalInventorySlot gasInputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getGasItemOutput", docPlaceholder = "gas item output slot")
    ChemicalInventorySlot gasOutputSlot;

    private boolean emittingRedstone;
    private TileEntityLaserAmplifier.RedstoneOutput outputMode = TileEntityLaserAmplifier.RedstoneOutput.OFF;


    public TileEntityLaserDisenchanter(BlockPos pos, BlockState state) {
        super(EMBlocks.LASER_DISENCHANTER, pos, state);
    }

    @Override
    protected void addInitialEnergyContainers(EnergyContainerHelper builder, IContentsListener listener) {
        builder.addContainer(energyContainer = LaserEnergyContainer.create(BasicEnergyContainer.notExternal, ConstantPredicates.alwaysTrue(), this, listener), RelativeSide.BACK);
    }

    @Override
    public @Nullable IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSide(facingSupplier);
        builder.addTank(chemicalTank = BasicChemicalTank.createModern(MAX_GAS, stack->stack.is(EMChemicals.CRYONOCTIS), listener));
        return builder.build();
    }

    @Override
    protected @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(facingSupplier);

        for (int slotX = 0; slotX < 6; slotX++) {
            for (int slotY = 0; slotY < 2; slotY++) {
                OutputInventorySlot slot = OutputInventorySlot.at(listener, 43 + slotX * 18, 30 + slotY * 18);
                builder.addSlot(slot);
                slot.setSlotType(ContainerSlotType.NORMAL);
            }
        }

        builder.addSlot(gasInputSlot = ChemicalInventorySlot.drain(chemicalTank, listener, 16, 16));
        builder.addSlot(gasOutputSlot = ChemicalInventorySlot.fill(chemicalTank, listener, 16, 48));
        gasInputSlot.setSlotType(ContainerSlotType.INPUT);
        gasInputSlot.setSlotOverlay(SlotOverlay.PLUS);
        gasOutputSlot.setSlotType(ContainerSlotType.OUTPUT);
        gasOutputSlot.setSlotOverlay(SlotOverlay.MINUS);
        return builder.build();
    }

    @Override
    protected void handleBreakBlock(BlockState state, ServerLevel level, BlockPos hitPos, Player player, ItemStack tool) {
        List<ItemStack> drops = WorldUtils.getDrops(state, level, hitPos, WorldUtils.getTileEntity(level, hitPos), player, tool);
        CommonWorldTickHandler.fallbackItemCollector = drops::add;
        breakBlock(state, level, hitPos, tool);
        CommonWorldTickHandler.fallbackItemCollector = null;

        for (int i = 0; i < drops.size(); i++) {
            ItemStack stack = drops.get(i);
            if (stack.isEnchanted()){
                ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS,ItemEnchantments.EMPTY);
                for (Object2IntMap.Entry<Holder<Enchantment>> enchantment : enchantments.entrySet()) {
                    ItemEnchantments.Mutable bookEnchants = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
                    bookEnchants.set(enchantment.getKey(), enchantment.getIntValue());
                    int xp = getEnchantmentXp(enchantment);
                    if (drops.stream().anyMatch(it->it.is(Items.BOOK)) && chemicalTank.extract(xp * 100L, Action.SIMULATE, AutomationType.INTERNAL).getAmount() >= xp * 100L){
                        chemicalTank.extract(xp * 100L, Action.EXECUTE, AutomationType.INTERNAL);
                        drops.stream().filter(it->it.is(Items.BOOK)).findFirst().ifPresent(it->it.shrink(1));
                        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
                        EnchantmentHelper.setEnchantments(book,bookEnchants.toImmutable());
                        drops.addLast(book);
                        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(enchantments);
                        mutable.removeIf(h->h.equals(enchantment.getKey()));
                        enchantments = mutable.toImmutable();
                        EnchantmentHelper.setEnchantments(stack,enchantments);
                    }
                }
            }
        }

        if (!drops.isEmpty()) {
            BlockPos dropPos = null;
            Direction opposite = null;
            List<IInventorySlot> inventorySlots = getInventorySlots(null);
            for (ItemStack drop : drops) {
                drop = InventoryUtils.insertItem(inventorySlots, drop, Action.EXECUTE, AutomationType.INTERNAL);
                if (!drop.isEmpty()) {
                    if (dropPos == null) {
                        Direction direction = getDirection();
                        dropPos = worldPosition.relative(direction, 2);
                        opposite = direction.getOpposite();
                    }
                    Block.popResourceFromFace(level, dropPos, opposite, drop);
                }
            }
        }
    }

    @Override
    protected boolean handleHitItem(ItemEntity entity) {
        ItemStack stack = entity.getItem();
        stack = InventoryUtils.insertItem(getInventorySlots(null), stack, Action.EXECUTE, AutomationType.INTERNAL);
        entity.setItem(stack);
        return true;
    }


    @Override
    protected boolean onUpdateServer() {
        gasInputSlot.drainTank();
        gasOutputSlot.fillTank();
        setEmittingRedstone(false);

        boolean sendUpdatePacket = super.onUpdateServer();
        if (outputMode != TileEntityLaserAmplifier.RedstoneOutput.ENTITY_DETECTION) {
            setEmittingRedstone(false);
        }
        return sendUpdatePacket;
    }

    @Override
    public void setEmittingRedstone(boolean foundEntity) {
        this.emittingRedstone = foundEntity;
    }


    @Override
    protected long toFire() {
        return canFunction() ? Math.min(super.toFire(), Long.MAX_VALUE) : 0L;
    }


    @Override
    public int getRedstoneLevel() {
        if (outputMode == TileEntityLaserAmplifier.RedstoneOutput.ENERGY_CONTENTS) {
            return MekanismUtils.redstoneLevelFromContents(energyContainer.getEnergy(), energyContainer.getMaxEnergy());
        }
        return emittingRedstone ? Redstone.SIGNAL_MAX : Redstone.SIGNAL_NONE;
    }

    @Override
    protected boolean makesComparatorDirty(ContainerType<?, ?, ?> type) {
        return type == ContainerType.ENERGY;
    }

    private int getEnchantmentXp(Object2IntMap.Entry<Holder<Enchantment>> enchant) {
        Holder<Enchantment> enchantment = enchant.getKey();
        int integer = enchant.getIntValue();
        return enchantment.value().getMinCost(integer);
    }

    public IChemicalTank getChemicalTank() {
        return chemicalTank;
    }

    @Override
    public void nextMode() {
        outputMode = outputMode.getNext();
        setChanged();
    }

    @Override
    public void previousMode() {
        outputMode = outputMode.getPrevious();
        setChanged();
    }

    @Override
    public void readSustainedData(HolderLookup.Provider provider, CompoundTag data) {
        super.readSustainedData(provider, data);
        NBTUtils.setEnumIfPresent(data, SerializationConstants.OUTPUT_MODE, TileEntityLaserAmplifier.RedstoneOutput.BY_ID, mode -> outputMode = mode);
    }

    @Override
    public void writeSustainedData(HolderLookup.Provider provider, CompoundTag data) {
        super.writeSustainedData(provider, data);
        NBTUtils.writeEnum(data, SerializationConstants.OUTPUT_MODE, outputMode);
    }

    @Override
    protected void applyImplicitComponents(@NotNull DataComponentInput input) {
        super.applyImplicitComponents(input);
        outputMode = input.getOrDefault(MekanismDataComponents.REDSTONE_OUTPUT, outputMode);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NotNull Builder builder) {
        super.collectImplicitComponents(builder);
        builder.set(MekanismDataComponents.REDSTONE_OUTPUT, outputMode);
    }

    @Override
    public boolean supportsMode(RedstoneControl mode) {
        return true;
    }

    @ComputerMethod(nameOverride = "getRedstoneOutputMode")
    public TileEntityLaserAmplifier.RedstoneOutput getOutputMode() {
        return outputMode;
    }


    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableEnum.create(TileEntityLaserAmplifier.RedstoneOutput.BY_ID, TileEntityLaserAmplifier.RedstoneOutput.OFF, this::getOutputMode, value -> outputMode = value));
    }


}
