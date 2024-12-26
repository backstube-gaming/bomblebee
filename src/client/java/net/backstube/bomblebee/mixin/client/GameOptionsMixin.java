package net.backstube.bomblebee.mixin.client;

import net.backstube.bomblebee.BomblebeeHandler;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    // Prevents switching to third person in freecam.
    @Inject(method = "setPerspective", at = @At("HEAD"), cancellable = true)
    private void bomblebee$setPerspective(CallbackInfo ci) {
        if (BomblebeeHandler.isUsing()) {
            ci.cancel();
        }
    }
}
