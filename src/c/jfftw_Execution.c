#include "jfftw_Execution.h"
#include "common.h"

#include <jni.h>
#include <fftw3.h>

/*
 * Class:     jfftw_Execution
 * Method:    jfftw_execute
 * Signature: (Ljfftw/Plan;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Execution_jfftw_1execute
  (JNIEnv *env, jclass class, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    fftw_execute(p);
}

/*
 * Class:     jfftw_Execution
 * Method:    jfftw_execute_dft
 * Signature: (Ljfftw/Plan;Ljfftw/Complex;Ljfftw/Complex;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Execution_jfftw_1execute_1dft
  (JNIEnv *env, jclass class, jobject plan, jobject i, jobject o) {
    _PRE_EXECUTE
    fftw_execute_dft(p, (fftw_complex*) in->c, (fftw_complex*) out->c);
    _POST_EXECUTE;
}

/*
 * Class:     jfftw_Execution
 * Method:    jfftw_execute_dft_c2r
 * Signature: (Ljfftw/Plan;Ljfftw/Complex;Ljfftw/Real;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Execution_jfftw_1execute_1dft_1c2r
  (JNIEnv *env, jclass class, jobject plan, jobject i, jobject o) {
    _PRE_EXECUTE
    fftw_execute_dft_c2r(p, (fftw_complex*) in->c, out->c);
    _POST_EXECUTE
}

/*
 * Class:     jfftw_Execution
 * Method:    jfftw_execute_dft_r2c
 * Signature: (Ljfftw/Plan;Ljfftw/Real;Ljfftw/Complex;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Execution_jfftw_1execute_1dft_1r2c
  (JNIEnv *env, jclass class, jobject plan, jobject i, jobject o) {
    _PRE_EXECUTE
    fftw_execute_dft_r2c(p, in->c, (fftw_complex*) out->c);
    _POST_EXECUTE
}

/*
 * Class:     jfftw_Execution
 * Method:    jfftw_execute_r2r
 * Signature: (Ljfftw/Plan;Ljfftw/Real;Ljfftw/Real;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Execution_jfftw_1execute_1r2r
  (JNIEnv *env, jclass class, jobject plan, jobject i, jobject o) {
    _PRE_EXECUTE
    fftw_execute_r2r(p, in->c, out->c);
    _POST_EXECUTE
}

