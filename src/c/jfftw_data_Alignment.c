#include "jfftw_data_Alignment.h"
#include "jfftw.h"

/*
 * Class:     jfftw_data_Alignment
 * Method:    jfftw_alignment_of
 * Signature: ([D)I
 */
JNIEXPORT jint JNICALL Java_jfftw_data_Alignment_jfftw_1alignment_1of___3D
  (JNIEnv *env, jclass clazz, jdoubleArray jarr) {
    double *carr = get_array(env, jarr);
    jint alignment = fftw_alignment_of(carr);
    release_array(env, jarr, carr, JNI_COMMIT);
    return alignment;
}

/*
 * Class:     jfftw_data_Alignment
 * Method:    jfftw_alignment_of
 * Signature: (Ljava/nio/DoubleBuffer;)I
 */
JNIEXPORT jint JNICALL Java_jfftw_data_Alignment_jfftw_1alignment_1of__Ljava_nio_DoubleBuffer_2
  (JNIEnv *env, jclass clazz, jobject buff) {
    double *carr = (double *) get_direct_buffer(env, buff);
    return fftw_alignment_of(carr);
}
