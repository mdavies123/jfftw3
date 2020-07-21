package jfftw;

import jfftw.Plan.Complexity;

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
     * Creates a new complex-to-complex Plan using the Guru interface.
     *
     * @param dims		array of transform dimensions
     * @param hmDims	array of vector dimensions
     * @param i			complex interleaved input
     * @param o			complex interleaved output
     * @param s			sign (ignored if the transform is C2R or R2C)
     * @param f			flags
     * @return          a new Plan
     */
    public static Plan plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Complex i, Complex o, Sign s, int f) {
        return new Plan(dims, hmDims, i, o, s, Complexity.COMPLEX_TO_COMPLEX, f);
    }
    
    /**
     * Creates a new complex-to-real Plan using the Guru interface.
     *
     * @param dims		array of transform dimensions
     * @param hmDims	array of vector dimensions
     * @param i			complex interleaved input
     * @param o			real interleaved output
     * @param s			sign (ignored if the transform is C2R or R2C)
     * @param f			flags
     * @return          a new Plan
     */
    public static Plan plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Complex i, Real o, Sign s, int f) {
        return new Plan(dims, hmDims, i, o, s, Complexity.COMPLEX_TO_COMPLEX, f);
    }
    
    /**
     * Creates a new real-to-complex Plan using the Guru interface.
     *
     * @param dims		array of transform dimensions
     * @param hmDims	array of vector dimensions
     * @param i			real interleaved input
     * @param o			complex interleaved output
     * @param s			sign (ignored if the transform is C2R or R2C)
     * @param f			flags
     * @return          a new Plan
     */
    public static Plan plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Real i, Complex o, Sign s, int f) {
        return new Plan(dims, hmDims, i, o, s, Complexity.COMPLEX_TO_COMPLEX, f);
    }
    
    /**
     * Creates a new real-to-real Plan using the Guru interface.
     *
     * @param dims		array of transform dimensions
     * @param hmDims	array of vector dimensions
     * @param i			real interleaved input
     * @param o			real interleaved output
     * @param s			sign (ignored if the transform is C2R or R2C)
     * @param f			flags
     * @return          a new Plan
     */
    public static Plan plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Real i, Real o, Sign s, int f) {
        return new Plan(dims, hmDims, i, o, s, Complexity.COMPLEX_TO_COMPLEX, f);
    }

    /**
     * Creates a new complex-to-complex Plan using the Guru interface and split arrays.
     *
     * @param dims		array of transform dimensions
     * @param hmDims	array of vector dimensions
     * @param i	    	complex split array input
     * @param o 		complex split array output
     * @param f			flags
     * @return          a new Plan
     */
    public static Plan plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Split i, Split o, int f) {
        return new Plan(dims, hmDims, i, o, Complexity.COMPLEX_TO_COMPLEX, f);
    }
    
    /**
     * Creates a new complex-to-real Plan using the Guru interface and split arrays.
     *
     * @param dims		array of transform dimensions
     * @param hmDims	array of vector dimensions
     * @param i	    	complex split array input
     * @param o 		real array output
     * @param f			flags
     * @return          a new Plan
     */
    public static Plan plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Split i, Real o, int f) {
    	Split ro = new Split(o.buff, null);
        return new Plan(dims, hmDims, i, ro, Complexity.COMPLEX_TO_REAL, f);
    }
    
    /**
     * Creates a new real-to-complex Plan using the Guru interface and split arrays.
     *
     * @param dims		array of transform dimensions
     * @param hmDims	array of vector dimensions
     * @param i	    	real array input
     * @param o 		complex split array output
     * @param f			flags
     * @return          a new Plan
     */
    public static Plan plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Real i, Split o, int f) {
    	Split ri = new Split(i.buff, null);
        return new Plan(dims, hmDims, ri, o, Complexity.COMPLEX_TO_COMPLEX, f);
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
