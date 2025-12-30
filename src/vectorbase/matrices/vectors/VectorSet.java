package vectorbase.matrices.vectors;

import vectorbase.matrices.Matrix;

import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;

public record VectorSet(Vector... vectors) {

    public int getSize() {
        return vectors.length;
    }

    public boolean isLinearlyIndependent() {
        return new Matrix(vectors).getNullSpace().getSize() == 0;
    }

    public VectorSet getBasis() {
        Matrix rref = (new Matrix(vectors())).getRowReducedEchelonForm();

        boolean[] pivots = new boolean[rref.getColumnCount()];
        int pivotsCount = 0;
        for (int i = 0; i < rref.getColumnCount(); i++) {
            for (int j = 0; j < rref.getRowCount(); j++) {
                if (rref.get(j, i) == 0) continue;
                if (rref.get(j, i) == 1) {
                    pivots[i] = true;
                    pivotsCount++;
                    continue;
                }

                if (pivots[i] && rref.get(j, i) != 0) {
                    pivots[i] = false;
                    pivotsCount--;
                    break;
                }
            }
        }

        Vector[] basis = new Vector[pivotsCount];
        int basisIndex = 0;
        for (int i = 0; i < pivots.length; i++) {
            if (!(pivots[i])) continue;

            basis[basisIndex] = vectors[i];
            basisIndex++;
        }

        return new VectorSet(basis);
    }

    public VectorSet getOrthogonalBasis() throws InvalidPropertiesFormatException {
        VectorSet basis = getBasis();
        Vector[] vectors = basis.vectors.clone();

        Vector[] orthogonalVectors = new Vector[basis.getSize()];
        orthogonalVectors[0] = vectors[0];

        for (int i = 1; i < vectors.length; i++) {
            Vector proj = vectors[i].projectionOf(new VectorSet(orthogonalVectors));

            orthogonalVectors[i] = new Vector((Matrix) vectors[i].add(proj.scaleWith(-1)));
        }

        return new VectorSet(orthogonalVectors);
    }

    public VectorSet getOrthonormalBasis() throws InvalidPropertiesFormatException {
        VectorSet orthogonalBasis = getOrthogonalBasis();
        Vector[] vectors = orthogonalBasis.vectors;

        Vector[] orthonormalVectors = new Vector[orthogonalBasis.getSize()];
        for (int i = 0; i < orthogonalBasis.getSize(); i++) {
            Vector v = vectors[i];
            Vector orthonormal = new Vector((Matrix) v.scaleWith(1 / v.norm()));

            orthonormalVectors[i] = orthonormal;
        }

        return new VectorSet(orthonormalVectors);
    }

    public VectorSet getStandardBasis() {
        int dimension = vectors[0].getDimension();
        Vector[] basisVectors = new Vector[dimension];

        for (int i = 0; i < dimension; i++) {
            double[] v = new double[dimension];
            v[i] = 1;
            basisVectors[i] = new Vector(v);
        }

        return new VectorSet(basisVectors);
    }

    public String toString() {
        return Arrays.toString(vectors);
    }
}
