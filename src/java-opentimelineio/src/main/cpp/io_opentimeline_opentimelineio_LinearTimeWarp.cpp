#include <exceptions.h>
#include <handle.h>
#include <io_opentimeline_opentimelineio_LinearTimeWarp.h>
#include <opentimelineio/linearTimeWarp.h>
#include <opentimelineio/version.h>
#include <utilities.h>

/*
 * Class:     io_opentimeline_opentimelineio_LinearTimeWarp
 * Method:    initialize
 * Signature: (Ljava/lang/String;Ljava/lang/String;DLio/opentimeline/opentimelineio/AnyDictionary;)V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_LinearTimeWarp_initialize(
    JNIEnv* env,
    jobject thisObj,
    jstring name,
    jstring effectName,
    jdouble timeScalar,
    jobject metadata)
{
    std::string nameStr       = env->GetStringUTFChars(name, 0);
    std::string effectNameStr = env->GetStringUTFChars(effectName, 0);
    auto metadataHandle = getHandle<OTIO_NS::AnyDictionary>(env, metadata);
    auto linearTimeWarp = new OTIO_NS::LinearTimeWarp(
        nameStr, effectNameStr, timeScalar, *metadataHandle);
    setHandle(env, thisObj, linearTimeWarp);
}

/*
 * Class:     io_opentimeline_opentimelineio_LinearTimeWarp
 * Method:    getTimeScalar
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL
Java_io_opentimeline_opentimelineio_LinearTimeWarp_getTimeScalar(
    JNIEnv*, jobject);

/*
 * Class:     io_opentimeline_opentimelineio_LinearTimeWarp
 * Method:    setTimeScalar
 * Signature: (D)V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_LinearTimeWarp_setTimeScalar(
    JNIEnv*, jobject, jdouble);