#include "jfftw_Interface.h"

#include <stdlib.h>
#include <string.h>

#include <jni.h>
#include <fftw3.h>

#define _GET_BUFFERS \
double *in = get_buffer(env, i); \
double *out = get_buffer(env, o);

#define _PRE_EXECUTE \
fftw_plan p = get_fftw_plan(env, plan); \
_GET_BUFFERS

#define _GET_GURU_DIMS \
fftw_iodim *dims = malloc(sizeof(fftw_iodim) * rank); \
fftw_iodim *hmdims = NULL; \
if (howmany > 0) \
    hmdims = malloc(sizeof(fftw_iodim) * howmany); \
get_dims(env, rank, jdims, howmany, jhmdims, dims, hmdims);

#define _GET_GURU64_DIMS \
fftw_iodim64 *dims = malloc(sizeof(fftw_iodim64) * rank); \
fftw_iodim64 *hmdims = NULL; \
if (howmany > 0) \
    hmdims = malloc(sizeof(fftw_iodim64) * howmany); \
get_dims64(env, rank, jdims, howmany, jhmdims, dims, hmdims);

double * get_buffer(JNIEnv *env, jobject v) {
    jclass c = (*env)->GetObjectClass(env, v);
    jfieldID f = (*env)->GetFieldID(env, c, "buff", "Ljava/nio/DoubleBuffer;");
    jobject b = (*env)->GetObjectField(env, v, f);
    return (double *) (*env)->GetDirectBufferAddress(env, b);
}

fftw_plan get_fftw_plan(JNIEnv *env, jobject p) {
    jclass c = (*env)->GetObjectClass(env, p);
    jfieldID f = (*env)->GetFieldID(env, c, "address", "J");
    return (fftw_plan) (*env)->GetLongField(env, p, f);
}

void get_dims
  (JNIEnv *env, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, fftw_iodim *dims, fftw_iodim *hmdims) {
    jclass jcls;
    jfieldID jfid;
    for (int i = 0; i < rank; i++) {
        jobject jdim = (*env)->GetObjectArrayElement(env, jdims, i);
        jcls = (*env)->GetObjectClass(env, jdim);
        jfid = (*env)->GetFieldID(env, jcls, "is", "I");
        dims[i].is = (*env)->GetIntField(env, jdim, jfid);
        jfid = (*env)->GetFieldID(env, jcls, "os", "I");
        dims[i].os = (*env)->GetIntField(env, jdim, jfid);
        jfid = (*env)->GetFieldID(env, jcls, "n", "I");
        dims[i].n = (*env)->GetIntField(env, jdim, jfid);
    }
    for (int i = 0; i < howmany; i++) {
        jobject jhmdim = (*env)->GetObjectArrayElement(env, jhmdims, i);
        jcls = (*env)->GetObjectClass(env, jhmdim);
        jfid = (*env)->GetFieldID(env, jcls, "is", "I");
        hmdims[i].is = (*env)->GetIntField(env, jhmdim, jfid);
        jfid = (*env)->GetFieldID(env, jcls, "os", "I");
        hmdims[i].os = (*env)->GetIntField(env, jhmdim, jfid);
        jfid = (*env)->GetFieldID(env, jcls, "n", "I");
        hmdims[i].n = (*env)->GetIntField(env, jhmdim, jfid);
    }
}

