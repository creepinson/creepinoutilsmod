package me.creepinson.creepinoutils.util;

import me.creepinson.creepinoutils.api.util.math.Vector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class BlockData implements INBTSerializable<CompoundNBT> {
    private BlockState state;
    private Vector pos;
    private World world;

    public BlockData(World world, Vector pos, BlockState state) {
        this(pos, state);
        this.world = world;
    }

    public BlockData(World world) {
        setWorld(world);
    }

    public BlockData(Vector pos, BlockState state) {
        this.pos = pos;
        this.state = state;
    }

    public BlockState getState() {
        return state;
    }

    public void setState(BlockState state) {
        this.state = state;
    }

    public Vector getPosition() {
        return pos;
    }

    public void setPosition(Vector pos) {
        this.pos = pos;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Helper method to get the block from the state stored in the data container
     */
    public Block getBlock() {
        return state.getBlock();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("block", state.getBlock().getRegistryName().toString());
        nbt.putInt("meta", state.getBlock().;
        nbt.setTag("position", VectorUtils.toNBT(pos));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("block")));
        this.state = block.getStateFromMeta(nbt.getInteger("meta"));
        CompoundNBT posTag = nbt.getCompoundTag("position");
        this.pos = VectorUtils.fromNBT(posTag);
    }
}
