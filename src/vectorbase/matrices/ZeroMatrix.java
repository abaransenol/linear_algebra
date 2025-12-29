package vectorbase.matrices;

public class ZeroMatrix extends Matrix{
    public ZeroMatrix(int rowCount, int columnCount) {
        super(new double[rowCount][columnCount]);
    }
}
