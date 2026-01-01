import vectorbase.matrices.Matrix;
import vectorbase.matrices.vectors.Vector;
import vectorbase.matrices.vectors.VectorSet;

public class Main {
    public static void main(String[] args) {
        // Playground for the library.

        double[] r1 = {0, 1, -4};
        double[] r2 = {2, -3, 2};
        double[] r3 = {4, -8, 12};
        Matrix m = new Matrix(r1, r2, r3);

        double[] r1_ = {-3, 6, -1, 1, 7};
        double[] r2_ = {1, -2, 2, 3, -1};
        double[] r3_ = {2, -4, 5, 8, -4};
        Matrix m_ = new Matrix(r1_, r2_, r3_);

        VectorSet b = new VectorSet(new Vector(1, 2), new Vector(4, 1));
        System.out.println(b.getRelativeCoordinatesOf(new Vector(6, 5)));

        Vector v = new Vector(8,1,1);

        System.out.println(m.solve(v));

        //System.out.println(m.getColumnSpace().getBasis());

    }
}