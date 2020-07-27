package jfftw;

import static jfftw.Interface.*;

public final class PrimitivePlan extends Plan<double[]> {

    /**
     * Constructs a new plan using primitive double arrays.
     *
     * @param in    complex or real input
     * @param out   complex or real output
     * @param sign  transform sign (-1 or 1)
     * @param cplx  transform complexity
     * @param flag  FFTW flags
     * @param dims  optional variadic dimensions
     */
    public PrimitivePlan(double[] in, double[] out, int sign, Complexity cplx, int flag, int... dims) {
        super(
                in,
                out,
                in.length,
                out.length,
                sign,
                cplx,
                flag,
                dims,
                in == out ? Placement.IN_PLACE : Placement.OUT_OF_PLACE,
                new int[]{alignmentOf(in), alignmentOf(out)}
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
            ensureAlignment(alignmentOf(in), alignmentOf(out));
        jfftw_execute_dft(this, in, out);
    }

}
