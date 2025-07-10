package fr.iglee42.evolvedmekanism.mixins;

import fr.iglee42.evolvedmekanism.interfaces.EMInputRecipeCache;
import fr.iglee42.evolvedmekanism.registries.EMFactoryType;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.AbstractInputRecipeCache;
import mekanism.common.recipe.lookup.cache.TripleInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.IInputCache;
import mekanism.common.tier.FactoryTier;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.TriPredicate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(value = TripleInputRecipeCache.class,remap = false)
public abstract class TripleInputRecipeCacheMixin<INPUT_A, INGREDIENT_A extends InputIngredient<INPUT_A>, INPUT_B, INGREDIENT_B extends InputIngredient<INPUT_B>,
        INPUT_C, INGREDIENT_C extends InputIngredient<INPUT_C>, RECIPE extends MekanismRecipe<?> & TriPredicate<INPUT_A, INPUT_B, INPUT_C>,
        CACHE_A extends IInputCache<INPUT_A, INGREDIENT_A, RECIPE>, CACHE_B extends IInputCache<INPUT_B, INGREDIENT_B, RECIPE>,
        CACHE_C extends IInputCache<INPUT_C, INGREDIENT_C, RECIPE>> extends AbstractInputRecipeCache<RECIPE> implements EMInputRecipeCache.IFindRecipes<INPUT_A,INGREDIENT_A,INPUT_B,INGREDIENT_B,INPUT_C,INGREDIENT_C,RECIPE,CACHE_A,CACHE_B,CACHE_C> {


    @Shadow @Final private CACHE_A cacheA;

    @Shadow @Final private CACHE_B cacheB;

    @Shadow @Final private CACHE_C cacheC;

    @Shadow @Final private Function<RECIPE, INGREDIENT_C> inputCExtractor;

    @Shadow @Final private Set<RECIPE> complexRecipes;

    @Shadow @Final private Function<RECIPE, INGREDIENT_A> inputAExtractor;

    @Shadow @Final private Function<RECIPE, INGREDIENT_B> inputBExtractor;

    @Shadow @Final private Set<RECIPE> complexIngredientA;

    @Shadow @Final private Set<RECIPE> complexIngredientB;

    @Shadow @Final private Set<RECIPE> complexIngredientC;

    protected TripleInputRecipeCacheMixin(MekanismRecipeType<?, RECIPE, ?> recipeType) {
        super(recipeType);
    }


    @Override
    public @Nullable RECIPE findTypeBasedRecipe(@Nullable Level world, INPUT_A inputA, INPUT_B inputB, INPUT_C inputC, Predicate<RECIPE> matchCriteria) {
        initCacheIfNeeded(world);
        if (cacheA.isEmpty(inputA)) {
            //Don't allow empty primary inputs
            return null;
        }
        Predicate<RECIPE> matchPredicate;
        if (cacheB.isEmpty(inputB)) {
            //If b is empty, lookup by A and our match criteria
            return null;
        }
        if (cacheC.isEmpty(inputC)){
            matchPredicate = matchCriteria;
        } else {
            matchPredicate = recipe -> inputCExtractor.apply(recipe).testType(inputC) && matchCriteria.test(recipe);
        }
        RECIPE recipe = cacheA.findFirstRecipe(inputA, matchPredicate);
        if (recipe == null) {
            return complexRecipes.stream().filter(r-> inputAExtractor.apply(r).testType(inputA) && inputBExtractor.apply(r).testType(inputB) && matchPredicate.test(r)).findFirst().orElse(null);
        }
        return recipe;
    }


    @Override
    protected void initCache(List<RecipeHolder<RECIPE>> recipes) {
        for (RecipeHolder<RECIPE> recipeHolder : recipes) {
            RECIPE recipe = recipeHolder.value();
            boolean complexA = cacheA.mapInputs(recipe, inputAExtractor.apply(recipe));
            boolean complexB = cacheB.mapInputs(recipe, inputBExtractor.apply(recipe));
            boolean complexC = cacheC.mapInputs(recipe, inputCExtractor.apply(recipe));
            if (complexA) {
                complexIngredientA.add(recipe);
            }
            if (complexB) {
                complexIngredientB.add(recipe);
            }
            if (complexC) {
                complexIngredientC.add(recipe);
            }
            if (complexA || complexB || complexC) {
                complexRecipes.add(recipe);
            }
        }
    }
}
