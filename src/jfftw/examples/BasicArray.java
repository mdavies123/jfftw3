package jfftw.examples;

import jfftw.ComplexArray;
import jfftw.Flag;
import jfftw.Plan;

import java.util.Arrays;

public class BasicArray {

    public static void main(String[] args) {
        int N = 4;
        ComplexArray in = new ComplexArray(N);
        ComplexArray out = new ComplexArray(N);
        Plan p = new Plan(in, out, Plan.Sign.POSITIVE, Flag.combine(Flag.MEASURE), N);
        Arrays.fill(in.get(), 2);
        p.execute(in, out);
        doSomething(out);
    }

    private static void doSomething(ComplexArray array) {
        double[] output = array.get();
        int bound = array.size() * 2;
        for (int i = 0; i < bound; i += 2)
            System.out.println("Real: " + output[i] + "\tImag: " + output[i + 1]);
    }

}
