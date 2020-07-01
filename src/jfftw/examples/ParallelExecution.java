package jfftw.examples;

import jfftw.Complex;
import jfftw.Flag;
import jfftw.Interface;
import jfftw.Plan;

class ParallelExecution implements Runnable {

    protected static Plan plan;
    protected static int N;

    protected long start;

    public static void main(String[] args) {
        N = 8192;
        int nThreads = 8;
        Interface.loadLibraries("fftw3", "jfftw");
        Complex in = new Complex(N);
        Complex out = new Complex(N);
        plan = new Plan(in, out, Plan.Sign.POSITIVE, Flag.combine(Flag.MEASURE), N);
        long start = System.nanoTime();
        for (int i = 0; i < nThreads; i++)
            new Thread(new ParallelExecution(start)).start();
    }

    public ParallelExecution(long start) {
        this.start = start;
    }

    public void run() {
        Complex newInput = new Complex(N);
        Complex newOutput = new Complex(N);
        plan.execute(newInput, newOutput);
        double time = (double) (System.nanoTime() - start) / 1e6;
        System.out.println(Thread.currentThread().getName() + " completed in " + time + " ms.");
    }

}
