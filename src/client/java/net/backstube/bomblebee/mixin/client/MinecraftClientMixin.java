package net.backstube.bomblebee.mixin.client;

import net.backstube.bomblebee.BomblebeeHandler;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    // Prevents attacks when allowInteract is disabled.
    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void bomblebee$doAttack(CallbackInfoReturnable<Boolean> cir) {
        if (bomblebee$disableInteract()) {
            cir.cancel();
        }
    }

    // Prevents item pick when allowInteract is disabled.
    @Inject(method = "doItemPick", at = @At("HEAD"), cancellable = true)
    private void bomblebee$doItemPick(CallbackInfo ci) {
        if (bomblebee$disableInteract()) {
            ci.cancel();
        }
    }

    // Prevents block breaking when allowInteract is disabled.
    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void bomblebee$handleBlockBreaking(CallbackInfo ci) {
        if (bomblebee$disableInteract()) {
            ci.cancel();
        }
    }

    @Unique
    private static boolean bomblebee$disableInteract() {
        return BomblebeeHandler.isUsing() && !BomblebeeHandler.isPlayerControlEnabled();/* && !bomblebee$allowInteract()*/
    }
}
