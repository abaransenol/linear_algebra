import vectorbase.matrices.Matrix;
import vectorbase.matrices.vectors.Vector;

import java.util.InvalidPropertiesFormatException;

public class Main {
    public static void main(String[] args) throws InvalidPropertiesFormatException {
        double[] r1 = {1, 2, 3, 4};
        double[] r2 = {5, 6, 7, 8};
        double[] r3 = {9, 10, 11, 12};
        Matrix m = new Matrix(r1, r2, r3);

        Vector v = new Vector(3,4,5,6);

    }
}