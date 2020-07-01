package jfftw;

import java.nio.DoubleBuffer;

abstract class Interleave {

    protected DoubleBuffer buff;
    protected int size;

    /**
     * Returns this size of this Interleave's transformable array.
     * 
     * @return	size
     */
    public int size() {
        return size;
    }

    /**
     * Gives access to the DoubleBuffer backing this Interleave.
     * 
     * @return	DoubleBuffer backing this value
     */
    public DoubleBuffer get() {
        return buff;
    }

    /**
     * Returns the result of calling the `fftw_alignment_of` function upon
     * this Interleave.
     * 
     * @return	array alignment
     */
    public int alignment() {
        return Interface.jfftw_alignment_of(this);
    }

}
