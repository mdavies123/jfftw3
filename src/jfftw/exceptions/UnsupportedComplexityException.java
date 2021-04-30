package jfftw.exceptions;

public class UnsupportedComplexityException extends UnsupportedOperationException {

    private final static String defaultErrorMessage = "complexity is not supported";

    public UnsupportedComplexityException() {
        super(defaultErrorMessage);
    }

    public UnsupportedComplexityException(Object cplx) {
        super(String.format(cplx.toString() + " %s", defaultErrorMessage));
    }
}
