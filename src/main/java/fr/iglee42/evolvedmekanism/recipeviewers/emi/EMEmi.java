package fr.iglee42.evolvedmekanism.recipeviewers.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiRegistryAdapter;
import dev.emi.emi.api.stack.EmiStack;
import fr.iglee42.evolvedmekanism.recipes.MeltingRecipe;
import fr.iglee42.evolvedmekanism.recipeviewers.EMRecipeViewersTypes;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.categories.APTEMIRecipeCategory;
import fr.iglee42.evolvedmekanism.recipeviewers.emi.recipes.*;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.api.recipes.ItemStackChemicalToItemStackRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.client.recipe_viewer.emi.*;
import mekanism.client.recipe_viewer.emi.recipe.ItemStackToFluidEmiRecipe;
import mekanism.client.recipe_viewer.emi.recipe.MekanismEmiRecipe;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mekanism.common.attachments.containers.ContainerType;
import mekanism.common.block.BlockOre;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeFactoryType;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.*;
import java.util.function.BiFunction;

@EmiEntrypoint
public class EMEmi implements EmiPlugin {
    private static final ChemicalEmiIngredientSerializer CHEMICAL_SERIALIZER = new ChemicalEmiIngredientSerializer();
    private static final EmiRegistryAdapter<Chemical> CHEMICAL_REGISTRY_ADAPTER = EmiRegistryAdapter.simple(Chemical.class, MekanismAPI.CHEMICAL_REGISTRY, ChemicalEmiStack::new);

    private static final Comparison MEKANISM_COMPARISON = Comparison.compareData(emiStack -> {
        Set<Object> representation = new HashSet<>();
        ItemStack stack = emiStack.getItemStack();
        addChemicalComponent(representation, stack);
        addFluidComponent(representation, stack);
        addEnergyComponent(representation, stack);
        if (!representation.isEmpty()) {
            return representation;
        }
        return null;
    });

    private static void addChemicalComponent(Set<Object> representation, ItemStack stack) {
        IChemicalHandler handler = ContainerType.CHEMICAL.createHandlerIfData(stack);
        if (handler == null) {
            handler = stack.getCapability(Capabilities.CHEMICAL.item());
        }
        if (handler != null) {
            int tanks = handler.getChemicalTanks();
            if (tanks == 1) {
                ChemicalStack chemicalStack = handler.getChemicalInTank(0);
                if (!chemicalStack.isEmpty()) {
                    representation.add(chemicalStack.getChemical());
                }
            } else if (tanks > 1) {
                List<Chemical> chemicals = new ArrayList<>(tanks);
                for (int tank = 0; tank < tanks; tank++) {
                    chemicals.add(handler.getChemicalInTank(tank).getChemical());
                }
                representation.add(chemicals);
            }
        }
    }

    private static void addFluidComponent(Set<Object> representation, ItemStack stack) {
        IFluidHandler handler = ContainerType.FLUID.createHandlerIfData(stack);
        if (handler == null) {
            handler = Capabilities.FLUID.getCapability(stack);
        }
        if (handler != null) {
            int tanks = handler.getTanks();
            if (tanks == 1) {
                FluidStack fluidStack = handler.getFluidInTank(0);
                if (!fluidStack.isEmpty()) {
                    //Equals and hashcode ignore the count, so we can just add the fluid stack
                    representation.add(fluidStack);
                }
            } else if (tanks > 1) {
                List<FluidStack> fluids = new ArrayList<>(tanks);
                for (int tank = 0; tank < tanks; tank++) {
                    //Equals and hashcode ignore the count, so we can just add the fluid stack
                    fluids.add(handler.getFluidInTank(tank));
                }
                representation.add(fluids);
            }
        }
    }

