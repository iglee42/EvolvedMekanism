package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.tiers.EMAlloyTier;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import fr.iglee42.evolvedmekanism.tiers.storage.EMQIODriveTier;
import mekanism.api.Upgrade;
import mekanism.api.text.TextComponentUtil;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.item.*;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.resource.IResource;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ResourceType;
import mekanism.common.tier.QIODriveTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class EMItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(EvolvedMekanism.MODID);

    //MODULES
    public static final ItemRegistryObject<ItemModule> AIR_AFFINITY = ITEMS.registerModule(EMModules.AIR_AFFINITY);
    public static final ItemRegistryObject<ItemModule> AQUA_AFFINITY = ITEMS.registerModule(EMModules.AQUA_AFFINITY);
    public static final ItemRegistryObject<ItemModule> CAPTURING = ITEMS.registerModule(EMModules.CAPTURING);
    public static final ItemRegistryObject<ItemModule> LUCK = ITEMS.registerModule(EMModules.LUCK);

    public static final ItemRegistryObject<ItemUpgrade> RADIOACTIVE_UPGRADE = registerUpgrade(EMUpgrades.RADIOACTIVE_UPGRADE);

    public static final ItemRegistryObject<Item> MOLD_BLOCK = registerMold("storage_block");
    public static final ItemRegistryObject<Item> MOLD_INGOT = registerMold("ingot");
    public static final ItemRegistryObject<Item> MOLD_GEM = registerMold("gem");
    public static final ItemRegistryObject<Item> MOLD_NUGGET = registerMold("nugget");
    public static final ItemRegistryObject<Item> MOLD_DUST = registerMold("dust");
    public static final ItemRegistryObject<Item> MOLD_PLATE = registerMold("plate");
    public static final ItemRegistryObject<Item> MOLD_GEAR = registerMold("gear");
    public static final ItemRegistryObject<Item> MOLD_ROD = registerMold("rod");
    public static final ItemRegistryObject<Item> MOLD_WIRE = registerMold("wire");
    public static final ItemRegistryObject<Item> MOLD_COIN = registerMold("coin");


    public static final ItemRegistryObject<ItemTierInstaller> OVERCLOCKED_TIER_INSTALLER = registerInstaller(BaseTier.ULTIMATE, EMBaseTier.OVERCLOCKED);
    public static final ItemRegistryObject<ItemTierInstaller> QUANTUM_TIER_INSTALLER = registerInstaller(EMBaseTier.OVERCLOCKED, EMBaseTier.QUANTUM);
    public static final ItemRegistryObject<ItemTierInstaller> DENSE_TIER_INSTALLER = registerInstaller(EMBaseTier.QUANTUM,EMBaseTier.DENSE);
    public static final ItemRegistryObject<ItemTierInstaller> MULTIVERSAL_TIER_INSTALLER = registerInstaller(EMBaseTier.DENSE,EMBaseTier.MULTIVERSAL);
    public static final ItemRegistryObject<ItemTierInstaller> CREATIVE_TIER_INSTALLER = registerInstaller(EMBaseTier.MULTIVERSAL,BaseTier.CREATIVE);

    public static final ItemRegistryObject<Item> OVERCLOCKED_CONTROL_CIRCUIT = registerCircuit(EMBaseTier.OVERCLOCKED);
    public static final ItemRegistryObject<Item> QUANTUM_CONTROL_CIRCUIT = registerCircuit(EMBaseTier.QUANTUM);
    public static final ItemRegistryObject<Item> DENSE_CONTROL_CIRCUIT = registerCircuit(EMBaseTier.DENSE);
    public static final ItemRegistryObject<Item> MULTIVERSAL_CONTROL_CIRCUIT = registerCircuit(EMBaseTier.MULTIVERSAL);
    public static final ItemRegistryObject<Item> CREATIVE_CONTROL_CIRCUIT = registerCircuit(BaseTier.CREATIVE);

    public static final ItemRegistryObject<ItemQIODrive> BOOSTED_QIO_DRIVE = registerQIODrive(EMQIODriveTier.BOOSTED);
    public static final ItemRegistryObject<ItemQIODrive> SINGULARITY_QIO_DRIVE = registerQIODrive(EMQIODriveTier.SINGULARITY);
    public static final ItemRegistryObject<ItemQIODrive> HYPRA_SOLIDIFIED_QIO_DRIVE = registerQIODrive(EMQIODriveTier.HYPRA_SOLIDIFIED);
    public static final ItemRegistryObject<ItemQIODrive> BLACK_HOLE_QIO_DRIVE = registerQIODrive(EMQIODriveTier.BLACK_HOLE);
    public static final ItemRegistryObject<ItemQIODrive> CREATIVE_QIO_DRIVE = registerQIODrive(EMQIODriveTier.CREATIVE);

    public static final ItemRegistryObject<ItemAlloy> HYPERCHARGED_ALLOY = registerAlloy(EMAlloyTier.HYPERCHARGED);
    public static final ItemRegistryObject<ItemAlloy> SUBATOMIC_ALLOY = registerAlloy(EMAlloyTier.SUBATOMIC);
    public static final ItemRegistryObject<ItemAlloy> SINGULAR_ALLOY = registerAlloy(EMAlloyTier.SINGULAR);
    public static final ItemRegistryObject<ItemAlloy> EXOVERSAL_ALLOY = registerAlloy(EMAlloyTier.EXOVERSAL);
    public static final ItemRegistryObject<ItemAlloy> CREATIVE_ALLOY = registerAlloy(EMAlloyTier.CREATIVE);

    public static final ItemRegistryObject<Item> ENRICHED_URANIUM = registerResource(ResourceType.ENRICHED, PrimaryResource.URANIUM);
    public static final ItemRegistryObject<Item> ENRICHED_BETTER_GOLD = registerResource(ResourceType.ENRICHED,EMResources.BETTER_GOLD);
    public static final ItemRegistryObject<Item> ENRICHED_PLASLITHERITE = registerUnburnableResource(ResourceType.ENRICHED,EMResources.PLASLITHERITE);

    public static final ItemRegistryObject<Item> BETTER_GOLD_DUST = registerResource(ResourceType.DUST, EMResources.BETTER_GOLD);
    public static final ItemRegistryObject<Item> PLASLITHERITE_DUST = registerUnburnableResource(ResourceType.DUST, EMResources.PLASLITHERITE);

    public static final ItemRegistryObject<Item> BETTER_GOLD_INGOT = registerResource(ResourceType.INGOT, EMResources.BETTER_GOLD);
    public static final ItemRegistryObject<Item> PLASLITHERITE_INGOT = registerUnburnableResource(ResourceType.INGOT, EMResources.PLASLITHERITE);

    public static final ItemRegistryObject<Item> BETTER_GOLD_NUGGET = registerResource(ResourceType.NUGGET, EMResources.BETTER_GOLD);
    public static final ItemRegistryObject<Item> PLASLITHERITE_NUGGET = registerUnburnableResource(ResourceType.NUGGET, EMResources.PLASLITHERITE);


    private static ItemRegistryObject<Item> registerCircuit(BaseTier tier) {
        return ITEMS.register(tier.getLowerName() + "_control_circuit", properties -> new Item(properties) {
            @NotNull
            @Override
            public Component getName(@NotNull ItemStack stack) {
                return TextComponentUtil.build(tier.getColor(), super.getName(stack));
            }
        });
    }

    private static ItemRegistryObject<Item> registerMold(String name) {
        return ITEMS.register("mold_"+name, ()-> new Item(new Item.Properties().stacksTo(1)));
    }

    private static ItemRegistryObject<ItemTierInstaller> registerInstaller(@Nullable BaseTier fromTier, @NotNull BaseTier toTier) {
        return ITEMS.register(toTier.getLowerName() + "_tier_installer", properties -> new ItemTierInstaller(fromTier, toTier, properties));
    }

    private static ItemRegistryObject<ItemAlloy> registerAlloy(AlloyTier tier) {
        return ITEMS.register("alloy_" + tier.getName(), properties -> new ItemAlloy(tier, properties) {
            @NotNull
            @Override
            public Component getName(@NotNull ItemStack stack) {
                return TextComponentUtil.build(tier.getBaseTier().getColor(), super.getName(stack));
            }
        });
    }

    private static ItemRegistryObject<Item> registerResource(ResourceType type, IResource resource) {
        return ITEMS.register(type.getRegistryPrefix() + "_" + resource.getRegistrySuffix());
    }
    private static ItemRegistryObject<Item> registerUnburnableResource(ResourceType type, IResource resource) {
        return ITEMS.registerUnburnable(type.getRegistryPrefix() + "_" + resource.getRegistrySuffix());
    }

    private static ItemRegistryObject<ItemQIODrive> registerQIODrive(QIODriveTier tier) {
        return ITEMS.register("qio_drive_" + tier.name().toLowerCase(Locale.ROOT), properties -> new ItemQIODrive(tier, properties));
    }

    private static ItemRegistryObject<ItemUpgrade> registerUpgrade(Upgrade type) {
        return ITEMS.register("upgrade_" + type.getRawName(), properties -> new ItemUpgrade(type, properties));
    }
}
