package jfftw;

public class Interface {

    private static native int jfftw_alignment_of(double[] p);
    private static native void jfftw_cleanup();
    private static native void jfftw_cleanup_threads();
    private static native double jfftw_cost(Plan p);
    private static native void jfftw_destroy_plan(Plan p);
    private static native double jfftw_estimate_cost(Plan p);
    private static native void jfftw_flops(Plan p, double[] add, double[] mul, double[] fmas);
    private static native void jfftw_init_threads();
    private static native void jfftw_make_planner_thread_safe();
    private static native void jfftw_plan_with_nthreads(int nthreads);
    private static native void jfftw_set_timelimit(double t);

    protected static int alignmentOf(Value v) {
        return jfftw_alignment_of(v.arr);
    }
    public static void cleanup() {
        jfftw_cleanup();
    }
    public static void cleanupThreads() {
        jfftw_cleanup_threads();
    }
    protected static double cost(Plan p) {
        return jfftw_cost(p);
    }
    protected static void destroyPlan(Plan p) {
        jfftw_destroy_plan(p);
    }
    protected static double estimateCost(Plan p) {
        return jfftw_estimate_cost(p);
    }
    public static void flops(Plan p, double[] add, double[] mul, double[] fmas) {
        jfftw_flops(p, add, mul, fmas);
    }
    public static void initThreads() {
        jfftw_init_threads();
    }
    public static void makePlannerThreadSafe() {
        jfftw_make_planner_thread_safe();
    }
    public static void planWithNumThreads(int nthreads) {
        jfftw_plan_with_nthreads(nthreads);
    }
    public static void setTimelimit(double t) {
        jfftw_set_timelimit(t);
    }

}
