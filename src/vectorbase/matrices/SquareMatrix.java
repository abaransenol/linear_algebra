package vectorbase.matrices;

import vectorbase.matrices.vectors.Vector;

public class SquareMatrix extends Matrix {
    public SquareMatrix(double[]... elements) {
        super(elements);
    }

    public SquareMatrix(Matrix m) {
        super(m.getMatrix());
    }

    public SquareMatrix(Vector[] vectors) {
        super(vectors);
    }

    public double determinantOriginal() {
        if (rowCount == 1) return get(0, 0);

        double determinant = 0;
        int expansionRow = 0;

        for (int i = 0; i < rowCount; i++) {
            double coefficient = get(expansionRow, i);
            SquareMatrix minor = new SquareMatrix(getMinor(expansionRow, i));

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
        for (int i = 0; i < columnCount; i++) {
            double pivot = m.get(pivotsRow, i);

            if (pivot == 0) {
                // changing pivot to the non-zero element.
                for (int j = pivotsRow; j < rowCount; j++) {
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
            for (int j = i + 1; j < rowCount; j++) {
                if (m.get(j, i) == 0) continue;

                double el = m.get(j, i);
                double c = -1 * (el / pivot);
                m = m.addRowTo(pivotsRow, j, c);
            }

            pivotsRow++;
            if (pivotsRow == rowCount - 1) break;
        }

        for (int i = 0; i < rowCount; i++) {
            determinant *= m.get(i, i);
        }

        return determinant;
    }

    public double determinantFromEchelon(double coefficient) {
        for (int i = 0; i < rowCount; i++) {
            coefficient *= get(i, i);
        }

        return coefficient;
    }

    public double determinant() { return determinantFast(); }

    public SquareMatrix inverse() {
        IdentityMatrix identity = new IdentityMatrix(rowCount);
        AugmentedMatrix augmented = new AugmentedMatrix(this, identity);
        AugmentedMatrix rrefAugmented = (AugmentedMatrix)(augmented.getRowReducedEchelonForm());

        SquareMatrix rref = new SquareMatrix(rrefAugmented.getMatrix1());
        SquareMatrix inverse = new SquareMatrix(rrefAugmented.getMatrix2());

        double absoluteDeterminant = rref.determinantFromEchelon(1);
        if (absoluteDeterminant == 0) throw new UnsupportedOperationException("Matrix is not invertible.");

        return inverse;
    }

}
