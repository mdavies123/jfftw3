package jfftw;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class Plan {

    public final Sign sign;
    public final Type type;
    private final int[] size;
    private final int flags;
    protected final Value in;
    protected final Value out;
    protected final long planPointer;

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
            throw new ArrayStoreException("array sizes invalid");
    }

    private static native void jfftw_fprint_plan(Plan p, File output_file);
    private static native void jfftw_print_plan(Plan p);
    private static native String jfftw_sprint_plan(Plan p);
    private static native long jfftw_plan_dft(int rank, int[] n, Complex in, Complex out, int sign, int flags);
    private static native long jfftw_plan_dft_1d(int n, Complex in, Complex out, int sign, int flags);
    private static native long jfftw_plan_dft_2d(int n0, int n1, Complex in, Complex out, int sign, int flags);
    private static native long jfftw_plan_dft_3d(int n0, int n1, int n2, Complex in, Complex out, int sign, int flags);
    private static native long jfftw_plan_dft_c2r(int rank, int[] n, Complex in, Real out, int flags);
    private static native long jfftw_plan_dft_c2r_1d(int n, Complex in, Real out, int flags);
    private static native long jfftw_plan_dft_c2r_2d(int n0, int n1, Complex in, Real out, int flags);
    private static native long jfftw_plan_dft_c2r_3d(int n0, int n1, int n2, Complex in, Real out, int flags);
    private static native long jfftw_plan_dft_r2c(int rank, int[] n, Real in, Complex out, int flags);
    private static native long jfftw_plan_dft_r2c_1d(int n, Real in, Complex out, int flags);
    private static native long jfftw_plan_dft_r2c_2d(int n0, int n1, Real in, Complex out, int flags);
    private static native long jfftw_plan_dft_r2c_3d(int n0, int n1, int n2, Real in, Complex out, int flags);

    public void fprint(File f) {
        jfftw_fprint_plan(this, f);
    }
    public void print() {
        jfftw_print_plan(this);
    }
    public double cost() { return Interface.cost(this); }
    public void destroy() { Interface.destroyPlan(this); }
    public double estimateCost() { return Interface.estimateCost(this); }
    public void execute() { Execution.execute(this); }
    public void executeDft(Complex in, Complex out) { Execution.executeDft(this, in, out); }
    public void executeDftComplexToReal(Complex in, Real out) { Execution.executeDftComplexToReal(this, in, out); }
    public void executeDftRealToComplex(Real in, Complex out) { Execution.executeDftRealToComplex(this, in, out); }
    public void executeRealToReal(Real in, Real out) { Execution.executeRealToReal(this, in, out); }
    public void finalize() { destroy(); }
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
        switch (type) {
            case COMPLEX_TO_COMPLEX:
                if (in.getSize() != N * 2 || out.getSize() != N * 2)
                    return false;
                break;
            case COMPLEX_TO_REAL:
                if (in.getSize() != N * 2 || out.getSize() != N)
                    return false;
                break;
            case REAL_TO_COMPLEX:
                if (in.getSize() != N || out.getSize() != N * 2)
                    return false;
                break;
            case REAL_TO_REAL:
                if (in.getSize() != N || out.getSize() != N)
                    return false;
                break;
        }
        return true;
    }

    private long create() {
        long ptr;
        switch (type) {
            case COMPLEX_TO_COMPLEX:
                switch (size.length) {
                    case 1:
                        ptr = jfftw_plan_dft_1d(size[0], (Complex) in, (Complex) out, sign.value, flags);
                        break;
                    case 2:
                        ptr = jfftw_plan_dft_2d(size[0], size[1], (Complex) in, (Complex) out, sign.value, flags);
                        break;
                    case 3:
                        ptr = jfftw_plan_dft_3d(size[0], size[1], size[2], (Complex) in, (Complex) out, sign.value, flags);
                        break;
                    default:
                        ptr = jfftw_plan_dft(size.length, size, (Complex) in, (Complex) out, sign.value, flags);
                        break;
                }
                break;
            case COMPLEX_TO_REAL:
                switch (size.length) {
                    case 1:
                        ptr = jfftw_plan_dft_c2r_1d(size[0], (Complex) in, (Real) out, flags);
                        break;
                    case 2:
                        ptr = jfftw_plan_dft_c2r_2d(size[0], size[1], (Complex) in, (Real) out, flags);
                        break;
                    case 3:
                        ptr = jfftw_plan_dft_c2r_3d(size[0], size[1], size[2], (Complex) in, (Real) out, flags);
                        break;
                    default:
                        ptr = jfftw_plan_dft_c2r(size.length, size, (Complex) in, (Real) out, flags);
                }
                break;
            case REAL_TO_COMPLEX:
                switch (size.length) {
                    case 1:
                        ptr = jfftw_plan_dft_r2c_1d(size[0], (Real) in, (Complex) out, flags);
                        break;
                    case 2:
                        ptr = jfftw_plan_dft_r2c_2d(size[0], size[1], (Real) in, (Complex) out, flags);
                        break;
                    case 3:
                        ptr = jfftw_plan_dft_r2c_3d(size[0], size[1], size[2], (Real) in, (Complex) out, flags);
                        break;
                    default:
                        ptr = jfftw_plan_dft_r2c(size.length, size, (Real) in, (Complex) out, flags);
                }
                break;
            default:
                throw new UnsupportedOperationException("real to real transforms not supported");
        }
        return ptr;
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
        REAL_TO_REAL;
    }

}
