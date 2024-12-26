package net.backstube.bomblebee.networking;

import net.backstube.bomblebee.Bomblebee;
import net.backstube.bomblebee.BomblebeeHandler;
import net.backstube.bomblebee.entities.BombleBeeServerEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageReceiver {
    public static final Logger LOGGER = LoggerFactory.getLogger(Bomblebee.MOD_ID + "/client/MessageReceiver");

    public static void SetupReceivers() {
        registerGlobalReceiverWithLog(BombleBeeActivatePayload.ID,
                (payload, context) -> {
                    context.client().execute(() -> {
                        var world = context.client().world;
                        if (world == null)
                            return;

                        var entity = world.getEntityById(payload.bombleBeeId());
                        if (entity instanceof BombleBeeServerEntity serverBee) {
                            BomblebeeHandler.activate(serverBee);
                        }
                    });
                });
    }

    private static <T extends CustomPayload> void registerGlobalReceiverWithLog(CustomPayload.Id<T> channelName, ClientPlayNetworking.PlayPayloadHandler<T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(channelName,
                (payload, context) -> {
                    LOGGER.info("Message of type {} received from server", channelName.id().getPath());
                    handler.receive(payload, context);
                });
    }
}