    private static void addEnergyComponent(Set<Object> representation, ItemStack stack) {
        IStrictEnergyHandler energyHandlerItem = ContainerType.ENERGY.createHandlerIfData(stack);
        if (energyHandlerItem == null) {
            energyHandlerItem = Capabilities.STRICT_ENERGY.getCapability(stack);
        }
        if (energyHandlerItem != null) {
            int containers = energyHandlerItem.getEnergyContainerCount();
            if (containers == 1) {
                long neededEnergy = energyHandlerItem.getNeededEnergy(0);
                if (neededEnergy == 0L) {
                    representation.add("filled");
                }
            } else if (containers > 1) {
                StringBuilder component = new StringBuilder();
                for (int container = 0; container < containers; container++) {
                    long neededEnergy = energyHandlerItem.getNeededEnergy(container);
                    if (neededEnergy == 0L) {
                        component.append("filled");
                    } else {
                        component.append("empty");
                    }
                }
                representation.add(component.toString());
            }
        }
    }

    @Override
    public void initialize(EmiInitRegistry registry) {
        registry.addIngredientSerializer(ChemicalEmiStack.class, CHEMICAL_SERIALIZER);
        registry.addRegistryAdapter(CHEMICAL_REGISTRY_ADAPTER);
    }

    @Override
    public void register(EmiRegistry registry) {

        //Note: We have to add these as generic and then instance check the class so that we can have them be generic across our classes
        registry.addGenericExclusionArea(new EmiExclusionHandler());
        registry.addGenericDragDropHandler(new EmiGhostIngredientHandler());
        registry.addGenericStackProvider(new EmiStackUnderMouseProvider());
        addCategories(registry);
        addWorkstations(registry, MekanismEmiRecipeCategory.create(RecipeViewerRecipeType.ACTIVATING),List.of(EMBlocks.LUNAR_NEUTRON_ACTIVATOR.get()));

        registerItemSubtypes(registry, EMItems.ITEMS.getEntries());
        registerItemSubtypes(registry, EMBlocks.BLOCKS.getSecondaryEntries());

        List<Fluid> fluidsToRemove = new ArrayList<>();
        List<ItemLike> itemsToRemove = new ArrayList<>();
        EMFluids.FLUIDS.getFluidEntries().stream().filter(ro->ro.getId().getPath().startsWith("molten_")).forEach(ro->{
            boolean hasMelting = EMRecipeType.MELTING.getRecipes((Level) null).stream().anyMatch(r->r.value().getOutput(ItemStack.EMPTY).getFluid().equals(ro.get()));
            boolean hasSolidifying = EMRecipeType.SOLIDIFICATION.getRecipes((Level) null).stream().anyMatch(r->r.value().getInputFluid().test(new FluidStack(ro.get(), (int) r.value().getInputFluid().getNeededAmount(new FluidStack(ro.get(),1)))));
            if (!hasMelting && !hasSolidifying){
                fluidsToRemove.add(ro.get());
                if (ro.get() instanceof BaseFlowingFluid.Source) itemsToRemove.add(ro.get().getBucket());
            }
        });
        EMBlocks.BLOCKS.getPrimaryEntries().stream().filter(b->b.get() instanceof BlockOre).forEach(b->{
            String stone = b.getId().getPath().split("_")[0].toLowerCase();
            if (stone.equals("holystone") && !ModList.get().isLoaded("aether")) {
                itemsToRemove.add(b.get());
            }
            if ((stone.equals("depthrock") || stone.equals("shiverstone")) && !ModList.get().isLoaded("undergarden")) {
                itemsToRemove.add(b.get());
            }
        });
        EMItems.ITEMS.getEntries().stream().filter(i->i.getId().getPath().startsWith("mold_")).forEach(m->{
            boolean used = EMRecipeType.SOLIDIFICATION.getRecipes((Level) null).stream().anyMatch(r->r.value().getInputSolid().getRepresentations().stream().anyMatch(s->s.getItem().equals(m.get())));
            if (!used) itemsToRemove.add(m.get());
        });

        fluidsToRemove.stream().map(EmiStack::of).forEach(registry::removeEmiStacks);
        itemsToRemove.stream().map(EmiStack::of).forEach(registry::removeEmiStacks);
    }

    public static void registerItemSubtypes(EmiRegistry registry, Collection<? extends Holder<Item>> items) {
        for (Holder<Item> item : items) {
            //Handle items
            ItemStack stack = new ItemStack(item);
            if (Capabilities.STRICT_ENERGY.hasCapability(stack) || Capabilities.CHEMICAL.hasCapability(stack) || Capabilities.FLUID.hasCapability(stack)) {
                registry.setDefaultComparison(stack.getItem(), MEKANISM_COMPARISON);
            }
        }
    }


