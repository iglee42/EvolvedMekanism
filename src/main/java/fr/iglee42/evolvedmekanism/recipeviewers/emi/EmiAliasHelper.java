package fr.iglee42.evolvedmekanism.recipeviewers.emi;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.neoforge.NeoForgeEmiStack;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.text.IHasTranslationKey;
import mekanism.client.recipe_viewer.alias.RVAliasHelper;
import mekanism.client.recipe_viewer.emi.ChemicalEmiStack;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.List;
import java.util.function.Function;

public class EmiAliasHelper implements RVAliasHelper<EmiIngredient, EmiIngredient, EmiIngredient> {

    private final EmiRegistry registry;

    public EmiAliasHelper(EmiRegistry registry) {
        this.registry = registry;
    }

    @Override
    public EmiIngredient ingredient(ItemStack item) {
        return EmiStack.of(item);
    }

    @Override
    public EmiIngredient itemIngredient(Holder<Item> item) {
        return EmiStack.of(item.value());
    }

    @Override
    public List<EmiIngredient> itemTagContents(TagKey<Item> tag) {
        return List.of(EmiIngredient.of(tag));
    }

    @Override
    public EmiIngredient fluidIngredient(Holder<Fluid> fluid) {
        return EmiStack.of(fluid.value(), FluidType.BUCKET_VOLUME);
    }

    @Override
    public EmiIngredient ingredient(FluidStack fluid) {
        return NeoForgeEmiStack.of(fluid);
    }

    @Override
    public List<EmiIngredient> fluidTagContents(TagKey<Fluid> tag) {
        return List.of(EmiIngredient.of(tag));
    }

    @Override
    public EmiIngredient chemicalIngredient(Holder<Chemical> chemical) {
        return new ChemicalEmiStack(chemical, 1);
    }

    @Override
    public List<EmiIngredient> chemicalTagContents(TagKey<Chemical> tag) {
        return tagContents(MekanismAPI.CHEMICAL_REGISTRY, tag, holder -> new ChemicalEmiStack(holder, 1));
    }

    private <TYPE> List<EmiIngredient> tagContents(Registry<TYPE> registry, TagKey<TYPE> tag, Function<Holder<TYPE>, EmiIngredient> stackFunction) {
        return registry.getTag(tag)
                .stream()
                .flatMap(HolderSet::stream)
                .map(stackFunction)
                .toList();
    }

    @Override
    public void addItemAliases(List<EmiIngredient> stacks, IHasTranslationKey... aliases) {
        addAliases(stacks, aliases);
    }

    @Override
    public void addFluidAliases(List<EmiIngredient> stacks, IHasTranslationKey... aliases) {
        addAliases(stacks, aliases);
    }

    @Override
    public void addChemicalAliases(List<EmiIngredient> stacks, IHasTranslationKey... aliases) {
        addAliases(stacks, aliases);
    }

    private void addAliases(List<EmiIngredient> stacks, IHasTranslationKey... aliases) {
        for (EmiIngredient stack : stacks) {
            for (IHasTranslationKey alias : aliases) {
                registry.addAlias(stack, Component.translatable(alias.getTranslationKey()));
            }
        }
    }
}
