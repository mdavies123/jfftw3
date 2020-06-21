package jfftw.examples;

import jfftw.ComplexBuffer;
import jfftw.Flag;
import jfftw.Plan;

public class ParallelExecution implements Runnable {

    protected static Plan plan;
    protected static int N;

    protected int t;

    public static void main(String[] args) {
        N = 8192;
        int nThreads = 8;
        ComplexBuffer in = new ComplexBuffer(N);
        ComplexBuffer out = new ComplexBuffer(N);
        plan = new Plan(in, out, Plan.Sign.POSITIVE, Flag.combine(Flag.MEASURE), N);
        for (int i = 0; i < nThreads; i++)
            new Thread(new ParallelExecution(i)).start();
    }

    public ParallelExecution(int t) {
        this.t = t;
    }

    public void run() {
        long startThread = System.nanoTime();
        ComplexBuffer newInput = new ComplexBuffer(N);
        ComplexBuffer newOutput = new ComplexBuffer(N);
        plan.execute(newInput, newOutput);
        double execMs = (double) (System.nanoTime() - startThread) / 1e6;
        System.out.println("Thread " + t + " completed in " + execMs + " ms.");
    }

}
