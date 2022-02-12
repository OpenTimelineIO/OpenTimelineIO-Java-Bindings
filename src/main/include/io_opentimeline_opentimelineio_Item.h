/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class io_opentimeline_opentimelineio_Item */

#ifndef _Included_io_opentimeline_opentimelineio_Item
#define _Included_io_opentimeline_opentimelineio_Item
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    initialize
 * Signature: (Ljava/lang/String;Lio/opentimeline/opentime/TimeRange;Lio/opentimeline/opentimelineio/AnyDictionary;[Lio/opentimeline/opentimelineio/Effect;[Lio/opentimeline/opentimelineio/Marker;Z)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_Item_initialize
  (JNIEnv *, jobject, jstring, jobject, jobject, jobjectArray, jobjectArray, jboolean);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    isVisible
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Item_isVisible
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    isOverlapping
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Item_isOverlapping
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    isEnabled
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Item_isEnabled
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    setEnabled
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_Item_setEnabled
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    getSourceRange
 * Signature: ()Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Item_getSourceRange
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    setSourceRange
 * Signature: (Lio/opentimeline/opentime/TimeRange;)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_Item_setSourceRange
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    getEffectsNative
 * Signature: ()[Lio/opentimeline/opentimelineio/Effect;
 */
JNIEXPORT jobjectArray JNICALL Java_io_opentimeline_opentimelineio_Item_getEffectsNative
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    getMarkersNative
 * Signature: ()[Lio/opentimeline/opentimelineio/Marker;
 */
JNIEXPORT jobjectArray JNICALL Java_io_opentimeline_opentimelineio_Item_getMarkersNative
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    getDuration
 * Signature: ()Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Item_getDuration
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    getAvailableRange
 * Signature: ()Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Item_getAvailableRange
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    getTrimmedRange
 * Signature: ()Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Item_getTrimmedRange
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    getVisibleRange
 * Signature: ()Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Item_getVisibleRange
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    getTrimmedRangeInParent
 * Signature: ()Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Item_getTrimmedRangeInParent
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    getRangeInParent
 * Signature: ()Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Item_getRangeInParent
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    getTransformedTime
 * Signature: (Lio/opentimeline/opentime/RationalTime;Lio/opentimeline/opentimelineio/Item;)Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Item_getTransformedTime
  (JNIEnv *, jobject, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_Item
 * Method:    getTransformedTimeRange
 * Signature: (Lio/opentimeline/opentime/TimeRange;Lio/opentimeline/opentimelineio/Item;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Item_getTransformedTimeRange
  (JNIEnv *, jobject, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif
