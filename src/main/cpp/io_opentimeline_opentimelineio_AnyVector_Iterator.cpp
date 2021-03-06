// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

#include <exceptions.h>
#include <handle.h>
#include <io_opentimeline_opentimelineio_AnyVector_Iterator.h>
#include <utilities.h>

#include <opentimelineio/anyVector.h>
#include <opentimelineio/version.h>

using namespace opentimelineio::OPENTIMELINEIO_VERSION;

/*
 * Class:     io_opentimeline_opentimelineio_AnyVector_Iterator
 * Method:    initialize
 * Signature: (Lio/opentimeline/opentimelineio/AnyVector;)V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_AnyVector_00024Iterator_initialize(
        JNIEnv *env, jobject thisObj, jobject vectorObj) {
    if (vectorObj == nullptr)
        throwNullPointerException(env, "");
    else {
        auto vectorHandle = getHandle<OTIO_NS::AnyVector>(env, vectorObj);
        auto *it =
                new AnyVector::iterator(vectorHandle->begin());
        setHandle(env, thisObj, it);
    }
}

/*
 * Class:     io_opentimeline_opentimelineio_AnyVector_Iterator
 * Method:    nextNative
 * Signature:
 * (Lio/opentimeline/opentimelineio/AnyVector;)Lio/opentimeline/opentimelineio/Any;
 */
JNIEXPORT jobject JNICALL
Java_io_opentimeline_opentimelineio_AnyVector_00024Iterator_nextNative(
        JNIEnv *env, jobject thisObj, jobject vectorObj) {
    if (vectorObj == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    }
    auto vectorHandle = getHandle<AnyVector>(env, vectorObj);
    auto thisHandle = getHandle<AnyVector::iterator>(env, thisObj);
    if (*thisHandle == vectorHandle->end()) {
        throwIndexOutOfBoundsException(env, "");
        return nullptr;
    }
    auto result = &(**thisHandle);
    (*thisHandle)++;
    return anyFromNative(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_AnyVector_Iterator
 * Method:    previousNative
 * Signature:
 * (Lio/opentimeline/opentimelineio/AnyVector;)Lio/opentimeline/opentimelineio/Any;
 */
JNIEXPORT jobject JNICALL
Java_io_opentimeline_opentimelineio_AnyVector_00024Iterator_previousNative(
        JNIEnv *env, jobject thisObj, jobject vectorObj) {
    if (vectorObj == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    } else {
        auto vectorHandle = getHandle<AnyVector>(env, vectorObj);
        auto thisHandle = getHandle<AnyVector::iterator>(env, thisObj);
        if (*thisHandle == vectorHandle->begin()) {
            throwIndexOutOfBoundsException(env, "");
            return nullptr;
        } else {
            (*thisHandle)--;
            return anyFromNative(env, &(**thisHandle));
        }
    }
}

/*
 * Class:     io_opentimeline_opentimelineio_AnyVector_Iterator
 * Method:    hasNextNative
 * Signature: (Lio/opentimeline/opentimelineio/AnyVector;)Z
 */
JNIEXPORT jboolean JNICALL
Java_io_opentimeline_opentimelineio_AnyVector_00024Iterator_hasNextNative(
        JNIEnv *env, jobject thisObj, jobject vectorObj) {
    if (vectorObj == nullptr) {
        throwNullPointerException(env, "");
        return false;
    }
    auto vectorHandle = getHandle<AnyVector>(env, vectorObj);
    auto thisHandle = getHandle<AnyVector::iterator>(env, thisObj);
    return !(*thisHandle == vectorHandle->end());
}

/*
 * Class:     io_opentimeline_opentimelineio_AnyVector_Iterator
 * Method:    hasPreviousNative
 * Signature: (Lio/opentimeline/opentimelineio/AnyVector;)Z
 */
JNIEXPORT jboolean JNICALL
Java_io_opentimeline_opentimelineio_AnyVector_00024Iterator_hasPreviousNative(
        JNIEnv *env, jobject thisObj, jobject vectorObj) {
    if (vectorObj == nullptr) {
        throwNullPointerException(env, "");
        return false;
    }
    auto vectorHandle = getHandle<AnyVector>(env, vectorObj);
    auto thisHandle = getHandle<AnyVector::iterator>(env, thisObj);
    return !(*thisHandle == vectorHandle->begin());
}
