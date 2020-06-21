package jfftw.examples;

import jfftw.ComplexArray;
import jfftw.Flag;
import jfftw.Plan;

public class FlagUsage {

    public static void main(String[] args) {
        int flag = Flag.combine(Flag.PRESERVE_INPUT, Flag.MEASURE, Flag.UNALIGNED);
        int N = 8192;
        ComplexArray in = new ComplexArray(N);
        ComplexArray out = new ComplexArray(N);
        new Plan(in, out, Plan.Sign.NEGATIVE, flag, N).execute(in, out);
    }

}
