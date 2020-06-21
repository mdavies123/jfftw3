package jfftw;

public class RealArray extends Real {

    protected double[] arr;

    public RealArray(int n) {
        arr = new double[n];
    }

    public RealArray(double[] d) {
        arr = d;
    }

    protected boolean isBuffer() {
        return false;
    }

    public double[] get() {
        return arr;
    }

    public int size() {
        return arr.length;
    }

    public void set(double[] d) {
        arr = d;
    }
}
