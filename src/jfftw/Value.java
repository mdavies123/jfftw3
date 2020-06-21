package jfftw;

abstract class Value {

    protected abstract boolean isBuffer();

    public abstract int size();

    public abstract void set(double[] d);

    public int alignment() {
        return Interface.jfftw_alignment_of(this);
    }

}
