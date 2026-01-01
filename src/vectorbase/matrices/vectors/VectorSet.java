package vectorbase.matrices.vectors;

import vectorbase.matrices.IdentityMatrix;
import vectorbase.matrices.Matrix;
import vectorbase.matrices.Solution;
import vectorbase.matrices.SquareMatrix;

import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;

public class VectorSet {
    private final Vector[] vectors;

    public VectorSet(Vector... vectors) {
        if (vectors.length == 0) throw new RuntimeException("Vector set is empty.");

        this.vectors = vectors;
    }

    public Vector get(int i) { return vectors[i]; }

    public int getSize() {
        return this.vectors.length;
    }

    public int getDimension() {
        return this.vectors[0].getDimension();
    }
    
    public Matrix toMatrix() {
        if (this.getDimension() == this.getSize()) return (new Matrix(this)).toSquareMatrix();
        return new Matrix(this);
    }

    public boolean isLinearlyIndependent() {
        return this.toMatrix().getNullSpace().getSize() == 0;
    }

    public VectorSet getBasis() {
        Matrix rref = this.toMatrix().getRowReducedEchelonForm();

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

            basis[basisIndex] = this.get(i);
            basisIndex++;
        }

        return new VectorSet(basis);
    }

    public VectorSet getStandardBasis() {
        VectorSet basis = this.getBasis();

        return (new IdentityMatrix(basis.getDimension())).toVectorSet();
    }

    public VectorSet getOrthogonalBasis() {
        VectorSet basis = getBasis();

        Vector[] orthogonalVectors = new Vector[basis.getSize()];
        orthogonalVectors[0] = basis.get(0);

        for (int i = 1; i < basis.getSize(); i++) {
            Vector v = basis.get(i);
            Vector proj = v.projectionOf(new VectorSet(orthogonalVectors));

            orthogonalVectors[i] = (Vector) v.add(proj.scaleWith(-1));
        }

        return new VectorSet(orthogonalVectors);
    }

    public VectorSet getOrthonormalBasis() {
        VectorSet orthogonalBasis = getOrthogonalBasis();

        Vector[] orthonormalVectors = new Vector[orthogonalBasis.getSize()];
        for (int i = 0; i < orthogonalBasis.getSize(); i++) {
            Vector v = orthogonalBasis.get(i);
            Vector orthonormal = (Vector) v.scaleWith(1 / v.norm());

            orthonormalVectors[i] = orthonormal;
        }

        return new VectorSet(orthonormalVectors);
    }

    public Vector getRelativeCoordinatesOf(Vector v) {
        SquareMatrix changeOfCoordinatesMatrix = (SquareMatrix) this.toMatrix();

        Solution solution = changeOfCoordinatesMatrix.solve(v);

        return solution.solutionVector();
    }

    public Vector getRelativeCoordinatesOf(Vector v, VectorSet otherSystem) {
        // B is this, C is otherSystem.
        SquareMatrix changeOfCoordinatesMatrixB = (SquareMatrix) this.toMatrix();
        SquareMatrix changeOfCoordinatesMatrixC = (SquareMatrix) otherSystem.toMatrix();

        Matrix changeOfCoordinatesMatrixCToB = changeOfCoordinatesMatrixB.inverse().multiplyWith(changeOfCoordinatesMatrixC);

        Solution solution = changeOfCoordinatesMatrixCToB.solve(v);

        return solution.solutionVector();
    }

    public String toString() {
        return Arrays.toString(this.vectors);
    }
}
