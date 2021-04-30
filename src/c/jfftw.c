#include "jfftw.h"

double *get_direct_buffer(JNIEnv *env, jobject jbuff) {
    return (*env)->GetDirectBufferAddress(env, jbuff);
}

double *get_array(JNIEnv *env, jdoubleArray jarr) {
    return (*env)->GetPrimitiveArrayCritical(env, jarr, 0);
}

void release_array(JNIEnv *env, jobject jarr, void *carr, jint commit) {
    (*env)->ReleasePrimitiveArrayCritical(env, jarr, carr, commit);
}

fftw_plan get_fftw_plan(JNIEnv *env, jobject plan) {
    jclass clazz = (*env)->GetObjectClass(env, plan);
    jfieldID jfid = (*env)->GetFieldID(env, clazz, "address", "J");
    return (fftw_plan) (*env)->GetLongField(env, plan, jfid);
}
