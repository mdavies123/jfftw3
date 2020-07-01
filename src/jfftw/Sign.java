package jfftw;

/**
 * Enumeration representing the sign of the exponent in the transform.
 */
public enum Sign {

    POSITIVE(1), NEGATIVE(-1);

    public final int value;

    Sign(int v) {
        value = v;
    }
}
