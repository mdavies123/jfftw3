package jfftw;

import java.nio.DoubleBuffer;

public class Split {

    public final DoubleBuffer re, im;
    public final boolean isComplex;

    /**
     * Allocates and returns a Split container backed by real and imaginary DoubleBuffers each of size n.
     *
     * @param n transform size
     * @return  new complex split array
     */
    public static Split allocateComplex(long n) {
        return new Split(n, true);
    }

    /**
     * Allocates and returns a Split container backed by a real DoubleBuffer of size n.
     * @param n transform size
     * @return  new real split array
     */
    public static Split allocateReal(long n) {
        return new Split(n, false);
    }

    /**
     * Creates a new split array structure for use in the Guru interface.
     *
     * @param n         transform size
     */
    protected Split(long n, boolean c) {
        isComplex = c;
        re = Interface.jfftw_allocate_real_buffer(n).asDoubleBuffer();
        im = isComplex ? Interface.jfftw_allocate_real_buffer(n).asDoubleBuffer() : null;
    }

}
