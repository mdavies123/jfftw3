package jfftw.examples;

import jfftw.ComplexArray;
import jfftw.ComplexBuffer;
import jfftw.Plan;

import java.util.Arrays;

public class MixedArrays {

    public static void main(String[] args) {
        int N = 8192;
        ComplexArray in = new ComplexArray(N);
        ComplexBuffer out = new ComplexBuffer(N);
        Plan p = new Plan(in, out, Plan.Sign.POSITIVE, 0, N);
        Arrays.fill(in.get(), 2);
        p.execute(in, out);
    }

}
