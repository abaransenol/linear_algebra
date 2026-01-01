package vectorbase.matrices;

public class AugmentedMatrix {
    private final Matrix m1;
    private final Matrix m2;

    public AugmentedMatrix(Matrix m1, Matrix m2) {
        if (m1.getRowCount() != m2.getRowCount()) throw new RuntimeException("Matrices cannot be augmented.");

        this.m1 = m1;
        this.m2 = m2;
    }

    public AugmentedMatrix getEchelonForm() {
        // Gets row echelon form of the m1, makes same processes to m2.
        int pivotsRow = 0;

        Matrix m1 = this.m1;
        Matrix m2 = this.m2;

        // 'i' is the column index.
        for (int i = 0; i < m1.getColumnCount(); i++) {
            double pivot = m1.get(pivotsRow, i);

            if (pivot == 0) {
                // changing pivot to the non-zero element.
                for (int j = pivotsRow; j < m1.getRowCount(); j++) {
                    if (m1.get(j, i) == 0) continue;

                    m1 = m1.interchangeRows(pivotsRow, j);
                    m2 = m2.interchangeRows(pivotsRow, j);

                    pivot = m1.get(pivotsRow, i);
                    break;
                }

                // if there is no non-zero element, continue
                if (pivot == 0) continue;
            }

            // 'j' is the row index
            for (int j = pivotsRow + 1; j < m1.getRowCount(); j++) {
                if (m1.get(j, i) == 0) continue;

                double el = m1.get(j, i);
                double c = -1 * (el / pivot);

                m1 = m1.addRowTo(pivotsRow, j, c);
                m2 = m2.addRowTo(pivotsRow, j, c);
            }

            pivotsRow++;
            if (pivotsRow == m1.getRowCount() - 1) break;
        }

        return new AugmentedMatrix(m1, m2);
    }

    public AugmentedMatrix getRowReducedEchelonForm() {
        // Gets row reduced echelon form of the m1, makes same processes to m2.
        int pivotsRow = 0;

        AugmentedMatrix echelonForm = this.getEchelonForm();
        Matrix m1 = echelonForm.getMatrix1();
        Matrix m2 = echelonForm.getMatrix2();

        // 'i' is the column index.
        for (int i = 0; i < m1.getColumnCount(); i++) {
            double pivot = m1.get(pivotsRow, i);

            if (pivot == 0) {
                // changing pivot to the non-zero element.
                for (int j = pivotsRow; j < m1.getRowCount(); j++) {
                    if (m1.get(j, i) == 0) continue;

                    m1 = m1.interchangeRows(pivotsRow, j);
                    m2 = m2.interchangeRows(pivotsRow, j);

                    pivot = m1.get(pivotsRow, i);
                    break;
                }

                // if there is no non-zero element, continue
                if (pivot == 0) continue;
            }

            if (pivot != 1) {
                m1 = m1.scaleRowWith(pivotsRow ,1 / pivot);
                m2 = m2.scaleRowWith(pivotsRow ,1 / pivot);

                pivot = m1.get(pivotsRow, i);
            }

            // 'j' is the row index
            for (int j = 0; j < pivotsRow; j++) {
                if (m1.get(j, i) == 0) continue;

                double el = m1.get(j, i);
                double c = -1 * (el / pivot);

                m1 = m1.addRowTo(pivotsRow, j, c);
                m2 = m2.addRowTo(pivotsRow, j, c);
            }

            if (pivotsRow == m1.getRowCount() - 1) break;
            pivotsRow++;
        }

        return new AugmentedMatrix(m1, m2);
    }

    public Matrix getMatrix1() {
        return m1;
    }

    public Matrix getMatrix2() {
        return m2;
    }
}
