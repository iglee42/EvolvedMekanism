package fr.iglee42.evolvedmekanism.jei;

import java.util.List;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.jei.categories.APTRecipeCategory;
import fr.iglee42.evolvedmekanism.jei.categories.AlloyerRecipeCategory;
import fr.iglee42.evolvedmekanism.jei.categories.ChemixerRecipeCategory;
import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.providers.IItemProvider;
import mekanism.api.recipes.ItemStackGasToItemStackRecipe;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.client.jei.RecipeRegistryHelper;
import mekanism.common.block.BlockOre;
import mekanism.common.capabilities.Capabilities;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class EMJEI implements IModPlugin {
    public static final MekanismJEIRecipeType<AlloyerRecipe> ALLOYING = new MekanismJEIRecipeType<>(EMBlocks.APT_CASING, AlloyerRecipe.class);
    public static final MekanismJEIRecipeType<ChemixerRecipe> CHEMIXING = new MekanismJEIRecipeType<>(EMBlocks.APT_PORT, ChemixerRecipe.class);
    public static final MekanismJEIRecipeType<ItemStackGasToItemStackRecipe> APT = new MekanismJEIRecipeType<>(EMItems.BETTER_GOLD_INGOT, ItemStackGasToItemStackRecipe.class);

    @NotNull
    @Override
    public ResourceLocation getPluginUid() {
        return EvolvedMekanism.rl("jei_plugin");
    }

    public static void registerItemSubtypes(List<? extends IItemProvider> itemProviders) {
        for (IItemProvider itemProvider : itemProviders) {
            //Handle items
            ItemStack itemStack = itemProvider.getItemStack();
            if (!itemStack.getCapability(Capabilities.STRICT_ENERGY).isPresent() && !itemStack.getCapability(Capabilities.GAS_HANDLER).isPresent() && !itemStack.getCapability(Capabilities.INFUSION_HANDLER).isPresent() && !itemStack.getCapability(Capabilities.PIGMENT_HANDLER).isPresent() && !itemStack.getCapability(Capabilities.SLURRY_HANDLER).isPresent()) {
                FluidUtil.getFluidHandler(itemStack).isPresent();
            }
        }
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registry) {
        registerItemSubtypes(EMItems.ITEMS.getAllItems());
        registerItemSubtypes(EMBlocks.BLOCKS.getAllBlocks());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new AlloyerRecipeCategory(guiHelper, ALLOYING));
        registry.addRecipeCategories(new ChemixerRecipeCategory(guiHelper, CHEMIXING));
        registry.addRecipeCategories(new APTRecipeCategory(guiHelper, APT));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registry) {
        EMBlocks.BLOCKS.getAllBlocks().stream().filter(b->b.getBlock() instanceof BlockOre).forEach(b->{
        });
        RecipeRegistryHelper.register(registry, APT, EMRecipeType.APT);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registry) {
        CatalystRegistryHelper.register(registry,APT, EMBlocks.APT_CASING,EMBlocks.APT_PORT,EMBlocks.SUPERCHARGING_ELEMENT);

    }
}