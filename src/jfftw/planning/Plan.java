package jfftw.planning;

import jfftw.data.Alignment;
import jfftw.data.Dimensions;
import jfftw.enums.Complexity;
import jfftw.enums.Flag;

public abstract class Plan<T> {

    protected final T input, output;
    protected final int sign, size, rank, flags;
    protected final Complexity complexity;
    protected final Placement placement;
    protected final long address;
    protected final Dimensions dimensions;
    protected final Alignment iAlign, oAlign;
    protected final boolean requiresAligned;
    protected boolean destroyed = false;

    protected static synchronized native double jfftw_cost(Plan<?> p);
    protected static synchronized native void jfftw_destroy_plan(Plan<?> p);
    protected static synchronized native double jfftw_estimate_cost(Plan<?> p);
    protected static native void jfftw_execute(Plan<?> p);
    protected static synchronized native void jfftw_print_plan(Plan<?> p);
    protected static synchronized native String jfftw_sprint_plan(Plan<?> p);

    protected Plan(T i, T o, int is, int os, int s, Complexity c, int f, Dimensions d, Placement p, Alignment ia, Alignment oa) {

        input = i;
        output = o;
        sign = s;
        complexity = c;
        flags = f;
        placement = p;

        if (d == null || d.size() == 0) {
            if (complexity == Complexity.COMPLEX_TO_COMPLEX || complexity == Complexity.COMPLEX_TO_REAL)
                d = new Dimensions(is / 2);
            else
                d = new Dimensions(is);
        }

        rank = d.size();
        dimensions = new Dimensions(d);

        int N = 1;
        for (int n : dimensions.get())
            N *= n;
        size = N;

        iAlign = new Alignment(ia);
        oAlign = new Alignment(oa);
        requiresAligned = (flags & Flag.UNALIGNED.value) == 0;

        ensureSizes(is, os);
        address = create();
        if (address == 0)
            throw new NullPointerException("plan creation returned null");
    }

    protected abstract long create();

    protected final void ensureSizes(int iSize, int oSize) {
        switch (complexity) {
            case COMPLEX_TO_COMPLEX: {
                if (iSize != oSize)
                    throw new IllegalArgumentException("input and output not equal size");
                if ((iSize / 2) != size)
                    throw new IllegalArgumentException("transform size not equal to dimensions size");
            } break;
            case COMPLEX_TO_REAL: {
                if (iSize / 2 != oSize)
                    throw new IllegalArgumentException("input and output not compatible size");
                if ((iSize / 2) != size)
                    throw new IllegalArgumentException("transform size not equal to dimensions size");
            } break;
            case REAL_TO_COMPLEX: {
                if (iSize != (oSize / 2))
                    throw new IllegalArgumentException("input and output not compatible size");
                if (iSize != size)
                    throw new IllegalArgumentException("transform size not equal to dimensions size");
            } break;
            case REAL_TO_REAL: {
                if (iSize != oSize)
                    throw new IllegalArgumentException("input and output not equal size");
                if (iSize != size)
                    throw new IllegalArgumentException("transform size not equal to dimensions size");
            } break;
            default: throw new UnsupportedOperationException(complexity + " transform not supported");
        }
    }

    protected final void ensureNotDestroyed() {
        if (destroyed)
            throw new NullPointerException("plan is destroyed");
    }

    protected final void ensureAlignment(Alignment in, Alignment out) {
        if (!in.equals(iAlign) || !out.equals(oAlign))
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
     * Get a copy of the input buffer alignment
     *
     * @return copy of input alignment
     */
    public final Alignment getInputAlignment() {
        return new Alignment(iAlign);
    }

    /**
     * Get a copy of the output buffer alignment
     *
     * @return copy of output alignment
     */
    public final Alignment getOutputAlignment() {
        return new Alignment(oAlign);
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
    public final Dimensions getDimensions() {
        return new Dimensions(dimensions);
    }

    /**
     * @return linear size of this plan
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
