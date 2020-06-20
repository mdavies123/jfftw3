#include "jfftw_Plan.h"
#include "common.h"

#include <jni.h>
#include <fftw3.h>

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_fprint_plan
 * Signature: (Ljfftw/Plan;Ljava/io/File;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Plan_jfftw_1fprint_1plan
  (JNIEnv *env, jclass class, jobject plan, jobject file) {
    fftw_plan p = get_fftw_plan(env, plan);
    jmethodID jmid = (*env)->GetMethodID(env, file, "getAbsolutePath", "java/lang/String;");
    jobject jstr = (*env)->CallObjectMethod(env, file, jmid);
    const char* path = (*env)->GetStringUTFChars(env, jstr, 0);
    FILE *f = fopen(path, "w+");
    fftw_fprint_plan(p, f);
    fclose(f);
    (*env)->ReleaseStringUTFChars(env, jstr, path);
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_print_plan
 * Signature: (Ljfftw/Plan;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Plan_jfftw_1print_1plan
  (JNIEnv *env, jclass class, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    fftw_print_plan(p);
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_sprint_plan
 * Signature: (Ljfftw/Plan;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_jfftw_Plan_jfftw_1sprint_1plan
  (JNIEnv *env, jclass class, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    char* str = fftw_sprint_plan(p);
    jstring jstr = (*env)->NewStringUTF(env, str);
    free(str);
    return jstr;
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_plan_dft
 * Signature: (I[ILjfftw/Complex;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Plan_jfftw_1plan_1dft
  (JNIEnv *env, jclass class, jint rank, jintArray n, jobject i, jobject o, jint sign, jint flags) {
    jint* nums = (*env)->GetIntArrayElements(env, n, 0);
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft(rank, nums, (fftw_complex*) in->c, (fftw_complex*) out->c, sign, flags);
    (*env)->ReleaseIntArrayElements(env, n, nums, JNI_ABORT);
    _POST_PLAN
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_plan_dft_1d
 * Signature: (ILjfftw/Complex;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Plan_jfftw_1plan_1dft_11d
  (JNIEnv *env, jclass class, jint n, jobject i, jobject o, jint sign, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_1d(n, (fftw_complex*) in->c, (fftw_complex*) out->c, sign, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_plan_dft_2d
 * Signature: (IILjfftw/Complex;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Plan_jfftw_1plan_1dft_12d
  (JNIEnv *env, jclass class, jint n0, jint n1, jobject i, jobject o, jint sign, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_2d(n0, n1, (fftw_complex*) in->c, (fftw_complex*) out->c, sign, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_plan_dft_3d
 * Signature: (IIILjfftw/Complex;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Plan_jfftw_1plan_1dft_13d
  (JNIEnv *env, jclass class, jint n0, jint n1, jint n2, jobject i, jobject o, jint sign, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_3d(n0, n1, n2, (fftw_complex*) in->c, (fftw_complex*) out->c, sign, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_plan_dft_c2r
 * Signature: (I[ILjfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Plan_jfftw_1plan_1dft_1c2r
  (JNIEnv *env, jclass class, jint rank, jintArray n, jobject i, jobject o, jint flags) {
    int* nums = (*env)->GetIntArrayElements(env, n, 0);
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_c2r(rank, nums, (fftw_complex*) in->c, out->c, flags);
    (*env)->ReleaseIntArrayElements(env, n, nums, JNI_ABORT);
    _POST_PLAN
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_plan_dft_c2r_1d
 * Signature: (ILjfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Plan_jfftw_1plan_1dft_1c2r_11d
  (JNIEnv *env, jclass class, jint n, jobject i, jobject o, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_c2r_1d(n, (fftw_complex*) in->c, out->c, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_plan_dft_c2r_2d
 * Signature: (IILjfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Plan_jfftw_1plan_1dft_1c2r_12d
  (JNIEnv *env, jclass class, jint n0, jint n1, jobject i, jobject o, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_c2r_2d(n0, n1, (fftw_complex*) in->c, out->c, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_plan_dft_c2r_3d
 * Signature: (IIILjfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Plan_jfftw_1plan_1dft_1c2r_13d
  (JNIEnv *env, jclass class, jint n0, jint n1, jint n2, jobject i, jobject o, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_c2r_3d(n0, n1, n2, (fftw_complex*) in->c, out->c, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_plan_dft_r2c
 * Signature: (I[ILjfftw/Real;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Plan_jfftw_1plan_1dft_1r2c
  (JNIEnv *env, jclass class, jint rank, jintArray n, jobject i, jobject o, jint flags) {
    int* nums = (*env)->GetIntArrayElements(env, n, 0);
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_r2c(rank, nums, in->c, (fftw_complex*) out->c, flags);
    (*env)->ReleaseIntArrayElements(env, n, nums, JNI_ABORT);
    _POST_PLAN
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_plan_dft_r2c_1d
 * Signature: (ILjfftw/Real;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Plan_jfftw_1plan_1dft_1r2c_11d
  (JNIEnv *env, jclass class, jint n, jobject i, jobject o, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_r2c_1d(n, in->c, (fftw_complex*) out->c, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_plan_dft_r2c_2d
 * Signature: (IILjfftw/Real;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Plan_jfftw_1plan_1dft_1r2c_12d
  (JNIEnv *env, jclass class, jint n0, jint n1, jobject i, jobject o, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_r2c_2d(n0, n1, in->c, (fftw_complex*) out->c, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Plan
 * Method:    jfftw_plan_dft_r2c_3d
 * Signature: (IIILjfftw/Real;Ljfftw/Complex;IIJ
 */
JNIEXPORT jlong JNICALL Java_jfftw_Plan_jfftw_1plan_1dft_1r2c_13d
  (JNIEnv *env, jclass class, jint n0, jint n1, jint n2, jobject i, jobject o, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_r2c_3d(n0, n1, n2, in->c, (fftw_complex*) out->c, flags);
    _POST_PLAN
}
