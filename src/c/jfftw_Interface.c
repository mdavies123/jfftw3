#include "jfftw_Interface.h"
#include "common.h"

#include <jni.h>
#include <fftw3.h>

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_alignment_of
 * Signature: ([D)I
 */
JNIEXPORT jint JNICALL Java_jfftw_Interface_jfftw_1alignment_1of
  (JNIEnv *env, jclass class, jdoubleArray arr) {
    double* d = (*env)->GetPrimitiveArrayCritical(env, arr, 0);
    jint a = fftw_alignment_of(d);
    (*env)->ReleasePrimitiveArrayCritical(env, arr, d, JNI_ABORT);
    return a;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_cleanup
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1cleanup
  (JNIEnv *env, jclass class) {
    fftw_cleanup();
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_cleanup_threads
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1cleanup_1threads
  (JNIEnv *env, jclass class) {
    fftw_cleanup_threads();
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_cost
 * Signature: (Ljfftw/Plan;)D
 */
JNIEXPORT jdouble JNICALL Java_jfftw_Interface_jfftw_1cost
  (JNIEnv *env, jclass class, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    return fftw_cost(p);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_destroy_plan
 * Signature: (Ljfftw/Plan;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1destroy_1plan
  (JNIEnv *env, jclass class, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    fftw_destroy_plan(p);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_estimate_cost
 * Signature: (Ljfftw/Plan;)D
 */
JNIEXPORT jdouble JNICALL Java_jfftw_Interface_jfftw_1estimate_1cost
  (JNIEnv *env, jclass class, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    return fftw_estimate_cost(p);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_flops
 * Signature: (Ljfftw/Plan;[D[D[D)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1flops
  (JNIEnv *env, jclass class, jobject plan, jdoubleArray a1, jdoubleArray a2, jdoubleArray a3) {
    printf("%s\n", "Java_jfftw_Interface_jfftw_1flops not implemented.");
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_init_threads
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1init_1threads
  (JNIEnv *env, jclass class) {
    fftw_init_threads();
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_make_planner_thread_safe
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1make_1planner_1thread_1safe
  (JNIEnv *env, jclass class) {
    fftw_make_planner_thread_safe();
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_with_nthreads
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1plan_1with_1nthreads
  (JNIEnv *env, jclass class, jint nthreads) {
    fftw_plan_with_nthreads(nthreads);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_set_timelimit
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1set_1timelimit
  (JNIEnv *env, jclass class, jdouble t) {
    fftw_set_timelimit(t);
}
