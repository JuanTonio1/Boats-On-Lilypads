package net.juantonio.boats_on_lilies;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.juantonio.boats_on_lilies.CustomLilyPadBlock.SUBMERGED;

public class CollisionHandler {

    public static int MAX_TICK = 20;
    public static List<Map<String, Object>> dataList = new ArrayList<>();

    public static List<BlockPos> submergedList = new ArrayList<>();
    public CollisionHandler() {
        ServerTickEvents.START_SERVER_TICK.register(this::startServerTick);
        ServerTickEvents.END_SERVER_TICK.register(this::endServerTick);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::serverStopping);
    }

    public static void boatCollision(BlockPos blockPos, World world)   {
        MinecraftServer server = world.getServer();
        if (server != null) {
            if (world.getBlockState(blockPos).getBlock() != Blocks.LILY_PAD) {
                return;
            }
            BlockPos immutable = new BlockPos(blockPos);
            BlockState state = world.getBlockState(immutable);

            if (!state.getBlock().equals(Blocks.LILY_PAD)) {
                return;
            }
            for (Map<String, Object> dataMap : dataList) {
                BlockPos pos = (BlockPos) dataMap.get("pos");
                if (!pos.equals(immutable)) {
                    continue;
                }
                dataMap.put("tick", 0);
                dataMap.put("boatTick", 0);
            }

            setCollidable(false, false, world, immutable);
            updateLists(world);
        }
    }

    public static void setCollidable(boolean collidable,
                                     boolean forceRemove, World world, BlockPos temp) {
        BlockPos immutable = new BlockPos(temp);
        BlockState state = world.getBlockState(immutable);
        if (collidable) {
            if (forceRemove) {
                world.setBlockState(immutable, state.with(SUBMERGED, false));

                submergedList.remove(immutable);
                return;
            }
            for (Map<String, Object> dataMap : dataList) {

                BlockPos pos = (BlockPos) dataMap.get("pos");

                if (!pos.equals(immutable)) {
                    continue;
                }

                int tick = (int) dataMap.get("tick");

                if (tick >= MAX_TICK) {
                    world.setBlockState(immutable, state.with(SUBMERGED, false));

                    submergedList.remove(immutable);
                }
                return;
            }
            world.setBlockState(immutable, state.with(SUBMERGED, false));

            submergedList.remove(immutable);
        } else {
            world.setBlockState(immutable, state.with(SUBMERGED, true));

            if (submergedList.contains(immutable)) {
                return;
            }
            submergedList.add(immutable);

            Map<String, Object> dataMap = new HashMap<>();

            dataMap.put("tick", 0);
            dataMap.put("boatTick", 0);
            dataMap.put("pos", immutable);

            dataList.add(dataMap);

        }
    }

    private static void updateLists(World world) {
        MinecraftServer  server = world.getServer();
        if (server != null) {
            server.execute(() -> {
                List<BlockPos> submergeClearList = new ArrayList<>();
                for (BlockPos pos : submergedList) {
                    BlockState state = world.getBlockState(pos);
                    if (!state.getBlock().equals(Blocks.LILY_PAD)) {
                        submergeClearList.add(pos);
                        continue;
                    }
                    if (!state.get(SUBMERGED)) {
                        submergeClearList.add(pos);
                    }
                }

                List<Map<String, Object>> dataClearList = new ArrayList<>();
                for (Map<String, Object> dataMap : dataList) {
                    BlockPos pos = (BlockPos) dataMap.get("pos");
                    BlockState state = world.getBlockState(pos);
                    if (!state.getBlock().equals(Blocks.LILY_PAD)) {
                        dataClearList.add(dataMap);
                        continue;
                    }
                    if (!state.get(SUBMERGED)) {
                        dataClearList.add(dataMap);
                    }
                }

                for (BlockPos pos : submergeClearList) {
                    submergedList.remove(pos);
                }
                submergeClearList.clear();

                for (Map<String, Object> dataMap : dataClearList) {
                    dataList.remove(dataMap);
                }
                dataClearList.clear();
            });
        }
    }

    private void startServerTick(MinecraftServer server) {

        ServerWorld world = server.getOverworld();

        incrementTick(world);
    }

    private void incrementTick(World world) {
        int count = 0;

        for (Map<String, Object> dataMap : dataList) {
            int tick = (int) dataMap.get("tick");
            BlockPos pos = (BlockPos) dataMap.get("pos");
            if (tick < MAX_TICK) {
                dataMap.put("tick", tick + 1);
            }
            if (tick >= MAX_TICK) {
                count++;
                setCollidable(true, false, world, pos);
            }
        }

        if (count > 0) {
            updateLists(world);
        }
    }

    private void endServerTick(MinecraftServer server) {
        updateLists(server.getOverworld());
    }

    private void serverStopping(MinecraftServer server) {
        World world = server.getOverworld();
        for (BlockPos pos : submergedList) {
            BlockState state = world.getBlockState(pos);
            world.setBlockState(pos, state.with(SUBMERGED, false));
        }

        dataList.clear();
        submergedList.clear();
    }
}
