package net.juantonio.boats_on_lilies;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Map;

import static net.juantonio.boats_on_lilies.CollisionHandler.MAX_TICK;

public class CustomLilyPadBlock extends LilyPadBlock {


    public static BooleanProperty SUBMERGED = BooleanProperty.of("submerged");
    public CustomLilyPadBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(SUBMERGED, false));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof BoatEntity) {
            CollisionHandler.boatCollision(pos, world);
        } else {
            if (entity instanceof PlayerEntity) {
                BlockPos immutable = new BlockPos(pos);
                // very scuffed anti-cut-player-in-half logic
                if (entity.getVehicle() == null) {

                    if (CollisionHandler.submergedList.isEmpty()) {
                        // This should never even happen
                        CollisionHandler.setCollidable(true, true, world, pos);
                        return;
                    }
                    boolean above = false;
                    boolean isNonCollidable = true;

                    for (BlockPos blockPos : CollisionHandler.submergedList) {
                        if (!blockPos.equals(immutable)) {
                            continue;
                        }
                        BlockState blockState = world.getBlockState(blockPos);
                        if (!blockState.get(SUBMERGED)) {
                            isNonCollidable = false;
                            break;
                        }
                        if (entity.getPos().getY() > immutable.getY() - .05) {
                            above = true;
                        }
                        break;
                    }

                    boolean boatTickHigh = false;

                    /*
                     * my very best effort to not make the player get
                     * cut in half. Happens from time to time and
                     * I may fix it prolly in the future
                     */
                    if (above) {
                        for (Map<String, Object> dataMap : CollisionHandler.dataList) {
                            BlockPos blockPos = (BlockPos) dataMap.get("pos");
                            if (!blockPos.equals(immutable)) {
                                continue;
                            }
                            dataMap.put("tick", 0);
                            int boatTick = (int) dataMap.get("boatTick");
                            if (boatTick < MAX_TICK) {
                                dataMap.put("boatTick", boatTick + 1);
                            }
                            if (boatTick >= MAX_TICK) {
                                boatTickHigh = true;
                            }
                            break;
                        }
                        if (boatTickHigh) {
                            CollisionHandler.setCollidable(true, true, world, pos);
                        }
                    } else {
                        if (!isNonCollidable) {
                            CollisionHandler.setCollidable(false, false, world, pos);
                        }
                    }
                }
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (!state.get(SUBMERGED)) {
            return state.getOutlineShape(world, pos, context);
        } else {
            return VoxelShapes.empty();
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SUBMERGED);
    }
}
