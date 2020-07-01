package jfftw.examples;

import jfftw.*;

import java.nio.DoubleBuffer;

class OutOfPlace {

    public static void main(String[] args) {
        int N = 4096;
        Interface.loadLibraries("fftw3", "jfftw");
        double[] inArr = new double[N * 2];
        Complex in = new Complex(inArr);
        Complex out = new Complex(N);
        Plan p = new Plan(in, out, Sign.POSITIVE, Flag.combine(Flag.MEASURE, Flag.PRESERVE_INPUT), N);
        p.execute();
        doSomething(out);
    }

    private static void doSomething(Complex buffer) {
        DoubleBuffer db = buffer.get();
        int bound = db.capacity();
        double re, im;
        for (int i = 0; i < bound; i += 2) {
            re = db.get(i);
            im = db.get(i + 1);
        }
    }

}
