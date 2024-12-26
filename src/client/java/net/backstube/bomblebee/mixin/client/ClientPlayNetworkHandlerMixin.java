package net.backstube.bomblebee.mixin.client;

import net.backstube.bomblebee.BomblebeeHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    // Disables freecam when the player respawns/switches dimensions.
    @Inject(method = "onPlayerRespawn", at = @At("HEAD"))
    private void bomblebee$onPlayerRespawn(CallbackInfo ci) {
        if (BomblebeeHandler.isUsing()) {
            BomblebeeHandler.stopUsing();
        }
    }
}
