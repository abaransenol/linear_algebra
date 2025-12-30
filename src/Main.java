import vectorbase.matrices.Matrix;
import vectorbase.matrices.vectors.Vector;
import vectorbase.matrices.vectors.VectorSet;

import java.util.InvalidPropertiesFormatException;

public class Main {
    public static void main(String[] args) throws InvalidPropertiesFormatException {
        // Playground for the library.

        double[] r1 = {1, 2, 3, 4};
        double[] r2 = {5, 6, 7, 8};
        double[] r3 = {9, 10, 11, 12};
        Matrix m = new Matrix(r1, r2, r3);

        double[] r1_ = {-3, 6, -1, 1, 7};
        double[] r2_ = {1, -2, 2, 3, -1};
        double[] r3_ = {2, -4, 5, 8, -4};
        Matrix m_ = new Matrix(r1_, r2_, r3_);

        Vector v = new Vector(1,2,3);
        Vector v1 = new Vector(1,0,0);
        Vector v2 = new Vector(0,1,0);
        Vector v3 = new Vector(0,0,1);

        VectorSet vectorSet = new VectorSet(v3, v3);
        System.out.println(vectorSet.isLinearlyIndependent());

        //System.out.println(m.getColumnSpace().getBasis());

    }
}