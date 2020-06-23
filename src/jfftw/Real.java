package jfftw;

import java.nio.ByteOrder;

public class Real extends Value {

    public Real(int n) {
        size = n;
        buff = Interface.jfftw_allocate_real_buffer(n).order(ByteOrder.nativeOrder()).asDoubleBuffer();
    }

    public Real(double[] in) {
        this(in.length);
        buff.put(in);
    }

}
