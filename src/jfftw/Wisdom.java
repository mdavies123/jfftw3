package jfftw;

import java.io.File;

public class Wisdom {

    private static native void jfftw_forget_wisdom();
    private static native void jfftw_export_wisdom_to_file(File f);
    private static native void jfftw_export_wisdom_to_filename(String s);
    private static native String jfftw_export_wisdom_to_string();
    private static native void jfftw_import_system_wisdom();
    private static native void jfftw_import_wisdom_from_file(File f);
    private static native void jfftw_import_wisdom_from_filename(String s);
    private static native void jfftw_import_wisdom_from_string(String s);

    public static void forgetWisdom() { jfftw_forget_wisdom(); }
    public static void exportWisdomToFile(File f) { jfftw_export_wisdom_to_file(f); }
    public static void exportWisdomToFilename(String s) { jfftw_export_wisdom_to_filename(s); }
    public static String exportWisdomToString() { return jfftw_export_wisdom_to_string(); }
    public static void importSystemWisdom() { jfftw_import_system_wisdom(); }
    public static void importWisdomFromFile(File f) { jfftw_import_wisdom_from_file(f); }
    public static void impotyWisdomFromFilename(String s) { jfftw_import_wisdom_from_filename(s); }
    public static void importWisdomFromString(String s) { jfftw_import_wisdom_from_string(s); }
}
