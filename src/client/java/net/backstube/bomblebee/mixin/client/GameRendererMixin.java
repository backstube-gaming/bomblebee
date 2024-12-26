package net.backstube.bomblebee.mixin.client;

import net.backstube.bomblebee.BomblebeeHandler;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    // Disables block outlines when allowInteract is disabled.
    @Inject(method = "shouldRenderBlockOutline", at = @At("HEAD"), cancellable = true)
    private void bomblebee$shouldRenderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        if (BomblebeeHandler.isUsing() && !BomblebeeHandler.isPlayerControlEnabled() && !bomblebee$allowInteract()) {
            cir.setReturnValue(false);
        }
    }
/*
    // Makes mouse clicks come from the player rather than the freecam entity when player control is enabled or if interaction mode is set to player.
    @ModifyVariable(method = "pick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/Minecraft;getCameraEntity()Lnet/minecraft/world/entity/Entity;"))
    private Entity bomblebee$onUpdateTargetedEntity(Entity entity) {
        if (BomblebeeHandler.isUsing() && (BomblebeeHandler.isPlayerControlEnabled() || ModConfig.INSTANCE.utility.interactionMode.equals(ModConfig.InteractionMode.PLAYER))) {
            return MC.player;
        }
        return entity;
    }*/

    @Unique
    private static boolean bomblebee$allowInteract() {
        return false;
    }
}
