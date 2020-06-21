package jfftw.examples;

import jfftw.*;

public class MixedComplexity {

    public static void main(String[] args) {
        int N = 1024;

        RealArray ri = new RealArray(N);
        ComplexArray co = new ComplexArray(N);
        Plan r2c = new Plan(ri, co, Plan.Sign.POSITIVE, 0, N);
        r2c.execute(ri, co);

        ComplexBuffer ci = new ComplexBuffer(N);
        RealBuffer ro = new RealBuffer(N);
        Plan c2r = new Plan(ci, ro, Plan.Sign.POSITIVE, 0, N);
        c2r.execute();
    }

}
