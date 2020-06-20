#include "jfftw_Wisdom.h"
#include "common.h"

#include <jni.h>
#include <fftw3.h>

/*
 * Class:     jfftw_Wisdom
 * Method:    jfftw_forget_wisdom
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_Wisdom_jfftw_1forget_1wisdom
  (JNIEnv *env, jclass class) {
    fftw_forget_wisdom();
}

/*
 * Class:     jfftw_Wisdom
 * Method:    jfftw_export_wisdom_to_file
 * Signature: (Ljava/io/File;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Wisdom_jfftw_1export_1wisdom_1to_1file
  (JNIEnv *env, jclass class, jobject file) {
    jmethodID jmid = (*env)->GetMethodID(env, file, "getAbsolutePath", "java/lang/String;");
    jobject jstr = (*env)->CallObjectMethod(env, file, jmid);
    const char* path = (*env)->GetStringUTFChars(env, jstr, 0);
    FILE *f = fopen(path, "w+");
    fftw_export_wisdom_to_file(f);
    fclose(f);
    (*env)->ReleaseStringUTFChars(env, jstr, path);
}

/*
 * Class:     jfftw_Wisdom
 * Method:    jfftw_export_wisdom_to_filename
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Wisdom_jfftw_1export_1wisdom_1to_1filename
  (JNIEnv *env, jclass class, jstring s) {
    const char* fn = (*env)->GetStringUTFChars(env, s, 0);
    fftw_export_wisdom_to_filename(fn);
    (*env)->ReleaseStringUTFChars(env, s, fn);
}

/*
 * Class:     jfftw_Wisdom
 * Method:    jfftw_export_wisdom_to_string
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_jfftw_Wisdom_jfftw_1export_1wisdom_1to_1string
  (JNIEnv *env, jclass class) {
    char* s = fftw_export_wisdom_to_string();
    jstring jstr = (*env)->NewStringUTF(env, s);
    free(s);
    return jstr;
}

/*
 * Class:     jfftw_Wisdom
 * Method:    jfftw_import_system_wisdom
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_Wisdom_jfftw_1import_1system_1wisdom
  (JNIEnv *env, jclass class) {
    fftw_import_system_wisdom();
}

/*
 * Class:     jfftw_Wisdom
 * Method:    jfftw_import_wisdom_from_file
 * Signature: (Ljava/io/File;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Wisdom_jfftw_1import_1wisdom_1from_1file
  (JNIEnv *env, jclass class, jobject file) {
    jmethodID jmid = (*env)->GetMethodID(env, file, "getAbsolutePath", "java/lang/String;");
    jobject jstr = (*env)->CallObjectMethod(env, file, jmid);
    const char* path = (*env)->GetStringUTFChars(env, jstr, 0);
    FILE *f = fopen(path, "w+");
    fftw_import_wisdom_from_file(f);
    fclose(f);
    (*env)->ReleaseStringUTFChars(env, jstr, path);
}

/*
 * Class:     jfftw_Wisdom
 * Method:    jfftw_import_wisdom_from_filename
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Wisdom_jfftw_1import_1wisdom_1from_1filename
  (JNIEnv *env, jclass class, jstring s) {
    const char* fn = (*env)->GetStringUTFChars(env, s, 0);
    fftw_import_wisdom_from_filename(fn);
    (*env)->ReleaseStringUTFChars(env, s, fn);
}

/*
 * Class:     jfftw_Wisdom
 * Method:    jfftw_import_wisdom_from_string
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Wisdom_jfftw_1import_1wisdom_1from_1string
  (JNIEnv *env, jclass class, jstring s) {
    const char* str = (*env)->GetStringUTFChars(env, s, 0);
    fftw_import_wisdom_from_string(str);
    (*env)->ReleaseStringUTFChars(env, s, str);
}
