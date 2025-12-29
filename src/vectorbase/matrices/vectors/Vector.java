package vectorbase.matrices.vectors;

import vectorbase.matrices.Matrix;

import java.util.InvalidPropertiesFormatException;

public class Vector extends Matrix {
    public Vector(double... elements) {
        super(new double[elements.length][1]);

        for (int i = 0; i < elements.length; i++) {
            set(i, 0, elements[i]);
        }
    }

    public Vector(Matrix m) {
        super(m.getColumn(0));
    }

    public boolean isOrthogonalWith(Vector v) throws InvalidPropertiesFormatException {
        return innerProductWith(v) == 0;
    }

    public boolean isOrthonormalWith(Vector v) throws InvalidPropertiesFormatException {
        return isOrthogonalWith(v) && norm() == 1;
    }
}
