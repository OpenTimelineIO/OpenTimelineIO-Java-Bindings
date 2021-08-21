// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

#include <exceptions.h>
#include <handle.h>
#include <io_opentimeline_opentimelineio_Timeline.h>
#include <opentimelineio/errorStatus.h>
#include <opentimelineio/timeline.h>
#include <opentimelineio/version.h>
#include <utilities.h>
#include <iostream>
#include <cstring>

using namespace opentimelineio::OPENTIMELINEIO_VERSION;

/*
 * Class:     io_opentimeline_opentimelineio_Timeline
 * Method:    initialize
 * Signature: (Ljava/lang/String;Lio/opentimeline/opentime/RationalTime;Lio/opentimeline/opentimelineio/AnyDictionary;)V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_Timeline_initialize(
        JNIEnv *env,
        jobject thisObj,
        jstring name,
        jobject globalStartTimeRationalTime,
        jobject metadataObj) {
    if (name == nullptr || metadataObj == nullptr)
        throwNullPointerException(env, "");
    else {
        optional<RationalTime> globalStartTime = nullopt;
        if (globalStartTimeRationalTime != nullptr)
            globalStartTime =
                    rationalTimeFromJObject(env, globalStartTimeRationalTime);
        std::string nameStr = env->GetStringUTFChars(name, 0);
        auto metadataHandle =
                getHandle<AnyDictionary>(env, metadataObj);
        auto timeline =
                new Timeline(nameStr, globalStartTime, *metadataHandle);
        auto timelineManager =
                new SerializableObject::Retainer<Timeline>(timeline);
        setHandle(env, thisObj, timelineManager);
    }
}

/*
 * Class:     io_opentimeline_opentimelineio_Timeline
 * Method:    getTracks
 * Signature: ()Lio/opentimeline/opentimelineio/Stack;
 */
