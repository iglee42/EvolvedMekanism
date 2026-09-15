package fr.iglee42.evolvedmekanism.tiles;

import java.util.List;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerChemicalTankWrapper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.SyntheticComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler.ChemicalRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.interfaces.IBoundingBlock;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileEntityLunarNeutronActivator extends TileEntityRecipeMachine<GasToGasRecipe> implements IBoundingBlock, ChemicalRecipeLookupHandler<Gas, GasStack, GasToGasRecipe> {

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    public static final long MAX_GAS = 10L * FluidType.BUCKET_VOLUME;

    @WrappingComputerMethod(wrapper = ComputerChemicalTankWrapper.class, methodNames = {"getInput", "getInputCapacity", "getInputNeeded",
            "getInputFilledPercentage"}, docPlaceholder = "input tank")
    public IGasTank inputTank;
    @WrappingComputerMethod(wrapper = ComputerChemicalTankWrapper.class, methodNames = {"getOutput", "getOutputCapacity", "getOutputNeeded",
            "getOutputFilledPercentage"}, docPlaceholder = "output tank")
    public IGasTank outputTank;

    @SyntheticComputerMethod(getter = "getPeakProductionRate")
    private float peakProductionRate;
    @SyntheticComputerMethod(getter = "getProductionRate")
    private float productionRate;
    private boolean settingsChecked;
    private boolean needsRainCheck;

    private final IOutputHandler<@NotNull GasStack> outputHandler;
    private final IInputHandler<@NotNull GasStack> inputHandler;

    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getInputItem", docPlaceholder = "input slot")
    GasInventorySlot inputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutputItem", docPlaceholder = "output slot")
    GasInventorySlot outputSlot;

    public TileEntityLunarNeutronActivator(BlockPos pos, BlockState state) {
        super(EMBlocks.LUNAR_NEUTRON_ACTIVATOR, pos, state, TRACKED_ERROR_TYPES);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.GAS);
        configComponent.setupIOConfig(TransmissionType.ITEM, inputSlot, outputSlot, RelativeSide.FRONT);
        configComponent.setupIOConfig(TransmissionType.GAS, inputTank, outputTank, RelativeSide.FRONT);
        configComponent.addDisabledSides(RelativeSide.TOP);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.GAS)
                .setCanTankEject(tank -> tank != inputTank);
        inputHandler = InputHelper.getInputHandler(inputTank, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @NotNull
    @Override
    public IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper.forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTank = ChemicalTankBuilder.GAS.create(MAX_GAS,
                (gas, type) -> type != AutomationType.EXTERNAL || outputTank.isEmpty(),
                (gas, automationType) -> containsRecipe(gas),
                this::containsRecipe,
                ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener));
        builder.addTank(outputTank = ChemicalTankBuilder.GAS.output(MAX_GAS, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlot = GasInventorySlot.fill(inputTank, listener, 5, 56));
        builder.addSlot(outputSlot = GasInventorySlot.drain(outputTank, listener, 155, 56));
        inputSlot.setSlotType(ContainerSlotType.INPUT);
        inputSlot.setSlotOverlay(SlotOverlay.MINUS);
        outputSlot.setSlotType(ContainerSlotType.OUTPUT);
        outputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    private void recheckSettings() {
        Level world = getLevel();
        if (world == null) {
            return;
        }
        BlockPos pos = getBlockPos();
        Biome b = world.getBiomeManager().getBiome(pos).value();
        needsRainCheck = b.getPrecipitationAt(pos) != Precipitation.NONE;
        float tempEff = 0.3F * (0.8F - b.getBaseTemperature());
        float humidityEff = needsRainCheck ? -0.3F * b.getModifiedClimateSettings().downfall() : 0.0F;
        peakProductionRate = MekanismConfig.general.maxSolarNeutronActivatorRate.get() * (1.0F + tempEff + humidityEff);
        settingsChecked = true;
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        if (!settingsChecked) {
            recheckSettings();
        }
        inputSlot.fillTank();
        outputSlot.drainTank();
        productionRate = recalculateProductionRate();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @NotNull
    @Override
    public IMekanismRecipeTypeProvider<GasToGasRecipe, SingleChemical<Gas, GasStack, GasToGasRecipe>> getRecipeType() {
        return MekanismRecipeType.ACTIVATING;
    }

    @Nullable
    @Override
    public GasToGasRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler);
    }

    @ComputerMethod
    boolean canSeeMoon() {
        return level != null && level.dimensionType().hasSkyLight() && level.canSeeSky(worldPosition.above()) && level.isNight();
    }

    private boolean canOperate() {
        return MekanismUtils.canFunction(this) && canSeeMoon();
    }

    private float recalculateProductionRate() {
        Level world = getLevel();
        if (world == null || !canOperate()) {
            return 0;
        }
        float brightness = 1.0F - WorldUtils.getSunBrightness(world, 1.0F);
        float production = peakProductionRate * brightness;
        if (needsRainCheck && (world.isRaining() || world.isThundering())) {
            production *= 0.2F;
        }
        return production;
    }

    @NotNull
    @Override
    public CachedRecipe<GasToGasRecipe> createNewCachedRecipe(@NotNull GasToGasRecipe recipe, int cacheIndex) {
        return OneInputCachedRecipe.chemicalToChemical(recipe, recheckAllRecipeErrors, inputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(this::canOperate)
                .setActive(this::setActive)
                .setOnFinish(this::markForSave)
                .setRequiredTicks(() -> productionRate > 0 && productionRate < 1 ? Mth.ceil(1 / productionRate) : 1)
                .setBaselineMaxOperations(() -> productionRate > 0 && productionRate < 1 ? 1 : (int) productionRate);
    }

    @Override
    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(inputTank.getStored(), inputTank.getCapacity());
    }
}
