package fr.iglee42.evolvedmekanism.tiles.enchantment;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMGases;
import fr.iglee42.evolvedmekanism.utils.EMEnchantmentHelper;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.NBTConstants;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableBoolean;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TileEntityEnchantingStation extends TileEntityMekanism implements IBoundingBlock {

    private static final int BASE_TICKS_REQUIRED = 10;
    public static final long MAX_GAS = 10000;
    public static final long CHEMICAL_PER_RARITY = 50;
    public static final int TICKS_PER_LEVEL = 10;

    public int ticksRequired = BASE_TICKS_REQUIRED;
    public int operatingTicks;
    private boolean usedEnergy;

    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem", docPlaceholder = "energy slot")
    EnergyInventorySlot energySlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class, methodNames = {"getChemical", "getChemicalCapacity", "getChemicalNeeded",
          "getChemicalFilledPercentage"}, docPlaceholder = "chemical")
    public IGasTank chemicalTank;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getGasItemInput", docPlaceholder = "gas item input slot")
    GasInventorySlot gasInputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getContainerItem", docPlaceholder = "item holder slot (armor, tool, etc)")
    public InputInventorySlot containerSlot;
    private MachineEnergyContainer<TileEntityEnchantingStation> energyContainer;

    private Map<Enchantment, Integer> pendingResult;
    private Map<Enchantment, Integer> pendingBase = Map.of();
    private FloatingLong pendingEnergyPerTick = FloatingLong.ZERO;
    private long pendingChemical;

    public TileEntityEnchantingStation(BlockPos pos, BlockState state) {
        super(EMBlocks.ENCHANTING_STATION, pos, state);
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSide(this::getDirection);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener), RelativeSide.BACK);
        return builder.build();
    }

    public MachineEnergyContainer<TileEntityEnchantingStation> getEnergyContainer() {
        return energyContainer;
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(containerSlot = InputInventorySlot.at(stack -> stack.getItem().isEnchantable(stack) || stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK), listener, 125, 118));
        containerSlot.setSlotType(ContainerSlotType.NORMAL);
        builder.addSlot(gasInputSlot = GasInventorySlot.fill(chemicalTank, listener, 8, 118));
        gasInputSlot.setSlotType(ContainerSlotType.OUTPUT);
        gasInputSlot.setSlotOverlay(SlotOverlay.MINUS);
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 151, 21));
        return builder.build();
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper.forSide(this::getDirection);
        builder.addTank(chemicalTank = ChemicalTankBuilder.GAS.create(MAX_GAS, gas -> gas == EMGases.CRYONOCTIS.get(), listener), RelativeSide.LEFT);
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        gasInputSlot.fillTank();
        energySlot.fillContainerOrConvert();
        FloatingLong clientEnergyUsed = FloatingLong.ZERO;
        if (pendingResult != null) {
            boolean operated = false;
            Map<Enchantment, Integer> current = EMEnchantmentHelper.getEnchantments(containerSlot.getStack());
            if (MekanismUtils.canFunction(this) && !containerSlot.isEmpty() && pendingBase.equals(current) && chemicalTank.getStored() >= pendingChemical) {
                if (energyContainer.getEnergy().greaterOrEqual(pendingEnergyPerTick)) {
                    operated = true;
                    operatingTicks++;
                    clientEnergyUsed = energyContainer.extract(pendingEnergyPerTick, Action.EXECUTE, AutomationType.INTERNAL);
                    if (operatingTicks >= ticksRequired) {
                        chemicalTank.extract(pendingChemical, Action.EXECUTE, AutomationType.INTERNAL);
                        EMEnchantmentHelper.setEnchantments(containerSlot.getStack(), pendingResult);
                        level.playSound(null, getBlockPos(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
                        cancelOperation();
                    }
                } else {
                    operated = true;
                }
            } else if (!MekanismUtils.canFunction(this)) {
                operated = true;
            }
            if (!operated) {
                cancelOperation();
            }
        }
        usedEnergy = clientEnergyUsed.greaterThan(FloatingLong.ZERO);
    }

    public boolean usedEnergy() {
        return usedEnergy;
    }

    private void cancelOperation() {
        pendingResult = null;
        pendingBase = Map.of();
        pendingEnergyPerTick = FloatingLong.ZERO;
        pendingChemical = 0;
        operatingTicks = 0;
    }

    public boolean isWorking() {
        return pendingResult != null;
    }

    public void applyEnchantments(Map<Enchantment, Integer> requested) {
        if (level == null || containerSlot.isEmpty() || pendingResult != null) {
            return;
        }
        ItemStack stack = containerSlot.getStack();
        Map<Enchantment, Integer> current = EMEnchantmentHelper.getEnchantments(stack);
        Map<Enchantment, Integer> result = new HashMap<>(current);
        int added = 0;
        long weightedLevels = 0;
        Set<Enchantment> raised = new HashSet<>();
        for (Map.Entry<Enchantment, Integer> entry : requested.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int newLevel = entry.getValue();
            int oldLevel = current.getOrDefault(enchantment, 0);
            if (newLevel == oldLevel) {
                continue;
            }
            if (newLevel < 0 || newLevel > enchantment.getMaxLevel()) {
                return;
            }
            if (EMEnchantmentHelper.isCurse(enchantment) && newLevel < oldLevel) {
                return;
            }
            if (newLevel > oldLevel) {
                boolean canApply = enchantment.canEnchant(stack) || stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK) || oldLevel > 0;
                if (!canApply) {
                    return;
                }
                added += newLevel - oldLevel;
                weightedLevels += (long) (newLevel - oldLevel) * EMEnchantmentHelper.getRarityMultiplier(enchantment);
                raised.add(enchantment);
            }
            if (newLevel == 0) {
                result.remove(enchantment);
            } else {
                result.put(enchantment, newLevel);
            }
        }
        for (Enchantment enchantment : raised) {
            for (Enchantment other : result.keySet()) {
                if (enchantment != other && !EMEnchantmentHelper.areCompatible(enchantment, other)) {
                    return;
                }
            }
        }
        if (added == 0) {
            if (!result.equals(current)) {
                EMEnchantmentHelper.setEnchantments(stack, result);
            }
            return;
        }
        long chemicalCost = CHEMICAL_PER_RARITY * weightedLevels;
        if (chemicalTank.getCapacity() < chemicalCost) {
            return;
        }
        pendingResult = result;
        pendingBase = current;
        pendingChemical = chemicalCost;
        ticksRequired = TICKS_PER_LEVEL * added;
        FloatingLong perTick = energyContainer.getEnergyPerTick().multiply(weightedLevels).divide(added);
        pendingEnergyPerTick = perTick.isZero() ? FloatingLong.ONE : perTick;
        operatingTicks = 0;
    }

    public double getScaledProgress() {
        return ticksRequired == 0 ? 0 : (double) operatingTicks / ticksRequired;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        operatingTicks = nbt.getInt(NBTConstants.PROGRESS);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.putInt(NBTConstants.PROGRESS, operatingTicks);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(() -> operatingTicks, value -> operatingTicks = value));
        container.track(SyncableInt.create(() -> ticksRequired, value -> ticksRequired = value));
        container.track(SyncableBoolean.create(this::usedEnergy, value -> usedEnergy = value));
    }

    public IGasTank getChemicalTank() {
        return chemicalTank;
    }
}
