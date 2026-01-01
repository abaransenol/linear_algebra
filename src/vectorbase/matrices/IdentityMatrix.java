package vectorbase.matrices;

public class IdentityMatrix extends SquareMatrix {
    public IdentityMatrix(int n) {
        super(createIdentityMatrix(n));
    }

    // Static factory
    private static double[][] createIdentityMatrix(int n) {
        double[][] matrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            matrix[i][i] = 1;
        }

        return matrix;
    }
}
