#include "jfftw_planning_PrimitivePlan.h"
#include "jfftw.h"

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_execute_dft
 * Signature: (Ljfftw/planning/PrimitivePlan;[D[D)V
 */
JNIEXPORT void JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1execute_1dft
  (JNIEnv *env, jclass clazz, jobject jplan, jdoubleArray jin, jdoubleArray jout) {
    fftw_plan plan = get_fftw_plan(env, jplan);
    fftw_complex *ci = (fftw_complex *) get_array(env, jin);
    fftw_complex *co = (fftw_complex *) get_array(env, jout);
    fftw_execute_dft(plan, ci, co);
    release_array(env, jin, ci, JNI_COMMIT);
    release_array(env, jout, co, JNI_COMMIT);
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_execute_dft_c2r
 * Signature: (Ljfftw/planning/PrimitivePlan;[D[D)V
 */
JNIEXPORT void JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1execute_1dft_1c2r
  (JNIEnv *env, jclass clazz, jobject jplan, jdoubleArray jin, jdoubleArray jout) {
    fftw_plan plan = get_fftw_plan(env, jplan);
    fftw_complex *ci = (fftw_complex *) get_array(env, jin);
    double *ro = (double *) get_array(env, jout);
    fftw_execute_dft_c2r(plan, ci, ro);
    release_array(env, jin, ci, JNI_COMMIT);
    release_array(env, jout, ro, JNI_COMMIT);
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_execute_dft_r2c
 * Signature: (Ljfftw/planning/PrimitivePlan;[D[D)V
 */
JNIEXPORT void JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1execute_1dft_1r2c
  (JNIEnv *env, jclass clazz, jobject jplan, jdoubleArray jin, jdoubleArray jout) {
    fftw_plan plan = get_fftw_plan(env, jplan);
    double *ri = (double *) get_array(env, jin);
    fftw_complex *co = (fftw_complex *) get_array(env, jout);
    fftw_execute_dft_r2c(plan, ri, co);
    release_array(env, jin, ri, JNI_COMMIT);
    release_array(env, jout, co, JNI_COMMIT);
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_plan_dft
 * Signature: (I[I[D[DII)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1plan_1dft
  (JNIEnv *env, jclass clazz, jint rank, jintArray jdims, jdoubleArray jin, jdoubleArray jout, jint sign, jint flags) {
    int *dims = (int *) (*env)->GetPrimitiveArrayCritical(env, jdims, 0);
    fftw_complex *ci = (fftw_complex *) get_array(env, jin);
    fftw_complex *co = (fftw_complex *) get_array(env, jout);
    fftw_plan plan = fftw_plan_dft(rank, dims, ci, co, sign, flags);
    (*env)->ReleasePrimitiveArrayCritical(env, jdims, dims, JNI_COMMIT);
    release_array(env, jin, ci, JNI_COMMIT);
    release_array(env, jout, co, JNI_COMMIT);
    return (jlong) plan;
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_plan_dft_1d
 * Signature: (I[D[DII)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1plan_1dft_11d
  (JNIEnv *env, jclass clazz, jint n, jdoubleArray jin, jdoubleArray jout, jint sign, jint flags) {
    fftw_complex *ci = (fftw_complex *) get_array(env, jin);
    fftw_complex *co = (fftw_complex *) get_array(env, jout);
    fftw_plan plan = fftw_plan_dft_1d(n, ci, co, sign, flags);
    release_array(env, jin, ci, JNI_COMMIT);
    release_array(env, jout, co, JNI_COMMIT);
    return (jlong) plan;
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_plan_dft_2d
 * Signature: (II[D[DII)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1plan_1dft_12d
  (JNIEnv *env, jclass clazz, jint n0, jint n1, jdoubleArray jin, jdoubleArray jout, jint sign, jint flags) {
    fftw_complex *ci = (fftw_complex *) get_array(env, jin);
    fftw_complex *co = (fftw_complex *) get_array(env, jout);
    fftw_plan plan = fftw_plan_dft_2d(n0, n1, ci, co, sign, flags);
    release_array(env, jin, ci, JNI_COMMIT);
    release_array(env, jout, co, JNI_COMMIT);
    return (jlong) plan;
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_plan_dft_3d
 * Signature: (III[D[DII)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1plan_1dft_13d
  (JNIEnv *env, jclass clazz, jint n0, jint n1, jint n2, jdoubleArray jin, jdoubleArray jout, jint sign, jint flags) {
    fftw_complex *ci = (fftw_complex *) get_array(env, jin);
    fftw_complex *co = (fftw_complex *) get_array(env, jout);
    fftw_plan plan = fftw_plan_dft_3d(n0, n1, n2, ci, co, sign, flags);
    release_array(env, jin, ci, JNI_COMMIT);
    release_array(env, jout, co, JNI_COMMIT);
    return (jlong) plan;
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_plan_dft_c2r
 * Signature: (I[I[D[DI)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1plan_1dft_1c2r
  (JNIEnv *env, jclass clazz, jint rank, jintArray jdims, jdoubleArray jin, jdoubleArray jout, jint flags) {
    int *dims = (int *) (*env)->GetPrimitiveArrayCritical(env, jdims, 0);
    fftw_complex *ci = (fftw_complex *) get_array(env, jin);
    double *ro = (double *) get_array(env, jout);
    fftw_plan plan = fftw_plan_dft_c2r(rank, dims, ci, ro, flags);
    (*env)->ReleasePrimitiveArrayCritical(env, jdims, dims, JNI_COMMIT);
    release_array(env, jin, ci, JNI_COMMIT);
    release_array(env, jout, ro, JNI_COMMIT);
    return (jlong) plan;
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_plan_dft_c2r_1d
 * Signature: (I[D[DI)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1plan_1dft_1c2r_11d
  (JNIEnv *env, jclass clazz, jint n, jdoubleArray jin, jdoubleArray jout, jint flags) {
    fftw_complex *ci = (fftw_complex *) get_array(env, jin);
    double *ro = (double *) get_array(env, jout);
    fftw_plan plan = fftw_plan_dft_c2r_1d(n, ci, ro, flags);
    release_array(env, jin, ci, JNI_COMMIT);
    release_array(env, jout, ro, JNI_COMMIT);
    return (jlong) plan;
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_plan_dft_c2r_2d
 * Signature: (II[D[DI)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1plan_1dft_1c2r_12d
  (JNIEnv *env, jclass clazz, jint n0, jint n1, jdoubleArray jin, jdoubleArray jout, jint flags) {
    fftw_complex *ci = (fftw_complex *) get_array(env, jin);
    double *ro = (double *) get_array(env, jout);
    fftw_plan plan = fftw_plan_dft_c2r_2d(n0, n1, ci, ro, flags);
    release_array(env, jin, ci, JNI_COMMIT);
    release_array(env, jout, ro, JNI_COMMIT);
    return (jlong) plan;
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_plan_dft_c2r_3d
 * Signature: (III[D[DI)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1plan_1dft_1c2r_13d
  (JNIEnv *env, jclass clazz, jint n0, jint n1, jint n2, jdoubleArray jin, jdoubleArray jout, jint flags) {
    fftw_complex *ci = (fftw_complex *) get_array(env, jin);
    double *ro = (double *) get_array(env, jout);
    fftw_plan plan = fftw_plan_dft_c2r_3d(n0, n1, n2, ci, ro, flags);
    release_array(env, jin, ci, JNI_COMMIT);
    release_array(env, jout, ro, JNI_COMMIT);
    return (jlong) plan;
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_plan_dft_r2c
 * Signature: (I[I[D[DI)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1plan_1dft_1r2c
  (JNIEnv *env, jclass clazz, jint rank, jintArray jdims, jdoubleArray jin, jdoubleArray jout, jint flags) {
    int *dims = (*env)->GetPrimitiveArrayCritical(env, jdims, 0);
    double *ri = (double *) get_array(env, jin);
    fftw_complex *co = (fftw_complex *) get_array(env, jout);
    fftw_plan plan = fftw_plan_dft_r2c(rank, dims, ri, co, flags);
    (*env)->ReleasePrimitiveArrayCritical(env, jdims, dims, JNI_COMMIT);
    release_array(env, jin, ri, JNI_COMMIT);
    release_array(env, jout, co, JNI_COMMIT);
    return (jlong) plan;
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_plan_dft_r2c_1d
 * Signature: (I[D[DI)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1plan_1dft_1r2c_11d
  (JNIEnv *env, jclass clazz, jint n, jdoubleArray jin, jdoubleArray jout, jint flags) {
    double *ri = (double *) get_array(env, jin);
    fftw_complex *co = (fftw_complex *) get_array(env, jout);
    fftw_plan plan = fftw_plan_dft_r2c_1d(n, ri, co, flags);
    release_array(env, jin, ri, JNI_COMMIT);
    release_array(env, jout, co, JNI_COMMIT);
    return (jlong) plan;
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_plan_dft_r2c_2d
 * Signature: (II[D[DI)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1plan_1dft_1r2c_12d
  (JNIEnv *env, jclass clazz, jint n0, jint n1, jdoubleArray jin, jdoubleArray jout, jint flags) {
    double *ri = (double *) get_array(env, jin);
    fftw_complex *co = (fftw_complex *) get_array(env, jout);
    fftw_plan plan = fftw_plan_dft_r2c_2d(n0, n1, ri, co, flags);
    release_array(env, jin, ri, JNI_COMMIT);
    release_array(env, jout, co, JNI_COMMIT);
    return (jlong) plan;
}

/*
 * Class:     jfftw_planning_PrimitivePlan
 * Method:    jfftw_plan_dft_r2c_3d
 * Signature: (III[D[DI)J
 */
JNIEXPORT jlong JNICALL Java_jfftw_planning_PrimitivePlan_jfftw_1plan_1dft_1r2c_13d
  (JNIEnv *env, jclass clazz, jint n0, jint n1, jint n2, jdoubleArray jin, jdoubleArray jout, jint flags) {
    double *ri = (double *) get_array(env, jin);
    fftw_complex *co = (fftw_complex *) get_array(env, jout);
    fftw_plan plan = fftw_plan_dft_r2c_3d(n0, n1, n2, ri, co, flags);
    release_array(env, jin, ri, JNI_COMMIT);
    release_array(env, jout, co, JNI_COMMIT);
    return (jlong) plan;
}
