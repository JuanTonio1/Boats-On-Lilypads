package net.juantonio.boats_on_lilies;

import com.ibm.icu.impl.coll.CollationLoader;
import net.fabricmc.api.ModInitializer;

import net.juantonio.boats_on_lilies.events.event.PlayerEnterBoat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoatsOnLilies implements ModInitializer {
	@Override
	public void onInitialize() {
		CollisionHandler collisionHandler = new CollisionHandler();
		PlayerEnterBoat event = new PlayerEnterBoat();
	}
}