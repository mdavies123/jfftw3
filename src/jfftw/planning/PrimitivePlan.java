package jfftw.planning;

import jfftw.data.Alignment;
import jfftw.data.Dimensions;
import jfftw.enums.Complexity;
import jfftw.exceptions.UnsupportedComplexityException;

public final class PrimitivePlan extends Plan<double[]> {

    protected static native void jfftw_execute_dft(PrimitivePlan p, double[] ci, double[] co);
    protected static native void jfftw_execute_dft_c2r(PrimitivePlan p, double[] ci, double[] ro);
    protected static native void jfftw_execute_dft_r2c(PrimitivePlan p, double[] ri, double[] co);
    protected static synchronized native long jfftw_plan_dft(int rank, int[] n, double[] ci, double[] co, int sign, int flags);
    protected static synchronized native long jfftw_plan_dft_1d(int n, double[] ci, double[] co, int sign, int flags);
    protected static synchronized native long jfftw_plan_dft_2d(int n0, int n1, double[] ci, double[] co, int sign, int flags);
    protected static synchronized native long jfftw_plan_dft_3d(int n0, int n1, int n2, double[] ci, double[] co, int sign, int flags);
    protected static synchronized native long jfftw_plan_dft_c2r(int rank, int[] n, double[] ci, double[] ro, int flags);
    protected static synchronized native long jfftw_plan_dft_c2r_1d(int n, double[] ci, double[] ro, int flags);
    protected static synchronized native long jfftw_plan_dft_c2r_2d(int n0, int n1, double[] ci, double[] ro, int flags);
    protected static synchronized native long jfftw_plan_dft_c2r_3d(int n0, int n1, int n2, double[] ci, double[] ro, int flags);
    protected static synchronized native long jfftw_plan_dft_r2c(int rank, int[] n, double[] ri, double[] co, int flags);
    protected static synchronized native long jfftw_plan_dft_r2c_1d(int n, double[] ri, double[] co, int flags);
    protected static synchronized native long jfftw_plan_dft_r2c_2d(int n0, int n1, double[] ri, double[] co, int flags);
    protected static synchronized native long jfftw_plan_dft_r2c_3d(int n0, int n1, int n2, double[] ri, double[] co, int flags);

    /**
     * Constructs a new plan using primitive double arrays.
     *
     * @param in    complex or real input
     * @param out   complex or real output
     * @param sign  transform sign (-1 or 1)
     * @param cplx  transform complexity
     * @param flags FFTW flags
     * @param dims  nullable dimensions
     */
    public PrimitivePlan(double[] in, double[] out, int sign, Complexity cplx, int flags, Dimensions dims) {
        super(
                in,
                out,
                in.length,
                out.length,
                sign,
                cplx,
                flags,
                dims,
                in == out ? Placement.IN_PLACE : Placement.OUT_OF_PLACE,
                Alignment.of(in),
                Alignment.of(out)
        );
    }

    /**
     * Executes a plan using new arrays
     *
     * @param in    new direct array for input
     * @param out   new direct array for output
     */
    public void execute(double[] in, double[] out) {
        ensureNotDestroyed();
        ensureSizes(in.length, out.length);
        ensurePlacement(in == out ? Placement.IN_PLACE : Placement.OUT_OF_PLACE);
        if(requiresAligned)
            ensureAlignment(Alignment.of(in), Alignment.of(out));
        switch (complexity) {
            case COMPLEX_TO_COMPLEX -> jfftw_execute_dft(this, in, out);
            case COMPLEX_TO_REAL -> jfftw_execute_dft_c2r(this, in, out);
            case REAL_TO_COMPLEX -> jfftw_execute_dft_r2c(this, in, out);
            default -> throw new UnsupportedComplexityException(complexity);
        }
    }

    protected long create()  {
        int[] dims = dimensions.get();
        return switch (complexity) {
            case COMPLEX_TO_COMPLEX -> switch (rank) {
                case 1 -> jfftw_plan_dft_1d(dims[0], input, output, sign, flags);
                case 2 -> jfftw_plan_dft_2d(dims[0], dims[1], input, output, sign, flags);
                case 3 -> jfftw_plan_dft_3d(dims[0], dims[1], dims[2], input, output, sign, flags);
                default -> jfftw_plan_dft(rank, dims, input, output, sign, flags);
            };
            case COMPLEX_TO_REAL -> switch (rank) {
                case 1 -> jfftw_plan_dft_c2r_1d(dims[0], input, output, flags);
                case 2 -> jfftw_plan_dft_c2r_2d(dims[0], dims[1], input, output, flags);
                case 3 -> jfftw_plan_dft_c2r_3d(dims[0], dims[1], dims[2], input, output, flags);
                default -> jfftw_plan_dft_c2r(rank, dims, input, output, flags);
            };
            case REAL_TO_COMPLEX -> switch (rank) {
                case 1 -> jfftw_plan_dft_r2c_1d(dims[0], input, output, flags);
                case 2 -> jfftw_plan_dft_r2c_2d(dims[0], dims[1], input, output, flags);
                case 3 -> jfftw_plan_dft_r2c_3d(dims[0], dims[1], dims[2], input, output, flags);
                default -> jfftw_plan_dft_r2c(rank, dims, input, output, flags);
            };
            default -> throw new UnsupportedComplexityException(complexity);
        };
    }

}
