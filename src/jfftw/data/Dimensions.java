package jfftw.data;

import java.util.Arrays;

public class Dimensions {

    protected final int[] dimensions;

    /**
     * Construct a transform dimension
     *
     * @param dims dimensions
     */
    public Dimensions(int... dims) {
        dimensions = Arrays.copyOf(dims, dims.length);
    }

    /**
     * Copy constructor
     *
     * @param d object to copy from
     */
    public Dimensions(Dimensions d) {
        this(d.dimensions);
    }

    /**
     * @return copy of dimensions array
     */
    public int[] get() {
        return Arrays.copyOf(dimensions, dimensions.length);
    }

    /**
     * @return number of dimensions
     */
    public int size() {
        return dimensions.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimensions dims = (Dimensions) o;
        return Arrays.equals(this.dimensions, dims.dimensions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(dimensions);
    }
}