void get_dims64
  (JNIEnv *env, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, fftw_iodim64 *dims, fftw_iodim64 *hmdims) {
    jclass jcls;
    jfieldID jfid;
    for (int i = 0; i < rank; i++) {
        jobject jdim = (*env)->GetObjectArrayElement(env, jdims, i);
        jcls = (*env)->GetObjectClass(env, jdim);
        jfid = (*env)->GetFieldID(env, jcls, "is", "J");
        dims[i].is = (*env)->GetLongField(env, jdim, jfid);
        jfid = (*env)->GetFieldID(env, jcls, "os", "J");
        dims[i].os = (*env)->GetLongField(env, jdim, jfid);
        jfid = (*env)->GetFieldID(env, jcls, "n", "J");
        dims[i].n = (*env)->GetLongField(env, jdim, jfid);
    }
    for (int i = 0; i < howmany; i++) {
        jobject jhmdim = (*env)->GetObjectArrayElement(env, jhmdims, i);
        jcls = (*env)->GetObjectClass(env, jhmdim);
        jfid = (*env)->GetFieldID(env, jcls, "is", "J");
        hmdims[i].is = (*env)->GetLongField(env, jhmdim, jfid);
        jfid = (*env)->GetFieldID(env, jcls, "os", "J");
        hmdims[i].os = (*env)->GetLongField(env, jhmdim, jfid);
        jfid = (*env)->GetFieldID(env, jcls, "n", "J");
        hmdims[i].n = (*env)->GetLongField(env, jhmdim, jfid);
    }
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_alignment_of
 * Signature: (Ljfftw/Value)I
 */
JNIEXPORT jint JNICALL Java_jfftw_Interface_jfftw_1alignment_1of
  (JNIEnv *env, jclass class, jobject v) {
    double *d = get_buffer(env, v);
    return fftw_alignment_of(d);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_allocate_complex_buffer
 * Signature: (J)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_jfftw_Interface_jfftw_1allocate_1complex_1buffer
  (JNIEnv *env, jclass class, jlong n) {
    fftw_complex *buff = fftw_malloc(n * sizeof(fftw_complex));
    jobject jbuff = (*env)->NewDirectByteBuffer(env, buff, n * sizeof(fftw_complex));
    return jbuff;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_allocate_real_buffer
 * Signature: (J)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_jfftw_Interface_jfftw_1allocate_1real_1buffer
  (JNIEnv *env, jclass class, jlong n) {
    double *buff = fftw_malloc(n * sizeof(double));
    jobject jbuff = (*env)->NewDirectByteBuffer(env, buff, n * sizeof(double));
    return jbuff;
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
    fftw_execute_dft(p, (fftw_complex *) in, (fftw_complex *) out);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_dft_c2r
 * Signature: (Ljfftw/Plan;Ljfftw/Complex;Ljfftw/Real;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1dft_1c2r
  (JNIEnv *env, jclass class, jobject plan, jobject i, jobject o) {
    _PRE_EXECUTE
    fftw_execute_dft_c2r(p, (fftw_complex *) in, out);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_dft_r2c
 * Signature: (Ljfftw/Plan;Ljfftw/Real;Ljfftw/Complex;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1dft_1r2c
  (JNIEnv *env, jclass class, jobject plan, jobject i, jobject o) {
    _PRE_EXECUTE
    fftw_execute_dft_r2c(p, in, (fftw_complex *) out);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_r2r
 * Signature: (Ljfftw/Plan;Ljfftw/Real;Ljfftw/Real;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1r2r
  (JNIEnv *env, jclass class, jobject plan, jobject i, jobject o) {
    _PRE_EXECUTE
    fftw_execute_r2r(p, in, out);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_split_dft
 * Signature: (Ljfftw/Plan;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1split_1dft
  (JNIEnv *env, jclass class, jobject plan, jobject ri_buff, jobject ii_buff, jobject ro_buff, jobject io_buff) {
    fftw_plan p = get_fftw_plan(env, plan);
    double *ri = (*env)->GetDirectBufferAddress(env, ri_buff);
    double *ii = (*env)->GetDirectBufferAddress(env, ii_buff);
    double *ro = (*env)->GetDirectBufferAddress(env, ro_buff);
    double *io = (*env)->GetDirectBufferAddress(env, io_buff);
    fftw_execute_split_dft(p, ri, ii, ro, io);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_split_dft_c2r
 * Signature: (Ljfftw/Plan;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1split_1dft_1c2r
  (JNIEnv *env, jclass class, jobject plan, jobject ri_buff, jobject ii_buff, jobject ro_buff) {
    fftw_plan p = get_fftw_plan(env, plan);
    double *ri = (*env)->GetDirectBufferAddress(env, ri_buff);
    double *ii = (*env)->GetDirectBufferAddress(env, ii_buff);
    double *ro = (*env)->GetDirectBufferAddress(env, ro_buff);
    fftw_execute_split_dft_c2r(p, ri, ii, ro);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_execute_split_dft_r2c
 * Signature: (Ljfftw/Plan;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1execute_1split_1dft_1r2c
  (JNIEnv *env, jclass class, jobject plan, jobject ri_buff, jobject ro_buff, jobject io_buff) {
    fftw_plan p = get_fftw_plan(env, plan);
    double *ri = (*env)->GetDirectBufferAddress(env, ri_buff);
    double *ro = (*env)->GetDirectBufferAddress(env, ro_buff);
    double *io = (*env)->GetDirectBufferAddress(env, io_buff);
    fftw_execute_split_dft_c2r(p, ri, ro, io);
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_export_wisdom_to_filename
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_Interface_jfftw_1export_1wisdom_1to_1filename
  (JNIEnv *env, jclass class, jstring s) {
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
  (JNIEnv *env, jclass class) {
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
 * Signature: (Ljfftw/Plan;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_jfftw_Interface_jfftw_1fprint_1plan
  (JNIEnv *env, jclass class, jobject plan, jobject s) {
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
  (JNIEnv *env, jclass class) {
    return fftw_import_system_wisdom() == 1;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_import_wisdom_from_file
 * Signature: (Ljava/io/File;)Z
 */
JNIEXPORT jboolean JNICALL Java_jfftw_Interface_jfftw_1import_1wisdom_1from_1file
  (JNIEnv *env, jclass class, jobject file) {
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
  (JNIEnv *env, jclass class, jstring s) {
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
  (JNIEnv *env, jclass class, jstring s) {
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
  (JNIEnv *env, jclass class) {
    return fftw_init_threads() != 0;
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
    char *str = p == NULL ? "empty plan" : fftw_sprint_plan(p);
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
    int *nums = (*env)->GetPrimitiveArrayCritical(env, n, 0);
    _GET_BUFFERS
    fftw_plan p = fftw_plan_dft(rank, nums, (fftw_complex *) in, (fftw_complex *) out, sign, flags);
    (*env)->ReleasePrimitiveArrayCritical(env, n, nums, JNI_COMMIT);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_1d
 * Signature: (ILjfftw/Complex;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_11d
  (JNIEnv *env, jclass class, jint n, jobject i, jobject o, jint sign, jint flags) {
    _GET_BUFFERS
    fftw_plan p = fftw_plan_dft_1d(n, (fftw_complex *) in, (fftw_complex *) out, sign, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_2d
 * Signature: (IILjfftw/Complex;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_12d
  (JNIEnv *env, jclass class, jint n0, jint n1, jobject i, jobject o, jint sign, jint flags) {
    _GET_BUFFERS
    fftw_plan p = fftw_plan_dft_2d(n0, n1, (fftw_complex *) in, (fftw_complex *) out, sign, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_3d
 * Signature: (IIILjfftw/Complex;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_13d
  (JNIEnv *env, jclass class, jint n0, jint n1, jint n2, jobject i, jobject o, jint sign, jint flags) {
    _GET_BUFFERS
    fftw_plan p = fftw_plan_dft_3d(n0, n1, n2, (fftw_complex *) in, (fftw_complex *) out, sign, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_c2r
 * Signature: (I[ILjfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1c2r
  (JNIEnv *env, jclass class, jint rank, jintArray n, jobject i, jobject o, jint flags) {
    int *nums = (*env)->GetPrimitiveArrayCritical(env, n, 0);
    _GET_BUFFERS
    fftw_plan p = fftw_plan_dft_c2r(rank, nums, (fftw_complex *) in, out, flags);
    (*env)->ReleasePrimitiveArrayCritical(env, n, nums, JNI_COMMIT);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_c2r_1d
 * Signature: (ILjfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1c2r_11d
  (JNIEnv *env, jclass class, jint n, jobject i, jobject o, jint flags) {
    _GET_BUFFERS
    fftw_plan p = fftw_plan_dft_c2r_1d(n, (fftw_complex *) in, out, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_c2r_2d
 * Signature: (IILjfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1c2r_12d
  (JNIEnv *env, jclass class, jint n0, jint n1, jobject i, jobject o, jint flags) {
    _GET_BUFFERS
    fftw_plan p = fftw_plan_dft_c2r_2d(n0, n1, (fftw_complex *) in, out, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_c2r_3d
 * Signature: (IIILjfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1c2r_13d
  (JNIEnv *env, jclass class, jint n0, jint n1, jint n2, jobject i, jobject o, jint flags) {
    _GET_BUFFERS
    fftw_plan p = fftw_plan_dft_c2r_3d(n0, n1, n2, (fftw_complex *) in, out, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_r2c
 * Signature: (I[ILjfftw/Real;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1r2c
  (JNIEnv *env, jclass class, jint rank, jintArray n, jobject i, jobject o, jint flags) {
    int *nums = (*env)->GetPrimitiveArrayCritical(env, n, 0);
    _GET_BUFFERS
    fftw_plan p = fftw_plan_dft_r2c(rank, nums, in, (fftw_complex *) out, flags);
    (*env)->ReleasePrimitiveArrayCritical(env, n, nums, JNI_COMMIT);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_r2c_1d
 * Signature: (ILjfftw/Real;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1r2c_11d
  (JNIEnv *env, jclass class, jint n, jobject i, jobject o, jint flags) {
    _GET_BUFFERS
    fftw_plan p = fftw_plan_dft_r2c_1d(n, in, (fftw_complex *) out, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_r2c_2d
 * Signature: (IILjfftw/Real;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1r2c_12d
  (JNIEnv *env, jclass class, jint n0, jint n1, jobject i, jobject o, jint flags) {
    _GET_BUFFERS
    fftw_plan p = fftw_plan_dft_r2c_2d(n0, n1, in, (fftw_complex *) out, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_dft_r2c_3d
 * Signature: (IIILjfftw/Real;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1dft_1r2c_13d
  (JNIEnv *env, jclass class, jint n0, jint n1, jint n2, jobject i, jobject o, jint flags) {
    _GET_BUFFERS
    fftw_plan p = fftw_plan_dft_r2c_3d(n0, n1, n2, in, (fftw_complex *) out, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_guru_dft
 * Signature: (I[Ljfftw/Guru/Dimension;I[Ljfftw/Guru/Dimension;Ljfftw/Complex;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1guru_1dft
  (JNIEnv *env, jclass class, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, jobject in, jobject out, jint sign, jint flags) {
    _GET_GURU_DIMS
    fftw_complex *cin = (fftw_complex *) get_buffer(env, in);
    fftw_complex *cout = (fftw_complex *) get_buffer(env, out);
    fftw_plan p = fftw_plan_guru_dft(rank, dims, howmany, hmdims, cin, cout, sign, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_guru_dft_c2r
 * Signature: (I[Ljfftw/Guru/Dimension;I[Ljfftw/Guru/Dimension;Ljfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1guru_1dft_1c2r
  (JNIEnv *env, jclass class, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, jobject in, jobject out, jint flags) {
    _GET_GURU_DIMS
    fftw_complex *cin = (fftw_complex *) get_buffer(env, in);
    double *rout = get_buffer(env, out);
    fftw_plan p = fftw_plan_guru_dft_c2r(rank, dims, howmany, hmdims, cin, rout, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_guru_dft_r2c
 * Signature: (I[Ljfftw/Guru/Dimension;I[Ljfftw/Guru/Dimension;Ljfftw/Real;Ljfftw/Complex;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1guru_1dft_1r2c
  (JNIEnv *env, jclass class, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, jobject in, jobject out, jint flags) {
    _GET_GURU_DIMS
    double *rin = get_buffer(env, in);
    fftw_complex *cout = (fftw_complex *) get_buffer(env, out);
    fftw_plan p = fftw_plan_guru_dft_r2c(rank, dims, howmany, hmdims, rin, cout, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_guru_dft_split
 * Signature: (I[Ljfftw/Guru/Dimension;I[Ljfftw/Guru/Dimension;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1guru_1dft_1split
  (JNIEnv *env, jclass class, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, jobject ri_buff, jobject ii_buff, jobject ro_buff, jobject io_buff, jint flags) {
    _GET_GURU_DIMS
    double *ri, *ii, *ro, *io;
    ri = (double *) (*env)->GetDirectBufferAddress(env, ri_buff);
    ii = (double *) (*env)->GetDirectBufferAddress(env, ii_buff);
    ro = (double *) (*env)->GetDirectBufferAddress(env, ro_buff);
    io = (double *) (*env)->GetDirectBufferAddress(env, io_buff);
    fftw_plan p = fftw_plan_guru_split_dft(rank, dims, howmany, hmdims, ri, ii, ro, io, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_guru_dft_split_c2r
 * Signature: (I[Ljfftw/Guru/Dimension;I[Ljfftw/Guru/Dimension;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1guru_1dft_1split_1c2r
  (JNIEnv *env, jclass class, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, jobject ri_buff, jobject ii_buff, jobject ro_buff, jint flags) {
    _GET_GURU_DIMS
    double *ri, *ii, *ro;
    ri = (double *) (*env)->GetDirectBufferAddress(env, ri_buff);
    ii = (double *) (*env)->GetDirectBufferAddress(env, ii_buff);
    ro = (double *) (*env)->GetDirectBufferAddress(env, ro_buff);
    fftw_plan p = fftw_plan_guru_split_dft_c2r(rank, dims, howmany, hmdims, ri, ii, ro, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_guru_dft_split_r2c
 * Signature: (I[Ljfftw/Guru/Dimension;I[Ljfftw/Guru/Dimension;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1guru_1dft_1split_1r2c
  (JNIEnv *env, jclass class, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, jobject ri_buff, jobject ro_buff, jobject io_buff, jint flags) {
    _GET_GURU_DIMS
    double *ri, *ro, *io;
    ri = (double *) (*env)->GetDirectBufferAddress(env, ri_buff);
    ro = (double *) (*env)->GetDirectBufferAddress(env, ro_buff);
    io = (double *) (*env)->GetDirectBufferAddress(env, io_buff);
    fftw_plan p = fftw_plan_guru_split_dft_r2c(rank, dims, howmany, hmdims, ri, ro, io, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_guru64_dft
 * Signature: (I[Ljfftw/Guru64/Dimension;I[Ljfftw/Guru64/Dimension;Ljfftw/Complex;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1guru64_1dft
  (JNIEnv *env, jclass class1, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, jobject in, jobject out, jint sign, jint flags) {
    _GET_GURU64_DIMS
    fftw_complex *cin = (fftw_complex *) get_buffer(env, in);
    fftw_complex *cout = (fftw_complex *) get_buffer(env, out);
    fftw_plan p = fftw_plan_guru64_dft(rank, dims, howmany, hmdims, cin, cout, sign, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_guru64_dft_c2r
 * Signature: (I[Ljfftw/Guru64/Dimension;I[Ljfftw/Guru64/Dimension;Ljfftw/Complex;Ljfftw/Real;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1guru64_1dft_1c2r
  (JNIEnv *env, jclass class, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, jobject in, jobject out, jint flags) {
    _GET_GURU64_DIMS
    fftw_complex *cin = (fftw_complex *) get_buffer(env, in);
    double *rout = get_buffer(env, out);
    fftw_plan p = fftw_plan_guru64_dft_c2r(rank, dims, howmany, hmdims, cin, rout, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_guru64_dft_r2c
 * Signature: (I[Ljfftw/Guru64/Dimension;I[Ljfftw/Guru64/Dimension;Ljfftw/Real;Ljfftw/Complex;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1guru64_1dft_1r2c
  (JNIEnv *env, jclass class, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, jobject in, jobject out, jint flags) {
    _GET_GURU64_DIMS
    double *rin = get_buffer(env, in);
    fftw_complex *cout = (fftw_complex *) get_buffer(env, out);
    fftw_plan p = fftw_plan_guru64_dft_r2c(rank, dims, howmany, hmdims, rin, cout, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_guru64_dft_split
 * Signature: (I[Ljfftw/Guru64/Dimension;I[Ljfftw/Guru64/Dimension;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1guru64_1dft_1split
  (JNIEnv *env, jclass class, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, jobject ri_buff, jobject ii_buff, jobject ro_buff, jobject io_buff, jint flags) {
    _GET_GURU64_DIMS
    double *ri, *ii, *ro, *io;
    ri = (double *) (*env)->GetDirectBufferAddress(env, ri_buff);
    ii = (double *) (*env)->GetDirectBufferAddress(env, ii_buff);
    ro = (double *) (*env)->GetDirectBufferAddress(env, ro_buff);
    io = (double *) (*env)->GetDirectBufferAddress(env, io_buff);
    fftw_plan p = fftw_plan_guru64_split_dft(rank, dims, howmany, hmdims, ri, ii, ro, io, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_guru64_dft_split_c2r
 * Signature: (I[Ljfftw/Guru64/Dimension;I[Ljfftw/Guru64/Dimension;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;I)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1guru64_1dft_1split_1c2r
  (JNIEnv *env, jclass class, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, jobject ri_buff, jobject ii_buff, jobject ro_buff, jint flags) {
    _GET_GURU64_DIMS
    double *ri, *ii, *ro;
    ri = (double *) (*env)->GetDirectBufferAddress(env, ri_buff);
    ii = (double *) (*env)->GetDirectBufferAddress(env, ii_buff);
    ro = (double *) (*env)->GetDirectBufferAddress(env, ro_buff);
    fftw_plan p = fftw_plan_guru64_split_dft_c2r(rank, dims, howmany, hmdims, ri, ii, ro, flags);
    return (jlong) p;
}

/*
 * Class:     jfftw_Interface
 * Method:    jfftw_plan_guru64_dft_split_r2c
 * Signature: (I[Ljfftw/Guru64/Dimension;I[Ljfftw/Guru64/Dimension;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;Ljava/nio/DoubleBuffer;II)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_Interface_jfftw_1plan_1guru64_1dft_1split_1r2c
  (JNIEnv *env, jclass class, jint rank, jobjectArray jdims, jint howmany, jobjectArray jhmdims, jobject ri_buff, jobject ro_buff, jobject io_buff, jint flags) {
    _GET_GURU64_DIMS
    double *ri, *ro, *io;
    ri = (double *) (*env)->GetDirectBufferAddress(env, ri_buff);
    ro = (double *) (*env)->GetDirectBufferAddress(env, ro_buff);
    io = (double *) (*env)->GetDirectBufferAddress(env, io_buff);
    fftw_plan p = fftw_plan_guru64_split_dft_c2r(rank, dims, howmany, hmdims, ri, ro, io, flags);
    return (jlong) p;
}
