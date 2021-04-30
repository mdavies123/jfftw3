package jfftw.exceptions;

public class NonDirectBufferException extends UnsupportedOperationException {

    private final static String defaultErrorMessage = "buffer is not direct";

    public NonDirectBufferException() {
        super(defaultErrorMessage);
    }

    public NonDirectBufferException(String bufferName) {
        super(String.format(bufferName + " %s", defaultErrorMessage));
    }

}
