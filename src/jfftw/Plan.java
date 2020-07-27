package jfftw;

import java.util.Arrays;

import static jfftw.Interface.*;

abstract class Plan<T> {

    protected final T input, output;
    protected final int sign, size, rank, flags;
    protected final Complexity complexity;
    protected final Placement placement;
    protected final long address;
    protected final int[] dimensions, alignment;
    protected final boolean requiresAligned;
    protected boolean destroyed = false;

    protected Plan(T i, T o, int iSize, int oSize, int s, Complexity cplx, int f, int[] dims, Placement plc, int[] align) {

        input = i;
        output = o;
        sign = s;
        complexity = cplx;
        flags = f;
        placement = plc;

        if (dims == null || dims.length == 0) {
            if (complexity == Complexity.COMPLEX_TO_COMPLEX || complexity == Complexity.COMPLEX_TO_REAL)
                dims = new int[]{iSize / 2};
            else
                dims = new int[]{iSize};
        }

        rank = dims.length;
        dimensions = Arrays.copyOf(dims, rank);

        int N = 1;
        for (int d : dimensions)
            N *= d;
        size = N;

        alignment = Arrays.copyOf(align, align.length);
        requiresAligned = (flags & Flag.UNALIGNED.value) == 0;

        ensureSizes(iSize, oSize);
        address = create();
        if (address == 0)
            throw new NullPointerException("plan creation returned null");
    }

    protected final long create() {
        return switch (complexity) {
            case COMPLEX_TO_COMPLEX -> switch (rank) {
                case 1 -> jfftw_plan_dft_1d(dimensions[0], input, output, sign, flags);
                case 2 -> jfftw_plan_dft_2d(dimensions[0], dimensions[1], input, output, sign, flags);
                case 3 -> jfftw_plan_dft_3d(dimensions[0], dimensions[1], dimensions[2], input, output, sign, flags);
                default -> jfftw_plan_dft(rank, dimensions, input, output, sign, flags);
            };
            case COMPLEX_TO_REAL -> switch (rank) {
                case 1 -> jfftw_plan_dft_c2r_1d(dimensions[0], input, output, flags);
                case 2 -> jfftw_plan_dft_c2r_2d(dimensions[0], dimensions[1], input, output, flags);
                case 3 -> jfftw_plan_dft_c2r_3d(dimensions[0], dimensions[1], dimensions[2], input, output, flags);
                default -> jfftw_plan_dft_c2r(rank, dimensions, input, output, flags);
            };
            case REAL_TO_COMPLEX -> switch (rank) {
                case 1 -> jfftw_plan_dft_r2c_1d(dimensions[0], input, output, flags);
                case 2 -> jfftw_plan_dft_r2c_2d(dimensions[0], dimensions[1], input, output, flags);
                case 3 -> jfftw_plan_dft_r2c_3d(dimensions[0], dimensions[1], dimensions[2], input, output, flags);
                default -> jfftw_plan_dft_r2c(rank, dimensions, input, output, flags);
            };
            default -> throw new UnsupportedOperationException(complexity + " transform not supported");
        };
    }

    protected final void ensureSizes(int iSize, int oSize) {
        switch (complexity) {
            case COMPLEX_TO_COMPLEX -> {
                if (iSize != oSize)
                    throw new IllegalArgumentException("input and output not equal size");
                if ((iSize / 2) != size)
                    throw new IllegalArgumentException("transform size not equal to dimensions size");
            }
            case COMPLEX_TO_REAL -> {
                if (iSize / 2 != oSize)
                    throw new IllegalArgumentException("input and output not compatible size");
                if ((iSize / 2) != size)
                    throw new IllegalArgumentException("transform size not equal to dimensions size");
            }
            case REAL_TO_COMPLEX -> {
                if (iSize != (oSize / 2))
                    throw new IllegalArgumentException("input and output not compatible size");
                if (iSize != size)
                    throw new IllegalArgumentException("transform size not equal to dimensions size");
            }
            case REAL_TO_REAL -> {
                if (iSize != oSize)
                    throw new IllegalArgumentException("input and output not equal size");
                if (iSize != size)
                    throw new IllegalArgumentException("transform size not equal to dimensions size");
            }
        }
    }

    protected final void ensureNotDestroyed() {
        if (destroyed)
            throw new NullPointerException("plan is destroyed");
    }

    protected final void ensureAlignment(int iAlign, int oAlign) {
        if (iAlign != alignment[0] || oAlign != alignment[1])
            throw new IllegalArgumentException("new array alignment not equal to plan alignment");
    }

    protected final void ensurePlacement(Placement p) {
        if (p != placement)
            throw new IllegalArgumentException("mismatched placement: expected " + placement + "; received " + p);
    }

    /**
     * @return  input supplied upon plan creation
     */
    public final T getInput() {
        return input;
    }

    /**
     * @return  output supplied upon plan creation
     */
    public final T getOutput() {
        return output;
    }

    /**
     * Prints this plan to stdout.
     * <p>
     * Since this method is implemented in native code, it cannot guarantee the output will be as expected.
     */
    public final void print() {
        ensureNotDestroyed();
        jfftw_print_plan(this);
    }

    /**
     * Returns the internal measurement of how effective this plan is.
     *
     * @return cost of this plan
     */
    public final double cost() {
        ensureNotDestroyed();
        return jfftw_cost(this);
    }

    /**
     * Destroys the plan pointed to by this object's address field.
     */
    public final void destroy() {
        ensureNotDestroyed();
        jfftw_destroy_plan(this);
        destroyed = true;
    }

    /**
     * Gives an estimate of how effective this plan is.
     *
     * @return estimated cost of this plan
     */
    public final double estimateCost() {
        ensureNotDestroyed();
        return jfftw_estimate_cost(this);
    }

    /**
     * Executes this plan using the input and output arrays associated with this plan.
     */
    public final void execute() {
        ensureNotDestroyed();
        jfftw_execute(this);
    }

    /**
     * Executes this plan with new arrays or buffers.
     *
     * @param i input
     * @param o output
     */
    public abstract void execute(T i, T o);

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
    public final String toString() {
        return destroyed ? "destroyed plan" : jfftw_sprint_plan(this);
    }

    /**
     * @return true if the plan is destroyed and therefore not suitable for execution
     */
    public final boolean isDestroyed() {
        return destroyed;
    }

    /**
     * @return returns true if this plan was constructed without Flag.UNALIGNED
     */
    public final boolean requiresAligned() {
        return requiresAligned;
    }

}
