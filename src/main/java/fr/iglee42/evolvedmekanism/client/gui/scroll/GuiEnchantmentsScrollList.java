package fr.iglee42.evolvedmekanism.client.gui.scroll;

import fr.iglee42.evolvedmekanism.utils.EMEnchantmentHelper;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiElementHolder;
import mekanism.client.gui.element.scroll.GuiScrollList;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GuiEnchantmentsScrollList extends GuiScrollList {

    private static final ResourceLocation MODULE_SELECTION = MekanismUtils.getResource(ResourceType.GUI, "module_selection.png");

    private final Supplier<ItemStack> itemSupplier;
    private ItemStack currentItem = ItemStack.EMPTY;
    private final Map<Enchantment, Integer> originalLevels = new HashMap<>();
    private final Map<Enchantment, Integer> pendingLevels = new HashMap<>();
    private final List<Enchantment> currentList = new ArrayList<>();
    private int selected = -1;

    public GuiEnchantmentsScrollList(IGuiWrapper gui, int x, int y, int height, Supplier<ItemStack> itemSupplier) {
        super(gui, x, y, 112, height, 12, GuiElementHolder.HOLDER, GuiElementHolder.HOLDER_SIZE);
        this.itemSupplier = itemSupplier;
        updateItemAndList(itemSupplier.get());
    }

    public void updateItemAndList(ItemStack stack) {
        currentItem = stack;
        originalLevels.clear();
        pendingLevels.clear();
        selected = -1;
        if (!stack.isEmpty()) {
            Map<Enchantment, Integer> levels = new HashMap<>();
            for (Enchantment enchantment : EMEnchantmentHelper.getPossibleEnchantments(stack)) {
                levels.put(enchantment, 0);
            }
            levels.putAll(EMEnchantmentHelper.getEnchantments(stack));
            originalLevels.putAll(levels);
            pendingLevels.putAll(levels);
        }
        rebuild();
    }

    private void rebuild() {
        currentList.clear();
        pendingLevels.entrySet().stream()
              .sorted(Comparator.<Map.Entry<Enchantment, Integer>>comparingInt(entry -> entry.getValue() > 0 ? 0 : (isBlocked(entry.getKey()) ? 2 : 1))
                    .thenComparing(entry -> Component.translatable(entry.getKey().getDescriptionId()).getString()))
              .forEach(entry -> currentList.add(entry.getKey()));
    }

    private void recheckItem() {
        ItemStack stack = itemSupplier.get();
        if (!ItemStack.matches(currentItem, stack)) {
            updateItemAndList(stack.copy());
        }
    }

    public Map<Enchantment, Integer> getPendingLevels() {
        return pendingLevels;
    }

    public boolean hasChanges() {
        return !pendingLevels.equals(originalLevels);
    }

    private boolean isBlocked(Enchantment enchantment) {
        if (pendingLevels.getOrDefault(enchantment, 0) > 0) {
            return false;
        }
        for (Map.Entry<Enchantment, Integer> entry : pendingLevels.entrySet()) {
            if (entry.getValue() > 0 && entry.getKey() != enchantment && !EMEnchantmentHelper.areCompatible(enchantment, entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    private int minimumLevel(Enchantment enchantment) {
        if (EMEnchantmentHelper.isCurse(enchantment)) {
            return originalLevels.getOrDefault(enchantment, 0);
        }
        return 0;
    }

    public boolean canChangeSelected(int delta) {
        Enchantment enchantment = selectedEnchantment();
        if (enchantment == null) {
            return false;
        }
        int level = pendingLevels.getOrDefault(enchantment, 0);
        int newLevel = level + delta;
        if (newLevel < minimumLevel(enchantment) || newLevel > enchantment.getMaxLevel()) {
            return false;
        }
        return delta <= 0 || level > 0 || !isBlocked(enchantment);
    }

    public void stepSelected(int direction, boolean full) {
        Enchantment enchantment = selectedEnchantment();
        if (enchantment == null) {
            return;
        }
        int level = pendingLevels.getOrDefault(enchantment, 0);
        int delta = full ? (direction > 0 ? enchantment.getMaxLevel() - level : minimumLevel(enchantment) - level) : direction;
        if (delta != 0 && canChangeSelected(delta)) {
            pendingLevels.merge(enchantment, delta, Integer::sum);
            rebuild();
        }
    }

    private Enchantment selectedEnchantment() {
        return selected >= 0 && selected < currentList.size() ? currentList.get(selected) : null;
    }

    @Override
    protected int getMaxElements() {
        return currentList.size();
    }

    @Override
    public boolean hasSelection() {
        return selectedEnchantment() != null;
    }

    @Override
    protected void setSelected(int index) {
        selected = index >= 0 && index < currentList.size() ? index : -1;
    }

    @Override
    public void clearSelection() {
        selected = -1;
    }

    @Override
    protected void renderElements(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        recheckItem();
        int start = getCurrentSelection();
        for (int i = 0; i < getFocusedElements(); i++) {
            int index = start + i;
            if (index >= currentList.size()) {
                break;
            }
            Enchantment enchantment = currentList.get(index);
            int level = pendingLevels.getOrDefault(enchantment, 0);
            boolean enabled = level > 0;
            boolean blocked = isBlocked(enchantment);
            boolean curse = EMEnchantmentHelper.isCurse(enchantment);
            int color = curse ? (enabled ? 0xFF5555 : 0xAA4444) : (blocked ? 0x5E1D1D : (enabled ? 0xFFFFFF : 0x7F7F7F));
            Component name = enabled ? enchantment.getFullname(level).copy().withStyle(ChatFormatting.RESET)
                  : Component.translatable(enchantment.getDescriptionId()).withStyle(ChatFormatting.RESET);
            int rowY = i * elementHeight;
            int state = 1;
            if (index == selected) {
                state = 2;
            } else if (mouseX >= getX() + 1 && mouseX < getX() + barXShift - 1 && mouseY >= getY() + 1 + rowY && mouseY < getY() + 1 + rowY + elementHeight) {
                state = 0;
            }
            graphics.blit(MODULE_SELECTION, relativeX + 1, relativeY + 1 + rowY, 0, elementHeight * state, 100, elementHeight, 100, 36);
            gui().renderItem(graphics, iconFor(enchantment), relativeX + 3, relativeY + 3 + rowY, 0.5F);
            drawScaledTextScaledBound(graphics, name, relativeX + 13, relativeY + 3 + rowY, color, 82, 0.7F);
        }
    }

    public ItemStack iconFor(Enchantment enchantment) {
        return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, Math.max(1, pendingLevels.getOrDefault(enchantment, 1))));
    }
}
