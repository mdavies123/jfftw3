package jfftw;

import java.nio.ByteOrder;
import java.nio.DoubleBuffer;

public class Complex extends Interleave {

	/**
	 * Creates a new Complex array backed by a direct DoubleBuffer of size n * 2.
	 * <p>
	 * The memory for the direct DoubleBuffer is allocated using fftw_malloc to 
	 * ensure the array is aligned in a way that enables FFTW to use SIMD.
	 * 
	 * @param n		transform size
	 */
    public Complex(int n) {
        size = n;
        buff = Interface.jfftw_allocate_complex_buffer(n).order(ByteOrder.nativeOrder()).asDoubleBuffer();
    }

    /**
     * Creates a new Complex and copies the contents of in to the DoubleBuffer.
     * 
     * @param in	complex interleaved signal
     */
    public Complex(double[] in) {
        this(in.length / 2);
        buff.put(in);
    }
    
    public Complex(DoubleBuffer b) {
    	buff = b;
    }

}
