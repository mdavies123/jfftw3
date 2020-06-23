package jfftw;

import java.nio.ByteOrder;

public class Complex extends Value {

    public Complex(int n) {
        size = n;
        buff = Interface.jfftw_allocate_complex_buffer(n).order(ByteOrder.nativeOrder()).asDoubleBuffer();
    }

    public Complex(double[] in) {
        this(in.length / 2);
        buff.put(in);
    }

}
