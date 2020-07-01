package jfftw;

import java.nio.DoubleBuffer;

public class SplitArray {

    public final DoubleBuffer re, im;

    /**
     * Creates a new split array structure for use in the Guru interface.
     *
     * @param n         transform size
     * @param isComplex true to create a complex split array
     */
    public SplitArray(long n, boolean isComplex) {
        re = Interface.jfftw_allocate_real_buffer(n).asDoubleBuffer();
        im = isComplex ? Interface.jfftw_allocate_real_buffer(n).asDoubleBuffer() : null;
    }

}
