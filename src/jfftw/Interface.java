package jfftw;

public class Interface {

    protected static boolean libsLoaded = false, threadsLoaded = false;


    protected static synchronized native void jfftw_cleanup();
    protected static synchronized native void jfftw_cleanup_threads();
    protected static synchronized native boolean jfftw_init_threads();
    protected static synchronized native void jfftw_make_planner_thread_safe();
    protected static synchronized native void jfftw_plan_with_nthreads(int nthreads);
    protected static synchronized native void jfftw_set_timelimit(double t);

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
