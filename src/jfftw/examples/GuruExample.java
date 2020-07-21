package jfftw.examples;

import jfftw.*;

public class GuruExample {

    public static void main(String[] args) {
        Interface.loadLibraries("fftw3", "jfftw");
        Guru.Dimension[] dims = Guru.makeDimensions(new int[]{4, 5000, 2}, new int[]{1, 1, 1}, new int[]{1, 1, 1});
        Guru.Dimension[] howmany = Guru.makeDimensions(new int[0], new int[0], new int[0]);
        Complex signal = new Complex(4 * 5000 * 2);
        Plan p = Guru.plan(dims, howmany, signal, signal, Sign.NEGATIVE, 0);
        p.execute();
        Split splitSignal = Split.allocate(4 * 5000 * 2);
        Real out = new Real(4 * 5000 * 2);
        p = Guru.plan(dims, howmany, splitSignal, out, 0);
        System.out.println(p);
        out = new Real(4 * 5000 * 2);
        p.execute(splitSignal, out);
        p = new Plan(new Complex(4*5000*2), new Complex(4*5000*2), Sign.NEGATIVE, 0);
        try {
        	p.execute(splitSignal, splitSignal);
        } catch (IllegalArgumentException e) {
        	System.out.println("Incorrect input arrays for new array execute function.");
        }
    }

}
