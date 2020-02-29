package me.creepinson.creepinoutils.api.util.math;

import mekanism.api.TileNetworkList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

/**
 * Coord4D - an integer-based way to keep track of and perform operations on blocks in a Minecraft-based environment. This also takes in account the dimension the
 * coordinate is in.
 *
 * @author aidancbrady
 */
public class Coord4D {

    public Vector3 vector;
    public int dimensionId;

    /**
     * Creates a Coord4D from an entity's position, rounded down.
     *
     * @param entity - entity to create the Coord4D from
     */
    public Coord4D(Entity entity) {
        this.vector = new Vector3(entity.posX, entity.posY, entity.posZ);
        this.dimensionId = entity.world.provider.getDimension();
    }

    /**
     * Creates a Coord4D from the defined x, y, z, and dimension values.
     *
     */
    public Coord4D(Vector3 vec, int d) {
        this.vector = vec;
        this.dimensionId = d;
    }

    /**
     * Creates a Coord4D from the defined x, y, z, and dimension values.
     *
     */
    public Coord4D(Vector3 vec, World world) {
        this.vector = vec;
        this.dimensionId = world.provider.getDimension();
    }

    public Coord4D(BlockPos pos, World world) {
        this(new Vector3(pos), world);
    }

    public Coord4D(RayTraceResult mop, World world) {
        this(mop.getBlockPos(), world);
    }

    /**
     * Returns a new Coord4D from a defined TileEntity's x, y and z values.
     *
     * @param tileEntity - TileEntity at the location that will represent this Coord4D
     * @return the Coord4D object from the TileEntity
     */
    public static Coord4D get(TileEntity tileEntity) {
        return new Coord4D(tileEntity.getPos(), tileEntity.getWorld());
    }

    /**
     * Returns a new Coord4D from a tag compound.
     *
     * @param tag - tag compound to read from
     * @return the Coord4D from the tag compound
     */
    public static Coord4D read(NBTTagCompound tag) {
        return new Coord4D(Vector3.readFromNBT(tag.getCompoundTag("vector")), tag.getInteger("id"));
    }

    /**
     * Gets the state of the block representing this Coord4D.
     *
     * @param world - world this Coord4D is in
     * @return the state of this Coord4D's block
     */
    public IBlockState getBlockState(IBlockAccess world) {
        return world.getBlockState(getPos());
    }

    public int getBlockMeta(IBlockAccess world) {
        IBlockState state = getBlockState(world);
        return state == null ? 0 : state.getBlock().getMetaFromState(state);
    }

    public BlockPos getPos() {
        return vector.toBlockPos();
    }



    /**
     * Gets the Block value of the block representing this Coord4D.
     *
     * @param world - world this Coord4D is in
     * @return the Block value of this Coord4D's block
     */
    public Block getBlock(IBlockAccess world) {
        if (world instanceof World && !exists((World) world)) {
            return null;
        }
        return getBlockState(world).getBlock();
    }

    /**
     * Writes this Coord4D's data to an NBTTagCompound.
     *
     * @param nbtTags - tag compound to write to
     * @return the tag compound with this Coord4D's data
     */
    public NBTTagCompound write(NBTTagCompound nbtTags) {
        nbtTags.setTag("vector", vector.writeToNBT(new NBTTagCompound()));
        nbtTags.setInteger("dimensionId", dimensionId);
        return nbtTags;
    }

    /**
     * Writes this Coord4D's data to an TileNetworkList for packet transfer.
     *
     * @param data - the TileNetworkList to add the data to
     */
    public void write(TileNetworkList data) {
        data.add(vector);
        data.add(dimensionId);
    }

    /**
     * Translates this Coord4D by the defined x, y, and z values.
     *
     * @param x - x value to translate
     * @param y - y value to translate
     * @param z - z value to translate
     * @return translated Coord4D
     */
    public Coord4D translate(int x, int y, int z) {
        return new Coord4D(this.vector.add(x, y, z), dimensionId);
    }

    /**
     * Creates and returns a new Coord4D translated to the defined offsets of the side.
     *
     * @param side - side to translate this Coord4D to
     * @return translated Coord4D
     */
    public Coord4D offset(EnumFacing side) {
        return offset(side, 1);
    }

    /**
     * Creates and returns a new Coord4D translated to the defined offsets of the side by the defined amount.
     *
     * @param side   - side to translate this Coord4D to
     * @param amount - how far to translate this Coord4D
     * @return translated Coord4D
     */
    public Coord4D offset(EnumFacing side, int amount) {
        if (side == null || amount == 0) {
            return this;
        }
        return new Coord4D(vector.add(side.getXOffset() * amount, side.getYOffset() * amount, side.getZOffset() * amount), dimensionId);
    }

    public ItemStack getStack(IBlockAccess world) {
        IBlockState state = getBlockState(world);
        if (state == null || state.getBlock().isAir(state, world, null)) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
    }

    /**
     * Gets a TargetPoint with the defined range from this Coord4D with the appropriate coordinates and dimension ID.
     *
     * @param range - the range the packet can be sent in of this Coord4D
     * @return TargetPoint relative to this Coord4D
     */
    public TargetPoint getTargetPoint(double range) {
        return new TargetPoint(dimensionId, vector.x, vector.y, vector.z, range);
    }

    /**
     * Whether or not the chunk this Coord4D is in exists and is loaded.
     *
     * @param world - world this Coord4D is in
     * @return the chunk of this Coord4D
     */
    public boolean exists(World world) {
        return world.isBlockLoaded(vector.toBlockPos());//world.getChunkProvider() == null || world.getChunkProvider().getLoadedChunk(x >> 4, z >> 4) != null;
    }

    /**
     * Gets the chunk this Coord4D is in.
     *
     * @param world - world this Coord4D is in
     * @return the chunk of this Coord4D
     */
    public Chunk getChunk(World world) {
        return world.getChunk(getPos());
    }

    /**
     * Gets the Chunk3D object with chunk coordinates correlating to this Coord4D's location
     *
     * @return Chunk3D with correlating chunk coordinates.
     */
    public Chunk3D getChunk3D() {
        return new Chunk3D(this);
    }

    /**
     * Whether or not the block this Coord4D represents is an air block.
     *
     * @param world - world this Coord4D is in
     * @return if this Coord4D is an air block
     */
    public boolean isAirBlock(IBlockAccess world) {
        return world.isAirBlock(getPos());
    }

    /**
     * Whether or not this block this Coord4D represents is replaceable.
     *
     * @param world - world this Coord4D is in
     * @return if this Coord4D is replaceable
     */
    public boolean isReplaceable(World world) {
        return getBlock(world).isReplaceable(world, getPos());
    }

    /**
     * Gets a bounding box that contains the area this Coord4D would take up in a world.
     *
     * @return this Coord4D's bounding box
     */
    public AxisAlignedBB getBoundingBox() {
        return new AxisAlignedBB(vector.x, vector.y, vector.z, vector.x + 1, vector.y + 1, vector.z + 1);
    }

    @Override
    public Coord4D clone() {
        return new Coord4D(vector, dimensionId);
    }

    @Override
    public String toString() {
        return "[Coord4D: " + vector.x + ", " + vector.y + ", " + vector.z + ", dim=" + dimensionId + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Coord4D && ((Coord4D) obj).vector.equals(vector) && ((Coord4D) obj).dimensionId == dimensionId;
    }
}