package fr.iglee42.evolvedmekanism.recipeviewers;

import fr.iglee42.evolvedmekanism.recipes.AlloyerRecipe;
import fr.iglee42.evolvedmekanism.recipes.ChemixerRecipe;
import fr.iglee42.evolvedmekanism.recipes.MeltingRecipe;
import fr.iglee42.evolvedmekanism.recipes.SolidificationRecipe;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMRecipeType;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.ItemStackChemicalToItemStackRecipe;
import mekanism.api.recipes.ItemStackToFluidRecipe;
import mekanism.client.recipe_viewer.type.RVRecipeTypeWrapper;

@NothingNullByDefault
public class EMRecipeViewersTypes {

    private EMRecipeViewersTypes() {
    }

    public static final RVRecipeTypeWrapper<?, AlloyerRecipe,?> ALLOYING = new RVRecipeTypeWrapper<>(EMRecipeType.ALLOYING, AlloyerRecipe.class, -28, -16, 144, 54, EMBlocks.ALLOYER);
    public static final RVRecipeTypeWrapper<?, ItemStackChemicalToItemStackRecipe,?> APT = new RVRecipeTypeWrapper<>(EMRecipeType.APT, ItemStackChemicalToItemStackRecipe.class, -3, -12, 168, 74, EMItems.BETTER_GOLD_INGOT,EMBlocks.APT_CASING,EMBlocks.APT_PORT,EMBlocks.SUPERCHARGING_ELEMENT);
    public static final RVRecipeTypeWrapper<?, ChemixerRecipe,?> CHEMIXING = new RVRecipeTypeWrapper<>(EMRecipeType.CHEMIXING, ChemixerRecipe.class, -28, -13, 144, 60,EMBlocks.CHEMIXER);
    public static final RVRecipeTypeWrapper<?, SolidificationRecipe,?> SOLIDIFICATION = new RVRecipeTypeWrapper<>(EMRecipeType.SOLIDIFICATION, SolidificationRecipe.class,  -3, -10, 170, 60,EMBlocks.SOLIDIFIER);
    public static final RVRecipeTypeWrapper<?, MeltingRecipe,?> MELTING = new RVRecipeTypeWrapper<>(EMRecipeType.MELTING, MeltingRecipe.class,   -20, -12, 132, 62,EMBlocks.MELTER);


}
