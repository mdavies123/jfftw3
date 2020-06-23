package jfftw.examples;

import jfftw.Complex;
import jfftw.Plan;

import java.io.File;

class InPlace {

    public static void main(String[] args) {
        int N = 8192;
        Complex cplx = new Complex(N);
        Plan p = new Plan(cplx, cplx, Plan.Sign.POSITIVE, 0, N);
        p.execute();
        p.fprint(new File("plan.txt"));
    }

}
