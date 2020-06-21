package jfftw;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import static jfftw.Interface.*;

public class Plan {

    public final Sign sign;
    public final Type type;
    protected final Value in;
    protected final Value out;
    protected final long planPointer;
    private final int[] size;
    private final int flags;

    public Plan(Value i, Value o, Sign s, Flag[] f, int... n) {
        this(i, o, s, Flag.combine(f), n);
    }

    public Plan(Value i, Value o, Sign s, int f, int... n) {
        in = i;
        out = o;
        sign = s;
        flags = f;
        size = Arrays.copyOf(n, n.length);
        type = determineType();
        if (validateSize())
            planPointer = create();
        else
            throw new UnsupportedOperationException("mismatched input sizes");
    }

    public void fprint(File f) {
        jfftw_fprint_plan(this, f);
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
        jfftw_execute_dft(this, in, out);
    }

    public void execute(Complex in, Real out) {
        jfftw_execute_dft_c2r(this, in, out);
    }

    public void execute(Real in, Complex out) {
        jfftw_execute_dft_r2c(this, in, out);
    }

    public void execute(Real in, Real out) {
        jfftw_execute_r2r(this, in, out);
    }

    public void finalize() {
        destroy();
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

    private boolean validateSize() {
        int N = 1;
        for (int i : size)
            N *= i;
        return in.size() == N && out.size() == N;
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
            default:
                throw new UnsupportedOperationException("real to real transforms not supported");
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plan plan = (Plan) o;
        return flags == plan.flags &&
                sign == plan.sign &&
                type == plan.type &&
                Arrays.equals(size, plan.size);
    }

    public int hashCode() {
        int result = Objects.hash(sign, type, flags);
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

    private enum Type {
        COMPLEX_TO_COMPLEX,
        COMPLEX_TO_REAL,
        REAL_TO_COMPLEX,
        REAL_TO_REAL
    }

}
