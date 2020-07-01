package jfftw.examples;

import jfftw.Complex;

import static jfftw.Plan.Sign;
import static jfftw.Plan.Complexity;
import jfftw.Interface;
import jfftw.Plan;

import java.io.File;

class InPlace {

    public static void main(String[] args) {
        int N = 8192;
        Interface.loadLibraries("fftw3", "jfftw");
        Complex cplx = new Complex(N);
        Plan p = new Plan(cplx, cplx, Sign.POSITIVE, 0, N);
        p.execute();
        p.fprint(new File("plan.txt"));
        Complexity t = p.getType();
        System.out.println(t);
    }

}
