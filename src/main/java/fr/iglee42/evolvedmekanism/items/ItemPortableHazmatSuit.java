package fr.iglee42.evolvedmekanism.items;

import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.registries.EMDataComponents;
import mekanism.api.radiation.capability.IRadiationShielding;
import mekanism.api.text.EnumColor;
import mekanism.client.key.MekKeyHandler;
import mekanism.client.key.MekanismKeyHandler;
import mekanism.common.MekanismLang;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.item.interfaces.IModeItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemPortableHazmatSuit extends Item implements IModeItem {

    private static final IRadiationShielding ACTIVE_SHIELDING = () -> 1D;
    private static final IRadiationShielding INACTIVE_SHIELDING = () -> 0D;

    public ItemPortableHazmatSuit(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public static boolean isActive(ItemStack stack) {
        return EMDataComponents.isActive(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (MekKeyHandler.isKeyPressed(MekanismKeyHandler.descriptionKey)) {
            tooltip.add(EvolvedMekanismLang.PORTABLE_HAZMAT_SUIT_TOOLTIP.translate());
        } else {
            tooltip.add(MekanismLang.HOLD_FOR_DESCRIPTION.translateColored(EnumColor.GRAY, EnumColor.AQUA, MekanismKeyHandler.descriptionKey.getTranslatedKeyMessage()));
        }
        tooltip.add((isActive(stack) ? EvolvedMekanismLang.CURIO_ENABLED : EvolvedMekanismLang.CURIO_DISABLED)
                .translateColored(isActive(stack) ? EnumColor.BRIGHT_GREEN : EnumColor.DARK_RED));
        super.appendHoverText(stack, world, tooltip, flag);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilityProvider() {
            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if (cap == Capabilities.RADIATION_SHIELDING) {
                    return LazyOptional.of(() -> isActive(stack) ? ACTIVE_SHIELDING : INACTIVE_SHIELDING).cast();
                }
                return LazyOptional.empty();
            }
        };
    }

    @Override
    public void changeMode(@NotNull Player player, @NotNull ItemStack stack, int shift, DisplayChange displayChange) {
        boolean enabled = EMDataComponents.toggle(stack);
        displayChange.sendMessage(player, () -> enabled
                ? EvolvedMekanismLang.CURIO_ENABLED.translate()
                : EvolvedMekanismLang.CURIO_DISABLED.translate());
    }
}
