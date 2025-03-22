package fr.iglee42.evolvedmekanism.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import mekanism.api.text.TextComponentUtil;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.item.ItemAlloy;
import mekanism.common.item.ItemTierInstaller;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
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
}
