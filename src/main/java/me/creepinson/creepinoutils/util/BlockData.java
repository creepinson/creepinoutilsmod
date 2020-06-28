package me.creepinson.creepinoutils.util;

import dev.throwouterror.util.math.Tensor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class BlockData implements INBTSerializable<CompoundNBT> {
    private BlockState state;
    private Tensor pos;
    private World world;

    public BlockData(World world, Tensor pos, BlockState state) {
        this(pos, state);
        this.world = world;
    }

    public BlockData(World world) {
        setWorld(world);
    }

    public BlockData(Tensor pos, BlockState state) {
        this.pos = pos;
        this.state = state;
    }

    public BlockState getState() {
        return state;
    }

    public void setState(BlockState state) {
        this.state = state;
    }

    public Tensor getPosition() {
        return pos;
    }

    public void setPosition(Tensor pos) {
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
        nbt.put("block", NBTUtil.writeBlockState(state));
        nbt.put("position", TensorUtils.toNBT(pos));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.state = NBTUtil.readBlockState(nbt.getCompound("block"));
        this.pos = TensorUtils.fromNBT(nbt.getCompound("position"));
    }
}
