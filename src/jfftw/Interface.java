package jfftw;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public class Interface {

    protected static boolean isLoaded = false;

    protected static synchronized native int jfftw_alignment_of(Interleave v);

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

    protected static native void jfftw_execute_split_dft(Plan p, DoubleBuffer ri, DoubleBuffer ii, DoubleBuffer ro, DoubleBuffer io);

    protected static native void jfftw_execute_split_dft_c2r(Plan p, DoubleBuffer ri, DoubleBuffer ii, DoubleBuffer ro);

    protected static native void jfftw_execute_split_dft_r2c(Plan p, DoubleBuffer ri, DoubleBuffer ro, DoubleBuffer io);

    protected static native void jfftw_execute_r2r(Plan p, Real ri, Real ro);

    protected static synchronized native void jfftw_export_wisdom_to_file(File f);

    protected static synchronized native boolean jfftw_export_wisdom_to_filename(String s);

    protected static synchronized native String jfftw_export_wisdom_to_string();

    protected static synchronized native void jfftw_flops(Plan p, double[] add, double[] mul, double[] fmas);

    protected static synchronized native void jfftw_forget_wisdom();

    protected static synchronized native void jfftw_fprint_plan(Plan p, String s);

    protected static synchronized native boolean jfftw_import_system_wisdom();

    protected static synchronized native boolean jfftw_import_wisdom_from_file(File f);

    protected static synchronized native boolean jfftw_import_wisdom_from_filename(String s);

    protected static synchronized native boolean jfftw_import_wisdom_from_string(String s);

    protected static synchronized native boolean jfftw_init_threads();

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

    protected static synchronized native long jfftw_plan_guru_dft(int rank, Guru.Dimension[] dims, int howmany_rank, Guru.Dimension[] howmany_dims, Complex in, Complex out, int sign, int flags);

    protected static synchronized native long jfftw_plan_guru_dft_c2r(int rank, Guru.Dimension[] dims, int howmany_rank, Guru.Dimension[] howmany_dims, Complex in, Real out, int flags);

    protected static synchronized native long jfftw_plan_guru_dft_r2c(int rank, Guru.Dimension[] dims, int howmany_rank, Guru.Dimension[] howmany_dims, Real in, Complex out, int flags);

    protected static synchronized native long jfftw_plan_guru_dft_split(int rank, Guru.Dimension[] dims, int howmany_rank, Guru.Dimension[] howmany_dims, DoubleBuffer ri, DoubleBuffer ii, DoubleBuffer ro, DoubleBuffer io, int flags);

    protected static synchronized native long jfftw_plan_guru_dft_split_c2r(int rank, Guru.Dimension[] dims, int howmany_rank, Guru.Dimension[] howmany_dims, DoubleBuffer ri, DoubleBuffer ii, DoubleBuffer ro, int flags);

    protected static synchronized native long jfftw_plan_guru_dft_split_r2c(int rank, Guru.Dimension[] dims, int howmany_rank, Guru.Dimension[] howmany_dims, DoubleBuffer ri, DoubleBuffer ro, DoubleBuffer io, int flags);

    protected static synchronized native long jfftw_plan_guru64_dft(int rank, Guru64.Dimension[] dims, int howmany_rank, Guru64.Dimension[] howmany_dims, Complex in, Complex out, int sign, int flags);

    protected static synchronized native long jfftw_plan_guru64_dft_c2r(int rank, Guru64.Dimension[] dims, int howmany_rank, Guru64.Dimension[] howmany_dims, Complex in, Real out, int flags);

    protected static synchronized native long jfftw_plan_guru64_dft_r2c(int rank, Guru64.Dimension[] ims, int howmany_rank, Guru64.Dimension[] howmany_dims, Real in, Complex out, int flags);

    protected static synchronized native long jfftw_plan_guru64_dft_split(int rank, Guru64.Dimension[] dims, int howmany_rank, Guru64.Dimension[] howmany_dims, DoubleBuffer ri, DoubleBuffer ii, DoubleBuffer ro, DoubleBuffer io, int sign, int flags);

    protected static synchronized native long jfftw_plan_guru64_dft_split_c2r(int rank, Guru64.Dimension[] dims, int howmany_rank, Guru64.Dimension[] howmany_dims, DoubleBuffer ri, DoubleBuffer ii, DoubleBuffer ro, int flags);

    protected static synchronized native long jfftw_plan_guru64_dft_split_r2c(int rank, Guru64.Dimension[] dims, int howmany_rank, Guru64.Dimension[] howmany_dims, DoubleBuffer ri, DoubleBuffer ro, DoubleBuffer io, int flags);

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
     * @param lib	name of the FFTW native library responsible for threads
     * @return		true if threads loaded successfully, false otherwise
     */
    public static boolean initThreads(String lib) {
		if (lib != null)
			System.loadLibrary(lib);
        return jfftw_init_threads();
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
     * @param nthreads	number of threads
     */
    public static void planWithNThreads(int nthreads) {
        jfftw_plan_with_nthreads(nthreads);
    }

    /**
     * Sets a rough timelimit for planning.
     * 
     * @param t	time in seconds
     */
    public static void setTimelimit(double t) {
        jfftw_set_timelimit(t);
    }
	
    /**
     * Loads the native FFTW and JFFTW libraries.
     * <p>
     * These native libraries must be in the JVM's java.library.path directory.
     * 
     * @param fftw		name of the FFTW library
     * @param jfftw		name of the JFFTW library
     */
	public static void loadLibraries(String fftw, String jfftw) {
		if (!isLoaded) {
			System.loadLibrary(fftw);
			System.loadLibrary(jfftw);
			isLoaded = true;
		}
	}

}
