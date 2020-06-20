package jfftw.examples;

import jfftw.*;
import jfftw.Plan.*;

class Example1 {

    public static void main(String[] args) {
        System.loadLibrary("fftw3");
        System.loadLibrary("jfftw");
        double[] someSignal = new double[2 * 512 * 512 * 4];
        Complex ci = Complex.wrap(someSignal);
        Complex co = new Complex(512*512*4);
        int[] n = { 512, 512, 4 };
        int f = Flag.combine(Flag.MEASURE, Flag.PRESERVE_INPUT);
        Plan p = new Plan(ci, co, Sign.NEGATIVE, f, n);
        for (int i = 0; i < 5; i++)
            p.execute();
    }
}
