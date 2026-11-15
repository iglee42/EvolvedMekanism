package fr.iglee42.evolvedmekanism.tiles.enchantment;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMChemicals;
import fr.iglee42.evolvedmekanism.utils.EMEnchantmentHelper;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.SerializationConstants;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.gear.IModuleHelper;
import mekanism.api.gear.ModuleData;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.content.gear.IModuleItem;
import mekanism.common.content.gear.ModuleContainer;
import mekanism.common.content.gear.ModuleHelper;
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
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityEnchantingStation extends TileEntityMekanism implements IBoundingBlock {

    private static final int BASE_TICKS_REQUIRED = MekanismUtils.TICKS_PER_HALF_SECOND;
    public static final long MAX_GAS = 32000;


    public int ticksRequired = BASE_TICKS_REQUIRED;
    public int operatingTicks;
    private boolean usedEnergy = false;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem", docPlaceholder = "energy slot")
    EnergyInventorySlot energySlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class, methodNames = {"getChemical", "getChemicalCapacity", "getChemicalNeeded",
            "getChemicalFilledPercentage"}, docPlaceholder = "chemical")
    public IChemicalTank chemicalTank;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getGasItemInput", docPlaceholder = "gas item input slot")
    ChemicalInventorySlot gasInputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getContainerItem", docPlaceholder = "item holder slot (armor, tool, etc)")
    public InputInventorySlot containerSlot;
    private MachineEnergyContainer<TileEntityEnchantingStation> energyContainer;

    public TileEntityEnchantingStation(BlockPos pos, BlockState state) {
        super(EMBlocks.ENCHANTING_STATION, pos, state);
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSide(facingSupplier);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener), RelativeSide.BACK);
        return builder.build();
    }

    public MachineEnergyContainer<TileEntityEnchantingStation> getEnergyContainer() {
        return energyContainer;
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(facingSupplier);
        builder.addSlot(containerSlot = InputInventorySlot.at(stack->stack.getItem().isEnchantable(stack), listener, 125, 118));
        containerSlot.setSlotType(ContainerSlotType.NORMAL);
        builder.addSlot(gasInputSlot = ChemicalInventorySlot.fill(chemicalTank, listener, 8, 118));
        gasInputSlot.setSlotType(ContainerSlotType.OUTPUT);
        gasInputSlot.setSlotOverlay(SlotOverlay.MINUS);
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 151, 21));
        return builder.build();
    }

    @Override
    public @Nullable IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSide(facingSupplier);
        builder.addTank(chemicalTank = BasicChemicalTank.createModern(MAX_GAS, stack->stack.is(EMChemicals.CRYONOCTIS), listener), RelativeSide.LEFT);
        return builder.build();
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        gasInputSlot.fillTank();
        energySlot.fillContainerOrConvert();
        long clientEnergyUsed = 0L;
        if (pendingResult != null) {
            boolean operated = false;
            if (canFunction() && !containerSlot.isEmpty() && pendingBase.equals(containerSlot.getStack().getTagEnchantments())
                && chemicalTank.getStored() >= pendingChemical) {
                if (energyContainer.getEnergy() >= pendingEnergyPerTick) {
                    operated = true;
                    operatingTicks++;
                    clientEnergyUsed = energyContainer.extract(pendingEnergyPerTick, Action.EXECUTE, AutomationType.INTERNAL);
                    if (operatingTicks >= ticksRequired) {
                        chemicalTank.extract(pendingChemical, Action.EXECUTE, AutomationType.INTERNAL);
                        writeEnchantments(pendingResult);
                        level.playSound(null, getBlockPos(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
                        cancelOperation();
                    }
                } else {
                    operated = true;
                }
            } else if (!canFunction()) {
                operated = true;
            }
            if (!operated) {
                cancelOperation();
            }
        }
        usedEnergy = clientEnergyUsed > 0L;
        return sendUpdatePacket;
    }

    public boolean usedEnergy() {
        return usedEnergy;
    }

    public static final long CHEMICAL_PER_RARITY = 50;
    public static final int TICKS_PER_LEVEL = MekanismUtils.TICKS_PER_HALF_SECOND;

    private Map<Holder<Enchantment>, Integer> pendingResult;
    private ItemEnchantments pendingBase = ItemEnchantments.EMPTY;
    private long pendingEnergyPerTick;
    private long pendingChemical;

    //Common enchantments (high weight) cost 1x, very rare ones 8x
    public static int getRarityMultiplier(Holder<Enchantment> enchantment) {
        int weight = enchantment.value().getWeight();
        if (weight >= 10) {
            return 1;
        } else if (weight >= 5) {
            return 2;
        } else if (weight >= 2) {
            return 4;
        }
        return 8;
    }

    private void cancelOperation() {
        pendingResult = null;
        pendingBase = ItemEnchantments.EMPTY;
        pendingEnergyPerTick = 0;
        pendingChemical = 0;
        operatingTicks = 0;
    }

    private void writeEnchantments(Map<Holder<Enchantment>, Integer> result) {
        ItemStack stack = containerSlot.getStack();
        EnchantmentHelper.updateEnchantments(stack, mutable -> {
            mutable.removeIf(e -> true);
            result.forEach(mutable::set);
        });
        containerSlot.setStack(stack);
    }

    public boolean isWorking() {
        return pendingResult != null;
    }

    public long calculateChemicalCost(Map<Holder<Enchantment>, Integer> requested) {
        if (containerSlot.isEmpty()) {
            return 0;
        }
        ItemEnchantments current = containerSlot.getStack().getTagEnchantments();
        long weightedLevels = 0;
        for (Map.Entry<Holder<Enchantment>, Integer> entry : requested.entrySet()) {
            Holder<Enchantment> enchantment = entry.getKey();
            int levelsToAdd = entry.getValue() - current.getLevel(enchantment);
            if (levelsToAdd > 0) {
                weightedLevels += (long) levelsToAdd * getRarityMultiplier(enchantment);
            }
        }
        return CHEMICAL_PER_RARITY * weightedLevels;
    }

    public void applyEnchantments(Map<Holder<Enchantment>, Integer> requested) {
        if (level == null || containerSlot.isEmpty() || pendingResult != null) {
            return;
        }
        ItemStack stack = containerSlot.getStack();
        ItemEnchantments current = stack.getTagEnchantments();
        Map<Holder<Enchantment>, Integer> result = new HashMap<>();
        current.entrySet().forEach(e -> result.put(e.getKey(), e.getIntValue()));
        int added = 0;
        long weightedLevels = 0;
        Set<Holder<Enchantment>> raised = new HashSet<>();
        for (Map.Entry<Holder<Enchantment>, Integer> entry : requested.entrySet()) {
            Holder<Enchantment> enchantment = entry.getKey();
            int newLevel = entry.getValue();
            int oldLevel = current.getLevel(enchantment);
            if (newLevel == oldLevel) {
                continue;
            }
            if (newLevel < 0 || newLevel > enchantment.value().getMaxLevel()) {
                return;
            }
            if (EMEnchantmentHelper.isCurse(enchantment) && newLevel < oldLevel) {
                return;
            }
            if (newLevel > oldLevel) {
                if (!stack.supportsEnchantment(enchantment)) {
                    return;
                }
                added += newLevel - oldLevel;
                weightedLevels += (long) (newLevel - oldLevel) * getRarityMultiplier(enchantment);
                raised.add(enchantment);
            }
            if (newLevel == 0) {
                result.remove(enchantment);
            } else {
                result.put(enchantment, newLevel);
            }
        }
        for (Holder<Enchantment> enchantment : raised) {
            for (Holder<Enchantment> other : result.keySet()) {
                if (!enchantment.equals(other) && !Enchantment.areCompatible(enchantment, other)) {
                    return;
                }
            }
        }
        if (added == 0) {
            if (!result.equals(current.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getIntValue())))) {
                writeEnchantments(result);
            }
            return;
        }
        long chemicalCost = calculateChemicalCost(requested);
        if (chemicalTank.getCapacity() < chemicalCost) {
            return;
        }
        pendingResult = result;
        pendingBase = current;
        pendingChemical = chemicalCost;
        ticksRequired = TICKS_PER_LEVEL * added;
        pendingEnergyPerTick = Math.max(1, energyContainer.getEnergyPerTick() * weightedLevels / added);
        operatingTicks = 0;
    }


    public double getScaledProgress() {
        return (double) operatingTicks / ticksRequired;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        operatingTicks = nbt.getInt(SerializationConstants.PROGRESS);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(nbtTags, provider);
        nbtTags.putInt(SerializationConstants.PROGRESS, operatingTicks);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(() -> operatingTicks, value -> operatingTicks = value));
        container.track(SyncableInt.create(() -> ticksRequired, value -> ticksRequired = value));
        container.track(SyncableBoolean.create(this::usedEnergy, value -> usedEnergy = value));
    }

    public IChemicalTank getChemicalTank() {
        return chemicalTank;
    }
}
