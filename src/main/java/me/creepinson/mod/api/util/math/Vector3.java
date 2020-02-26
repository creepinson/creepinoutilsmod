package me.creepinson.mod.api.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class Vector3 {

    public static final Vector3 X_AXIS = new Vector3(1, 0, 0);
    public static final Vector3 Y_AXIS = new Vector3(0, 1, 0);
    public static final Vector3 Z_AXIS = new Vector3(0, 0, 1);
    public static final Vector3 X_AXIS_NEG = new Vector3(-1, 0, 0);
    public static final Vector3 Y_AXIS_NEG = new Vector3(0, -1, 0);
    public static final Vector3 Z_AXIS_NEG = new Vector3(0, 0, -1);


    /**
     * Offset this Vector 1 block in the given direction
     */
    public Vector3 offset(EnumFacing facing) {
        return this.offset(facing, 1);
    }


    /**
     * Offsets this Vector n blocks in the given direction
     */
    public Vector3 offset(EnumFacing facing, int n) {
        return n == 0 ? this : new Vector3(this.x + facing.getXOffset() * n, this.y + facing.getYOffset() * n, this.z + facing.getZOffset() * n);
    }


    /**
     * Calculates the scalar-product of the given vectors.
     */
    public static int scalar(Vector3... vecs) {
        int x = 1, y = 1, z = 1;
        for (Vector3 vec : vecs) {
            x *= vec.x;
            y *= vec.y;
            z *= vec.z;
        }
        return x + y + z;
    }

    /**
     * Calculates the cross-product of the given vectors.
     */
    public static Vector3 cross(Vector3 vec1, Vector3 vec2) {
        return new Vector3(vec1.y * vec2.z - vec1.z * vec2.y, vec1.z * vec2.x - vec1.x * vec2.z, vec1.x * vec2.y - vec1.y * vec2.x);
    }

    public float x;
    public float y;
    public float z;

    /**
     * Creates a new Vector3 with x, y, z.
     */
    public Vector3(int x, int y, int z) {
        this((float) x, (float) y, (float) z);
    }

    /**
     * Creates a new Vector3 with x, y, z.
     */
    public Vector3(double x, double y, double z) {
        this((float) x, (float) y, (float) z);
    }

    /**
     * Creates a new Vector3 with x, y, z.
     */
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int intX() {
        return (int) this.x;
    }

    public int intY() {
        return (int) this.y;
    }

    public int intZ() {
        return (int) this.z;
    }

    public BlockPos toBlockPos() {
        return new BlockPos(this.x, this.y, this.z);
    }

    public Vector3(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Equivalent to Vector3(0, 0, 0).
     */
    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3 clone() {
        return new Vector3(this.x, this.y, this.z);
    }


    /**
     * Returns itself not a new Vector.
     *
     * @param other to add
     */
    public Vector3 add(Vector3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    public Vector3 add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }


    /**
     * Returns itself not a new Vector.
     *
     * @param other to subtract
     */
    public Vector3 sub(Vector3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    /**
     * Returns itself not a new Vector.
     *
     * @param factor to multiply with
     */
    public Vector3 mul(int factor) {
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        return this;
    }

    /**
     * Returns itself not a new Vector.
     *
     * @param factor to multiply with
     */
    public Vector3 mul(float factor) {
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        return this;
    }

    /**
     * Returns itself not a new Vector.
     *
     * @param factor to multiply with
     */
    public Vector3 mul(double factor) {
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        return this;
    }

    /**
     * Returns itself not a new Vector. Equivalent to mul(-1).
     */
    public Vector3 reverse() {
        mul(-1);
        return this;
    }

    /**
     * Returns itself not a new Vector. x-Axis = 0, y-Axis = 1, z-Axis = 2.
     * Params are axis and angle.
     */
    public Vector3 rotate(int axis, int angle) {

        double a = Math.toRadians(angle);

        if (axis == 0) {
            this.y = (int) (y * Math.cos(a) + z * -Math.sin(a));
            this.z = (int) (y * Math.sin(a) + z * Math.cos(a));
        } else if (axis == 1) {
            //System.out.println("Applying rotation for y-Axis with angle " + angle);
            //System.out.println("radiant angle " + a);
            //System.out.println("Cos a = " + Math.cos(a));
            this.x = (int) (x * Math.cos(a) + z * -Math.sin(a));
            this.z = (int) (x * Math.sin(a) + z * Math.cos(a));
        } else if (axis == 2) {
            this.x = (int) (x * Math.cos(a) + y * -Math.sin(a));
            this.y = (int) (x * Math.sin(a) + y * Math.cos(a));
        }

        return this;
    }

    /**
     * Returns the length of the Vector.
     */
    public int amount() {
        return (int) Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Returns itself not a new Vector. Normalizes the vector to length 1.
     * Note that this vector uses int values for coordinates.
     */
    public Vector3 normalize() {

        int amt = this.amount();
        if (amt == 0) return this;
        this.x /= amt;
        this.y /= amt;
        this.z /= amt;

        return this;
    }

    public Vector3 copy() {
        return new Vector3(x, y, z);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) x;
        result = prime * result + (int) y;
        result = prime * result + (int) z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector3 other = (Vector3) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        return true;
    }

    public String toString() {
        return "Vector(" + x + ", " + y + ", " + z + ")";
    }

    public javax.vecmath.Vector3f toJava() {
        return new javax.vecmath.Vector3f(this.x, this.y, this.z);
    }

    public float distanceTo(Vector3 vector3) {
        float var2 = vector3.x - this.x;
        float var4 = vector3.y - this.y;
        float var6 = vector3.z - this.z;
        return (float) Math.sqrt(var2 * var2 + var4 * var4 + var6 * var6);
    }

    /**
     * Gets all entities inside of this position in block space.
     */
    public List<Entity> getEntitiesWithin(World worldObj, Class<? extends Entity> par1Class) {
        return worldObj.getEntitiesWithinAABB(par1Class, new AxisAlignedBB(this.intX(), this.intY(), this.intZ(), this.intX() + 1, this.intY() + 1, this.intZ() + 1));
    }

    /**
     * Gets a position relative to a position's side
     *
     * @param side - The side. 0-5
     * @return The position relative to the original position's side
     */
    public Vector3 modifyPositionFromSide(EnumFacing side, double amount) {
        switch (side.ordinal()) {
            case 0:
                this.y -= amount;
                break;
            case 1:
                this.y += amount;
                break;
            case 2:
                this.z -= amount;
                break;
            case 3:
                this.z += amount;
                break;
            case 4:
                this.x -= amount;
                break;
            case 5:
                this.x += amount;
                break;
        }
        return this;
    }

    public Vector3 modifyPositionFromSide(EnumFacing side) {
        this.modifyPositionFromSide(side, 1);
        return this;
    }

    /**
     * Loads a Vector3 from an NBT compound.
     */
    public static Vector3 readFromNBT(NBTTagCompound nbtCompound) {
        Vector3 tempVector = new Vector3();
        tempVector.x = nbtCompound.getFloat("x");
        tempVector.y = nbtCompound.getFloat("y");
        tempVector.z = nbtCompound.getFloat("z");
        return tempVector;
    }

    /**
     * Saves this Vector3 to disk
     *
     * @param par1NBTTagCompound - The NBT compound object to save the data in
     */
    public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
        par1NBTTagCompound.setFloat("x", this.x);
        par1NBTTagCompound.setFloat("y", this.y);
        par1NBTTagCompound.setFloat("z", this.z);
        return par1NBTTagCompound;
    }

    /**
     * Gets the TileEntity of the block representing this Vector.
     *
     * @param world - world this Vector is in
     * @return the TileEntity of this Vector's block
     */
    public TileEntity getTileEntity(IBlockAccess world) {
        return world.getTileEntity(toBlockPos());
    }

    public boolean intersects(Vector3 other) {
        return this.equals(other);
    }
}