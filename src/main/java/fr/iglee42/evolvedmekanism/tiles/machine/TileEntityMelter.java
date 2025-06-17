package fr.iglee42.evolvedmekanism.tiles.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.iglee42.evolvedmekanism.registries.EMBlockTypes;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.heat.HeatAPI;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.ItemStackToGasRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerChemicalTankWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.SyntheticComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.integration.computer.computercraft.ComputerConstants;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler.ItemRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleItem;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityMelter extends TileEntityProgressMachine<ItemStackToFluidRecipe> implements ItemRecipeLookupHandler<ItemStackToFluidRecipe> {

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
          RecipeError.NOT_ENOUGH_INPUT,
          RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
          RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );

    public static final double MAX_MULTIPLIER_TEMP = 1500;
    private static final int MAX_FLUID = 16_000;

    private double biomeAmbientTemp;

    @WrappingComputerMethod(wrapper = ComputerChemicalTankWrapper.class, methodNames = {"getOutput", "getOutputCapacity", "getOutputNeeded", "getOutputFilledPercentage"}, docPlaceholder = "output tank")
    public IExtendedFluidTank fluidTank;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerHeatCapacitorWrapper.class, methodNames = "getTemperature", docPlaceholder = "generator")
    BasicHeatCapacitor heatCapacitor;

    private final IOutputHandler<@NotNull FluidStack> outputHandler;
    private final IInputHandler<@NotNull ItemStack> inputHandler;

    private MachineEnergyContainer<TileEntityMelter> energyContainer;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getInput", docPlaceholder = "input slot")
    InputInventorySlot inputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getContainerFillItem", docPlaceholder = "fillable container slot")
    FluidInventorySlot containerFillSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutputItem", docPlaceholder = "filled container output slot")
    OutputInventorySlot outputSlot;

    @ContainerSync
    @SyntheticComputerMethod(getter = "getEnvironmentalLoss")
    public double lastEnvironmentLoss;

    public TileEntityMelter(BlockPos pos, BlockState state) {
        super(EMBlocks.MELTER, pos, state, TRACKED_ERROR_TYPES, 100);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID, TransmissionType.ENERGY,TransmissionType.HEAT);
        List<IInventorySlot> inputSlots = List.of(inputSlot,containerFillSlot);
        List<IInventorySlot> outputSlots = Collections.singletonList(outputSlot);
        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false, inputSlots));
            itemConfig.addSlotInfo(DataType.OUTPUT, new InventorySlotInfo(false, true, outputSlots));
            List<IInventorySlot> ioSlots = new ArrayList<>(inputSlots);
            ioSlots.addAll(outputSlots);
            itemConfig.addSlotInfo(DataType.INPUT_OUTPUT, new InventorySlotInfo(true, true, ioSlots));
            itemConfig.setDefaults();
        }
        configComponent.setupOutputConfig(TransmissionType.FLUID, fluidTank, RelativeSide.RIGHT);
        configComponent.setupInputConfig(TransmissionType.HEAT,heatCapacitor);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.FLUID);

        inputHandler = InputHelper.getInputHandler(inputSlot, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(fluidTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @Override
    protected @Nullable IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection,this::getConfig);
        builder.addTank( fluidTank = BasicFluidTank.output(MAX_FLUID,listener));
        return builder.build();
    }


    @Override
    protected @Nullable IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, IContentsListener recipeCacheListener, CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSide(this::getDirection);
        builder.addCapacitor(heatCapacitor = BasicHeatCapacitor.create(10, 5, 100, ambientTemperature, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = InputInventorySlot.at(this::containsRecipe, recipeCacheListener, 26, 36))
              .tracksWarnings(slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(containerFillSlot = FluidInventorySlot.drain(fluidTank, listener, 155, 25));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 155, 56));
        containerFillSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        biomeAmbientTemp = HeatAPI.getAmbientTemp(level,getBlockPos());
        containerFillSlot.drainTank(outputSlot);
        recipeCacheLookupMonitor.updateAndProcess();
        lastEnvironmentLoss = simulateEnvironment();
        // update temperature
        updateHeatCapacitors(null);
        //After we update the heat capacitors, update our temperature multiplier
        // Note: We use the ambient temperature without taking our biome into account as we want to have a consistent multiplier
    }

    @NotNull
    @Override
    public IMekanismRecipeTypeProvider<ItemStackToFluidRecipe, SingleItem<ItemStackToFluidRecipe>> getRecipeType() {
        return EMRecipeType.MELTING;
    }

    @Nullable
    @Override
    public ItemStackToFluidRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler);
    }

    @NotNull
    @Override
    public CachedRecipe<ItemStackToFluidRecipe> createNewCachedRecipe(@NotNull ItemStackToFluidRecipe recipe, int cacheIndex) {
        return OneInputCachedRecipe.itemToFluid(recipe, recheckAllRecipeErrors, inputHandler, outputHandler)
              .setErrorsChanged(this::onErrorsChanged)
              .setCanHolderFunction(() -> MekanismUtils.canFunction(this) && hasMachineEnoughHeat())
              .setActive(this::setActive)
              .setRequiredTicks(this::getTicksRequired)
              .setOnFinish(this::markForSave)
              .setOperatingTicksChanged(this::setOperatingTicks);
    }

    public MachineEnergyContainer<TileEntityMelter> getEnergyContainer() {
        return energyContainer;
    }

    public boolean hasMachineEnoughHeat(){
        return heatCapacitor.getTemperature() > 1273;
    }

    @Override
    public double simulateEnvironment() {
        double currentTemperature = getTemperature(0);
        double heatCapacity = heatCapacitor.getHeatCapacity();
        if (Math.abs(currentTemperature - biomeAmbientTemp) < 0.001) {
            heatCapacitor.handleHeat(biomeAmbientTemp * heatCapacity - heatCapacitor.getHeat());
        } else {
            double incr = MekanismConfig.general.evaporationHeatDissipation.get() * Math.sqrt(Math.abs(currentTemperature - biomeAmbientTemp));
            if (currentTemperature > biomeAmbientTemp) {
                incr = -incr;
            }
            heatCapacitor.handleHeat(heatCapacity * incr);
            if (incr < 0) {
                return -incr;
            }
        }
        return 0;
    }

}
