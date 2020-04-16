package me.creepinson.creepinoutils.serializer;

import com.google.gson.annotations.Expose;
import me.creepinson.creepinoutils.api.util.math.shape.Cuboid;
import net.minecraft.block.*;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public class BlockInfoHolder {
    private static final String BLOCK_HARDNESS_FIELD = "field_149782_v"; // 1.12_stable39;

    @Expose
    public float lightValue;

    @Expose
    public float resistance;

    @Expose
    public float hardness;

    @Expose
    public String registryName;

    public BlockInfoHolder(@Nonnull Block b) {
        this.registryName = b.getRegistryName().toString();
/*        this.hardness = (float) ReflectionUtils.getFieldValue("blockHardness", b);
        this.resistance = (float) ReflectionUtils.getFieldValue("blockResistance", b);
        this.lightValue = (float) ReflectionUtils.getFieldValue("lightValue", b);*/
    }

    public static class CuboidUtils {
        protected static final Cuboid NORTH_CHEST_AABB = new Cuboid(0.0625D, 0.0D, 0.0D, 0.9375D, 0.875D, 0.9375D);
        protected static final Cuboid SOUTH_CHEST_AABB = new Cuboid(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 1.0D);
        protected static final Cuboid WEST_CHEST_AABB = new Cuboid(0.0D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
        protected static final Cuboid EAST_CHEST_AABB = new Cuboid(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);
        protected static final Cuboid NOT_CONNECTED_AABB = new Cuboid(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

        protected static final Cuboid[] REDSTONE_WIRE_AABB = new Cuboid[]{new Cuboid(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new Cuboid(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new Cuboid(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new Cuboid(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new Cuboid(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new Cuboid(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new Cuboid(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new Cuboid(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new Cuboid(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new Cuboid(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new Cuboid(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new Cuboid(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new Cuboid(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new Cuboid(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D), new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D)};

        protected static final Cuboid DOOR_SOUTH_AABB = new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
        protected static final Cuboid DOOR_NORTH_AABB = new Cuboid(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
        protected static final Cuboid DOOR_WEST_AABB = new Cuboid(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        protected static final Cuboid DOOR_EAST_AABB = new Cuboid(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);

        protected static final Cuboid[] FENCE_BOUNDING_BOXES = new Cuboid[]{new Cuboid(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new Cuboid(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), new Cuboid(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), new Cuboid(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), new Cuboid(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D), new Cuboid(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D), new Cuboid(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D), new Cuboid(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D), new Cuboid(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), new Cuboid(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), new Cuboid(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), new Cuboid(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), new Cuboid(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D), new Cuboid(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D), new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

        // Fence Gate
        protected static final Cuboid AABB_HITBOX_ZAXIS = new Cuboid(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
        protected static final Cuboid AABB_HITBOX_XAXIS = new Cuboid(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D);
        protected static final Cuboid AABB_HITBOX_ZAXIS_INWALL = new Cuboid(0.0D, 0.0D, 0.375D, 1.0D, 0.8125D, 0.625D);
        protected static final Cuboid AABB_HITBOX_XAXIS_INWALL = new Cuboid(0.375D, 0.0D, 0.0D, 0.625D, 0.8125D, 1.0D);

        // Glass Pane
        protected static final Cuboid[] PANE_AABB_BY_INDEX = new Cuboid[]{new Cuboid(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new Cuboid(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new Cuboid(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new Cuboid(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new Cuboid(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new Cuboid(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new Cuboid(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new Cuboid(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new Cuboid(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new Cuboid(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new Cuboid(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new Cuboid(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new Cuboid(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new Cuboid(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};


        // Stairs
        protected static final Cuboid[] STAIRS = new Cuboid[]{
                new Cuboid(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D),
                new Cuboid(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D),
                new Cuboid(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D),
                new Cuboid(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D),
                new Cuboid(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D),
                new Cuboid(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D),
                new Cuboid(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D),
                new Cuboid(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D),
                new Cuboid(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D),
                new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
                new Cuboid(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D),
                new Cuboid(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
                new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D),
                new Cuboid(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D),
                new Cuboid(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D),
                new Cuboid(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D),
                new Cuboid(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D),
                new Cuboid(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D)
        };

        // Vines
        protected static final Cuboid UP_AABB = new Cuboid(0.0D, 0.9375D, 0.0D, 1.0D, 1.0D, 1.0D);
        protected static final Cuboid WEST_AABB = new Cuboid(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
        protected static final Cuboid EAST_AABB = new Cuboid(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        protected static final Cuboid NORTH_AABB = new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
        protected static final Cuboid SOUTH_AABB = new Cuboid(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);

        // Walls (Cobblestone, etc.)
        protected static final Cuboid[] WALL_AABB_BY_INDEX = new Cuboid[]{new Cuboid(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new Cuboid(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new Cuboid(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new Cuboid(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new Cuboid(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new Cuboid(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D), new Cuboid(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new Cuboid(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D), new Cuboid(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D), new Cuboid(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new Cuboid(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D), new Cuboid(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new Cuboid(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new Cuboid(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

        public static Set<Cuboid> getBoxes(Block b) {
            Set<Cuboid> boxes = new HashSet<>();
            if (b instanceof BlockFenceGate) {
                boxes.add(AABB_HITBOX_XAXIS_INWALL);
                boxes.add(AABB_HITBOX_ZAXIS_INWALL);
                boxes.add(AABB_HITBOX_XAXIS);
                boxes.add(AABB_HITBOX_ZAXIS);
            } else if (b instanceof BlockHorizontal) {
                boxes.add(new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
            } else if (b instanceof BlockDirectional) {
                /*for (EnumFacing f : BlockDirectional.FACING.getAllowedValues()) {
                    boxes.add(b.getCuboid(b.getDefaultState().withProperty(BlockDirectional.FACING, f), null, BlockPos.ORIGIN));
                }*/
                boxes.add(new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
            } else if (b instanceof BlockChest) {
                boxes.add(NOT_CONNECTED_AABB);
                boxes.add(NORTH_CHEST_AABB);
                boxes.add(SOUTH_CHEST_AABB);
                boxes.add(WEST_CHEST_AABB);
                boxes.add(EAST_CHEST_AABB);
            } else if (b instanceof BlockRedstoneWire) {
                boxes.addAll(Arrays.asList(REDSTONE_WIRE_AABB));
            } else if (b instanceof BlockDoor) {
                boxes.add(DOOR_SOUTH_AABB);
                boxes.add(DOOR_NORTH_AABB);
                boxes.add(DOOR_WEST_AABB);
                boxes.add(DOOR_EAST_AABB);
            } else if (b instanceof BlockFence) {
                boxes.addAll(Arrays.asList(FENCE_BOUNDING_BOXES));
            } else if (b instanceof BlockStairs) {
                boxes.addAll(Arrays.asList(STAIRS));
            } else if (b instanceof BlockPane) {
                boxes.addAll(Arrays.asList(PANE_AABB_BY_INDEX));
            } else if (b instanceof BlockWall) {
                boxes.addAll(Arrays.asList(WALL_AABB_BY_INDEX));
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
                boxes.add(new Cuboid(0, 0, 0, 0, 0, 0));
            } else {
//                boxes.add(b.getCuboid(b.getDefaultState(), null, BlockPos.ORIGIN));
                boxes.add(new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
            }

            return boxes;
        }

    }
}
