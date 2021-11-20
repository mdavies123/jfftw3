package jfftw.planning;

import jfftw.data.Alignment;
import jfftw.data.Dimensions;
import jfftw.enums.Complexity;
import jfftw.exceptions.NonDirectBufferException;
import jfftw.exceptions.UnsupportedComplexityException;

import java.nio.DoubleBuffer;

public final class DirectPlan extends Plan<DoubleBuffer> {

    protected static native void jfftw_execute_dft(DirectPlan p, DoubleBuffer ci, DoubleBuffer co);
    protected static native void jfftw_execute_dft_c2r(DirectPlan  p, DoubleBuffer ci, DoubleBuffer ro);
    protected static native void jfftw_execute_dft_r2c(DirectPlan p, DoubleBuffer ri, DoubleBuffer co);
    protected static synchronized native long jfftw_plan_dft(int rank, int[] n, DoubleBuffer ci, DoubleBuffer co, int sign, int flags);
    protected static synchronized native long jfftw_plan_dft_1d(int n, DoubleBuffer ci, DoubleBuffer co, int sign, int flags);
    protected static synchronized native long jfftw_plan_dft_2d(int n0, int n1, DoubleBuffer ci, DoubleBuffer co, int sign, int flags);
    protected static synchronized native long jfftw_plan_dft_3d(int n0, int n1, int n2, DoubleBuffer ci, DoubleBuffer co, int sign, int flags);
    protected static synchronized native long jfftw_plan_dft_c2r(int rank, int[] n, DoubleBuffer ci, DoubleBuffer ro, int flags);
    protected static synchronized native long jfftw_plan_dft_c2r_1d(int n, DoubleBuffer ci, DoubleBuffer ro, int flags);
    protected static synchronized native long jfftw_plan_dft_c2r_2d(int n0, int n1, DoubleBuffer ci, DoubleBuffer ro, int flags);
    protected static synchronized native long jfftw_plan_dft_c2r_3d(int n0, int n1, int n2, DoubleBuffer ci, DoubleBuffer ro, int flags);
    protected static synchronized native long jfftw_plan_dft_r2c(int rank, int[] n, DoubleBuffer ri, DoubleBuffer co, int flags);
    protected static synchronized native long jfftw_plan_dft_r2c_1d(int n, DoubleBuffer ri, DoubleBuffer co, int flags);
    protected static synchronized native long jfftw_plan_dft_r2c_2d(int n0, int n1, DoubleBuffer ri, DoubleBuffer co, int flags);
    protected static synchronized native long jfftw_plan_dft_r2c_3d(int n0, int n1, int n2, DoubleBuffer ri, DoubleBuffer co, int flags);

    /**
     * Constructs a new plan using direct DoubleBuffers.
     *
     * @param in    direct DoubleBuffer with complex or real data for input
     * @param out   direct DoubleBuffer with complex or real data for output
     * @param sign  transform sign (-1 or 1)
     * @param cplx  transform complexity
     * @param flags FFTW flags
     * @param dims  nullable dimensions
     */
    public DirectPlan(DoubleBuffer in, DoubleBuffer out, int sign, Complexity cplx, int flags, Dimensions dims) {
        super(
                in,
                out,
                in.capacity(),
                out.capacity(),
                sign,
                cplx,
                flags,
                dims,
                in == out ? Placement.IN_PLACE : Placement.OUT_OF_PLACE,
                Alignment.of(in),
                Alignment.of(out)
        );
        ensureDirect(in, out);
    }

    /**
     * Executes a plan using new DoubleBuffers
     *
     * @param in    new direct DoubleBuffer for input
     * @param out   new direct DoubleBuffer for output
     */
    public void execute(DoubleBuffer in, DoubleBuffer out) {
        ensureDirect(in, out);
        ensureNotDestroyed();
        ensureSizes(in.capacity(), out.capacity());
        ensurePlacement(in == out ? Placement.IN_PLACE : Placement.OUT_OF_PLACE);
        if(requiresAligned)
            ensureAlignment(Alignment.of(in), Alignment.of(out));
        switch (complexity) {
            case COMPLEX_TO_COMPLEX: jfftw_execute_dft(this, in, out); break;
            case COMPLEX_TO_REAL: jfftw_execute_dft_c2r(this, in, out); break;
            case REAL_TO_COMPLEX: jfftw_execute_dft_r2c(this, in, out); break;
            default: throw new UnsupportedComplexityException(complexity);
        }
    }

    protected long create()  {
        int[] dims = dimensions.get();
        switch (complexity) {
            case COMPLEX_TO_COMPLEX: switch (rank) {
                case 1: return jfftw_plan_dft_1d(dims[0], input, output, sign, flags);
                case 2: return jfftw_plan_dft_2d(dims[0], dims[1], input, output, sign, flags);
                case 3: return jfftw_plan_dft_3d(dims[0], dims[1], dims[2], input, output, sign, flags);
                default: return jfftw_plan_dft(rank, dims, input, output, sign, flags);
            }
            case COMPLEX_TO_REAL: switch (rank) {
                case 1: return jfftw_plan_dft_c2r_1d(dims[0], input, output, flags);
                case 2: return jfftw_plan_dft_c2r_2d(dims[0], dims[1], input, output, flags);
                case 3: return jfftw_plan_dft_c2r_3d(dims[0], dims[1], dims[2], input, output, flags);
                default: return jfftw_plan_dft_c2r(rank, dims, input, output, flags);
            }
            case REAL_TO_COMPLEX: switch (rank) {
                case 1: return jfftw_plan_dft_r2c_1d(dims[0], input, output, flags);
                case 2: return jfftw_plan_dft_r2c_2d(dims[0], dims[1], input, output, flags);
                case 3: return jfftw_plan_dft_r2c_3d(dims[0], dims[1], dims[2], input, output, flags);
                default: return jfftw_plan_dft_r2c(rank, dims, input, output, flags);
            }
            default: throw new UnsupportedComplexityException(complexity);
        }
    }

    private void ensureDirect(DoubleBuffer in, DoubleBuffer out) {
        if (!in.isDirect())
            throw new NonDirectBufferException("input");
        if (!out.isDirect())
            throw new NonDirectBufferException("output");
    }
}
