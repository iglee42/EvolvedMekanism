package fr.iglee42.evolvedmekanism.tiles.machine;

import java.util.List;

import fr.iglee42.evolvedmekanism.interfaces.EMInputRecipeCache;
import fr.iglee42.evolvedmekanism.interfaces.FluidAlloyingCachedRecipe;
import fr.iglee42.evolvedmekanism.recipes.FluidAlloyingRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import fr.iglee42.evolvedmekanism.utils.FluidBucketHelper;
import fr.iglee42.evolvedmekanism.utils.RoutedFluidTankHolder;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerFluidTankWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.integration.computer.computercraft.ComputerConstants;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityFluidAlloyer extends TileEntityProgressMachine<FluidAlloyingRecipe> implements
        EMInputRecipeCache.TripleFluidRecipeLookupHandler<FluidAlloyingRecipe> {

    public static final RecipeError NOT_ENOUGH_TERTIARY_INPUT = RecipeError.create();
    public static final int MAX_FLUID = 16_000;
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            NOT_ENOUGH_TERTIARY_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );

    @WrappingComputerMethod(wrapper = ComputerFluidTankWrapper.class, methodNames = {"getInput", "getInputCapacity", "getInputNeeded", "getInputFilledPercentage"}, docPlaceholder = "main input tank")
    public BasicFluidTank inputTank;
    @WrappingComputerMethod(wrapper = ComputerFluidTankWrapper.class, methodNames = {"getSecondaryInput", "getSecondaryInputCapacity", "getSecondaryInputNeeded", "getSecondaryInputFilledPercentage"}, docPlaceholder = "secondary input tank")
    public BasicFluidTank extraTank;
    @WrappingComputerMethod(wrapper = ComputerFluidTankWrapper.class, methodNames = {"getTertiaryInput", "getTertiaryInputCapacity", "getTertiaryInputNeeded", "getTertiaryInputFilledPercentage"}, docPlaceholder = "tertiary input tank")
    public BasicFluidTank tertiaryTank;
    @WrappingComputerMethod(wrapper = ComputerFluidTankWrapper.class, methodNames = {"getOutput", "getOutputCapacity", "getOutputNeeded", "getOutputFilledPercentage"}, docPlaceholder = "output tank")
    public BasicFluidTank outputTank;

    private final IInputHandler<@NotNull FluidStack> inputHandler;
    private final IInputHandler<@NotNull FluidStack> extraHandler;
    private final IInputHandler<@NotNull FluidStack> tertiaryHandler;
    private final IOutputHandler<@NotNull FluidStack> outputHandler;

    private MachineEnergyContainer<TileEntityFluidAlloyer> energyContainer;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getInputItem", docPlaceholder = "main fill slot")
    FluidInventorySlot inputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getSecondaryInputItem", docPlaceholder = "secondary fill slot")
    FluidInventorySlot extraSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getTertiaryInputItem", docPlaceholder = "tertiary fill slot")
    FluidInventorySlot tertiarySlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutputItem", docPlaceholder = "output drain slot")
    FluidInventorySlot outputSlot;
    EnergyInventorySlot energySlot;

    public TileEntityFluidAlloyer(BlockPos pos, BlockState state) {
        super(EMBlocks.FLUID_ALLOYER, pos, state, TRACKED_ERROR_TYPES, 100);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID, TransmissionType.ENERGY);
        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, inputSlot, extraSlot, tertiarySlot));
            itemConfig.addSlotInfo(DataType.OUTPUT, new InventorySlotInfo(false, true, outputSlot));
            itemConfig.addSlotInfo(DataType.ENERGY, new InventorySlotInfo(true, true, energySlot));
            for (RelativeSide side : RelativeSide.values()) {
                itemConfig.setDataType(DataType.INPUT, side);
            }
            itemConfig.setDataType(DataType.OUTPUT, RelativeSide.RIGHT);
            itemConfig.setDataType(DataType.ENERGY, RelativeSide.BACK);
            itemConfig.setCanEject(true);
        }
        ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.FLUID);
        if (fluidConfig != null) {
            fluidConfig.addSlotInfo(DataType.INPUT, new FluidSlotInfo(true, false, inputTank));
            fluidConfig.addSlotInfo(DataType.EXTRA, new FluidSlotInfo(true, false, extraTank));
            fluidConfig.addSlotInfo(DataType.INPUT_2, new FluidSlotInfo(true, false, tertiaryTank));
            fluidConfig.addSlotInfo(DataType.OUTPUT, new FluidSlotInfo(false, true, outputTank));
            for (RelativeSide side : RelativeSide.values()) {
                fluidConfig.setDataType(DataType.INPUT, side);
            }
            fluidConfig.setDataType(DataType.EXTRA, RelativeSide.BACK);
            fluidConfig.setDataType(DataType.INPUT_2, RelativeSide.TOP);
            fluidConfig.setDataType(DataType.OUTPUT, RelativeSide.RIGHT);
            fluidConfig.setCanEject(true);
        }
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.FLUID);
        inputHandler = InputHelper.getInputHandler(inputTank, RecipeError.NOT_ENOUGH_INPUT);
        extraHandler = InputHelper.getInputHandler(extraTank, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        tertiaryHandler = InputHelper.getInputHandler(tertiaryTank, NOT_ENOUGH_TERTIARY_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        RoutedFluidTankHolder builder = new RoutedFluidTankHolder(this);
        builder.addInput(inputTank = BasicFluidTank.input(MAX_FLUID, this::containsRecipeA, this::containsRecipeA, recipeCacheListener));
        builder.addInput(extraTank = BasicFluidTank.input(MAX_FLUID, this::containsRecipeB, this::containsRecipeB, recipeCacheListener));
        builder.addInput(tertiaryTank = BasicFluidTank.input(MAX_FLUID, this::containsRecipeC, this::containsRecipeC, recipeCacheListener));
        builder.addOutput(outputTank = BasicFluidTank.output(MAX_FLUID, recipeCacheListener));
        return builder;
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = FluidInventorySlot.fill(inputTank, listener, 8, 79))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(extraSlot = FluidInventorySlot.fill(extraTank, listener, 30, 79))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT)));
        builder.addSlot(tertiarySlot = FluidInventorySlot.fill(tertiaryTank, listener, 52, 79))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(NOT_ENOUGH_TERTIARY_INPUT)));
        builder.addSlot(outputSlot = FluidInventorySlot.drain(outputTank, listener, 132, 79));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 153, 79));
        inputSlot.setSlotOverlay(SlotOverlay.MINUS);
        extraSlot.setSlotOverlay(SlotOverlay.MINUS);
        tertiarySlot.setSlotOverlay(SlotOverlay.MINUS);
        outputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        FluidBucketHelper.fillInPlace(inputSlot);
        FluidBucketHelper.fillInPlace(extraSlot);
        FluidBucketHelper.fillInPlace(tertiarySlot);
        FluidBucketHelper.drainInPlace(outputSlot);
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @NotNull
    @Override
    public IMekanismRecipeTypeProvider<FluidAlloyingRecipe, EMInputRecipeCache.TripleFluid<FluidAlloyingRecipe>> getRecipeType() {
        return EMRecipeType.FLUID_ALLOYING;
    }

    @Nullable
    @Override
    public FluidAlloyingRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler, extraHandler, tertiaryHandler);
    }

    @NotNull
    @Override
    public CachedRecipe<FluidAlloyingRecipe> createNewCachedRecipe(@NotNull FluidAlloyingRecipe recipe, int cacheIndex) {
        return new FluidAlloyingCachedRecipe(recipe, recheckAllRecipeErrors, inputHandler, extraHandler, tertiaryHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    public MachineEnergyContainer<TileEntityFluidAlloyer> getEnergyContainer() {
        return energyContainer;
    }

    @ComputerMethod(methodDescription = ComputerConstants.DESCRIPTION_GET_ENERGY_USAGE)
    FloatingLong getEnergyUsage() {
        return getActive() ? energyContainer.getEnergyPerTick() : FloatingLong.ZERO;
    }
}
