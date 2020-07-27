package jfftw;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public class Interface {

    protected static boolean libsLoaded = false, threadsLoaded = false;

    protected static synchronized native ByteBuffer jfftw_alloc_real(long N);

    protected static synchronized native ByteBuffer jfftw_alloc_complex(long N);

    protected static synchronized native <T> int jfftw_alignment_of(T v);

    protected static synchronized native void jfftw_cleanup();

    protected static synchronized native void jfftw_cleanup_threads();

    protected static synchronized native double jfftw_cost(Plan<?> p);

    protected static synchronized native void jfftw_destroy_plan(Plan<?> p);

    protected static synchronized native double jfftw_estimate_cost(Plan<?> p);

    protected static native void jfftw_execute(Plan<?> p);

    protected static native <T> void jfftw_execute_dft(Plan<T> p, T ci, T co);

    protected static native <T> void jfftw_execute_dft_c2r(Plan<T> p, T ci, T ro);

    protected static native <T> void jfftw_execute_dft_r2c(Plan<T> p, T ri, T co);

    protected static synchronized native boolean jfftw_export_wisdom_to_filename(String s);

    protected static synchronized native String jfftw_export_wisdom_to_string();

    protected static synchronized native void jfftw_forget_wisdom();

    protected static synchronized native boolean jfftw_import_system_wisdom();

    protected static synchronized native boolean jfftw_import_wisdom_from_filename(String s);

    protected static synchronized native boolean jfftw_import_wisdom_from_string(String s);

    protected static synchronized native boolean jfftw_init_threads();

    protected static synchronized native void jfftw_make_planner_thread_safe();

    protected static synchronized native void jfftw_plan_with_nthreads(int nthreads);

    protected static synchronized native void jfftw_set_timelimit(double t);

    protected static synchronized native void jfftw_print_plan(Plan<?> p);

    protected static synchronized native String jfftw_sprint_plan(Plan<?> p);

    protected static synchronized native <T> long jfftw_plan_dft(int rank, int[] n, T ci, T co, int sign, int flags);

    protected static synchronized native <T> long jfftw_plan_dft_1d(int n, T ci, T co, int sign, int flags);

    protected static synchronized native <T> long jfftw_plan_dft_2d(int n0, int n1, T ci, T co, int sign, int flags);

    protected static synchronized native <T> long jfftw_plan_dft_3d(int n0, int n1, int n2, T ci, T co, int sign, int flags);

    protected static synchronized native <T> long jfftw_plan_dft_c2r(int rank, int[] n, T ci, T ro, int flags);

    protected static synchronized native <T> long jfftw_plan_dft_c2r_1d(int n, T ci, T ro, int flags);

    protected static synchronized native <T> long jfftw_plan_dft_c2r_2d(int n0, int n1, T ci, T ro, int flags);

    protected static synchronized native <T> long jfftw_plan_dft_c2r_3d(int n0, int n1, int n2, T ci, T ro, int flags);

    protected static synchronized native <T> long jfftw_plan_dft_r2c(int rank, int[] n, T ri, T co, int flags);

    protected static synchronized native <T> long jfftw_plan_dft_r2c_1d(int n, T ri, T co, int flags);

    protected static synchronized native <T> long jfftw_plan_dft_r2c_2d(int n0, int n1, T ri, T co, int flags);

    protected static synchronized native <T> long jfftw_plan_dft_r2c_3d(int n0, int n1, int n2, T ri, T co, int flags);

    /**
     * Used to determine the alignment of a DoubleBuffer.
     *
     * @param arr DoubleBuffer to check the alignment of
     * @return alignment
     */
    public static <T> int alignmentOf(T arr) {
        if (arr instanceof DoubleBuffer && !((DoubleBuffer)arr).isDirect())
            throw new IllegalArgumentException("input buffer is not direct");
        return jfftw_alignment_of(arr);
    }

    /**
     * Used to determine the alignment of an array.
     *
     * @param arr array to check the alignment of
     * @return alignment
     */
    public static int alignmentOf(double[] arr) {
        return jfftw_alignment_of(arr);
    }

    /**
     * Allocates a complex buffer of size N*2 using fftw_alloc_complex.
     *
     * @param N buffer size
     * @return new direct DoubleBuffer
     */
    public static DoubleBuffer allocateComplex(long N) {
        return jfftw_alloc_complex(N).asDoubleBuffer();
    }

    /**
     * Allocated a real buffer of size N using fftw_alloc_real
     *
     * @param N buffer size
     * @return new direct DoubleBuffer
     */
    public static DoubleBuffer allocateReal(long N) {
        return jfftw_alloc_real(N).asDoubleBuffer();
    }

    /**
     * Cleans up FFTW internal state as if the program just began.
     */
    public static void cleanup() {
        jfftw_cleanup();
    }

    /**
     * Use to get rid of all memory and other resources allocated internally by FFTW.
     * <p>
     * You must not execute any previously created plans after calling this function.
     */
    public static void cleanupThreads() {
        jfftw_cleanup_threads();
    }

    /**
     * This function, which need only be called once, performs any one-time
     * initialization required to use threads on your system.
     * <p>
     * You may also specify the name of the FFTW native library (if any) which handles
     * threads. If your FFTW threads library is built in to the main FFTW library,
     * you may pass `null` in for this argument.
     *
     * @param lib name of the FFTW native library responsible for threads
     * @return true if threads loaded successfully, false otherwise
     */
    public static boolean initThreads(String lib) {
        if (!threadsLoaded) {
            if (lib != null)
                System.loadLibrary(lib);
            threadsLoaded = jfftw_init_threads();
        }
        return threadsLoaded;
    }

    /**
     * Installs a hook that wraps a lock (chosen by FFTW) around all planner calls.
     * <p>
     * This method should be used only as a last resort or in situations where you
     * cannot control other programs which may be creating plans concurrently.
     */
    public static void makePlannerThreadSafe() {
        jfftw_make_planner_thread_safe();
    }

    /**
     * Specifies a number of threads to use in planning.
     *
     * @param nthreads number of threads
     */
    public static void planWithNThreads(int nthreads) {
        jfftw_plan_with_nthreads(nthreads);
    }

    /**
     * Sets a rough timelimit for planning.
     *
     * @param t time in seconds
     */
    public static void setTimelimit(double t) {
        jfftw_set_timelimit(t);
    }

    /**
     * Loads the native FFTW and JFFTW libraries.
     * <p>
     * These native libraries must be in the JVM's java.library.path directory.
     *
     * @param fftw  name of the FFTW library
     * @param jfftw name of the JFFTW library
     */
    public static void loadLibraries(String fftw, String jfftw) {
        if (!libsLoaded) {
            System.loadLibrary(fftw);
            System.loadLibrary(jfftw);
            libsLoaded = true;
        }
    }

}
