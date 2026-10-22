package fr.iglee42.evolvedmekanism.tiles.enchantment;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMGases;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.NBTConstants;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.math.FloatingLong;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.LaserEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableEnum;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.tile.interfaces.IHasMode;
import mekanism.common.tile.laser.TileEntityLaserAmplifier;
import mekanism.common.tile.laser.TileEntityLaserReceptor;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TileEntityLaserDisenchanter extends TileEntityLaserReceptor implements IHasMode {

    public static final long MAX_GAS = 10000;

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class, methodNames = {"getChemical", "getChemicalCapacity", "getChemicalNeeded",
          "getChemicalFilledPercentage"}, docPlaceholder = "chemical")
    public IGasTank chemicalTank;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getGasItemInput", docPlaceholder = "gas item input slot")
    GasInventorySlot gasInputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getGasItemOutput", docPlaceholder = "gas item output slot")
    GasInventorySlot gasOutputSlot;

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
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper.forSide(this::getDirection);
        builder.addTank(chemicalTank = ChemicalTankBuilder.GAS.create(MAX_GAS, gas -> gas == EMGases.CRYONOCTIS.get(), listener));
        return builder.build();
    }

    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        for (int slotX = 0; slotX < 6; slotX++) {
            for (int slotY = 0; slotY < 2; slotY++) {
                var slot = mekanism.common.inventory.slot.OutputInventorySlot.at(listener, 43 + slotX * 18, 30 + slotY * 18);
                builder.addSlot(slot);
                slot.setSlotType(ContainerSlotType.NORMAL);
            }
        }
        builder.addSlot(gasInputSlot = GasInventorySlot.drain(chemicalTank, listener, 16, 16));
        builder.addSlot(gasOutputSlot = GasInventorySlot.fill(chemicalTank, listener, 16, 48));
        gasInputSlot.setSlotType(ContainerSlotType.INPUT);
        gasInputSlot.setSlotOverlay(SlotOverlay.PLUS);
        gasOutputSlot.setSlotType(ContainerSlotType.OUTPUT);
        gasOutputSlot.setSlotOverlay(SlotOverlay.MINUS);
        return builder.build();
    }

    @Override
    protected void handleBreakBlock(BlockState state, BlockPos hitPos, Player player, ItemStack tool) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        List<ItemStack> drops = new ArrayList<>(Block.getDrops(state, serverLevel, hitPos, WorldUtils.getTileEntity(serverLevel, hitPos), player, tool));
        CommonWorldTickHandler.fallbackItemCollector = stack -> {
            drops.add(stack);
            return true;
        };
        breakBlock(state, hitPos);
        CommonWorldTickHandler.fallbackItemCollector = null;
        disenchantDrops(drops);
        if (drops.isEmpty()) {
            return;
        }
        Direction direction = getDirection();
        BlockPos dropPos = worldPosition.relative(direction, 2);
        Direction opposite = direction.getOpposite();
        for (ItemStack drop : drops) {
            drop = InventoryUtils.insertItem(getInventorySlots(null), drop, Action.EXECUTE, AutomationType.INTERNAL);
            if (!drop.isEmpty()) {
                Block.popResourceFromFace(level, dropPos, opposite, drop);
            }
        }
    }

    private void disenchantDrops(List<ItemStack> drops) {
        int originalCount = drops.size();
        for (int i = 0; i < originalCount; i++) {
            ItemStack stack = drops.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            Map<Enchantment, Integer> enchantments = new HashMap<>(EnchantmentHelper.getEnchantments(stack));
            if (stack.is(Items.ENCHANTED_BOOK)) {
                enchantments.putAll(EnchantmentHelper.deserializeEnchantments(EnchantedBookItem.getEnchantments(stack)));
            }
            if (enchantments.isEmpty()) {
                continue;
            }
            Iterator<Map.Entry<Enchantment, Integer>> iterator = enchantments.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Enchantment, Integer> enchantment = iterator.next();
                int xp = enchantment.getKey().getMinCost(enchantment.getValue());
                long cost = xp * 100L;
                int bookIndex = indexOfBook(drops);
                if (bookIndex >= 0 && chemicalTank.getStored() >= cost) {
                    chemicalTank.extract(cost, Action.EXECUTE, AutomationType.INTERNAL);
                    drops.get(bookIndex).shrink(1);
                    ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
                    EnchantedBookItem.addEnchantment(book, new EnchantmentInstance(enchantment.getKey(), enchantment.getValue()));
                    drops.add(book);
                    iterator.remove();
                    if (stack.is(Items.ENCHANTED_BOOK)) {
                        stack.removeTagKey("StoredEnchantments");
                        if (enchantments.isEmpty()) {
                            stack.setCount(0);
                        } else {
                            enchantments.forEach((left, level) -> EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(left, level)));
                        }
                    } else {
                        EnchantmentHelper.setEnchantments(enchantments, stack);
                    }
                }
            }
        }
        drops.removeIf(ItemStack::isEmpty);
    }

    private static int indexOfBook(List<ItemStack> drops) {
        for (int i = 0; i < drops.size(); i++) {
            ItemStack stack = drops.get(i);
            if (!stack.isEmpty() && stack.is(Items.BOOK)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean handleHitItem(ItemEntity entity) {
        ItemStack stack = entity.getItem();
        stack = InventoryUtils.insertItem(getInventorySlots(null), stack, Action.EXECUTE, AutomationType.INTERNAL);
        entity.setItem(stack);
        return true;
    }

    @Override
    protected void onUpdateServer() {
        gasInputSlot.drainTank();
        gasOutputSlot.fillTank();
        super.onUpdateServer();
        if (outputMode != TileEntityLaserAmplifier.RedstoneOutput.ENTITY_DETECTION) {
            setEmittingRedstone(false);
        }
    }

    @Override
    public void setEmittingRedstone(boolean foundEntity) {
        this.emittingRedstone = foundEntity;
    }

    @Override
    protected FloatingLong toFire() {
        return MekanismUtils.canFunction(this) ? super.toFire() : FloatingLong.ZERO;
    }

    @Override
    public int getRedstoneLevel() {
        if (outputMode == TileEntityLaserAmplifier.RedstoneOutput.ENERGY_CONTENTS) {
            return MekanismUtils.redstoneLevelFromContents(energyContainer.getEnergy(), energyContainer.getMaxEnergy());
        }
        return emittingRedstone ? Redstone.SIGNAL_MAX : Redstone.SIGNAL_NONE;
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
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        NBTUtils.setEnumIfPresent(nbt, NBTConstants.OUTPUT_MODE, TileEntityLaserAmplifier.RedstoneOutput::byIndexStatic, mode -> outputMode = mode);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        NBTUtils.writeEnum(nbt, NBTConstants.OUTPUT_MODE, outputMode);
    }

    public IGasTank getChemicalTank() {
        return chemicalTank;
    }

    @ComputerMethod(nameOverride = "getRedstoneOutputMode")
    public TileEntityLaserAmplifier.RedstoneOutput getOutputMode() {
        return outputMode;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableEnum.create(TileEntityLaserAmplifier.RedstoneOutput::byIndexStatic, TileEntityLaserAmplifier.RedstoneOutput.OFF, this::getOutputMode, value -> outputMode = value));
    }
}
