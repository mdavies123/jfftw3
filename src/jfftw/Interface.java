package jfftw;

import java.io.File;
import java.nio.ByteBuffer;

public class Interface {

    static {
        System.loadLibrary("fftw3");
        System.loadLibrary("jfftw");
    }

    protected static synchronized native int jfftw_alignment_of(Value v);

    protected static synchronized native ByteBuffer jfftw_allocate_complex_buffer(long n);

    protected static synchronized native ByteBuffer jfftw_allocate_real_buffer(long n);

    protected static synchronized native void jfftw_cleanup();

    protected static synchronized native void jfftw_cleanup_threads();

    protected static synchronized native double jfftw_cost(Plan p);

    protected static synchronized native void jfftw_destroy_plan(Plan p);

    protected static synchronized native double jfftw_estimate_cost(Plan p);

    protected static native void jfftw_execute(Plan p);

    protected static native void jfftw_execute_dft(Plan p, Complex ci, Complex co);

    protected static native void jfftw_execute_dft_c2r(Plan p, Complex ci, Real ro);

    protected static native void jfftw_execute_dft_r2c(Plan p, Real ri, Complex co);

    protected static native void jfftw_execute_r2r(Plan p, Real ri, Real ro);

    protected static synchronized native void jfftw_export_wisdom_to_file(File f);

    protected static synchronized native void jfftw_export_wisdom_to_filename(String s);

    protected static synchronized native String jfftw_export_wisdom_to_string();

    protected static synchronized native void jfftw_flops(Plan p, double[] add, double[] mul, double[] fmas);

    protected static synchronized native void jfftw_forget_wisdom();

    protected static synchronized native void jfftw_fprint_plan(Plan p, String s);

    protected static synchronized native void jfftw_import_system_wisdom();

    protected static synchronized native void jfftw_import_wisdom_from_file(File f);

    protected static synchronized native void jfftw_import_wisdom_from_filename(String s);

    protected static synchronized native void jfftw_import_wisdom_from_string(String s);

    protected static synchronized native void jfftw_init_threads();

    protected static synchronized native void jfftw_make_planner_thread_safe();

    protected static synchronized native void jfftw_plan_with_nthreads(int nthreads);

    protected static synchronized native void jfftw_set_timelimit(double t);

    protected static synchronized native void jfftw_print_plan(Plan p);

    protected static synchronized native String jfftw_sprint_plan(Plan p);

    protected static synchronized native long jfftw_plan_dft(int rank, int[] n, Complex in, Complex out, int sign, int flags);

    protected static synchronized native long jfftw_plan_dft_1d(int n, Complex in, Complex out, int sign, int flags);

    protected static synchronized native long jfftw_plan_dft_2d(int n0, int n1, Complex in, Complex out, int sign, int flags);

    protected static synchronized native long jfftw_plan_dft_3d(int n0, int n1, int n2, Complex in, Complex out, int sign, int flags);

    protected static synchronized native long jfftw_plan_dft_c2r(int rank, int[] n, Complex in, Real out, int flags);

    protected static synchronized native long jfftw_plan_dft_c2r_1d(int n, Complex in, Real out, int flags);

    protected static synchronized native long jfftw_plan_dft_c2r_2d(int n0, int n1, Complex in, Real out, int flags);

    protected static synchronized native long jfftw_plan_dft_c2r_3d(int n0, int n1, int n2, Complex in, Real out, int flags);

    protected static synchronized native long jfftw_plan_dft_r2c(int rank, int[] n, Real in, Complex out, int flags);

    protected static synchronized native long jfftw_plan_dft_r2c_1d(int n, Real in, Complex out, int flags);

    protected static synchronized native long jfftw_plan_dft_r2c_2d(int n0, int n1, Real in, Complex out, int flags);

    protected static synchronized native long jfftw_plan_dft_r2c_3d(int n0, int n1, int n2, Real in, Complex out, int flags);

    public static void cleanup() {
        jfftw_cleanup();
    }

    public static void cleanupThreads() {
        jfftw_cleanup_threads();
    }

    public static void flops(Plan p, double[] add, double[] mul, double[] fmas) {
        jfftw_flops(p, add, mul, fmas);
    }

    public static void initThreads(String lib) {
        System.loadLibrary(lib);
        jfftw_init_threads();
    }

    public static void makePlannerThreadSafe() {
        jfftw_make_planner_thread_safe();
    }

    public static void planWithNThreads(int nthreads) {
        jfftw_plan_with_nthreads(nthreads);
    }

    public static void setTimelimit(double t) {
        jfftw_set_timelimit(t);
    }

}
