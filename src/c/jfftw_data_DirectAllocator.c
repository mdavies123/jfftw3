#include "jfftw_data_DirectAllocator.h"
#include "jfftw.h"

/*
 * Class:     jfftw_data_DirectAllocator
 * Method:    jfftw_alloc_complex
 * Signature: (I)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_jfftw_data_DirectAllocator_jfftw_1alloc_1complex
  (JNIEnv *env, jclass clazz, jint size) {
    fftw_complex *cbuff = fftw_alloc_complex(size);
    return (*env)->NewDirectByteBuffer(env, cbuff, sizeof(*cbuff) * size);
}

/*
 * Class:     jfftw_data_DirectAllocator
 * Method:    jfftw_alloc_real
 * Signature: (I)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_jfftw_data_DirectAllocator_jfftw_1alloc_1real
  (JNIEnv *env, jclass clazz, jint size) {
    double *rbuff = fftw_alloc_real(size);
    return (*env)->NewDirectByteBuffer(env, rbuff, sizeof(*rbuff) * size);
}
