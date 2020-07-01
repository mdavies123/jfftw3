package jfftw;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static jfftw.Interface.*;

public class Plan {

	protected final Sign sign;
	protected final Complexity complexity;
	protected final Placement placement;
	protected final SplitArray splitInput, splitOutput;
	protected final Value input, output;
	protected final long address, size;
	protected final int[] dimensions;
	protected final int rank, flags;
	protected final boolean isGuruPlan, isSplitArrayPlan;
	protected boolean destroyed = false;

	/**
	 * Constructs a new plan.
	 * 
	 * @param i input complex or real array
	 * @param o output complex or real array
	 * @param s sign of the transform
	 * @param f FFTW flags
	 * @param n variable number of dimensions
	 */
	public Plan(Value i, Value o, Sign s, int f, int... n) {
		input = i;
		output = o;
		sign = s;
		complexity = determineComplexity(i, o);
		placement = determinePlacement(i, o);
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
		if (input.size() == size && output.size() == size && size > 0)
			address = create();
		else
			throw new IllegalArgumentException("invalid plan configuration");
		if (address == 0)
			throw new NullPointerException("plan creation returned null");
	}

	/**
	 * Special constructor for use in the Guru interface.
	 *
	 * @param dims		array of transform dimensions
	 * @param hmDims	array of vector dimensions
	 * @param i			complex or real input
	 * @param o			complex or real output
	 * @param s			sign (ignored if the transform is C2R or R2C)
	 * @param f			flags
	 */
	protected Plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, Value i, Value o, Sign s, int f) {
		input = i;
		output = o;
		sign = s;
		complexity = determineComplexity(i, o);
		placement = determinePlacement(i, o);
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
		if (input.size() == size && output.size() == size && size > 0)
			address = guruCreate(dims, hmDims);
		else
			throw new IllegalArgumentException("invalid plan configuration");
		if (address == 0)
			throw new NullPointerException("plan creation returned null");
	}

	/**
	 * Special constructor for use in the Guru64 interface.
	 *
	 * @param dims		array of transform dimensions
	 * @param hmDims	array of vector dimensions
	 * @param i			complex or real input
	 * @param o			complex or real output
	 * @param s			sign (ignored if the transform is C2R or R2C)
	 * @param f			flags
	 */
	protected Plan(Guru64.Dimension[] dims, Guru64.Dimension[] hmDims, Value i, Value o, Sign s, int f) {
		input = i;
		output = o;
		sign = s;
		complexity = determineComplexity(i, o);
		placement = determinePlacement(i, o);
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
		if (input.size() == size && output.size() == size && size > 0)
			address = guru64Create(dims, hmDims);
		else
			throw new IllegalArgumentException("invalid plan configuration");
		if (address == 0)
			throw new NullPointerException("plan creation returned null");
	}

	/**
	 * Special constructor for use in the Guru interface.
	 *
	 * @param dims		array of transform dimensions
	 * @param hmDims	array of vector dimensions
	 * @param in		complex or real split array input
	 * @param out		complex or real split array output
	 * @param s			sign (ignored if the transform is C2R or R2C)
	 * @param f			flags
	 */
	protected Plan(Guru.Dimension[] dims, Guru.Dimension[] hmDims, SplitArray in, SplitArray out, Sign s, int f) {
		input = null;
		output = null;
		sign = s;
		complexity = determineSplitComplexity(in, out);
		placement = in == out ? Placement.IN_PLACE : Placement.OUT_OF_PLACE;
		rank = dims.length;
		flags = f;
		isGuruPlan = true;
		isSplitArrayPlan = true;
		splitInput = in;
		splitOutput = out;
		dimensions = new int[rank];
		long N = 1;
		for (int j = 0; j < rank; j++) {
			N *= dims[j].n;
			dimensions[j] = dims[j].n;
		}
		size = N;
		address = guruSplitCreate(dims, hmDims);
		if (address == 0)
			throw new NullPointerException("plan creation returned null");
	}

	/**
	 * Special constructor for use in the Guru64 interface.
	 *
	 * @param dims		array of transform dimensions
	 * @param hmDims	array of vector dimensions
	 * @param in		complex or real split array input
	 * @param out		complex or real split array output
	 * @param s			sign (ignored if the transform is C2R or R2C)
	 * @param f			flags
	 */
	protected Plan(Guru64.Dimension[] dims, Guru64.Dimension[] hmDims, SplitArray in, SplitArray out, Sign s, int f) {
		input = null;
		output = null;
		sign = s;
		complexity = determineSplitComplexity(in, out);
		placement = in == out ? Placement.IN_PLACE : Placement.OUT_OF_PLACE;
		rank = dims.length;
		flags = f;
		isGuruPlan = true;
		isSplitArrayPlan = true;
		splitInput = in;
		splitOutput = out;
		dimensions = new int[rank];
		long N = 1;
		for (int j = 0; j < rank; j++) {
			N *= dims[j].n;
			dimensions[j] = (int) dims[j].n;
		}
		size = N;
		address = guru64SplitCreate(dims, hmDims);
		if (address == 0)
			throw new NullPointerException("plan creation returned null");
	}

	/**
	 * Determines the C2C, C2R, R2C, or R2R complexity of two split arrays.
	 *
	 * @param i	input
	 * @param o	output
	 * @return	the complexity of the input and output plans
	 */
	private static Complexity determineSplitComplexity(SplitArray i, SplitArray o) {
		if (i.im != null) {
			if (o.im != null) {
				return Complexity.COMPLEX_TO_COMPLEX;
			} else {
				return Complexity.COMPLEX_TO_REAL;
			}
		} else {
			if (o.im != null) {
				return Complexity.REAL_TO_COMPLEX;
			}
		}
		return Complexity.REAL_TO_REAL;
	}

	/**
	 * Determines the placement of two interleaved arrays.
	 *
	 * @param input		input
	 * @param output	output
	 * @return			the placement of the two arrays
	 */
	private static Placement determinePlacement(Value input, Value output) {
		if (input == output) // intentionally compare the address of these Values
			return Placement.IN_PLACE;
		return Placement.OUT_OF_PLACE;
	}

	/**
	 * Determines the complexity of two interleaved arrays.
	 * @param input		input
	 * @param output	output
	 * @return			the complexity of the two arrays
	 */
	private static Complexity determineComplexity(Value input, Value output) {
		if (input instanceof Complex) {
			if (output instanceof Complex) {
				return Complexity.COMPLEX_TO_COMPLEX;
			} else { 
				return Complexity.COMPLEX_TO_REAL;
			}
		} else if (output instanceof Complex) {
			return Complexity.REAL_TO_COMPLEX;
		}
		return Complexity.REAL_TO_REAL;
	}

	/**
	 * Validates a new array execute function before any native code is run.
	 *
	 * @param t	type
	 * @param i	interleaved input
	 * @param o	interleaved output
	 */
	private void validate(Complexity t, Value i, Value o) {
		if (destroyed)
			throw new NullPointerException("attempted to execute destroyed plan");
		int in = i.size(), out = o.size();
		if (complexity != t)
			throw new IllegalArgumentException("requested " + t + " transform for " + complexity + " plan");
		if (in != out)
			throw new IllegalArgumentException("input arrays not equal size");
		if (size != in)
			throw new IllegalArgumentException("requested " + in + " size transform for " + size + " size plan");
		if (i == o) {
			if (placement == Placement.OUT_OF_PLACE) {
				throw new IllegalArgumentException("requested in-place transform for out-of-place plan");
			}
		} else if (placement == Placement.IN_PLACE) {
			throw new IllegalArgumentException("requested out-of-place transform for in-place plan");
		}
	}

	/**
	 * Creates a new fftw_plan and returns its address.
	 *
	 * @return	the address of a newly created fftw_plan
	 */
	private long create() {
		switch (complexity) {
		case COMPLEX_TO_COMPLEX:
			switch (rank) {
			case 1:
				return jfftw_plan_dft_1d(dimensions[0], (Complex) input, (Complex) output, sign.value, flags);
			case 2:
				return jfftw_plan_dft_2d(dimensions[0], dimensions[1], (Complex) input, (Complex) output, sign.value, flags);
			case 3:
				return jfftw_plan_dft_3d(dimensions[0], dimensions[1], dimensions[2], (Complex) input, (Complex) output, sign.value, flags);
			default:
				return jfftw_plan_dft(rank, dimensions, (Complex) input, (Complex) output, sign.value, flags);
			}
		case COMPLEX_TO_REAL:
			switch (rank) {
			case 1:
				return jfftw_plan_dft_c2r_1d(dimensions[0], (Complex) input, (Real) output, flags);
			case 2:
				return jfftw_plan_dft_c2r_2d(dimensions[0], dimensions[1], (Complex) input, (Real) output, flags);
			case 3:
				return jfftw_plan_dft_c2r_3d(dimensions[0], dimensions[1], dimensions[2], (Complex) input, (Real) output, flags);
			default:
				return jfftw_plan_dft_c2r(rank, dimensions, (Complex) input, (Real) output, flags);
			}
		case REAL_TO_COMPLEX:
			switch (rank) {
			case 1:
				return jfftw_plan_dft_r2c_1d(dimensions[0], (Real) input, (Complex) output, flags);
			case 2:
				return jfftw_plan_dft_r2c_2d(dimensions[0], dimensions[1], (Real) input, (Complex) output, flags);
			case 3:
				return jfftw_plan_dft_r2c_3d(dimensions[0], dimensions[1], dimensions[2], (Real) input, (Complex) output, flags);
			default:
				return jfftw_plan_dft_r2c(rank, dimensions, (Real) input, (Complex) output, flags);
			}
		default:
			throw new UnsupportedOperationException(complexity + " transform not supported");
		}
	}

	/**
	 * Creates and returns a newly created fftw_plan using the Guru interface.
	 *
	 * @param dims   	transform dimensions
	 * @param hmDims	vector dimensions
	 * @return			address of a new fftw_plan
	 */
	private long guruCreate(Guru.Dimension[] dims, Guru.Dimension[] hmDims) {
		switch(complexity) {
			case COMPLEX_TO_COMPLEX:
				return jfftw_plan_guru_dft(rank, dims, hmDims.length, hmDims, (Complex) input, (Complex) output, sign.value, flags);
			case COMPLEX_TO_REAL:
				return jfftw_plan_guru_dft_c2r(rank, dims, hmDims.length, hmDims, (Complex) input, (Real) output, flags);
			case REAL_TO_COMPLEX:
				return jfftw_plan_guru_dft_r2c(rank, dims, hmDims.length, hmDims, (Real) input, (Complex) output, flags);
			default:
				throw new UnsupportedOperationException(complexity + " transform not supported");
		}
	}

	/**
	 * Creates and returns a newly created fftw_plan using the Guru64 interface.
	 *
	 * @param dims   	transform dimensions
	 * @param hmDims	vector dimensions
	 * @return			address of a new fftw_plan
	 */
	private long guru64Create(Guru64.Dimension[] dims, Guru64.Dimension[] hmDims) {
		switch (complexity) {
			case COMPLEX_TO_COMPLEX:
				return jfftw_plan_guru64_dft(rank, dims, hmDims.length, hmDims, (Complex) input, (Complex) output, sign.value, flags);
			case COMPLEX_TO_REAL:
				return jfftw_plan_guru64_dft_c2r(rank, dims, hmDims.length, hmDims, (Complex) input, (Real) output, flags);
			case REAL_TO_COMPLEX:
				return jfftw_plan_guru64_dft_r2c(rank, dims, hmDims.length, hmDims, (Real) input, (Complex) output, flags);
			default:
				throw new UnsupportedOperationException(complexity + " transform not supported");
		}
	}

	/**
	 * Creates and returns a newly created fftw_plan using the Guru interface and split arrays.
	 *
	 * @param dims   	transform dimensions
	 * @param hmDims	vector dimensions
	 * @return			address of a new fftw_plan
	 */
	private long guruSplitCreate(Guru.Dimension[] dims, Guru.Dimension[] hmDims) {
		switch (complexity) {
			case COMPLEX_TO_COMPLEX:
				return jfftw_plan_guru_dft_split(rank, dims, hmDims.length, hmDims, splitInput.re, splitInput.im, splitOutput.re, splitOutput.im, sign.value, flags);
			case COMPLEX_TO_REAL:
				return jfftw_plan_guru_dft_split_c2r(rank, dims, hmDims.length, hmDims, splitInput.re, splitInput.im, splitOutput.re, flags);
			case REAL_TO_COMPLEX:
				return jfftw_plan_guru_dft_split_r2c(rank, dims, hmDims.length, hmDims, splitInput.re, splitOutput.re, splitOutput.im, flags);
			default:
				throw new UnsupportedOperationException(complexity + " transform not supported");
		}
	}

	/**
	 * Creates and returns a newly created fftw_plan using the Guru64 interface and split arrays.
	 *
	 * @param dims   	transform dimensions
	 * @param hmDims	vector dimensions
	 * @return			address of a new fftw_plan
	 */
	private long guru64SplitCreate(Guru64.Dimension[] dims, Guru64.Dimension[] hmDims) {
		switch (complexity) {
			case COMPLEX_TO_COMPLEX:
				return jfftw_plan_guru64_dft_split(rank, dims, hmDims.length, hmDims, splitInput.re, splitInput.im, splitOutput.re, splitInput.im, sign.value, flags);
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
		if (!destroyed)
			jfftw_fprint_plan(this, f.getAbsolutePath());
		else
			throw new NullPointerException("plan is destroyed");
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
		}
		else
			throw new NullPointerException("plan is destroyed");
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
		validate(Complexity.COMPLEX_TO_COMPLEX, i, o);
		jfftw_execute_dft(this, i, o);
	}

	/**
	 * Executes this COMPLEX_TO_REAL plan using new arrays.
	 * 
	 * @param i new complex input array
	 * @param o new real output array
	 */
	public void execute(Complex i, Real o) {
		validate(Complexity.COMPLEX_TO_REAL, i, o);
		jfftw_execute_dft_c2r(this, i, o);
	}

	/**
	 * Executes this REAL_TO_COMPLEX plan using new arrays.
	 * 
	 * @param i new real input array
	 * @param o new complex output array
	 */
	public void execute(Real i, Complex o) {
		validate(Complexity.REAL_TO_COMPLEX, i, o);
		jfftw_execute_dft_r2c(this, i, o);
	}

	/**
	 * Executes this REAL_TO_REAL plan using new arrays.
	 * 
	 * @param i	new real input array
	 * @param o new real output array
	 */
	public void execute(Real i, Real o) {
		validate(Complexity.REAL_TO_REAL, i, o);
		jfftw_execute_r2r(this, i, o);
	}

	/**
	 * Executes this split array plan using new split arrays.
	 *
	 * @param i	new split array input
	 * @param o	new split array output
	 */
	public void execute(SplitArray i, SplitArray o) {
		if (!destroyed) {
			if (isGuruPlan && isSplitArrayPlan) {
				switch (complexity) {
					case COMPLEX_TO_COMPLEX:
						if (i.im == null || o.im == null)
							throw new IllegalArgumentException("plan requires " + complexity + " inputs");
						jfftw_execute_split_dft(this, i.re, i.im, o.re, o.im);
						break;
					case COMPLEX_TO_REAL:
						if (i.im == null)
							throw new IllegalArgumentException("plan requires " + complexity + " inputs");
						jfftw_execute_split_dft_c2r(this, i.re, i.im, o.re);
						break;
					case REAL_TO_COMPLEX:
						if (o.im == null)
							throw new IllegalArgumentException("plan requires " + complexity + " inputs");
						jfftw_execute_split_dft_r2c(this, i.re, o.re, o.im);
						break;
					default:
						throw new UnsupportedOperationException(complexity + " transform not supported");
				}
			} else
				throw new UnsupportedOperationException("plan not made for split arrays");
		} else
			throw new NullPointerException("plan is destroyed");
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
			throw new UnsupportedOperationException("plan not made for split arrays");
		if (input instanceof Complex)
			return (Complex) input;
		else
			throw new ClassCastException("input is not complex");
	}
	
	/**
	 * @return the real input of this plan
	 */
	public final Real getRealInput() {
		if (isSplitArrayPlan)
			throw new UnsupportedOperationException("plan not made for split arrays");
		if (input instanceof Real)
			return (Real) input;
		else
			throw new ClassCastException("input is not real");
	}

	/**
	 * @return the complex output of this plan
	 */
	public final Complex getComplexOutput() {
		if (isSplitArrayPlan)
			throw new UnsupportedOperationException("plan not made for split arrays");
		if (output instanceof Complex)
			return (Complex) output;
		else
			throw new ClassCastException("output is not complex");
	}
	
	/**
	 * @return the real output of this plan
	 */
	public final Real getRealOutput() {
		if (isSplitArrayPlan)
			throw new UnsupportedOperationException("plan not made for split arrays");
		if (output instanceof Real)
			return (Real) output;
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
	 * @return	split input arrays, null if this plan is not a split array plan
	 */
	public final SplitArray getSplitInput() {
		if (isSplitArrayPlan)
			return splitInput;
		else
			throw new UnsupportedOperationException("plan not made with split arrays");
	}

	/**
	 * @return	split output arrays, null if this plan is not a split array plan
	 */
	public final SplitArray getSplitOutput() {
		if (isSplitArrayPlan)
			return splitOutput;
		else
			throw new UnsupportedOperationException("plan not made with split arrays");
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
			throw new NullPointerException("plan ");
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
