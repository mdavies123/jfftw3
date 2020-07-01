package jfftw;

public class Guru {

    /**
     * Combines the input dimension size, input strides, and output strides arrays into an array of Guru.Dimension
     * objects for use in the Guru interface.
     *
     * @param n     array of dimension sizes
     * @param is    array of input strides
     * @param os    array of output strides
     * @return      Guru.Dimension[] used in the Guru interface
     */
    public static Dimension[] makeDimensions(int[] n, int[] is, int[] os) {
        int howMany = n.length;
        if (howMany != is.length || howMany != os.length)
            throw new IllegalArgumentException("inputs are not the same length");
        Dimension[] dims = new Dimension[howMany];
        for (int i = 0; i < howMany; i++)
            dims[i] = new Dimension(n[i], is[i], os[i]);
        return dims;
    }

    /**
     * Creates a new Plan using the Guru interface.
     *
     * @param dims		array of transform dimensions
     * @param hmDims	array of vector dimensions
     * @param i			complex or real input
     * @param o			complex or real output
     * @param s			sign (ignored if the transform is C2R or R2C)
     * @param f			flags
     * @return          a new Plan
     */
    public static Plan plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Interleave i, Interleave o, Sign s, int f) {
        return new Plan(dims, hmDims, i, o, s, f);
    }

    /**
     * Creates a new Plan using the Guru interface and split arrays.
     *
     * @param dims		array of transform dimensions
     * @param hmDims	array of vector dimensions
     * @param i	    	complex or real split array input
     * @param o 		complex or real split array output
     * @param s			sign (ignored if the transform is C2R or R2C)
     * @param f			flags
     * @return          a new Plan
     */
    public static Plan plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Split i, Split o, int f) {
        return new Plan(dims, hmDims, i, o, f);
    }

    /**
     * The guru interface introduces one basic new data structure, fftw_iodim, that is used to specify sizes and strides
     * for multi-dimensional transforms and vectors. This subclass implements that new data structure.
     */
    public static class Dimension {
        public final int n, is, os;
        protected Dimension(int n, int is, int os) {
            this.n = n;
            this.is = is;
            this.os = os;
        }
    }

}
