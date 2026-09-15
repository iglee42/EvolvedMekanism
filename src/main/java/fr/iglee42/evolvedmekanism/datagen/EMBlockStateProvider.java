package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMFactoryType;
import fr.iglee42.evolvedmekanism.tiers.EMFactoryTier;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.resource.ore.OreType;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.CompositeModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EMBlockStateProvider extends BlockStateProvider {

    private static final ExistingFileHelper.ResourceType MODEL_TYPE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".json", "models");
    private static final ExistingFileHelper.ResourceType TEXTURE_TYPE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");
    private static final Map<String, Integer> LED_UV = Map.of(
            "overclocked", 0,
            "quantum", 1,
            "dense", 2,
            "multiversal", 3,
            "creative", 4
    );
    private static final List<String> CUSTOM_FACTORIES = List.of("alloying", "thermalizing", "solidifying");
    private static final List<String> MACHINES = List.of("alloyer", "chemixer", "thermalizer", "solidification_chamber");
    private static final List<String> SMALL_TRANSMITTERS = List.of("universal_cable", "pressurized_tube", "thermodynamic_conductor");
    private static final List<String> STORAGE = List.of("better_gold", "plaslitherite", "refined_redstone", "noctis_rozuli");
    private static final List<String> ALLOYS = List.of("infused", "reinforced", "atomic", "hypercharged", "subatomic", "singular", "exoversal", "creative");
    private static final List<String> ORE_PREFIXES = List.of("netherrack", "end_stone", "holystone", "depthrock", "shiverstone");

    private final Set<Block> generated = new HashSet<>();
    private final ExistingFileHelper existingFiles;

    public EMBlockStateProvider(PackOutput output, ExistingFileHelper existing) {
        super(output, EvolvedMekanism.MODID, existing);
        this.existingFiles = existing;
    }

    @Override
    protected void registerStatesAndModels() {
        LED_UV.forEach((tier, uv) -> {
            frontLed(tier, uv, false);
            frontLed(tier, uv, true);
        });

        List<FactoryTier> allTiers = allFactoryTiers();
        for (FactoryTier tier : allTiers) {
            if (tier == null) {
                continue;
            }
            boolean alloyingDone = false;
            for (FactoryType type : EnumUtils.FACTORY_TYPES) {
                boolean custom = type == EMFactoryType.ALLOYING || customFactory(type.getRegistryNameComponent());
                factory(tier, type, custom);
                alloyingDone |= type == EMFactoryType.ALLOYING;
            }
            if (!alloyingDone && EMFactoryType.ALLOYING != null) {
                factory(tier, EMFactoryType.ALLOYING, true);
            }
            for (String custom : List.of("thermalizing", "solidifying")) {
                if (!textureExists("block/factory/" + custom + "/" + custom + "_factory_front")) {
                    continue;
                }
                factoryModel(custom, tier.getBaseTier().getLowerName(), false, true, LED_UV.containsKey(tier.getBaseTier().getLowerName()));
                factoryModel(custom, tier.getBaseTier().getLowerName(), true, true, LED_UV.containsKey(tier.getBaseTier().getLowerName()));
            }
        }

        for (OreType ore : EnumUtils.ORE_TYPES) {
            String suffix = ore.getResource().getRegistrySuffix() + "_ore";
            for (String prefix : ORE_PREFIXES) {
                String model = "block/ores/" + prefix + "_" + suffix;
                cube(model, "block/ores/" + prefix + "/" + prefix + "_" + suffix);
                blockWithItem(prefix + "_" + suffix, model);
            }
        }

        for (String storage : STORAGE) {
            cube("block/storage/" + storage, "block/block_" + storage);
            blockWithItem("block_" + storage, "block/storage/" + storage);
        }
        cube("block/ores/noctis_rozuli_ore", "block/ores/stone/noctis_rozuli_ore");
        cube("block/ores/deepslate_noctis_rozuli_ore", "block/ores/deepslate/deepslate_noctis_rozuli_ore");
        blockWithItem("noctis_rozuli_ore", "block/ores/noctis_rozuli_ore");
        blockWithItem("deepslate_noctis_rozuli_ore", "block/ores/deepslate_noctis_rozuli_ore");
        naturalOre("noctis_rozuli_ore_natural", modLoc("block/ores/noctis_rozuli_ore"), mcLoc("block/lapis_ore"));
        naturalOre("deepslate_noctis_rozuli_ore_natural", modLoc("block/ores/deepslate_noctis_rozuli_ore"), mcLoc("block/deepslate_lapis_ore"));
        for (String alloy : ALLOYS) {
            cube("block/storage/alloy_" + alloy, "block/block_alloy_" + alloy);
            blockWithItem("block_alloy_" + alloy, "block/storage/alloy_" + alloy);
        }

        cube("block/apt_casing", "block/apt_casing");
        cube("block/supercharging_element", "block/supercharging_element");
        ledCube("apt_port", "block/apt_port", "block/apt_port_led");
        ledCube("apt_port_output", "block/apt_port_output", "block/apt_port_output_led");
        ledCube("supercharging_element_active", "block/supercharging_element_on", "block/supercharging_element_on_led");
        for (String machine : MACHINES) {
            machineModel(machine);
        }

        for (Block block : BuiltInRegistries.BLOCK) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
            if (id == null || !EvolvedMekanism.MODID.equals(id.getNamespace())) {
                continue;
            }
            if (block instanceof LiquidBlock) {
                String path = id.getPath();
                String modelPath = path.startsWith("molten_") ? "block/moltens/" + path.substring("molten_".length()) : "block/" + path;
                models().getBuilder(modelPath).texture("particle", modLoc("block/molten_still"));
                if (generated.add(block)) {
                    simpleBlock(block, models().getExistingFile(modLoc(modelPath)));
                }
                continue;
            }
            registerRemaining(block, id.getPath());
        }
    }

    private void registerRemaining(Block block, String path) {
        if (generated.contains(block) || block.defaultBlockState().isAir()) {
            return;
        }
        String tier = tierPrefix(path);
        if (path.endsWith("_personal_barrel")) {
            barrel(block, tier);
        } else if (path.endsWith("_personal_chest")) {
            facingHorizontal(block, ResourceLocation.fromNamespaceAndPath("mekanism", "block/personal_chest"), false);
        } else if (path.endsWith("_energy_cube")) {
            energyCube(block, tier);
        } else if (path.endsWith("_fluid_tank")) {
            activeOnly(block, ResourceLocation.fromNamespaceAndPath("mekanism", "block/fluid_tank"), ResourceLocation.fromNamespaceAndPath("mekanism", "block/fluid_tank_active"), false);
        } else if (path.endsWith("_chemical_tank")) {
            chemicalTank(block, tier);
        } else if (path.endsWith("_bin")) {
            bin(block, tier);
        } else if (path.endsWith("_induction_cell")) {
            inductionCell(block, path, tier);
        } else if (path.endsWith("_induction_provider")) {
            inductionProvider(block, path, tier);
        } else if (path.endsWith("_solar_generator")) {
            facingHorizontal(block, modLoc("block/solar_generators/" + path), true);
        } else if (path.contains("lunar_generator")) {
            ResourceLocation model = modLoc("block/lunar_generators/" + path);
            if (block.getStateDefinition().getProperty("facing") != null) {
                facingHorizontal(block, model, true);
            } else {
                simpleExisting(block, model, true);
            }
        } else if (path.endsWith("_mechanical_pipe")) {
            transmitter(block, "block/transmitter/large/mechanical_pipe/" + tier, "mekanism:block/transmitter/large/large", path);
        } else if (path.endsWith("_logistical_transporter")) {
            transmitter(block, "block/transmitter/large/logistical_transporter/" + tier, "mekanism:block/transmitter/large/logistical_transporter/transporter", path);
        } else if (SMALL_TRANSMITTERS.stream().anyMatch(path::endsWith)) {
            String kind = SMALL_TRANSMITTERS.stream().filter(path::endsWith).findFirst().orElseThrow();
            transmitter(block, "block/transmitter/small/" + kind + "/" + tier, "mekanism:block/transmitter/small/small", path);
        } else if (path.equals("apt_casing")) {
            simpleExisting(block, modLoc("block/apt_casing"), true);
        } else if (path.equals("apt_port")) {
            activeOnly(block, modLoc("block/apt_port"), modLoc("block/apt_port_output"), true);
        } else if (path.equals("supercharging_element")) {
            activeOnly(block, modLoc("block/supercharging_element"), modLoc("block/supercharging_element_active"), true);
        } else if (MACHINES.contains(path)) {
            facingActive(block, "block/" + path, "block/" + path + "_active");
            simpleBlockItem(block, models().getExistingFile(modLoc("block/" + path)));
            generated.add(block);
        } else if (block.getStateDefinition().getProperty("facing") != null && block.getStateDefinition().getProperty("active") != null) {
            String idle = "block/" + path;
            String active = "block/" + path + "_active";
            facingActive(block, idle, active);
            if (modelExists(idle)) {
                simpleBlockItem(block, new ModelFile.UncheckedModelFile(modLoc(idle)));
            }
            generated.add(block);
        } else if (block.getStateDefinition().getProperty("facing") != null) {
            facingHorizontal(block, modLoc("block/" + path), modelExists("block/" + path));
        } else if (modelExists("block/" + path)) {
            simpleExisting(block, modLoc("block/" + path), true);
        }
    }

    private void machineModel(String name) {
        BlockModelBuilder idle = models().withExistingParent(name, ResourceLocation.fromNamespaceAndPath("mekanism", "block/machine"));
        if (name.equals("solidification_chamber")) {
            idle.texture("sides", modLoc("block/" + name + "/right"))
                    .texture("front", modLoc("block/" + name + "/front"))
                    .texture("west", modLoc("block/" + name + "/right"))
                    .texture("east", modLoc("block/" + name + "/left"))
                    .texture("south", modLoc("block/" + name + "/back"))
                    .texture("up", modLoc("block/" + name + "/top"))
                    .texture("down", modLoc("block/" + name + "/bottom"));
            models().withExistingParent(name + "_active", modLoc("block/" + name))
                    .texture("front", modLoc("block/" + name + "/front_active"))
                    .texture("west", modLoc("block/" + name + "/right_active"))
                    .texture("east", modLoc("block/" + name + "/left_active"))
                    .texture("south", modLoc("block/" + name + "/back_active"))
                    .texture("up", modLoc("block/" + name + "/top_active"));
            return;
        }
        idle.texture("sides", modLoc("block/" + name + "/side"))
                .texture("front", modLoc("block/" + name + "/front"))
                .texture("west", modLoc("block/" + name + "/side"))
                .texture("east", modLoc("block/" + name + "/side"))
                .texture("south", modLoc("block/" + name + "/back"))
                .texture("up", modLoc("block/" + name + "/top"))
                .texture("down", modLoc("block/" + name + "/bottom"));
        models().withExistingParent(name + "_active", modLoc("block/" + name))
                .texture("front", modLoc("block/" + name + "/front_active"))
                .texture("down", modLoc("block/" + name + "/bottom_active"));
    }

    private void ledCube(String modelPath, String all, String led) {
        models().withExistingParent(modelPath, ResourceLocation.fromNamespaceAndPath("mekanism", "block/template/cube_all_led"))
                .texture("all", modLoc(all))
                .texture("led", modLoc(led));
    }

    private void barrel(Block block, String tier) {
        models().withExistingParent("block/personal_barrel/" + tier, mcLoc("block/cube_bottom_top"))
                .texture("bottom", ResourceLocation.fromNamespaceAndPath("mekanism", "block/personal_barrel/bottom"))
                .texture("side", modLoc("block/personal_barrel/" + tier + "/side"))
                .texture("top", modLoc("block/personal_barrel/" + tier + "/top"));
        models().withExistingParent("block/personal_barrel_open/" + tier, modLoc("block/personal_barrel/" + tier))
                .texture("top", modLoc("block/personal_barrel/" + tier + "/top_open"));
        DirectionProperty facing = BlockStateProperties.FACING;
        BooleanProperty open = BlockStateProperties.OPEN;
        Property<?>[] ignored = ignored(block, facing, open);
        getVariantBuilder(block).forAllStatesExcept(state -> {
            Direction dir = state.getValue(facing);
            boolean isOpen = state.getValue(open);
            ModelFile model = models().getExistingFile(modLoc("block/personal_barrel" + (isOpen ? "_open/" : "/") + tier));
            return switch (dir) {
                case DOWN -> ConfiguredModel.builder().modelFile(model).rotationX(180).build();
                case UP -> ConfiguredModel.builder().modelFile(model).build();
                case NORTH -> ConfiguredModel.builder().modelFile(model).rotationX(90).build();
                case SOUTH -> ConfiguredModel.builder().modelFile(model).rotationX(90).rotationY(180).build();
                case WEST -> ConfiguredModel.builder().modelFile(model).rotationX(90).rotationY(270).build();
                case EAST -> ConfiguredModel.builder().modelFile(model).rotationX(90).rotationY(90).build();
            };
        }, ignored);
        simpleBlockItem(block, models().getExistingFile(modLoc("block/personal_barrel/" + tier)));
        generated.add(block);
    }

    private void energyCube(Block block, String tier) {
        models().withExistingParent("block/energy_cube/" + tier, ResourceLocation.fromNamespaceAndPath("mekanism", "block/energy_cube/base"))
                .texture("corner", modLoc("block/models/energy_cube_" + tier + "_corner"));
        DirectionProperty facing = (DirectionProperty) block.getStateDefinition().getProperty("facing");
        ModelFile model = models().getExistingFile(modLoc("block/energy_cube/" + tier));
        getVariantBuilder(block).forAllStatesExcept(state -> {
            Direction dir = state.getValue(facing);
            return switch (dir) {
                case UP -> ConfiguredModel.builder().modelFile(model).rotationX(-90).build();
                case DOWN -> ConfiguredModel.builder().modelFile(model).rotationX(90).build();
                case SOUTH -> ConfiguredModel.builder().modelFile(model).rotationY(180).build();
                case EAST -> ConfiguredModel.builder().modelFile(model).rotationY(90).build();
                case WEST -> ConfiguredModel.builder().modelFile(model).rotationY(-90).build();
                default -> ConfiguredModel.builder().modelFile(model).build();
            };
        }, ignored(block, facing));
        generated.add(block);
    }

    private void chemicalTank(Block block, String tier) {
        models().withExistingParent("block/chemical_tank/" + tier, ResourceLocation.fromNamespaceAndPath("mekanism", "block/chemical_tank/base"))
                .texture("valve", modLoc("block/models/chemical_tank_" + tier + "_valve"));
        facingHorizontal(block, modLoc("block/chemical_tank/" + tier), true);
    }

    private void bin(Block block, String tier) {
        models().withExistingParent("block/bin/" + tier, ResourceLocation.fromNamespaceAndPath("mekanism", "block/bin/basic"))
                .texture("led", modLoc("block/bin/" + tier + "_led"))
                .texture("side", modLoc("block/bin/" + tier + "_side"))
                .texture("back", modLoc("block/bin/" + tier + "_back"));
        models().withExistingParent("block/bin/active/" + tier, ResourceLocation.fromNamespaceAndPath("mekanism", "block/bin/active/basic"))
                .texture("led", modLoc("block/bin/" + tier + "_led"))
                .texture("side", modLoc("block/bin/" + tier + "_side"))
                .texture("back", modLoc("block/bin/" + tier + "_back"));
        facingActive(block, "block/bin/" + tier, "block/bin/active/" + tier);
        simpleBlockItem(block, models().getExistingFile(modLoc("block/bin/" + tier)));
        generated.add(block);
    }

    private void inductionCell(Block block, String path, String tier) {
        models().withExistingParent("block/induction/cell/" + tier, ResourceLocation.fromNamespaceAndPath("mekanism", "block/induction/cell/basic"))
                .renderType("cutout")
                .texture("particle", modLoc("block/" + path))
                .texture("all", modLoc("block/" + path));
        simpleExisting(block, modLoc("block/induction/cell/" + tier), true);
    }

    private void inductionProvider(Block block, String path, String tier) {
        models().withExistingParent("block/induction/provider/" + tier, ResourceLocation.fromNamespaceAndPath("mekanism", "block/induction/provider/base"))
                .renderType("cutout")
                .texture("particle", modLoc("block/" + path))
                .texture("all", modLoc("block/" + path))
                .texture("glow", ResourceLocation.fromNamespaceAndPath("mekanism", "block/induction_provider_glow"))
                .texture("led", modLoc("block/" + path + "_led"));
        simpleExisting(block, modLoc("block/induction/provider/" + tier), true);
    }

    private void transmitter(Block block, String modelPath, String parent, String path) {
        String center = "block/models/multipart/" + path;
        String side = "block/models/multipart/" + path + "_vertical";
        String opaqueCenter = textureExists("block/models/multipart/opaque/" + path)
                ? "block/models/multipart/opaque/" + path
                : center;
        String opaqueSide = textureExists("block/models/multipart/opaque/" + path + "_vertical")
                ? "block/models/multipart/opaque/" + path + "_vertical"
                : side;
        models().withExistingParent(modelPath, ResourceLocation.parse(parent))
                .texture("side", modLoc(side))
                .texture("center_down", modLoc(center))
                .texture("side_opaque", modLoc(opaqueSide))
                .texture("center_opaque", modLoc(opaqueCenter));
        simpleExisting(block, modLoc(modelPath), true);
    }

    private void cube(String modelPath, String texturePath) {
        models().cubeAll(modelPath, modLoc(texturePath));
    }

    private void blockWithItem(String blockPath, String modelPath) {
        Block block = block(blockPath);
        if (block.defaultBlockState().isAir() || !generated.add(block)) {
            return;
        }
        ModelFile model = models().getExistingFile(modLoc(modelPath));
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void naturalOre(String blockPath, ResourceLocation uncovered, ResourceLocation covered) {
        Block block = block(blockPath);
        if (block.defaultBlockState().isAir() || !generated.add(block)) {
            return;
        }
        BooleanProperty uncoveredProp = (BooleanProperty) block.getStateDefinition().getProperty("uncovered");
        getVariantBuilder(block)
                .partialState().with(uncoveredProp, true).modelForState().modelFile(new ModelFile.UncheckedModelFile(uncovered)).addModel()
                .partialState().with(uncoveredProp, false).modelForState().modelFile(new ModelFile.UncheckedModelFile(covered)).addModel();
    }

    private void factory(FactoryTier tier, FactoryType type, boolean custom) {
        if (type == null) {
            return;
        }
        String tierName = tier.getBaseTier().getLowerName();
        String typeName = type.getRegistryNameComponent();
        String blockPath = tierName + "_" + typeName + "_factory";
        Block block = BuiltInRegistries.BLOCK.get(EvolvedMekanism.rl(blockPath));
        factoryModel(typeName, tierName, false, custom, LED_UV.containsKey(tierName));
        factoryModel(typeName, tierName, true, custom, LED_UV.containsKey(tierName));
        if (block.defaultBlockState().isAir() || !generated.add(block)) {
            return;
        }
        facingActive(block, "block/factory/" + typeName + "/" + tierName, "block/factory/" + typeName + "/active/" + tierName);
        simpleBlockItem(block, models().getExistingFile(modLoc("block/factory/" + typeName + "/" + tierName)));
    }

    private void factoryModel(String type, String tier, boolean active, boolean custom, boolean emLed) {
        String path = "block/factory/" + type + (active ? "/active/" : "/") + tier;
        ResourceLocation baseParent = custom
                ? modLoc("block/factory/" + type + "/base")
                : ResourceLocation.fromNamespaceAndPath("mekanism", "block/factory/" + type + "/base");
        ResourceLocation particle = custom
                ? modLoc("block/factory/" + type + "/" + type + "_factory_front")
                : ResourceLocation.fromNamespaceAndPath("mekanism", "block/factory/" + type + "/" + type + "_factory_front");
        ResourceLocation led = emLed
                ? modLoc("block/factory/front_led/" + (active ? "active/" : "") + tier)
                : ResourceLocation.fromNamespaceAndPath("mekanism", "block/factory/front_led/" + (active ? "active/" : "") + tier);

        BlockModelBuilder model = models().getBuilder(path)
                .parent(new ModelFile.UncheckedModelFile("block/block"))
                .texture("particle", particle);
        CompositeModelBuilder<BlockModelBuilder> loader = model.customLoader(CompositeModelBuilder::begin);
        BlockModelBuilder baseChild = models().getBuilder(path + "_base")
                .parent(new ModelFile.UncheckedModelFile(baseParent));
        if (active) {
            ResourceLocation activeFront = custom
                    ? modLoc("block/factory/" + type + "/" + type + "_factory_front_active")
                    : ResourceLocation.fromNamespaceAndPath("mekanism", "block/factory/" + type + "/" + type + "_factory_front_active");
            baseChild.texture("front", activeFront);
        }
        loader.child("base", baseChild);
        loader.child("front_led", models().getBuilder(path + "_led").parent(new ModelFile.UncheckedModelFile(led)));
    }

    private void facingActive(Block block, String idle, String active) {
        DirectionProperty facing = (DirectionProperty) block.getStateDefinition().getProperty("facing");
        BooleanProperty activeProp = (BooleanProperty) block.getStateDefinition().getProperty("active");
        getVariantBuilder(block).forAllStatesExcept(state -> {
            Direction dir = state.getValue(facing);
            boolean isActive = state.getValue(activeProp);
            return ConfiguredModel.builder()
                    .modelFile(new ModelFile.UncheckedModelFile(modLoc(isActive ? active : idle)))
                    .rotationY(horizontalY(dir))
                    .build();
        }, ignored(block, facing, activeProp));
    }

    private void facingHorizontal(Block block, ResourceLocation model, boolean item) {
        DirectionProperty facing = (DirectionProperty) block.getStateDefinition().getProperty("facing");
        ModelFile file = new ModelFile.UncheckedModelFile(model);
        getVariantBuilder(block).forAllStatesExcept(state -> ConfiguredModel.builder()
                .modelFile(file)
                .rotationY(horizontalY(state.getValue(facing)))
                .build(), ignored(block, facing));
        if (item) {
            simpleBlockItem(block, file);
        }
        generated.add(block);
    }

    private void activeOnly(Block block, ResourceLocation idle, ResourceLocation active, boolean item) {
        BooleanProperty activeProp = (BooleanProperty) block.getStateDefinition().getProperty("active");
        getVariantBuilder(block).forAllStatesExcept(state -> ConfiguredModel.builder()
                .modelFile(new ModelFile.UncheckedModelFile(state.getValue(activeProp) ? active : idle))
                .build(), ignored(block, activeProp));
        if (item) {
            simpleBlockItem(block, new ModelFile.UncheckedModelFile(idle));
        }
        generated.add(block);
    }

    private void simpleExisting(Block block, ResourceLocation model, boolean item) {
        ModelFile file = new ModelFile.UncheckedModelFile(model);
        simpleBlock(block, file);
        if (item) {
            simpleBlockItem(block, file);
        }
        generated.add(block);
    }

    private void frontLed(String tier, int uv, boolean active) {
        String path = "block/factory/front_led/" + (active ? "active/" : "") + tier;
        var element = models().getBuilder(path)
                .texture("led", modLoc("block/factory/led"))
                .element()
                .from(4.98F, 14.99F, 0.01F)
                .to(11.02F, 15.99F, 1.01F)
                .face(Direction.NORTH).uvs(0, uv, 6, uv + 1).texture("#led").cullface(Direction.NORTH).end()
                .face(Direction.UP).uvs(0, uv, 6, uv + 1).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#led").cullface(Direction.UP).end();
        if (active) {
            element.emissivity(15, 15);
        }
        element.end();
    }

    private boolean textureExists(String path) {
        return existingFiles.exists(modLoc(path), TEXTURE_TYPE);
    }

    private boolean modelExists(String path) {
        return existingFiles.exists(modLoc(path), MODEL_TYPE);
    }

    private static int horizontalY(Direction dir) {
        return switch (dir) {
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            default -> 0;
        };
    }

    private static Property<?>[] ignored(Block block, Property<?>... keep) {
        Set<Property<?>> kept = Set.of(keep);
        return block.getStateDefinition().getProperties().stream()
                .filter(property -> !kept.contains(property))
                .toArray(Property<?>[]::new);
    }

    private static String tierPrefix(String path) {
        int index = path.indexOf('_');
        return index < 0 ? path : path.substring(0, index);
    }

    private static boolean customFactory(String type) {
        return CUSTOM_FACTORIES.contains(type);
    }

    private static List<FactoryTier> allFactoryTiers() {
        List<FactoryTier> tiers = new ArrayList<>(List.of(EnumUtils.FACTORY_TIERS));
        tiers.add(EMFactoryTier.OVERCLOCKED);
        tiers.add(EMFactoryTier.QUANTUM);
        tiers.add(EMFactoryTier.DENSE);
        tiers.add(EMFactoryTier.MULTIVERSAL);
        tiers.add(EMFactoryTier.CREATIVE);
        return tiers;
    }

    private Block block(String path) {
        return BuiltInRegistries.BLOCK.get(EvolvedMekanism.rl(path));
    }
}
