#include "jfftw_Interface.h"

#include <stdlib.h>
#include <string.h>

#include <jni.h>
#include <fftw3.h>

void * get_array(JNIEnv *env, jobject jbuff) {
    return (*env)->GetDirectBufferAddress(env, jbuff);
}

fftw_plan get_fftw_plan(JNIEnv *env, jobject p) {
    jclass c = (*env)->GetObjectClass(env, p);
    jfieldID f = (*env)->GetFieldID(env, c, "address", "J");
    return (fftw_plan) (*env)->GetLongField(env, p, f);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_alloc_real
 * Signature: (J)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_jfftw_Interface_jfftw_1alloc_1real
  (JNIEnv *env, jclass clazz, jlong N) {
    double *cbuff = fftw_alloc_real(N);
    return (*env)->NewDirectByteBuffer(env, cbuff, sizeof(double) * N);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_alloc_complex
 * Signature: (J)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_jfftw_Interface_jfftw_1alloc_1complex
  (JNIEnv *env, jclass clazz, jlong N) {
    fftw_complex *cbuff = fftw_alloc_complex(N);
    return (*env)->NewDirectByteBuffer(env, cbuff, sizeof(fftw_complex) * N);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_alignment_of
 * Signature: (Ljava/nio/DoubleBuffer;)I
 */
JNIEXPORT jint JNICALL Java_jfftw_Interface_jfftw_1alignment_1of
  (JNIEnv *env, jclass clazz, jobject jarr) {
    double *carr = (double *) get_array(env, jarr);
    jint alignment = fftw_alignment_of(carr);
    return alignment;
}

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
 * Method:    jfftw_cost
 * Signature: (Ljfftw/Plan;)D
 */
JNIEXPORT jdouble JNICALL Java_jfftw_Interface_jfftw_1cost
  (JNIEnv *env, jclass clazz, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    return fftw_cost(p);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_destroy_plan
 * Signature: (Ljfftw/Plan;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1destroy_1plan
  (JNIEnv *env, jclass clazz, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    fftw_destroy_plan(p);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_estimate_cost
 * Signature: (Ljfftw/Plan;)D
 */
JNIEXPORT jdouble JNICALL Java_jfftw_Interface_jfftw_1estimate_1cost
  (JNIEnv *env, jclass clazz, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    return fftw_estimate_cost(p);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute
 * Signature: (Ljfftw/Plan;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute
  (JNIEnv *env, jclass clazz, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    fftw_execute(p);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_dft
 * Signature: (Ljfftw/Plan;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1dft
  (JNIEnv *env, jclass clazz, jobject plan, jobject ji, jobject jo) {
    fftw_plan p = get_fftw_plan(env, plan);
    fftw_complex *ci = (fftw_complex *) get_array(env, ji);
    fftw_complex *co = (fftw_complex *) get_array(env, jo);
    fftw_execute_dft(p, ci, co);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_dft_c2r
 * Signature: (Ljfftw/Plan;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1dft_1c2r
  (JNIEnv *env, jclass clazz, jobject plan, jobject ji, jobject jo) {
    fftw_plan p = get_fftw_plan(env, plan);
    fftw_complex *ci = (fftw_complex *) get_array(env, ji);
    double *ro = get_array(env, jo);
    fftw_execute_dft_c2r(p, ci, ro);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_dft_r2c
 * Signature: (Ljfftw/Plan;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1dft_1r2c
  (JNIEnv *env, jclass clazz, jobject plan, jobject ji, jobject jo) {
    fftw_plan p = get_fftw_plan(env, plan);
    double *ri = get_array(env, ji);
    fftw_complex *co = (fftw_complex *) get_array(env, jo);
    fftw_execute_dft_r2c(p, ri, co);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_r2r
 * Signature: (Ljfftw/Plan;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1r2r
  (JNIEnv *env, jclass clazz, jobject plan, jobject ji, jobject jo) {
    fftw_plan p = get_fftw_plan(env, plan);
    double *ri = get_array(env, ji);
    double *ro = get_array(env, jo);
    fftw_execute_r2r(p, ri, ro);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_export_wisdom_to_filename
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_Interface_jfftw_1export_1wisdom_1to_1filename
  (JNIEnv *env, jclass clazz, jstring s) {
    const char *fn = (*env)->GetStringUTFChars(env, s, 0);
    jboolean success = fftw_export_wisdom_to_filename(fn) == 1;
    (*env)->ReleaseStringUTFChars(env, s, fn);
    return success;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_export_wisdom_to_string
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_jfftw_Interface_jfftw_1export_1wisdom_1to_1string
  (JNIEnv *env, jclass clazz) {
    char *s = fftw_export_wisdom_to_string();
    jstring jstr = (*env)->NewStringUTF(env, s);
    free(s);
    return jstr;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_flops
 * Signature: (Ljfftw/Plan;[D[D[D)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1flops
  (JNIEnv *env, jclass clazz, jobject plan, jdoubleArray a1, jobject a2, jobject a3) {
    printf("%s\n", "Java_jfftw_Interface_jfftw_1flops not implemented.");
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_forget_wisdom
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1forget_1wisdom
  (JNIEnv *env, jclass clazz) {
    fftw_forget_wisdom();
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_fprint_plan
 * Signature: (Ljfftw/Plan;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1fprint_1plan
  (JNIEnv *env, jclass clazz, jobject plan, jobject s) {
    fftw_plan p = get_fftw_plan(env, plan);
    const char *path = (*env)->GetStringUTFChars(env, s, 0);
    FILE *f = fopen(path, "w+");
    fftw_fprint_plan(p, f);
    fclose(f);
    (*env)->ReleaseStringUTFChars(env, s, path);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_import_system_wisdom
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_Interface_jfftw_1import_1system_1wisdom
  (JNIEnv *env, jclass clazz) {
    return fftw_import_system_wisdom() == 1;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_import_wisdom_from_file
 * Signature: (Ljava/io/File;)Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_Interface_jfftw_1import_1wisdom_1from_1file
  (JNIEnv *env, jclass clazz, jobject file) {
    jmethodID jmid = (*env)->GetMethodID(env, file, "getAbsolutePath", "java/lang/String;");
    jobject jstr = (*env)->CallObjectMethod(env, file, jmid);
    const char *path = (*env)->GetStringUTFChars(env, jstr, 0);
    FILE *f = fopen(path, "w+");
    jboolean success = fftw_import_wisdom_from_file(f) == 1;
    fclose(f);
    (*env)->ReleaseStringUTFChars(env, jstr, path);
    return success;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_import_wisdom_from_filename
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_Interface_jfftw_1import_1wisdom_1from_1filename
  (JNIEnv *env, jclass clazz, jstring s) {
    const char *fn = (*env)->GetStringUTFChars(env, s, 0);
    jboolean success = fftw_import_wisdom_from_filename(fn) == 1;
    (*env)->ReleaseStringUTFChars(env, s, fn);
    return success;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_import_wisdom_from_string
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_Interface_jfftw_1import_1wisdom_1from_1string
  (JNIEnv *env, jclass clazz, jstring s) {
    const char *str = (*env)->GetStringUTFChars(env, s, 0);
    jboolean success = fftw_import_wisdom_from_string(str) == 1;
    (*env)->ReleaseStringUTFChars(env, s, str);
    return success;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_init_threads
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_Interface_jfftw_1init_1threads
  (JNIEnv *env, jclass clazz) {
    return fftw_init_threads() != 0;
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
  (JNIEnv *env, jclass clazz, jint nthreads) {
    fftw_plan_with_nthreads(nthreads);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_set_timelimit
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1set_1timelimit
  (JNIEnv *env, jclass clazz, jdouble t) {
    fftw_set_timelimit(t);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_print_plan
 * Signature: (Ljfftw/Plan;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1print_1plan
  (JNIEnv *env, jclass clazz, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    fftw_print_plan(p);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_sprint_plan
 * Signature: (Ljfftw/Plan;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_jfftw_Interface_jfftw_1sprint_1plan
  (JNIEnv *env, jclass clazz, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    char *str = p == NULL ? "null plan" : fftw_sprint_plan(p);
    jstring jstr = (*env)->NewStringUTF(env, str);
    return jstr;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft
 * Signature: (I[ILjava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft
  (JNIEnv *env, jclass clazz, jint rank, jintArray n, jobject ji, jobject jo, jint sign, jint flags) {
    int *nums = (int *) (*env)->GetPrimitiveArrayCritical(env, n, 0);
    fftw_complex *ci = (fftw_complex *) get_array(env, ji);
    fftw_complex *co = (fftw_complex *) get_array(env, jo);
    fftw_plan p = fftw_plan_dft(rank, nums, ci, co, sign, flags);
    (*env)->ReleasePrimitiveArrayCritical(env, n, nums, JNI_COMMIT);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_1d
 * Signature: (ILjava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_11d
  (JNIEnv *env, jclass clazz, jint n, jobject ji, jobject jo, jint sign, jint flags) {
    fftw_complex *ci = (fftw_complex *) get_array(env, ji);
    fftw_complex *co = (fftw_complex *) get_array(env, jo);
    fftw_plan p = fftw_plan_dft_1d(n, ci, co, sign, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_2d
 * Signature: (IILjava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_12d
  (JNIEnv *env, jclass clazz, jint n0, jint n1, jobject ji, jobject jo, jint sign, jint flags) {
    fftw_complex *ci = (fftw_complex *) get_array(env, ji);
    fftw_complex *co = (fftw_complex *) get_array(env, jo);
    fftw_plan p = fftw_plan_dft_2d(n0, n1, ci, co, sign, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_3d
 * Signature: (IIILjava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_13d
  (JNIEnv *env, jclass clazz, jint n0, jint n1, jint n2, jobject ji, jobject jo, jint sign, jint flags) {
    fftw_complex *ci = (fftw_complex *) get_array(env, ji);
    fftw_complex *co = (fftw_complex *) get_array(env, jo);
    fftw_plan p = fftw_plan_dft_3d(n0, n1, n2, ci, co, sign, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_c2r
 * Signature: (I[ILjava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1c2r
  (JNIEnv *env, jclass clazz, jint rank, jintArray n, jobject ji, jobject jo, jint flags) {
    int *nums = (int *) (*env)->GetPrimitiveArrayCritical(env, n, 0);
    fftw_complex *ci = (fftw_complex *) get_array(env, ji);
    double *ro = (double *) get_array(env, jo);
    fftw_plan p = fftw_plan_dft_c2r(rank, nums, ci, ro, flags);
    (*env)->ReleasePrimitiveArrayCritical(env, n, nums, JNI_COMMIT);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_c2r_1d
 * Signature: (ILjava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1c2r_11d
  (JNIEnv *env, jclass clazz, jint n, jobject ji, jobject jo, jint flags) {
    fftw_complex *ci = (fftw_complex *) get_array(env, ji);
    double *ro = (double *) get_array(env, jo);
    fftw_plan p = fftw_plan_dft_c2r_1d(n, ci, ro, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_c2r_2d
 * Signature: (IILjava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1c2r_12d
  (JNIEnv *env, jclass clazz, jint n0, jint n1, jobject ji, jobject jo, jint flags) {
    fftw_complex *ci = (fftw_complex *) get_array(env, ji);
    double *ro = (double *) get_array(env, jo);
    fftw_plan p = fftw_plan_dft_c2r_2d(n0, n1, ci, ro, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_c2r_3d
 * Signature: (IIILjava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1c2r_13d
  (JNIEnv *env, jclass clazz, jint n0, jint n1, jint n2, jobject ji, jobject jo, jint flags) {
    fftw_complex *ci = (fftw_complex *) get_array(env, ji);
    double *ro = (double *) get_array(env, jo);
    fftw_plan p = fftw_plan_dft_c2r_3d(n0, n1, n2, ci, ro, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_r2c
 * Signature: (I[ILjava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1r2c
  (JNIEnv *env, jclass clazz, jint rank, jintArray n, jobject ji, jobject jo, jint flags) {
    int *nums = (*env)->GetPrimitiveArrayCritical(env, n, 0);
    double *ri = (double *) get_array(env, ji);
    fftw_complex *co = (fftw_complex *) get_array(env, jo);
    fftw_plan p = fftw_plan_dft_r2c(rank, nums, ri, co, flags);
    (*env)->ReleasePrimitiveArrayCritical(env, n, nums, JNI_COMMIT);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_r2c_1d
 * Signature: (ILjava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1r2c_11d
  (JNIEnv *env, jclass clazz, jint n, jobject ji, jobject jo, jint flags) {
    double *ri = (double *) get_array(env, ji);
    fftw_complex *co = (fftw_complex *) get_array(env, jo);
    fftw_plan p = fftw_plan_dft_r2c_1d(n, ri, co, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_r2c_2d
 * Signature: (IILjava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1r2c_12d
  (JNIEnv *env, jclass clazz, jint n0, jint n1, jobject ji, jobject jo, jint flags) {
    double *ri = (double *) get_array(env, ji);
    fftw_complex *co = (fftw_complex *) get_array(env, jo);
    fftw_plan p = fftw_plan_dft_r2c_2d(n0, n1, ri, co, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_r2c_3d
 * Signature: (IIILjava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1r2c_13d
  (JNIEnv *env, jclass clazz, jint n0, jint n1, jint n2, jobject ji, jobject jo, jint flags) {
    double *ri = (double *) get_array(env, ji);
    fftw_complex *co = (fftw_complex *) get_array(env, jo);
    fftw_plan p = fftw_plan_dft_r2c_3d(n0, n1, n2, ri, co, flags);
    return (jlong) p;
}
