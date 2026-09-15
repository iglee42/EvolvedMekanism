package fr.iglee42.evolvedmekanism.recipeviewers.emi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.recipes.MeltingRecipe;
import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes.APTEmiRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes.AlloyerEmiRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes.ChemixerEmiRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes.MeltingEmiRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes.SolidificationEmiRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IItemProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.block.BlockOre;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeFactoryType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.ModList;

@EmiEntrypoint
public class EMEmi implements EmiPlugin {

    public static final EMEmiRecipeCategory ALLOYING = category("alloying", EMBlocks.ALLOYER, -28, -16, 144, 54);
    public static final EMEmiRecipeCategory CHEMIXING = category("chemixing", EMBlocks.CHEMIXER, -28, -13, 144, 60);
    public static final EMEmiRecipeCategory MELTING = category("melting", EMBlocks.MELTER, -20, -12, 132, 62);
    public static final EMEmiRecipeCategory SOLIDIFICATION = category("solidification", EMBlocks.SOLIDIFIER, -3, -10, 170, 60);
    public static final EMEmiRecipeCategory APT = new EMEmiRecipeCategory(EvolvedMekanism.rl("apt"), EmiStack.of(EMItems.BETTER_GOLD_INGOT.getItemStack()), EvolvedMekanismLang.APT.translate(), -3, -12, 168, 74);

    private static final Comparison MEKANISM_COMPARISON = Comparison.compareData(emiStack -> {
        Set<Object> representation = new HashSet<>();
        ItemStack stack = emiStack.getItemStack();
        addChemicalComponent(representation, stack, Capabilities.GAS_HANDLER);
        addChemicalComponent(representation, stack, Capabilities.INFUSION_HANDLER);
        addChemicalComponent(representation, stack, Capabilities.PIGMENT_HANDLER);
        addChemicalComponent(representation, stack, Capabilities.SLURRY_HANDLER);
        addFluidComponent(representation, stack);
        addEnergyComponent(representation, stack);
        return representation.isEmpty() ? null : representation;
    });

    private static EMEmiRecipeCategory category(String path, IItemProvider icon, int xOffset, int yOffset, int width, int height) {
        return new EMEmiRecipeCategory(EvolvedMekanism.rl(path), EmiStack.of(icon.getItemStack()), icon.getTextComponent(), xOffset, yOffset, width, height);
    }

    private static void addChemicalComponent(Set<Object> representation, ItemStack stack, Capability<? extends IChemicalHandler<?, ?>> capability) {
        Optional<? extends IChemicalHandler<?, ?>> cap = stack.getCapability(capability).resolve();
        if (cap.isPresent()) {
            IChemicalHandler<?, ?> handler = cap.get();
            int tanks = handler.getTanks();
            if (tanks == 1) {
                ChemicalStack<?> chemicalStack = handler.getChemicalInTank(0);
                if (!chemicalStack.isEmpty()) {
                    representation.add(chemicalStack.getType());
                }
            } else if (tanks > 1) {
                List<Object> chemicals = new ArrayList<>(tanks);
                for (int tank = 0; tank < tanks; tank++) {
                    chemicals.add(handler.getChemicalInTank(tank).getType());
                }
                representation.add(chemicals);
            }
        }
    }

    private static void addFluidComponent(Set<Object> representation, ItemStack stack) {
        Optional<IFluidHandlerItem> cap = FluidUtil.getFluidHandler(stack).resolve();
        if (cap.isPresent()) {
            IFluidHandlerItem handler = cap.get();
            int tanks = handler.getTanks();
            if (tanks == 1) {
                FluidStack fluidStack = handler.getFluidInTank(0);
                if (!fluidStack.isEmpty()) {
                    representation.add(fluidStack);
                }
            } else if (tanks > 1) {
                List<FluidStack> fluids = new ArrayList<>(tanks);
                for (int tank = 0; tank < tanks; tank++) {
                    fluids.add(handler.getFluidInTank(tank));
                }
                representation.add(fluids);
            }
        }
    }

    private static void addEnergyComponent(Set<Object> representation, ItemStack stack) {
        Optional<IStrictEnergyHandler> capability = stack.getCapability(Capabilities.STRICT_ENERGY).resolve();
        if (capability.isPresent()) {
            IStrictEnergyHandler energyHandlerItem = capability.get();
            int containers = energyHandlerItem.getEnergyContainerCount();
            if (containers == 1) {
                if (energyHandlerItem.getNeededEnergy(0).isZero()) {
                    representation.add("filled");
                }
            } else if (containers > 1) {
                StringBuilder component = new StringBuilder();
                for (int container = 0; container < containers; container++) {
                    FloatingLong neededEnergy = energyHandlerItem.getNeededEnergy(container);
                    component.append(neededEnergy.isZero() ? "filled" : "empty");
                }
                representation.add(component.toString());
            }
        }
    }

