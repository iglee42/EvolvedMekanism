package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.tiers.EMAlloyTier;
import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.text.TextComponentUtil;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.item.ItemAlloy;
import mekanism.common.item.ItemRefinedGlowstoneIngot;
import mekanism.common.item.ItemTierInstaller;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.resource.IResource;
import mekanism.common.resource.MiscResource;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ResourceType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EMItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(EvolvedMekanism.MODID);


    public static final ItemRegistryObject<ItemTierInstaller> OVERCLOCKED_TIER_INSTALLER = registerInstaller(BaseTier.ULTIMATE, EMBaseTier.OVERCLOCKED);
    public static final ItemRegistryObject<ItemTierInstaller> QUANTUM_TIER_INSTALLER = registerInstaller(EMBaseTier.OVERCLOCKED, EMBaseTier.QUANTUM);
    public static final ItemRegistryObject<ItemTierInstaller> DENSE_TIER_INSTALLER = registerInstaller(EMBaseTier.QUANTUM,EMBaseTier.DENSE);
    public static final ItemRegistryObject<ItemTierInstaller> MULTIVERSAL_TIER_INSTALLER = registerInstaller(EMBaseTier.DENSE,EMBaseTier.MULTIVERSAL);

    public static final ItemRegistryObject<Item> OVERCLOCKED_CONTROL_CIRCUIT = registerCircuit(EMBaseTier.OVERCLOCKED);
    public static final ItemRegistryObject<Item> QUANTUM_CONTROL_CIRCUIT = registerCircuit(EMBaseTier.QUANTUM);
    public static final ItemRegistryObject<Item> DENSE_CONTROL_CIRCUIT = registerCircuit(EMBaseTier.DENSE);
    public static final ItemRegistryObject<Item> MULTIVERSAL_CONTROL_CIRCUIT = registerCircuit(EMBaseTier.MULTIVERSAL);

    public static final ItemRegistryObject<ItemAlloy> HYPERCHARGED_ALLOY = registerAlloy(EMAlloyTier.HYPERCHARGED);
    public static final ItemRegistryObject<ItemAlloy> SUBATOMIC_ALLOY = registerAlloy(EMAlloyTier.SUBATOMIC);
    public static final ItemRegistryObject<ItemAlloy> SINGULAR_ALLOY = registerAlloy(EMAlloyTier.SINGULAR);
    public static final ItemRegistryObject<ItemAlloy> EXOVERSAL_ALLOY = registerAlloy(EMAlloyTier.EXOVERSAL);

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
}
