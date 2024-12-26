package net.backstube.bomblebee.mixin.client;

import net.backstube.bomblebee.BomblebeeHandler;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    private LivingEntity getThis(){
        return (LivingEntity)(Object)this;
    }

    // Allows for the horizontal speed of creative flight to be configured separately from vertical speed.
    @Inject(method = "getMovementSpeed()F", at = @At("HEAD"), cancellable = true)
    private void onGetMovementSpeed(CallbackInfoReturnable<Float> cir) {
        var cam = BomblebeeHandler.getCamera();
        if (BomblebeeHandler.isUsing()
                && BomblebeeHandler.config.flightMode() == BomblebeeHandler.FlightMode.CREATIVE
                && getThis().equals(cam)) {
            cir.setReturnValue((float) (BomblebeeHandler.config.horizontalSpeed() / 10.0) * (cam.isSprinting() ? 2 : 1));
        }
    }

    /*@Shadow
    public abstract float getHealth();

    // Disables freecam upon receiving damage if disableOnDamage is enabled.
    @Inject(method = "setHealth", at = @At("HEAD"))
    private void onSetHealth(float health, CallbackInfo ci) {
        if (BomblebeeHandler.isUsing() && ModConfig.INSTANCE.utility.disableOnDamage && getThis().equals(MinecraftClient.getInstance().player)) {
            if (!MinecraftClient.getInstance().player.isCreative() && getHealth() > health) {
                BomblebeeHandler.disableNextTick();
            }
        }
    }*/
}
