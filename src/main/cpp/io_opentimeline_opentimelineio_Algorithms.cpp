// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

#include <exceptions.h>
#include <handle.h>
#include <io_opentimeline_opentimelineio_Algorithms.h>
#include <opentimelineio/stackAlgorithm.h>
#include <opentimelineio/trackAlgorithm.h>
#include <opentimelineio/version.h>
#include <utilities.h>

using namespace opentimelineio::OPENTIMELINEIO_VERSION;

/*
 * Class:     io_opentimeline_opentimelineio_Algorithms
 * Method:    flattenStack
 * Signature: (Lio/opentimeline/opentimelineio/Stack;)Lio/opentimeline/opentimelineio/Track;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Algorithms_flattenStack
        (JNIEnv *env, jobject thisObj, jobject inStack) {
    if (inStack == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    }
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto inStackHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Stack>>(env, inStack);
    auto stack = inStackHandle->value;
    auto result = OTIO_NS::flatten_stack(stack, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return trackFromNative(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Algorithms
 * Method:    flattenStackNative
 * Signature: ([Lio/opentimeline/opentimelineio/Track;)Lio/opentimeline/opentimelineio/Track;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Algorithms_flattenStackNative
        (JNIEnv *env, jobject thisObj, jobjectArray tracksArray) {
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto tracksVector = trackVectorFromArray(env, tracksArray);
    auto result = flatten_stack(tracksVector, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return trackFromNative(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Algorithms
 * Method:    trackTrimmedToRange
 * Signature: (Lio/opentimeline/opentimelineio/Track;Lio/opentimeline/opentime/TimeRange;)Lio/opentimeline/opentimelineio/Track;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Algorithms_trackTrimmedToRange
        (JNIEnv *env, jobject thisObj, jobject inTrack, jobject trimRangeObj) {
    if (inTrack == nullptr || trimRangeObj == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    }
    auto inTrackHandle =
            getHandle<SerializableObject::Retainer<Track>>(env, inTrack);
    auto track = inTrackHandle->value;
    auto trimRange = timeRangeFromJObject(env, trimRangeObj);
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result = track_trimmed_to_range(
            track, trimRange, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return trackFromNative(env, result);
}
