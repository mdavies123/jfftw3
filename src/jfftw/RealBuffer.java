package jfftw;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.util.stream.IntStream;

public class RealBuffer extends Real {

    private final ByteBuffer buff;

    public RealBuffer(long n) {
        buff = Interface.jfftw_allocate_real_buffer(n);
        buff.order(ByteOrder.nativeOrder());
    }

    public DoubleBuffer get() {
        return buff.asDoubleBuffer();
    }

    protected boolean isBuffer() {
        return true;
    }

    public int size() {
        return get().capacity();
    }

    public void set(double[] dbl) {
        if (dbl.length > size())
            throw new ArrayIndexOutOfBoundsException("array size too large");
        IntStream.range(0, (int) size()).forEach(i -> set(i, dbl[i]));
    }

    public void set(int i, double d) {
        buff.putDouble(i * Double.BYTES, d);
    }

}
