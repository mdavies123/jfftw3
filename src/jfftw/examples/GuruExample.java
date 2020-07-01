package jfftw.examples;

import jfftw.*;

public class GuruExample {

    public static void main(String[] args) {
        Interface.loadLibraries("fftw3", "jfftw");
        Guru.Dimension[] dims = Guru.makeDimensions(new int[]{4, 5000, 2}, new int[]{1, 1, 1}, new int[]{1, 1, 1});
        Plan p = Guru.plan(dims, dims, new Complex(4 * 5000 * 2), new Complex(4 * 5000 * 2), Sign.NEGATIVE, 0);
        SplitArray arr = p.getSplitInput();
    }

}
