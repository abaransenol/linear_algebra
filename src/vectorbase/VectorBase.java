package vectorbase;

public abstract class VectorBase {
    public abstract VectorBase scaleWith(double c);
    public abstract VectorBase add(VectorBase v);
    public abstract double innerProductWith(VectorBase v);
    public double norm() { return Math.sqrt(this.innerProductWith(this)); }
    public abstract int getDimension();
}
