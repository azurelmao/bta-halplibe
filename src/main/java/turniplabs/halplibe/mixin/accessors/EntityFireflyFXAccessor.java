package turniplabs.halplibe.mixin.accessors;

import net.minecraft.client.entity.particle.FireflyParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = FireflyParticle.class, remap = false)
public interface EntityFireflyFXAccessor {
    @Accessor
    void setMidR(float midR);

    @Accessor
    void setMidG(float midG);

    @Accessor
    void setMidB(float midB);

    @Mutable
    @Accessor
    void setMinR(float minR);

    @Mutable
    @Accessor
    void setMinG(float minG);

    @Mutable
    @Accessor
    void setMinB(float minB);

    @Accessor
    void setMaxR(float maxR);

    @Accessor
    void setMaxG(float maxG);

    @Accessor
    void setMaxB(float maxB);
}
