package fr.iglee42.evolvedmekanism.datagen.recipe;

import com.google.gson.JsonObject;
import fr.iglee42.emgenerators.registries.EMGenBlocks;
import fr.iglee42.emgenerators.registries.EMGenItems;
import fr.iglee42.emtools.registries.EMToolsItems;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.datagen.EMDatagenTags;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import fr.iglee42.evolvedmekanism.registries.EMGases;
import fr.iglee42.evolvedmekanism.registries.EMInfuseTypes;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import fr.iglee42.evolvedmekanism.registries.EMTags;
import mekanism.api.datagen.recipe.builder.CombinerRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToChemicalRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import mekanism.api.datagen.recipe.builder.NucleosynthesizingRecipeBuilder;
import mekanism.api.datagen.recipe.builder.PressurizedReactionRecipeBuilder;
import mekanism.api.datagen.recipe.builder.RotaryRecipeBuilder;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismGases;
import mekanism.common.registries.MekanismItems;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ResourceType;
import mekanism.common.tags.MekanismTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class EMRecipeProvider extends RecipeProvider {

    public EMRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> output) {
        MoltenRecipes.addRecipes(output);
        addMachineRecipes(output);
        addTieredUpgradeRecipes(output);
        addProcessingRecipes(output);
        addToolRecipes(output);
        addMiscRecipes(output);
    }

    private void addMachineRecipes(Consumer<FinishedRecipe> output) {
        EMCrafting.shaped(output, "alloyer", EMBlocks.ALLOYER, 1, new String[]{"ACA", "SXT", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_BASIC, 'C', EMDatagenTags.forgeItem("circuits/advanced"),
                        'S', MekanismBlocks.ENERGIZED_SMELTER, 'T', MekanismBlocks.COMBINER, 'X', MekanismBlocks.STEEL_CASING),
                has(MekanismBlocks.STEEL_CASING));
        EMCrafting.shaped(EMCrafting.mekData(output), "chemixer", EMBlocks.CHEMIXER, 1, new String[]{"ACA", "OPO", "ACA"},
                EMCrafting.keys('A', EMDatagenTags.item("evolvedmekanism", "alloys/singular"), 'C', EMDatagenTags.forgeItem("circuits/dense"),
                        'O', EMDatagenTags.forgeItem("ingots/osmium"), 'P', EMBlocks.ALLOYER),
                has(EMBlocks.ALLOYER));
        EMCrafting.shaped(output, "thermalizer", EMBlocks.MELTER, 1, new String[]{"ACA", "SXS", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_INFUSED, 'C', Items.LAVA_BUCKET,
                        'S', MekanismBlocks.RESISTIVE_HEATER, 'X', MekanismBlocks.STEEL_CASING),
                has(MekanismBlocks.STEEL_CASING));
        EMCrafting.shaped(output, "solidification_chamber", EMBlocks.SOLIDIFIER, 1, new String[]{"ACA", "SXS", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_INFUSED, 'C', Items.BLUE_ICE,
                        'S', Items.BUCKET, 'X', MekanismBlocks.STEEL_CASING),
                has(MekanismBlocks.STEEL_CASING));
        EMCrafting.shaped(output, "apt_port", EMBlocks.APT_PORT, 1, new String[]{" # ", "#C#", " # "},
                EMCrafting.keys('#', EMBlocks.APT_CASING, 'C', EMDatagenTags.forgeItem("circuits/quantum")),
                has(EMBlocks.APT_CASING));
        EMCrafting.shaped(output, "supercharging_element", EMBlocks.SUPERCHARGING_ELEMENT, 1, new String[]{"AIA", "IXI", "AIA"},
                EMCrafting.keys('A', EMDatagenTags.forgeItem("storage_blocks/alloys/singular"),
                        'I', EMDatagenTags.forgeItem("ingots/refined_redstone"), 'X', MekanismBlocks.SUPERHEATING_ELEMENT),
                has(MekanismBlocks.SUPERHEATING_ELEMENT));
        EMCrafting.shaped(output, "lunar_neutron_activator", EMBlocks.LUNAR_NEUTRON_ACTIVATOR, 1, new String[]{"A#A", "CXC", "III"},
                EMCrafting.keys('#', MekanismItems.HDPE_SHEET, 'A', MekanismTags.Items.ALLOYS_REINFORCED,
                        'C', EMDatagenTags.forgeItem("circuits/elite"), 'I', EMTags.Items.GEMS_NOCTIS_ROZULI,
                        'X', MekanismBlocks.STEEL_CASING),
                has(MekanismBlocks.STEEL_CASING));

        addModule(output, "module_air_affinity_unit", EMItems.AIR_AFFINITY, EMCrafting.item("mekanism:oxygen_bucket"));
        addModule(output, "module_aqua_affinity_unit", EMItems.AQUA_AFFINITY, MekanismItems.SCUBA_MASK);
        addModule(output, "module_capturing_unit", EMItems.CAPTURING, Items.NETHERITE_SWORD);
        EMCrafting.shaped(output, "module_luck_unit", EMItems.LUCK, 1, new String[]{"A#A", "#P#", "HHH"},
                EMCrafting.keys('#', EMDatagenTags.forgeItem("storage_blocks/lapis"), 'A', EMDatagenTags.forgeItem("alloys/elite"),
                        'H', EMDatagenTags.forgeItem("pellets/polonium"), 'P', MekanismItems.MODULE_BASE),
                has(MekanismItems.MODULE_BASE));

        addQio(output, "qio_drive_boosted", EMItems.BOOSTED_QIO_DRIVE, EMCrafting.item("mekanism:qio_drive_supermassive"), EMDatagenTags.mekItem("crystals/uranium"));
        addQio(output, "qio_drive_singularity", EMItems.SINGULARITY_QIO_DRIVE, EMItems.BOOSTED_QIO_DRIVE, EMDatagenTags.forgeItem("pellets/antimatter"));
        addQio(output, "qio_drive_hypra_solidified", EMItems.HYPRA_SOLIDIFIED_QIO_DRIVE, EMItems.SINGULARITY_QIO_DRIVE, EMDatagenTags.forgeItem("nuggets/better_gold"));
        addQio(output, "qio_drive_black_hole", EMItems.BLACK_HOLE_QIO_DRIVE, EMItems.HYPRA_SOLIDIFIED_QIO_DRIVE, EMDatagenTags.forgeItem("nuggets/plaslitherite"));
        addQio(output, "qio_drive_creative", EMItems.CREATIVE_QIO_DRIVE, EMItems.BLACK_HOLE_QIO_DRIVE, EMDatagenTags.item("evolvedmekanism", "alloys/creative"));

        EMCrafting.shaped(EMCrafting.mekData(output), "max_tier_installer", EMItems.MAX_TIER_INSTALLER, 1,
                new String[]{"123", "4 5", "678"},
                EMCrafting.keys('1', MekanismItems.BASIC_TIER_INSTALLER, '2', MekanismItems.ADVANCED_TIER_INSTALLER,
                        '3', MekanismItems.ELITE_TIER_INSTALLER, '4', MekanismItems.ULTIMATE_TIER_INSTALLER,
                        '5', EMItems.OVERCLOCKED_TIER_INSTALLER, '6', EMItems.QUANTUM_TIER_INSTALLER,
                        '7', EMItems.DENSE_TIER_INSTALLER, '8', EMItems.MULTIVERSAL_TIER_INSTALLER),
                has(EMItems.MULTIVERSAL_TIER_INSTALLER));
    }

    private void addModule(Consumer<FinishedRecipe> output, String path, ItemLike result, Object center) {
        EMCrafting.shaped(output, path, result, 1, new String[]{"A#A", "APA", "HHH"},
                EMCrafting.keys('#', center, 'A', EMDatagenTags.forgeItem("alloys/elite"),
                        'H', EMDatagenTags.forgeItem("pellets/polonium"), 'P', MekanismItems.MODULE_BASE),
                has(MekanismItems.MODULE_BASE));
    }

    private void addQio(Consumer<FinishedRecipe> output, String path, ItemLike result, ItemLike previous, Object center) {
        EMCrafting.shaped(EMCrafting.mekData(output), path, result, 1, new String[]{"IPI", "P#P", "IPI"},
                EMCrafting.keys('#', center, 'I', EMDatagenTags.forgeItem("ingots/lead"), 'P', previous),
                has(previous));
    }

    private void addTieredUpgradeRecipes(Consumer<FinishedRecipe> output) {
        Consumer<FinishedRecipe> mek = EMCrafting.mekData(output);
        for (TieredRecipes.TierData tier : TieredRecipes.EM_TIERS) {
            for (String type : TieredRecipes.factoryTypes()) {
                EMCrafting.shaped(mek, "factory/" + tier.name() + "/" + type,
                        TieredRecipes.factoryResult(tier.name(), type), 1, new String[]{"ACA", "IPI", "ACA"},
                        EMCrafting.keys('A', tier.alloy(), 'C', tier.circuit(), 'I', tier.extra(),
                                'P', TieredRecipes.previousFactory(tier, type)),
                        has(tier.circuit()));
            }
            addBin(mek, tier);
            addTransmitterSet(output, tier);
            addEnergyCube(mek, tier);
            addFluidTank(mek, tier);
            addChemicalTank(mek, tier);
            addInduction(mek, output, tier);
            addCircuit(output, tier);
            addTierInstaller(output, tier);
            addSolarGenerator(mek, tier);
            addLunarGenerator(mek, tier);
        }
        for (TieredRecipes.TierData tier : TieredRecipes.ALLOYING_VANILLA_TIERS) {
            EMCrafting.shaped(mek, "factory/" + tier.name() + "/alloying",
                    TieredRecipes.factoryResult(tier.name(), "alloying"), 1, new String[]{"ACA", "IPI", "ACA"},
                    EMCrafting.keys('A', tier.alloy(), 'C', tier.circuit(), 'I', tier.extra(),
                            'P', TieredRecipes.previousFactory(tier, "alloying")),
                    has(tier.circuit()));
        }
        addPersonalStorageChain(mek);
    }

    private void addBin(Consumer<FinishedRecipe> mek, TieredRecipes.TierData tier) {
        ItemLike result = "creative".equals(tier.name()) ? MekanismBlocks.CREATIVE_BIN : EMCrafting.item("evolvedmekanism:" + tier.name() + "_bin");
        ItemLike previous = tier.previousMekanism() ? MekanismBlocks.ULTIMATE_BIN : EMCrafting.item("evolvedmekanism:" + tier.previousPrefix() + "_bin");
        EMCrafting.shaped(mek, "bin/" + tier.name(), result, 1, new String[]{"_C_", "APA", "___"},
                EMCrafting.keys('A', tier.alloy(), 'C', tier.circuit(), 'P', previous, '_', EMDatagenTags.forgeItem("cobblestone/normal")),
                has(previous));
    }

    private void addEnergyCube(Consumer<FinishedRecipe> mek, TieredRecipes.TierData tier) {
        ItemLike result = "creative".equals(tier.name()) ? MekanismBlocks.CREATIVE_ENERGY_CUBE : EMCrafting.item("evolvedmekanism:" + tier.name() + "_energy_cube");
        ItemLike previous = tier.previousMekanism() ? MekanismBlocks.ULTIMATE_ENERGY_CUBE : EMCrafting.item("evolvedmekanism:" + tier.previousPrefix() + "_energy_cube");
        EMCrafting.shaped(mek, "energy_cube/" + tier.name(), result, 1, new String[]{"AEA", "IPI", "AEA"},
                EMCrafting.keys('A', tier.alloy(), 'E', MekanismItems.ENERGY_TABLET, 'I', Tags.Items.GEMS_DIAMOND, 'P', previous),
                has(previous));
    }

    private void addFluidTank(Consumer<FinishedRecipe> mek, TieredRecipes.TierData tier) {
        ItemLike result = "creative".equals(tier.name()) ? MekanismBlocks.CREATIVE_FLUID_TANK : EMCrafting.item("evolvedmekanism:" + tier.name() + "_fluid_tank");
        ItemLike previous = tier.previousMekanism() ? MekanismBlocks.ULTIMATE_FLUID_TANK : EMCrafting.item("evolvedmekanism:" + tier.previousPrefix() + "_fluid_tank");
        EMCrafting.shaped(mek, "fluid_tank/" + tier.name(), result, 1, new String[]{"AIA", "IPI", "AIA"},
                EMCrafting.keys('A', tier.alloy(), 'I', Tags.Items.INGOTS_IRON, 'P', previous),
                has(previous));
    }

    private void addChemicalTank(Consumer<FinishedRecipe> mek, TieredRecipes.TierData tier) {
        ItemLike result = "creative".equals(tier.name()) ? MekanismBlocks.CREATIVE_CHEMICAL_TANK : EMCrafting.item("evolvedmekanism:" + tier.name() + "_chemical_tank");
        ItemLike previous = tier.previousMekanism() ? MekanismBlocks.ULTIMATE_CHEMICAL_TANK : EMCrafting.item("evolvedmekanism:" + tier.previousPrefix() + "_chemical_tank");
        EMCrafting.shaped(mek, "chemical_tank/" + tier.name(), result, 1, new String[]{"AOA", "OPO", "AOA"},
                EMCrafting.keys('A', tier.alloy(), 'O', EMDatagenTags.forgeItem("ingots/osmium"), 'P', previous),
                has(previous));
    }

    private void addInduction(Consumer<FinishedRecipe> mek, Consumer<FinishedRecipe> output, TieredRecipes.TierData tier) {
        ItemLike cube = "creative".equals(tier.name()) ? MekanismBlocks.CREATIVE_ENERGY_CUBE : EMCrafting.item("evolvedmekanism:" + tier.name() + "_energy_cube");
        ItemLike prevCell = tier.previousMekanism() ? MekanismBlocks.ULTIMATE_INDUCTION_CELL : EMCrafting.item("evolvedmekanism:" + tier.previousPrefix() + "_induction_cell");
        ItemLike prevProvider = tier.previousMekanism() ? MekanismBlocks.ULTIMATE_INDUCTION_PROVIDER : EMCrafting.item("evolvedmekanism:" + tier.previousPrefix() + "_induction_provider");
        EMCrafting.shaped(mek, "induction/cell/" + tier.name(), EMCrafting.item("evolvedmekanism:" + tier.name() + "_induction_cell"), 1,
                new String[]{"EPE", "P#P", "EPE"},
                EMCrafting.keys('#', cube, 'E', MekanismItems.ENERGY_TABLET, 'P', prevCell), has(prevCell));
        EMCrafting.shaped(output, "induction/provider/" + tier.name(), EMCrafting.item("evolvedmekanism:" + tier.name() + "_induction_provider"), 1,
                new String[]{"CPC", "P#P", "CPC"},
                EMCrafting.keys('#', cube, 'C', tier.circuit(), 'P', prevProvider), has(prevProvider));
    }

    private void addPersonalStorageChain(Consumer<FinishedRecipe> mek) {
        addPersonalStorage(mek, "advanced", EMDatagenTags.forgeItem("circuits/advanced"), MekanismBlocks.PERSONAL_BARREL, MekanismBlocks.PERSONAL_CHEST);
        addPersonalStorage(mek, "elite", EMDatagenTags.forgeItem("circuits/elite"), EMBlocks.ADVANCED_PERSONAL_BARREL, EMBlocks.ADVANCED_PERSONAL_CHEST);
        addPersonalStorage(mek, "ultimate", EMDatagenTags.forgeItem("circuits/ultimate"), EMBlocks.ELITE_PERSONAL_BARREL, EMBlocks.ELITE_PERSONAL_CHEST);
        addPersonalStorage(mek, "overclocked", EMDatagenTags.forgeItem("circuits/overclocked"), EMBlocks.ULTIMATE_PERSONAL_BARREL, EMBlocks.ULTIMATE_PERSONAL_CHEST);
        addPersonalStorage(mek, "quantum", EMDatagenTags.forgeItem("circuits/quantum"), EMBlocks.OVERCLOCKED_PERSONAL_BARREL, EMBlocks.OVERCLOCKED_PERSONAL_CHEST);
        addPersonalStorage(mek, "dense", EMDatagenTags.forgeItem("circuits/dense"), EMBlocks.QUANTUM_PERSONAL_BARREL, EMBlocks.QUANTUM_PERSONAL_CHEST);
        addPersonalStorage(mek, "multiversal", EMDatagenTags.forgeItem("circuits/multiversal"), EMBlocks.DENSE_PERSONAL_BARREL, EMBlocks.DENSE_PERSONAL_CHEST);
        addPersonalStorage(mek, "creative", EMDatagenTags.forgeItem("circuits/creative"), EMBlocks.MULTIVERSAL_PERSONAL_BARREL, EMBlocks.MULTIVERSAL_PERSONAL_CHEST);
    }

    private void addPersonalStorage(Consumer<FinishedRecipe> mek, String tier, Object circuit, ItemLike previousBarrel, ItemLike previousChest) {
        EMCrafting.shaped(mek, "personal_barrel/" + tier, EMCrafting.item("evolvedmekanism:" + tier + "_personal_barrel"), 1,
                new String[]{"_C_", "APA", "___"},
                EMCrafting.keys('A', circuit, 'C', EMDatagenTags.forgeItem("glass/silica"), 'P', previousBarrel, '_', EMDatagenTags.forgeItem("ingots/steel")),
                has(previousBarrel));
        EMCrafting.shaped(mek, "personal_chest/" + tier, EMCrafting.item("evolvedmekanism:" + tier + "_personal_chest"), 1,
                new String[]{"_C_", "APA", "___"},
                EMCrafting.keys('A', circuit, 'C', EMDatagenTags.forgeItem("glass/silica"), 'P', previousChest, '_', EMDatagenTags.forgeItem("ingots/steel")),
                has(previousChest));
    }

    private void addCircuit(Consumer<FinishedRecipe> output, TieredRecipes.TierData tier) {
        Object previousCircuit;
        switch (tier.name()) {
            case "overclocked" -> previousCircuit = EMDatagenTags.forgeItem("circuits/ultimate");
            case "quantum" -> previousCircuit = EMDatagenTags.forgeItem("circuits/overclocked");
            case "dense" -> previousCircuit = EMDatagenTags.forgeItem("circuits/quantum");
            case "multiversal" -> previousCircuit = EMDatagenTags.forgeItem("circuits/dense");
            default -> previousCircuit = EMDatagenTags.forgeItem("circuits/multiversal");
        }
        EMCrafting.shaped(output, "control_circuit/" + tier.name(), EMCrafting.item("evolvedmekanism:" + tier.name() + "_control_circuit"), 1,
                new String[]{" A ", "ACA", " A "},
                EMCrafting.keys('A', tier.alloy(), 'C', previousCircuit), has(tier.alloy()));
    }

    private void addTierInstaller(Consumer<FinishedRecipe> output, TieredRecipes.TierData tier) {
        EMCrafting.shaped(output, "tier_installer/" + tier.name(), EMCrafting.item("evolvedmekanism:" + tier.name() + "_tier_installer"), 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', tier.alloy(), 'C', tier.circuit(), 'I', tier.extra(), 'P', ItemTags.PLANKS),
                has(tier.circuit()));
    }

    private void addTransmitterSet(Consumer<FinishedRecipe> output, TieredRecipes.TierData tier) {
        addTransmitter(output, tier, "universal_cable", MekanismBlocks.ULTIMATE_UNIVERSAL_CABLE);
        addTransmitter(output, tier, "mechanical_pipe", MekanismBlocks.ULTIMATE_MECHANICAL_PIPE);
        addTransmitter(output, tier, "pressurized_tube", MekanismBlocks.ULTIMATE_PRESSURIZED_TUBE);
        addTransmitter(output, tier, "logistical_transporter", MekanismBlocks.ULTIMATE_LOGISTICAL_TRANSPORTER);
        addTransmitter(output, tier, "thermodynamic_conductor", MekanismBlocks.ULTIMATE_THERMODYNAMIC_CONDUCTOR);
    }

    private void addTransmitter(Consumer<FinishedRecipe> output, TieredRecipes.TierData tier, String type, ItemLike ultimate) {
        ItemLike previous = tier.previousMekanism() ? ultimate : EMCrafting.item("evolvedmekanism:" + tier.previousPrefix() + "_" + type);
        EMCrafting.shaped(output, "transmitter/" + type + "/" + tier.name(),
                EMCrafting.item("evolvedmekanism:" + tier.name() + "_" + type), 8, new String[]{"PPP", "PAP", "PPP"},
                EMCrafting.keys('A', tier.alloy(), 'P', previous), has(previous));
    }

    private void addSolarGenerator(Consumer<FinishedRecipe> mek, TieredRecipes.TierData tier) {
        ItemLike prev;
        switch (tier.name()) {
            case "overclocked" -> prev = EMGenBlocks.ULTIMATE_SOLAR_GENERATOR;
            case "quantum" -> prev = EMGenBlocks.OVERCLOCKED_SOLAR_GENERATOR;
            case "dense" -> prev = EMGenBlocks.QUANTUM_SOLAR_GENERATOR;
            case "multiversal" -> prev = EMGenBlocks.DENSE_SOLAR_GENERATOR;
            case "creative" -> prev = EMGenBlocks.MULTIVERSAL_SOLAR_GENERATOR;
            default -> throw new IllegalArgumentException(tier.name());
        }
        EMCrafting.shaped(mek, "solar_generators/" + tier.name() + "_generator",
                EMCrafting.item("evolvedmekanism:" + tier.name() + "_solar_generator"), 1, new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', tier.alloy(), 'C', tier.circuit(), 'I', tier.extra(), 'P', prev),
                has(prev), new ModLoadedCondition("mekanismgenerators"));
    }

    private void addProcessingRecipes(Consumer<FinishedRecipe> output) {
        addIngotSet(output, "better_gold", EMItems.BETTER_GOLD_INGOT, EMItems.BETTER_GOLD_NUGGET, EMBlocks.BETTER_GOLD_BLOCK, EMItems.BETTER_GOLD_DUST, true);
        addIngotSet(output, "plaslitherite", EMItems.PLASLITHERITE_INGOT, EMItems.PLASLITHERITE_NUGGET, EMBlocks.PLASLITHERITE_BLOCK, EMItems.PLASLITHERITE_DUST, true);
        addIngotSet(output, "refined_redstone", EMItems.REFINED_REDSTONE_INGOT, EMItems.REFINED_REDSTONE_NUGGET, EMBlocks.REFINED_REDSTONE_BLOCK, null, false);
        ItemStackToItemStackRecipeBuilder.crushing(IngredientCreatorAccess.item().from(EMDatagenTags.forgeItem("ingots/refined_redstone")), new ItemStack(Items.REDSTONE))
                .build(output, EvolvedMekanism.rl("processing/refined_redstone/ingot_to_dust"));
        ItemStackChemicalToItemStackRecipeBuilder.compressing(
                IngredientCreatorAccess.item().from(Tags.Items.DUSTS_REDSTONE),
                IngredientCreatorAccess.gas().from(MekanismGases.OSMIUM, 1),
                EMItems.REFINED_REDSTONE_INGOT.getItemStack()
        ).build(output, EvolvedMekanism.rl("processing/refined_redstone/ingot/from_dust"));

        EMCrafting.shapeless(output, EvolvedMekanism.rl("processing/noctis_rozuli/from_block"), EMItems.NOCTIS_ROZULI, 9,
                EMTags.Items.STORAGE_BLOCKS_NOCTIS_ROZULI, has(EMItems.NOCTIS_ROZULI));
        EMCrafting.shaped(output, "processing/noctis_rozuli/from_gems", EMBlocks.NOCTIS_ROZULI_BLOCK, 1, new String[]{"###", "#P#", "###"},
                EMCrafting.keys('#', EMTags.Items.GEMS_NOCTIS_ROZULI, 'P', EMItems.NOCTIS_ROZULI), has(EMItems.NOCTIS_ROZULI));
        ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(EMTags.Items.DUSTS_NOCTIS_ROZULI), EMItems.NOCTIS_ROZULI.getItemStack())
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/from_dust"));
        ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(EMTags.Items.ORES_NOCTIS_ROZULI), new ItemStack(EMItems.NOCTIS_ROZULI, 12))
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/from_ore"));
        ItemStackToItemStackRecipeBuilder.crushing(IngredientCreatorAccess.item().from(EMTags.Items.GEMS_NOCTIS_ROZULI), EMItems.NOCTIS_ROZULI_DUST.getItemStack())
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/to_dust"));
        CombinerRecipeBuilder.combining(IngredientCreatorAccess.item().from(EMTags.Items.DUSTS_NOCTIS_ROZULI, 27),
                IngredientCreatorAccess.item().from(EMDatagenTags.forgeItem("cobblestone/normal")), EMCrafting.stack(EMBlocks.NOCTIS_ROZULI_ORE))
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/to_ore"));
        CombinerRecipeBuilder.combining(IngredientCreatorAccess.item().from(EMTags.Items.DUSTS_NOCTIS_ROZULI, 27),
                IngredientCreatorAccess.item().from(EMDatagenTags.forgeItem("cobblestone/deepslate")), EMCrafting.stack(EMBlocks.DEEPSLATE_NOCTIS_ROZULI_ORE))
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/to_deepslate_ore"));
        ItemStackToChemicalRecipeBuilder.oxidizing(IngredientCreatorAccess.item().from(Items.PITCHER_PLANT), new GasStack(EMGases.NITROGEN.get(), 250))
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/nitrogen"));
        PressurizedReactionRecipeBuilder.reaction(
                IngredientCreatorAccess.item().from(EMTags.Items.GEMS_NOCTIS_ROZULI),
                IngredientCreatorAccess.fluid().from(EMDatagenTags.forgeFluid("nitrogen"), 1),
                IngredientCreatorAccess.gas().from(EMTags.Gases.NITROGEN, 1),
                60,
                new GasStack(EMGases.CRYONOCTIS.get(), 1000)
        ).build(output, EvolvedMekanism.rl("processing/noctis_rozuli/cryonoctis"));
        PressurizedReactionRecipeBuilder.reaction(
                IngredientCreatorAccess.item().from(EMTags.Items.STORAGE_BLOCKS_NOCTIS_ROZULI),
                IngredientCreatorAccess.fluid().from(EMDatagenTags.forgeFluid("nitrogen"), 9),
                IngredientCreatorAccess.gas().from(EMTags.Gases.NITROGEN, 9),
                60,
                new GasStack(EMGases.CRYONOCTIS.get(), 9000)
        ).build(output, EvolvedMekanism.rl("processing/noctis_rozuli/cryonoctis_from_block"));

        addDimOreProcessing(output, "osmium", MekanismItems.PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.OSMIUM), EMDatagenTags.forgeItem("raw_materials/osmium"));
        addDimOreProcessing(output, "tin", MekanismItems.PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.TIN), EMDatagenTags.forgeItem("raw_materials/tin"));
        addDimOreProcessing(output, "lead", MekanismItems.PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.LEAD), EMDatagenTags.forgeItem("raw_materials/lead"));
        addDimOreProcessing(output, "uranium", MekanismItems.PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.URANIUM), EMDatagenTags.forgeItem("raw_materials/uranium"));
        CombinerRecipeBuilder.combining(IngredientCreatorAccess.item().from(EMDatagenTags.forgeItem("dusts/fluorite"), 14),
                IngredientCreatorAccess.item().from(Blocks.NETHERRACK), EMCrafting.stack(EMCrafting.item("evolvedmekanism:netherrack_fluorite_ore")))
                .build(output, EvolvedMekanism.rl("processing/fluorite/to_netherrack_ore"));
        CombinerRecipeBuilder.combining(IngredientCreatorAccess.item().from(EMDatagenTags.forgeItem("dusts/fluorite"), 14),
                IngredientCreatorAccess.item().from(Blocks.END_STONE), EMCrafting.stack(EMCrafting.item("evolvedmekanism:end_stone_fluorite_ore")))
                .build(output, EvolvedMekanism.rl("processing/fluorite/to_end_stone_ore"));

        addAlloyBlock(output, "infused", MekanismItems.INFUSED_ALLOY, MekanismTags.Items.ALLOYS_INFUSED, EMBlocks.INFUSED_ALLOY_BLOCK);
        addAlloyBlock(output, "reinforced", MekanismItems.REINFORCED_ALLOY, MekanismTags.Items.ALLOYS_REINFORCED, EMBlocks.REINFORCED_ALLOY_BLOCK);
        addAlloyBlock(output, "atomic", MekanismItems.ATOMIC_ALLOY, MekanismTags.Items.ALLOYS_ATOMIC, EMBlocks.ATOMIC_ALLOY_BLOCK);
        addAlloyBlock(output, "hypercharged", EMItems.HYPERCHARGED_ALLOY, EMDatagenTags.item("evolvedmekanism", "alloys/hypercharged"), EMBlocks.HYPERCHARGED_ALLOY_BLOCK);
        addAlloyBlock(output, "subatomic", EMItems.SUBATOMIC_ALLOY, EMDatagenTags.item("evolvedmekanism", "alloys/subatomic"), EMBlocks.SUBATOMIC_ALLOY_BLOCK);
        addAlloyBlock(output, "singular", EMItems.SINGULAR_ALLOY, EMDatagenTags.item("evolvedmekanism", "alloys/singular"), EMBlocks.SINGULAR_ALLOY_BLOCK);
        addAlloyBlock(output, "exoversal", EMItems.EXOVERSAL_ALLOY, EMDatagenTags.item("evolvedmekanism", "alloys/exoversal"), EMBlocks.EXOVERSAL_ALLOY_BLOCK);
        addAlloyBlock(output, "creative", EMItems.CREATIVE_ALLOY, EMDatagenTags.item("evolvedmekanism", "alloys/creative"), EMBlocks.CREATIVE_ALLOY_BLOCK);

        ItemStackToChemicalRecipeBuilder.infusionConversion(IngredientCreatorAccess.item().from(EMDatagenTags.forgeItem("dusts/better_gold")), EMInfuseTypes.BETTER_GOLD.getStack(10))
                .build(output, EvolvedMekanism.rl("infusion_conversion/better_gold/from_dust"));
        ItemStackToChemicalRecipeBuilder.infusionConversion(IngredientCreatorAccess.item().from(EMDatagenTags.item("evolvedmekanism", "enriched/better_gold")), EMInfuseTypes.BETTER_GOLD.getStack(80))
                .build(output, EvolvedMekanism.rl("infusion_conversion/better_gold/from_enriched"));
        ItemStackToChemicalRecipeBuilder.infusionConversion(IngredientCreatorAccess.item().from(EMDatagenTags.forgeItem("dusts/plaslitherite")), EMInfuseTypes.PLASLITHERITE.getStack(10))
                .build(output, EvolvedMekanism.rl("infusion_conversion/plaslitherite/from_dust"));
        ItemStackToChemicalRecipeBuilder.infusionConversion(IngredientCreatorAccess.item().from(EMDatagenTags.item("evolvedmekanism", "enriched/plaslitherite")), EMInfuseTypes.PLASLITHERITE.getStack(80))
                .build(output, EvolvedMekanism.rl("infusion_conversion/plaslitherite/from_enriched"));
        ItemStackToChemicalRecipeBuilder.infusionConversion(IngredientCreatorAccess.item().from(EMDatagenTags.forgeItem("dusts/uranium")), EMInfuseTypes.URANIUM.getStack(10))
                .build(output, EvolvedMekanism.rl("infusion_conversion/uranium/from_dust"));
        ItemStackToChemicalRecipeBuilder.infusionConversion(IngredientCreatorAccess.item().from(EMDatagenTags.item("evolvedmekanism", "enriched/uranium")), EMInfuseTypes.URANIUM.getStack(80))
                .build(output, EvolvedMekanism.rl("infusion_conversion/uranium/from_enriched"));

        ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(EMDatagenTags.forgeItem("dusts/better_gold")), EMItems.ENRICHED_BETTER_GOLD.getItemStack())
                .build(output, EvolvedMekanism.rl("enriching/enriched/better_gold"));
        ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(EMDatagenTags.forgeItem("dusts/plaslitherite")), EMItems.ENRICHED_PLASLITHERITE.getItemStack())
                .build(output, EvolvedMekanism.rl("enriching/enriched/plaslitherite"));
        ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(EMDatagenTags.forgeItem("dusts/uranium")), EMItems.ENRICHED_URANIUM.getItemStack())
                .build(output, EvolvedMekanism.rl("enriching/enriched/uranium"));
    }

    private void addIngotSet(Consumer<FinishedRecipe> output, String name, ItemLike ingot, ItemLike nugget, ItemLike block, ItemLike dust, boolean dustRecipes) {
        EMCrafting.shapeless(output, EvolvedMekanism.rl("processing/" + name + "/ingot/from_block"), ingot, 9, EMDatagenTags.forgeItem("storage_blocks/" + name), has(ingot));
        EMCrafting.shapeless(output, EvolvedMekanism.rl("processing/" + name + "/nugget/from_ingot"), nugget, 9, EMDatagenTags.forgeItem("ingots/" + name), has(ingot));
        EMCrafting.shaped(output, "processing/" + name + "/ingot/from_nuggets", ingot, 1, new String[]{"###", "#P#", "###"},
                EMCrafting.keys('#', EMDatagenTags.forgeItem("nuggets/" + name), 'P', nugget), has(nugget));
        EMCrafting.shaped(output, "processing/" + name + "/storage_blocks/from_ingots", block, 1, new String[]{"###", "#P#", "###"},
                EMCrafting.keys('#', EMDatagenTags.forgeItem("ingots/" + name), 'P', ingot), has(ingot));
        if (dustRecipes && dust != null) {
            ItemStackToItemStackRecipeBuilder.crushing(IngredientCreatorAccess.item().from(EMDatagenTags.forgeItem("ingots/" + name)), new ItemStack(dust.asItem()))
                    .build(output, EvolvedMekanism.rl("processing/" + name + "/dust/from_ingot"));
            EMCrafting.smelting(output, EvolvedMekanism.rl("processing/" + name + "/ingot/from_dust_smelting"),
                    Ingredient.of(EMDatagenTags.forgeItem("dusts/" + name)), ingot, 0.3F, 200, has(dust));
            EMCrafting.blasting(output, EvolvedMekanism.rl("processing/" + name + "/ingot/from_dust_blasting"),
                    Ingredient.of(EMDatagenTags.forgeItem("dusts/" + name)), ingot, 0.3F, 100, has(dust));
        }
    }

    private void addAlloyBlock(Consumer<FinishedRecipe> output, String name, ItemLike alloy, Object alloyTag, ItemLike block) {
        EMCrafting.shapeless(output, EvolvedMekanism.rl("block_alloys/" + name + "/from_block"), alloy, 9, block, has(block));
        EMCrafting.shaped(output, "block_alloys/" + name + "/from_ingots", block, 1, new String[]{"###", "#P#", "###"},
                EMCrafting.keys('#', alloyTag, 'P', alloy), has(alloy));
    }

    private void addDimOreProcessing(Consumer<FinishedRecipe> output, String ore, ItemLike ingot, net.minecraft.tags.TagKey<net.minecraft.world.item.Item> rawTag) {
        Ingredient ores = Ingredient.of(
                EMCrafting.item("evolvedmekanism:netherrack_" + ore + "_ore"),
                EMCrafting.item("evolvedmekanism:end_stone_" + ore + "_ore"),
                EMCrafting.item("evolvedmekanism:depthrock_" + ore + "_ore"),
                EMCrafting.item("evolvedmekanism:shiverstone_" + ore + "_ore"),
                EMCrafting.item("evolvedmekanism:holystone_" + ore + "_ore")
        );
        EMCrafting.smelting(output, EvolvedMekanism.rl("processing/" + ore + "/ingot/from_ore_smelting"), ores, ingot, 0.6F, 200, has(ingot));
        EMCrafting.blasting(output, EvolvedMekanism.rl("processing/" + ore + "/ingot/from_ore_blasting"), ores, ingot, 0.6F, 100, has(ingot));
        CombinerRecipeBuilder.combining(IngredientCreatorAccess.item().from(rawTag, 8),
                IngredientCreatorAccess.item().from(Blocks.NETHERRACK),
                EMCrafting.stack(EMCrafting.item("evolvedmekanism:netherrack_" + ore + "_ore")))
                .build(output, EvolvedMekanism.rl("processing/" + ore + "/ore/netherrack_from_raw"));
        CombinerRecipeBuilder.combining(IngredientCreatorAccess.item().from(rawTag, 8),
                IngredientCreatorAccess.item().from(Blocks.END_STONE),
                EMCrafting.stack(EMCrafting.item("evolvedmekanism:end_stone_" + ore + "_ore")))
                .build(output, EvolvedMekanism.rl("processing/" + ore + "/ore/end_stone_from_raw"));
    }

    private void addToolRecipes(Consumer<FinishedRecipe> output) {
        addToolSet(output, "better_gold", EMDatagenTags.forgeItem("ingots/better_gold"), EMItems.BETTER_GOLD_NUGGET,
                EMToolsItems.BETTER_GOLD_HELMET, EMToolsItems.BETTER_GOLD_CHESTPLATE, EMToolsItems.BETTER_GOLD_LEGGINGS, EMToolsItems.BETTER_GOLD_BOOTS,
                EMToolsItems.BETTER_GOLD_SWORD, EMToolsItems.BETTER_GOLD_PICKAXE, EMToolsItems.BETTER_GOLD_AXE, EMToolsItems.BETTER_GOLD_SHOVEL,
                EMToolsItems.BETTER_GOLD_HOE, EMToolsItems.BETTER_GOLD_PAXEL, EMToolsItems.BETTER_GOLD_SHIELD);
        addToolSet(output, "plaslitherite", EMDatagenTags.forgeItem("ingots/plaslitherite"), EMItems.PLASLITHERITE_NUGGET,
                EMToolsItems.PLASLITHERITE_HELMET, EMToolsItems.PLASLITHERITE_CHESTPLATE, EMToolsItems.PLASLITHERITE_LEGGINGS, EMToolsItems.PLASLITHERITE_BOOTS,
                EMToolsItems.PLASLITHERITE_SWORD, EMToolsItems.PLASLITHERITE_PICKAXE, EMToolsItems.PLASLITHERITE_AXE, EMToolsItems.PLASLITHERITE_SHOVEL,
                EMToolsItems.PLASLITHERITE_HOE, EMToolsItems.PLASLITHERITE_PAXEL, EMToolsItems.PLASLITHERITE_SHIELD);
        addToolSet(output, "refined_redstone", EMDatagenTags.forgeItem("ingots/refined_redstone"), EMItems.REFINED_REDSTONE_NUGGET,
                EMToolsItems.REFINED_REDSTONE_HELMET, EMToolsItems.REFINED_REDSTONE_CHESTPLATE, EMToolsItems.REFINED_REDSTONE_LEGGINGS, EMToolsItems.REFINED_REDSTONE_BOOTS,
                EMToolsItems.REFINED_REDSTONE_SWORD, EMToolsItems.REFINED_REDSTONE_PICKAXE, EMToolsItems.REFINED_REDSTONE_AXE, EMToolsItems.REFINED_REDSTONE_SHOVEL,
                EMToolsItems.REFINED_REDSTONE_HOE, EMToolsItems.REFINED_REDSTONE_PAXEL, EMToolsItems.REFINED_REDSTONE_SHIELD);
        addToolSet(output, "noctis_rozuli", EMDatagenTags.forgeItem("gems/noctis_rozuli"), null,
                EMToolsItems.NOCTIS_ROZULI_HELMET, EMToolsItems.NOCTIS_ROZULI_CHESTPLATE, EMToolsItems.NOCTIS_ROZULI_LEGGINGS, EMToolsItems.NOCTIS_ROZULI_BOOTS,
                EMToolsItems.NOCTIS_ROZULI_SWORD, EMToolsItems.NOCTIS_ROZULI_PICKAXE, EMToolsItems.NOCTIS_ROZULI_AXE, EMToolsItems.NOCTIS_ROZULI_SHOVEL,
                EMToolsItems.NOCTIS_ROZULI_HOE, EMToolsItems.NOCTIS_ROZULI_PAXEL, EMToolsItems.NOCTIS_ROZULI_SHIELD);
    }

    private void addToolSet(Consumer<FinishedRecipe> output, String name, Object material, ItemLike nugget,
                            ItemLike helmet, ItemLike chest, ItemLike legs, ItemLike boots,
                            ItemLike sword, ItemLike pickaxe, ItemLike axe, ItemLike shovel, ItemLike hoe, ItemLike paxel, ItemLike shield) {
        ModLoadedCondition tools = new ModLoadedCondition("mekanismtools");
        Consumer<FinishedRecipe> gated = EMCrafting.withConditions(output, tools);
        Object rod = EMDatagenTags.forgeItem("rods/wooden");
        EMCrafting.shaped(gated, "tools/" + name + "/armor/helmet", helmet, 1, new String[]{"III", "I I"}, EMCrafting.keys('I', material), has(Items.IRON_INGOT));
        EMCrafting.shaped(gated, "tools/" + name + "/armor/chestplate", chest, 1, new String[]{"I I", "III", "III"}, EMCrafting.keys('I', material), has(Items.IRON_INGOT));
        EMCrafting.shaped(gated, "tools/" + name + "/armor/leggings", legs, 1, new String[]{"III", "I I", "I I"}, EMCrafting.keys('I', material), has(Items.IRON_INGOT));
        EMCrafting.shaped(gated, "tools/" + name + "/armor/boots", boots, 1, new String[]{"I I", "I I"}, EMCrafting.keys('I', material), has(Items.IRON_INGOT));
        EMCrafting.shaped(gated, "tools/" + name + "/tools/sword", sword, 1, new String[]{"I", "I", "R"}, EMCrafting.keys('I', material, 'R', rod), has(Items.IRON_INGOT));
        EMCrafting.shaped(gated, "tools/" + name + "/tools/pickaxe", pickaxe, 1, new String[]{"III", " R ", " R "}, EMCrafting.keys('I', material, 'R', rod), has(Items.IRON_INGOT));
        EMCrafting.shaped(gated, "tools/" + name + "/tools/axe", axe, 1, new String[]{"II", "IR", " R"}, EMCrafting.keys('I', material, 'R', rod), has(Items.IRON_INGOT));
        EMCrafting.shaped(gated, "tools/" + name + "/tools/shovel", shovel, 1, new String[]{"I", "R", "R"}, EMCrafting.keys('I', material, 'R', rod), has(Items.IRON_INGOT));
        EMCrafting.shaped(gated, "tools/" + name + "/tools/hoe", hoe, 1, new String[]{"II", " R", " R"}, EMCrafting.keys('I', material, 'R', rod), has(Items.IRON_INGOT));
        EMCrafting.shaped(gated, "tools/" + name + "/shield", shield, 1, new String[]{"IPI", "III", " I "}, EMCrafting.keys('I', material, 'P', Items.SHIELD), has(Items.SHIELD));
        EMCrafting.shaped(EMCrafting.paxel(gated), "tools/" + name + "/tools/paxel", paxel, 1, new String[]{"APS", " R ", " R "},
                EMCrafting.keys('A', EMDatagenTags.forgeItem("tools/axes/" + name), 'P', EMDatagenTags.forgeItem("tools/pickaxes/" + name),
                        'S', EMDatagenTags.forgeItem("tools/shovels/" + name), 'R', rod), has(Items.IRON_INGOT));
        if (nugget != null) {
            Ingredient scrap = Ingredient.of(helmet, chest, legs, boots, sword, pickaxe, axe, shovel, hoe, paxel);
            EMCrafting.smelting(gated, EvolvedMekanism.rl("tools/" + name + "/nugget_from_smelting"), scrap, nugget, 0.1F, 200, has(nugget));
            EMCrafting.blasting(gated, EvolvedMekanism.rl("tools/" + name + "/nugget_from_blasting"), scrap, nugget, 0.1F, 100, has(nugget));
        }
    }

    private void addMiscRecipes(Consumer<FinishedRecipe> output) {
        addMold(output, EMItems.MOLD_INGOT, EMDatagenTags.forgeItem("ingots"), "ingot");
        addMold(output, EMItems.MOLD_NUGGET, EMDatagenTags.forgeItem("nuggets"), "nugget");
        addMold(output, EMItems.MOLD_BLOCK, EMDatagenTags.forgeItem("storage_blocks"), "storage_block");
        addMold(output, EMItems.MOLD_DUST, EMDatagenTags.forgeItem("dusts"), "dust");
        addMold(output, EMItems.MOLD_PLATE, EMDatagenTags.forgeItem("plates"), "plate");
        addMold(output, EMItems.MOLD_GEAR, EMDatagenTags.forgeItem("gears"), "gear");
        addMold(output, EMItems.MOLD_ROD, EMDatagenTags.forgeItem("rods"), "rod");
        addMold(output, EMItems.MOLD_WIRE, EMDatagenTags.forgeItem("wires"), "wire");
        addMold(output, EMItems.MOLD_COIN, EMDatagenTags.forgeItem("coins"), "coin");
        addMold(output, EMItems.MOLD_GEM, EMDatagenTags.forgeItem("gems"), "gem");

        ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
                IngredientCreatorAccess.item().from(MekanismTags.Items.ALLOYS_ATOMIC),
                IngredientCreatorAccess.infusion().from(EMTags.InfuseTypes.URANIUM, 20),
                EMItems.HYPERCHARGED_ALLOY.getItemStack()
        ).build(output, EvolvedMekanism.rl("metallurgic_infusing/alloy/hypercharged"));
        ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
                IngredientCreatorAccess.item().from(EMDatagenTags.item("evolvedmekanism", "alloys/subatomic")),
                IngredientCreatorAccess.infusion().from(EMTags.InfuseTypes.BETTER_GOLD, 20),
                EMItems.SINGULAR_ALLOY.getItemStack()
        ).build(output, EvolvedMekanism.rl("metallurgic_infusing/alloy/singular"));
        ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
                IngredientCreatorAccess.item().from(EMDatagenTags.item("evolvedmekanism", "alloys/singular")),
                IngredientCreatorAccess.infusion().from(EMTags.InfuseTypes.PLASLITHERITE, 20),
                EMItems.EXOVERSAL_ALLOY.getItemStack()
        ).build(output, EvolvedMekanism.rl("metallurgic_infusing/alloy/exoversal"));

        NucleosynthesizingRecipeBuilder.nucleosynthesizing(
                IngredientCreatorAccess.item().from(EMDatagenTags.item("evolvedmekanism", "alloys/hypercharged")),
                IngredientCreatorAccess.gas().from(MekanismGases.ANTIMATTER, 50),
                EMItems.SUBATOMIC_ALLOY.getItemStack(), 1000
        ).build(output, EvolvedMekanism.rl("nucleosynthesizing/alloy_subatomic"));
        NucleosynthesizingRecipeBuilder.nucleosynthesizing(
                IngredientCreatorAccess.item().from(MekanismBlocks.SPS_CASING),
                IngredientCreatorAccess.gas().from(MekanismGases.ANTIMATTER, 50),
                EMBlocks.APT_CASING.getItemStack(), 1000
        ).build(output, EvolvedMekanism.rl("nucleosynthesizing/apt_casing"));
        NucleosynthesizingRecipeBuilder.nucleosynthesizing(
                IngredientCreatorAccess.item().from(Items.PINK_WOOL),
                IngredientCreatorAccess.gas().from(MekanismGases.ANTIMATTER, 2),
                EMBlocks.NOCTIS_ROZULI_BLOCK.getItemStack(), 500
        ).build(output, EvolvedMekanism.rl("nucleosynthesizing/noctis_block"));

        addApt(output, "apt/ingot_better_gold", "forge:ingots/gold", 100, "evolvedmekanism:ingot_better_gold");
        addApt(output, "apt/dust_better_gold", "forge:dusts/gold", 100, "evolvedmekanism:dust_better_gold");
        addApt(output, "apt/block_better_gold", "forge:storage_blocks/gold", 900, "evolvedmekanism:block_better_gold");

        addChemixing(output, "chemixing/ingot_plaslitherite", "forge:ingots/netherite", "mekanism:hdpe_pellet", 3, 100, "evolvedmekanism:ingot_plaslitherite");
        addChemixing(output, "chemixing/dust_plaslitherite", "forge:dusts/netherite", "mekanism:hdpe_pellet", 3, 100, "evolvedmekanism:dust_plaslitherite");
        addChemixing(output, "chemixing/block_plaslitherite", "forge:storage_blocks/netherite", "mekanism:hdpe_sheet", 3, 900, "evolvedmekanism:block_plaslitherite");

        EMCrafting.shaped(output, "upgrade/radioactive", EMItems.RADIOACTIVE_UPGRADE, 1, new String[]{" G ", "A#A", " G "},
                EMCrafting.keys('#', EMDatagenTags.forgeItem("pellets/antimatter"), 'A', EMDatagenTags.item("evolvedmekanism", "alloys/singular"),
                        'G', EMDatagenTags.forgeItem("glass/silica")),
                has(EMItems.SINGULAR_ALLOY));
        EMCrafting.shaped(output, "upgrade/solar", EMGenItems.SOLAR_UPGRADE, 1, new String[]{" G ", "A#A", " G "},
                EMCrafting.keys('#', Tags.Items.DUSTS_GLOWSTONE, 'A', EMDatagenTags.item("evolvedmekanism", "alloys/singular"),
                        'G', EMDatagenTags.forgeItem("glass/silica")),
                has(EMItems.SINGULAR_ALLOY), new ModLoadedCondition("mekanismgenerators"));

        Consumer<FinishedRecipe> gen = EMCrafting.withConditions(EMCrafting.mekData(output), new ModLoadedCondition("mekanismgenerators"));
        EMCrafting.shaped(gen, "solar_generators/advanced_generator", EMGenBlocks.ADVANCED_SOLAR_GENERATOR, 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_INFUSED, 'C', EMDatagenTags.forgeItem("circuits/advanced"),
                        'I', EMDatagenTags.forgeItem("ingots/osmium"), 'P', EMCrafting.item("mekanismgenerators:advanced_solar_generator")),
                has(EMDatagenTags.forgeItem("circuits/advanced")));
        EMCrafting.shaped(gen, "solar_generators/elite_generator", EMGenBlocks.ELITE_SOLAR_GENERATOR, 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_REINFORCED, 'C', EMDatagenTags.forgeItem("circuits/elite"),
                        'I', EMDatagenTags.forgeItem("ingots/gold"), 'P', EMGenBlocks.ADVANCED_SOLAR_GENERATOR),
                has(EMGenBlocks.ADVANCED_SOLAR_GENERATOR));
        EMCrafting.shaped(gen, "solar_generators/ultimate_generator", EMGenBlocks.ULTIMATE_SOLAR_GENERATOR, 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_ATOMIC, 'C', EMDatagenTags.forgeItem("circuits/ultimate"),
                        'I', Tags.Items.GEMS_DIAMOND, 'P', EMGenBlocks.ELITE_SOLAR_GENERATOR),
                has(EMGenBlocks.ELITE_SOLAR_GENERATOR));
        RotaryRecipeBuilder.rotary(
                IngredientCreatorAccess.fluid().from(EMDatagenTags.forgeFluid("nitrogen"), 1),
                IngredientCreatorAccess.gas().from(EMTags.Gases.NITROGEN, 1),
                new GasStack(EMGases.NITROGEN.get(), 1),
                EMFluids.NITROGEN.getFluidStack(1)
        ).build(output, EvolvedMekanism.rl("rotary/nitrogen"));
        RotaryRecipeBuilder.rotary(
                IngredientCreatorAccess.fluid().from(EMDatagenTags.forgeFluid("cryonoctis"), 1),
                IngredientCreatorAccess.gas().from(EMTags.Gases.CRYONOCTIS, 1),
                new GasStack(EMGases.CRYONOCTIS.get(), 1),
                EMFluids.CRYONOCTIS.getFluidStack(1)
        ).build(output, EvolvedMekanism.rl("rotary/cryonoctis"));
        EMCrafting.shaped(output, "lunar_panel", EMGenItems.LUNAR_PANEL, 1, new String[]{"GGG", "RAR", "OOO"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_INFUSED, 'G', Tags.Items.GLASS_PANES,
                        'O', EMTags.Items.GEMS_NOCTIS_ROZULI, 'R', Tags.Items.DUSTS_REDSTONE),
                has(Tags.Items.DUSTS_REDSTONE), new ModLoadedCondition("mekanismgenerators"));
        EMCrafting.shaped(output, "upgrade/lunar", EMGenItems.LUNAR_UPGRADE, 1, new String[]{" G ", "A#A", " G "},
                EMCrafting.keys('#', EMTags.Items.DUSTS_NOCTIS_ROZULI, 'A', EMDatagenTags.item("evolvedmekanism", "alloys/singular"),
                        'G', EMDatagenTags.forgeItem("glass/silica")),
                has(EMItems.SINGULAR_ALLOY), new ModLoadedCondition("mekanismgenerators"));
        EMCrafting.shaped(EMCrafting.mekData(gen), "lunar_generators/generator", EMGenBlocks.LUNAR_GENERATOR, 1, new String[]{"###", "AIA", "OEO"},
                EMCrafting.keys('#', EMGenItems.LUNAR_PANEL, 'A', MekanismTags.Items.ALLOYS_INFUSED, 'E', MekanismItems.ENERGY_TABLET,
                        'I', Tags.Items.INGOTS_IRON, 'O', EMDatagenTags.forgeItem("ingots/osmium")),
                has(EMGenItems.LUNAR_PANEL));
        EMCrafting.shaped(EMCrafting.mekData(gen), "lunar_generators/basic_advanced_generator", EMGenBlocks.BASIC_ADVANCED_LUNAR_GENERATOR, 1,
                new String[]{"PAP", "PAP", "CCC"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_INFUSED, 'C', EMTags.Items.GEMS_NOCTIS_ROZULI, 'P', EMGenBlocks.LUNAR_GENERATOR),
                has(EMGenBlocks.LUNAR_GENERATOR));
        EMCrafting.shaped(EMCrafting.mekData(gen), "lunar_generators/advanced_generator", EMGenBlocks.ADVANCED_LUNAR_GENERATOR, 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_INFUSED, 'C', EMDatagenTags.forgeItem("circuits/advanced"),
                        'I', EMDatagenTags.forgeItem("ingots/osmium"), 'P', EMGenBlocks.BASIC_ADVANCED_LUNAR_GENERATOR),
                has(EMGenBlocks.BASIC_ADVANCED_LUNAR_GENERATOR));
        EMCrafting.shaped(EMCrafting.mekData(gen), "lunar_generators/elite_generator", EMGenBlocks.ELITE_LUNAR_GENERATOR, 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_REINFORCED, 'C', EMDatagenTags.forgeItem("circuits/elite"),
                        'I', EMDatagenTags.forgeItem("ingots/gold"), 'P', EMGenBlocks.ADVANCED_LUNAR_GENERATOR),
                has(EMGenBlocks.ADVANCED_LUNAR_GENERATOR));
        EMCrafting.shaped(EMCrafting.mekData(gen), "lunar_generators/ultimate_generator", EMGenBlocks.ULTIMATE_LUNAR_GENERATOR, 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_ATOMIC, 'C', EMDatagenTags.forgeItem("circuits/ultimate"),
                        'I', Tags.Items.GEMS_DIAMOND, 'P', EMGenBlocks.ELITE_LUNAR_GENERATOR),
                has(EMGenBlocks.ELITE_LUNAR_GENERATOR));
    }

    private void addLunarGenerator(Consumer<FinishedRecipe> mek, TieredRecipes.TierData tier) {
        ItemLike prev;
        switch (tier.name()) {
            case "overclocked" -> prev = EMGenBlocks.ULTIMATE_LUNAR_GENERATOR;
            case "quantum" -> prev = EMGenBlocks.OVERCLOCKED_LUNAR_GENERATOR;
            case "dense" -> prev = EMGenBlocks.QUANTUM_LUNAR_GENERATOR;
            case "multiversal" -> prev = EMGenBlocks.DENSE_LUNAR_GENERATOR;
            case "creative" -> prev = EMGenBlocks.MULTIVERSAL_LUNAR_GENERATOR;
            default -> throw new IllegalArgumentException(tier.name());
        }
        EMCrafting.shaped(mek, "lunar_generators/" + tier.name() + "_generator",
                EMCrafting.item("evolvedmekanism:" + tier.name() + "_lunar_generator"), 1, new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', tier.alloy(), 'C', tier.circuit(), 'I', tier.extra(), 'P', prev),
                has(prev), new ModLoadedCondition("mekanismgenerators"));
    }

    private void addApt(Consumer<FinishedRecipe> output, String path, String itemTag, int gas, String result) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "evolvedmekanism:apt");
        JsonObject chemical = new JsonObject();
        chemical.addProperty("amount", gas);
        chemical.addProperty("gas", "mekanism:spent_nuclear_waste");
        json.add("chemicalInput", chemical);
        json.add("itemInput", EMJsonRecipes.tagIngredient(itemTag));
        json.add("output", EMJsonRecipes.itemResult(result, 1));
        EMJsonRecipes.save(output, EvolvedMekanism.rl(path), json);
    }

    private void addChemixing(Consumer<FinishedRecipe> output, String path, String mainTag, String extraItem, int extraAmount, int gas, String result) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "evolvedmekanism:chemixing");
        json.add("mainInput", EMJsonRecipes.tagIngredient(mainTag));
        json.add("extraInput", EMJsonRecipes.countedItemIngredient(extraItem, extraAmount));
        JsonObject gasInput = new JsonObject();
        gasInput.addProperty("amount", gas);
        gasInput.addProperty("gas", "mekanism:lithium");
        json.add("gasInput", gasInput);
        json.add("output", EMJsonRecipes.itemResult(result, 1));
        EMJsonRecipes.save(output, EvolvedMekanism.rl(path), json);
    }

    private void addMold(Consumer<FinishedRecipe> output, ItemLike mold, net.minecraft.tags.TagKey<net.minecraft.world.item.Item> center, String name) {
        EMCrafting.shaped(output, "molds/" + name, mold, 1, new String[]{"ABA", "BCB", "ABA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_INFUSED, 'B', EMDatagenTags.forgeItem("storage_blocks/steel"), 'C', center),
                has(MekanismTags.Items.ALLOYS_INFUSED), new NotCondition(new TagEmptyCondition(center.location().toString())));
    }
}
