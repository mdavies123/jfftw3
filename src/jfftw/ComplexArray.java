package jfftw;

public class ComplexArray extends Complex {

    protected double[] arr;

    public ComplexArray(int n) {
        arr = new double[n * 2];
    }

    public ComplexArray(double[] d) {
        arr = d;
    }

    public double[] get() {
        return arr;
    }

    protected boolean isBuffer() {
        return false;
    }

    public int size() {
        return arr.length / 2;
    }

    public void set(double[] d) {
        arr = d;
    }
}
