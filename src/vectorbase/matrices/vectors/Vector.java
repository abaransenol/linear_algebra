package vectorbase.matrices.vectors;

import vectorbase.matrices.Matrix;

import java.util.InvalidPropertiesFormatException;

public class Vector extends Matrix {
    public Vector(double... elements) {
        super(new double[elements.length][1]);

        for (int i = 0; i < elements.length; i++) {
            set(i, 0, elements[i]);
        }
    }

    //TODO: projection to vector ve projection to basis ekle, orthogonal basis bulma işini hallet.

    public Vector(Matrix m) {
        super(m.getMatrix());
    }

    public boolean isOrthogonalWith(Vector v) throws InvalidPropertiesFormatException {
        return this.innerProductWith(v) == 0;
    }

    public boolean isOrthonormalWith(Vector v) throws InvalidPropertiesFormatException {
        return this.isOrthogonalWith(v) && norm() == 1;
    }

    public Vector projectionOf(Vector v) throws InvalidPropertiesFormatException {
        double coefficient = this.innerProductWith(v) / v.innerProductWith(v);

        return new Vector((Matrix) v.scaleWith(coefficient));
    }

    public Vector projectionOf(VectorSet vectorSet) throws InvalidPropertiesFormatException {
        Vector[] vectors = vectorSet.vectors();
        Vector result = this.projectionOf(vectors[0]);

        for (int i = 1; i < vectorSet.getSize(); i++) {
            if (vectors[i] == null) continue;
            Vector proj = this.projectionOf(vectors[i]);
            result = new Vector((Matrix) result.add(proj));
        }

        return result;
    }
}
