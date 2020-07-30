/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class io_opentimeline_opentime_TimeRange */

#ifndef _Included_io_opentimeline_opentime_TimeRange
#define _Included_io_opentimeline_opentime_TimeRange
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    endTimeInclusive
 * Signature: ()Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_TimeRange_endTimeInclusive
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    endTimeExclusive
 * Signature: ()Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_TimeRange_endTimeExclusive
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    durationExtendedBy
 * Signature: (Lio/opentimeline/opentime/RationalTime;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_TimeRange_durationExtendedBy
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    extendedBy
 * Signature: (Lio/opentimeline/opentime/TimeRange;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_TimeRange_extendedBy
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    clamped
 * Signature: (Lio/opentimeline/opentime/RationalTime;)Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_TimeRange_clamped__Lio_opentimeline_opentime_RationalTime_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    clamped
 * Signature: (Lio/opentimeline/opentime/TimeRange;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_TimeRange_clamped__Lio_opentimeline_opentime_TimeRange_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    contains
 * Signature: (Lio/opentimeline/opentime/RationalTime;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_contains__Lio_opentimeline_opentime_RationalTime_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    contains
 * Signature: (Lio/opentimeline/opentime/TimeRange;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_contains__Lio_opentimeline_opentime_TimeRange_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    overlaps
 * Signature: (Lio/opentimeline/opentime/RationalTime;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_overlaps__Lio_opentimeline_opentime_RationalTime_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    overlaps
 * Signature: (Lio/opentimeline/opentime/TimeRange;D)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_overlaps__Lio_opentimeline_opentime_TimeRange_2D
  (JNIEnv *, jobject, jobject, jdouble);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    overlaps
 * Signature: (Lio/opentimeline/opentime/TimeRange;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_overlaps__Lio_opentimeline_opentime_TimeRange_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    before
 * Signature: (Lio/opentimeline/opentime/TimeRange;D)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_before__Lio_opentimeline_opentime_TimeRange_2D
  (JNIEnv *, jobject, jobject, jdouble);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    before
 * Signature: (Lio/opentimeline/opentime/TimeRange;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_before__Lio_opentimeline_opentime_TimeRange_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    before
 * Signature: (Lio/opentimeline/opentime/RationalTime;D)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_before__Lio_opentimeline_opentime_RationalTime_2D
  (JNIEnv *, jobject, jobject, jdouble);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    before
 * Signature: (Lio/opentimeline/opentime/RationalTime;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_before__Lio_opentimeline_opentime_RationalTime_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    meets
 * Signature: (Lio/opentimeline/opentime/TimeRange;D)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_meets__Lio_opentimeline_opentime_TimeRange_2D
  (JNIEnv *, jobject, jobject, jdouble);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    meets
 * Signature: (Lio/opentimeline/opentime/TimeRange;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_meets__Lio_opentimeline_opentime_TimeRange_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    begins
 * Signature: (Lio/opentimeline/opentime/TimeRange;D)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_begins__Lio_opentimeline_opentime_TimeRange_2D
  (JNIEnv *, jobject, jobject, jdouble);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    begins
 * Signature: (Lio/opentimeline/opentime/TimeRange;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_begins__Lio_opentimeline_opentime_TimeRange_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    begins
 * Signature: (Lio/opentimeline/opentime/RationalTime;D)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_begins__Lio_opentimeline_opentime_RationalTime_2D
  (JNIEnv *, jobject, jobject, jdouble);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    begins
 * Signature: (Lio/opentimeline/opentime/RationalTime;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_begins__Lio_opentimeline_opentime_RationalTime_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    finishes
 * Signature: (Lio/opentimeline/opentime/TimeRange;D)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_finishes__Lio_opentimeline_opentime_TimeRange_2D
  (JNIEnv *, jobject, jobject, jdouble);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    finishes
 * Signature: (Lio/opentimeline/opentime/TimeRange;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_finishes__Lio_opentimeline_opentime_TimeRange_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    finishes
 * Signature: (Lio/opentimeline/opentime/RationalTime;D)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_finishes__Lio_opentimeline_opentime_RationalTime_2D
  (JNIEnv *, jobject, jobject, jdouble);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    finishes
 * Signature: (Lio/opentimeline/opentime/RationalTime;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_finishes__Lio_opentimeline_opentime_RationalTime_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    equals
 * Signature: (Lio/opentimeline/opentime/TimeRange;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_equals
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    notEquals
 * Signature: (Lio/opentimeline/opentime/TimeRange;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_TimeRange_notEquals
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_TimeRange
 * Method:    rangeFromStartEndTime
 * Signature: (Lio/opentimeline/opentime/RationalTime;Lio/opentimeline/opentime/RationalTime;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_TimeRange_rangeFromStartEndTime
  (JNIEnv *, jclass, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif
