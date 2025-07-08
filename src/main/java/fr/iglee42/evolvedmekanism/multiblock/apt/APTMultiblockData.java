package fr.iglee42.evolvedmekanism.multiblock.apt;

import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.NBTConstants;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.common.capabilities.chemical.multiblock.MultiblockChemicalTankBuilder;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerChemicalTankWrapper;
import mekanism.common.integration.computer.annotation.SyntheticComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.Color;
import mekanism.common.lib.multiblock.IValveHandler;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class APTMultiblockData extends MultiblockData implements IValveHandler{

    @ContainerSync
    @WrappingComputerMethod(wrapper = ComputerChemicalTankWrapper.class, methodNames = {"getInput", "getInputCapacity", "getInputNeeded", "getInputFilledPercentage"}, docPlaceholder = "input tank")
    public IGasTank inputTank;

    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getInputSlot", docPlaceholder = "input slot")
    InputInventorySlot inputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getOutputSlot", docPlaceholder = "output slot")
    OutputInventorySlot outputSlot;

    @ContainerSync
    public int progress;
    @ContainerSync
    public int defaultRecipeProgress = -1;
    public int lastProgress;

    @ContainerSync
    @SyntheticComputerMethod(getter = "getSuperchargers", getterDescription = "How many superchargers this APT has")
    public int superchargingElements;


    @ContainerSync
    public IEnergyContainer energyContainer;

    public ItemStackGasToItemStackRecipe currentRecipe;

    public float prevGasScale;

    public APTMultiblockData(TileEntityAPTCasing tile) {
        super(tile);
        gasTanks.add(inputTank = MultiblockChemicalTankBuilder.GAS.input(this, EMConfig.general.aptInputStorage::getOrDefault, this::hasRecipeWith,
              ChemicalAttributeValidator.ALWAYS_ALLOW, createSaveAndComparator()));
        inventorySlots.add(inputSlot = InputInventorySlot.at(item->hasRecipeForInputs(item,inputTank.getStack()),this::hasRecipeWith,createSaveAndComparator(), 28, 40));
        inventorySlots.add(outputSlot = OutputInventorySlot.at(createSaveAndComparator(), 132, 40));
        energyContainers.add(energyContainer =  BasicEnergyContainer.create(EMConfig.general.aptEnergyStorage.getOrDefault(), automationType -> isFormed(), type->isFormed(), createSaveAndComparator()));
    }

    private boolean hasRecipeWith(ItemStack item) {
        return getWorld().getRecipeManager().getAllRecipesFor(EMRecipeType.APT.getRecipeType()).stream().anyMatch(r-> r.getItemInput().testType(item));
    }

    private boolean hasRecipeWith(Gas gas) {
        return getWorld().getRecipeManager().getAllRecipesFor(EMRecipeType.APT.getRecipeType()).stream().anyMatch(r-> r.getChemicalInput().testType(gas));
    }


    private boolean hasRecipeForInputs(ItemStack stack,GasStack gas){
        return hasRecipeWith(stack) && getWorld().getRecipeManager().getAllRecipesFor(EMRecipeType.APT.getRecipeType()).stream().anyMatch(r-> r.getChemicalInput().testType(gas));
    }

    @Override
    public boolean tick(Level world) {
        boolean needsPacket = super.tick(world);

        if (world.isClientSide) return false;

        if (currentRecipe == null){
            world.getRecipeManager().getAllRecipesFor(EMRecipeType.APT.getRecipeType()).stream().filter(r->r.test(inputSlot.getStack(),inputTank.getStack())).findFirst().ifPresent(r->{
                currentRecipe = r;
                markDirty();
                progress = (int) ((EMConfig.general.aptDefaultDuration.getOrDefault() * (r.getChemicalInput().getNeededAmount(inputTank.getStack()) / 100)) / (superchargingElements  + 1));
                defaultRecipeProgress = progress;
            });
        } else {
            if (!currentRecipe.test(inputSlot.getStack(),inputTank.getStack())){
                currentRecipe = null;
                progress = 0;
                defaultRecipeProgress = -1;
                markDirty();
                return true;
            }
            if (canProcess()){
                extractEnergy(0,EMConfig.general.aptEnergyConsumption.getOrDefault(),null,Action.EXECUTE);
                progress--;
                if (progress == 0){
                    extractItem(0,Math.toIntExact(currentRecipe.getItemInput().getNeededAmount(inputSlot.getStack())),null,Action.EXECUTE);
                    inputTank.extract(currentRecipe.getChemicalInput().getNeededAmount(inputTank.getStack()),Action.EXECUTE,AutomationType.INTERNAL);
                    outputSlot.insertItem(currentRecipe.getOutput(inputSlot.getStack(),inputTank.getStack()),Action.EXECUTE,AutomationType.INTERNAL);
                    currentRecipe = null;
                    defaultRecipeProgress = -1;
                }
                needsPacket = true;
            }
        }

        if (lastProgress != progress) {
            markDirty();
            needsPacket = true;
        }
        float gasScale = MekanismUtils.getScale(prevGasScale, inputTank);
        if (gasScale != prevGasScale) {
            needsPacket = true;
            prevGasScale = gasScale;
        }

        return needsPacket;
    }

    private boolean canProcess() {
        return getEnergy(0).greaterOrEqual(EMConfig.general.aptEnergyConsumption.getOrDefault()) && (outputSlot.isEmpty() || outputSlot.insertItem(currentRecipe.getOutput(inputSlot.getStack(), inputTank.getStack()), Action.SIMULATE, AutomationType.INTERNAL).isEmpty());
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        super.writeUpdateTag(tag);
        tag.putInt(NBTConstants.PROGRESS, progress);
        tag.putInt("defaultRecipeProgress", defaultRecipeProgress);
        tag.putFloat(NBTConstants.SCALE, prevGasScale);
        tag.put(NBTConstants.GAS_STORED, inputTank.getStack().write(new CompoundTag()));

    }

    @Override
    public void readUpdateTag(CompoundTag tag) {
        super.readUpdateTag(tag);
        NBTUtils.setFloatIfPresent(tag, NBTConstants.SCALE, scale -> prevGasScale = scale);
        NBTUtils.setIntIfPresent(tag,NBTConstants.PROGRESS, pg -> progress = pg);
        NBTUtils.setIntIfPresent(tag,"defaultRecipeProgress", pg -> defaultRecipeProgress = pg);
        NBTUtils.setGasStackIfPresent(tag, NBTConstants.GAS_STORED, value -> inputTank.setStack(value));
    }

    @Override
    protected int getMultiblockRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(inputTank.getStored(), inputTank.getCapacity());
    }


    public boolean handlesSound(TileEntityAPTCasing tile) {
        return tile.getBlockPos().equals(getMinPos().offset(3, 0, 0)) ||
               tile.getBlockPos().equals(getMaxPos().offset(-3, 0, 0));
    }

    public double getScaledProgress() {
        return defaultRecipeProgress == -1 ? 0 : (double) (defaultRecipeProgress - progress) / defaultRecipeProgress;
    }

    public Color getColor(){
        return inputTank.isEmpty() ? Color.WHITE : Color.rgb(inputTank.getStack().getChemicalTint());
    }
}
