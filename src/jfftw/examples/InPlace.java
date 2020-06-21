package jfftw.examples;

import jfftw.ComplexArray;
import jfftw.ComplexBuffer;
import jfftw.Plan;

public class InPlace {

    public static void main(String[] args) {
        int N = 8192;
        ComplexBuffer cplx = new ComplexBuffer(N);
        Plan p = new Plan(cplx, cplx, Plan.Sign.POSITIVE, 0, N);
        p.execute();
        ComplexArray cplxArray = new ComplexArray(N);
        p.execute(cplxArray, cplxArray);
    }

}
