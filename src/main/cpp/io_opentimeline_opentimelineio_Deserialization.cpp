// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

#include <exceptions.h>
#include <handle.h>
#include <io_opentimeline_opentimelineio_Deserialization.h>
#include <opentimelineio/deserialization.h>
#include <opentimelineio/version.h>

using namespace opentimelineio::OPENTIMELINEIO_VERSION;

/*
 * Class:     io_opentimeline_opentimelineio_Deserialization
 * Method:    deserializeJSONFromString
 * Signature: (Ljava/lang/String;Lio/opentimeline/opentimelineio/Any;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Deserialization_deserializeJSONFromString
        (JNIEnv *env, jobject thisObj, jstring input, jobject anyDestination) {
    if (anyDestination == nullptr || input == nullptr) {
        throwNullPointerException(env, "");
        return false;
    }
    std::string inputStr = env->GetStringUTFChars(input, nullptr);
    auto anyDestinationHandle =
            getHandle<any>(env, anyDestination);
    auto errorStatus = OTIO_NS::ErrorStatus();
    bool result = deserialize_json_from_string(
            inputStr, anyDestinationHandle, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return result;
}

/*
 * Class:     io_opentimeline_opentimelineio_Deserialization
 * Method:    deserializeJSONFromFile
 * Signature: (Ljava/lang/String;Lio/opentimeline/opentimelineio/Any;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Deserialization_deserializeJSONFromFile
        (JNIEnv *env, jobject thisObj, jstring fileName, jobject anyDestination) {
    if (anyDestination == nullptr || fileName == nullptr) {
        throwNullPointerException(env, "");
        return false;
    }
    std::string fileNameStr = env->GetStringUTFChars(fileName, nullptr);
    auto anyDestinationHandle =
            getHandle<any>(env, anyDestination);
    auto errorStatus = OTIO_NS::ErrorStatus();
    bool result = deserialize_json_from_string(
            fileNameStr, anyDestinationHandle, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return result;
}