    @Override
    public void register(EmiRegistry registry) {
        addCategoryAndRecipes(registry, ALLOYING, EMRecipeType.ALLOYING, AlloyerEmiRecipe::new, EMBlocks.ALLOYER);
        addCategoryAndRecipes(registry, CHEMIXING, EMRecipeType.CHEMIXING, ChemixerEmiRecipe::new, EMBlocks.CHEMIXER);
        addCategoryAndRecipes(registry, MELTING, EMRecipeType.MELTING, MeltingEmiRecipe::new, EMBlocks.MELTER);
        addCategoryAndRecipes(registry, SOLIDIFICATION, EMRecipeType.SOLIDIFICATION, SolidificationEmiRecipe::new, EMBlocks.SOLIDIFIER);

        registry.addCategory(APT);
        addWorkstations(registry, APT, EMItems.BETTER_GOLD_INGOT, EMBlocks.APT_CASING, EMBlocks.APT_PORT, EMBlocks.SUPERCHARGING_ELEMENT);
        for (ItemStackGasToItemStackRecipe recipe : EMRecipeType.APT.getRecipes(null)) {
            registry.addRecipe(new APTEmiRecipe(APT, recipe));
        }

        registerItemSubtypes(registry, EMItems.ITEMS.getAllItems());
        registerItemSubtypes(registry, EMBlocks.BLOCKS.getAllBlocks());

        List<EmiStack> toRemove = new ArrayList<>();
        EMFluids.FLUIDS.getAllFluids().forEach(ro -> {
            boolean hasMelting = EMRecipeType.MELTING.getRecipes(null).stream().anyMatch(r -> r.getOutputDefinition().stream().anyMatch(s -> s.getFluid().equals(ro.getFluid())));
            boolean hasSolidifying = EMRecipeType.SOLIDIFICATION.getRecipes(null).stream().anyMatch(r -> r.getInputFluid().test(new FluidStack(ro.getFluid(), (int) r.getInputFluid().getNeededAmount(new FluidStack(ro.getFluid(), 1)))));
            if (!hasMelting && !hasSolidifying) {
                toRemove.add(EmiStack.of(ro.getFluid()));
                toRemove.add(EmiStack.of(ro.getFluid().getBucket()));
            }
        });
        EMBlocks.BLOCKS.getAllBlocks().stream().filter(b -> b.getBlock() instanceof BlockOre).forEach(b -> {
            String stone = b.getRegistryName().getPath().split("_")[0].toLowerCase();
            if (stone.equals("holystone") && !ModList.get().isLoaded("aether")) {
                toRemove.add(EmiStack.of(b.getBlock()));
            }
            if ((stone.equals("depthrock") || stone.equals("shiverstone")) && !ModList.get().isLoaded("undergarden")) {
                toRemove.add(EmiStack.of(b.getBlock()));
            }
        });
        EMItems.ITEMS.getAllItems().stream().filter(i -> i.getRegistryName().getPath().startsWith("mold_")).forEach(m -> {
            boolean used = EMRecipeType.SOLIDIFICATION.getRecipes(null).stream().anyMatch(r -> r.getInputSolid().getRepresentations().stream().anyMatch(s -> s.getItem().equals(m.asItem())));
            if (!used) {
                toRemove.add(EmiStack.of(m.asItem()));
            }
        });
        toRemove.forEach(registry::removeEmiStacks);
    }

    public static void registerItemSubtypes(EmiRegistry registry, List<? extends IItemProvider> itemProviders) {
        for (IItemProvider itemProvider : itemProviders) {
            ItemStack itemStack = itemProvider.getItemStack();
            if (itemStack.getCapability(Capabilities.STRICT_ENERGY).isPresent() || itemStack.getCapability(Capabilities.GAS_HANDLER).isPresent() ||
                    itemStack.getCapability(Capabilities.INFUSION_HANDLER).isPresent() || itemStack.getCapability(Capabilities.PIGMENT_HANDLER).isPresent() ||
                    itemStack.getCapability(Capabilities.SLURRY_HANDLER).isPresent() || FluidUtil.getFluidHandler(itemStack).isPresent()) {
                registry.setDefaultComparison(itemStack.getItem(), MEKANISM_COMPARISON);
            }
        }
    }

    private static <RECIPE extends MekanismRecipe> void addCategoryAndRecipes(EmiRegistry registry, EMEmiRecipeCategory category,
                                                                              IMekanismRecipeTypeProvider<RECIPE, ?> recipeType,
                                                                              BiFunction<EMEmiRecipeCategory, RECIPE, EmiRecipe> recipeCreator,
                                                                              IItemProvider... workstations) {
        registry.addCategory(category);
        addWorkstations(registry, category, workstations);
        for (RECIPE recipe : recipeType.getRecipes(null)) {
            registry.addRecipe(recipeCreator.apply(category, recipe));
        }
    }

    private static void addWorkstations(EmiRegistry registry, EmiRecipeCategory category, IItemProvider... workstations) {
        for (IItemProvider workstation : workstations) {
            registry.addWorkstation(category, EmiStack.of(workstation.getItemStack()));
            if (workstation.asItem() instanceof BlockItem blockItem) {
                AttributeFactoryType factoryType = Attribute.get(blockItem.getBlock(), AttributeFactoryType.class);
                if (factoryType != null) {
                    for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
                        registry.addWorkstation(category, EmiStack.of(MekanismBlocks.getFactory(tier, factoryType.getFactoryType()).getItemStack()));
                    }
                }
            }
        }
    }
}
