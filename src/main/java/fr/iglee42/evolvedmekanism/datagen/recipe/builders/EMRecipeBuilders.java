package fr.iglee42.evolvedmekanism.datagen.recipe.builders;

import fr.iglee42.evolvedmekanism.impl.BasicAPTRecipe;
import fr.iglee42.evolvedmekanism.impl.BasicAlloyerRecipe;
import fr.iglee42.evolvedmekanism.impl.BasicChemixerRecipe;
import fr.iglee42.evolvedmekanism.impl.BasicMelterRecipe;
import fr.iglee42.evolvedmekanism.impl.BasicSolidificationRecipe;
import mekanism.api.datagen.recipe.MekanismRecipeBuilder;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public final class EMRecipeBuilders {

    private EMRecipeBuilders() {
    }

    public static MeltingBuilder melting(ItemStackIngredient input, FluidStackIngredient output) {
        return new MeltingBuilder(input, output);
    }

    public static SolidificationBuilder solidifying(ItemStackIngredient mold, FluidStackIngredient molten, FluidStackIngredient extra,
                                                    int duration, ItemStackIngredient output) {
        return new SolidificationBuilder(mold, molten, extra, duration, output);
    }

    public static AlloyingBuilder alloying(ItemStackIngredient main, ItemStackIngredient extra, ItemStackIngredient secondExtra, ItemStack output) {
        return new AlloyingBuilder(main, extra, secondExtra, output);
    }

    public static ChemixingBuilder chemixing(ItemStackIngredient main, ItemStackIngredient extra, ChemicalStackIngredient chemical, ItemStack output) {
        return new ChemixingBuilder(main, extra, chemical, output);
    }

    public static APTBuilder apt(ItemStackIngredient itemInput, ChemicalStackIngredient chemicalInput, ItemStack output, boolean perTickUsage) {
        return new APTBuilder(itemInput, chemicalInput, output, perTickUsage);
    }

    public static class MeltingBuilder extends MekanismRecipeBuilder<MeltingBuilder> {
        private final ItemStackIngredient input;
        private final FluidStackIngredient output;

        private MeltingBuilder(ItemStackIngredient input, FluidStackIngredient output) {
            this.input = input;
            this.output = output;
        }

        @Override
        protected Recipe<?> asRecipe() {
            return new BasicMelterRecipe(input, output);
        }
    }

    public static class SolidificationBuilder extends MekanismRecipeBuilder<SolidificationBuilder> {
        private final ItemStackIngredient mold;
        private final FluidStackIngredient molten;
        private final FluidStackIngredient extra;
        private final int duration;
        private final ItemStackIngredient output;

        private SolidificationBuilder(ItemStackIngredient mold, FluidStackIngredient molten, FluidStackIngredient extra,
                                      int duration, ItemStackIngredient output) {
            this.mold = mold;
            this.molten = molten;
            this.extra = extra;
            this.duration = duration;
            this.output = output;
        }

        @Override
        protected Recipe<?> asRecipe() {
            return new BasicSolidificationRecipe(mold, molten, extra, 0L, duration, output, true);
        }
    }

    public static class AlloyingBuilder extends MekanismRecipeBuilder<AlloyingBuilder> {
        private final ItemStackIngredient main;
        private final ItemStackIngredient extra;
        private final ItemStackIngredient secondExtra;
        private final ItemStack output;

        private AlloyingBuilder(ItemStackIngredient main, ItemStackIngredient extra, ItemStackIngredient secondExtra, ItemStack output) {
            this.main = main;
            this.extra = extra;
            this.secondExtra = secondExtra;
            this.output = output;
        }

        @Override
        protected Recipe<?> asRecipe() {
            return new BasicAlloyerRecipe(main, extra, secondExtra, output);
        }
    }

    public static class ChemixingBuilder extends MekanismRecipeBuilder<ChemixingBuilder> {
        private final ItemStackIngredient main;
        private final ItemStackIngredient extra;
        private final ChemicalStackIngredient chemical;
        private final ItemStack output;

        private ChemixingBuilder(ItemStackIngredient main, ItemStackIngredient extra, ChemicalStackIngredient chemical, ItemStack output) {
            this.main = main;
            this.extra = extra;
            this.chemical = chemical;
            this.output = output;
        }

        @Override
        protected Recipe<?> asRecipe() {
            return new BasicChemixerRecipe(main, extra, chemical, output);
        }
    }

    public static class APTBuilder extends MekanismRecipeBuilder<APTBuilder> {
        private final ItemStackIngredient itemInput;
        private final ChemicalStackIngredient chemicalInput;
        private final ItemStack output;
        private final boolean perTickUsage;

        private APTBuilder(ItemStackIngredient itemInput, ChemicalStackIngredient chemicalInput, ItemStack output, boolean perTickUsage) {
            this.itemInput = itemInput;
            this.chemicalInput = chemicalInput;
            this.output = output;
            this.perTickUsage = perTickUsage;
        }

        @Override
        protected Recipe<?> asRecipe() {
            return new BasicAPTRecipe(itemInput, chemicalInput, output, perTickUsage);
        }
    }
}
