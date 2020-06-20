package jfftw;

class Execution {

    private static native void jfftw_execute(Plan p);
    private static native void jfftw_execute_dft(Plan p, Complex ci, Complex co);
    private static native void jfftw_execute_dft_c2r(Plan p, Complex ci, Real ro);
    private static native void jfftw_execute_dft_r2c(Plan p, Real ri, Complex co);
    private static native void jfftw_execute_r2r(Plan p, Real ri, Real ro);

    protected static void execute(Plan p) { jfftw_execute(p); }
    protected static void executeDft(Plan p, Complex ci, Complex co) { jfftw_execute_dft(p, ci, co); }
    protected static void executeDftComplexToReal(Plan p, Complex ci, Real ro) { jfftw_execute_dft_c2r(p, ci, ro); }
    protected static void executeDftRealToComplex(Plan p, Real ri, Complex co) { jfftw_execute_dft_r2c(p, ri, co); }
    protected static void executeRealToReal(Plan p, Real ri, Real ro) { jfftw_execute_r2r(p, ri, ro); }

}
