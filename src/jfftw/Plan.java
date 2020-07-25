package jfftw;

import java.io.File;
import java.nio.DoubleBuffer;
import java.util.Arrays;

import static jfftw.Interface.*;

public class Plan {

    protected final DoubleBuffer input, output;
    protected final Complexity complexity;
    protected final Placement placement;
    protected final long address;
    protected final int[] dimensions, alignment;
    protected final int sign, size, rank, flags;
    protected final boolean requiresAligned;
    protected boolean destroyed = false;

    /**
     * Constructs a new FFTW plan.
     *
     * @param in   direct DoubleBuffer with complex or real data for input
     * @param out  direct DoubleBuffer with complex or real data for output
     * @param s    transform sign (-1 or 1)
     * @param cplx transform complexity
     * @param flag FFTW flags
     * @param dims optional variadic dimensions
     */
    public Plan(DoubleBuffer in, DoubleBuffer out, int s, Complexity cplx, int flag, int... dims) {

        ensureDirectBuffers(in, out);

        input = in;
        output = out;
        sign = s;
        complexity = cplx;
        placement = in == out ? Placement.IN_PLACE : Placement.OUT_OF_PLACE;

        if (dims == null || dims.length == 0) {
            if (complexity == Complexity.COMPLEX_TO_COMPLEX || complexity == Complexity.COMPLEX_TO_REAL)
                dims = new int[]{in.capacity() / 2};
            else
                dims = new int[]{in.capacity()};
        }

        rank = dims.length;
        dimensions = Arrays.copyOf(dims, rank);
        int N = 1;
        for (int d : dimensions)
            N *= d;
        size = N;

        flags = flag;
        alignment = new int[]{jfftw_alignment_of(input), jfftw_alignment_of(output)};
        requiresAligned = (flags & Flag.UNALIGNED.value) == 0;

        ensureSizes(in.capacity(), out.capacity());
        address = create();
        if (address == 0)
            throw new NullPointerException("plan creation returned null");
    }

    private void ensureSizes(int iSize, int oSize) {
        switch (complexity) {
            case COMPLEX_TO_COMPLEX:
                if (iSize != oSize)
                    throw new IllegalArgumentException("input arrays not equal size");
                if ((iSize / 2) != size)
                    throw new IllegalArgumentException("transform size not equal to dimensions size");
                break;
            case COMPLEX_TO_REAL:
                if (iSize / 2 != oSize)
                    throw new IllegalArgumentException("input arrays not compatible size");
                if ((iSize / 2) != size)
                    throw new IllegalArgumentException("transform size not equal to dimensions size");
                break;
            case REAL_TO_COMPLEX:
                if (iSize != (oSize / 2))
                    throw new IllegalArgumentException("input arrays not compatible size");
                if (iSize != size)
                    throw new IllegalArgumentException("transform size not equal to dimensions size");
                break;
            case REAL_TO_REAL:
                if (iSize != oSize)
                    throw new IllegalArgumentException("input arrays not equal size");
                if (iSize != size)
                    throw new IllegalArgumentException("transform size not equal to dimensions size");
        }
    }

    private void ensureNotDestroyed() {
        if (destroyed)
            throw new NullPointerException("plan is destroyed");
    }

    private void ensureDirectBuffers(DoubleBuffer in, DoubleBuffer out) {
        if (!in.isDirect())
            throw new IllegalArgumentException("input buffer is not  direct");
        if (!out.isDirect())
            throw new IllegalArgumentException("output buffer is not  direct");
    }

    private long create() {
        switch (complexity) {
            case COMPLEX_TO_COMPLEX:
                switch (rank) {
                    case 1:
                        return jfftw_plan_dft_1d(dimensions[0], input, output, sign, flags);
                    case 2:
                        return jfftw_plan_dft_2d(dimensions[0], dimensions[1], input, output, sign, flags);
                    case 3:
                        return jfftw_plan_dft_3d(dimensions[0], dimensions[1], dimensions[2], input, output, sign, flags);
                    default:
                        return jfftw_plan_dft(rank, dimensions, input, output, sign, flags);
                }
            case COMPLEX_TO_REAL:
                switch (rank) {
                    case 1:
                        return jfftw_plan_dft_c2r_1d(dimensions[0], input, output, flags);
                    case 2:
                        return jfftw_plan_dft_c2r_2d(dimensions[0], dimensions[1], input, output, flags);
                    case 3:
                        return jfftw_plan_dft_c2r_3d(dimensions[0], dimensions[1], dimensions[2], input, output, flags);
                    default:
                        return jfftw_plan_dft_c2r(rank, dimensions, input, output, flags);
                }
            case REAL_TO_COMPLEX:
                switch (rank) {
                    case 1:
                        return jfftw_plan_dft_r2c_1d(dimensions[0], input, output, flags);
                    case 2:
                        return jfftw_plan_dft_r2c_2d(dimensions[0], dimensions[1], input, output, flags);
                    case 3:
                        return jfftw_plan_dft_r2c_3d(dimensions[0], dimensions[1], dimensions[2], input, output, flags);
                    default:
                        return jfftw_plan_dft_r2c(rank, dimensions, input, output, flags);
                }
            default:
                throw new UnsupportedOperationException(complexity + " transform not supported");
        }
    }

