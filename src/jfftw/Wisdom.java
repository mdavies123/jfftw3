package jfftw;

import java.io.File;

import static jfftw.Interface.*;

public class Wisdom {

	/**
	 * Clears all currently stored wisdom.
	 */
    public static void forgetWisdom() {
        jfftw_forget_wisdom();
    }

    /**
     * Exports wisdom to the file pointed to by f.
     * 
     * @param f		file to store wisdom
     */
    public static void export(File f) {
        jfftw_export_wisdom_to_filename(f.getAbsolutePath());
    }

    /**
     * Exports wisdom to the file represented by s.
     * 
     * @param s		absolute name of the file to store wisdom
     */
    public static void exportToFilename(String s) {
        jfftw_export_wisdom_to_filename(s);
    }

    /**
     * Exports currently stored wisdom to a String.
     * 
     * @return		currently stored wisdom
     */
    public static String export() {
        return jfftw_export_wisdom_to_string();
    }

    /**
     * Imports wisdom from a system location.
     */
    public static void importSystemWisdom() {
        jfftw_import_system_wisdom();
    }

    /**
     * Imports wisdom from a file pointed to by f.
     * 
     * @param f		file to import wisdom from
     */
    public static void importFromFile(File f) {
        jfftw_import_wisdom_from_file(f);
    }

    /**
     * Imports wisdom from a file represented by s.
     * 
     * @param s		absolute path of a file to import wisdom from
     */
    public static void importFromFilename(String s) {
        jfftw_import_wisdom_from_filename(s);
    }

    /**
     * Imports wisdom from a String.
     * @param s		wisdom string
     */
    public static void importFromString(String s) {
        jfftw_import_wisdom_from_string(s);
    }
}
