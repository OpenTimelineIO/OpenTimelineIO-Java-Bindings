// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

#include <exceptions.h>
#include <handle.h>
#include <io_opentimeline_opentimelineio_Serialization.h>
#include <opentimelineio/serialization.h>
#include <opentimelineio/version.h>
#include <utilities.h>

using namespace opentimelineio::OPENTIMELINEIO_VERSION;

/*
 * Class:     io_opentimeline_opentimelineio_Serialization
 * Method:    serializeJSONToStringNative
 * Signature: (Lio/opentimeline/opentimelineio/Any;I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_io_opentimeline_opentimelineio_Serialization_serializeJSONToStringNative
        (JNIEnv *env, jobject thisObj, jobject anyValueObj, jint indent) {
    if (anyValueObj == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    }
    auto anyValueHandle = getHandle<any>(env, anyValueObj);
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result = env->NewStringUTF(serialize_json_to_string(
            *anyValueHandle, &errorStatus, indent)
                                            .c_str());
    processOTIOErrorStatus(env, errorStatus);
    return result;
}

/*
 * Class:     io_opentimeline_opentimelineio_Serialization
 * Method:    serializeJSONToFileNative
 * Signature: (Lio/opentimeline/opentimelineio/Any;Ljava/lang/String;I)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Serialization_serializeJSONToFileNative
        (JNIEnv *env, jobject thisObj, jobject anyValueObj, jstring fileName, jint indent) {
    if (anyValueObj == nullptr || fileName == nullptr) {
        throwNullPointerException(env, "");
        return false;
    }
    auto anyValueHandle = getHandle<any>(env, anyValueObj);
    auto errorStatus = OTIO_NS::ErrorStatus();
    std::string fileNameStr = env->GetStringUTFChars(fileName, nullptr);
    auto result = serialize_json_to_file(
            *anyValueHandle, fileNameStr, &errorStatus, indent);
    processOTIOErrorStatus(env, errorStatus);
    return result;
}