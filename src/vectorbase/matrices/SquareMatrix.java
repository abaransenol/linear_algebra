package vectorbase.matrices;

import vectorbase.matrices.vectors.Vector;
import vectorbase.matrices.vectors.VectorSet;

public class SquareMatrix extends Matrix {
    public SquareMatrix(double[]... elements) {
        super(elements);
    }

    public SquareMatrix(VectorSet vectorSet) {
        super(vectorSet);
    }

    public double determinantOriginal() {
        if (this.getRowCount() == 1) return get(0, 0);

        double determinant = 0;
        int expansionRow = 0;

        for (int i = 0; i < this.getRowCount(); i++) {
            double coefficient = get(expansionRow, i);
            SquareMatrix minor = getMinor(expansionRow, i).toSquareMatrix();

            double cofactor = Math.pow(-1, expansionRow + i) * minor.determinantOriginal();

            determinant += cofactor * coefficient;
        }

        return determinant;
    }

    public double determinantFast() {
        double determinant = 1;

        int pivotsRow = 0;
        Matrix m = this;

        // 'i' is the column index.
        for (int i = 0; i < this.getColumnCount(); i++) {
            double pivot = m.get(pivotsRow, i);

            if (pivot == 0) {
                // changing pivot to the non-zero element.
                for (int j = pivotsRow; j < this.getRowCount(); j++) {
                    if (m.get(j, i) == 0) continue;

                    m = m.interchangeRows(pivotsRow, j);
                    pivot = m.get(pivotsRow, i);

                    determinant *= -1;
                    break;
                }

                // if there is no non-zero element, continue
                if (pivot == 0) continue;
            }

            // 'j' is the row index
            for (int j = i + 1; j < this.getRowCount(); j++) {
                if (m.get(j, i) == 0) continue;

                double el = m.get(j, i);
                double c = -1 * (el / pivot);
                m = m.addRowTo(pivotsRow, j, c);
            }

            pivotsRow++;
            if (pivotsRow == this.getRowCount() - 1) break;
        }

        for (int i = 0; i < this.getRowCount(); i++) {
            determinant *= m.get(i, i);
        }

        return determinant;
    }

    public double determinant() { return determinantFast(); }

    public SquareMatrix inverse() {
        IdentityMatrix identity = new IdentityMatrix(this.getRowCount());
        AugmentedMatrix augmented = new AugmentedMatrix(this, identity);

        AugmentedMatrix rrefAugmented = augmented.getRowReducedEchelonForm();

        SquareMatrix rref = rrefAugmented.getMatrix1().toSquareMatrix();
        SquareMatrix inverse = rrefAugmented.getMatrix2().toSquareMatrix();

        if (!(rref.isIdentityMatrix())) throw new RuntimeException("Matrix is not invertible.");

        return inverse;
    }

    public double getTrace() {
        double result = 0;
        for (int i = 0; i < this.getRowCount(); i++) {
            result += this.get(i, i);
        }

        return result;
    }

    public boolean isIdentityMatrix() {
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                if (i == j && this.get(i, j) == 1) continue;
                if (i != j && this.get(i, j) == 0) continue;

                return false;
            }
        }

        return true;
    }

    public IdentityMatrix toIdentityMatrix() {
        if (this.isIdentityMatrix()) return new IdentityMatrix(this.getRowCount());

        throw new RuntimeException("This is not an identity matrix.");
    }
}
