#include "jfftw_Interface.h"

#include <stdlib.h>

#include <jni.h>
#include <fftw3.h>

#define _PRE_EXECUTE \
fftw_plan p = get_fftw_plan(env, plan); \
array_pair in = get_value(env, i); \
array_pair out = get_value(env, o);

#define _POST_EXECUTE \
release_array_pair(env, in, JNI_COMMIT); \
release_array_pair(env, out, JNI_COMMIT);

#define _PRE_PLAN \
array_pair in = get_value(env, i); \
array_pair out = get_value(env, o);

#define _POST_PLAN \
release_array_pair(env, in, JNI_COMMIT); \
release_array_pair(env, out, JNI_COMMIT); \
return (jlong) p;

jboolean is_buffer(JNIEnv *env, jobject jobj) {
    jclass jcls = (*env)->GetObjectClass(env, jobj);
    jmethodID jmid = (*env)->GetMethodID(env, jcls, "isBuffer", "()Z");
    return (*env)->CallBooleanMethod(env, jobj, jmid);
}

fftw_plan get_fftw_plan(JNIEnv *env, jobject jobj) {
    jclass c = (*env)->GetObjectClass(env, jobj);
    jfieldID jfid = (*env)->GetFieldID(env, c, "planPointer", "J");
    return (fftw_plan) (*env)->GetLongField(env, jobj, jfid);
}

array_pair get_value_array(JNIEnv *env, jobject jobj) {
    jclass c = (*env)->GetObjectClass(env, jobj);
    jfieldID jfid = (*env)->GetFieldID(env, c, "arr", "[D");
    jdoubleArray jda = (jdoubleArray) (*env)->GetObjectField(env, jobj, jfid);
    double* d = (*env)->GetDoubleArrayElements(env, jda, 0);
    array_pair apt = (array_pair) malloc(sizeof(array_pair));
    apt->c = d;
    apt->j = jda;
    return apt;
}

array_pair get_value_buffer(JNIEnv *env, jobject jobj) {
    jclass c = (*env)->GetObjectClass(env, jobj);
    jfieldID jfid = (*env)->GetFieldID(env, c, "buff", "Ljava/nio/ByteBuffer;");
    jobject jbuf = (*env)->GetObjectField(env, jobj, jfid);
    double* d = (double*) (*env)->GetDirectBufferAddress(env, jbuf);
    array_pair apt = (array_pair) malloc(sizeof(array_pair));
    apt->c = d;
    apt->j = NULL;
    return apt;
}

array_pair get_value(JNIEnv *env, jobject jobj) {
    if (is_buffer(env, jobj))
        return get_value_buffer(env, jobj);
    else
        return get_value_array(env, jobj);
}

