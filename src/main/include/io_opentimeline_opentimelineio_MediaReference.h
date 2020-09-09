/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class io_opentimeline_opentimelineio_MediaReference */

#ifndef _Included_io_opentimeline_opentimelineio_MediaReference
#define _Included_io_opentimeline_opentimelineio_MediaReference
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     io_opentimeline_opentimelineio_MediaReference
 * Method:    initialize
 * Signature: (Ljava/lang/String;Lio/opentimeline/opentime/TimeRange;Lio/opentimeline/opentimelineio/AnyDictionary;)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_MediaReference_initialize
  (JNIEnv *, jobject, jstring, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_MediaReference
 * Method:    getAvailableRange
 * Signature: ()Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_MediaReference_getAvailableRange
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_MediaReference
 * Method:    setAvailableRange
 * Signature: (Lio/opentimeline/opentime/TimeRange;)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_MediaReference_setAvailableRange
  (JNIEnv *, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_MediaReference
 * Method:    isMissingReference
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_MediaReference_isMissingReference
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif