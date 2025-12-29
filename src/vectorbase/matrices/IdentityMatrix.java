package vectorbase.matrices;

public class IdentityMatrix extends SquareMatrix {
    public IdentityMatrix(int n) {
        super(new double[n][n]);
        for (int i = 0; i < n; i++) {
            set(i, i, 1);
        }
    }
}
