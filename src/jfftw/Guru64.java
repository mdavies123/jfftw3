package jfftw;

public class Guru64 {

    /**
     * Combines the input dimension size, input strides, and output strides arrays into an array of Guru64.Dimension
     * objects for use in the Guru64 interface.
     *
     * @param n     array of dimension sizes
     * @param is    array of input strides
     * @param os    array of output strides
     * @return      Guru64.Dimension[] used in the Guru64 interface
     */
    public static Dimension[] makeDimensions(long[] n, long[] is, long[] os) {
        int howMany = n.length;
        if (howMany != is.length || howMany != os.length)
            throw new IllegalArgumentException("inputs are not the same length");
        Dimension[] dims = new Dimension[howMany];
        for (int i = 0; i < howMany; i++)
            dims[i] = new Dimension(n[i], is[i], os[i]);
        return dims;
    }

    /**
     * Creates a new Plan using the Guru64 interface.
     *
     * @param dims		array of transform dimensions
     * @param hmDims	array of vector dimensions
     * @param i			complex or real input
     * @param o			complex or real output
     * @param s			sign (ignored if the transform is C2R or R2C)
     * @param f			flags
     * @return          a new Plan
     */
    public static Plan plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Value i, Value o, Sign s, int f) {
        return new Plan(dims, hmDims, i, o, s, f);
    }

    /**
     * Creates a new Plan using the Guru64 interface and split arrays.
     *
     * @param dims		array of transform dimensions
     * @param hmDims	array of vector dimensions
     * @param i	    	complex or real split array input
     * @param o 		complex or real split array output
     * @param s			sign (ignored if the transform is C2R or R2C)
     * @param f			flags
     * @return          a new Plan
     */
    public static Plan plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, SplitArray i, SplitArray o, Sign s, int f) {
        return new Plan(dims, hmDims, i, o, s, f);
    }

    /**
     * The fftw_iodim64 type is similar to fftw_iodim, with the same interpretation, except that it uses type ptrdiff_t
     * instead of type int. This class implements that data structure.
     */
    public static class Dimension {
        public final long n, is, os;
        protected Dimension(long n, long is, long os) {
            this.n = n;
            this.is = is;
            this.os = os;
        }
    }
}
