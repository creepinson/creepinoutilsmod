package me.creepinson.creepinoutils.util;

import me.creepinson.creepinoutils.api.util.math.Vector;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockData implements INBTSerializable<NBTTagCompound> {
    private IBlockState state;
    private Vector pos;
    private World world;

    public BlockData(World world, Vector pos, IBlockState state) {
        this(pos, state);
        this.world = world;
    }

    public BlockData(World world) {
        setWorld(world);
    }

    public BlockData(Vector pos, IBlockState state) {
        this.pos = pos;
        this.state = state;
    }

    public IBlockState getState() {
        return state;
    }

    public void setState(IBlockState state) {
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
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("block", state.getBlock().getRegistryName().toString());
        nbt.setInteger("meta", state.getBlock().getMetaFromState(state));
        nbt.setTag("position", VectorUtils.toNBT(pos));
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("block")));
        this.state = block.getStateFromMeta(nbt.getInteger("meta"));
        NBTTagCompound posTag = nbt.getCompoundTag("position");
        this.pos = VectorUtils.fromNBT(posTag);
    }
}