    /**
     * Prints this plan to a file.
     *
     * @param f file to print plan
     */
    public void fprint(File f) {
        if (f.canWrite()) {
            ensureNotDestroyed();
            jfftw_fprint_plan(this, f.getAbsolutePath());
        } else {
            throw new SecurityException("cannot write to file: " + f);
        }
    }

    /**
     * Prints this plan to stdout.
     * <p>
     * Since this method is implemented in native code, it cannot guarantee the output will be as expected.
     */
    public void print() {
        ensureNotDestroyed();
        jfftw_print_plan(this);
    }

    /**
     * Returns the internal measurement of how effective this plan is.
     *
     * @return cost of this plan
     */
    public double cost() {
        ensureNotDestroyed();
        return jfftw_cost(this);
    }

    /**
     * Destroys the plan pointed to by this object's address field.
     */
    public void destroy() {
        ensureNotDestroyed();
        jfftw_destroy_plan(this);
        destroyed = true;
    }

    /**
     * Gives an estimate of how effective this plan is.
     *
     * @return estimated cost of this plan
     */
    public double estimateCost() {
        ensureNotDestroyed();
        return jfftw_estimate_cost(this);
    }

    /**
     * Executes this plan using the input and output arrays associated with this plan.
     */
    public void execute() {
        ensureNotDestroyed();
        jfftw_execute(this);
    }

    /**
     * Executes this COMPLEX_TO_COMPLEX plan using new arrays.
     *
     * @param i new complex input array
     * @param o new complex output array
     */
    public void execute(DoubleBuffer i, DoubleBuffer o) {

        ensureNotDestroyed();
        ensureSizes(i.capacity(), o.capacity());
        ensureDirectBuffers(i, o);

        if (requiresAligned) {
            int[] newAlignments = new int[]{jfftw_alignment_of(i), jfftw_alignment_of(o)};
            boolean aligned = Arrays.equals(alignment, newAlignments);
            if (!aligned)
                throw new IllegalArgumentException("new array alignment not equal to plan alignment");
        }

        Placement p = i == o ? Placement.IN_PLACE : Placement.OUT_OF_PLACE;
        if (p != placement)
            throw new IllegalArgumentException("requested " + p + " transform for " + placement + " plan");

        jfftw_execute_dft(this, i, o);
    }

    /**
     * Gets the alignment of the input and output buffers
     *
     * @return alignment of input and output buffers
     */
    public final int[] getAlignment() {
        return Arrays.copyOf(alignment, alignment.length);
    }

    /**
     * @return the placement of this plan
     */
    public final Placement getPlacement() {
        return placement;
    }

    /**
     * @return the sign of this plan
     */
    public final int getSign() {
        return sign;
    }

    /**
     * @return the complexity of this plan
     */
    public final Complexity getComplexity() {
        return complexity;
    }

    /**
     * @return the input of this plan
     */
    public final DoubleBuffer getInput() {
        return input;
    }

    /**
     * @return the output of this plan
     */
    public final DoubleBuffer getOutput() {
        return output;
    }

    /**
     * @return the address of this plan
     */
    public final long getAddress() {
        return address;
    }

    /**
     * @return the rank of this plan
     */
    public final int getRank() {
        return rank;
    }

    /**
     * @return copy of this plan's dimensions
     */
    public final int[] getDimensions() {
        return Arrays.copyOf(dimensions, rank);
    }

    /**
     * @return the size of this plan
     */
    public final long getSize() {
        return size;
    }

    /**
     * @return the flags used to create this plan
     */
    public final int getFlags() {
        return flags;
    }

    /**
     * @return string representation of this plan
     */
    public String toString() {
        return destroyed ? "destroyed plan" : jfftw_sprint_plan(this);
    }

    /**
     * @return true if the plan is destroyed and therefore not suitable for execution
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * @return returns true if this plan was constructed without the FFTW_UNALIGNED flag
     */
    public boolean requiresAligned() {
        return requiresAligned;
    }
}
