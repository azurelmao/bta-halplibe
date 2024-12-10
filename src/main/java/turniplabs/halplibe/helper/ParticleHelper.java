package turniplabs.halplibe.helper;

import net.minecraft.client.entity.particle.ParticleDispatcher;
import net.minecraft.client.entity.particle.ParticleFirefly;
import net.minecraft.client.entity.particle.ParticleLambda;
import net.minecraft.core.util.helper.MathHelper;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.mixin.accessors.EntityFXAccessor;
import turniplabs.halplibe.mixin.accessors.EntityFireflyFXAccessor;

public final class ParticleHelper {


    @SuppressWarnings("unused") // API function
    public static void createParticle(String name, ParticleLambda lambda) {
        if (!HalpLibe.isClient) return;
        ParticleDispatcher.getInstance().addDispatch(name, lambda);
    }


    /**
     * Set the firefly particle's color using the vanilla scheme,
     * which only takes in mid RGB colors and interpolates the rest.
     */
    public static void setFireflyColor(ParticleFirefly particle, float r, float g, float b) {
        ((EntityFireflyFXAccessor) particle).setMidR(r);
        ((EntityFireflyFXAccessor) particle).setMidG(g);
        ((EntityFireflyFXAccessor) particle).setMidB(b);
        ((EntityFXAccessor) particle).setRCol(r);
        ((EntityFXAccessor) particle).setGCol(g);
        ((EntityFXAccessor) particle).setBCol(b);
        ((EntityFireflyFXAccessor) particle).setMaxR(MathHelper.clamp(r + 0.25F, 0.0F, 1.0F));
        ((EntityFireflyFXAccessor) particle).setMaxG(MathHelper.clamp(g + 0.25F, 0.0F, 1.0F));
        ((EntityFireflyFXAccessor) particle).setMaxB(MathHelper.clamp(b + 0.25F, 0.0F, 1.0F));
    }

    public static void setFireflyColorMin(ParticleFirefly particle, float r, float g, float b) {
        ((EntityFireflyFXAccessor) particle).setMinR(r);
        ((EntityFireflyFXAccessor) particle).setMinG(g);
        ((EntityFireflyFXAccessor) particle).setMinB(b);
    }

    public static void setFireflyColorMid(ParticleFirefly particle, float r, float g, float b) {
        ((EntityFireflyFXAccessor) particle).setMidR(r);
        ((EntityFireflyFXAccessor) particle).setMidG(g);
        ((EntityFireflyFXAccessor) particle).setMidB(b);
        ((EntityFXAccessor) particle).setRCol(r);
        ((EntityFXAccessor) particle).setGCol(g);
        ((EntityFXAccessor) particle).setBCol(b);
    }

    public static void setFireflyColorMax(ParticleFirefly particle, float r, float g, float b) {
        ((EntityFireflyFXAccessor) particle).setMaxR(r);
        ((EntityFireflyFXAccessor) particle).setMaxG(g);
        ((EntityFireflyFXAccessor) particle).setMaxB(b);
    }

}
