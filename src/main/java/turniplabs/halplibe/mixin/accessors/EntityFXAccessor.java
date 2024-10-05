package turniplabs.halplibe.mixin.accessors;

import net.minecraft.client.entity.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Particle.class, remap = false)
public interface EntityFXAccessor {
    @Accessor
    void setRCol(float particleRed);

    @Accessor
    void setGCol(float particleGreen);

    @Accessor
    void setBCol(float particleBlue);
}
