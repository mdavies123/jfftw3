package jfftw;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static jfftw.Interface.*;

public class Plan {

    protected final Sign sign;
    protected final Type type;
    protected final Value in;
    protected final Value out;
    protected final long address;
    protected final int rank;
    protected final int[] size;
    protected final int flags;

    public Plan(Value i, Value o, Sign s, int f, int... n) {
        this.in = i;
        this.out = o;
        this.sign = s;
        this.type = determineType();
        this.rank = n.length;
        this.size = Arrays.copyOf(n, rank);
        this.flags = f;
        if (validateSize())
            this.address = create();
        else
            throw new IllegalArgumentException("invalid plan configuration");
    }

    public void fprint(File f) {
        jfftw_fprint_plan(this, f.getAbsolutePath());
    }

    public void print() {
        jfftw_print_plan(this);
    }

    public double cost() {
        return jfftw_cost(this);
    }

    public void destroy() {
        jfftw_destroy_plan(this);
    }

    public double estimateCost() {
        return jfftw_estimate_cost(this);
    }

    public void execute() {
        jfftw_execute(this);
    }

    public void execute(Complex in, Complex out) {
        validateType(Type.COMPLEX_TO_COMPLEX);
        jfftw_execute_dft(this, in, out);
    }

    public void execute(Complex in, Real out) {
        validateType(Type.COMPLEX_TO_REAL);
        jfftw_execute_dft_c2r(this, in, out);
    }

    public void execute(Real in, Complex out) {
        validateType(Type.REAL_TO_COMPLEX);
        jfftw_execute_dft_r2c(this, in, out);
    }

    public void execute(Real in, Real out) {
        validateType(Type.REAL_TO_REAL);
        jfftw_execute_r2r(this, in, out);
    }

    public String toString() {
        return jfftw_sprint_plan(this);
    }

    private Type determineType() {
        if (in instanceof Complex)
            if (out instanceof Complex)
                return Type.COMPLEX_TO_COMPLEX;
            else
                return Type.COMPLEX_TO_REAL;
        else if (out instanceof Complex)
            return Type.REAL_TO_COMPLEX;
        else
            return Type.REAL_TO_REAL;
    }

    private void validateType(Type t) {
        if (type != t)
            throw new IllegalArgumentException("requested " + t + " transform for " + type + " plan");
    }

    private boolean validateSize() {
        int N = 1;
        for (int i : size)
            N *= i;
        return in.size() == N && out.size() == N && N > 0;
    }

    private long create() {
        switch (type) {
            case COMPLEX_TO_COMPLEX:
                switch (size.length) {
                    case 1:
                        return jfftw_plan_dft_1d(size[0], (Complex) in, (Complex) out, sign.value, flags);
                    case 2:
                        return jfftw_plan_dft_2d(size[0], size[1], (Complex) in, (Complex) out, sign.value, flags);
                    case 3:
                        return jfftw_plan_dft_3d(size[0], size[1], size[2], (Complex) in, (Complex) out, sign.value, flags);
                    default:
                        return jfftw_plan_dft(size.length, size, (Complex) in, (Complex) out, sign.value, flags);
                }
            case COMPLEX_TO_REAL:
                switch (size.length) {
                    case 1:
                        return jfftw_plan_dft_c2r_1d(size[0], (Complex) in, (Real) out, flags);
                    case 2:
                        return jfftw_plan_dft_c2r_2d(size[0], size[1], (Complex) in, (Real) out, flags);
                    case 3:
                        return jfftw_plan_dft_c2r_3d(size[0], size[1], size[2], (Complex) in, (Real) out, flags);
                    default:
                        return jfftw_plan_dft_c2r(size.length, size, (Complex) in, (Real) out, flags);
                }
            case REAL_TO_COMPLEX:
                switch (size.length) {
                    case 1:
                        return jfftw_plan_dft_r2c_1d(size[0], (Real) in, (Complex) out, flags);
                    case 2:
                        return jfftw_plan_dft_r2c_2d(size[0], size[1], (Real) in, (Complex) out, flags);
                    case 3:
                        return jfftw_plan_dft_r2c_3d(size[0], size[1], size[2], (Real) in, (Complex) out, flags);
                    default:
                        return jfftw_plan_dft_r2c(size.length, size, (Real) in, (Complex) out, flags);
                }
            case REAL_TO_REAL:
                throw new UnsupportedOperationException("real to real transforms not supported");
            default:
                throw new UnsupportedOperationException("unknown transform type");
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plan plan = (Plan) o;
        return flags == plan.flags &&
                sign == plan.sign &&
                type == plan.type &&
                address == plan.address &&
                Arrays.equals(size, plan.size);
    }

    public int hashCode() {
        int result = Objects.hash(sign, type, flags, address);
        result = 31 * result + Arrays.hashCode(size);
        return result;
    }

    public enum Sign {

        POSITIVE(1),
        NEGATIVE(-1);

        public final int value;

        Sign(int v) {
            value = v;
        }
    }

    protected enum Type {
        COMPLEX_TO_COMPLEX,
        COMPLEX_TO_REAL,
        REAL_TO_COMPLEX,
        REAL_TO_REAL
    }

}
