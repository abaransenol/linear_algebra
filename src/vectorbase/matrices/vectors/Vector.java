package vectorbase.matrices.vectors;

import vectorbase.VectorBase;
import vectorbase.matrices.Matrix;

public class Vector extends VectorBase {
    private final double[] vector;

    public Vector(double... elements) {
        if (elements.length == 0) throw new RuntimeException("Vector is empty.");

        this.vector = elements;
    }
    
    public double get(int i) {
        return this.vector[i];
    }
    
    public Matrix toMatrix() {
        return new Matrix(new VectorSet(this));
    }

    public boolean isOrthogonalWith(Vector v) {
        return this.innerProductWith(v) == 0;
    }

    public boolean isOrthonormalWith(Vector v) {
        return this.isOrthogonalWith(v) && norm() == 1;
    }

    public Vector projectionOf(Vector v) {
        double coefficient = this.innerProductWith(v) / v.innerProductWith(v);

        return (Vector) v.scaleWith(coefficient);
    }

    public Vector projectionOf(VectorSet vectorSet) {
        Vector result = this.projectionOf(vectorSet.get(0));

        for (int i = 1; i < vectorSet.getSize(); i++) {
            if (vectorSet.get(i) == null) continue;

            Vector proj = this.projectionOf(vectorSet.get(i));
            result = (Vector) result.add(proj);
        }

        return result;
    }

    @Override
    public VectorBase scaleWith(double c) {
        double[] result = this.vector.clone();

        for (int i = 0; i < this.getDimension(); i++) {
            result[i] *= c;
        }

        return new Vector(result);
    }

    @Override
    public VectorBase add(VectorBase v) {
        if (!(v instanceof Vector v2)) throw new RuntimeException("Not a valid vector.");
        if (this.getDimension() != v2.getDimension()) throw new RuntimeException("Vectors are not in the same dimension.");

        double[] result = this.vector.clone();
        for (int i = 0; i < this.getDimension(); i++) {
            result[i] += this.get(i);
        }

        return new Vector(result);
    }

    @Override
    public double innerProductWith(VectorBase v) {
        if (!(v instanceof Vector v2)) throw new RuntimeException("Not a valid vector.");
        if (this.getDimension() != v2.getDimension()) throw new RuntimeException("Vectors are not in the same dimension.");

        double result = 0;
        for (int i = 0; i < this.getDimension(); i++) {
            result += this.get(i) * v2.get(i);
        }

        return result;
    }

    @Override
    public int getDimension() {
        return this.vector.length;
    }

    @Override
    public String toString() {
        return this.toMatrix().toString();
    }
}
