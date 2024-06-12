package turniplabs.halplibe.mixin.mixins;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Global;
import net.minecraft.core.achievement.stat.StatList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.net.thread.ThreadServerApplication;
import org.apache.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

@Mixin(value = MinecraftServer.class, remap = false)
public class MinecraftServerMixin {
    @Shadow private static MinecraftServer instance;

    @Shadow public static Logger logger;

    @Inject(method = "startServer", at = @At(value = "INVOKE",target = "Lnet/minecraft/core/data/DataLoader;loadRecipesFromFile(Ljava/lang/String;)V", ordinal = 3, shift = At.Shift.AFTER))
    public void recipeEntrypoint(CallbackInfoReturnable<Boolean> cir){
        FabricLoader.getInstance().getEntrypoints("recipesReady", RecipeEntrypoint.class).forEach(RecipeEntrypoint::initNamespaces);
        FabricLoader.getInstance().getEntrypoints("recipesReady", RecipeEntrypoint.class).forEach(RecipeEntrypoint::onRecipesReady);
    }
    @Inject(method = "startServer", at = @At("HEAD"))
    public void beforeGameStartEntrypoint(CallbackInfoReturnable<Boolean> cir){
        instance = (MinecraftServer)(Object)this;
        Global.isServer = true;
        FabricLoader.getInstance().getEntrypoints("beforeGameStart", GameStartEntrypoint.class).forEach(GameStartEntrypoint::beforeGameStart);
    }

    @Inject(method = "startServer", at = @At("TAIL"))
    public void afterGameStartEntrypoint(CallbackInfoReturnable<Boolean> cir){
        FabricLoader.getInstance().getEntrypoints("afterGameStart", GameStartEntrypoint.class).forEach(GameStartEntrypoint::afterGameStart);
        if (HalpLibe.exportRecipes){
            RecipeBuilder.exportRecipes();
        }
    }

    /**
     * @author sunsetsatellite
     * @reason begone log4j (this fixes logging not existing on a modded server at the cost of no gui)
     */
    @Overwrite
    public static void main(String[] args) {
        StatList.init();

        try {
            MinecraftServer minecraftserver = new MinecraftServer();
            (new ThreadServerApplication("Server thread", minecraftserver)).start();
        } catch (Exception e) {
            logger.error("Failed to start the minecraft server", e);
        }

    }
}
