package vectorbase.matrices;

public class AugmentedMatrix extends Matrix {
    private final Matrix m1;
    private final Matrix m2;

    public AugmentedMatrix(Matrix m1, Matrix m2) {
        super(m1.getMatrix());
        this.m1 = m1;
        this.m2 = m2;
    }

    @Override
    public Matrix scaleRowWith(int r, double c) {
        Matrix resultMatrix1 = m1.scaleRowWith(r, c);
        Matrix resultMatrix2 = m2.scaleRowWith(r, c);

        return new AugmentedMatrix(resultMatrix1, resultMatrix2);
    }

    @Override
    public Matrix interchangeRows(int r1, int r2) {
        Matrix resultMatrix1 = m1.interchangeRows(r1, r2);
        Matrix resultMatrix2 = m2.interchangeRows(r1, r2);

        return new AugmentedMatrix(resultMatrix1, resultMatrix2);
    }

    @Override
    public Matrix addRowTo(int r1, int r2, double c) {
        Matrix resultMatrix1 = m1.addRowTo(r1, r2, c);
        Matrix resultMatrix2 = m2.addRowTo(r1, r2, c);

        return new AugmentedMatrix (resultMatrix1, resultMatrix2);
    }

    public Matrix getMatrix1() {
        return m1;
    }

    public Matrix getMatrix2() {
        return m2;
    }
}
