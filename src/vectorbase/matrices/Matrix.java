package vectorbase.matrices;

import vectorbase.*;
import vectorbase.matrices.vectors.Vector;
import vectorbase.matrices.vectors.VectorSet;

import java.util.Arrays;

public class Matrix extends VectorBase {
    private final double[][] matrix;

    public Matrix(double[]... matrix) {
        if (matrix.length == 0) throw new RuntimeException("Matrix is empty.");

        int columnCount = matrix[0].length;
        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i].length != columnCount) throw new RuntimeException(Arrays.toString(matrix) + " is not a valid matrix.");
        }

        this.matrix = deepCopyMatrix(matrix);
    }

    public Matrix(VectorSet columnVectors) {
        if (columnVectors.getSize() == 0) throw new RuntimeException("Matrix is empty.");

        int rowCount = columnVectors.get(0).getDimension();
        int columnCount = columnVectors.getSize();

        double[][] matrix = new double[rowCount][columnCount];

        for (int i = 0; i < columnCount; i++) {
            Vector v = columnVectors.get(i);

            for (int j = 0; j < rowCount; j++) {
                matrix[j][i] = v.get(j);
            }
        }

        this.matrix = matrix;
    }

    private double[][] deepCopyMatrix(double[][] m) {
        double[][] matrix = new double[m.length][m[0].length];

        for (int i = 0; i < m.length; i++) {
            matrix[i] = m[i].clone();
        }

        return matrix;
    }

    public int getRowCount() {
        return this.matrix.length;
    }

    public int getColumnCount() {
        if (this.getRowCount() == 0) return 0;
        return this.matrix[0].length;
    }

    public double get(int row, int col) {
        return this.matrix[row][col];
    }

    public double[] getRow(int row) {
        return this.matrix[row].clone();
    }

    public double[] getColumn(int col) {
        double[] column = new double[this.getRowCount()];
        for (int i = 0; i < this.getRowCount(); i++) {
            column[i] = matrix[i][col];
        }

        return column;
    }

    public Solution solve(Vector v) {
        AugmentedMatrix augmentedMatrix = new AugmentedMatrix(this, v.toMatrix());
        AugmentedMatrix rref = augmentedMatrix.getRowReducedEchelonForm();

        Matrix matrix = rref.getMatrix1();
        Vector solution = rref.getMatrix2().toVector();

        if (
            solution.get(solution.getDimension() - 1) != 0 &&
            matrix.get(matrix.getRowCount() - 1, matrix.getColumnCount() - 1) == 0
        ) {
            return new Solution(null, null);
        }

        if (matrix.getColumnCount() > matrix.getRowCount()) {
            VectorSet nullSpace = matrix.getNullSpace();
            return new Solution(solution, nullSpace);
        }

        return new Solution(solution, null);
    }

    public Matrix multiplyWith(Matrix m) {
        if (this.getColumnCount() != m.getColumnCount()) throw new RuntimeException("Those matrices cannot be multiplied.");

        double[][] res = new double[this.getRowCount()][m.getColumnCount()];

        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < m.getColumnCount(); j++) {
                for (int k = 0; k < this.getColumnCount(); k++) {
                    res[i][j] += get(i, k) * m.get(k, j);
                }
            }
        }

        if (res.length == res[0].length) return new SquareMatrix(res);

        return new Matrix(res);
    }

    public Matrix getMinor(int i, int j) {
        double[][] minor = new double[this.getRowCount() - 1][this.getColumnCount() - 1];

        for (int row = 0; row < this.getRowCount(); row++) {
            if (row == i) continue;
            for (int col = 0; col < this.getColumnCount(); col++) {
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
        for (int i = 0; i < m.getColumnCount(); i++) {
            double pivot = m.get(pivotsRow, i);

            if (pivot == 0) {
                // changing pivot to the non-zero element.
                for (int j = pivotsRow; j < m.getRowCount(); j++) {
                    if (m.get(j, i) == 0) continue;

                    m = m.interchangeRows(pivotsRow, j);
                    pivot = m.get(pivotsRow, i);
                    break;
                }

                // if there is no non-zero element, continue
                if (pivot == 0) continue;
            }

            // 'j' is the row index
            for (int j = pivotsRow + 1; j < m.getRowCount(); j++) {
                if (m.get(j, i) == 0) continue;

                double el = m.get(j, i);
                double c = -1 * (el / pivot);
                m = m.addRowTo(pivotsRow, j, c);
            }

            pivotsRow++;
            if (pivotsRow == m.getRowCount() - 1) break;
        }

        return m;
    }

    public Matrix getRowReducedEchelonForm() {
        int pivotsRow = 0;
        Matrix m = getEchelonForm();

        // 'i' is the column index.
        for (int i = 0; i < m.getColumnCount(); i++) {
            double pivot = m.get(pivotsRow, i);

            if (pivot == 0) {
                // changing pivot to the non-zero element.
                for (int j = pivotsRow; j < m.getRowCount(); j++) {
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

            if (pivotsRow == m.getRowCount() - 1) break;
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

    public Vector toVector() {
        double[] res = new double[this.getRowCount() * this.getColumnCount()];
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                int pos = i * this.getColumnCount() + j;
                res[pos] = this.get(i, j);
            }
        }

        return new Vector(res);
    }

    public VectorSet toVectorSet() {
        Vector[] vectors = new Vector[this.getColumnCount()];

        for (int i = 0; i < this.getColumnCount(); i++) {
            vectors[i] = new Vector(this.getColumn(i));
        }

        return new VectorSet(vectors);
    }

    public Matrix getTranspose() {
        double[][] res = new double[this.getColumnCount()][this.getRowCount()];
        for (int i = 0; i < this.getColumnCount(); i++) {
            res[i] = getColumn(i);
        }

        return new Matrix(res);
    }

    public VectorSet getColumnSpace() {
        Vector[] columnSpace = new Vector[this.getColumnCount()];

        for (int i = 0; i < this.getColumnCount(); i++) {
            double[] v = new double[this.getRowCount()];

            for (int j = 0; j < this.getRowCount(); j++) {
                v[j] = get(j, i);
            }

            columnSpace[i] = new Vector(v);
        }

        return new VectorSet(columnSpace);
    }

    public VectorSet getNullSpace() {
        Matrix rref = this.getRowReducedEchelonForm();

        boolean[] pivots = new boolean[rref.getColumnCount()];
        int pivotsCount = 0;
        for (int i = 0; i < this.getColumnCount(); i++) {
            for (int j = pivotsCount; j < this.getRowCount(); j++) {
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

        for (int i = 0; i < this.getColumnCount(); i++) {
            if (pivots[i]) continue;

            double[] vector = new double[this.getColumnCount()];

            int rowIndex = 0;
            for (int j = 0; j < this.getColumnCount(); j++) {
                if (!pivots[j]) {
                    if (i == j) vector[j] = 1;
                    else vector[j] = 0;
                    continue;
                }

                vector[j] = -1 * rref.get(rowIndex, i);
                rowIndex++;
            }

            nullSpace[vectorIndex] = new Vector(vector);
            vectorIndex++;
        }

        return new VectorSet(nullSpace);
    }

    public Matrix[] getLUFactorization() {
        IdentityMatrix identityMatrix = new IdentityMatrix(this.getRowCount());

        AugmentedMatrix augmentedMatrix = new AugmentedMatrix(this, identityMatrix);
        AugmentedMatrix ref = augmentedMatrix.getEchelonForm();

        Matrix u = ref.getMatrix1();
        SquareMatrix l = ref.getMatrix2().toSquareMatrix().inverse();

        return new Matrix[] {u, l};
    }

    public boolean isOrthogonal() {
        for (int i = 0; i < this.getColumnCount() - 1; i++) {
            for (int j = i + 1; j < this.getColumnCount(); j++) {
                Vector v1 = new Vector(getColumn(i));
                Vector v2 = new Vector(getColumn(j));

                if (!(v1.isOrthonormalWith(v2))) return false;
            }
        }

        return true;
    }

    public SquareMatrix toSquareMatrix() {
        if (this.getRowCount() != this.getColumnCount()) throw new RuntimeException("This matrix is not a square matrix.");

        return new SquareMatrix(this.matrix);
    }

    @Override
    public VectorBase scaleWith(double c) {
        double[][] resultMatrix = new double[this.getRowCount()][this.getColumnCount()];
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[0].length; j++) {
                resultMatrix[i][j] = c * this.matrix[i][j];
            }
        }

        return new Matrix(resultMatrix);
    }

    @Override
    public VectorBase add(VectorBase v) {
        if (!(v instanceof Matrix m2)) throw new RuntimeException("Not a valid matrix.");
        if (this.getRowCount() != m2.getRowCount() ||
            this.getColumnCount() != m2.getColumnCount()
        ) throw new RuntimeException("Matrices are not in the same dimension.");
        
        double[][] resultMatrix = new double[this.getRowCount()][this.getColumnCount()];
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                resultMatrix[i][j] = this.matrix[i][j] + m2.matrix[i][j];
            }
        }
        
        return new Matrix(resultMatrix);
    }

    @Override
    public double innerProductWith(VectorBase v) {
        if (!(v instanceof Matrix m)) throw new RuntimeException("Not a valid matrix.");

        if (this.getRowCount() != m.getRowCount() ||
            this.getColumnCount() != m.getColumnCount()
        ) throw new RuntimeException("Inner product is not defined in these matrices.");

        double result = 0;
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                result += this.get(i, j) * m.get(i, j);
            }
        }

        return result;
    }

    @Override
    public int getDimension() {
        return this.getRowCount() * this.getColumnCount();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("\n");
        for (int i = 0; i < this.getRowCount(); i++) {
            s.append("[");
            for (int j = 0; j < this.getColumnCount(); j++) {
                s.append(" ").append(this.matrix[i][j]).append(" ");
            }
            s.append("]\n");
        }

        return s.toString();
    }
}
