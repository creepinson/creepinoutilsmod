package me.creepinson.creepinoutils.serializer;

import com.google.gson.annotations.Expose;
import net.minecraft.block.*;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public class BlockInfoHolder {
    private static final String BLOCK_HARDNESS_FIELD = "field_149782_v"; // 1.12_stable39;

    @Expose
    public Set<BoundingBox> boundingBoxes;

    @Expose
    public float resistance;

    @Expose
    public float hardness;

    @Expose
    public String registryName;

    public BlockInfoHolder(@Nonnull Block b) {
        this.registryName = b.getRegistryName().toString();
        this.hardness = (float) ReflectionUtils.getFieldValue(BLOCK_HARDNESS_FIELD, b);
        this.resistance = (float) ReflectionUtils.getFieldValue("blockResistance", b);
        this.boundingBoxes = BoundingBoxUtils.getBoundingBoxes(b);
    }

    public static class BoundingBoxUtils {
        protected static final BoundingBox NORTH_CHEST_AABB = new BoundingBox(0.0625D, 0.0D, 0.0D, 0.9375D, 0.875D, 0.9375D);
        protected static final BoundingBox SOUTH_CHEST_AABB = new BoundingBox(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 1.0D);
        protected static final BoundingBox WEST_CHEST_AABB = new BoundingBox(0.0D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
        protected static final BoundingBox EAST_CHEST_AABB = new BoundingBox(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);
        protected static final BoundingBox NOT_CONNECTED_AABB = new BoundingBox(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

        protected static final BoundingBox[] REDSTONE_WIRE_AABB = new BoundingBox[]{new BoundingBox(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new BoundingBox(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new BoundingBox(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new BoundingBox(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new BoundingBox(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new BoundingBox(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new BoundingBox(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new BoundingBox(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new BoundingBox(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new BoundingBox(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new BoundingBox(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D)};

        protected static final BoundingBox DOOR_SOUTH_AABB = new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
        protected static final BoundingBox DOOR_NORTH_AABB = new BoundingBox(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
        protected static final BoundingBox DOOR_WEST_AABB = new BoundingBox(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        protected static final BoundingBox DOOR_EAST_AABB = new BoundingBox(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);

        protected static final BoundingBox[] FENCE_BOUNDING_BOXES = new BoundingBox[]{new BoundingBox(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new BoundingBox(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new BoundingBox(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), new BoundingBox(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D), new BoundingBox(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D), new BoundingBox(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D), new BoundingBox(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), new BoundingBox(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), new BoundingBox(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), new BoundingBox(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D), new BoundingBox(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D), new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

        // Fence Gate
        protected static final BoundingBox AABB_HITBOX_ZAXIS = new BoundingBox(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
        protected static final BoundingBox AABB_HITBOX_XAXIS = new BoundingBox(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
        protected static final BoundingBox AABB_HITBOX_ZAXIS_INWALL = new BoundingBox(0.0D, 0.0D, 0.375D, 1.0D, 0.8125D, 0.625D);
        protected static final BoundingBox AABB_HITBOX_XAXIS_INWALL = new BoundingBox(0.375D, 0.0D, 0.0D, 0.625D, 0.8125D, 1.0D);

        // Glass Pane
        protected static final BoundingBox[] PANE_AABB_BY_INDEX = new BoundingBox[]{new BoundingBox(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new BoundingBox(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new BoundingBox(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new BoundingBox(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new BoundingBox(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new BoundingBox(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new BoundingBox(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new BoundingBox(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new BoundingBox(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new BoundingBox(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new BoundingBox(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};


        // Stairs
        protected static final BoundingBox[] STAIRS = new BoundingBox[]{
                new BoundingBox(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D),
                new BoundingBox(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D),
                new BoundingBox(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D),
                new BoundingBox(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D),
                new BoundingBox(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D),
                new BoundingBox(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D),
                new BoundingBox(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D),
                new BoundingBox(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D),
                new BoundingBox(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D),
                new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
                new BoundingBox(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D),
                new BoundingBox(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
                new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D),
                new BoundingBox(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D),
                new BoundingBox(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D),
                new BoundingBox(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D),
                new BoundingBox(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D),
                new BoundingBox(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D)
        };

        // Vines
        protected static final BoundingBox UP_AABB = new BoundingBox(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
        protected static final BoundingBox WEST_AABB = new BoundingBox(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
        protected static final BoundingBox EAST_AABB = new BoundingBox(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        protected static final BoundingBox NORTH_AABB = new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
        protected static final BoundingBox SOUTH_AABB = new BoundingBox(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);

        // Walls (Cobblestone, etc.)
        protected static final BoundingBox[] WALL_AABB_BY_INDEX = new BoundingBox[]{new BoundingBox(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new BoundingBox(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new BoundingBox(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new BoundingBox(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new BoundingBox(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new BoundingBox(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D), new BoundingBox(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D), new BoundingBox(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D), new BoundingBox(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new BoundingBox(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new BoundingBox(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

        public static Set<BoundingBox> getBoundingBoxes(Block b) {
            Set<BoundingBox> boxes = new HashSet<>();
            if (b instanceof BlockFenceGate) {
                boxes.add(AABB_HITBOX_XAXIS_INWALL);
                boxes.add(AABB_HITBOX_ZAXIS_INWALL);
                boxes.add(AABB_HITBOX_XAXIS);
                boxes.add(AABB_HITBOX_ZAXIS);
            } else if (b instanceof BlockHorizontal) {
                boxes.add(new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
            } else if (b instanceof BlockDirectional) {
                /*for (EnumFacing f : BlockDirectional.FACING.getAllowedValues()) {
                    boxes.add(b.getBoundingBox(b.getDefaultState().withProperty(BlockDirectional.FACING, f), null, BlockPos.ORIGIN));
                }*/
                boxes.add(new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
            } else if (b instanceof BlockChest) {
                boxes.add(NOT_CONNECTED_AABB);
                boxes.add(NORTH_CHEST_AABB);
                boxes.add(SOUTH_CHEST_AABB);
                boxes.add(WEST_CHEST_AABB);
                boxes.add(EAST_CHEST_AABB);
            } else if (b instanceof BlockRedstoneWire) {
                for (BoundingBox box : REDSTONE_WIRE_AABB) {
                    boxes.add(box);
                }
            } else if (b instanceof BlockDoor) {
                boxes.add(DOOR_SOUTH_AABB);
                boxes.add(DOOR_NORTH_AABB);
                boxes.add(DOOR_WEST_AABB);
                boxes.add(DOOR_EAST_AABB);
            } else if (b instanceof BlockFence) {
                for (BoundingBox box : FENCE_BOUNDING_BOXES) {
                    boxes.add(box);
                }
            } else if (b instanceof BlockStairs) {
                for (BoundingBox box : STAIRS) {
                    boxes.add(box);
                }
            } else if (b instanceof BlockPane) {
                for (BoundingBox box : PANE_AABB_BY_INDEX) {
                    boxes.add(box);
                }
            } else if (b instanceof BlockWall) {
                for (BoundingBox box : WALL_AABB_BY_INDEX) {
                    boxes.add(box);
                }
            } else if (b instanceof BlockChorusPlant) {
                // TODO; add chorus plant bounding box
            } else if (b instanceof BlockVine) {
                boxes.add(UP_AABB);
                boxes.add(WEST_AABB);
                boxes.add(EAST_AABB);
                boxes.add(NORTH_AABB);
                boxes.add(SOUTH_AABB);
            } else if (b instanceof BlockShulkerBox) {
                // TODO; fix
            } else if (b instanceof BlockAir) {
                boxes.add(new BoundingBox(0, 0, 0, 0, 0, 0));
            } else {
//                boxes.add(b.getBoundingBox(b.getDefaultState(), null, BlockPos.ORIGIN));
                boxes.add(new BoundingBox(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
            }

            return boxes;
        }

    }
}
