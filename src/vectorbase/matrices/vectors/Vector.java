package vectorbase.matrices.vectors;

import vectorbase.matrices.Matrix;

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
}
