package fr.iglee42.evolvedmekanism.tiles.machine;

import fr.iglee42.evolvedmekanism.interfaces.EMInputRecipeCache;
import fr.iglee42.evolvedmekanism.interfaces.ThreeInputCachedRecipe;
import fr.iglee42.evolvedmekanism.recipes.FluidAlloyingRecipe;
import fr.iglee42.evolvedmekanism.recipes.vanilla_input.TriFluidRecipeInput;
import fr.iglee42.evolvedmekanism.recipeviewers.EMRecipeViewersTypes;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import fr.iglee42.evolvedmekanism.utils.FluidBucketHelper;
import fr.iglee42.evolvedmekanism.utils.RoutedFluidTankHolder;
import mekanism.api.IContentsListener;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerFluidTankWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.ITripleRecipeLookupHandler;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileEntityFluidAlloyer extends TileEntityProgressMachine<FluidAlloyingRecipe> implements
        ITripleRecipeLookupHandler<FluidStack, FluidStack, FluidStack, FluidAlloyingRecipe, EMInputRecipeCache.TripleFluid<FluidAlloyingRecipe>> {

    public static final int MAX_FLUID = 16_000;
    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
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
    FluidInventorySlot outputSlot;
    EnergyInventorySlot energySlot;

    public TileEntityFluidAlloyer(BlockPos pos, BlockState state) {
        super(EMBlocks.FLUID_ALLOYER, pos, state, TRACKED_ERROR_TYPES, 100);
        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, inputSlot));
            itemConfig.addSlotInfo(DataType.EXTRA, new InventorySlotInfo(true, false, extraSlot));
            itemConfig.addSlotInfo(DataType.INPUT_2, new InventorySlotInfo(true, false, tertiarySlot));
            itemConfig.addSlotInfo(DataType.ENERGY, new InventorySlotInfo(true, true, energySlot));
        }
        ConfigInfo fluidConfig = configComponent.getConfig(TransmissionType.FLUID);
        if (fluidConfig != null) {
            fluidConfig.addSlotInfo(DataType.INPUT, new FluidSlotInfo(true, false, inputTank));
            fluidConfig.addSlotInfo(DataType.EXTRA, new FluidSlotInfo(true, false, extraTank));
            fluidConfig.addSlotInfo(DataType.INPUT_2, new FluidSlotInfo(true, false, tertiaryTank));
            fluidConfig.addSlotInfo(DataType.OUTPUT, new FluidSlotInfo(false, true, outputTank));
        }
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.FLUID);
        inputHandler = InputHelper.getInputHandler(inputTank, RecipeError.NOT_ENOUGH_INPUT);
        extraHandler = InputHelper.getInputHandler(extraTank, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        tertiaryHandler = InputHelper.getInputHandler(tertiaryTank, RecipeError.NOT_ENOUGH_SECONDARY_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener unpause) {
        RoutedFluidTankHolder builder = new RoutedFluidTankHolder(this);
        builder.addInput(inputTank = BasicFluidTank.input(MAX_FLUID, this::containsRecipeA, this::containsRecipeA, recipeCacheListener));
        builder.addInput(extraTank = BasicFluidTank.input(MAX_FLUID, this::containsRecipeB, this::containsRecipeB, recipeCacheListener));
        builder.addInput(tertiaryTank = BasicFluidTank.input(MAX_FLUID, this::containsRecipeC, this::containsRecipeC, recipeCacheListener));
        builder.addOutput(outputTank = BasicFluidTank.output(MAX_FLUID, unpause));
        return builder;
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener unpause) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, unpause));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener unpause) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        builder.addSlot(inputSlot = FluidInventorySlot.fill(inputTank, listener, 8, 79));
        builder.addSlot(extraSlot = FluidInventorySlot.fill(extraTank, listener, 30, 79));
        builder.addSlot(tertiarySlot = FluidInventorySlot.fill(tertiaryTank, listener, 52, 79));
        builder.addSlot(outputSlot = FluidInventorySlot.drain(outputTank, listener, 132, 79));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 153, 79));
        inputSlot.setSlotOverlay(SlotOverlay.MINUS);
        extraSlot.setSlotOverlay(SlotOverlay.MINUS);
        tertiarySlot.setSlotOverlay(SlotOverlay.MINUS);
        outputSlot.setSlotOverlay(SlotOverlay.PLUS);
        inputSlot.tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        extraSlot.tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT)));
        tertiarySlot.tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(RecipeError.NOT_ENOUGH_SECONDARY_INPUT)));
        return builder.build();
    }

    @Override
    protected boolean onUpdateServer() {
        boolean send = super.onUpdateServer();
        FluidBucketHelper.fillInPlace(inputSlot);
        FluidBucketHelper.fillInPlace(extraSlot);
        FluidBucketHelper.fillInPlace(tertiarySlot);
        FluidBucketHelper.drainInPlace(outputSlot);
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
        return send;
    }

    @NotNull
    @Override
    public IMekanismRecipeTypeProvider<TriFluidRecipeInput, FluidAlloyingRecipe, EMInputRecipeCache.TripleFluid<FluidAlloyingRecipe>> getRecipeType() {
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
        return ThreeInputCachedRecipe.fluidAlloyer(recipe, recheckAllRecipeErrors, inputHandler, extraHandler, tertiaryHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(this::canFunction)
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    public MachineEnergyContainer<TileEntityFluidAlloyer> getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public @Nullable IRecipeViewerRecipeType<FluidAlloyingRecipe> recipeViewerType() {
        return EMRecipeViewersTypes.FLUID_ALLOYING;
    }
}
