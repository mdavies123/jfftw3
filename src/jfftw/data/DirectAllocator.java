package jfftw.data;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public class DirectAllocator {

    protected synchronized static native ByteBuffer jfftw_alloc_complex(int N);
    protected synchronized static native ByteBuffer jfftw_alloc_real(int N);

    public static DoubleBuffer allocateComplex(int N) {
        return jfftw_alloc_complex(N).asDoubleBuffer();
    }

    public static DoubleBuffer allocateReal(int N) {
        return jfftw_alloc_real(N).asDoubleBuffer();
    }
}
