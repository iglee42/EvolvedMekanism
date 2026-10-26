package fr.iglee42.evolvedmekanism.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

/**
 * Lava's pop, drawn with the flame sprite and tinted by the molten color.
 */
public class MoltenPopParticle extends TextureSheetParticle {

    protected MoltenPopParticle(ClientLevel level, double x, double y, double z, float r, float g, float b) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D);
        this.gravity = 0.75F;
        this.friction = 0.999F;
        this.xd *= 0.8D;
        this.yd *= 0.8D;
        this.zd *= 0.8D;
        this.yd = this.random.nextFloat() * 0.4F + 0.05F;
        this.quadSize *= this.random.nextFloat() * 2.0F + 0.2F;
        this.lifetime = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
        this.setColor(r, g, b);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTick) {
        int light = super.getLightColor(partialTick);
        int sky = light >> 16 & 255;
        return 240 | sky << 16;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        float progress = ((float) this.age + scaleFactor) / (float) this.lifetime;
        return this.quadSize * (1.0F - progress * progress);
    }

    public static class Factory implements ParticleProvider<ColoredMoltenOptions> {
        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(ColoredMoltenOptions type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            MoltenPopParticle particle = new MoltenPopParticle(level, x, y, z, type.red(), type.green(), type.blue());
            particle.pickSprite(this.sprites);
            return particle;
        }
    }
}