void release_array_pair(JNIEnv *env, array_pair ap, jint mode) {
    if (ap->j != NULL)
        (*env)->ReleaseDoubleArrayElements(env, ap->j, ap->c, mode);
    free(ap);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_alignment_of
 * Signature: (Ljfftw/Value)I
 */
JNIEXPORT jint JNICALL Java_jfftw_Interface_jfftw_1alignment_1of
        (JNIEnv *env, jclass class, jobject v) {
    array_pair ap = get_value(env, v);
    jint a = fftw_alignment_of(ap->c);
    release_array_pair(env, ap, JNI_COMMIT);
    return a;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_allocate_complex_buffer
 * Signature: (J)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_jfftw_Interface_jfftw_1allocate_1complex_1buffer
  (JNIEnv *env, jclass class, jlong n) {
      fftw_complex* buff = fftw_malloc(n * sizeof(fftw_complex));
      jobject cbuf = (*env)->NewDirectByteBuffer(env, buff, n * sizeof(fftw_complex));
      return cbuf;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_allocate_real_buffer
 * Signature: (J)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_jfftw_Interface_jfftw_1allocate_1real_1buffer
  (JNIEnv *env, jclass class, jlong n) {
    double* buff = fftw_malloc(n * sizeof(double));
    jobject cbuf = (*env)->NewDirectByteBuffer(env, buff, n * sizeof(double));
    return cbuf;
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
 * Method:    jfftw_execute
 * Signature: (Ljfftw/Plan;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute
  (JNIEnv *env, jclass class, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    fftw_execute(p);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_dft
 * Signature: (Ljfftw/Plan;Ljfftw/Complex;Ljfftw/Complex;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1dft
  (JNIEnv *env, jclass class, jobject plan, jobject i, jobject o) {
      _PRE_EXECUTE
      fftw_execute_dft(p, (fftw_complex*) in->c, (fftw_complex*) out->c);
      _POST_EXECUTE
      }

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_dft_c2r
 * Signature: (Ljfftw/Plan;Ljfftw/Complex;Ljfftw/Real;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1dft_1c2r
  (JNIEnv *env, jclass class, jobject plan, jobject i, jobject o) {
    _PRE_EXECUTE
    fftw_execute_dft_c2r(p, (fftw_complex*) in->c, out->c);
    _POST_EXECUTE
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_dft_r2c
 * Signature: (Ljfftw/Plan;Ljfftw/Real;Ljfftw/Complex;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1dft_1r2c
  (JNIEnv *env, jclass class, jobject plan, jobject i, jobject o) {
    _PRE_EXECUTE
    fftw_execute_dft_r2c(p, in->c, (fftw_complex*) out->c);
    _POST_EXECUTE
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_r2r
 * Signature: (Ljfftw/Plan;Ljfftw/Real;Ljfftw/Real;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1r2r
  (JNIEnv *env, jclass class, jobject plan, jobject i, jobject o) {
    _PRE_EXECUTE
    fftw_execute_r2r(p, in->c, out->c);
    _POST_EXECUTE
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_export_wisdom_to_file
 * Signature: (Ljava/io/File;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1export_1wisdom_1to_1file
  (JNIEnv *env, jclass class, jobject file) {
    jmethodID jmid = (*env)->GetMethodID(env, file, "getAbsolutePath", "java/lang/String;");
    jobject jstr = (*env)->CallObjectMethod(env, file, jmid);
    const char* path = (*env)->GetStringUTFChars(env, jstr, 0);
    FILE *f = fopen(path, "w+");
    fftw_export_wisdom_to_file(f);
    fclose(f);
    (*env)->ReleaseStringUTFChars(env, jstr, path);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_export_wisdom_to_filename
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1export_1wisdom_1to_1filename
  (JNIEnv *env, jclass class, jstring s) {
    const char* fn = (*env)->GetStringUTFChars(env, s, 0);
    fftw_export_wisdom_to_filename(fn);
    (*env)->ReleaseStringUTFChars(env, s, fn);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_export_wisdom_to_string
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_jfftw_Interface_jfftw_1export_1wisdom_1to_1string
  (JNIEnv *env, jclass class) {
    char* s = fftw_export_wisdom_to_string();
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
        (JNIEnv *env, jclass class, jobject plan, jdoubleArray a1, jdoubleArray a2, jdoubleArray a3) {
    printf("%s\n", "Java_jfftw_Interface_jfftw_1flops not implemented.");
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_forget_wisdom
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1forget_1wisdom
  (JNIEnv *env, jclass class) {
    fftw_forget_wisdom();
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_fprint_plan
 * Signature: (Ljfftw/Plan;Ljava/io/File;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1fprint_1plan
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
 * Class:     jfftw_Interface
 * Method:    jfftw_import_system_wisdom
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1import_1system_1wisdom
  (JNIEnv *env, jclass class) {
    fftw_import_system_wisdom();
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_import_wisdom_from_file
 * Signature: (Ljava/io/File;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1import_1wisdom_1from_1file
  (JNIEnv *env, jclass class, jobject file) {
    jmethodID jmid = (*env)->GetMethodID(env, file, "getAbsolutePath", "java/lang/String;");
    jobject jstr = (*env)->CallObjectMethod(env, file, jmid);
    const char* path = (*env)->GetStringUTFChars(env, jstr, 0);
    FILE *f = fopen(path, "w+");
    fftw_import_wisdom_from_file(f);
    fclose(f);
    (*env)->ReleaseStringUTFChars(env, jstr, path);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_import_wisdom_from_filename
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1import_1wisdom_1from_1filename
  (JNIEnv *env, jclass class, jstring s) {
    const char* fn = (*env)->GetStringUTFChars(env, s, 0);
    fftw_import_wisdom_from_filename(fn);
    (*env)->ReleaseStringUTFChars(env, s, fn);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_import_wisdom_from_string
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1import_1wisdom_1from_1string
  (JNIEnv *env, jclass class, jstring s) {
    const char* str = (*env)->GetStringUTFChars(env, s, 0);
    fftw_import_wisdom_from_string(str);
    (*env)->ReleaseStringUTFChars(env, s, str);
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

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_print_plan
 * Signature: (Ljfftw/Plan;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1print_1plan
  (JNIEnv *env, jclass class, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    fftw_print_plan(p);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_sprint_plan
 * Signature: (Ljfftw/Plan;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_jfftw_Interface_jfftw_1sprint_1plan
  (JNIEnv *env, jclass class, jobject plan) {
    fftw_plan p = get_fftw_plan(env, plan);
    char* str = p == NULL ? "empty plan" : fftw_sprint_plan(p);
    jstring jstr = (*env)->NewStringUTF(env, str);
    return jstr;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft
 * Signature: (I[ILjfftw/Complex;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft
  (JNIEnv *env, jclass class, jint rank, jintArray n, jobject i, jobject o, jint sign, jint flags) {
    jint* nums = (*env)->GetIntArrayElements(env, n, 0);
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft(rank, nums, (fftw_complex*) in->c, (fftw_complex*) out->c, sign, flags);
    (*env)->ReleaseIntArrayElements(env, n, nums, JNI_ABORT);
    _POST_PLAN
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_1d
 * Signature: (ILjfftw/Complex;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_11d
  (JNIEnv *env, jclass class, jint n, jobject i, jobject o, jint sign, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_1d(n, (fftw_complex*) in->c, (fftw_complex*) out->c, sign, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_2d
 * Signature: (IILjfftw/Complex;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_12d
  (JNIEnv *env, jclass class, jint n0, jint n1, jobject i, jobject o, jint sign, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_2d(n0, n1, (fftw_complex*) in->c, (fftw_complex*) out->c, sign, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_3d
 * Signature: (IIILjfftw/Complex;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_13d
  (JNIEnv *env, jclass class, jint n0, jint n1, jint n2, jobject i, jobject o, jint sign, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_3d(n0, n1, n2, (fftw_complex*) in->c, (fftw_complex*) out->c, sign, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_c2r
 * Signature: (I[ILjfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1c2r
  (JNIEnv *env, jclass class, jint rank, jintArray n, jobject i, jobject o, jint flags) {
    int* nums = (*env)->GetIntArrayElements(env, n, 0);
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_c2r(rank, nums, (fftw_complex*) in->c, out->c, flags);
    (*env)->ReleaseIntArrayElements(env, n, nums, JNI_ABORT);
    _POST_PLAN
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_c2r_1d
 * Signature: (ILjfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1c2r_11d
  (JNIEnv *env, jclass class, jint n, jobject i, jobject o, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_c2r_1d(n, (fftw_complex*) in->c, out->c, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_c2r_2d
 * Signature: (IILjfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1c2r_12d
  (JNIEnv *env, jclass class, jint n0, jint n1, jobject i, jobject o, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_c2r_2d(n0, n1, (fftw_complex*) in->c, out->c, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_c2r_3d
 * Signature: (IIILjfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1c2r_13d
  (JNIEnv *env, jclass class, jint n0, jint n1, jint n2, jobject i, jobject o, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_c2r_3d(n0, n1, n2, (fftw_complex*) in->c, out->c, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_r2c
 * Signature: (I[ILjfftw/Real;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1r2c
  (JNIEnv *env, jclass class, jint rank, jintArray n, jobject i, jobject o, jint flags) {
    int* nums = (*env)->GetIntArrayElements(env, n, 0);
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_r2c(rank, nums, in->c, (fftw_complex*) out->c, flags);
    (*env)->ReleaseIntArrayElements(env, n, nums, JNI_ABORT);
    _POST_PLAN
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_r2c_1d
 * Signature: (ILjfftw/Real;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1r2c_11d
  (JNIEnv *env, jclass class, jint n, jobject i, jobject o, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_r2c_1d(n, in->c, (fftw_complex*) out->c, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_r2c_2d
 * Signature: (IILjfftw/Real;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1r2c_12d
  (JNIEnv *env, jclass class, jint n0, jint n1, jobject i, jobject o, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_r2c_2d(n0, n1, in->c, (fftw_complex*) out->c, flags);
    _POST_PLAN
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_r2c_3d
 * Signature: (IIILjfftw/Real;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1r2c_13d
  (JNIEnv *env, jclass class, jint n0, jint n1, jint n2, jobject i, jobject o, jint flags) {
    _PRE_PLAN
    fftw_plan p = fftw_plan_dft_r2c_3d(n0, n1, n2, in->c, (fftw_complex*) out->c, flags);
    _POST_PLAN
}

