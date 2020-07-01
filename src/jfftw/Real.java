package jfftw;

import java.nio.ByteOrder;

public class Real extends Value {

	/**
	 * Creates a new Real array backed by a direct DoubleBuffer of size n.
	 * <p>
	 * The memory for the direct DoubleBuffer is allocated using fftw_malloc to 
	 * ensure the array is aligned in a way that enables FFTW to use SIMD.
	 * 
	 * @param n		transform size
	 */
    public Real(int n) {
        size = n;
        buff = Interface.jfftw_allocate_real_buffer(n).order(ByteOrder.nativeOrder()).asDoubleBuffer();
    }

    /**
     * Creates a new Real and copies the contents of in to the DoubleBuffer.
     * 
     * @param in	real signal
     */
    public Real(double[] in) {
        this(in.length);
        buff.put(in);
    }

}
