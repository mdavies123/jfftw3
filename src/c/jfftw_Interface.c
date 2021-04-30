#include "jfftw_Interface.h"
#include "jfftw.h"

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_cleanup
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1cleanup
  (JNIEnv *env, jclass clazz) {
    fftw_cleanup();
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_cleanup_threads
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1cleanup_1threads
  (JNIEnv *env, jclass clazz) {
    fftw_cleanup_threads();
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_init_threads
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_Interface_jfftw_1init_1threads
  (JNIEnv *env, jclass clazz) {
    return fftw_init_threads();
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_make_planner_thread_safe
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1make_1planner_1thread_1safe
  (JNIEnv *env, jclass clazz) {
    fftw_make_planner_thread_safe();
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_with_nthreads
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1plan_1with_1nthreads
  (JNIEnv *env, jclass clazz, jint n) {
    fftw_plan_with_nthreads(n);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_set_timelimit
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1set_1timelimit
  (JNIEnv *env, jclass clazz, jdouble limit) {
    fftw_set_timelimit(limit);
}
