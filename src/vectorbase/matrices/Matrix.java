package vectorbase.matrices;

import vectorbase.*;
import vectorbase.matrices.vectors.Vector;

import java.util.InvalidPropertiesFormatException;

public class Matrix extends VectorBase {
    private final double[][] matrix;
    protected final int rowCount;
    protected final int columnCount;

    public double get(int row, int col) {
        return matrix[row][col];
    }

    public double[] getRow(int row) {
        return matrix[row].clone();
    }

    public double[] getColumn(int col) {
        double[] res = new double[columnCount];
        for (int i = 0; i < columnCount; i++) {
            res[i] = matrix[i][col];
        }

        return res;
    }

    public double[][] getMatrix() {
        return matrix.clone();
    }

    public void set(int row, int col, double val) {
        matrix[row][col] = val;
    }

    public Matrix(double[]... matrix) {
        this.matrix = matrix;

        rowCount = matrix.length;
        columnCount = matrix[0].length;
    }

    public boolean isAddable(Matrix m2) {
        return this.rowCount == m2.rowCount && this.columnCount == m2.columnCount;
    }

    public Matrix multiplyWith(Matrix m) {
        if (columnCount != m.rowCount) throw new UnsupportedOperationException("Those vectorbase.matrices cannot be multiplied.");

        double[][] res = new double[rowCount][m.columnCount];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < m.columnCount; j++) {
                for (int k = 0; k < columnCount; k++) {
                    res[i][j] += get(i, k) * m.get(k, j);
                }
            }
        }

        return new Matrix(res);
    }

    public Matrix getMinor(int i, int j) {
        double[][] minor = new double[rowCount - 1][rowCount - 1];

        for (int row = 0; row < rowCount; row++) {
            if (row == i) continue;
            for (int col = 0; col < columnCount; col++) {
                if (col == j) continue;

                int rowIndex = row < i ? row : row - 1;
                int columnIndex = col < j ? col : col - 1;

                minor[rowIndex][columnIndex] = get(row, col);
            }
        }

        return new Matrix(minor);
    }
    
    public Matrix getEchelonForm() {
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
                    break;
                }

                // if there is no non-zero element, continue
                if (pivot == 0) continue;
            }

            // 'j' is the row index
            for (int j = pivotsRow + 1; j < rowCount; j++) {
                if (m.get(j, i) == 0) continue;

                double el = m.get(j, i);
                double c = -1 * (el / pivot);
                m = m.addRowTo(pivotsRow, j, c);
            }

            pivotsRow++;
            if (pivotsRow == rowCount - 1) break;
        }

        return m;
    }

    public Matrix getRowReducedEchelonForm() {
        int pivotsRow = 0;
        Matrix m = getEchelonForm();

        // 'i' is the column index.
        for (int i = 0; i < columnCount; i++) {
            double pivot = m.get(pivotsRow, i);

            if (pivot == 0) {
                // changing pivot to the non-zero element.
                for (int j = pivotsRow; j < rowCount; j++) {
                    if (m.get(j, i) == 0) continue;

                    m = m.interchangeRows(pivotsRow, j);
                    pivot = m.get(pivotsRow, i);
                    break;
                }

                // if there is no non-zero element, continue
                if (pivot == 0) continue;
            }

            if (pivot != 1) {
                m = m.scaleRowWith(pivotsRow ,1 / pivot);
                pivot = m.get(pivotsRow, i);
            }

            // 'j' is the row index
            for (int j = 0; j < pivotsRow; j++) {
                if (m.get(j, i) == 0) continue;

                double el = m.get(j, i);
                double c = -1 * (el / pivot);
                m = m.addRowTo(pivotsRow, j, c);
            }

            if (pivotsRow == rowCount - 1) break;
            pivotsRow++;
        }

        return m;
    }

    public Matrix scaleRowWith(int r, double c) {
        double[] row = getRow(r);
        
        for (int i = 0; i < row.length; i++) {
            row[i] *= c;
        }
        
        double[][] res = this.matrix.clone();
        res[r] = row;
        
        return new Matrix(res);
    }

    public Matrix interchangeRows(int r1, int r2) {
        double[][] res = this.matrix.clone();

        res[r1] = getRow(r2);
        res[r2] = getRow(r1);

        return new Matrix(res);
    }

    public Matrix addRowTo(int r1, int r2, double c) {
        double[][] res = this.matrix.clone();
        
        double[] row1 = getRow(r1);
        double[] row2 = getRow(r2);
        for (int i = 0; i < row1.length; i++) {
            row2[i] += c * row1[i];
        }

        res[r2] = row2;
        return new Matrix(res);
    }

    public Vector convertToVector() {
        double[] res = new double[rowCount * columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                int pos = i * (rowCount + 1) + j;
                res[pos] = get(i, j);
            }
        }

        return new Vector(res);
    }

    public Matrix transpose() {
        double[][] res = new double[columnCount][rowCount];
        for (int i = 0; i < columnCount; i++) {
            res[i] = getColumn(i);
        }

        return new Matrix(res);
    }

    public Vector[] getColumnSpace() {
        Vector[] columnSpace = new Vector[columnCount];

        for (int i = 0; i < columnCount; i++) {
            double[] v = new double[rowCount];

            for (int j = 0; j < rowCount; j++) {
                v[j] = get(j, i);
            }

            columnSpace[i] = new Vector(v);
        }

        return columnSpace;
    }

    public Vector[] getNullSpace() {
        Matrix rref = getRowReducedEchelonForm();

        boolean[] pivots = new boolean[rref.columnCount];
        int pivotsCount = 0;
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < rowCount; j++) {
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

        Vector[] nullSpace = new Vector[pivots.length - pivotsCount];
        int vectorIndex = 0;

        for (int i = 0; i < columnCount; i++) {
            if (pivots[i]) continue;
            double[] vector = new double[columnCount];

            for (int j = 0; j < columnCount; j++) {
                if (!pivots[j] && i == j) {
                    vector[j] = 1;
                    continue;
                }
                if (!pivots[j] && i != j) {
                    vector[j] = 0;
                    continue;
                }

                vector[j] = -1 * rref.get(j, i);
            }

            nullSpace[vectorIndex] = new Vector(vector);
            vectorIndex++;
        }

        return nullSpace;
    }

    public Matrix[] getLUFactorization() {
        IdentityMatrix identityMatrix = new IdentityMatrix(rowCount);

        AugmentedMatrix augmentedMatrix = new AugmentedMatrix(this, identityMatrix);
        AugmentedMatrix ref = (AugmentedMatrix) (augmentedMatrix.getEchelonForm());

        Matrix u = ref.getMatrix1();
        SquareMatrix l = (new SquareMatrix(ref.getMatrix2())).inverse();

        return new Matrix[] {u, l};
    }

    @Override
    public VectorBase scaleWith(double c) {
        double[][] resultMatrix = new double[rowCount][columnCount];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                resultMatrix[i][j] = c * matrix[i][j];
            }
        }

        return new Matrix(resultMatrix);
    }

    @Override
    public VectorBase add(VectorBase v) throws InvalidPropertiesFormatException {
        if (!(v instanceof Matrix m2)) throw new InvalidPropertiesFormatException("Not a valid matrix.");
        if (!isAddable(m2)) throw new InvalidPropertiesFormatException("Matrices are not in the same dimension.");
        
        double[][] resultMatrix = new double[rowCount][columnCount];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                resultMatrix[i][j] = matrix[i][j] + m2.matrix[i][j];
            }
        }
        
        return new Matrix(resultMatrix);
    }

    @Override
    public double innerProductWith(VectorBase v) throws InvalidPropertiesFormatException {
        if (!(v instanceof Matrix m) ||
            m.rowCount != rowCount ||
            m.columnCount != columnCount
        ) throw new InvalidPropertiesFormatException("Not a valid matrix.");

        Matrix production = transpose().multiplyWith(m);
        double result = 0;

        for (int i = 0; i < production.rowCount; i++) {
            for (int j = 0; j < production.columnCount; j++) {
                result += production.matrix[i][j];
            }
        }

        return result;
    }

    @Override
    public int getDimension() {
        return rowCount * columnCount;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("\n");
        for (int i = 0; i < rowCount; i++) {
            s.append("[");
            for (int j = 0; j < columnCount; j++) {
                s.append(" ").append(matrix[i][j]).append(" ");
            }
            s.append("]\n");
        }

        return s.toString();
    }
}
