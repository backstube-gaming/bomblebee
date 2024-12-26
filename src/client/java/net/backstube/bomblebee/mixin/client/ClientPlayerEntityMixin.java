package net.backstube.bomblebee.mixin.client;

import net.backstube.bomblebee.BomblebeeHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Unique
    private ClientPlayerEntity getThis() {
        return (ClientPlayerEntity) (Object) this;
    }

    // Needed for Baritone compatibility.
    @Inject(method = "isCamera", at = @At("HEAD"), cancellable = true)
    private void onIsCamera(CallbackInfoReturnable<Boolean> cir) {
        if (BomblebeeHandler.isUsing() && getThis().equals(MinecraftClient.getInstance().player)) {
            cir.setReturnValue(true);
        }
    }
}
