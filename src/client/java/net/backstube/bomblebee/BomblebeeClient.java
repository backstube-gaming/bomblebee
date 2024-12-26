package net.backstube.bomblebee;

import net.backstube.bomblebee.entities.BombleBeeServerEntity;
import net.backstube.bomblebee.networking.MessageReceiver;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class BomblebeeClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(BombleBeeServerEntity.ENTITY_TYPE, BombleBeeServerEntityRenderer::new);
		MessageReceiver.SetupReceivers();
	}
}