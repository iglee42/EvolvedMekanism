package fr.iglee42.evolvedmekanism.recipeviewers.jei;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.recipeviewers.EMRecipeViewersTypes;
import fr.iglee42.evolvedmekanism.recipeviewers.jei.categories.AlloyerJEIRecipeCategory;
import fr.iglee42.evolvedmekanism.recipeviewers.jei.categories.ChemixerJEIRecipeCategory;
import fr.iglee42.evolvedmekanism.recipeviewers.jei.categories.MeltingJEIRecipeCategory;
import fr.iglee42.evolvedmekanism.recipeviewers.jei.categories.SolidificationJEIRecipeCategory;
import fr.iglee42.evolvedmekanism.recipeviewers.jei.categories.multiblock.APTJEIRecipeCategory;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.client.recipe_viewer.jei.CatalystRegistryHelper;
import mekanism.client.recipe_viewer.jei.MekanismJEI;
import mekanism.client.recipe_viewer.jei.MekanismSubtypeInterpreter;
import mekanism.client.recipe_viewer.jei.RecipeRegistryHelper;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mekanism.common.Mekanism;
import mekanism.common.block.BlockOre;
import mekanism.common.capabilities.Capabilities;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.registration.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@JeiPlugin
public class EMJEI implements IModPlugin {


    private static final ISubtypeInterpreter<ItemStack> MEKANISM_DATA_INTERPRETER = new MekanismSubtypeInterpreter();

    public static boolean shouldLoad() {
        return !Mekanism.hooks.emi.isLoaded();
    }


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
        if (shouldLoad()) {
            registry.addRecipeCategories(new AlloyerJEIRecipeCategory(guiHelper, EMRecipeViewersTypes.ALLOYING));
            registry.addRecipeCategories(new ChemixerJEIRecipeCategory(guiHelper, EMRecipeViewersTypes.CHEMIXING));
            registry.addRecipeCategories(new APTJEIRecipeCategory(guiHelper, EMRecipeViewersTypes.APT));
            registry.addRecipeCategories(new MeltingJEIRecipeCategory(guiHelper, EMRecipeViewersTypes.MELTING, false));
            registry.addRecipeCategories(new SolidificationJEIRecipeCategory(guiHelper, EMRecipeViewersTypes.SOLIDIFICATION));
        }
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
        if (shouldLoad()) {
            registerItemSubtypes(registry, EMItems.ITEMS.getEntries());
            registerItemSubtypes(registry, EMBlocks.BLOCKS.getSecondaryEntries());
        }
    }


    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registry) {}

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        if (!shouldLoad()) return;
        List<FluidStack> fluidsToRemove = new ArrayList<>();
        List<ItemStack> itemsToRemove = new ArrayList<>();
        EMFluids.FLUIDS.getFluidEntries().stream().filter(ro->ro.getId().getPath().startsWith("molten_")).forEach(ro->{
            boolean hasMelting = EMRecipeType.MELTING.getRecipes((Level) null).stream().anyMatch(r->r.value().getOutput(ItemStack.EMPTY).getFluid().equals(ro.get()));
            boolean hasSolidifying = EMRecipeType.SOLIDIFICATION.getRecipes((Level) null).stream().anyMatch(r->r.value().getInputFluid().test(new FluidStack(ro.get(), (int) r.value().getInputFluid().getNeededAmount(new FluidStack(ro.get(),1)))));
            if (!hasMelting && !hasSolidifying){
                fluidsToRemove.add(new FluidStack(ro.get(),1000));
                if (ro.get() instanceof BaseFlowingFluid.Source) itemsToRemove.add(ro.get().getBucket().getDefaultInstance());
            }
        });
        EMBlocks.BLOCKS.getPrimaryEntries().stream().filter(b->b.get() instanceof BlockOre).forEach(b->{
            String stone = b.getId().getPath().split("_")[0].toLowerCase();
            if (stone.equals("holystone") && !ModList.get().isLoaded("aether")) {
                itemsToRemove.add(new ItemStack(b.get()));
            }
            if ((stone.equals("depthrock") || stone.equals("shiverstone")) && !ModList.get().isLoaded("undergarden")) {
                itemsToRemove.add(new ItemStack(b.get()));
            }
        });
        EMItems.ITEMS.getEntries().stream().filter(i->i.getId().getPath().startsWith("mold_")).forEach(m->{
            boolean used = EMRecipeType.SOLIDIFICATION.getRecipes((Level) null).stream().anyMatch(r->r.value().getInputSolid().getRepresentations().stream().anyMatch(s->s.getItem().equals(m.get())));
            if (!used) itemsToRemove.add(new ItemStack(m.get()));
        });
        registry.getIngredientManager().removeIngredientsAtRuntime(NeoForgeTypes.FLUID_STACK,fluidsToRemove);
        registry.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK,itemsToRemove);
        RecipeRegistryHelper.register(registry, EMRecipeViewersTypes.ALLOYING, EMRecipeType.ALLOYING);
        RecipeRegistryHelper.register(registry, EMRecipeViewersTypes.CHEMIXING, EMRecipeType.CHEMIXING);
        RecipeRegistryHelper.register(registry, EMRecipeViewersTypes.APT, EMRecipeType.APT);
        RecipeRegistryHelper.register(registry, EMRecipeViewersTypes.MELTING, EMRecipeType.MELTING);
        RecipeRegistryHelper.register(registry, EMRecipeViewersTypes.SOLIDIFICATION, EMRecipeType.SOLIDIFICATION);

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        if (!shouldLoad()) return;
        CatalystRegistryHelper.register(registry, MekanismJEI.genericRecipeType(RecipeViewerRecipeType.ACTIVATING),Arrays.asList(EMBlocks.LUNAR_NEUTRON_ACTIVATOR));
        CatalystRegistryHelper.register(registry,MekanismJEI.genericRecipeType(EMRecipeViewersTypes.CHEMIXING),Arrays.asList(EMBlocks.CHEMIXER));
        CatalystRegistryHelper.register(registry,MekanismJEI.genericRecipeType(EMRecipeViewersTypes.APT),Arrays.asList(EMBlocks.APT_CASING,EMBlocks.APT_PORT,EMBlocks.SUPERCHARGING_ELEMENT));
        CatalystRegistryHelper.register(registry,MekanismJEI.genericRecipeType(EMRecipeViewersTypes.MELTING),Arrays.asList(EMBlocks.MELTER));
        CatalystRegistryHelper.register(registry,MekanismJEI.genericRecipeType(EMRecipeViewersTypes.SOLIDIFICATION),Arrays.asList(EMBlocks.SOLIDIFIER));
        List<ItemLike> alloying = BuiltInRegistries.BLOCK.holders().filter(h->h.getKey().location().getNamespace().equals(EvolvedMekanism.MODID) && (h.getKey().location().getPath().contains("alloyer") || h.getKey().location().getPath().contains("alloying"))).map(Holder.Reference::value).map(ItemLike.class::cast).toList();
        alloying.forEach(i->registry.addRecipeCatalyst(i,MekanismJEI.genericRecipeType(EMRecipeViewersTypes.ALLOYING)));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {

    }
}