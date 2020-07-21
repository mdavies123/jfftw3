package jfftw;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static jfftw.Interface.*;

public class Plan {

	protected final Sign sign;
	protected final Complexity complexity;
	protected final Placement placement;
	protected final Split splitInput, splitOutput;
	protected final Interleave interleavedInput, interleavedOutput;
	protected final long address, size;
	protected final int[] dimensions;
	protected final int rank, flags;
	protected final boolean isGuruPlan, isSplitArrayPlan;
	protected boolean destroyed = false;
	
	/**
	 * Constructs a new complex-to-complex plan.
	 * 
	 * @param i input complex interleaved array
	 * @param o output complex interleaved array
	 * @param s sign of the transform
	 * @param f FFTW flags
	 * @param n variable number of dimensions; defaults to `i.size()`
	 */
	public Plan(Complex i, Complex o, Sign s, int f, int... n) {
		this(i, o, s, Complexity.COMPLEX_TO_COMPLEX, f, n);
	}
	
	/**
	 * Constructs a new complex-to-real plan.
	 * 
	 * @param i input complex interleaved array
	 * @param o output real interleaved array
	 * @param s sign of the transform
	 * @param f FFTW flags
	 * @param n variable number of dimensions; defaults to `i.size()`
	 */
	public Plan(Complex i, Real o, Sign s, int f, int... n) {
		this(i, o, s, Complexity.COMPLEX_TO_REAL, f, n);
	}
	
	/**
	 * Constructs a new real-to-complex plan.
	 * 
	 * @param i input real interleaved array
	 * @param o output complex interleaved array
	 * @param s sign of the transform
	 * @param f FFTW flags
	 * @param n variable number of dimensions; defaults to `i.size()`
	 */
	public Plan(Real i, Complex o, Sign s, int f, int... n) {
		this(i, o, s, Complexity.REAL_TO_COMPLEX, f, n);
	}
	
	/**
	 * Constructs a new real-to-real plan.
	 * 
	 * @param i input real interleaved array
	 * @param o output real interleaved array
	 * @param s sign of the transform
	 * @param f FFTW flags
	 * @param n variable number of dimensions; defaults to `i.size()`
	 */
	public Plan(Real i, Real o, Sign s, int f, int... n) {
		this(i, o, s, Complexity.REAL_TO_REAL, f, n);
	}

	private Plan(Interleave i, Interleave o, Sign s, Complexity cplx, int f, int... n) {
		if (n == null || n.length == 0)
			n = new int[] { i.size() };
		interleavedInput = i;
		interleavedOutput = o;
		sign = s;
		complexity = cplx;
		placement = i == o ? Placement.IN_PLACE : Placement.OUT_OF_PLACE;
		rank = n.length;
		dimensions = Arrays.copyOf(n, rank);
		flags = f;
		splitInput = null;
		splitOutput = null;
		isGuruPlan = false;
		isSplitArrayPlan = false;
		long N = 1;
		for (int d : dimensions)
			N *= d;
		size = N;
		validateConfiguration(i.size(), o.size());
		address = create();
		validateAddress();
	}
	
