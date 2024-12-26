package net.backstube.bomblebee.mixin.client;

import net.backstube.bomblebee.BomblebeeHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    // Prevent rendering of nametag in inventory screen
    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    private void bomblebee$renderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci) {
        if (BomblebeeHandler.isUsing() && !MinecraftClient.getInstance().getEntityRenderDispatcher().renderShadows) {
            ci.cancel();
        }
    }
}
