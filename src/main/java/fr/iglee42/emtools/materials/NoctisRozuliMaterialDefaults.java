package fr.iglee42.emtools.materials;

import fr.iglee42.emtools.registries.EMToolsTags;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import mekanism.tools.common.material.impl.LapisLazuliMaterialDefaults;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NoctisRozuliMaterialDefaults extends LapisLazuliMaterialDefaults {

    @NotNull
    @Override
    public String getRegistryPrefix() {
        return "noctis_rozuli";
    }

    @NotNull
    @Override
    public String getConfigCommentName() {
        return "Noctis Rozuli";
    }

    @Nullable
    @Override
    public TagKey<Block> getTag() {
        return EMToolsTags.Blocks.NEEDS_NOCTIS_ROZULI_TOOL;
    }

    @NotNull
    @Override
    public Ingredient getCommonRepairMaterial() {
        return Ingredient.of(EMTags.Items.GEMS_NOCTIS_ROZULI);
    }

    @Override
    public String getName() {
        return EvolvedMekanism.MODID + ":" + getRegistryPrefix();
    }
}
