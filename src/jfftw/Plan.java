package jfftw;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static jfftw.Interface.*;

public class Plan {

	protected final Sign sign;
	protected final Complexity type;
	protected final Placement placement;
	protected final Value input;
	protected final Value output;
	protected final long address;
	protected final int rank;
	protected final int[] dimensions;
	protected final int size;
	protected final int flags;
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
		this.input = i;
		this.output = o;
		this.sign = s;
		this.type = determineComplexity(i, o);
		this.placement = determinePlacement(i, o);
		this.rank = n.length;
		this.dimensions = Arrays.copyOf(n, rank);
		this.flags = f;

		int N = 1;
		for (int d : this.dimensions)
			N *= d;
		this.size = N;

		if (input.size() == this.size && output.size() == this.size && this.size > 0)
			this.address = create();
		else
			throw new IllegalArgumentException("invalid plan configuration");
		if (this.address == 0)
			throw new NullPointerException("plan creation returned null");
	}

	private static Placement determinePlacement(Value input, Value output) {
		if (input == output) // intentionally compare the address of these Values
			return Placement.IN_PLACE;
		return Placement.OUT_OF_PLACE;
	}
	
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
	
	private void validate(Complexity t, Value i, Value o) {
		if (destroyed)
			throw new NullPointerException("attempted to execute destroyed plan");
		int in = i.size(), out = o.size();
		if (type != t)
			throw new IllegalArgumentException("requested " + t + " transform for " + type + " plan");
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

	private long create() {
		switch (type) {
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
			throw new UnsupportedOperationException(type + " transform not supported");
		}
	}

	/**
	 * Prints this plan to a file.
	 * 
	 * @param f file to print plan
	 */
	public void fprint(File f) {
		jfftw_fprint_plan(this, f.getAbsolutePath());
	}

	/**
	 * Prints this plan to stdout.
	 * <p>
	 * Since this method is implemented in native code, it cannot guarantee the
	 * output will be as expected.
	 */
	public void print() {
		jfftw_print_plan(this);
	}

	/**
	 * Returns the internal measurement of how effective this plan is.
	 * 
	 * @return cost of this plan
	 */
	public double cost() {
		return jfftw_cost(this);
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
			throw new NullPointerException("plan is already destroyed");
	}

	/**
	 * Gives an estimate of how effective this plan is.
	 * 
	 * @return estimated cost of this plan
	 */
	public double estimateCost() {
		return jfftw_estimate_cost(this);
	}

	/**
	 * Executes this plan using the input and output arrays supplied upon object
	 * creations.
	 */
	public void execute() {
		jfftw_execute(this);
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
	 * @param o new real output array
	 */
	public void execute(Real i, Complex o) {
		validate(Complexity.REAL_TO_COMPLEX, i, o);
		jfftw_execute_dft_r2c(this, i, o);
	}

	/**
	 * Executes this REAL_TO_REAL plan using new arrays.
	 * 
	 * @param input  new real input array
	 * @param output new real output array
	 */
	public void execute(Real i, Real o) {
		validate(Complexity.REAL_TO_REAL, i, o);
		jfftw_execute_r2r(this, i, o);
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
		return type;
	}

	/**
	 * @return the complex input of this plan
	 */
	public final Complex getComplexInput() {
		if (input instanceof Complex)
			return (Complex) input;
		else
			throw new ClassCastException("input is not complex");
	}
	
	/**
	 * @return the real input of this plan
	 */
	public final Real getRealInput() {
		if (input instanceof Real)
			return (Real) input;
		else
			throw new ClassCastException("input is not real");
	}

	/**
	 * @return the complex output of this plan
	 */
	public final Complex getComplexOutput() {
		if (output instanceof Complex)
			return (Complex) output;
		else
			throw new ClassCastException("output is not complex");
	}
	
	/**
	 * @return the real output of this plan
	 */
	public final Real getRealOutput() {
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
	public final int getSize() {
		return size;
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
		return jfftw_sprint_plan(this);
	}
	
	/**
	 * @return true if the plan is destroyed and therefore invalid, false otherwise
	 */
	public boolean isDestroyed() {
		return destroyed;
	}

	/**
	 * Uses all the address of this plan to create a hash.
	 * 
	 * @return hashcode of this plan
	 */
	public int hashCode() {
		return Objects.hash(address);
	}

	/**
	 * Enumeration representing the sign of the exponent in the transform.
	 */
	public static enum Sign {

		POSITIVE(1), NEGATIVE(-1);

		public final int value;

		Sign(int v) {
			value = v;
		}
	}

	/**
	 * Enumeration representing the placement of a plan.
	 */
	public static enum Placement {
		IN_PLACE, OUT_OF_PLACE;
	}

	/**
	 * Enumeration representing the type of complexity of a plan.
	 */
	public static enum Complexity {
		COMPLEX_TO_COMPLEX, COMPLEX_TO_REAL, REAL_TO_COMPLEX, REAL_TO_REAL
	}

}
