package fr.iglee42.evolvedmekanism.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class RisingBubbleParticle extends TextureSheetParticle {
    protected RisingBubbleParticle(ClientLevel level, double x, double y, double z,
                                   double vx, double vy, double vz,float r,float g,float b) {
        super(level, x, y, z, vx, vy, vz);
        this.lifetime = 20;
        this.gravity = -0.02F;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
        this.scale(0.2F + level.random.nextFloat());
        this.setColor(lighten(r,0.05f),lighten(g,0.05f),lighten(b,0.05f));
    }

    public static float lighten(float value, float factor) {
        return value + (1.0f - value) * factor;
    }


    @Override
    public void tick() {
        super.tick();
        this.yd += 0.002;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    @Override
    protected int getLightColor(float p_107249_) {
        return 240;
    }

    public static class Factory implements ParticleProvider<ColoredRisingBubleOptions> {
        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(ColoredRisingBubleOptions type, ClientLevel level,
                                       double x, double y, double z,
                                       double vx, double vy, double vz) {
            RisingBubbleParticle particle = new RisingBubbleParticle(level, x, y, z, vx, vy, vz,type.red(),type.green(),type.blue());
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
