package fr.iglee42.evolvedmekanism.multiblock.apt;

import fr.iglee42.evolvedmekanism.config.EMConfig;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.SerializationConstants;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.recipes.ItemStackChemicalToItemStackRecipe;
import mekanism.common.capabilities.chemical.VariableCapacityChemicalTank;
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
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

public class APTMultiblockData extends MultiblockData implements IValveHandler{

    @ContainerSync
    @WrappingComputerMethod(wrapper = ComputerChemicalTankWrapper.class, methodNames = {"getInput", "getInputCapacity", "getInputNeeded", "getInputFilledPercentage"}, docPlaceholder = "input tank")
    public IChemicalTank inputTank;

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

    public RecipeHolder<ItemStackChemicalToItemStackRecipe> currentRecipe;

    public float prevGasScale;

    public APTMultiblockData(TileEntityAPTCasing tile) {
        super(tile);
        chemicalTanks.add(inputTank = VariableCapacityChemicalTank.input(this, EMConfig.general.aptInputStorage::getOrDefault, this::hasRecipeWith,
              ChemicalAttributeValidator.ALWAYS_ALLOW, createSaveAndComparator()));
        inventorySlots.add(inputSlot = InputInventorySlot.at(item->hasRecipeForInputs(item,inputTank.getStack()),this::hasRecipeWith,createSaveAndComparator(), 28, 40));
        inventorySlots.add(outputSlot = OutputInventorySlot.at(createSaveAndComparator(), 132, 40));
        energyContainers.add(energyContainer =  BasicEnergyContainer.create(EMConfig.general.aptEnergyStorage.getOrDefault(), automationType -> isFormed(), type->isFormed(), createSaveAndComparator()));
    }

    private boolean hasRecipeWith(ItemStack item) {
        return getLevel().getRecipeManager().getAllRecipesFor(EMRecipeType.APT.getRecipeType()).stream().anyMatch(r-> r.value().getItemInput().testType(item));
    }

    private boolean hasRecipeWith(ChemicalStack gas) {
        return getLevel().getRecipeManager().getAllRecipesFor(EMRecipeType.APT.getRecipeType()).stream().anyMatch(r-> r
                .value().getChemicalInput().testType(gas));
    }


    private boolean hasRecipeForInputs(ItemStack stack,ChemicalStack gas){
        return hasRecipeWith(stack) && getLevel().getRecipeManager().getAllRecipesFor(EMRecipeType.APT.getRecipeType()).stream().anyMatch(r-> r.value().getChemicalInput().testType(gas));
    }

    @Override
    public boolean tick(Level world) {
        boolean needsPacket = super.tick(world);

        if (world.isClientSide) return false;

        if (currentRecipe == null){
            world.getRecipeManager().getAllRecipesFor(EMRecipeType.APT.getRecipeType()).stream().filter(r->r.value().test(inputSlot.getStack(),inputTank.getStack())).findFirst().ifPresent(r->{
                currentRecipe = r;
                markDirty();
                progress = (int) ((EMConfig.general.aptDefaultDuration.getOrDefault() * (r.value().getChemicalInput().getNeededAmount(inputTank.getStack()) / 100)) / (superchargingElements  + 1));
                defaultRecipeProgress = progress;
            });
        } else {
            if (!currentRecipe.value().test(inputSlot.getStack(),inputTank.getStack())){
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
                    extractItem(0,Math.toIntExact(currentRecipe.value().getItemInput().getNeededAmount(inputSlot.getStack())),null,Action.EXECUTE);
                    inputTank.extract(currentRecipe.value().getChemicalInput().getNeededAmount(inputTank.getStack()),Action.EXECUTE,AutomationType.INTERNAL);
                    outputSlot.insertItem(currentRecipe.value().getOutput(inputSlot.getStack(),inputTank.getStack()),Action.EXECUTE,AutomationType.INTERNAL);
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
        return getEnergy(0) >= EMConfig.general.aptEnergyConsumption.getOrDefault() && (outputSlot.isEmpty() || outputSlot.insertItem(currentRecipe.value().getOutput(inputSlot.getStack(), inputTank.getStack()), Action.SIMULATE, AutomationType.INTERNAL).isEmpty());
    }

    @Override
    public void writeUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        super.writeUpdateTag(tag,provider);
        tag.putInt(SerializationConstants.PROGRESS, progress);
        tag.putInt("defaultRecipeProgress", defaultRecipeProgress);
        tag.putFloat(SerializationConstants.SCALE, prevGasScale);
        tag.put(SerializationConstants.CHEMICAL, inputTank.getStack().saveOptional(provider));

    }

    @Override
    public void readUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        super.readUpdateTag(tag,provider);
        NBTUtils.setFloatIfPresent(tag, SerializationConstants.SCALE, scale -> prevGasScale = scale);
        NBTUtils.setIntIfPresent(tag,SerializationConstants.PROGRESS, pg -> progress = pg);
        NBTUtils.setIntIfPresent(tag,"defaultRecipeProgress", pg -> defaultRecipeProgress = pg);
        NBTUtils.setChemicalStackIfPresent(provider,tag, SerializationConstants.CHEMICAL, value -> inputTank.setStack(value));
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
