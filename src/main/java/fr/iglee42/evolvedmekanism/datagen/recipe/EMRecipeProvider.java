package fr.iglee42.evolvedmekanism.datagen.recipe;

import fr.iglee42.emgenerators.registries.EMGenBlocks;
import fr.iglee42.emgenerators.registries.EMGenItems;
import fr.iglee42.emtools.registries.EMToolsItems;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.datagen.EMDatagenTags;
import fr.iglee42.evolvedmekanism.datagen.recipe.builders.EMRecipeBuilders;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import fr.iglee42.evolvedmekanism.registries.EMChemicals;
import fr.iglee42.evolvedmekanism.registries.EMFluids;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.datagen.recipe.builder.ChemicalDissolutionRecipeBuilder;
import mekanism.api.datagen.recipe.builder.CombinerRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackChemicalToItemStackRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToChemicalRecipeBuilder;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import mekanism.api.datagen.recipe.builder.NucleosynthesizingRecipeBuilder;
import mekanism.api.datagen.recipe.builder.RotaryRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismChemicals;
import mekanism.common.registries.MekanismItems;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ResourceType;
import mekanism.common.tags.MekanismTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;

import java.util.concurrent.CompletableFuture;

public class EMRecipeProvider extends RecipeProvider {

    public EMRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        MoltenRecipes.addRecipes(output);
        addMachineRecipes(output);
        addTieredUpgradeRecipes(output);
        addProcessingRecipes(output);
        addToolRecipes(output);
        addMiscRecipes(output);
    }

    private void addMachineRecipes(RecipeOutput output) {
        EMCrafting.shaped(output, "alloyer", EMBlocks.ALLOYER, 1, new String[]{"ACA", "SXT", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_BASIC, 'C', EMDatagenTags.cItem("circuits/advanced"),
                        'S', MekanismBlocks.ENERGIZED_SMELTER, 'T', MekanismBlocks.COMBINER, 'X', MekanismBlocks.STEEL_CASING),
                has(MekanismBlocks.STEEL_CASING));
        EMCrafting.shaped(EMCrafting.mekData(output), "chemixer", EMBlocks.CHEMIXER, 1, new String[]{"ACA", "OPO", "ACA"},
                EMCrafting.keys('A', EMDatagenTags.item("evolvedmekanism", "alloys/singular"), 'C', EMDatagenTags.cItem("circuits/dense"),
                        'O', EMDatagenTags.cItem("ingots/osmium"), 'P', EMBlocks.ALLOYER),
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
                EMCrafting.keys('#', EMBlocks.APT_CASING, 'C', EMDatagenTags.cItem("circuits/quantum")),
                has(EMBlocks.APT_CASING));
        EMCrafting.shaped(output, "supercharging_element", EMBlocks.SUPERCHARGING_ELEMENT, 1, new String[]{"AIA", "IXI", "AIA"},
                EMCrafting.keys('A', EMDatagenTags.cItem("storage_blocks/alloys/singular"),
                        'I', EMDatagenTags.cItem("ingots/refined_redstone"), 'X', MekanismBlocks.SUPERHEATING_ELEMENT),
                has(MekanismBlocks.SUPERHEATING_ELEMENT));
        EMCrafting.shaped(output, "lunar_neutron_activator", EMBlocks.LUNAR_NEUTRON_ACTIVATOR, 1, new String[]{"A#A", "CXC", "III"},
                EMCrafting.keys('#', MekanismItems.HDPE_SHEET, 'A', MekanismTags.Items.ALLOYS_REINFORCED,
                        'C', EMDatagenTags.cItem("circuits/elite"), 'I', EMDatagenTags.cItem("gems/noctis_rozuli"),
                        'X', MekanismBlocks.STEEL_CASING),
                has(MekanismBlocks.STEEL_CASING));
        EMCrafting.shaped(output, "lunar_panel", EMGenItems.LUNAR_PANEL, 1, new String[]{"GGG", "RAR", "OOO"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_INFUSED, 'G', Tags.Items.GLASS_PANES,
                        'O', EMDatagenTags.cItem("gems/noctis_rozuli"), 'R', Tags.Items.DUSTS_REDSTONE),
                has(Tags.Items.DUSTS_REDSTONE), new ModLoadedCondition("mekanismgenerators"));

        addModule(output, "module_air_affinity_unit", EMItems.AIR_AFFINITY, EMDatagenTags.cItem("buckets/oxygen"), "A#A", "APA", "HHH");
        addModule(output, "module_aqua_affinity_unit", EMItems.AQUA_AFFINITY, MekanismItems.SCUBA_MASK, "A#A", "APA", "HHH");
        addModule(output, "module_capturing_unit", EMItems.CAPTURING, Items.NETHERITE_SWORD, "A#A", "APA", "HHH");
        EMCrafting.shaped(output, "module_luck_unit", EMItems.LUCK, 1, new String[]{"A#A", "#P#", "HHH"},
                EMCrafting.keys('#', EMDatagenTags.cItem("storage_blocks/lapis"), 'A', EMDatagenTags.cItem("alloys/elite"),
                        'H', EMDatagenTags.cItem("pellets/polonium"), 'P', MekanismItems.MODULE_BASE),
                has(MekanismItems.MODULE_BASE));

        EMCrafting.shapeless(output, EvolvedMekanism.rl("pink_dye"), Items.PINK_DYE, 1, EMItems.NOCTIS_ROZULI, has(EMItems.NOCTIS_ROZULI));
        EMCrafting.shapeless(output, EvolvedMekanism.rl("portable_hazmat_suit"), EMItems.PORTABLE_HAZMAT_SUIT, 1,
                new Object[]{MekanismItems.HAZMAT_MASK, MekanismItems.HAZMAT_GOWN, MekanismItems.HAZMAT_PANTS, MekanismItems.HAZMAT_BOOTS},
                has(MekanismItems.HAZMAT_MASK), new ModLoadedCondition("curios"));

        addQio(output, "qio_drive_boosted", EMItems.BOOSTED_QIO_DRIVE, EMCrafting.item("mekanism:qio_drive_supermassive"), EMDatagenTags.cItem("crystals/uranium"));
        addQio(output, "qio_drive_singularity", EMItems.SINGULARITY_QIO_DRIVE, EMItems.BOOSTED_QIO_DRIVE, EMDatagenTags.cItem("pellets/antimatter"));
        addQio(output, "qio_drive_hypra_solidified", EMItems.HYPRA_SOLIDIFIED_QIO_DRIVE, EMItems.SINGULARITY_QIO_DRIVE, EMDatagenTags.cItem("nuggets/better_gold"));
        addQio(output, "qio_drive_black_hole", EMItems.BLACK_HOLE_QIO_DRIVE, EMItems.HYPRA_SOLIDIFIED_QIO_DRIVE, EMDatagenTags.cItem("nuggets/plaslitherite"));
        addQio(output, "qio_drive_creative", EMItems.CREATIVE_QIO_DRIVE, EMItems.BLACK_HOLE_QIO_DRIVE, EMDatagenTags.cItem("alloys/creative"));

        EMCrafting.shaped(EMCrafting.mekData(output), "max_tier_installer", EMItems.MAX_TIER_INSTALLER, 1,
                new String[]{"123", "4 5", "678"},
                EMCrafting.keys('1', MekanismItems.BASIC_TIER_INSTALLER, '2', MekanismItems.ADVANCED_TIER_INSTALLER,
                        '3', MekanismItems.ELITE_TIER_INSTALLER, '4', MekanismItems.ULTIMATE_TIER_INSTALLER,
                        '5', EMItems.OVERCLOCKED_TIER_INSTALLER, '6', EMItems.QUANTUM_TIER_INSTALLER,
                        '7', EMItems.DENSE_TIER_INSTALLER, '8', EMItems.MULTIVERSAL_TIER_INSTALLER),
                has(EMItems.MULTIVERSAL_TIER_INSTALLER));
    }

    private void addModule(RecipeOutput output, String path, ItemLike result, Object center, String... pattern) {
        EMCrafting.shaped(output, path, result, 1, pattern,
                EMCrafting.keys('#', center, 'A', EMDatagenTags.cItem("alloys/elite"),
                        'H', EMDatagenTags.cItem("pellets/polonium"), 'P', MekanismItems.MODULE_BASE),
                has(MekanismItems.MODULE_BASE));
    }

    private void addQio(RecipeOutput output, String path, ItemLike result, ItemLike previous, Object center) {
        EMCrafting.shaped(EMCrafting.mekData(output), path, result, 1, new String[]{"IPI", "P#P", "IPI"},
                EMCrafting.keys('#', center, 'I', EMDatagenTags.cItem("ingots/lead"), 'P', previous),
                has(previous));
    }

    private void addTieredUpgradeRecipes(RecipeOutput output) {
        RecipeOutput mek = EMCrafting.mekData(output);
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

    private void addBin(RecipeOutput mek, TieredRecipes.TierData tier) {
        ItemLike result = "creative".equals(tier.name()) ? MekanismBlocks.CREATIVE_BIN : EMCrafting.item("evolvedmekanism:" + tier.name() + "_bin");
        ItemLike previous = tier.previousMekanism() ? MekanismBlocks.ULTIMATE_BIN : EMCrafting.item("evolvedmekanism:" + tier.previousPrefix() + "_bin");
        EMCrafting.shaped(mek, "bin/" + tier.name(), result, 1, new String[]{"_C_", "APA", "___"},
                EMCrafting.keys('A', tier.alloy(), 'C', tier.circuit(), 'P', previous, '_', MekanismTags.Items.STONE_CRAFTING_MATERIALS),
                has(previous));
    }

    private void addEnergyCube(RecipeOutput mek, TieredRecipes.TierData tier) {
        ItemLike result = "creative".equals(tier.name()) ? MekanismBlocks.CREATIVE_ENERGY_CUBE : EMCrafting.item("evolvedmekanism:" + tier.name() + "_energy_cube");
        ItemLike previous = tier.previousMekanism() ? MekanismBlocks.ULTIMATE_ENERGY_CUBE : EMCrafting.item("evolvedmekanism:" + tier.previousPrefix() + "_energy_cube");
        EMCrafting.shaped(mek, "energy_cube/" + tier.name(), result, 1, new String[]{"AEA", "IPI", "AEA"},
                EMCrafting.keys('A', tier.alloy(), 'E', MekanismItems.ENERGY_TABLET, 'I', Tags.Items.GEMS_DIAMOND, 'P', previous),
                has(previous));
    }

    private void addFluidTank(RecipeOutput mek, TieredRecipes.TierData tier) {
        ItemLike result = "creative".equals(tier.name()) ? MekanismBlocks.CREATIVE_FLUID_TANK : EMCrafting.item("evolvedmekanism:" + tier.name() + "_fluid_tank");
        ItemLike previous = tier.previousMekanism() ? MekanismBlocks.ULTIMATE_FLUID_TANK : EMCrafting.item("evolvedmekanism:" + tier.previousPrefix() + "_fluid_tank");
        EMCrafting.shaped(mek, "fluid_tank/" + tier.name(), result, 1, new String[]{"AIA", "IPI", "AIA"},
                EMCrafting.keys('A', tier.alloy(), 'I', Tags.Items.INGOTS_IRON, 'P', previous),
                has(previous));
    }

    private void addChemicalTank(RecipeOutput mek, TieredRecipes.TierData tier) {
        ItemLike result = "creative".equals(tier.name()) ? MekanismBlocks.CREATIVE_CHEMICAL_TANK : EMCrafting.item("evolvedmekanism:" + tier.name() + "_chemical_tank");
        ItemLike previous = tier.previousMekanism() ? MekanismBlocks.ULTIMATE_CHEMICAL_TANK : EMCrafting.item("evolvedmekanism:" + tier.previousPrefix() + "_chemical_tank");
        EMCrafting.shaped(mek, "chemical_tank/" + tier.name(), result, 1, new String[]{"AOA", "OPO", "AOA"},
                EMCrafting.keys('A', tier.alloy(), 'O', EMDatagenTags.cItem("ingots/osmium"), 'P', previous),
                has(previous));
    }

    private void addInduction(RecipeOutput mek, RecipeOutput output, TieredRecipes.TierData tier) {
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

    private void addPersonalStorageChain(RecipeOutput mek) {
        addPersonalStorage(mek, "advanced", EMDatagenTags.cItem("circuits/advanced"), MekanismBlocks.PERSONAL_BARREL, MekanismBlocks.PERSONAL_CHEST);
        addPersonalStorage(mek, "elite", EMDatagenTags.cItem("circuits/elite"), EMBlocks.ADVANCED_PERSONAL_BARREL, EMBlocks.ADVANCED_PERSONAL_CHEST);
        addPersonalStorage(mek, "ultimate", EMDatagenTags.cItem("circuits/ultimate"), EMBlocks.ELITE_PERSONAL_BARREL, EMBlocks.ELITE_PERSONAL_CHEST);
        addPersonalStorage(mek, "overclocked", EMDatagenTags.cItem("circuits/overclocked"), EMBlocks.ULTIMATE_PERSONAL_BARREL, EMBlocks.ULTIMATE_PERSONAL_CHEST);
        addPersonalStorage(mek, "quantum", EMDatagenTags.cItem("circuits/quantum"), EMBlocks.OVERCLOCKED_PERSONAL_BARREL, EMBlocks.OVERCLOCKED_PERSONAL_CHEST);
        addPersonalStorage(mek, "dense", EMDatagenTags.cItem("circuits/dense"), EMBlocks.QUANTUM_PERSONAL_BARREL, EMBlocks.QUANTUM_PERSONAL_CHEST);
        addPersonalStorage(mek, "multiversal", EMDatagenTags.cItem("circuits/multiversal"), EMBlocks.DENSE_PERSONAL_BARREL, EMBlocks.DENSE_PERSONAL_CHEST);
        addPersonalStorage(mek, "creative", EMDatagenTags.cItem("circuits/creative"), EMBlocks.MULTIVERSAL_PERSONAL_BARREL, EMBlocks.MULTIVERSAL_PERSONAL_CHEST);
    }

    private void addPersonalStorage(RecipeOutput mek, String tier, Object circuit, ItemLike previousBarrel, ItemLike previousChest) {
        EMCrafting.shaped(mek, "personal_barrel/" + tier, EMCrafting.item("evolvedmekanism:" + tier + "_personal_barrel"), 1,
                new String[]{"_C_", "APA", "___"},
                EMCrafting.keys('A', circuit, 'C', EMDatagenTags.cItem("glass_blocks/cheap"), 'P', previousBarrel, '_', EMDatagenTags.cItem("ingots/steel")),
                has(previousBarrel));
        EMCrafting.shaped(mek, "personal_chest/" + tier, EMCrafting.item("evolvedmekanism:" + tier + "_personal_chest"), 1,
                new String[]{"_C_", "APA", "___"},
                EMCrafting.keys('A', circuit, 'C', EMDatagenTags.cItem("glass_blocks/cheap"), 'P', previousChest, '_', EMDatagenTags.cItem("ingots/steel")),
                has(previousChest));
    }

    private void addCircuit(RecipeOutput output, TieredRecipes.TierData tier) {
        Object previousCircuit = switch (tier.name()) {
            case "overclocked" -> EMDatagenTags.cItem("circuits/ultimate");
            case "quantum" -> EMDatagenTags.cItem("circuits/overclocked");
            case "dense" -> EMDatagenTags.cItem("circuits/quantum");
            case "multiversal" -> EMDatagenTags.cItem("circuits/dense");
            default -> EMDatagenTags.cItem("circuits/multiversal");
        };
        EMCrafting.shaped(output, "control_circuit/" + tier.name(), EMCrafting.item("evolvedmekanism:" + tier.name() + "_control_circuit"), 1,
                new String[]{" A ", "ACA", " A "},
                EMCrafting.keys('A', tier.alloy(), 'C', previousCircuit), has(tier.alloy()));
    }

    private void addTierInstaller(RecipeOutput output, TieredRecipes.TierData tier) {
        EMCrafting.shaped(output, "tier_installer/" + tier.name(), EMCrafting.item("evolvedmekanism:" + tier.name() + "_tier_installer"), 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', tier.alloy(), 'C', tier.circuit(), 'I', tier.extra(), 'P', ItemTags.PLANKS),
                has(tier.circuit()));
    }

    private void addTransmitterSet(RecipeOutput output, TieredRecipes.TierData tier) {
        addTransmitter(output, tier, "universal_cable", MekanismBlocks.ULTIMATE_UNIVERSAL_CABLE);
        addTransmitter(output, tier, "mechanical_pipe", MekanismBlocks.ULTIMATE_MECHANICAL_PIPE);
        addTransmitter(output, tier, "pressurized_tube", MekanismBlocks.ULTIMATE_PRESSURIZED_TUBE);
        addTransmitter(output, tier, "logistical_transporter", MekanismBlocks.ULTIMATE_LOGISTICAL_TRANSPORTER);
        addTransmitter(output, tier, "thermodynamic_conductor", MekanismBlocks.ULTIMATE_THERMODYNAMIC_CONDUCTOR);
    }

    private void addTransmitter(RecipeOutput output, TieredRecipes.TierData tier, String type, ItemLike ultimate) {
        ItemLike previous = tier.previousMekanism() ? ultimate : EMCrafting.item("evolvedmekanism:" + tier.previousPrefix() + "_" + type);
        EMCrafting.shaped(output, "transmitter/" + type + "/" + tier.name(),
                EMCrafting.item("evolvedmekanism:" + tier.name() + "_" + type), 8, new String[]{"PPP", "PAP", "PPP"},
                EMCrafting.keys('A', tier.alloy(), 'P', previous), has(previous));
    }

    private void addSolarGenerator(RecipeOutput mek, TieredRecipes.TierData tier) {
        ItemLike prev = switch (tier.name()) {
            case "overclocked" -> EMGenBlocks.ULTIMATE_SOLAR_GENERATOR;
            case "quantum" -> EMGenBlocks.OVERCLOCKED_SOLAR_GENERATOR;
            case "dense" -> EMGenBlocks.QUANTUM_SOLAR_GENERATOR;
            case "multiversal" -> EMGenBlocks.DENSE_SOLAR_GENERATOR;
            case "creative" -> EMGenBlocks.MULTIVERSAL_SOLAR_GENERATOR;
            default -> throw new IllegalArgumentException(tier.name());
        };
        EMCrafting.shaped(mek, "solar_generators/" + tier.name() + "_generator",
                EMCrafting.item("evolvedmekanism:" + tier.name() + "_solar_generator"), 1, new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', tier.alloy(), 'C', tier.circuit(), 'I', tier.extra(), 'P', prev),
                has(prev), new ModLoadedCondition("mekanismgenerators"));
    }

    private void addLunarGenerator(RecipeOutput mek, TieredRecipes.TierData tier) {
        ItemLike prev = switch (tier.name()) {
            case "overclocked" -> EMGenBlocks.ULTIMATE_LUNAR_GENERATOR;
            case "quantum" -> EMGenBlocks.OVERCLOCKED_LUNAR_GENERATOR;
            case "dense" -> EMGenBlocks.QUANTUM_LUNAR_GENERATOR;
            case "multiversal" -> EMGenBlocks.DENSE_LUNAR_GENERATOR;
            case "creative" -> EMGenBlocks.MULTIVERSAL_LUNAR_GENERATOR;
            default -> throw new IllegalArgumentException(tier.name());
        };
        EMCrafting.shaped(mek, "lunar_generators/" + tier.name() + "_generator",
                EMCrafting.item("evolvedmekanism:" + tier.name() + "_lunar_generator"), 1, new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', tier.alloy(), 'C', tier.circuit(), 'I', tier.extra(), 'P', prev),
                has(prev), new ModLoadedCondition("mekanismgenerators"));
    }

    private void addProcessingRecipes(RecipeOutput output) {
        addIngotSet(output, "better_gold", EMItems.BETTER_GOLD_INGOT, EMItems.BETTER_GOLD_NUGGET, EMBlocks.BETTER_GOLD_BLOCK, EMItems.BETTER_GOLD_DUST, true);
        addIngotSet(output, "plaslitherite", EMItems.PLASLITHERITE_INGOT, EMItems.PLASLITHERITE_NUGGET, EMBlocks.PLASLITHERITE_BLOCK, EMItems.PLASLITHERITE_DUST, true);
        addIngotSet(output, "refined_redstone", EMItems.REFINED_REDSTONE_INGOT, EMItems.REFINED_REDSTONE_NUGGET, EMBlocks.REFINED_REDSTONE_BLOCK, null, false);
        ItemStackToItemStackRecipeBuilder.crushing(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("ingots/refined_redstone")), new ItemStack(Items.REDSTONE))
                .build(output, EvolvedMekanism.rl("processing/refined_redstone/ingot_to_dust"));
        ItemStackChemicalToItemStackRecipeBuilder.compressing(
                IngredientCreatorAccess.item().from(Tags.Items.DUSTS_REDSTONE),
                IngredientCreatorAccess.chemicalStack().fromHolder(MekanismChemicals.OSMIUM, 1),
                EMItems.REFINED_REDSTONE_INGOT.asStack(), true
        ).build(output, EvolvedMekanism.rl("processing/refined_redstone/ingot/from_dust"));

        EMCrafting.shapeless(output, EvolvedMekanism.rl("processing/noctis_rozuli/from_block"), EMItems.NOCTIS_ROZULI, 9,
                EMDatagenTags.cItem("storage_blocks/noctis_rozuli"), has(EMItems.NOCTIS_ROZULI));
        EMCrafting.shaped(output, "processing/noctis_rozuli/from_gems", EMBlocks.NOCTIS_ROZULI_BLOCK, 1, new String[]{"###", "#P#", "###"},
                EMCrafting.keys('#', EMDatagenTags.cItem("gems/noctis_rozuli"), 'P', EMItems.NOCTIS_ROZULI), has(EMItems.NOCTIS_ROZULI));
        ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/noctis_rozuli")), EMItems.NOCTIS_ROZULI.asStack())
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/from_dust"));
        ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("ores/noctis_rozuli")), EMItems.NOCTIS_ROZULI.asStack(12))
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/from_ore"));
        ItemStackToItemStackRecipeBuilder.crushing(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("gems/noctis_rozuli")), EMItems.NOCTIS_ROZULI_DUST.asStack())
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/to_dust"));
        CombinerRecipeBuilder.combining(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/noctis_rozuli"), 27),
                IngredientCreatorAccess.item().from(EMDatagenTags.cItem("cobblestones/normal")), EMCrafting.stack(EMBlocks.ORES.get(fr.iglee42.evolvedmekanism.registries.EMOreType.NOCTIS_ROZULI).stone()))
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/to_ore"));
        CombinerRecipeBuilder.combining(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/noctis_rozuli"), 27),
                IngredientCreatorAccess.item().from(EMDatagenTags.cItem("cobblestones/deepslate")), EMCrafting.stack(EMBlocks.ORES.get(fr.iglee42.evolvedmekanism.registries.EMOreType.NOCTIS_ROZULI).deepslate()))
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/to_deepslate_ore"));
        ItemStackToChemicalRecipeBuilder.oxidizing(IngredientCreatorAccess.item().from(Items.PITCHER_PLANT), new ChemicalStack(EMChemicals.NITROGEN, 250))
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/nitrogen"));
        ChemicalDissolutionRecipeBuilder.dissolution(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("gems/noctis_rozuli")),
                IngredientCreatorAccess.chemicalStack().fromHolder(EMChemicals.NITROGEN, 1), new ChemicalStack(EMChemicals.CRYONOCTIS, 1000), true)
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/cryonoctis"));
        ChemicalDissolutionRecipeBuilder.dissolution(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("storage_blocks/noctis_rozuli")),
                IngredientCreatorAccess.chemicalStack().fromHolder(EMChemicals.NITROGEN, 9), new ChemicalStack(EMChemicals.CRYONOCTIS, 9000), true)
                .build(output, EvolvedMekanism.rl("processing/noctis_rozuli/cryonoctis_from_block"));

        addDimOreProcessing(output, "osmium", MekanismItems.PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.OSMIUM), EMDatagenTags.cItem("raw_materials/osmium"));
        addDimOreProcessing(output, "tin", MekanismItems.PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.TIN), EMDatagenTags.cItem("raw_materials/tin"));
        addDimOreProcessing(output, "lead", MekanismItems.PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.LEAD), EMDatagenTags.cItem("raw_materials/lead"));
        addDimOreProcessing(output, "uranium", MekanismItems.PROCESSED_RESOURCES.get(ResourceType.INGOT, PrimaryResource.URANIUM), EMDatagenTags.cItem("raw_materials/uranium"));
        CombinerRecipeBuilder.combining(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/fluorite"), 14),
                IngredientCreatorAccess.item().from(Blocks.NETHERRACK), EMCrafting.stack(EMCrafting.item("evolvedmekanism:netherrack_fluorite_ore")))
                .build(output, EvolvedMekanism.rl("processing/fluorite/to_netherrack_ore"));
        CombinerRecipeBuilder.combining(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/fluorite"), 14),
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

        ItemStackToChemicalRecipeBuilder.chemicalConversion(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/better_gold")), new ChemicalStack(EMChemicals.BETTER_GOLD, 10))
                .build(output, EvolvedMekanism.rl("chemical_conversion/better_gold/from_dust"));
        ItemStackToChemicalRecipeBuilder.chemicalConversion(IngredientCreatorAccess.item().from(EMDatagenTags.item("evolvedmekanism", "enriched/better_gold")), new ChemicalStack(EMChemicals.BETTER_GOLD, 80))
                .build(output, EvolvedMekanism.rl("chemical_conversion/better_gold/from_enriched"));
        ItemStackToChemicalRecipeBuilder.chemicalConversion(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/plaslitherite")), new ChemicalStack(EMChemicals.PLASLITHERITE, 10))
                .build(output, EvolvedMekanism.rl("chemical_conversion/plaslitherite/from_dust"));
        ItemStackToChemicalRecipeBuilder.chemicalConversion(IngredientCreatorAccess.item().from(EMDatagenTags.item("evolvedmekanism", "enriched/plaslitherite")), new ChemicalStack(EMChemicals.PLASLITHERITE, 80))
                .build(output, EvolvedMekanism.rl("chemical_conversion/plaslitherite/from_enriched"));
        ItemStackToChemicalRecipeBuilder.chemicalConversion(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/uranium")), new ChemicalStack(EMChemicals.URANIUM, 10))
                .build(output, EvolvedMekanism.rl("chemical_conversion/uranium/from_dust"));
        ItemStackToChemicalRecipeBuilder.chemicalConversion(IngredientCreatorAccess.item().from(EMDatagenTags.item("evolvedmekanism", "enriched/uranium")), new ChemicalStack(EMChemicals.URANIUM, 80))
                .build(output, EvolvedMekanism.rl("chemical_conversion/uranium/from_enriched"));

        ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/better_gold")), EMItems.ENRICHED_BETTER_GOLD.asStack())
                .build(output, EvolvedMekanism.rl("enriching/enriched/better_gold"));
        ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/plaslitherite")), EMItems.ENRICHED_PLASLITHERITE.asStack())
                .build(output, EvolvedMekanism.rl("enriching/enriched/plaslitherite"));
        ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/uranium")), EMItems.ENRICHED_URANIUM.asStack())
                .build(output, EvolvedMekanism.rl("enriching/enriched/uranium"));
        ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(EMItems.NOCTIS_ROZULI), new ItemStack(Items.PINK_DYE, 2))
                .build(output, EvolvedMekanism.rl("enriching/dye/pink"));
        ItemStackToChemicalRecipeBuilder.pigmentExtracting(IngredientCreatorAccess.item().from(EMItems.NOCTIS_ROZULI),
                new ChemicalStack(MekanismChemicals.PIGMENT_COLOR_LOOKUP.get(mekanism.api.text.EnumColor.BRIGHT_PINK), 768))
                .build(output, EvolvedMekanism.rl("pigment_extracting/flower/pink"));
    }

    private void addIngotSet(RecipeOutput output, String name, ItemLike ingot, ItemLike nugget, ItemLike block, ItemLike dust, boolean dustRecipes) {
        EMCrafting.shapeless(output, EvolvedMekanism.rl("processing/" + name + "/ingot/from_block"), ingot, 9, EMDatagenTags.cItem("storage_blocks/" + name), has(ingot));
        EMCrafting.shapeless(output, EvolvedMekanism.rl("processing/" + name + "/nugget/from_ingot"), nugget, 9, EMDatagenTags.cItem("ingots/" + name), has(ingot));
        EMCrafting.shaped(output, "processing/" + name + "/ingot/from_nuggets", ingot, 1, new String[]{"###", "#P#", "###"},
                EMCrafting.keys('#', EMDatagenTags.cItem("nuggets/" + name), 'P', nugget), has(nugget));
        EMCrafting.shaped(output, "processing/" + name + "/storage_blocks/from_ingots", block, 1, new String[]{"###", "#P#", "###"},
                EMCrafting.keys('#', EMDatagenTags.cItem("ingots/" + name), 'P', ingot), has(ingot));
        if (dustRecipes && dust != null) {
            ItemStackToItemStackRecipeBuilder.crushing(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("ingots/" + name)), new ItemStack(dust.asItem()))
                    .build(output, EvolvedMekanism.rl("processing/" + name + "/dust/from_ingot"));
            EMCrafting.smelting(output, EvolvedMekanism.rl("processing/" + name + "/ingot/from_dust_smelting"),
                    Ingredient.of(EMDatagenTags.cItem("dusts/" + name)), ingot, 0.3F, 200, has(dust));
            EMCrafting.blasting(output, EvolvedMekanism.rl("processing/" + name + "/ingot/from_dust_blasting"),
                    Ingredient.of(EMDatagenTags.cItem("dusts/" + name)), ingot, 0.3F, 100, has(dust));
        }
    }

    private void addAlloyBlock(RecipeOutput output, String name, ItemLike alloy, Object alloyTag, ItemLike block) {
        EMCrafting.shapeless(output, EvolvedMekanism.rl("block_alloys/" + name + "/from_block"), alloy, 9, block, has(block));
        EMCrafting.shaped(output, "block_alloys/" + name + "/from_ingots", block, 1, new String[]{"###", "#P#", "###"},
                EMCrafting.keys('#', alloyTag, 'P', alloy), has(alloy));
    }

    private void addDimOreProcessing(RecipeOutput output, String ore, ItemLike ingot, Object rawTag) {
        Ingredient ores = Ingredient.of(
                EMCrafting.item("evolvedmekanism:netherrack_" + ore + "_ore"),
                EMCrafting.item("evolvedmekanism:end_stone_" + ore + "_ore"),
                EMCrafting.item("evolvedmekanism:depthrock_" + ore + "_ore"),
                EMCrafting.item("evolvedmekanism:shiverstone_" + ore + "_ore"),
                EMCrafting.item("evolvedmekanism:holystone_" + ore + "_ore")
        );
        EMCrafting.smelting(output, EvolvedMekanism.rl("processing/" + ore + "/ingot/from_ore_smelting"), ores, ingot, 0.6F, 200, has(ingot));
        EMCrafting.blasting(output, EvolvedMekanism.rl("processing/" + ore + "/ingot/from_ore_blasting"), ores, ingot, 0.6F, 100, has(ingot));
        CombinerRecipeBuilder.combining(IngredientCreatorAccess.item().from((net.minecraft.tags.TagKey<net.minecraft.world.item.Item>) rawTag, 8),
                IngredientCreatorAccess.item().from(Blocks.NETHERRACK),
                EMCrafting.stack(EMCrafting.item("evolvedmekanism:netherrack_" + ore + "_ore")))
                .build(output, EvolvedMekanism.rl("processing/" + ore + "/ore/netherrack_from_raw"));
        CombinerRecipeBuilder.combining(IngredientCreatorAccess.item().from((net.minecraft.tags.TagKey<net.minecraft.world.item.Item>) rawTag, 8),
                IngredientCreatorAccess.item().from(Blocks.END_STONE),
                EMCrafting.stack(EMCrafting.item("evolvedmekanism:end_stone_" + ore + "_ore")))
                .build(output, EvolvedMekanism.rl("processing/" + ore + "/ore/end_stone_from_raw"));
    }

    private void addToolRecipes(RecipeOutput output) {
        addToolSet(output, "better_gold", EMDatagenTags.cItem("ingots/better_gold"), EMItems.BETTER_GOLD_NUGGET,
                EMToolsItems.BETTER_GOLD_HELMET, EMToolsItems.BETTER_GOLD_CHESTPLATE, EMToolsItems.BETTER_GOLD_LEGGINGS, EMToolsItems.BETTER_GOLD_BOOTS,
                EMToolsItems.BETTER_GOLD_SWORD, EMToolsItems.BETTER_GOLD_PICKAXE, EMToolsItems.BETTER_GOLD_AXE, EMToolsItems.BETTER_GOLD_SHOVEL,
                EMToolsItems.BETTER_GOLD_HOE, EMToolsItems.BETTER_GOLD_PAXEL, EMToolsItems.BETTER_GOLD_SHIELD);
        addToolSet(output, "plaslitherite", EMDatagenTags.cItem("ingots/plaslitherite"), EMItems.PLASLITHERITE_NUGGET,
                EMToolsItems.PLASLITHERITE_HELMET, EMToolsItems.PLASLITHERITE_CHESTPLATE, EMToolsItems.PLASLITHERITE_LEGGINGS, EMToolsItems.PLASLITHERITE_BOOTS,
                EMToolsItems.PLASLITHERITE_SWORD, EMToolsItems.PLASLITHERITE_PICKAXE, EMToolsItems.PLASLITHERITE_AXE, EMToolsItems.PLASLITHERITE_SHOVEL,
                EMToolsItems.PLASLITHERITE_HOE, EMToolsItems.PLASLITHERITE_PAXEL, EMToolsItems.PLASLITHERITE_SHIELD);
        addToolSet(output, "refined_redstone", EMDatagenTags.cItem("ingots/refined_redstone"), EMItems.REFINED_REDSTONE_NUGGET,
                EMToolsItems.REFINED_REDSTONE_HELMET, EMToolsItems.REFINED_REDSTONE_CHESTPLATE, EMToolsItems.REFINED_REDSTONE_LEGGINGS, EMToolsItems.REFINED_REDSTONE_BOOTS,
                EMToolsItems.REFINED_REDSTONE_SWORD, EMToolsItems.REFINED_REDSTONE_PICKAXE, EMToolsItems.REFINED_REDSTONE_AXE, EMToolsItems.REFINED_REDSTONE_SHOVEL,
                EMToolsItems.REFINED_REDSTONE_HOE, EMToolsItems.REFINED_REDSTONE_PAXEL, EMToolsItems.REFINED_REDSTONE_SHIELD);
        addToolSet(output, "noctis_rozuli", EMDatagenTags.cItem("gems/noctis_rozuli"), null,
                EMToolsItems.NOCTIS_ROZULI_HELMET, EMToolsItems.NOCTIS_ROZULI_CHESTPLATE, EMToolsItems.NOCTIS_ROZULI_LEGGINGS, EMToolsItems.NOCTIS_ROZULI_BOOTS,
                EMToolsItems.NOCTIS_ROZULI_SWORD, EMToolsItems.NOCTIS_ROZULI_PICKAXE, EMToolsItems.NOCTIS_ROZULI_AXE, EMToolsItems.NOCTIS_ROZULI_SHOVEL,
                EMToolsItems.NOCTIS_ROZULI_HOE, EMToolsItems.NOCTIS_ROZULI_PAXEL, EMToolsItems.NOCTIS_ROZULI_SHIELD);
    }

    private void addToolSet(RecipeOutput output, String name, Object material, ItemLike nugget,
                            ItemLike helmet, ItemLike chest, ItemLike legs, ItemLike boots,
                            ItemLike sword, ItemLike pickaxe, ItemLike axe, ItemLike shovel, ItemLike hoe, ItemLike paxel, ItemLike shield) {
        ModLoadedCondition tools = new ModLoadedCondition("mekanismtools");
        RecipeOutput gated = output.withConditions(tools);
        Object rod = EMDatagenTags.cItem("rods/wooden");
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
                EMCrafting.keys('A', axe, 'P', pickaxe, 'S', shovel, 'R', rod), has(pickaxe));
        if (nugget != null) {
            Ingredient scrap = Ingredient.of(helmet, chest, legs, boots, sword, pickaxe, axe, shovel, hoe, paxel);
            EMCrafting.smelting(gated, EvolvedMekanism.rl("tools/" + name + "/nugget_from_smelting"), scrap, nugget, 0.1F, 200, has(nugget));
            EMCrafting.blasting(gated, EvolvedMekanism.rl("tools/" + name + "/nugget_from_blasting"), scrap, nugget, 0.1F, 100, has(nugget));
        }
    }

    private void addMiscRecipes(RecipeOutput output) {
        addMold(output, EMItems.MOLD_INGOT, EMDatagenTags.cItem("ingots"), "ingot");
        addMold(output, EMItems.MOLD_NUGGET, EMDatagenTags.cItem("nuggets"), "nugget");
        addMold(output, EMItems.MOLD_BLOCK, EMDatagenTags.cItem("storage_blocks"), "storage_block");
        addMold(output, EMItems.MOLD_DUST, EMDatagenTags.cItem("dusts"), "dust");
        addMold(output, EMItems.MOLD_PLATE, EMDatagenTags.cItem("plates"), "plate");
        addMold(output, EMItems.MOLD_GEAR, EMDatagenTags.cItem("gears"), "gear");
        addMold(output, EMItems.MOLD_ROD, EMDatagenTags.cItem("rods"), "rod");
        addMold(output, EMItems.MOLD_WIRE, EMDatagenTags.cItem("wires"), "wire");
        addMold(output, EMItems.MOLD_COIN, EMDatagenTags.cItem("coins"), "coin");
        addMold(output, EMItems.MOLD_GEM, EMDatagenTags.cItem("gems"), "gem");

        ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
                IngredientCreatorAccess.item().from(MekanismTags.Items.ALLOYS_ATOMIC),
                IngredientCreatorAccess.chemicalStack().from(fr.iglee42.evolvedmekanism.registries.EMTags.Gases.URANIUM, 20),
                EMItems.HYPERCHARGED_ALLOY.asStack(), false
        ).build(output, EvolvedMekanism.rl("metallurgic_infusing/alloy/hypercharged"));
        ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
                IngredientCreatorAccess.item().from(EMDatagenTags.item("evolvedmekanism", "alloys/subatomic")),
                IngredientCreatorAccess.chemicalStack().from(fr.iglee42.evolvedmekanism.registries.EMTags.Gases.BETTER_GOLD, 20),
                EMItems.SINGULAR_ALLOY.asStack(), false
        ).build(output, EvolvedMekanism.rl("metallurgic_infusing/alloy/singular"));
        ItemStackChemicalToItemStackRecipeBuilder.metallurgicInfusing(
                IngredientCreatorAccess.item().from(EMDatagenTags.item("evolvedmekanism", "alloys/singular")),
                IngredientCreatorAccess.chemicalStack().from(fr.iglee42.evolvedmekanism.registries.EMTags.Gases.PLASLITHERITE, 20),
                EMItems.EXOVERSAL_ALLOY.asStack(), false
        ).build(output, EvolvedMekanism.rl("metallurgic_infusing/alloy/exoversal"));

        NucleosynthesizingRecipeBuilder.nucleosynthesizing(
                IngredientCreatorAccess.item().from(EMDatagenTags.item("evolvedmekanism", "alloys/hypercharged")),
                IngredientCreatorAccess.chemicalStack().fromHolder(MekanismChemicals.ANTIMATTER, 25),
                EMItems.SUBATOMIC_ALLOY.asStack(), 1000, false
        ).build(output, EvolvedMekanism.rl("nucleosynthesizing/alloy_subatomic"));
        NucleosynthesizingRecipeBuilder.nucleosynthesizing(
                IngredientCreatorAccess.item().from(MekanismBlocks.SPS_CASING),
                IngredientCreatorAccess.chemicalStack().fromHolder(MekanismChemicals.ANTIMATTER, 50),
                EMCrafting.stack(EMBlocks.APT_CASING), 1000, false
        ).build(output, EvolvedMekanism.rl("nucleosynthesizing/apt_casing"));
        NucleosynthesizingRecipeBuilder.nucleosynthesizing(
                IngredientCreatorAccess.item().from(Items.PINK_WOOL),
                IngredientCreatorAccess.chemicalStack().fromHolder(MekanismChemicals.ANTIMATTER, 2),
                EMCrafting.stack(EMBlocks.NOCTIS_ROZULI_BLOCK), 500, false
        ).build(output, EvolvedMekanism.rl("nucleosynthesizing/noctis_block"));

        EMRecipeBuilders.apt(IngredientCreatorAccess.item().from(Tags.Items.INGOTS_GOLD),
                IngredientCreatorAccess.chemicalStack().fromHolder(MekanismChemicals.SPENT_NUCLEAR_WASTE, 100),
                EMItems.BETTER_GOLD_INGOT.asStack(), true)
                .build(output, EvolvedMekanism.rl("apt/ingot_better_gold"));
        EMRecipeBuilders.apt(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/gold")),
                IngredientCreatorAccess.chemicalStack().fromHolder(MekanismChemicals.SPENT_NUCLEAR_WASTE, 100),
                EMItems.BETTER_GOLD_DUST.asStack(), true)
                .build(output, EvolvedMekanism.rl("apt/dust_better_gold"));
        EMRecipeBuilders.apt(IngredientCreatorAccess.item().from(Tags.Items.STORAGE_BLOCKS_GOLD),
                IngredientCreatorAccess.chemicalStack().fromHolder(MekanismChemicals.SPENT_NUCLEAR_WASTE, 900),
                EMCrafting.stack(EMBlocks.BETTER_GOLD_BLOCK), true)
                .build(output, EvolvedMekanism.rl("apt/block_better_gold"));

        EMRecipeBuilders.chemixing(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("ingots/netherite")),
                IngredientCreatorAccess.item().from(MekanismItems.HDPE_PELLET, 3),
                IngredientCreatorAccess.chemicalStack().fromHolder(MekanismChemicals.LITHIUM, 100),
                EMItems.PLASLITHERITE_INGOT.asStack())
                .build(output, EvolvedMekanism.rl("chemixing/ingot_plaslitherite"));
        EMRecipeBuilders.chemixing(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("dusts/netherite")),
                IngredientCreatorAccess.item().from(MekanismItems.HDPE_PELLET, 3),
                IngredientCreatorAccess.chemicalStack().fromHolder(MekanismChemicals.LITHIUM, 100),
                EMItems.PLASLITHERITE_DUST.asStack())
                .build(output, EvolvedMekanism.rl("chemixing/dust_plaslitherite"));
        EMRecipeBuilders.chemixing(IngredientCreatorAccess.item().from(EMDatagenTags.cItem("storage_blocks/netherite")),
                IngredientCreatorAccess.item().from(MekanismItems.HDPE_SHEET, 3),
                IngredientCreatorAccess.chemicalStack().fromHolder(MekanismChemicals.LITHIUM, 900),
                EMCrafting.stack(EMBlocks.PLASLITHERITE_BLOCK))
                .build(output, EvolvedMekanism.rl("chemixing/block_plaslitherite"));

        RotaryRecipeBuilder.rotary(
                IngredientCreatorAccess.fluid().from(EMDatagenTags.cFluid("nitrogen"), 1),
                IngredientCreatorAccess.chemicalStack().fromHolder(EMChemicals.NITROGEN, 1),
                new ChemicalStack(EMChemicals.NITROGEN, 1),
                EMFluids.NITROGEN.asStack(1)
        ).build(output, EvolvedMekanism.rl("rotary/nitrogen"));
        RotaryRecipeBuilder.rotary(
                IngredientCreatorAccess.fluid().from(EMDatagenTags.cFluid("cryonoctis"), 1),
                IngredientCreatorAccess.chemicalStack().fromHolder(EMChemicals.CRYONOCTIS, 1),
                new ChemicalStack(EMChemicals.CRYONOCTIS, 1),
                EMFluids.CRYONOCTIS.asStack(1)
        ).build(output, EvolvedMekanism.rl("rotary/cryonoctis"));

        EMCrafting.shaped(output, "upgrade/radioactive", EMItems.RADIOACTIVE_UPGRADE, 1, new String[]{" G ", "A#A", " G "},
                EMCrafting.keys('#', EMDatagenTags.cItem("pellets/antimatter"), 'A', EMDatagenTags.item("evolvedmekanism", "alloys/singular"),
                        'G', EMDatagenTags.cItem("glass_blocks/cheap")),
                has(EMItems.SINGULAR_ALLOY));
        EMCrafting.shaped(output, "upgrade/solar", EMGenItems.SOLAR_UPGRADE, 1, new String[]{" G ", "A#A", " G "},
                EMCrafting.keys('#', Tags.Items.DUSTS_GLOWSTONE, 'A', EMDatagenTags.item("evolvedmekanism", "alloys/singular"),
                        'G', EMDatagenTags.cItem("glass_blocks/cheap")),
                has(EMItems.SINGULAR_ALLOY), new ModLoadedCondition("mekanismgenerators"));
        EMCrafting.shaped(output, "upgrade/lunar", EMGenItems.LUNAR_UPGRADE, 1, new String[]{" G ", "A#A", " G "},
                EMCrafting.keys('#', EMDatagenTags.cItem("dusts/noctis_rozuli"), 'A', EMDatagenTags.item("evolvedmekanism", "alloys/singular"),
                        'G', EMDatagenTags.cItem("glass_blocks/cheap")),
                has(EMItems.SINGULAR_ALLOY), new ModLoadedCondition("mekanismgenerators"));

        RecipeOutput gen = output.withConditions(new ModLoadedCondition("mekanismgenerators"));
        EMCrafting.shaped(EMCrafting.mekData(gen), "lunar_generators/generator", EMGenBlocks.LUNAR_GENERATOR, 1, new String[]{"###", "AIA", "OEO"},
                EMCrafting.keys('#', EMGenItems.LUNAR_PANEL, 'A', MekanismTags.Items.ALLOYS_INFUSED, 'E', MekanismItems.ENERGY_TABLET,
                        'I', Tags.Items.INGOTS_IRON, 'O', EMDatagenTags.cItem("ingots/osmium")),
                has(EMGenItems.LUNAR_PANEL), new ModLoadedCondition("mekanismgenerators"));
        EMCrafting.shaped(EMCrafting.mekData(gen), "lunar_generators/basic_advanced_generator", EMGenBlocks.BASIC_ADVANCED_LUNAR_GENERATOR, 1,
                new String[]{"PAP", "PAP", "CCC"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_INFUSED, 'C', EMDatagenTags.cItem("gems/noctis_rozuli"), 'P', EMGenBlocks.LUNAR_GENERATOR),
                has(EMGenBlocks.LUNAR_GENERATOR), new ModLoadedCondition("mekanismgenerators"));
        EMCrafting.shaped(EMCrafting.mekData(gen), "lunar_generators/advanced_generator", EMGenBlocks.ADVANCED_LUNAR_GENERATOR, 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_INFUSED, 'C', EMDatagenTags.cItem("circuits/advanced"),
                        'I', EMDatagenTags.cItem("ingots/osmium"), 'P', EMGenBlocks.BASIC_ADVANCED_LUNAR_GENERATOR),
                has(EMGenBlocks.BASIC_ADVANCED_LUNAR_GENERATOR), new ModLoadedCondition("mekanismgenerators"));
        EMCrafting.shaped(EMCrafting.mekData(gen), "solar_generators/advanced_generator", EMGenBlocks.ADVANCED_SOLAR_GENERATOR, 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_INFUSED, 'C', EMDatagenTags.cItem("circuits/advanced"),
                        'I', EMDatagenTags.cItem("ingots/osmium"), 'P', EMCrafting.item("mekanismgenerators:advanced_solar_generator")),
                has(EMDatagenTags.cItem("circuits/advanced")), new ModLoadedCondition("mekanismgenerators"));
        EMCrafting.shaped(EMCrafting.mekData(gen), "solar_generators/elite_generator", EMGenBlocks.ELITE_SOLAR_GENERATOR, 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_REINFORCED, 'C', EMDatagenTags.cItem("circuits/elite"),
                        'I', EMDatagenTags.cItem("ingots/gold"), 'P', EMGenBlocks.ADVANCED_SOLAR_GENERATOR),
                has(EMGenBlocks.ADVANCED_SOLAR_GENERATOR), new ModLoadedCondition("mekanismgenerators"));
        EMCrafting.shaped(EMCrafting.mekData(gen), "lunar_generators/elite_generator", EMGenBlocks.ELITE_LUNAR_GENERATOR, 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_REINFORCED, 'C', EMDatagenTags.cItem("circuits/elite"),
                        'I', EMDatagenTags.cItem("ingots/gold"), 'P', EMGenBlocks.ADVANCED_LUNAR_GENERATOR),
                has(EMGenBlocks.ADVANCED_LUNAR_GENERATOR), new ModLoadedCondition("mekanismgenerators"));
        EMCrafting.shaped(EMCrafting.mekData(gen), "solar_generators/ultimate_generator", EMGenBlocks.ULTIMATE_SOLAR_GENERATOR, 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_ATOMIC, 'C', EMDatagenTags.cItem("circuits/ultimate"),
                        'I', Tags.Items.GEMS_DIAMOND, 'P', EMGenBlocks.ELITE_SOLAR_GENERATOR),
                has(EMGenBlocks.ELITE_SOLAR_GENERATOR), new ModLoadedCondition("mekanismgenerators"));
        EMCrafting.shaped(EMCrafting.mekData(gen), "lunar_generators/ultimate_generator", EMGenBlocks.ULTIMATE_LUNAR_GENERATOR, 1,
                new String[]{"ACA", "IPI", "ACA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_ATOMIC, 'C', EMDatagenTags.cItem("circuits/ultimate"),
                        'I', Tags.Items.GEMS_DIAMOND, 'P', EMGenBlocks.ELITE_LUNAR_GENERATOR),
                has(EMGenBlocks.ELITE_LUNAR_GENERATOR), new ModLoadedCondition("mekanismgenerators"));
    }

    private void addMold(RecipeOutput output, ItemLike mold, net.minecraft.tags.TagKey<net.minecraft.world.item.Item> center, String name) {
        EMCrafting.shaped(output, "molds/" + name, mold, 1, new String[]{"ABA", "BCB", "ABA"},
                EMCrafting.keys('A', MekanismTags.Items.ALLOYS_INFUSED, 'B', EMDatagenTags.cItem("storage_blocks/steel"), 'C', center),
                has(MekanismTags.Items.ALLOYS_INFUSED), new NotCondition(new TagEmptyCondition(center.location())));
    }
}
