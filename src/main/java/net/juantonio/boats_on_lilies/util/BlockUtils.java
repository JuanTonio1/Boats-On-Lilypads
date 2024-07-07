package net.juantonio.boats_on_lilies.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
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
    public static List<BlockPos> findBlocksInRadius(World world, BlockPos centerPos, Block targetBlock, double radius) {
        List<BlockPos> foundPositions = new ArrayList<>();

        Box searchBox = new Box(centerPos).expand(radius, radius, radius);

        for (BlockPos pos : BlockPos.iterate(
                (int) searchBox.minX, (int) searchBox.minY,
                (int) searchBox.minZ, (int) searchBox.maxX,
                (int) searchBox.maxY, (int) searchBox.maxZ)) {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if (block == targetBlock) {
                foundPositions.add(new BlockPos(pos));
            }
        }

        return foundPositions;
    }

    public static List<BlockPos> detectBlocksAroundPlayer(World world, Entity entity, Block targetBlock, double detectionRadius) {
        List<BlockPos> detectedBlocks = new ArrayList<>();

        // Get player's position as Vec3d
        Vec3d playerPos = entity.getPos();

        // Calculate the bounding box around the player
        Box boundingBox = new Box(playerPos.x - detectionRadius, playerPos.y - 0.5, playerPos.z - detectionRadius,
                playerPos.x + detectionRadius, playerPos.y + 1.5, playerPos.z + detectionRadius);

        // Iterate through all block positions within the bounding box
        BlockPos.stream(boundingBox).forEach(pos -> {
            BlockState state = world.getBlockState(pos);
            // Example: Check for specific block type or properties
            if (state.getBlock() == targetBlock) {
                detectedBlocks.add(new BlockPos(pos));
            }
        });

        return detectedBlocks;
    }
}
