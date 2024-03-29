/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class io_opentimeline_opentime_RationalTime */

#ifndef _Included_io_opentimeline_opentime_RationalTime
#define _Included_io_opentimeline_opentime_RationalTime
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    isInvalidTimeNative
 * Signature: (DD)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_RationalTime_isInvalidTimeNative
  (JNIEnv *, jclass, jdouble, jdouble);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    add
 * Signature: (Lio/opentimeline/opentime/RationalTime;)Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_RationalTime_add
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    subtract
 * Signature: (Lio/opentimeline/opentime/RationalTime;)Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_RationalTime_subtract
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    rescaledTo
 * Signature: (D)Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_RationalTime_rescaledTo__D
  (JNIEnv *, jobject, jdouble);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    rescaledTo
 * Signature: (Lio/opentimeline/opentime/RationalTime;)Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_RationalTime_rescaledTo__Lio_opentimeline_opentime_RationalTime_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    valueRescaledTo
 * Signature: (D)D
 */
JNIEXPORT jdouble JNICALL Java_io_opentimeline_opentime_RationalTime_valueRescaledTo__D
  (JNIEnv *, jobject, jdouble);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    valueRescaledTo
 * Signature: (Lio/opentimeline/opentime/RationalTime;)D
 */
JNIEXPORT jdouble JNICALL Java_io_opentimeline_opentime_RationalTime_valueRescaledTo__Lio_opentimeline_opentime_RationalTime_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    almostEqual
 * Signature: (Lio/opentimeline/opentime/RationalTime;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_RationalTime_almostEqual__Lio_opentimeline_opentime_RationalTime_2
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    almostEqual
 * Signature: (Lio/opentimeline/opentime/RationalTime;D)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_RationalTime_almostEqual__Lio_opentimeline_opentime_RationalTime_2D
  (JNIEnv *, jobject, jobject, jdouble);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    durationFromStartEndTime
 * Signature: (Lio/opentimeline/opentime/RationalTime;Lio/opentimeline/opentime/RationalTime;)Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_RationalTime_durationFromStartEndTime
  (JNIEnv *, jclass, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    isValidTimecodeRate
 * Signature: (D)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_RationalTime_isValidTimecodeRate
  (JNIEnv *, jclass, jdouble);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    fromTimecode
 * Signature: (Ljava/lang/String;D)Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_RationalTime_fromTimecode
  (JNIEnv *, jclass, jstring, jdouble);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    fromTimeString
 * Signature: (Ljava/lang/String;D)Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentime_RationalTime_fromTimeString
  (JNIEnv *, jclass, jstring, jdouble);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    toTimecodeNative
 * Signature: (Lio/opentimeline/opentime/RationalTime;DI)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_io_opentimeline_opentime_RationalTime_toTimecodeNative
  (JNIEnv *, jclass, jobject, jdouble, jint);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    toTimeString
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_io_opentimeline_opentime_RationalTime_toTimeString
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    equals
 * Signature: (Lio/opentimeline/opentime/RationalTime;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentime_RationalTime_equals
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    nearestValidTimecodeRate
 * Signature: (D)D
 */
JNIEXPORT jdouble JNICALL Java_io_opentimeline_opentime_RationalTime_nearestValidTimecodeRate
  (JNIEnv *, jclass, jdouble);

/*
 * Class:     io_opentimeline_opentime_RationalTime
 * Method:    compareTo
 * Signature: (Lio/opentimeline/opentime/RationalTime;)I
 */
JNIEXPORT jint JNICALL Java_io_opentimeline_opentime_RationalTime_compareTo
  (JNIEnv *, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif
