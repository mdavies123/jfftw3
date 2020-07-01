package jfftw;

import java.nio.DoubleBuffer;

abstract class Value {

    protected DoubleBuffer buff;
    protected int size;

    /**
     * Returns this size of this Value's transformmable array.
     * 
     * @return	size
     */
    public int size() {
        return size;
    }

    /**
     * Gives access to the DoubleBuffer backing this Value.
     * 
     * @return	DoubleBuffer backing this value
     */
    public DoubleBuffer get() {
        return buff;
    }

    /**
     * Returns the result of calling the `fftw_alignment_of` function upon
     * this value.
     * 
     * @return	array alignment
     */
    public int alignment() {
        return Interface.jfftw_alignment_of(this);
    }

}
