package net.backstube.bomblebee;

import net.backstube.bomblebee.entities.BombleBeeServerEntity;
import net.backstube.bomblebee.items.ItemsRegistry;
import net.backstube.bomblebee.networking.Packets;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bomblebee implements ModInitializer {
	public static final String MOD_ID = "bomblebee";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Packets.register();
		ItemsRegistry.register();
		BombleBeeServerEntity.register();
	}
}