#include <exceptions.h>
#include <handle.h>
#include <io_opentimeline_opentimelineio_Composable.h>
#include <opentimelineio/composable.h>
#include <opentimelineio/version.h>
#include <utilities.h>

/*
 * Class:     io_opentimeline_opentimelineio_Composable
 * Method:    initialize
 * Signature: (Ljava/lang/String;Lio/opentimeline/opentimelineio/AnyDictionary;)V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_Composable_initialize(
    JNIEnv* env, jobject thisObj, jstring name, jobject metadataObj)
{
    if(name == NULL || metadataObj == NULL)
        throwNullPointerException(env, "");
    else
    {
        std::string nameStr = env->GetStringUTFChars(name, 0);
        auto        metadataHandle =
            getHandle<OTIO_NS::AnyDictionary>(env, metadataObj);
        auto composable = new OTIO_NS::Composable(nameStr, *metadataHandle);
        setHandle(env, thisObj, composable);
    }
}

/*
 * Class:     io_opentimeline_opentimelineio_Composable
 * Method:    isVisible
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_io_opentimeline_opentimelineio_Composable_isVisible(
    JNIEnv* env, jobject thisObj)
{
    auto thisHandle = getHandle<OTIO_NS::Composable>(env, thisObj);
    return thisHandle->visible();
}

/*
 * Class:     io_opentimeline_opentimelineio_Composable
 * Method:    isOverlapping
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_io_opentimeline_opentimelineio_Composable_isOverlapping(
    JNIEnv* env, jobject thisObj)
{
    auto thisHandle = getHandle<OTIO_NS::Composable>(env, thisObj);
    return thisHandle->overlapping();
}

/*
 * Class:     io_opentimeline_opentimelineio_Composable
 * Method:    getDurationNative
 * Signature: (Lio/opentimeline/opentimelineio/ErrorStatus;)[D
 */
JNIEXPORT jdoubleArray JNICALL
Java_io_opentimeline_opentimelineio_Composable_getDurationNative(
    JNIEnv* env, jobject thisObj, jobject errorStatusObj)
{
    auto thisHandle = getHandle<OTIO_NS::Composable>(env, thisObj);
    auto errorStatusHandle =
        getHandle<OTIO_NS::ErrorStatus>(env, errorStatusObj);
    auto duration = thisHandle->duration(errorStatusHandle);
    return rationalTimeToArray(env, duration);
}