package fr.iglee42.evolvedmekanism.items;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.registries.EMDataComponents;
import mekanism.api.radiation.capability.IRadiationShielding;
import mekanism.api.text.EnumColor;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.ICapabilityAware;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemPortableHazmatSuit extends Item implements ICapabilityAware {

    private static final IRadiationShielding ACTIVE_SHIELDING = () -> 1D;
    private static final IRadiationShielding INACTIVE_SHIELDING = () -> 0D;

    public ItemPortableHazmatSuit(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public static boolean isActive(ItemStack stack) {
        return EMDataComponents.isActive(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(EvolvedMekanismLang.PORTABLE_HAZMAT_SUIT_TOOLTIP.translate());
        tooltip.add((isActive(stack) ? EvolvedMekanismLang.CURIO_ENABLED : EvolvedMekanismLang.CURIO_DISABLED)
                .translateColored(isActive(stack) ? EnumColor.BRIGHT_GREEN : EnumColor.DARK_RED));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public void attachCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.RADIATION_SHIELDING, (stack, ctx) -> isActive(stack) ? ACTIVE_SHIELDING : INACTIVE_SHIELDING, this);
    }
}
