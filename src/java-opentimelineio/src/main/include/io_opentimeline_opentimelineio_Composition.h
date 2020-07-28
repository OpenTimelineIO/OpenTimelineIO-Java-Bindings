/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class io_opentimeline_opentimelineio_Composition */

#ifndef _Included_io_opentimeline_opentimelineio_Composition
#define _Included_io_opentimeline_opentimelineio_Composition
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    initialize
 * Signature: (Ljava/lang/String;Lio/opentimeline/opentime/TimeRange;Lio/opentimeline/opentimelineio/AnyDictionary;[Lio/opentimeline/opentimelineio/Effect;[Lio/opentimeline/opentimelineio/Marker;)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_Composition_initialize
  (JNIEnv *, jobject, jstring, jobject, jobject, jobjectArray, jobjectArray);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getCompositionKind
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_io_opentimeline_opentimelineio_Composition_getCompositionKind
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getChildrenNative
 * Signature: ()[Lio/opentimeline/opentimelineio/Composable;
 */
JNIEXPORT jobjectArray JNICALL Java_io_opentimeline_opentimelineio_Composition_getChildrenNative
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    clearChildren
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_Composition_clearChildren
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    setChildrenNative
 * Signature: ([Lio/opentimeline/opentimelineio/Composable;Lio/opentimeline/opentimelineio/ErrorStatus;)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_Composition_setChildrenNative
  (JNIEnv *, jobject, jobjectArray, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    insertChild
 * Signature: (ILio/opentimeline/opentimelineio/Composable;Lio/opentimeline/opentimelineio/ErrorStatus;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Composition_insertChild
  (JNIEnv *, jobject, jint, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    setChild
 * Signature: (ILio/opentimeline/opentimelineio/Composable;Lio/opentimeline/opentimelineio/ErrorStatus;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Composition_setChild
  (JNIEnv *, jobject, jint, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    removeChild
 * Signature: (ILio/opentimeline/opentimelineio/ErrorStatus;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Composition_removeChild
  (JNIEnv *, jobject, jint, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    appendChild
 * Signature: (Lio/opentimeline/opentimelineio/Composable;Lio/opentimeline/opentimelineio/ErrorStatus;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Composition_appendChild
  (JNIEnv *, jobject, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    isParentOf
 * Signature: (Lio/opentimeline/opentimelineio/Composable;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Composition_isParentOf
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getHandlesOfChild
 * Signature: (Lio/opentimeline/opentimelineio/Composable;Lio/opentimeline/opentimelineio/ErrorStatus;)Lio/opentimeline/util/Pair;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_getHandlesOfChild
  (JNIEnv *, jobject, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getRangeOfChildAtIndex
 * Signature: (ILio/opentimeline/opentimelineio/ErrorStatus;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_getRangeOfChildAtIndex
  (JNIEnv *, jobject, jint, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getTrimmedRangeOfChildAtIndex
 * Signature: (ILio/opentimeline/opentimelineio/ErrorStatus;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_getTrimmedRangeOfChildAtIndex
  (JNIEnv *, jobject, jint, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getRangeOfChild
 * Signature: (Lio/opentimeline/opentimelineio/Composable;Lio/opentimeline/opentimelineio/ErrorStatus;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_getRangeOfChild
  (JNIEnv *, jobject, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getTrimmedRangeOfChild
 * Signature: (Lio/opentimeline/opentimelineio/Composable;Lio/opentimeline/opentimelineio/ErrorStatus;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_getTrimmedRangeOfChild
  (JNIEnv *, jobject, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    trimChildRange
 * Signature: (Lio/opentimeline/opentime/TimeRange;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_trimChildRange
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    hasChild
 * Signature: (Lio/opentimeline/opentimelineio/Composable;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Composition_hasChild
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getRangeOfAllChildren
 * Signature: (Lio/opentimeline/opentimelineio/ErrorStatus;)Ljava/util/HashMap;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_getRangeOfAllChildren
  (JNIEnv *, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif
