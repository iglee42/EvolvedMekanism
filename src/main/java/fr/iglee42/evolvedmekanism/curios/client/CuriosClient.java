package fr.iglee42.evolvedmekanism.curios.client;

import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import fr.iglee42.evolvedmekanism.EvolvedMekanismLang;
import fr.iglee42.evolvedmekanism.curios.CuriosHelper;
import fr.iglee42.evolvedmekanism.curios.CuriosSlots;
import fr.iglee42.evolvedmekanism.network.PacketCurioAction;
import fr.iglee42.evolvedmekanism.registries.EMDataComponents;
import mekanism.api.gear.IHUDElement;
import mekanism.api.gear.IHUDElement.HUDColor;
import mekanism.api.gear.IModuleHelper;
import mekanism.api.radiation.IRadiationManager;
import mekanism.api.text.EnumColor;
import mekanism.client.render.MekanismCurioRenderer;
import mekanism.client.render.armor.ISpecialGear;
import mekanism.common.MekanismLang;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.config.MekanismConfig;
import mekanism.common.lib.radiation.RadiationManager;
import mekanism.common.registries.MekanismItems;
import mekanism.common.registries.MekanismModules;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import mekanism.common.util.UnitDisplayUtils;
import mekanism.common.util.UnitDisplayUtils.RadiationUnit;
import mekanism.common.util.text.TextUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class CuriosClient {

    private static final ResourceLocation DOSIMETER_ICON = MekanismUtils.getResource(ResourceType.GUI_HUD, "dosimeter.png");
    private static final ResourceLocation GEIGER_ICON = MekanismUtils.getResource(ResourceType.GUI_HUD, "geiger_counter.png");

    public static KeyMapping openQio;
    public static KeyMapping openTeleporter;

    private CuriosClient() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(CuriosClient::onClientSetup);
        modEventBus.addListener(CuriosClient::registerKeys);
        modEventBus.addListener(CuriosClient::registerOverlays);
        MinecraftForge.EVENT_BUS.addListener(CuriosClient::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(CuriosClient::onRightClickEmpty);
        MinecraftForge.EVENT_BUS.addListener(CuriosClient::onScreenMouse);
        MinecraftForge.EVENT_BUS.addListener(CuriosClient::onTooltip);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            registerGearRenderer(MekanismItems.JETPACK.asItem());
            registerGearRenderer(MekanismItems.ARMORED_JETPACK.asItem());
            registerGearRenderer(MekanismItems.FREE_RUNNERS.asItem());
            registerGearRenderer(MekanismItems.ARMORED_FREE_RUNNERS.asItem());
            registerGearRenderer(MekanismItems.SCUBA_MASK.asItem());
            registerGearRenderer(MekanismItems.SCUBA_TANK.asItem());
            registerGearRenderer(MekanismItems.HDPE_REINFORCED_ELYTRA.asItem());
        });
    }

    private static void registerGearRenderer(net.minecraft.world.item.Item item) {
        if (item instanceof ArmorItem armor && IClientItemExtensions.of(armor) instanceof ISpecialGear gear) {
            CuriosRendererRegistry.register(armor, () -> new MekanismCurioRenderer(gear.getGearModel(armor.getType())));
        }
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        openQio = new KeyMapping("key.evolvedmekanism.open_qio_dashboard", GLFW.GLFW_KEY_UNKNOWN, "key.categories.evolvedmekanism");
        openTeleporter = new KeyMapping("key.evolvedmekanism.open_portable_teleporter", GLFW.GLFW_KEY_UNKNOWN, "key.categories.evolvedmekanism");
        event.register(openQio);
        event.register(openTeleporter);
    }

    private static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "curios_hud", (gui, graphics, partialTick, width, height) -> renderHud(graphics));
    }

    private static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        while (openQio != null && openQio.consumeClick()) {
            EvolvedMekanism.packetHandler().sendToServer(new PacketCurioAction(PacketCurioAction.Action.OPEN_QIO, ""));
        }
        while (openTeleporter != null && openTeleporter.consumeClick()) {
            EvolvedMekanism.packetHandler().sendToServer(new PacketCurioAction(PacketCurioAction.Action.OPEN_TELEPORTER, ""));
        }
    }

    private static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        if (event.getHand() != net.minecraft.world.InteractionHand.MAIN_HAND) {
            return;
        }
        EvolvedMekanism.packetHandler().sendToServer(new PacketCurioAction(PacketCurioAction.Action.DRINK_CANTEEN, ""));
    }

    private static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.is(MekanismItems.ENERGY_TABLET.get()) && EMDataComponents.hasToggleState(stack)) {
            boolean enabled = EMDataComponents.isActive(stack);
            event.getToolTip().add((enabled ? EvolvedMekanismLang.CURIO_ENABLED : EvolvedMekanismLang.CURIO_DISABLED)
                    .translateColored(enabled ? EnumColor.BRIGHT_GREEN : EnumColor.DARK_RED));
        }
    }

    private static void onScreenMouse(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() != GLFW.GLFW_MOUSE_BUTTON_RIGHT || !(event.getScreen() instanceof AbstractContainerScreen<?> screen)) {
            return;
        }
        Slot slot = screen.getSlotUnderMouse();
        if (slot == null || slot.getItem().isEmpty()) {
            return;
        }
        String identifier = slotIdentifier(slot);
        if (identifier == null) {
            return;
        }
        event.setCanceled(true);
        EvolvedMekanism.packetHandler().sendToServer(new PacketCurioAction(PacketCurioAction.Action.SLOT_USE, identifier));
    }

    private static String slotIdentifier(Slot slot) {
        try {
            Method method = slot.getClass().getMethod("getIdentifier");
            Object value = method.invoke(slot);
            return value instanceof String identifier ? identifier : null;
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    private static void renderHud(GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.options.hideGui) {
            return;
        }
        List<IHUDElement> elements = new ArrayList<>();
        if (!CuriosHelper.findInSlot(player, CuriosSlots.DOSIMETER).isEmpty()
                && !IModuleHelper.INSTANCE.isEnabled(player.getItemBySlot(EquipmentSlot.HEAD), MekanismModules.DOSIMETER_UNIT)) {
            elements.add(dosimeterElement(player));
        }
        if (!CuriosHelper.findInSlot(player, CuriosSlots.GEIGER_COUNTER).isEmpty()
                && !IModuleHelper.INSTANCE.isEnabled(player.getItemBySlot(EquipmentSlot.HEAD), MekanismModules.GEIGER_UNIT)) {
            elements.add(geigerElement(player));
        }
        int x = 10;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2 - elements.size() * 10;
        for (IHUDElement element : elements) {
            graphics.blit(element.getIcon(), x, y, 0, 0, 16, 16, 16, 16);
            graphics.drawString(minecraft.font, element.getText(), x + 20, y + 4, element.getColor(), true);
            y += 18;
        }
    }

    private static IHUDElement dosimeterElement(Player player) {
        double radiation = IRadiationManager.INSTANCE.isRadiationEnabled()
                ? player.getCapability(Capabilities.RADIATION_ENTITY).map(cap -> cap.getRadiation()).orElse(0D)
                : 0;
        Component text = UnitDisplayUtils.getDisplayShort(radiation, RadiationUnit.SV, 2);
        if (MekanismConfig.common.enableDecayTimers.get() && radiation > RadiationManager.MIN_MAGNITUDE) {
            text = MekanismLang.GENERIC_WITH_PARENTHESIS.translate(text, TextUtils.getHoursMinutes(RadiationManager.get().getDecayTime(radiation, false)));
        }
        HUDColor color = radiation < RadiationManager.MIN_MAGNITUDE ? HUDColor.REGULAR : radiation < 0.1 ? HUDColor.WARNING : HUDColor.DANGER;
        return IModuleHelper.INSTANCE.hudElement(DOSIMETER_ICON, text, color);
    }

    private static IHUDElement geigerElement(Player player) {
        double magnitude = RadiationManager.get().getClientEnvironmentalRadiation();
        Component text = UnitDisplayUtils.getDisplayShort(magnitude, RadiationUnit.SV, 2);
        if (MekanismConfig.common.enableDecayTimers.get() && magnitude > RadiationManager.BASELINE) {
            text = MekanismLang.GENERIC_WITH_PARENTHESIS.translate(text, TextUtils.getHoursMinutes(RadiationManager.get().getDecayTime(RadiationManager.get().getClientMaxMagnitude(), true)));
        }
        HUDColor color = magnitude <= RadiationManager.BASELINE ? HUDColor.REGULAR : magnitude < 0.1 ? HUDColor.WARNING : HUDColor.DANGER;
        return IModuleHelper.INSTANCE.hudElement(GEIGER_ICON, text, color);
    }
}
