#include <jni.h>
#include <fftw3.h>

#include <stdlib.h>

#ifndef JFFTW_COMMON_H
#define JFFTW_COMMON_H

typedef struct {
    jdoubleArray j;
    double* c;
} array_pair_s;

typedef array_pair_s* array_pair;

static fftw_plan get_fftw_plan(JNIEnv *env, jobject jobj) {
    jclass c = (*env)->GetObjectClass(env, jobj);
    jfieldID jfid = (*env)->GetFieldID(env, c, "planPointer", "J");
    return (fftw_plan) (*env)->GetLongField(env, jobj, jfid);
}

static array_pair get_value_array(JNIEnv *env, jobject jobj) {
    jclass c = (*env)->GetObjectClass(env, jobj);
    jfieldID jfid = (*env)->GetFieldID(env, c, "arr", "[D");
    jdoubleArray jda = (jdoubleArray) (*env)->GetObjectField(env, jobj, jfid);
    double* d = (double*) (*env)->GetDoubleArrayElements(env, jda, 0);
    array_pair apt = (array_pair) malloc(sizeof(array_pair));
    apt->c = d;
    apt->j = jda;
    return apt;
}

static void release_array_pair(JNIEnv *env, array_pair ap, jint mode) {
    (*env)->ReleaseDoubleArrayElements(env, ap->j, ap->c, mode);
    free(ap);
}

#endif //JFFTW_COMMON_H
