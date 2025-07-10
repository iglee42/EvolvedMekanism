package fr.iglee42.evolvedmekanism;

import mekanism.common.block.attribute.AttributeParticleFX;
import mekanism.common.block.attribute.AttributeSideConfig;
import mekanism.common.content.blocktype.*;
import mekanism.common.lib.math.Pos3D;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismContainerTypes;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.factory.TileEntityFactory;
import net.minecraft.core.particles.ParticleTypes;

import java.util.function.Supplier;

public class EMFactoryBuilder <FACTORY extends Factory<TILE>, TILE extends TileEntityFactory<?>, T extends Machine.MachineBuilder<FACTORY, TILE, T>>
        extends BlockTypeTile.BlockTileBuilder<FACTORY, TILE, T>{

        protected EMFactoryBuilder(FACTORY holder) {
            super(holder);
        }

        @SuppressWarnings("unchecked")
        public static <TILE extends TileEntityFactory<?>> EMFactoryBuilder<Factory<TILE>, TILE, ?> createFactory(Supplier<?> tileEntityRegistrar, FactoryType type,
                                                                                                                       FactoryTier tier) {
            // this is dirty but unfortunately necessary for things to play right
            EMFactoryBuilder<Factory<TILE>, TILE, ?> builder = new EMFactoryBuilder<>(new Factory<>((Supplier<TileEntityTypeRegistryObject<TILE>>) tileEntityRegistrar,
                    () -> MekanismContainerTypes.FACTORY, type.getBaseMachine(), tier));
            //Note, we can't just return the builder here as then it gets all confused about object types, so we just
            // assign the value here, and then return the builder itself as it is the same object
            builder.withComputerSupport(tier, type.getRegistryNameComponentCapitalized() + "Factory");
            builder.withCustomShape(BlockShapes.getShape(tier, type));
            if (!type.getTranslationKey().contains("evolvedmekanism"))
                builder.with(switch (type) {
                case SMELTING, ENRICHING, CRUSHING, COMBINING, SAWING -> AttributeSideConfig.ELECTRIC_MACHINE;
                case COMPRESSING, INJECTING, PURIFYING, INFUSING -> AttributeSideConfig.ADVANCED_ELECTRIC_MACHINE;
                 });
            else {
                builder.with(AttributeSideConfig.ELECTRIC_MACHINE);
            }
            builder.replace(new AttributeParticleFX().addDense(ParticleTypes.SMOKE, 5, rand -> new Pos3D(
                    rand.nextFloat() * 0.7F - 0.3F,
                    rand.nextFloat() * 0.1F + 0.7F,
                    rand.nextFloat() * 0.7F - 0.3F
            )));
            return builder;
        }
}
