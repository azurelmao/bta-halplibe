package turniplabs.halplibe.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.enums.EnumOS;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import turniplabs.halplibe.helper.network.NetworkHandler;

import turniplabs.halplibe.util.BlockInitEntrypoint;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.ItemInitEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

@Mixin(
        value = Minecraft.class,
        remap = false
)

public class MinecraftMixin {

    @Inject(method = "startGame", at = @At(value = "INVOKE",target = "Lnet/minecraft/core/data/DataLoader;loadRecipesFromFile(Ljava/lang/String;)V", ordinal = 3, shift = At.Shift.AFTER))
    public void recipeEntrypoint(CallbackInfo ci){
        FabricLoader.getInstance().getEntrypoints("recipesReady", RecipeEntrypoint.class).forEach(RecipeEntrypoint::initNamespaces);
        FabricLoader.getInstance().getEntrypoints("recipesReady", RecipeEntrypoint.class).forEach(RecipeEntrypoint::onRecipesReady);
    }

    @Inject(method = "startGame", at = @At("HEAD"))
    public void beforeGameStartEntrypoint(CallbackInfo ci){
        FabricLoader.getInstance().getEntrypoints("beforeClientStart", ClientStartEntrypoint.class).forEach(ClientStartEntrypoint::beforeClientStart);
        FabricLoader.getInstance().getEntrypoints("beforeGameStart", GameStartEntrypoint.class).forEach(GameStartEntrypoint::beforeGameStart);
    }

    @Inject(method = "startGame", at = @At("TAIL"))
    public void afterGameStartEntrypoint(CallbackInfo ci){
        NetworkHandler.setup();
        FabricLoader.getInstance().getEntrypoints("afterGameStart", GameStartEntrypoint.class).forEach(GameStartEntrypoint::afterGameStart);
        FabricLoader.getInstance().getEntrypoints("afterClientStart", ClientStartEntrypoint.class).forEach(ClientStartEntrypoint::afterClientStart);
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Blocks;init()V", shift = At.Shift.AFTER))
    public void afterBlockInitEntrypoint(CallbackInfo callbackInfo) {
        FabricLoader.getInstance().getEntrypoints("afterBlockInit", BlockInitEntrypoint.class).forEach(BlockInitEntrypoint::afterBlockInit);;
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/item/Items;init()V", shift = At.Shift.AFTER))
    public void afterItemInitEntrypoint(CallbackInfo callbackInfo) {
        FabricLoader.getInstance().getEntrypoints("afterItemInit", ItemInitEntrypoint.class).forEach(ItemInitEntrypoint::afterItemInit);;
    }

    @Inject(method = "printWrongJavaVersionInfo", at = @At("HEAD"), cancellable = true)
    private void printWrongJavaVersionInfo(CallbackInfo ci) {
        if (Minecraft.getOs() == EnumOS.linux){
            System.out.println("If the game crashes with a message similar to \n\"Inconsistency detected by ld.so: dl-lookup.c: 111: check_match: Assertion `version->filename == NULL || ! _dl_name_match_p (version->filename, map)' failed!\", \nEither use Java 8 or 17 from Eclipse Adoptium!");
        }
        ci.cancel();
    }
}
