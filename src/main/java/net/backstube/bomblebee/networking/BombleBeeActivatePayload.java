package net.backstube.bomblebee.networking;

import net.backstube.bomblebee.Bomblebee;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record BombleBeeActivatePayload(int bombleBeeId) implements CustomPayload {
    public static final Identifier PACKET_ID = new Identifier(Bomblebee.MOD_ID, "bomblebee_activate");
    public static final Id<BombleBeeActivatePayload> ID = new Id<>(PACKET_ID);

    public static final PacketCodec<RegistryByteBuf, BombleBeeActivatePayload> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, BombleBeeActivatePayload::bombleBeeId,
                    BombleBeeActivatePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
