package net.juantonio.boats_on_lilypads;

import net.fabricmc.api.ModInitializer;

import net.juantonio.boats_on_lilypads.events.event.PlayerEnterBoat;

public class BoatsOnLilypads implements ModInitializer {
	@Override
	public void onInitialize() {
		CollisionHandler collisionHandler = new CollisionHandler();
		PlayerEnterBoat event = new PlayerEnterBoat();
	}
}