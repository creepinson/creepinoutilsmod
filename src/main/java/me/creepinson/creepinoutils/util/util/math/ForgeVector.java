package me.creepinson.creepinoutils.util.util.math;

import me.creepinson.creepinoutils.api.util.math.Facing;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.vecmath.Vector3f;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public class ForgeVector extends Vector3 {
    public ForgeVector(int x, int y, int z) {
        super((float) x, (float) y, (float) z);
    }

    public ForgeVector(double x, double y, double z) {
        super((float) x, (float) y, (float) z);
    }

    public ForgeVector(float x, float y, float z) {
        super(x, y, z);
    }

    public ForgeVector(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public ForgeVector(Vector3 v) {
        this(v.x, v.y, v.z);
    }

    public boolean exists(World world) {
        return world.getChunk(toBlockPos()).isLoaded();
    }

    public BlockPos toBlockPos() {
        return new BlockPos(x, y, z);
    }

    public ForgeVector fromBlockPos(BlockPos pos) {
        return new ForgeVector(pos.getX(), pos.getY(), pos.getZ());
    }

    public ForgeVector fromTile(TileEntity tile) {
        return fromBlockPos(tile.getPos());
    }

    public TileEntity getTileEntity(World w) {
        return w.getTileEntity(toBlockPos());
    }

    public ForgeVector offset(EnumFacing side) {
        return fromBlockPos(toBlockPos().offset(EnumFacing.byIndex(side.ordinal())));
    }

    public Vector3f toJava() {
        return new Vector3f(x, y, z);
    }

    public IBlockState getBlockState(World world) {
        return world.getBlockState(toBlockPos());
    }

    public Block getBlock(World world) {
        return world.getBlockState(toBlockPos()).getBlock();
    }

    public ForgeVector modifyPositionFromSide(EnumFacing side) {
        this.modifyPositionFromSide(Facing.byIndex(side.ordinal()), 1.0D);
        return this;
    }

    @Override
    public ForgeVector modifyPositionFromSide(Facing side) {
        this.modifyPositionFromSide(side, 1.0D);
        return this;
    }

    @Override
    public ForgeVector clone() {
        return new ForgeVector(x, y, z);
    }

}
