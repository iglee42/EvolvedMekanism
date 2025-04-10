package fr.iglee42.evolvedmekanism.recipes;

import mekanism.api.functions.TriConsumer;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.*;

public class EMCachedRecipeHelper {

    public static <INPUT_A, INPUT_B, INPUT_C, OUTPUT> void threeInputCalculateOperationsThisTick(CachedRecipe.OperationTracker tracker, IInputHandler<INPUT_A> inputAHandler,
                                                                                                 Supplier<? extends InputIngredient<INPUT_A>> inputAIngredient, IInputHandler<INPUT_B> inputBHandler,
                                                                                                 Supplier<? extends InputIngredient<INPUT_B>> inputBIngredient, IInputHandler<INPUT_C> inputCHandler,
                                                                                                 Supplier<? extends InputIngredient<INPUT_C>> inputCIngredient, TriConsumer<INPUT_A, INPUT_B,INPUT_C> inputsSetter, IOutputHandler<OUTPUT> outputHandler,
                                                                                                 TriFunction<INPUT_A, INPUT_B, INPUT_C, OUTPUT> outputGetter, Consumer<OUTPUT> outputSetter, Predicate<INPUT_A> emptyCheckA, Predicate<INPUT_B> emptyCheckB, Predicate<INPUT_C> emptyCheckC) {
        if (tracker.shouldContinueChecking()) {
            INPUT_A inputA = inputAHandler.getRecipeInput(inputAIngredient.get());
            //Test to make sure we can even perform a single operation. This is akin to !recipe.test(inputA)
            if (emptyCheckA.test(inputA)) {
                //No input, we don't know if the recipe matches or not so treat it as not matching
                tracker.mismatchedRecipe();
            } else {
                INPUT_B inputB = inputBHandler.getRecipeInput(inputBIngredient.get());
                //Test to make sure we can even perform a single operation. This is akin to !recipe.test(inputB)
                if (emptyCheckB.test(inputB)) {
                    //No input, we don't know if the recipe matches or not so treat it as not matching
                    tracker.mismatchedRecipe();
                } else {
                    INPUT_C inputC = inputCHandler.getRecipeInput(inputCIngredient.get());
                    //Test to make sure we can even perform a single operation. This is akin to !recipe.test(inputB)
                    if (emptyCheckC.test(inputC)) {
                        //No input, we don't know if the recipe matches or not so treat it as not matching
                        tracker.mismatchedRecipe();
                    } else {
                        inputsSetter.accept(inputA, inputB,inputC);
                        //Calculate the current max based on the primary input
                        inputAHandler.calculateOperationsCanSupport(tracker, inputA);
                        if (tracker.shouldContinueChecking()) {
                            //Calculate the current max based on the secondary input
                            inputBHandler.calculateOperationsCanSupport(tracker, inputB);
                            if (tracker.shouldContinueChecking()) {
                                inputCHandler.calculateOperationsCanSupport(tracker,inputC);
                                if (tracker.shouldContinueChecking()){
                                    OUTPUT output = outputGetter.apply(inputA, inputB,inputC);
                                    outputSetter.accept(output);
                                    //Calculate the max based on the space in the output
                                    outputHandler.calculateOperationsCanSupport(tracker, output);
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}
