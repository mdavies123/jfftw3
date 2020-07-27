package jfftw;

import java.nio.DoubleBuffer;

import static jfftw.Interface.*;

public final class DirectPlan extends Plan<DoubleBuffer> {

    /**
     * Constructs a new plan using direct DoubleBuffers.
     *
     * @param in    direct DoubleBuffer with complex or real data for input
     * @param out   direct DoubleBuffer with complex or real data for output
     * @param sign  transform sign (-1 or 1)
     * @param cplx  transform complexity
     * @param flag  FFTW flags
     * @param dims  optional variadic dimensions
     */
    public DirectPlan(DoubleBuffer in, DoubleBuffer out, int sign, Complexity cplx, int flag, int... dims) {
        super(
                in,
                out,
                in.capacity(),
                out.capacity(),
                sign,
                cplx,
                flag,
                dims,
                in == out ? Placement.IN_PLACE : Placement.OUT_OF_PLACE,
                new int[]{alignmentOf(in), alignmentOf(out)}
        );
    }

    /**
     * Executes a plan using new DoubleBuffers
     *
     * @param in    new direct DoubleBuffer for input
     * @param out   new direct DoubleBuffer for output
     */
    public void execute(DoubleBuffer in, DoubleBuffer out) {
        ensureDirectBuffers(in, out);
        ensureNotDestroyed();
        ensureSizes(in.capacity(), out.capacity());
        ensurePlacement(in == out ? Placement.IN_PLACE : Placement.OUT_OF_PLACE);
        if(requiresAligned)
            ensureAlignment(alignmentOf(in), alignmentOf(out));
        jfftw_execute_dft(this, in, out);
    }

    private void ensureDirectBuffers(DoubleBuffer in, DoubleBuffer out) {
        if (!in.isDirect())
            throw new IllegalArgumentException("input buffer is not  direct");
        if (!out.isDirect())
            throw new IllegalArgumentException("output buffer is not  direct");
    }
}
