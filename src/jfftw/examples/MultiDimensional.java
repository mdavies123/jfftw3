package jfftw.examples;

import jfftw.ComplexBuffer;
import jfftw.Plan;

public class MultiDimensional {

    public static void main(String[] args) {

        int n0 = 512, n1 = 64, n2 = 16, n3 = 2, n4 = 2, n5 = 2;

        ComplexBuffer cplx_3d = new ComplexBuffer(n0 * n1 * n2);
        Plan p_3d = new Plan(cplx_3d, cplx_3d, Plan.Sign.POSITIVE, 0, n0, n1, n2);

        ComplexBuffer cplx_2d = new ComplexBuffer(n0 * n1);
        Plan p_2d = new Plan(cplx_2d, cplx_2d, Plan.Sign.POSITIVE, 0, n0, n1);

        ComplexBuffer cplx_6d = new ComplexBuffer(n0 * n1 * n2 * n3 * n4 * n5);
        Plan p_6d = new Plan(cplx_6d, cplx_6d, Plan.Sign.POSITIVE, 0, n0, n1, n2, n3, n4, n5);

    }

}
