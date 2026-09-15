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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;

public class EMBlockStateProvider extends BlockStateProvider {

    private static final Map<String, Integer> LED_UV = Map.of(
            "overclocked", 0,
            "quantum", 1,
            "dense", 2,
            "multiversal", 3,
            "creative", 4
    );

    public EMBlockStateProvider(PackOutput output, ExistingFileHelper existing) {
        super(output, EvolvedMekanism.MODID, existing);
    }

    @Override
    protected void registerStatesAndModels() {
        LED_UV.forEach((tier, uv) -> {
            frontLed(tier, uv, false);
            frontLed(tier, uv, true);
        });

        List<FactoryTier> emTiers = List.of(EMFactoryTier.OVERCLOCKED, EMFactoryTier.QUANTUM, EMFactoryTier.DENSE,
                EMFactoryTier.MULTIVERSAL, EMFactoryTier.CREATIVE);
        for (FactoryTier tier : emTiers) {
            boolean alloyingDone = false;
            for (FactoryType type : EnumUtils.FACTORY_TYPES) {
                boolean alloying = type == EMFactoryType.ALLOYING;
                factory(tier, type, alloying);
                alloyingDone |= alloying;
            }
            if (!alloyingDone && EMFactoryType.ALLOYING != null) {
                factory(tier, EMFactoryType.ALLOYING, true);
            }
        }
        for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
            alloyingModel(tier.getBaseTier().getLowerName(), false);
            alloyingModel(tier.getBaseTier().getLowerName(), true);
        }

        for (OreType ore : EnumUtils.ORE_TYPES) {
            String suffix = ore.getResource().getRegistrySuffix() + "_ore";
            for (String prefix : List.of("netherrack", "end_stone", "holystone", "depthrock", "shiverstone")) {
                String model = "block/ores/" + prefix + "_" + suffix;
                cube(model, model);
                blockWithItem(prefix + "_" + suffix, model);
            }
        }

        cube("block/storage/better_gold", "block/block_better_gold");
        cube("block/storage/plaslitherite", "block/block_plaslitherite");
        cube("block/storage/refined_redstone", "block/block_refined_redstone");
        blockWithItem("block_better_gold", "block/storage/better_gold");
        blockWithItem("block_plaslitherite", "block/storage/plaslitherite");
        blockWithItem("block_refined_redstone", "block/storage/refined_redstone");
        for (String alloy : List.of("infused", "reinforced", "atomic", "hypercharged", "subatomic", "singular", "exoversal", "creative")) {
            cube("block/storage/alloy_" + alloy, "block/block_alloy_" + alloy);
            blockWithItem("block_alloy_" + alloy, "block/storage/alloy_" + alloy);
        }

        for (Block block : ForgeRegistries.BLOCKS) {
            ResourceLocation id = ForgeRegistries.BLOCKS.getKey(block);
            if (id == null || !EvolvedMekanism.MODID.equals(id.getNamespace()) || !(block instanceof LiquidBlock)) {
                continue;
            }
            String path = id.getPath();
            String modelPath = path.startsWith("molten_") ? "block/moltens/" + path.substring("molten_".length()) : "block/" + path;
            models().getBuilder(modelPath).texture("particle", modLoc("block/molten_still"));
            simpleBlock(block, models().getExistingFile(modLoc(modelPath)));
        }
    }

    private void cube(String modelPath, String texturePath) {
        models().cubeAll(modelPath, modLoc(texturePath));
    }

    private void blockWithItem(String blockPath, String modelPath) {
        Block block = block(blockPath);
        if (block.defaultBlockState().isAir()) {
            return;
        }
        ModelFile model = models().getExistingFile(modLoc(modelPath));
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void factory(FactoryTier tier, FactoryType type, boolean alloying) {
        if (type == null) {
            return;
        }
        String tierName = tier.getBaseTier().getLowerName();
        String typeName = type.getRegistryNameComponent();
        String blockPath = tierName + "_" + typeName + "_factory";
        Block block = BuiltInRegistries.BLOCK.get(EvolvedMekanism.rl(blockPath));
        factoryModel(typeName, tierName, false, alloying, LED_UV.containsKey(tierName));
        factoryModel(typeName, tierName, true, alloying, LED_UV.containsKey(tierName));
        if (block.defaultBlockState().isAir()) {
            return;
        }
        facingActive(block, "block/factory/" + typeName + "/" + tierName, "block/factory/" + typeName + "/active/" + tierName);
        simpleBlockItem(block, models().getExistingFile(modLoc("block/factory/" + typeName + "/" + tierName)));
    }

    private void alloyingModel(String tier, boolean active) {
        factoryModel("alloying", tier, active, true, LED_UV.containsKey(tier));
    }

    private void factoryModel(String type, String tier, boolean active, boolean alloying, boolean emLed) {
        String path = "block/factory/" + type + (active ? "/active/" : "/") + tier;
        ResourceLocation baseParent = alloying
                ? modLoc("block/factory/alloying/base")
                : new ResourceLocation("mekanism", "block/factory/" + type + "/base");
        ResourceLocation particle = alloying
                ? modLoc("block/factory/alloying/alloying_factory_front")
                : new ResourceLocation("mekanism", "block/factory/" + type + "/" + type + "_factory_front");
        ResourceLocation led = emLed
                ? modLoc("block/factory/front_led/" + (active ? "active/" : "") + tier)
                : new ResourceLocation("mekanism", "block/factory/front_led/" + (active ? "active/" : "") + tier);

        BlockModelBuilder model = models().getBuilder(path)
                .parent(new ModelFile.UncheckedModelFile("block/block"))
                .texture("particle", particle);
        CompositeModelBuilder<BlockModelBuilder> loader = model.customLoader(CompositeModelBuilder::begin);
        BlockModelBuilder baseChild = models().getBuilder(path + "_base")
                .parent(new ModelFile.UncheckedModelFile(baseParent));
        if (active) {
            ResourceLocation activeFront = alloying
                    ? modLoc("block/factory/alloying/alloying_factory_front_active")
                    : new ResourceLocation("mekanism", "block/factory/" + type + "/" + type + "_factory_front_active");
            baseChild.texture("front", activeFront);
        }
        loader.child("base", baseChild);
        loader.child("front_led", models().getBuilder(path + "_led").parent(new ModelFile.UncheckedModelFile(led)));
    }

    private void facingActive(Block block, String idle, String active) {
        DirectionProperty facing = (DirectionProperty) block.getStateDefinition().getProperty("facing");
        BooleanProperty activeProp = (BooleanProperty) block.getStateDefinition().getProperty("active");
        Property<?>[] ignored = block.getStateDefinition().getProperties().stream()
                .filter(property -> property != facing && property != activeProp)
                .toArray(Property<?>[]::new);
        getVariantBuilder(block).forAllStatesExcept(state -> {
            Direction dir = state.getValue(facing);
            boolean isActive = state.getValue(activeProp);
            int y = switch (dir) {
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
                default -> 0;
            };
            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc(isActive ? active : idle)))
                    .rotationY(y)
                    .build();
        }, ignored);
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

    private Block block(String path) {
        return BuiltInRegistries.BLOCK.get(EvolvedMekanism.rl(path));
    }
}
