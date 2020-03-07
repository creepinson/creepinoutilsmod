package me.creepinson.creepinoutils.api.util.math.interpolation;


import me.creepinson.creepinoutils.api.util.math.Vector3;

public class CubicInterpolation extends Interpolation {

    public Vector3 beginVec;
    public Vector3 endVec;

    public CubicInterpolation(double[] times, Vector3[] points) {
        super(times, points);
        beginVec = (Vector3) points[0].add(points[0].sub(points[1]));
        endVec = (Vector3) points[points.length - 1].add(points[points.length - 1].sub(points[points.length - 2]));
    }

    public CubicInterpolation(Vector3... points) {
        super(points);
        beginVec = (Vector3) points[0].add(points[0].sub(points[1]));
        endVec = (Vector3) points[points.length - 1].add(points[points.length - 1].sub(points[points.length - 2]));
    }

    @Override
    protected double getValue(int index, int dim) {
        if (index < 0)
            return beginVec.getValueByDim(dim);
        if (index >= points.size())
            return endVec.getValueByDim(dim);
        return pointVecs.get(index).getValueByDim(dim);
    }

    @Override
    public float valueAt(double mu, int pointIndex, int pointIndexNext, int dim) {

        double v0 = getValue(pointIndex - 1, dim);
        double v1 = getValue(pointIndex, dim);
        double v2 = getValue(pointIndexNext, dim);
        double v3 = getValue(pointIndexNext + 1, dim);

        double a0, a1, a2, a3, mu2;

        mu2 = mu * mu;
        a0 = v3 - v2 - v0 + v1;
        a1 = v0 - v1 - a0;
        a2 = v2 - v0;
        a3 = v1;

        return (float) (a0 * mu * mu2 + a1 * mu2 + a2 * mu + a3);
    }
}
