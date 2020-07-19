/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class io_opentimeline_opentimelineio_SerializableCollection */

#ifndef _Included_io_opentimeline_opentimelineio_SerializableCollection
#define _Included_io_opentimeline_opentimelineio_SerializableCollection
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    initialize
 * Signature: (Ljava/lang/String;[Lio/opentimeline/opentimelineio/SerializableObject;Lio/opentimeline/opentimelineio/AnyDictionary;)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_SerializableCollection_initialize
  (JNIEnv *, jobject, jstring, jobjectArray, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    getChildrenNative
 * Signature: ()[Lio/opentimeline/opentimelineio/SerializableObject/Retainer;
 */
JNIEXPORT jobjectArray JNICALL Java_io_opentimeline_opentimelineio_SerializableCollection_getChildrenNative
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    setChildrenNative
 * Signature: ([Lio/opentimeline/opentimelineio/SerializableObject;)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_SerializableCollection_setChildrenNative
  (JNIEnv *, jobject, jobjectArray);

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    clearChildren
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_SerializableCollection_clearChildren
  (JNIEnv *, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    setChild
 * Signature: (ILio/opentimeline/opentimelineio/SerializableObject;Lio/opentimeline/opentimelineio/ErrorStatus;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_SerializableCollection_setChild
  (JNIEnv *, jobject, jint, jobject, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    removeChild
 * Signature: (ILio/opentimeline/opentimelineio/ErrorStatus;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_SerializableCollection_removeChild
  (JNIEnv *, jobject, jint, jobject);

#ifdef __cplusplus
}
#endif
#endif
