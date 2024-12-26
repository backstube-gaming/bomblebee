package net.backstube.bomblebee.mixin.client;

import net.backstube.bomblebee.BomblebeeHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    // Prevents interacting with blocks when allowInteract is disabled.
    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void bomblebee$interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (bomblebee$disableInteract()) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    // Prevents interacting with entities when allowInteract is disabled, and prevents interacting with self.
    @Inject(method = "interactEntity", at = @At("HEAD"), cancellable = true)
    private void bomblebee$interactEntity(PlayerEntity player, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (entity.equals(MinecraftClient.getInstance().player) || bomblebee$disableInteract()) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    // Prevents interacting with entities when allowInteract is disabled, and prevents interacting with self.
    @Inject(method = "interactEntityAtLocation", at = @At("HEAD"), cancellable = true)
    private void bomblebee$interactEntityAtLocation(PlayerEntity player, Entity entity, EntityHitResult hitResult, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (entity.equals(MinecraftClient.getInstance().player) || bomblebee$disableInteract()) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    // Prevents attacking self.
    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    private void bomblebee$attackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (target.equals(MinecraftClient.getInstance().player)) {
            ci.cancel();
        }
    }

    @Unique
    private static boolean bomblebee$disableInteract() {
        return BomblebeeHandler.isUsing() && !BomblebeeHandler.isPlayerControlEnabled(); /*&& !freecam$allowInteract();*/
    }

   /* @Unique
    private static boolean freecam$allowInteract() {
        return ModConfig.INSTANCE.utility.allowInteract && (BuildVariant.getInstance().cheatsPermitted() || ModConfig.INSTANCE.utility.interactionMode.equals(PLAYER));
    }*/
}
