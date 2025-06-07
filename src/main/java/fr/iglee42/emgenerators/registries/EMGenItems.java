package fr.iglee42.emgenerators.registries;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.registries.EMUpgrades;
import mekanism.api.Upgrade;
import mekanism.api.text.TextComponentUtil;
import mekanism.api.tier.AlloyTier;
import mekanism.api.tier.BaseTier;
import mekanism.common.item.*;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import mekanism.common.resource.IResource;
import mekanism.common.resource.ResourceType;
import mekanism.common.tier.QIODriveTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class EMGenItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(EvolvedMekanism.MODID);

    public static final ItemRegistryObject<ItemUpgrade> SOLAR_UPGRADE = registerUpgrade(EMUpgrades.SOLAR_UPGRADE);


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

    private static ItemRegistryObject<ItemQIODrive> registerQIODrive(QIODriveTier tier) {
        return ITEMS.register("qio_drive_" + tier.name().toLowerCase(Locale.ROOT), properties -> new ItemQIODrive(tier, properties));
    }

    private static ItemRegistryObject<ItemUpgrade> registerUpgrade(Upgrade type) {
        return ITEMS.register("upgrade_" + type.getRawName(), properties -> new ItemUpgrade(type, properties));
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
