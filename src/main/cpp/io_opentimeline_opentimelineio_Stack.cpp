// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

#include <exceptions.h>
#include <handle.h>
#include <io_opentimeline_opentimelineio_Stack.h>
#include <opentimelineio/errorStatus.h>
#include <opentimelineio/stack.h>
#include <opentimelineio/version.h>
#include <utilities.h>

using namespace opentimelineio::OPENTIMELINEIO_VERSION;

/*
 * Class:     io_opentimeline_opentimelineio_Stack
 * Method:    initialize
 * Signature: (Ljava/lang/String;Lio/opentimeline/opentime/TimeRange;Lio/opentimeline/opentimelineio/AnyDictionary;[Lio/opentimeline/opentimelineio/Effect;[Lio/opentimeline/opentimelineio/Marker;)V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_Stack_initialize(
        JNIEnv *env,
        jobject thisObj,
        jstring name,
        jobject sourceRangeObj,
        jobject metadataObj,
        jobjectArray effectsArray,
        jobjectArray markersArray) {
    if (name == nullptr || metadataObj == nullptr) {
        throwNullPointerException(env, "");
        return;
    }
    std::string nameStr = env->GetStringUTFChars(name, 0);
    optional<TimeRange> sourceRange = nullopt;
    if (sourceRangeObj != nullptr) { sourceRange = timeRangeFromJObject(env, sourceRangeObj); }
    auto metadataHandle =
            getHandle<AnyDictionary>(env, metadataObj);
    auto effects = effectVectorFromArray(env, effectsArray);
    auto markers = markerVectorFromArray(env, markersArray);
    auto stack = new Stack(
            nameStr, sourceRange, *metadataHandle, effects, markers);
    auto stackManager =
            new SerializableObject::Retainer<Stack>(stack);
    setHandle(env, thisObj, stackManager);
}

/*
 * Class:     io_opentimeline_opentimelineio_Stack
 * Method:    rangeOfChildAtIndex
 * Signature: (I)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Stack_rangeOfChildAtIndex(
        JNIEnv *env, jobject thisObj, jint index) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Stack>>(env, thisObj);
    auto stack = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result = stack->range_of_child_at_index(index, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return timeRangeToJObject(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Stack
 * Method:    trimmedRangeOfChildAtIndex
 * Signature: (I)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Stack_trimmedRangeOfChildAtIndex(
        JNIEnv *env, jobject thisObj, jint index) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Stack>>(env, thisObj);
    auto stack = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result =
            stack->trimmed_range_of_child_at_index(index, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return timeRangeToJObject(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Stack
 * Method:    getAvailableRange
 * Signature: ()Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Stack_getAvailableRange(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Stack>>(env, thisObj);
    auto stack = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result = stack->available_range(&errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return timeRangeToJObject(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Stack
 * Method:    getRangeOfAllChildren
 * Signature: ()Ljava/util/HashMap;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Stack_getRangeOfAllChildren(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Stack>>(env, thisObj);
    auto stack = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result = stack->range_of_all_children(&errorStatus);
    processOTIOErrorStatus(env, errorStatus);

    jclass hashMapClass = env->FindClass("java/util/HashMap");
    jmethodID hashMapInit = env->GetMethodID(hashMapClass, "<init>", "(I)V");
    jobject hashMapObj =
            env->NewObject(hashMapClass, hashMapInit, result.size());
    jmethodID hashMapPut = env->GetMethodID(
            hashMapClass,
            "put",
            "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    jclass composableClass =
            env->FindClass("io/opentimeline/opentimelineio/Composable");
    jmethodID composableInit =
            env->GetMethodID(composableClass, "<init>", "()V");

    for (auto it: result) {
        auto first = it.first;
        auto second = it.second;

        jobject composableObject =
                env->NewObject(composableClass, composableInit);
        auto firstManager =
                new SerializableObject::Retainer<Composable>(first);
        setHandle(env, composableObject, firstManager);
        registerObjectToOTIOFactory(env, composableObject);
        jobject tr = timeRangeToJObject(env, second);

        env->CallObjectMethod(hashMapObj, hashMapPut, composableObject, tr);
    }

    return hashMapObj;
}

/*
 * Class:     io_opentimeline_opentimelineio_Stack
 * Method:    clipIfNative
 * Signature: (Ljava/util/Optional;Z)[Lio/opentimeline/opentimelineio/Clip;
 */
JNIEXPORT jobjectArray JNICALL
Java_io_opentimeline_opentimelineio_Stack_clipIfNative(
        JNIEnv *env, jobject thisObj, jobject searchRangeTimeRangeOptional, jboolean shallowSearch){
    if (searchRangeTimeRangeOptional == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    }
    return getClipIfResult<Stack>(env, thisObj, searchRangeTimeRangeOptional, shallowSearch);
}

