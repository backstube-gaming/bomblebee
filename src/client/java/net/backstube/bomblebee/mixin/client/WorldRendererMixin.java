package net.backstube.bomblebee.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.backstube.bomblebee.BomblebeeHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow
    @Final
    private BufferBuilderStorage bufferBuilders;

    @Shadow
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
    }

    // Makes the player render if showPlayer is enabled.
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;checkEmpty(Lnet/minecraft/client/util/math/MatrixStack;)V", ordinal = 0))
    private void bomblebee$onRender(float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                          LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci, @Local MatrixStack matrices) {
        if (BomblebeeHandler.isUsing() /* && ModConfig.INSTANCE.visual.showPlayer */) {
            var cameraPos = camera.getPos();
            renderEntity(MinecraftClient.getInstance().player, cameraPos.x, cameraPos.y, cameraPos.z,
                    tickDelta, matrices, bufferBuilders.getEntityVertexConsumers());
        }
    }
}
