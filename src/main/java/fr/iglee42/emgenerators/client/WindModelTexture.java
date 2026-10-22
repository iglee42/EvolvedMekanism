package fr.iglee42.emgenerators.client;

import fr.iglee42.emgenerators.tiers.AdvancedWindGeneratorTier;
import fr.iglee42.evolvedmekanism.EvolvedMekanism;
import net.minecraft.resources.ResourceLocation;

public final class WindModelTexture {

    private static ResourceLocation current;

    private WindModelTexture() {
    }

    public static ResourceLocation forTier(AdvancedWindGeneratorTier tier) {
        return EvolvedMekanism.rl("render/wind_generators/" + tier.getBaseTier().getLowerName() + "_wind_generator.png");
    }

    public static void set(ResourceLocation texture) {
        current = texture;
    }

    public static void clear() {
        current = null;
    }

    public static ResourceLocation current() {
        return current;
    }
}
