package net.juantonio.boats_on_lilypads.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {


    public static List<BlockPos> detectBlocksAroundPlayer(World world, Entity entity, Block targetBlock, double detectionRadius) {
        List<BlockPos> detectedBlocks = new ArrayList<>();

        Vec3d playerPos = entity.getPos();

        Box boundingBox = new Box(playerPos.x - detectionRadius, playerPos.y - 0.5, playerPos.z - detectionRadius,
                playerPos.x + detectionRadius, playerPos.y + 1.5, playerPos.z + detectionRadius);

        BlockPos.stream(boundingBox).forEach(pos -> {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == targetBlock) {
                detectedBlocks.add(new BlockPos(pos));
            }
        });

        return detectedBlocks;
    }
}
