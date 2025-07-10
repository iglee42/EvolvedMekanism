package fr.iglee42.evolvedmekanism.jei;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.impl.BasicItemStackToFluidRecipe;
import fr.iglee42.evolvedmekanism.jei.categories.*;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.recipes.ItemStackChemicalToItemStackRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.client.recipe_viewer.jei.CatalystRegistryHelper;
import mekanism.client.recipe_viewer.jei.MekanismJEI;
import mekanism.client.recipe_viewer.jei.MekanismSubtypeInterpreter;
import mekanism.client.recipe_viewer.jei.RecipeRegistryHelper;
import mekanism.client.recipe_viewer.type.RVRecipeTypeWrapper;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismItems;
import mekanism.common.item.block.machine.ItemBlockFluidTank;
import mekanism.common.util.RegistryUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.registration.*;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

@JeiPlugin
public class EMJEI implements IModPlugin {


    private static final ISubtypeInterpreter<ItemStack> MEKANISM_DATA_INTERPRETER = new MekanismSubtypeInterpreter();


    @NotNull
    @Override
    public ResourceLocation getPluginUid() {
        return EvolvedMekanism.rl("jei_plugin");
    }

    @Override
    @SuppressWarnings("RedundantTypeArguments")
    public void registerIngredients(IModIngredientRegistration registry) {
    }


    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new AlloyerRecipeCategory(guiHelper, JEIRecipeTypes.ALLOYING));
        registry.addRecipeCategories(new ChemixerRecipeCategory(guiHelper, JEIRecipeTypes.CHEMIXING));
        registry.addRecipeCategories(new APTRecipeCategory(guiHelper, JEIRecipeTypes.APT));
        registry.addRecipeCategories(new ItemStackToFluidRecipeCategory(guiHelper, JEIRecipeTypes.MELTING,false));
        registry.addRecipeCategories(new SolidificationRecipeCategory(guiHelper, JEIRecipeTypes.SOLIDIFICATION));

    }

    public static void registerItemSubtypes(ISubtypeRegistration registry, Collection<? extends Holder<Item>> items) {
        for (Holder<Item> item : items) {
            //Handle items
            ItemStack stack = new ItemStack(item);
            if (Capabilities.STRICT_ENERGY.hasCapability(stack) || Capabilities.CHEMICAL.hasCapability(stack) || Capabilities.FLUID.hasCapability(stack)) {
                registry.registerSubtypeInterpreter(stack.getItem(), MEKANISM_DATA_INTERPRETER);
            }
        }
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registry) {
        registerItemSubtypes(registry, EMItems.ITEMS.getEntries());
        registerItemSubtypes(registry, EMBlocks.BLOCKS.getSecondaryEntries());
    }


    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry) {

    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        EMFluids.FLUIDS.getFluidEntries().forEach(ro->{
            boolean hasMelting = EMRecipeType.MELTING.getRecipes(null).stream().anyMatch(r->r.value().getOutput(ItemStack.EMPTY).getFluid().equals(ro.get()));
            boolean hasSolidifying = EMRecipeType.SOLIDIFICATION.getRecipes(null).stream().anyMatch(r->r.value().getInputFluid().test(new FluidStack(ro.get(), (int) r.value().getInputFluid().getNeededAmount(new FluidStack(ro.get(),1)))));
            if (!hasMelting && !hasSolidifying){
                registry.getJeiHelpers().getIngredientManager().removeIngredientsAtRuntime(NeoForgeTypes.FLUID_STACK, List.of(new FluidStack(ro.get(),1000)));
                registry.getJeiHelpers().getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, List.of(ro.get().getBucket().getDefaultInstance()));
            }
        });
        RecipeRegistryHelper.register(registry, JEIRecipeTypes.ALLOYING, EMRecipeType.ALLOYING);
        RecipeRegistryHelper.register(registry, JEIRecipeTypes.CHEMIXING, EMRecipeType.CHEMIXING);
        RecipeRegistryHelper.register(registry, JEIRecipeTypes.APT, EMRecipeType.APT);
        RecipeRegistryHelper.register(registry, JEIRecipeTypes.MELTING, EMRecipeType.MELTING);
        RecipeRegistryHelper.register(registry, JEIRecipeTypes.SOLIDIFICATION, EMRecipeType.SOLIDIFICATION);

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        //CatalystRegistryHelper.register(registry, MekanismJEI.genericRecipeType(JEIRecipeTypes.ALLOYING), Arrays.asList(EMBlocks.ALLOYER));
        CatalystRegistryHelper.register(registry,MekanismJEI.genericRecipeType(JEIRecipeTypes.CHEMIXING),Arrays.asList(EMBlocks.CHEMIXER));
        CatalystRegistryHelper.register(registry,MekanismJEI.genericRecipeType(JEIRecipeTypes.APT),Arrays.asList(EMBlocks.APT_CASING,EMBlocks.APT_PORT,EMBlocks.SUPERCHARGING_ELEMENT));
        CatalystRegistryHelper.register(registry,MekanismJEI.genericRecipeType(JEIRecipeTypes.MELTING),Arrays.asList(EMBlocks.MELTER));
        CatalystRegistryHelper.register(registry,MekanismJEI.genericRecipeType(JEIRecipeTypes.SOLIDIFICATION),Arrays.asList(EMBlocks.SOLIDIFIER));

    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {

    }
}