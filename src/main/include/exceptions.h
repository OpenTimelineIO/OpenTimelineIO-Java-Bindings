// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

#include <jni.h>
#include <opentime/errorStatus.h>

#ifndef _EXCEPTIONS_H_INCLUDED_
#define _EXCEPTIONS_H_INCLUDED_

inline jint throwNullPointerException(JNIEnv *env, const char *message) {
    const char *className = "java/lang/NullPointerException";
    jclass exClass = env->FindClass(className);
    return env->ThrowNew(exClass, message);
}

inline jint throwNoSuchElementException(JNIEnv *env, const char *message) {
    const char *className = "java/util/NoSuchElementException";
    jclass exClass = env->FindClass(className);
    return env->ThrowNew(exClass, message);
}

inline jint throwIndexOutOfBoundsException(JNIEnv *env, const char *message) {
    const char *className = "java/util/IndexOutOfBoundsException";
    jclass exClass = env->FindClass(className);
    return env->ThrowNew(exClass, message);
}

inline jint throwRuntimeException(JNIEnv *env, const char *message) {
    const char *className = "java/lang/RuntimeException";
    jclass exClass = env->FindClass(className);
    return env->ThrowNew(exClass, message);
}

inline jint processOpenTimeErrorStatus(JNIEnv *env, opentime::ErrorStatus &errorStatus) {
    std::string otio_msg = "An OpenTime call failed with ";
    switch (errorStatus.outcome) {
        case opentime::ErrorStatus::Outcome::INVALID_TIMECODE_RATE:
        case opentime::ErrorStatus::Outcome::INVALID_TIMECODE_STRING:
        case opentime::ErrorStatus::Outcome::INVALID_TIME_STRING:
        case opentime::ErrorStatus::Outcome::INVALID_RATE_FOR_DROP_FRAME_TIMECODE:
        case opentime::ErrorStatus::Outcome::NON_DROPFRAME_RATE:
        case opentime::ErrorStatus::Outcome::TIMECODE_RATE_MISMATCH:
        case opentime::ErrorStatus::Outcome::NEGATIVE_VALUE: {
            const char *className = "java/lang/IllegalArgumentException";
            jclass exClass = env->FindClass(className);
            otio_msg = otio_msg + opentime::ErrorStatus::outcome_to_string(errorStatus.outcome);
            return env->ThrowNew(exClass, otio_msg.c_str());
        }
        case opentime::ErrorStatus::Outcome::OK:
            return 0;
        default: {
            const char *className = "java/lang/Exception";
            jclass exClass = env->FindClass(className);
            otio_msg = otio_msg + "an unknown Exception.";
            return env->ThrowNew(exClass, otio_msg.c_str());
        }
    }
}

#endif