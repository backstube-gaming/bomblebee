package net.backstube.bomblebee.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class Packets {

    public static void register() {
        // Client to Server


        // Server to Client
        PayloadTypeRegistry.playS2C().register(BombleBeeActivatePayload.ID, BombleBeeActivatePayload.CODEC);
    }

    //public static final Identifier C2S_SAVE_CUSTOM_MAP_CONFIG = new Identifier(Bomblebee.MOD_ID,
    //        "save_custom_map_config");
}

