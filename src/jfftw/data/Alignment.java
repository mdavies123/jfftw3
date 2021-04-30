package jfftw.data;

import jfftw.exceptions.NonDirectBufferException;

import java.nio.DoubleBuffer;
import java.util.Objects;

public class Alignment {

    protected final int align;

    protected synchronized static native int jfftw_alignment_of(double[] arr);
    protected synchronized static native int jfftw_alignment_of(DoubleBuffer arr);

    /**
     * DoubleBuffer alignment wrapper
     *
     * @param buff buffer to test alignment
     */
    protected Alignment(DoubleBuffer buff) {
        if (!buff.isDirect())
            throw new NonDirectBufferException();
        align = jfftw_alignment_of(buff);
    }

    /**
     *
     * @param arr array to test alignment
     */
    protected Alignment(double[] arr) {
        align = jfftw_alignment_of(arr);
    }

    /**
     * Copy constructor
     *
     * @param a alignment to copy from
     */
    public Alignment(Alignment a) {
        align = a.align;
    }

    /**
     * @return alignment
     */
    public int get() {
        return align;
    }

    /**
     * Determine the FFTW alignment of a direct DoubleBuffer
     *
     * @param buff direct DoubleBuffer to check alignment of
     * @return alignment of buff
     */
    public static Alignment of(DoubleBuffer buff) {
        if (!buff.isDirect())
            throw new NonDirectBufferException();
        return new Alignment(buff);
    }

    /**
     * Determine the FFTW alignment of a primitive double array
     *
     * @param arr array to check alignment of
     * @return alignment of arr
     */
    public static Alignment of(double[] arr) {
        return new Alignment(arr);
    }

    /**
     * Bulk alignment constructor
     *
     * @param buffs direct DoubleBuffers
     * @return array of alignments
     */
    public static Alignment[] of(DoubleBuffer... buffs) {
        Alignment[] alignments = new Alignment[buffs.length];
        for (int i = 0; i < buffs.length; i++) {
            alignments[i] = of(buffs[i]);
        }
        return alignments;
    }

    /**
     * Bulk alignment constructor
     *
     * @param arrs arrays
     * @return array of alignments
     */
    public static Alignment[] of(double[]... arrs) {
        Alignment[] alignments = new Alignment[arrs.length];
        for (int i = 0; i < arrs.length; i++) {
            alignments[i] = of(arrs[i]);
        }
        return alignments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alignment alignment = (Alignment) o;
        return align == alignment.align;
    }

    @Override
    public int hashCode() {
        return Objects.hash(align);
    }
}
