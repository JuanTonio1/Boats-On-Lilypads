package net.juantonio.boats_on_lilies;

import com.ibm.icu.impl.coll.CollationLoader;
import net.fabricmc.api.ModInitializer;

import net.juantonio.boats_on_lilies.events.event.PlayerEnterBoat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoatsOnLilies implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("boats-on-lilies");

	@Override
	public void onInitialize() {
		CollisionHandler collisionHandler = new CollisionHandler();
		PlayerEnterBoat event = new PlayerEnterBoat();
	}
}