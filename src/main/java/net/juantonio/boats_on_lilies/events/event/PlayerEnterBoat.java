package net.juantonio.boats_on_lilies.events.event;

import net.juantonio.boats_on_lilies.CollisionHandler;
import net.juantonio.boats_on_lilies.events.inter.PlayerEnterBoatEvent;
import net.juantonio.boats_on_lilies.util.BlockUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class PlayerEnterBoat {

    public PlayerEnterBoat() {
        PlayerEnterBoatEvent.EVENT.register(this::onEnterBoat);
    }
    private ActionResult onEnterBoat(PlayerEntity player, BoatEntity boat) {
        List<BlockPos> lilyPadPositions = BlockUtils.detectBlocksAroundPlayer(player    .getWorld(),
                boat, Blocks.LILY_PAD, 1.6);
        float newSpeed = boat.horizontalSpeed - boat.prevHorizontalSpeed;
        int tick = 0;
        World world = player.getWorld();
        if (!lilyPadPositions.isEmpty()) {
            for (BlockPos pos : lilyPadPositions) {
                CollisionHandler.boatCollision(pos, world);
            }
        }

        return ActionResult.PASS;
    }
}
