package turniplabs.halplibe.helper;



import net.minecraft.client.entity.fx.EntityFX;
import net.minecraft.client.entity.fx.EntityFireflyFX;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import turniplabs.halplibe.mixin.accessors.EntityFXAccessor;
import turniplabs.halplibe.mixin.accessors.EntityFireflyFXAccessor;

import java.util.HashMap;
import java.util.Map;

abstract public class ParticleHelper {
    public static Map<String, Class<? extends EntityFX>> particlesOld = new HashMap<>();
    public static Map<String, ParticleLambda> particles = new HashMap<>();


    @SuppressWarnings("unused") // API function
    public static void createParticle(String name, ParticleLambda lambda) {
        particles.put(name, lambda);
    }


    /**
     * Set the firefly particle's color using the vanilla scheme,
     * which only takes in mid RGB colors and interpolates the rest.
     */
    public static void setFireflyColor(EntityFireflyFX particle, float r, float g, float b) {
        ((EntityFireflyFXAccessor) particle).setMidR(r);
        ((EntityFireflyFXAccessor) particle).setMidG(g);
        ((EntityFireflyFXAccessor) particle).setMidB(b);
        ((EntityFXAccessor) particle).setParticleRed(r);
        ((EntityFXAccessor) particle).setParticleGreen(g);
        ((EntityFXAccessor) particle).setParticleBlue(b);
        ((EntityFireflyFXAccessor) particle).setMaxR(MathHelper.clamp(r + 0.25F, 0.0F, 1.0F));
        ((EntityFireflyFXAccessor) particle).setMaxG(MathHelper.clamp(g + 0.25F, 0.0F, 1.0F));
        ((EntityFireflyFXAccessor) particle).setMaxB(MathHelper.clamp(b + 0.25F, 0.0F, 1.0F));
    }

    public static void setFireflyColorMin(EntityFireflyFX particle, float r, float g, float b) {
        ((EntityFireflyFXAccessor) particle).setMinR(r);
        ((EntityFireflyFXAccessor) particle).setMinG(g);
        ((EntityFireflyFXAccessor) particle).setMinB(b);
    }

    public static void setFireflyColorMid(EntityFireflyFX particle, float r, float g, float b) {
        ((EntityFireflyFXAccessor) particle).setMidR(r);
        ((EntityFireflyFXAccessor) particle).setMidG(g);
        ((EntityFireflyFXAccessor) particle).setMidB(b);
        ((EntityFXAccessor) particle).setParticleRed(r);
        ((EntityFXAccessor) particle).setParticleRed(g);
        ((EntityFXAccessor) particle).setParticleRed(b);
    }

    public static void setFireflyColorMax(EntityFireflyFX particle, float r, float g, float b) {
        ((EntityFireflyFXAccessor) particle).setMaxR(r);
        ((EntityFireflyFXAccessor) particle).setMaxG(g);
        ((EntityFireflyFXAccessor) particle).setMaxB(b);
    }

    @FunctionalInterface
    public interface ParticleLambda {
        EntityFX run(World world, double x, double y, double z, double motionX, double motionY, double motionZ);
    }
}
