package jfftw;

public class Real extends Value {

    public Real() { }
    public Real(int n) { arr = new double[n]; }

    public static Real wrap(double[] d) {
        Real r = new Real();
        r.setArray(d);
        return r;
    }

}