	protected Plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Interleave i, Interleave o, Sign s, Complexity c, int f) {
		interleavedInput = i;
		interleavedOutput = o;
		sign = s;
		complexity = c;
		placement = i == o ? Placement.IN_PLACE : Placement.OUT_OF_PLACE;
		rank = dims.length;
		flags = f;
		isGuruPlan = true;
		isSplitArrayPlan = false;
		splitInput = null;
		splitOutput = null;
		dimensions = new int[rank];
		long N = 1;
		for (int j = 0; j < rank; j++) {
			N *= dims[j].n;
			dimensions[j] = dims[j].n;
		}
		size = N;
		validateConfiguration(i.size(), o.size());
		address = guruCreate(dims, hmDims);
		validateAddress();
	}

	protected Plan(Guru64.Dimension[] dims, Guru64.Dimension[] hmDims, Interleave i, Interleave o, Sign s, Complexity c, int f) {
		interleavedInput = i;
		interleavedOutput = o;
		sign = s;
		complexity = c;
		placement = i == o ? Placement.IN_PLACE : Placement.OUT_OF_PLACE;
		rank = dims.length;
		flags = f;
		isGuruPlan = true;
		isSplitArrayPlan = false;
		splitInput = null;
		splitOutput = null;
		dimensions = new int[rank];
		long N = 1;
		for (int j = 0; j < rank; j++) {
			N *= dims[j].n;
			dimensions[j] = (int) dims[j].n;
		}
		size = N;
		validateConfiguration(i.size(), o.size());
		address = guru64Create(dims, hmDims);
		validateAddress();
	}

	protected Plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Split i, Split o, Complexity c, int f) {
		interleavedInput = null;
		interleavedOutput = null;
		sign = Sign.NEGATIVE;
		complexity = c;
		placement = i == o ? Placement.IN_PLACE : Placement.OUT_OF_PLACE;
		rank = dims.length;
		flags = f;
		isGuruPlan = true;
		isSplitArrayPlan = true;
		splitInput = i;
		splitOutput = o;
		dimensions = new int[rank];
		long N = 1;
		for (int j = 0; j < rank; j++) {
			N *= dims[j].n;
			dimensions[j] = dims[j].n;
		}
		size = N;
		validateConfiguration(i.size(), o.size());
		address = guruSplitCreate(dims, hmDims);
		validateAddress();
	}

	protected Plan(Guru64.Dimension[] dims, Guru64.Dimension[] hmDims, Split i, Split o, Complexity c, int f) {
		interleavedInput = null;
		interleavedOutput = null;
		sign = Sign.NEGATIVE;
		complexity = c;
		placement = i == o ? Placement.IN_PLACE : Placement.OUT_OF_PLACE;
		splitInput = i;
		splitOutput = o;
		rank = dims.length;
		flags = f;
		isGuruPlan = true;
		isSplitArrayPlan = true;
		dimensions = new int[rank];
		long N = 1;
		for (int j = 0; j < rank; j++) {
			N *= dims[j].n;
			dimensions[j] = (int) dims[j].n;
		}
		size = N;
		validateConfiguration(i.size(), o.size());
		address = guru64SplitCreate(dims, hmDims);
		validateAddress();
	}

	private void validateNewArrays(Complexity requestedComplexity, Placement requestedPlacement, int inputSize, int outputSize, boolean isSplit) { 
		if (destroyed)
			throw new NullPointerException("attempted to execute destroyed plan");
		if (complexity != requestedComplexity)
			throw new IllegalArgumentException("requested " + requestedComplexity + " transform for " + complexity + " plan");
		if (inputSize != outputSize)
			throw new IllegalArgumentException("input arrays not equal size");
		if (size != inputSize)
			throw new IllegalArgumentException("requested " + inputSize + " size transform for " + size + " size plan");
		if (requestedPlacement != placement)
			throw new IllegalArgumentException("requested " + requestedPlacement + " transform for " + placement + " plan");
		if (isSplitArrayPlan != isSplit)
			throw new IllegalArgumentException("incorrect array input type");
	}
	
	private void validateConfiguration(int iSize, int oSize) {
		if (iSize != oSize || iSize != size || size <= 0)
			throw new IllegalArgumentException("invalid plan configuration");
	}
	
	private void validateAddress() {
		if (address == 0)
			throw new IllegalArgumentException("plan creation returned null");
	}

	private long create() {
		switch (complexity) {
		case COMPLEX_TO_COMPLEX:
			switch (rank) {
			case 1:
				return jfftw_plan_dft_1d(dimensions[0], (Complex) interleavedInput, (Complex) interleavedOutput, sign.value, flags);
			case 2:
				return jfftw_plan_dft_2d(dimensions[0], dimensions[1], (Complex) interleavedInput, (Complex) interleavedOutput, sign.value, flags);
			case 3:
				return jfftw_plan_dft_3d(dimensions[0], dimensions[1], dimensions[2], (Complex) interleavedInput, (Complex) interleavedOutput, sign.value, flags);
			default:
				return jfftw_plan_dft(rank, dimensions, (Complex) interleavedInput, (Complex) interleavedOutput, sign.value, flags);
			}
		case COMPLEX_TO_REAL:
			switch (rank) {
			case 1:
				return jfftw_plan_dft_c2r_1d(dimensions[0], (Complex) interleavedInput, (Real) interleavedOutput, flags);
			case 2:
				return jfftw_plan_dft_c2r_2d(dimensions[0], dimensions[1], (Complex) interleavedInput, (Real) interleavedOutput, flags);
			case 3:
				return jfftw_plan_dft_c2r_3d(dimensions[0], dimensions[1], dimensions[2], (Complex) interleavedInput, (Real) interleavedOutput, flags);
			default:
				return jfftw_plan_dft_c2r(rank, dimensions, (Complex) interleavedInput, (Real) interleavedOutput, flags);
			}
		case REAL_TO_COMPLEX:
			switch (rank) {
			case 1:
				return jfftw_plan_dft_r2c_1d(dimensions[0], (Real) interleavedInput, (Complex) interleavedOutput, flags);
			case 2:
				return jfftw_plan_dft_r2c_2d(dimensions[0], dimensions[1], (Real) interleavedInput, (Complex) interleavedOutput, flags);
			case 3:
				return jfftw_plan_dft_r2c_3d(dimensions[0], dimensions[1], dimensions[2], (Real) interleavedInput, (Complex) interleavedOutput, flags);
			default:
				return jfftw_plan_dft_r2c(rank, dimensions, (Real) interleavedInput, (Complex) interleavedOutput, flags);
			}
		default:
			throw new UnsupportedOperationException(complexity + " transform not supported");
		}
	}

	private long guruCreate(Guru.Dimension[] dims, Guru.Dimension[] hmDims) {
		switch(complexity) {
			case COMPLEX_TO_COMPLEX:
				return jfftw_plan_guru_dft(rank, dims, hmDims.length, hmDims, (Complex) interleavedInput, (Complex) interleavedOutput, sign.value, flags);
			case COMPLEX_TO_REAL:
				return jfftw_plan_guru_dft_c2r(rank, dims, hmDims.length, hmDims, (Complex) interleavedInput, (Real) interleavedOutput, flags);
			case REAL_TO_COMPLEX:
				return jfftw_plan_guru_dft_r2c(rank, dims, hmDims.length, hmDims, (Real) interleavedInput, (Complex) interleavedOutput, flags);
			default:
				throw new UnsupportedOperationException(complexity + " transform not supported");
		}
	}

	private long guru64Create(Guru64.Dimension[] dims, Guru64.Dimension[] hmDims) {
		switch (complexity) {
			case COMPLEX_TO_COMPLEX:
				return jfftw_plan_guru64_dft(rank, dims, hmDims.length, hmDims, (Complex) interleavedInput, (Complex) interleavedOutput, sign.value, flags);
			case COMPLEX_TO_REAL:
				return jfftw_plan_guru64_dft_c2r(rank, dims, hmDims.length, hmDims, (Complex) interleavedInput, (Real) interleavedOutput, flags);
			case REAL_TO_COMPLEX:
				return jfftw_plan_guru64_dft_r2c(rank, dims, hmDims.length, hmDims, (Real) interleavedInput, (Complex) interleavedOutput, flags);
			default:
				throw new UnsupportedOperationException(complexity + " transform not supported");
		}
	}

	private long guruSplitCreate(Guru.Dimension[] dims, Guru.Dimension[] hmDims) {
		switch (complexity) {
			case COMPLEX_TO_COMPLEX:
				return jfftw_plan_guru_dft_split(rank, dims, hmDims.length, hmDims, splitInput.re, splitInput.im, splitOutput.re, splitOutput.im, flags);
			case COMPLEX_TO_REAL:
				return jfftw_plan_guru_dft_split_c2r(rank, dims, hmDims.length, hmDims, splitInput.re, splitInput.im, splitOutput.re, flags);
			case REAL_TO_COMPLEX:
				return jfftw_plan_guru_dft_split_r2c(rank, dims, hmDims.length, hmDims, splitInput.re, splitOutput.re, splitOutput.im, flags);
			default:
				throw new UnsupportedOperationException(complexity + " transform not supported");
		}
	}

	private long guru64SplitCreate(Guru64.Dimension[] dims, Guru64.Dimension[] hmDims) {
		switch (complexity) {
			case COMPLEX_TO_COMPLEX:
				return jfftw_plan_guru64_dft_split(rank, dims, hmDims.length, hmDims, splitInput.re, splitInput.im, splitOutput.re, splitOutput.im, sign.value, flags);
			case COMPLEX_TO_REAL:
				return jfftw_plan_guru64_dft_split_c2r(rank, dims, hmDims.length, hmDims, splitInput.re, splitInput.im, splitOutput.re, flags);
			case REAL_TO_COMPLEX:
				return jfftw_plan_guru64_dft_split_r2c(rank, dims, hmDims.length, hmDims, splitInput.re, splitOutput.re, splitOutput.im, flags);
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
			if (!destroyed) {
				jfftw_fprint_plan(this, f.getAbsolutePath());
			} else {
				throw new NullPointerException("plan is destroyed");
			}
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
		if (!destroyed)
			jfftw_print_plan(this);
		else
			throw new NullPointerException("plan is destroyed");
	}

	/**
	 * Returns the internal measurement of how effective this plan is.
	 * 
	 * @return cost of this plan
	 */
	public double cost() {
		if (!destroyed)
			return jfftw_cost(this);
		else
			throw new NullPointerException("plan is destroyed");
	}

	/**
	 * Destroys the plan pointed to by this object's address field.
	 */
	public void destroy() {
		if (!destroyed) {
			jfftw_destroy_plan(this);
			destroyed = true;
		} else {
			throw new NullPointerException("plan is destroyed");
		}
	}

	/**
	 * Gives an estimate of how effective this plan is.
	 * 
	 * @return estimated cost of this plan
	 */
	public double estimateCost() {
		if (!destroyed)
			return jfftw_estimate_cost(this);
		else
			throw new NullPointerException("plan is destroyed");
	}

	/**
	 * Executes this plan using the input and output arrays associated with this plan.
	 */
	public void execute() {
		if (!destroyed)
			jfftw_execute(this);
		else
			throw new NullPointerException("plan is destroyed");
	}

	/**
	 * Executes this COMPLEX_TO_COMPLEX plan using new arrays.
	 * 
	 * @param i new complex input array
	 * @param o new complex output array
	 */
	public void execute(Complex i, Complex o) {
		Placement p = i == o ? Placement.IN_PLACE : Placement.OUT_OF_PLACE;
		validateNewArrays(Complexity.COMPLEX_TO_COMPLEX, p, i.size(), o.size(), false);
		jfftw_execute_dft(this, i, o);
	}

	/**
	 * Executes this COMPLEX_TO_REAL plan using new arrays.
	 * 
	 * @param i new complex input array
	 * @param o new real output array
	 */
	public void execute(Complex i, Real o) {
		validateNewArrays(Complexity.COMPLEX_TO_REAL, Placement.OUT_OF_PLACE, i.size(), o.size(), false);
		jfftw_execute_dft_c2r(this, i, o);
	}

	/**
	 * Executes this REAL_TO_COMPLEX plan using new arrays.
	 * 
	 * @param i new real input array
	 * @param o new complex output array
	 */
	public void execute(Real i, Complex o) {
		validateNewArrays(Complexity.REAL_TO_COMPLEX, Placement.OUT_OF_PLACE, i.size(), o.size(), false);
		jfftw_execute_dft_r2c(this, i, o);
	}

	/**
	 * Executes this REAL_TO_REAL plan using new arrays.
	 * 
	 * @param i	new real input array
	 * @param o new real output array
	 */
	public void execute(Real i, Real o) {
		Placement p = i == o ? Placement.IN_PLACE : Placement.OUT_OF_PLACE;
		validateNewArrays(Complexity.REAL_TO_REAL, p, i.size(), o.size(), false);
		jfftw_execute_r2r(this, i, o);
	}
	
	/**
	 * Executes this COMPLEX_TO_COMPLEX split array plan using new arrays.
	 * 
	 * @param i	new complex split array input
	 * @param o new complex split array output
	 */
	public void execute(Split i, Split o) {
		Placement p = i == o ? Placement.IN_PLACE : Placement.OUT_OF_PLACE;
		validateNewArrays(Complexity.COMPLEX_TO_COMPLEX, p, i.size(), o.size(), true);
		jfftw_execute_split_dft(this, i.re, i.im, o.re, o.im);
	}
	
	/**
	 * Executes this COMPLEX_TO_REAL split array plan using new arrays.
	 * 
	 * @param i	new complex split array input
	 * @param o	new real array output
	 */
	public void execute(Split i, Real o) {
		validateNewArrays(Complexity.COMPLEX_TO_REAL, Placement.OUT_OF_PLACE, i.size(), o.size(), true);
		jfftw_execute_split_dft_c2r(this, i.re, i.im, o.buff);
	}
	
	/**
	 * Executes this REAL_TO_COMPLEX split array plan using new arrays.
	 * 
	 * @param i	new real array input
	 * @param o	new complex split array output
	 */
	public void execute(Real i, Split o) {
		validateNewArrays(Complexity.REAL_TO_COMPLEX, Placement.OUT_OF_PLACE, i.size(), o.size(), true);
		jfftw_execute_split_dft_r2c(this, i.buff, o.re, o.im);
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
	public final Sign getSign() {
		return sign;
	}

	/**
	 * @return the complexity of this plan
	 */
	public final Complexity getComplexity() {
		return complexity;
	}

	/**
	 * @return the complex input of this plan
	 */
	public final Complex getComplexInput() {
		if (isSplitArrayPlan)
			throw new UnsupportedOperationException("plan not made for split array inputs");
		if (interleavedInput instanceof Complex)
			return (Complex) interleavedInput;
		else
			throw new ClassCastException("input is not complex");
	}
	
	/**
	 * @return the real input of this plan
	 */
	public final Real getRealInput() {
		if (isSplitArrayPlan) {
			if (complexity != Complexity.REAL_TO_COMPLEX || complexity != Complexity.REAL_TO_REAL) {
				throw new UnsupportedOperationException("plan not made for real-only inputs");
			} else {
				return new Real(splitInput.re);
			}
		}
		if (interleavedInput instanceof Real)
			return (Real) interleavedInput;
		else
			throw new ClassCastException("input is not real");
	}

	/**
	 * @return the complex output of this plan
	 */
	public final Complex getComplexOutput() {
		if (isSplitArrayPlan)
			throw new UnsupportedOperationException("plan not made for split array outputs");
		if (interleavedOutput instanceof Complex)
			return (Complex) interleavedOutput;
		else
			throw new ClassCastException("output is not complex");
	}
	
	/**
	 * @return the real output of this plan
	 */
	public final Real getRealOutput() {
		if (isSplitArrayPlan) {
			if (complexity != Complexity.COMPLEX_TO_REAL || complexity != Complexity.REAL_TO_REAL) {
				throw new UnsupportedOperationException("plan not made for real-only outputs");
			} else {
				return new Real(splitOutput.re);
			}
		}
		if (interleavedOutput instanceof Real)
			return (Real) interleavedOutput;
		else
			throw new ClassCastException("output is not real");
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
	 * @return	split input arrays, throws an UnsupportedOperationException if this plan is not a split array plan
	 */
	public final Split getSplitInput() {
		if (isSplitArrayPlan) {
			if (complexity == Complexity.COMPLEX_TO_COMPLEX || complexity == Complexity.COMPLEX_TO_REAL) {
				return splitInput;
			}
			throw new UnsupportedOperationException("plan not made for complex split array inputs");
		} else {
			throw new UnsupportedOperationException("plan not made for split arrays");
		}
	}

	/**
	 * @return	split output arrays, throws an UnsupportedOperationException if this plan is not a split array plan
	 */
	public final Split getSplitOutput() {
		if (isSplitArrayPlan) {
			if (complexity == Complexity.COMPLEX_TO_COMPLEX || complexity == Complexity.REAL_TO_COMPLEX) {
				return splitOutput;
			}
			throw new UnsupportedOperationException("plan not made for complex split array outputs");
		} else {
			throw new UnsupportedOperationException("plan not made for split arrays");
		}
	}

	/**
	 * @return the flags used to create this plan
	 */
	public final int getFlags() {
		return flags;
	}

	/**
	 * Returns a string representation of this plan.
	 */
	public String toString() {
		if (!destroyed)
			return jfftw_sprint_plan(this);
		else
			return "destroyed plan";
	}
	
	/**
	 * @return true if the plan is destroyed and therefore invalid, false otherwise
	 */
	public boolean isDestroyed() {
		return destroyed;
	}

	/**
	 * @return true if the plan was created using the Guru interface
	 */
	public boolean isGuruPlan() {
		return false;
	}

	/**
	 * @return true if the plan was created using the split array Guru interface
	 */
	public boolean isSplit() {
		return this.splitInput != null || this.splitOutput != null;
	}

	public int hashCode() {
		return Objects.hash(address);
	}

	/**
	 * Enumeration representing the placement of a plan.
	 */
	public enum Placement {
		IN_PLACE, OUT_OF_PLACE;
	}

	/**
	 * Enumeration representing the type of complexity of a plan.
	 */
	public enum Complexity {
		COMPLEX_TO_COMPLEX, COMPLEX_TO_REAL, REAL_TO_COMPLEX, REAL_TO_REAL
	}

}
