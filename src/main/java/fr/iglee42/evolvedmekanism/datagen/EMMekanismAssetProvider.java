package fr.iglee42.evolvedmekanism.datagen;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.common.Mekanism;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;

public class EMMekanismAssetProvider extends BlockStateProvider {

    public EMMekanismAssetProvider(PackOutput output, ExistingFileHelper existing) {
        super(output, Mekanism.MODID, existing);
    }

    @Override
    protected void registerStatesAndModels() {
        for (String tier : List.of("basic", "advanced", "elite", "ultimate")) {
            Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(Mekanism.MODID, tier + "_alloying_factory"));
            if (block.defaultBlockState().isAir()) {
                continue;
            }
            ResourceLocation idle = EvolvedMekanism.rl("block/factory/alloying/" + tier);
            ResourceLocation active = EvolvedMekanism.rl("block/factory/alloying/active/" + tier);
            DirectionProperty facing = (DirectionProperty) block.getStateDefinition().getProperty("facing");
            BooleanProperty activeProp = (BooleanProperty) block.getStateDefinition().getProperty("active");
            Property<?>[] ignored = block.getStateDefinition().getProperties().stream()
                    .filter(property -> property != facing && property != activeProp)
                    .toArray(Property<?>[]::new);
            getVariantBuilder(block).forAllStatesExcept(state -> {
                Direction dir = state.getValue(facing);
                int y = switch (dir) {
                    case EAST -> 90;
                    case SOUTH -> 180;
                    case WEST -> 270;
                    default -> 0;
                };
                return ConfiguredModel.builder()
                        .modelFile(new ModelFile.UncheckedModelFile(state.getValue(activeProp) ? active : idle))
                        .rotationY(y)
                        .build();
            }, ignored);
            simpleBlockItem(block, new ModelFile.UncheckedModelFile(idle));
        }
    }
}
