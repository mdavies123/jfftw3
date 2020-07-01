package jfftw.examples;

import jfftw.*;

public class GuruExample {

    public static void main(String[] args) {
        Interface.loadLibraries("fftw3", "jfftw");
        Guru.Dimension[] dims = Guru.makeDimensions(new int[]{4, 5000, 2}, new int[]{1, 1, 1}, new int[]{1, 1, 1});
        Guru.Dimension[] howmany = Guru.makeDimensions(new int[0], new int[0], new int[0]);
        Split signal = Split.allocateComplex(4 * 5000 * 2);
        Plan p = Guru.plan(dims, howmany, signal, signal, 0);
        p.execute();
    }

}
