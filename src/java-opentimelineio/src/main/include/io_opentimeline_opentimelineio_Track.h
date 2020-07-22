/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class io_opentimeline_opentimelineio_Track */

#ifndef _Included_io_opentimeline_opentimelineio_Track
#define _Included_io_opentimeline_opentimelineio_Track
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     io_opentimeline_opentimelineio_Track
 * Method:    initialize
 * Signature: (Ljava/lang/String;Lio/opentimeline/opentime/TimeRange;Ljava/lang/String;Lio/opentimeline/opentimelineio/AnyDictionary;)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_Track_initialize
  (JNIEnv *, jobject, jstring, jobject, jstring, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Track
 * Method:    getKind
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_io_opentimeline_opentimelineio_Track_getKind
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Track
 * Method:    setKind
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_Track_setKind
  (JNIEnv *, jobject, jstring);

/*
 * Class:     io_opentimeline_opentimelineio_Track
 * Method:    rangeOfChildAtIndex
 * Signature: (ILio/opentimeline/opentimelineio/ErrorStatus;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Track_rangeOfChildAtIndex
  (JNIEnv *, jobject, jint, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Track
 * Method:    trimmedRangeOfChildAtIndex
 * Signature: (ILio/opentimeline/opentimelineio/ErrorStatus;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Track_trimmedRangeOfChildAtIndex
  (JNIEnv *, jobject, jint, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Track
 * Method:    getAvailableRange
 * Signature: (Lio/opentimeline/opentimelineio/ErrorStatus;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Track_getAvailableRange
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Track
 * Method:    getHandlesOfChild
 * Signature: (Lio/opentimeline/opentimelineio/Composable;Lio/opentimeline/opentimelineio/ErrorStatus;)Lio/opentimeline/util/Pair;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Track_getHandlesOfChild
  (JNIEnv *, jobject, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Track
 * Method:    getNeighborsOfNative
 * Signature: (Lio/opentimeline/opentimelineio/Composable;Lio/opentimeline/opentimelineio/ErrorStatus;I)Lio/opentimeline/util/Pair;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Track_getNeighborsOfNative
  (JNIEnv *, jobject, jobject, jobject, jint);

/*
 * Class:     io_opentimeline_opentimelineio_Track
 * Method:    getRangeOfAllChildren
 * Signature: (Lio/opentimeline/opentimelineio/ErrorStatus;)Ljava/util/HashMap;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Track_getRangeOfAllChildren
  (JNIEnv *, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif
