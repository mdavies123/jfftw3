package jfftw;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.util.stream.IntStream;

public class ComplexBuffer extends Complex {

    private final ByteBuffer buff;

    public ComplexBuffer(int n) {
        buff = Interface.jfftw_allocate_complex_buffer(n);
        buff.order(ByteOrder.nativeOrder());
    }

    public DoubleBuffer get() {
        return buff.asDoubleBuffer();
    }

    protected boolean isBuffer() {
        return true;
    }

    public int size() {
        return get().capacity() / 2;
    }

    public void set(double[] dbl) {
        if (dbl.length / 2 > size())
            throw new ArrayIndexOutOfBoundsException("array size too large");
        IntStream.range(0, (int) size()).forEach(i -> set(i, dbl[i]));
    }

    public void set(int i, double d) {
        buff.putDouble(i * Double.BYTES, d);
    }

}
