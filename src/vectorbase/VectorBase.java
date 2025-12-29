package vectorbase;

import java.util.InvalidPropertiesFormatException;

public abstract class VectorBase {
    public abstract VectorBase scaleWith(double c) throws InvalidPropertiesFormatException;
    public abstract VectorBase add(VectorBase v) throws InvalidPropertiesFormatException;
    public abstract double innerProductWith(VectorBase v) throws InvalidPropertiesFormatException;
    public double norm() throws InvalidPropertiesFormatException { return Math.sqrt(innerProductWith(this)); }
    public abstract int getDimension();
}
