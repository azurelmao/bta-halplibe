package turniplabs.halplibe.mixin.mixins.models;

import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.ItemBuilder;
@Mixin(value = ItemModelDispatcher.class, remap = false)
public abstract class ItemModelDispatcherMixin {
    @Shadow public abstract void addDispatch(ItemModel dispatchable);

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void addQueuedModels(CallbackInfo ci){
        for (ItemBuilder.Assignment.ItemAssignmentEntry<?> entry : ItemBuilder.Assignment.queuedItemModels.values()){
            try {
                addDispatch(entry.getModel());
            } catch (Exception e){
                throw new RuntimeException("Exception Occurred when applying " + entry.item.getKey(), e);
            }
        }
        ItemBuilder.Assignment.queuedItemModels.clear();
        ItemBuilder.Assignment.itemDispatcherInitialized = true;
    }
}