JNIEXPORT jobject JNICALL
Java_io_opentimeline_opentimelineio_Timeline_getTracks(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Timeline>>(env, thisObj);
    auto timeline = thisHandle->value;
    auto result = timeline->tracks();
    return stackFromNative(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Timeline
 * Method:    setTracks
 * Signature: (Lio/opentimeline/opentimelineio/Stack;)V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_Timeline_setTracks(
        JNIEnv *env, jobject thisObj, jobject stackObj) {
    if (stackObj == nullptr) {
        throwNullPointerException(env, "");
        return;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Timeline>>(env, thisObj);
    auto timeline = thisHandle->value;
    auto stackHandle =
            getHandle<SerializableObject::Retainer<Stack>>(env, stackObj);
    auto stack = stackHandle->value;
    timeline->set_tracks(stack);
}

/*
 * Class:     io_opentimeline_opentimelineio_Timeline
 * Method:    getGlobalStartTime
 * Signature: ()Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL
Java_io_opentimeline_opentimelineio_Timeline_getGlobalStartTime(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Timeline>>(env, thisObj);
    auto timeline = thisHandle->value;
    auto result = timeline->global_start_time();
    jobject resultObj = nullptr;
    if (result != nullopt)
        resultObj = rationalTimeToJObject(env, result.value());
    return resultObj;
}

/*
 * Class:     io_opentimeline_opentimelineio_Timeline
 * Method:    setGlobalStartTime
 * Signature: (Lio/opentimeline/opentime/RationalTime;)V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_Timeline_setGlobalStartTime(
        JNIEnv *env, jobject thisObj, jobject globalStartTimeRationalTime) {
    if (globalStartTimeRationalTime == nullptr) {
        throwNullPointerException(env, "");
        return;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Timeline>>(env, thisObj);
    auto timeline = thisHandle->value;
    optional<RationalTime> globalStartTime = nullopt;
    if (globalStartTimeRationalTime != nullptr)
        globalStartTime =
                rationalTimeFromJObject(env, globalStartTimeRationalTime);
    timeline->set_global_start_time(globalStartTime);
}

/*
 * Class:     io_opentimeline_opentimelineio_Timeline
 * Method:    getDuration
 * Signature: ()Lio/opentimeline/opentime/RationalTime;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Timeline_getDuration(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Timeline>>(env, thisObj);
    auto timeline = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result = timeline->duration(&errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return rationalTimeToJObject(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Timeline
 * Method:    getRangeOfChild
 * Signature: (Lio/opentimeline/opentimelineio/Composable;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Timeline_getRangeOfChild(
        JNIEnv *env,
        jobject thisObj,
        jobject composableChild) {
    if (composableChild == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Timeline>>(env, thisObj);
    auto timeline = thisHandle->value;
    auto childHandle =
            getHandle<SerializableObject::Retainer<Composable>>(env, composableChild);
    auto child = childHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result = timeline->range_of_child(child, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return timeRangeToJObject(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Timeline
 * Method:    getAudioTracksNative
 * Signature: ()[Lio/opentimeline/opentimelineio/Track;
 */
JNIEXPORT jobjectArray JNICALL
Java_io_opentimeline_opentimelineio_Timeline_getAudioTracksNative(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Timeline>>(env, thisObj);
    auto timeline = thisHandle->value;
    auto result = timeline->audio_tracks();
    return trackVectorToArray(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Timeline
 * Method:    getVideoTracksNative
 * Signature: ()[Lio/opentimeline/opentimelineio/Track;
 */
JNIEXPORT jobjectArray JNICALL
Java_io_opentimeline_opentimelineio_Timeline_getVideoTracksNative(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Timeline>>(env, thisObj);
    auto timeline = thisHandle->value;
    auto result = timeline->video_tracks();
    return trackVectorToArray(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Timeline
 * Method:    childrenIfNative
 * Signature: (Ljava/lang/Class;Lio/opentimeline/opentime/TimeRange;Z)[Lio/opentimeline/opentimelineio/Composable;
 */
JNIEXPORT jobjectArray JNICALL
Java_io_opentimeline_opentimelineio_Timeline_childrenIfNative(
        JNIEnv *env, jobject thisObj, jclass descendedFromClass, jobject searchRangeTimeRange, jboolean shallowSearch){
    if (searchRangeTimeRange == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Timeline>>(env, thisObj);
    auto timeline = thisHandle->value;
    optional<TimeRange> searchRange = nullopt;
    searchRange = timeRangeFromJObject(env, searchRangeTimeRange);
    jclass cls = env->GetObjectClass(descendedFromClass);
    jmethodID getName = env->GetMethodID(cls, "getName", "()Ljava/lang/String;");
    auto name = (jstring)env->CallObjectMethod(descendedFromClass, getName);
    const char* str = env->GetStringUTFChars(name, NULL);
    auto errorStatus = OTIO_NS::ErrorStatus();
    if(strcmp(str,"io.opentimeline.opentimelineio.Clip") == 0){
        auto result = timeline->children_if<Clip>(&errorStatus, searchRange, shallowSearch);
        processOTIOErrorStatus(env, errorStatus);
        env->ReleaseStringUTFChars(name, str);
        return clipRetainerVectorToArray(env, *(new std::vector<SerializableObject::Retainer<Clip>>(result)));
    }
    else if(strcmp(str,"io.opentimeline.opentimelineio.Track") == 0){
        auto result = timeline->children_if<Track>(&errorStatus, searchRange, shallowSearch);
        processOTIOErrorStatus(env, errorStatus);
        env->ReleaseStringUTFChars(name, str);
        return trackRetainerVectorToArray(env, *(new std::vector<SerializableObject::Retainer<Track>>(result)));
    }
    else if(strcmp(str,"io.opentimeline.opentimelineio.Gap") == 0){
        auto result = timeline->children_if<Gap>(&errorStatus, searchRange, shallowSearch);
        processOTIOErrorStatus(env, errorStatus);
        env->ReleaseStringUTFChars(name, str);
        return gapRetainerVectorToArray(env, *(new std::vector<SerializableObject::Retainer<Gap>>(result)));
    }
    //default return for Composable Type
    auto result = timeline->children_if(&errorStatus, searchRange, shallowSearch);
    processOTIOErrorStatus(env, errorStatus);
    env->ReleaseStringUTFChars(name, str);
    return composableRetainerVectorToArray(env, *(new std::vector<SerializableObject::Retainer<Composable>>(result)));
}
