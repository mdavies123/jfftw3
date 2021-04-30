package jfftw.planning;

public class Wisdom {

    protected static synchronized native boolean jfftw_export_wisdom_to_filename(String s);
    protected static synchronized native String jfftw_export_wisdom_to_string();
    protected static synchronized native void jfftw_forget_wisdom();
    protected static synchronized native boolean jfftw_import_system_wisdom();
    protected static synchronized native boolean jfftw_import_wisdom_from_filename(String s);
    protected static synchronized native boolean jfftw_import_wisdom_from_string(String s);

    /**
     * Clears all currently stored wisdom.
     */
    public static void forgetWisdom() {
        jfftw_forget_wisdom();
    }

    /**
     * Exports wisdom to the file represented by s.
     *
     * @param s absolute name of the file to store wisdom
     * @return true if wisdom export is successful, false otherwise
     */
    public static boolean exportToFilename(String s) {
        return jfftw_export_wisdom_to_filename(s);
    }

    /**
     * Exports currently stored wisdom to a String.
     *
     * @return currently stored wisdom
     */
    public static String export() {
        return jfftw_export_wisdom_to_string();
    }

    /**
     * Imports wisdom from a system location.
     *
     * @return true if wisdom import is successful, false otherwise
     */
    public static boolean importSystemWisdom() {
        return jfftw_import_system_wisdom();
    }

    /**
     * Imports wisdom from a file represented by s.
     *
     * @param s absolute path of a file to import wisdom from
     * @return true if wisdom import is successful, false otherwise
     */
    public static boolean importFromFilename(String s) {
        return jfftw_import_wisdom_from_filename(s);
    }

    /**
     * Imports wisdom from a String.
     *
     * @param s wisdom string
     * @return true if wisdom import is successful, false otherwise
     */
    public static boolean importFromString(String s) {
        return jfftw_import_wisdom_from_string(s);
    }

}
