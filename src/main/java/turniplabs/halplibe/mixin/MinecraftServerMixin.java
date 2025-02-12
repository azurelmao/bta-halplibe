package turniplabs.halplibe.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Global;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import turniplabs.halplibe.helper.network.NetworkHandler;
import turniplabs.halplibe.util.BlockInitEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.ItemInitEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

@Mixin(value = MinecraftServer.class, remap = false)
public class MinecraftServerMixin {
    @Shadow private static MinecraftServer instance;

    @Inject(method = "startServer", at = @At(value = "INVOKE",target = "Lnet/minecraft/core/data/DataLoader;loadRecipesFromFile(Ljava/lang/String;)V", ordinal = 3, shift = At.Shift.AFTER))
    public void recipeEntrypoint(CallbackInfoReturnable<Boolean> cir){
        FabricLoader.getInstance().getEntrypoints("recipesReady", RecipeEntrypoint.class).forEach(RecipeEntrypoint::initNamespaces);
        FabricLoader.getInstance().getEntrypoints("recipesReady", RecipeEntrypoint.class).forEach(RecipeEntrypoint::onRecipesReady);
    }
    @Inject(method = "startServer", at = @At("HEAD"))
    public void beforeGameStartEntrypoint(CallbackInfoReturnable<Boolean> cir){
        instance = (MinecraftServer)(Object)this;
        Global.isServer = true;
        NetworkHandler.setup();
        FabricLoader.getInstance().getEntrypoints("beforeGameStart", GameStartEntrypoint.class).forEach(GameStartEntrypoint::beforeGameStart);
    }

    @Inject(method = "startServer", at = @At("TAIL"))
    public void afterGameStartEntrypoint(CallbackInfoReturnable<Boolean> cir){
        FabricLoader.getInstance().getEntrypoints("afterGameStart", GameStartEntrypoint.class).forEach(GameStartEntrypoint::afterGameStart);
    }

    @Inject(method = "startServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Blocks;init()V", shift = At.Shift.AFTER))
    public void afterBlockInitEntrypoint(CallbackInfoReturnable<Boolean> cir) {
        FabricLoader.getInstance().getEntrypoints("afterBlockInit", BlockInitEntrypoint.class).forEach(BlockInitEntrypoint::afterBlockInit);;
    }

    @Inject(method = "startServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/item/Items;init()V", shift = At.Shift.AFTER))
    public void afterItemInitEntrypoint(CallbackInfoReturnable<Boolean> cir) {
        FabricLoader.getInstance().getEntrypoints("afterItemInit", ItemInitEntrypoint.class).forEach(ItemInitEntrypoint::afterItemInit);;
    }

    /*
     * @author sunsetsatellite
     * @reason begone log4j (this fixes logging not existing on a modded server at the cost of no gui)

    @Overwrite
    public static void main(String[] args) {
        StatList.init();

        try {
            MinecraftServer minecraftserver = new MinecraftServer();
            (new ThreadServerApplication("Server thread", minecraftserver)).start();
        } catch (Exception e) {
            logger.error("Failed to start the minecraft server", e);
        }

    }*/
}
