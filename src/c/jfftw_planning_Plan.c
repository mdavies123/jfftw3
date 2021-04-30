#include "jfftw_planning_Plan.h"
#include "jfftw.h"

/*
 * Class:     jfftw_planning_Plan
 * Method:    jfftw_cost
 * Signature: (Ljfftw/planning/Plan;)D
 */
JNIEXPORT jdouble JNICALL Java_jfftw_planning_Plan_jfftw_1cost
  (JNIEnv *env, jclass clazz, jobject jplan) {
    fftw_plan plan = get_fftw_plan(env, jplan);
    return fftw_cost(plan);
}

/*
 * Class:     jfftw_planning_Plan
 * Method:    jfftw_destroy_plan
 * Signature: (Ljfftw/planning/Plan;)V
 */
JNIEXPORT void JNICALL Java_jfftw_planning_Plan_jfftw_1destroy_1plan
  (JNIEnv *env, jclass clazz, jobject jplan) {
    fftw_plan plan = get_fftw_plan(env, jplan);
    fftw_destroy_plan(plan);
}

/*
 * Class:     jfftw_planning_Plan
 * Method:    jfftw_estimate_cost
 * Signature: (Ljfftw/planning/Plan;)D
 */
JNIEXPORT jdouble JNICALL Java_jfftw_planning_Plan_jfftw_1estimate_1cost
  (JNIEnv *env, jclass clazz, jobject jplan) {
    fftw_plan plan = get_fftw_plan(env, jplan);
    return fftw_estimate_cost(plan);
}

/*
 * Class:     jfftw_planning_Plan
 * Method:    jfftw_execute
 * Signature: (Ljfftw/planning/Plan;)V
 */
JNIEXPORT void JNICALL Java_jfftw_planning_Plan_jfftw_1execute
  (JNIEnv *env, jclass clazz, jobject jplan) {
    fftw_plan plan = get_fftw_plan(env, jplan);
    fftw_execute(plan);
}

/*
 * Class:     jfftw_planning_Plan
 * Method:    jfftw_print_plan
 * Signature: (Ljfftw/planning/Plan;)V
 */
JNIEXPORT void JNICALL Java_jfftw_planning_Plan_jfftw_1print_1plan
  (JNIEnv *env, jclass clazz, jobject jplan) {
    fftw_plan plan = get_fftw_plan(env, jplan);
    fftw_print_plan(plan);
}

/*
 * Class:     jfftw_planning_Plan
 * Method:    jfftw_sprint_plan
 * Signature: (Ljfftw/planning/Plan;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_jfftw_planning_Plan_jfftw_1sprint_1plan
  (JNIEnv *env, jclass clazz, jobject jplan) {
    fftw_plan plan = get_fftw_plan(env, jplan);
    char *str = plan == NULL ? "null plan" : fftw_sprint_plan(plan);
    jstring jstr = (*env)->NewStringUTF(env, str);
    return jstr;
}
