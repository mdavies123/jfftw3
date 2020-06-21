package jfftw;

import java.io.File;

import static jfftw.Interface.*;

public class Wisdom {

    public static void forgetWisdom() {
        jfftw_forget_wisdom();
    }

    public static void exportToFile(File f) {
        jfftw_export_wisdom_to_file(f);
    }

    public static void exportToFilename(String s) {
        jfftw_export_wisdom_to_filename(s);
    }

    public static String export() {
        return jfftw_export_wisdom_to_string();
    }

    public static void importSystemWisdom() {
        jfftw_import_system_wisdom();
    }

    public static void importFromFile(File f) {
        jfftw_import_wisdom_from_file(f);
    }

    public static void importFromFilename(String s) {
        jfftw_import_wisdom_from_filename(s);
    }

    public static void importFromString(String s) {
        jfftw_import_wisdom_from_string(s);
    }
}
