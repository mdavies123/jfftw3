#include "jfftw_planning_Wisdom.h"
#include "jfftw.h"

/*
 * Class:     jfftw_planning_Wisdom
 * Method:    jfftw_export_wisdom_to_filename
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_planning_Wisdom_jfftw_1export_1wisdom_1to_1filename
  (JNIEnv *env, jclass clazz, jstring jstr) {
    const char *fname = (*env)->GetStringUTFChars(env, jstr, 0);
    jboolean success = fftw_export_wisdom_to_filename(fname);
    (*env)->ReleaseStringUTFChars(env, jstr, fname);
    return success;
}

/*
 * Class:     jfftw_planning_Wisdom
 * Method:    jfftw_export_wisdom_to_string
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_jfftw_planning_Wisdom_jfftw_1export_1wisdom_1to_1string
  (JNIEnv *env, jclass clazz) {
    char *str = fftw_export_wisdom_to_string();
    jstring jstr = (*env)->NewStringUTF(env, str);
    fftw_free(str);
    return jstr;
}

/*
 * Class:     jfftw_planning_Wisdom
 * Method:    jfftw_forget_wisdom
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_planning_Wisdom_jfftw_1forget_1wisdom
  (JNIEnv *env, jclass clazz) {
    fftw_forget_wisdom();
}

/*
 * Class:     jfftw_planning_Wisdom
 * Method:    jfftw_import_system_wisdom
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_planning_Wisdom_jfftw_1import_1system_1wisdom
  (JNIEnv *env, jclass clazz) {
    return fftw_import_system_wisdom();
}

/*
 * Class:     jfftw_planning_Wisdom
 * Method:    jfftw_import_wisdom_from_filename
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_planning_Wisdom_jfftw_1import_1wisdom_1from_1filename
  (JNIEnv *env, jclass clazz, jstring jstr) {
    const char *fname = (*env)->GetStringUTFChars(env, jstr, 0);
    jboolean success = fftw_import_wisdom_from_filename(fname);
    (*env)->ReleaseStringUTFChars(env, jstr, fname);
    return success;
}

/*
 * Class:     jfftw_planning_Wisdom
 * Method:    jfftw_import_wisdom_from_string
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_planning_Wisdom_jfftw_1import_1wisdom_1from_1string
  (JNIEnv *env, jclass clazz, jstring jstr) {
    const char *str = (*env)->GetStringUTFChars(env, jstr, 0);
    jboolean success = fftw_import_wisdom_from_string(str);
    (*env)->ReleaseStringUTFChars(env, jstr, str);
    return success;
}
