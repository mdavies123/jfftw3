package jfftw.examples;

import jfftw.ComplexBuffer;
import jfftw.Flag;
import jfftw.Plan;

import java.nio.DoubleBuffer;

public class BasicBuffer {

    public static void main(String[] args) {
        int N = 4;
        ComplexBuffer in = new ComplexBuffer(N);
        ComplexBuffer out = new ComplexBuffer(N);
        Plan p = new Plan(in, out, Plan.Sign.POSITIVE, Flag.combine(Flag.MEASURE), N);
        int bound = in.size() * 2;
        for (int i = 0; i < bound; i += 2) {
            in.set(i, 2d);
            in.set(i + 1, 2d);
            System.out.println("Real: " + in.get().get(i) + "\tImag: " + in.get().get(i + 1));
        }
        System.out.println();
        p.execute();
        doSomething(out);
    }

    private static void doSomething(ComplexBuffer buffer) {
        DoubleBuffer db = buffer.get();
        int bound = buffer.size() * 2;
        for (int i = 0; i < bound; i += 2)
            System.out.println("Real: " + db.get(i) + "\tImag: " + db.get(i + 1));
    }

}
