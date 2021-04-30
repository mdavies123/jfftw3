#include <fftw3.h>
#include <jni.h>

double *get_direct_buffer(JNIEnv *env, jobject jbuff);

double *get_array(JNIEnv *env, jdoubleArray jarr);

void release_array(JNIEnv *env, jobject jarr, void *carr, jint commit);

fftw_plan get_fftw_plan(JNIEnv *env, jobject plan);
