package net.juantonio.boats_on_lilies.events.inter;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.ActionResult;

public interface PlayerEnterBoatEvent {
    Event<PlayerEnterBoatEvent> EVENT = EventFactory.createArrayBacked(PlayerEnterBoatEvent.class,
            (listeners) -> (player, boat) -> {
                for (PlayerEnterBoatEvent listener : listeners) {
                    ActionResult result = listener.onEnterBoat(player, boat);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult onEnterBoat(PlayerEntity player, BoatEntity boat);
}
