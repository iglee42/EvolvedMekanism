package fr.iglee42.evolvedmekanism.registries;

import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function7;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.crafting.PersonalBarrelUpgrading;
import fr.iglee42.evolvedmekanism.crafting.PersonalChestUpgrading;
import fr.iglee42.evolvedmekanism.impl.*;
import fr.iglee42.evolvedmekanism.utils.EMSerializationConstants;
import mekanism.api.SerializationConstants;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.common.recipe.serializer.MekanismRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Function;

public class EMRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, EvolvedMekanism.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PersonalChestUpgrading>> PERSONAL_CHEST_UPGRADING = RECIPE_SERIALIZERS.register("personal_chest_upgrade",()->new SimpleCraftingRecipeSerializer<>(PersonalChestUpgrading::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PersonalBarrelUpgrading>> PERSONAL_BARREL_UPGRADING = RECIPE_SERIALIZERS.register("personal_barrel_upgrade",()->new SimpleCraftingRecipeSerializer<>(PersonalBarrelUpgrading::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicAlloyerRecipe>> ALLOYER = RECIPE_SERIALIZERS.register("alloying", () -> alloyer(BasicAlloyerRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicChemixerRecipe>> CHEMIXER = RECIPE_SERIALIZERS.register("chemixing", () -> chemixer(BasicChemixerRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicAPTRecipe>> APT = RECIPE_SERIALIZERS.register("apt", ()->MekanismRecipeSerializer.itemChemicalToItem(BasicAPTRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicMelterRecipe>> MELTER = RECIPE_SERIALIZERS.register("melting", () ->itemToFluid(BasicMelterRecipe::new));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BasicSolidificationRecipe>> SOLIDIFICATION = RECIPE_SERIALIZERS.register("solidifying", () ->solidifier(BasicSolidificationRecipe::new));


    public static <RECIPE extends BasicAlloyerRecipe> MekanismRecipeSerializer<RECIPE> alloyer(Function4<ItemStackIngredient, ItemStackIngredient, ItemStackIngredient,ItemStack, RECIPE> factory) {
        return new MekanismRecipeSerializer<>(RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStackIngredient.CODEC.fieldOf(SerializationConstants.MAIN_INPUT).forGetter(BasicAlloyerRecipe::getMainInput),
                ItemStackIngredient.CODEC.fieldOf(SerializationConstants.EXTRA_INPUT).forGetter(BasicAlloyerRecipe::getExtraInput),
                ItemStackIngredient.CODEC.fieldOf(EMSerializationConstants.SECOND_EXTRA_INPUT).forGetter(BasicAlloyerRecipe::getTertiaryExtraInput),
                ItemStack.CODEC.fieldOf(SerializationConstants.OUTPUT).forGetter(BasicAlloyerRecipe::getOutputRaw)
        ).apply(instance, factory)), StreamCodec.composite(
                ItemStackIngredient.STREAM_CODEC, BasicAlloyerRecipe::getMainInput,
                ItemStackIngredient.STREAM_CODEC, BasicAlloyerRecipe::getExtraInput,
                ItemStackIngredient.STREAM_CODEC, BasicAlloyerRecipe::getTertiaryExtraInput,
                ItemStack.STREAM_CODEC, BasicAlloyerRecipe::getOutputRaw,
                factory
        ));
    }


    public static <RECIPE extends BasicItemStackToFluidRecipe> MekanismRecipeSerializer<RECIPE> itemToFluid(BiFunction<ItemStackIngredient, FluidStack, RECIPE> factory) {
        return new MekanismRecipeSerializer<>(RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStackIngredient.CODEC.fieldOf(SerializationConstants.INPUT).forGetter(BasicItemStackToFluidRecipe::getInput),
                FluidStack.CODEC.fieldOf(SerializationConstants.OUTPUT).forGetter(BasicItemStackToFluidRecipe::getOutputRaw)
        ).apply(instance, factory)), StreamCodec.composite(
                ItemStackIngredient.STREAM_CODEC, BasicItemStackToFluidRecipe::getInput,
                FluidStack.STREAM_CODEC, BasicItemStackToFluidRecipe::getOutputRaw,
                factory
        ));
    }

    public static <RECIPE extends BasicChemixerRecipe> MekanismRecipeSerializer<RECIPE> chemixer(Function4<ItemStackIngredient, ItemStackIngredient, ChemicalStackIngredient, ItemStack, RECIPE> factory) {
        return new MekanismRecipeSerializer<>(RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStackIngredient.CODEC.fieldOf(SerializationConstants.MAIN_INPUT).forGetter(BasicChemixerRecipe::getInputMain),
                ItemStackIngredient.CODEC.fieldOf(SerializationConstants.EXTRA_INPUT).forGetter(BasicChemixerRecipe::getInputExtra),
                ChemicalStackIngredient.CODEC.fieldOf(SerializationConstants.CHEMICAL_INPUT).forGetter(BasicChemixerRecipe::getInputGas),
                ItemStack.CODEC.fieldOf(SerializationConstants.OUTPUT).forGetter(BasicChemixerRecipe::getOutputRaw)
        ).apply(instance, factory)), StreamCodec.composite(
                ItemStackIngredient.STREAM_CODEC, BasicChemixerRecipe::getInputMain,
                ItemStackIngredient.STREAM_CODEC, BasicChemixerRecipe::getInputExtra,
                ChemicalStackIngredient.STREAM_CODEC, BasicChemixerRecipe::getInputGas,
                ItemStack.STREAM_CODEC, BasicChemixerRecipe::getOutputRaw,
                factory
        ));
    }

    public static <RECIPE extends BasicSolidificationRecipe> MekanismRecipeSerializer<RECIPE> solidifier(Function7<ItemStackIngredient, FluidStackIngredient, FluidStackIngredient, Long, Integer, ItemStackIngredient, Boolean, RECIPE> factory) {
        return new MekanismRecipeSerializer<>(RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStackIngredient.CODEC.fieldOf(SerializationConstants.ITEM_INPUT).forGetter(BasicSolidificationRecipe::getInputSolid),
                FluidStackIngredient.CODEC.fieldOf(SerializationConstants.FLUID_INPUT).forGetter(BasicSolidificationRecipe::getInputFluid),
                FluidStackIngredient.CODEC.fieldOf(SerializationConstants.EXTRA_INPUT).forGetter(BasicSolidificationRecipe::getFluidInputExtra),
                Codec.LONG.optionalFieldOf(SerializationConstants.ENERGY_REQUIRED,0L).forGetter(BasicSolidificationRecipe::getEnergyRequired),
                Codec.INT.optionalFieldOf(SerializationConstants.DURATION,0).forGetter(BasicSolidificationRecipe::getDuration),
                ItemStackIngredient.CODEC.fieldOf(SerializationConstants.OUTPUT).forGetter(BasicSolidificationRecipe::getOutputRaw),
                Codec.BOOL.optionalFieldOf(EMSerializationConstants.KEEP_ITEM,true).forGetter(BasicSolidificationRecipe::shouldKeepItem)
                ).apply(instance, factory)), composite(
                ItemStackIngredient.STREAM_CODEC, BasicSolidificationRecipe::getInputSolid,
                FluidStackIngredient.STREAM_CODEC, BasicSolidificationRecipe::getInputFluid,
                FluidStackIngredient.STREAM_CODEC, BasicSolidificationRecipe::getFluidInputExtra,
                ByteBufCodecs.VAR_LONG, BasicSolidificationRecipe::getEnergyRequired,
                ByteBufCodecs.INT, BasicSolidificationRecipe::getDuration,
                ItemStackIngredient.STREAM_CODEC, BasicSolidificationRecipe::getOutputRaw, ByteBufCodecs.BOOL, BasicSolidificationRecipe::shouldKeepItem, factory)
        );
    }

    static <B, C, T1, T2, T3, T4, T5, T6,T7> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> p_331822_, final Function<C, T1> p_330864_, final StreamCodec<? super B, T2> p_331390_, final Function<C, T2> p_331203_, final StreamCodec<? super B, T3> p_331499_, final Function<C, T3> p_330294_, final StreamCodec<? super B, T4> p_331169_, final Function<C, T4> p_331830_, final StreamCodec<? super B, T5> p_331057_, final Function<C, T5> p_331593_, final StreamCodec<? super B, T6> p_331117_, final Function<C, T6> p_331904_,final StreamCodec<? super B, T7> t7C, final Function<C, T7> t7f, final Function7<T1, T2, T3, T4, T5, T6,T7, C> p_331335_) {
        return new StreamCodec<B, C>() {
            public C decode(B p_330310_) {
                T1 t1 = (T1)p_331822_.decode(p_330310_);
                T2 t2 = (T2)p_331390_.decode(p_330310_);
                T3 t3 = (T3)p_331499_.decode(p_330310_);
                T4 t4 = (T4)p_331169_.decode(p_330310_);
                T5 t5 = (T5)p_331057_.decode(p_330310_);
                T6 t6 = (T6)p_331117_.decode(p_330310_);
                T7 t7 = (T7)t7C.decode(p_330310_);
                return (C)p_331335_.apply(t1, t2, t3, t4, t5, t6,t7);
            }

            public void encode(B p_332052_, C p_331912_) {
                p_331822_.encode(p_332052_, p_330864_.apply(p_331912_));
                p_331390_.encode(p_332052_, p_331203_.apply(p_331912_));
                p_331499_.encode(p_332052_, p_330294_.apply(p_331912_));
                p_331169_.encode(p_332052_, p_331830_.apply(p_331912_));
                p_331057_.encode(p_332052_, p_331593_.apply(p_331912_));
                p_331117_.encode(p_332052_, p_331904_.apply(p_331912_));
                t7C.encode(p_332052_, t7f.apply(p_331912_));
            }
        };
    }

}
