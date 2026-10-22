package fr.iglee42.evolvedmekanism.curios;

import fr.iglee42.evolvedmekanism.items.ItemPortableHazmatSuit;
import fr.iglee42.evolvedmekanism.mixins.curios.MekanismItemContainerAccessor;
import mekanism.common.inventory.container.item.MekanismItemContainer;
import mekanism.common.inventory.container.item.PortableQIODashboardContainer;
import mekanism.common.registries.MekanismItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Optional;
import java.util.function.Predicate;

public final class CuriosHelper {

    private CuriosHelper() {
    }

    @Nullable
    public static ICuriosItemHandler inventory(LivingEntity entity) {
        return CuriosApi.getCuriosHelper().getCuriosHandler(entity).resolve().orElse(null);
    }

    @Nullable
    public static IItemHandler itemHandler(LivingEntity entity) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(entity).resolve().orElse(null);
    }

    public static ItemStack findInSlot(LivingEntity entity, String identifier) {
        ICuriosItemHandler inventory = inventory(entity);
        if (inventory == null) {
            return ItemStack.EMPTY;
        }
        Optional<ICurioStacksHandler> handler = inventory.getStacksHandler(identifier);
        if (handler.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ICurioStacksHandler stacksHandler = handler.get();
        if (!isHandlerActive(inventory, identifier, stacksHandler)) {
            return ItemStack.EMPTY;
        }
        return stacksHandler.getStacks().getStackInSlot(0);
    }

    public static ItemStack findFirst(LivingEntity entity, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosHelper().findFirstCurio(entity, predicate)
                .map(SlotResult::stack)
                .orElseGet(() -> findFirstFromHandler(entity, predicate));
    }

    private static ItemStack findFirstFromHandler(LivingEntity entity, Predicate<ItemStack> predicate) {
        IItemHandler handler = itemHandler(entity);
        if (handler == null) {
            return ItemStack.EMPTY;
        }
        for (int slot = 0, slots = handler.getSlots(); slot < slots; slot++) {
            ItemStack stack = handler.getStackInSlot(slot);
            if (!stack.isEmpty() && predicate.test(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack findFirst(LivingEntity entity, Item item) {
        return findFirst(entity, stack -> stack.is(item));
    }

    public static ItemStack resolveFromHandOrCurio(Player player, InteractionHand hand, Predicate<ItemStack> matcher) {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof PortableQIODashboardContainer qio && matcher.test(qio.getStack())) {
            return qio.getStack();
        }
        if (menu instanceof MekanismItemContainer container) {
            ItemStack stack = ((MekanismItemContainerAccessor) container).evolvedmekanism$getStack();
            if (!stack.isEmpty() && matcher.test(stack)) {
                return stack;
            }
        }
        ItemStack held = player.getItemInHand(hand);
        if (!held.isEmpty() && matcher.test(held)) {
            return held;
        }
        return findFirst(player, matcher);
    }

    public static boolean isAvailableOutsideHand(Player player, ItemStack stack) {
        return !stack.isEmpty() && !findFirst(player, stack.getItem()).isEmpty();
    }

    public static boolean isSlotRendered(LivingEntity entity, String identifier) {
        ICuriosItemHandler inventory = inventory(entity);
        if (inventory == null) {
            return false;
        }
        return inventory.getStacksHandler(identifier).map(ICurioStacksHandler::isVisible).orElse(false);
    }

    public static boolean shouldReplaceArmorRender(LivingEntity entity) {
        ItemStack hazmat = findInSlot(entity, CuriosSlots.PORTABLE_HAZMAT_SUIT);
        return !hazmat.isEmpty() && ItemPortableHazmatSuit.isActive(hazmat) && isSlotRendered(entity, CuriosSlots.PORTABLE_HAZMAT_SUIT);
    }

    public static ItemStack getHazmatArmorPiece(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> MekanismItems.HAZMAT_MASK.getItemStack();
            case CHEST -> MekanismItems.HAZMAT_GOWN.getItemStack();
            case LEGS -> MekanismItems.HAZMAT_PANTS.getItemStack();
            case FEET -> MekanismItems.HAZMAT_BOOTS.getItemStack();
            default -> ItemStack.EMPTY;
        };
    }

    public static ItemStack findElytra(LivingEntity entity) {
        return findInSlot(entity, CuriosSlots.HDPE_ELYTRA);
    }

    public static void startFallFlying(LivingEntity entity) {
        try {
            var method = LivingEntity.class.getDeclaredMethod("startFallFlying");
            method.setAccessible(true);
            method.invoke(entity);
            return;
        } catch (ReflectiveOperationException ignored) {
        }
        try {
            var method = net.minecraft.world.entity.Entity.class.getDeclaredMethod("setSharedFlag", int.class, boolean.class);
            method.setAccessible(true);
            method.invoke(entity, 7, true);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static boolean isHandlerActive(ICuriosItemHandler inventory, String identifier, ICurioStacksHandler handler) {
        try {
            var method = inventory.getClass().getMethod("isSlotActive", String.class, int.class);
            Object result = method.invoke(inventory, identifier, 0);
            return result instanceof Boolean bool && bool;
        } catch (Throwable ignored) {
            return true;
        }
    }
}
