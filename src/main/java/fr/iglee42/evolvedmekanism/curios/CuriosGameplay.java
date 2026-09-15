package fr.iglee42.evolvedmekanism.curios;

import fr.iglee42.evolvedmekanism.items.ItemPortableHazmatSuit;
import fr.iglee42.evolvedmekanism.network.PacketCurioAction;
import fr.iglee42.evolvedmekanism.registries.EMDataComponents;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.MekanismAPITags;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.energy.EnergyCompatUtils;
import mekanism.common.item.gear.ItemCanteen;
import mekanism.common.item.gear.ItemScubaMask;
import mekanism.common.item.gear.ItemScubaTank;
import mekanism.common.item.interfaces.IGuiItem;
import mekanism.common.item.interfaces.IModeItem;
import mekanism.common.item.interfaces.IModeItem.DisplayChange;
import mekanism.common.registries.MekanismFluids;
import mekanism.common.registries.MekanismItems;
import mekanism.common.util.ChemicalUtil;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.StorageUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CuriosGameplay {

    private static final Map<UUID, Integer> CANTEEN_DRINK_TICKS = new ConcurrentHashMap<>();

    private CuriosGameplay() {
    }

    public static void tick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        Level level = entity.level();
        stack.inventoryTick(level, entity, slotContext.index(), false);
        if (!level.isClientSide() && entity instanceof Player player && stack.is(MekanismItems.ENERGY_TABLET) && EMDataComponents.isActive(stack)) {
            chargeFromTablet(player, stack);
        }
    }

    public static void handleAction(ServerPlayer player, PacketCurioAction.Action action, String slot) {
        switch (action) {
            case OPEN_QIO -> openGui(player, CuriosHelper.findInSlot(player, CuriosSlots.PORTABLE_QIO_DASHBOARD));
            case OPEN_TELEPORTER -> openGui(player, CuriosHelper.findInSlot(player, CuriosSlots.PORTABLE_TELEPORTER));
            case DRINK_CANTEEN -> startCanteenDrink(player, CuriosHelper.findInSlot(player, CuriosSlots.CANTEEN));
            case SLOT_USE -> useSlot(player, slot);
        }
    }

    private static void useSlot(ServerPlayer player, String slot) {
        ItemStack stack = CuriosHelper.findInSlot(player, slot);
        if (stack.isEmpty()) {
            return;
        }
        if (stack.is(MekanismItems.CANTEEN)) {
            startCanteenDrink(player, stack);
        } else if (stack.getItem() instanceof IGuiItem) {
            openGui(player, stack);
        } else if (stack.getItem() instanceof IModeItem modeItem) {
            modeItem.changeMode(player, stack, 1, DisplayChange.OTHER);
        } else if (stack.is(MekanismItems.ENERGY_TABLET) || stack.getItem() instanceof ItemPortableHazmatSuit) {
            boolean enabled = EMDataComponents.toggle(stack);
            player.displayClientMessage((enabled
                    ? fr.iglee42.evolvedmekanism.EvolvedMekanismLang.CURIO_ENABLED
                    : fr.iglee42.evolvedmekanism.EvolvedMekanismLang.CURIO_DISABLED).translate(), true);
        }
    }

    private static void openGui(ServerPlayer player, ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof IGuiItem guiItem)) {
            return;
        }
        guiItem.getContainerType().tryOpenGui(player, InteractionHand.MAIN_HAND, stack);
    }

    private static void startCanteenDrink(ServerPlayer player, ItemStack canteen) {
        if (canteen.isEmpty() || !(canteen.getItem() instanceof ItemCanteen item)) {
            return;
        }
        if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof ItemCanteen) {
            return;
        }
        if (!MekanismUtils.isPlayingMode(player) || !player.canEat(false) || getPasteAmount(canteen) < 50) {
            return;
        }
        CANTEEN_DRINK_TICKS.put(player.getUUID(), item.getUseDuration(canteen, player));
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }
        tickCanteen(player);
        tickScuba(player);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onMagicDamage(LivingIncomingDamageEvent event) {
        if (!event.getSource().is(MekanismAPITags.DamageTypes.IS_PREVENTABLE_MAGIC)) {
            return;
        }
        if (event.getEntity() instanceof Player player && isScubaSetOn(player)) {
            event.setCanceled(true);
        }
    }

    private static void tickCanteen(Player player) {
        Integer remaining = CANTEEN_DRINK_TICKS.get(player.getUUID());
        if (remaining == null) {
            return;
        }
        ItemStack canteen = CuriosHelper.findInSlot(player, CuriosSlots.CANTEEN);
        if (canteen.isEmpty() || !(canteen.getItem() instanceof ItemCanteen item) || !player.canEat(false) || getPasteAmount(canteen) < 50) {
            CANTEEN_DRINK_TICKS.remove(player.getUUID());
            return;
        }
        if (remaining % 4 == 0) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.5F, player.level().random.nextFloat() * 0.1F + 0.9F);
        }
        if (remaining <= 1) {
            CANTEEN_DRINK_TICKS.remove(player.getUUID());
            item.finishUsingItem(canteen, player.level(), player);
            return;
        }
        CANTEEN_DRINK_TICKS.put(player.getUUID(), remaining - 1);
    }

    private static void tickScuba(Player player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        if (chest.getItem() instanceof ItemScubaTank && head.getItem() instanceof ItemScubaMask) {
            return;
        }
        ItemStack tankStack = resolveScubaTank(player);
        ItemStack maskStack = resolveScubaMask(player);
        if (!isScubaPair(tankStack, maskStack)) {
            return;
        }
        ItemScubaTank tank = (ItemScubaTank) tankStack.getItem();
        int max = player.getMaxAirSupply();
        tank.useChemical(tankStack, 1);
        ChemicalStack received = tank.useChemical(tankStack, max - player.getAirSupply());
        if (!received.isEmpty()) {
            player.setAirSupply(player.getAirSupply() + (int) received.getAmount());
        }
        if (player.getAirSupply() == max) {
            for (MobEffectInstance effect : player.getActiveEffects()) {
                if (MekanismUtils.shouldSpeedUpEffect(effect)) {
                    for (int i = 0; i < 9; i++) {
                        MekanismUtils.speedUpEffectSafely(player, effect);
                    }
                }
            }
        }
    }

    public static boolean isScubaSetOn(Player player) {
        return isScubaPair(resolveScubaTank(player), resolveScubaMask(player));
    }

    private static ItemStack resolveScubaTank(Player player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.getItem() instanceof ItemScubaTank) {
            return chest;
        }
        return CuriosHelper.findInSlot(player, CuriosSlots.SCUBA_TANK);
    }

    private static ItemStack resolveScubaMask(Player player) {
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        if (head.getItem() instanceof ItemScubaMask) {
            return head;
        }
        return CuriosHelper.findInSlot(player, CuriosSlots.SCUBA_MASK);
    }

    private static boolean isScubaPair(ItemStack tankStack, ItemStack maskStack) {
        return !tankStack.isEmpty() && !maskStack.isEmpty()
                && tankStack.getItem() instanceof ItemScubaTank tank
                && maskStack.getItem() instanceof ItemScubaMask
                && ChemicalUtil.hasAnyChemical(tankStack)
                && tank.getMode(tankStack);
    }

    private static int getPasteAmount(ItemStack stack) {
        IFluidHandlerItem handler = Capabilities.FLUID.getCapability(stack);
        if (handler == null) {
            return 0;
        }
        FluidStack fluid = StorageUtils.getContainedFluid(handler, MekanismFluids.NUTRITIONAL_PASTE.asStack(1));
        return fluid.getAmount();
    }

    private static void chargeFromTablet(Player player, ItemStack tablet) {
        IEnergyContainer energyContainer = StorageUtils.getEnergyContainer(tablet, 0);
        if (energyContainer == null) {
            return;
        }
        long toCharge = Math.min(MekanismConfig.gear.mekaSuitInventoryChargeRate.get(), energyContainer.getEnergy());
        if (toCharge == 0L) {
            return;
        }
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        toCharge = charge(energyContainer, tablet, mainHand, toCharge);
        toCharge = charge(energyContainer, tablet, offHand, toCharge);
        if (toCharge == 0L) {
            return;
        }
        for (ItemStack armor : player.getArmorSlots()) {
            toCharge = charge(energyContainer, tablet, armor, toCharge);
            if (toCharge == 0L) {
                return;
            }
        }
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < 9; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack == mainHand || stack == offHand) {
                continue;
            }
            toCharge = charge(energyContainer, tablet, stack, toCharge);
            if (toCharge == 0L) {
                return;
            }
        }
        var handler = CuriosHelper.itemHandler(player);
        if (handler == null) {
            return;
        }
        for (int slot = 0, slots = handler.getSlots(); slot < slots; slot++) {
            toCharge = charge(energyContainer, tablet, handler.getStackInSlot(slot), toCharge);
            if (toCharge == 0L) {
                return;
            }
        }
    }

    private static long charge(IEnergyContainer energyContainer, ItemStack tablet, ItemStack stack, long amount) {
        if (stack.isEmpty() || amount <= 0L || stack == tablet) {
            return amount;
        }
        IStrictEnergyHandler handler = EnergyCompatUtils.getStrictEnergyHandler(stack);
        if (handler == null) {
            return amount;
        }
        long remaining = handler.insertEnergy(amount, Action.SIMULATE);
        if (remaining >= amount) {
            return amount;
        }
        long extracted = energyContainer.extract(amount - remaining, Action.EXECUTE, AutomationType.MANUAL);
        long inserted = handler.insertEnergy(extracted, Action.EXECUTE);
        return inserted + remaining;
    }
}
