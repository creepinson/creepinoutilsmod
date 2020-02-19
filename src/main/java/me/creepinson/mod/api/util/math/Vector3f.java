package me.creepinson.mod.api.util.math;

import net.minecraft.util.math.BlockPos;

public class Vector3f {

    public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
    public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
    public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);
    public static final Vector3f X_AXIS_NEG = new Vector3f(-1, 0, 0);
    public static final Vector3f Y_AXIS_NEG = new Vector3f(0, -1, 0);
    public static final Vector3f Z_AXIS_NEG = new Vector3f(0, 0, -1);


    /**
     * Calculates the scalar-product of the given vectors.
     */
    public static int scalar(Vector3f... vecs) {
        int x = 1, y = 1, z = 1;
        for (Vector3f vec : vecs) {
            x *= vec.x;
            y *= vec.y;
            z *= vec.z;
        }
        return x + y + z;
    }

    /**
     * Calculates the cross-product of the given vectors.
     */
    public static Vector3f cross(Vector3f vec1, Vector3f vec2) {
        return new Vector3f(vec1.y * vec2.z - vec1.z * vec2.y, vec1.z * vec2.x - vec1.x * vec2.z, vec1.x * vec2.y - vec1.y * vec2.x);
    }

    public float x;
    public float y;
    public float z;

    /**
     * Creates a new Vector3 with x, y, z.
     */
    public Vector3f(int x, int y, int z) {
        this((float) x, (float) y, (float) z);
    }

    /**
     * Creates a new Vector3 with x, y, z.
     */
    public Vector3f(double x, double y, double z) {
        this((float) x, (float) y, (float) z);
    }

    /**
     * Creates a new Vector3 with x, y, z.
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 toInt() {
        return new Vector3(this.x, this.y, this.z);
    }

    public BlockPos toBlockPos() {
        return new BlockPos(this.x, this.y, this.z);
    }


    /**
     * Equivalent to Vector3(0, 0, 0).
     */
    public Vector3f() {
        this(0, 0, 0);
    }

    public Vector3f clone() {
        return new Vector3f(this.x, this.y, this.z);
    }


    /**
     * Returns itself not a new Vector.
     *
     * @param other to add
     */
    public Vector3f add(Vector3f other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    /**
     * Returns itself not a new Vector.
     *
     * @param other to subtract
     */
    public Vector3f sub(Vector3f other) {
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
    public Vector3f mul(int factor) {
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
    public Vector3f mul(float factor) {
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
    public Vector3f mul(double factor) {
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        return this;
    }

    /**
     * Returns itself not a new Vector. Equivalent to mul(-1).
     */
    public Vector3f reverse() {
        mul(-1);
        return this;
    }

    /**
     * Returns itself not a new Vector. x-Axis = 0, y-Axis = 1, z-Axis = 2.
     * Params are axis and angle.
     */
    public Vector3f rotate(int axis, int angle) {

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
    public Vector3f normalize() {

        int amt = this.amount();
        if (amt == 0) return this;
        this.x /= amt;
        this.y /= amt;
        this.z /= amt;

        return this;
    }

    public Vector3f copy() {
        return new Vector3f(x, y, z);
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
        Vector3f other = (Vector3f) obj;
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
}