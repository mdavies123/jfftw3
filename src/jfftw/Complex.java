package jfftw;

public class Complex extends Value {

    public Complex() { }
    public Complex (int n) { arr = new double[n * 2]; }

    public static Complex wrap(double[] d) {
        Complex c = new Complex();
        c.setArray(d);
        return c;
    }

}
