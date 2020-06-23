package jfftw;

import java.nio.DoubleBuffer;

abstract class Value {

    protected DoubleBuffer buff;
    protected int size;

    public int size() {
        return size;
    }

    public DoubleBuffer get() {
        return buff;
    }

    public int alignment() {
        return Interface.jfftw_alignment_of(this);
    }

}
