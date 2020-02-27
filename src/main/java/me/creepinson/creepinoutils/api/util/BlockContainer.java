package me.creepinson.creepinoutils.api.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockContainer implements INBTSerializable<NBTTagCompound> {
    private IBlockState state;
    private BlockPos pos;
    private World world;


    public BlockContainer(World world) {
        setWorld(world);
    }

    public IBlockState getState() {
        return state;
    }

    public void setState(IBlockState state) {
        this.state = state;
    }

    public BlockPos getPosition() {
        return pos;
    }

    public void setPosition(BlockPos pos) {
        this.pos = pos;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("block", state.getBlock().getRegistryName().toString());
        nbt.setInteger("meta", state.getBlock().getMetaFromState(state));
        NBTTagCompound posTag = new NBTTagCompound();
        posTag.setInteger("x", pos.getX());
        posTag.setInteger("y", pos.getY());
        posTag.setInteger("z", pos.getZ());
        nbt.setTag("position", posTag);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("block")));
        this.state = block.getStateFromMeta(nbt.getInteger("meta"));
        NBTTagCompound posTag = nbt.getCompoundTag("position");
        this.pos = new BlockPos(posTag.getInteger("x"), posTag.getInteger("y"), posTag.getInteger("z"));
    }
}
