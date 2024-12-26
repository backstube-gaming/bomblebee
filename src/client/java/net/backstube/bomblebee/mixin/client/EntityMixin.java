package net.backstube.bomblebee.mixin.client;

import net.backstube.bomblebee.BomblebeeHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Unique
    private Entity getThis() {
        return (Entity) (Object) this;
    }

    // Makes mouse input rotate the FreeCamera.
    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void bomblebee$changeLookDirection(double x, double y, CallbackInfo ci) {
        if (BomblebeeHandler.isUsing() && getThis().equals(MinecraftClient.getInstance().player)
                && !BomblebeeHandler.isPlayerControlEnabled()) {
            BomblebeeHandler.getCamera().changeLookDirection(x, y);
            ci.cancel();
        }
    }

    // Prevents FreeCamera from pushing/getting pushed by entities.
    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    private void bomblebee$pushAwayFrom(Entity entity, CallbackInfo ci) {
        if (BomblebeeHandler.isUsing() &&
                (entity.equals(BomblebeeHandler.getCamera()) || getThis().equals(BomblebeeHandler.getCamera()))) {
            ci.cancel();
        }
    }

    // Freezes the player's position if freezePlayer is enabled.
    @Inject(method = "setVelocity(DDD)V", at = @At("HEAD"), cancellable = true)
    private void bomblebee$setVelocity(CallbackInfo ci) {
        if (bomblebee$shouldFreeze()) {
            ci.cancel();
        }
    }

    // Freezes the player's position if freezePlayer is enabled.
    @Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
    private void bomblebee$updateVelocity(CallbackInfo ci) {
        if (bomblebee$shouldFreeze()) {
            ci.cancel();
        }
    }

    // Freezes the player's position if freezePlayer is enabled.
    @Inject(method = "setPos(DDD)V", at = @At("HEAD"), cancellable = true)
    private void bomblebee$setPos(CallbackInfo ci) {
        if (bomblebee$shouldFreeze()) {
            ci.cancel();
        }
    }

    // Freezes the player's position if freezePlayer is enabled.
    @Inject(method = "setPosition(DDD)V", at = @At("HEAD"), cancellable = true)
    private void bomblebee$onSetPos(CallbackInfo ci) {
        if (bomblebee$shouldFreeze()) {
            ci.cancel();
        }
    }

    @Unique
    private boolean bomblebee$shouldFreeze() {
        return BomblebeeHandler.isUsing()
                && getThis().equals(MinecraftClient.getInstance().player)
                && bomblebee$allowFreeze();
    }

    @Unique
    private boolean bomblebee$allowFreeze() {
        return /* ModConfig.INSTANCE.utility.freezePlayer
                && BuildVariant.getInstance().cheatsPermitted()
                && */ !BomblebeeHandler.isPlayerControlEnabled();
    }
}
