package jfftw;

import java.nio.DoubleBuffer;

public class Split {
	
	public final DoubleBuffer re, im;
	
	/**
	 * Allocates a new complex split array with real and imaginary DoubleBuffers.
	 * 
	 * @param n		transform size
	 * @return		new Split array
	 */
	public static Split allocate(long n) {
		DoubleBuffer re = Interface.jfftw_allocate_real_buffer(n).asDoubleBuffer();
		DoubleBuffer im = Interface.jfftw_allocate_real_buffer(n).asDoubleBuffer();
		return new Split(re, im);
	}
	
	/**
	 * Returns the transform size of this split array.
	 * 
	 * @return	transform size
	 */
	public int size() {
		return re.capacity();
	}

	protected Split(DoubleBuffer re, DoubleBuffer im) {
		this.re = re;
		this.im = im;
	}

}