    private void addCategories(EmiRegistry registry) {
        addCategoryAndRecipes(registry, EMRecipeViewersTypes.ALLOYING, AlloyerEMIRecipe::new);
        addCategoryAndRecipes(registry, EMRecipeViewersTypes.CHEMIXING, ChemixerEMIRecipe::new);
        addCategoryAndRecipes(registry, EMRecipeViewersTypes.SOLIDIFICATION, SolidificationEMIRecipe::new);

        MekanismEmiRecipeCategory melting = addCategory(registry,EMRecipeViewersTypes.MELTING);
        for (RecipeHolder<MeltingRecipe> recipe : EMRecipeType.MELTING.getRecipes()){
            registry.addRecipe(new MeltingEmiRecipe(melting, recipe));
        }

        APTEMIRecipeCategory aptCategory = APTEMIRecipeCategory.create(EMRecipeViewersTypes.APT);
        registry.addCategory(aptCategory);
        addWorkstations(registry, aptCategory, EMRecipeViewersTypes.APT.workstations());
        for (RecipeHolder<ItemStackChemicalToItemStackRecipe> recipe : EMRecipeViewersTypes.APT.getRecipes(registry.getRecipeManager())) {
            registry.addRecipe(new APTEMIRecipe(aptCategory, recipe));
        }
    }

    public static <RECIPE extends MekanismRecipe<?>, TYPE extends IRecipeViewerRecipeType<RECIPE> & IMekanismRecipeTypeProvider<?, RECIPE, ?>> void addCategoryAndRecipes(
            EmiRegistry registry, TYPE recipeType, BiFunction<MekanismEmiRecipeCategory, RecipeHolder<RECIPE>, MekanismEmiRecipe<RECIPE>> recipeCreator) {
        MekanismEmiRecipeCategory category = addCategory(registry, recipeType);
        for (RecipeHolder<RECIPE> recipe : recipeType.getRecipes(registry.getRecipeManager())) {
            registry.addRecipe(recipeCreator.apply(category, recipe));
        }
    }

    public static <RECIPE> void addCategoryAndRecipes(EmiRegistry registry, IRecipeViewerRecipeType<RECIPE> recipeType, MekanismEmi.BasicRecipeCreator<RECIPE> recipeCreator,
                                                      Map<ResourceLocation, RECIPE> recipes) {
        MekanismEmiRecipeCategory category = addCategory(registry, recipeType);
        for (Map.Entry<ResourceLocation, RECIPE> entry : recipes.entrySet()) {
            registry.addRecipe(recipeCreator.create(category, entry.getKey(), entry.getValue()));
        }
    }

    public static <RECIPE extends INamedRVRecipe> void addCategoryAndRecipes(EmiRegistry registry, IRecipeViewerRecipeType<RECIPE> recipeType,
                                                                             MekanismEmi.BasicRecipeCreator<RECIPE> recipeCreator, List<RECIPE> recipes) {
        MekanismEmiRecipeCategory category = addCategory(registry, recipeType);
        for (RECIPE recipe : recipes) {
            registry.addRecipe(recipeCreator.create(category, recipe.id(), recipe));
        }
    }

    private static MekanismEmiRecipeCategory addCategory(EmiRegistry registry, IRecipeViewerRecipeType<?> recipeType) {
        MekanismEmiRecipeCategory category = MekanismEmiRecipeCategory.create(recipeType);
        registry.addCategory(category);
        addWorkstations(registry, category, recipeType.workstations());
        return category;
    }

    private static void addWorkstations(EmiRegistry registry, EmiRecipeCategory category, List<ItemLike> workstations) {
        for (ItemLike workstation : workstations) {
            Item item = workstation.asItem();
            registry.addWorkstation(category, EmiStack.of(item));
            if (item instanceof BlockItem blockItem) {
                AttributeFactoryType factoryType = Attribute.get(blockItem.getBlock(), AttributeFactoryType.class);
                if (factoryType != null) {
                    for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
                        registry.addWorkstation(category, EmiStack.of(EMBlocks.getFactory(tier, factoryType.getFactoryType())));
                    }
                }
            }
        }
    }

}
