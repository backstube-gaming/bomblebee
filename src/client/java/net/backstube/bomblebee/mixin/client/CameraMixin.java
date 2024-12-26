package net.backstube.bomblebee.mixin.client;

import net.backstube.bomblebee.BombleBeeClientEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {

    @Shadow
    private Entity focusedEntity;
    @Shadow
    private float lastCameraY;
    @Shadow
    private float cameraY;

    // When toggling freecam, update the camera's eye height instantly without any transition.
    @Inject(method = "update", at = @At("HEAD"))
    public void bomblebee$update(BlockView area, Entity newFocusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (newFocusedEntity == null || this.focusedEntity == null || newFocusedEntity.equals(this.focusedEntity)) {
            return;
        }

        if (newFocusedEntity instanceof BombleBeeClientEntity || this.focusedEntity instanceof BombleBeeClientEntity) {
            this.lastCameraY = this.cameraY = newFocusedEntity.getEyeHeight(EntityPose.STANDING);
        }
    }
/*
    // Removes the submersion overlay when underwater, in lava, or powdered snow.
    @Inject(method = "getSubmersionType", at = @At("HEAD"), cancellable = true)
    public void bomblebee$getSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        if (BomblebeeHandler.isUsing() && !ModConfig.INSTANCE.visual.showSubmersion) {
            cir.setReturnValue(FogType.NONE);
        }
    }*/
}
