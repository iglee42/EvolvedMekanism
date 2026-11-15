package fr.iglee42.evolvedmekanism.client;

import fr.iglee42.evolvedmekanism.tiers.EMBaseTier;
import mekanism.api.tier.BaseTier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;

public final class ClientNameAnimation {
    private static int lerpRGB(int a, int b, float t) {
        int rA = (a >> 16) & 0xFF;
        int gA = (a >> 8) & 0xFF;
        int bA = a & 0xFF;
        int rB = (b >> 16) & 0xFF;
        int gB = (b >> 8) & 0xFF;
        int bB = b & 0xFF;

        int r = (int) (rA + (rB - rA) * t);
        int g = (int) (gA + (gB - gA) * t);
        int bC = (int) (bA + (bB - bA) * t);

        return (r << 16) | (g << 8) | bC;
    }


    public static Component animateTierInstaller(ItemStack stack, Component name){
        String baseName = name.getString();
        int[] tierColors = new int[]{BaseTier.BASIC.getColor().getValue(), BaseTier.ADVANCED.getColor().getValue(), BaseTier.ELITE.getColor().getValue(), BaseTier.ULTIMATE.getColor().getValue(), EMBaseTier.OVERCLOCKED.getColor().getValue(), EMBaseTier.QUANTUM.getColor().getValue(), EMBaseTier.DENSE.getColor().getValue(), EMBaseTier.MULTIVERSAL.getColor().getValue()};
        long time = Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getGameTime() : System.currentTimeMillis() / 50;

        MutableComponent animatedName = Component.literal("");

        float speed = 6.0F;
        float shift = (time / speed) % tierColors.length;

        for (int i = 0; i < baseName.length(); i++) {
            float colorIndex = (shift + i * 0.3F) % tierColors.length;
            int indexA = (int) Math.floor(colorIndex);
            int indexB = (indexA + 1) % tierColors.length;

            float blend = colorIndex - indexA;
            int rgbA = tierColors[indexA];
            int rgbB = tierColors[indexB];
            int blended = lerpRGB(rgbA, rgbB, blend);
            animatedName.append(Component.literal(String.valueOf(baseName.charAt(i))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(blended))));
        }

        return animatedName;
    }
}
