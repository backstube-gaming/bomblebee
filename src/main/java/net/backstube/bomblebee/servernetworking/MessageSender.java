package net.backstube.bomblebee.servernetworking;

import net.backstube.bomblebee.networking.BombleBeeActivatePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

public class MessageSender {

    public static void activateBombleBee(ServerPlayerEntity player, Entity entity) {
        ServerPlayNetworking.send(player, new BombleBeeActivatePayload(entity.getId()));
    }
}
