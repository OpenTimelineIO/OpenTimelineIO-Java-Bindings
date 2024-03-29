// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

#include <exceptions.h>
#include <handle.h>
#include <io_opentimeline_opentimelineio_Composition.h>
#include <opentimelineio/composition.h>
#include <opentimelineio/version.h>
#include <utilities.h>

using namespace opentimelineio::OPENTIMELINEIO_VERSION;

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    initialize
 * Signature: (Ljava/lang/String;Lio/opentimeline/opentime/TimeRange;Lio/opentimeline/opentimelineio/AnyDictionary;[Lio/opentimeline/opentimelineio/Effect;[Lio/opentimeline/opentimelineio/Marker;)V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_Composition_initialize(
        JNIEnv *env,
        jobject thisObj,
        jstring name,
        jobject sourceRangeObj,
        jobject metadataObj,
        jobjectArray effectsArray,
        jobjectArray markersArray) {
    if (name == nullptr || metadataObj == nullptr)
        throwNullPointerException(env, "");
    else {
        std::string nameStr = env->GetStringUTFChars(name, 0);
        OTIO_NS::optional<TimeRange> sourceRange = OTIO_NS::nullopt;
        if (sourceRangeObj != nullptr) { sourceRange = timeRangeFromJObject(env, sourceRangeObj); }
        auto metadataHandle =
                getHandle<AnyDictionary>(env, metadataObj);
        auto effects = effectVectorFromArray(env, effectsArray);
        auto markers = markerVectorFromArray(env, markersArray);
        auto composition = new Composition(
                nameStr, sourceRange, *metadataHandle, effects, markers);
        auto compositionManager =
                new SerializableObject::Retainer<Composition>(composition);
        setHandle(env, thisObj, compositionManager);
    }
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getCompositionKind
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_io_opentimeline_opentimelineio_Composition_getCompositionKind(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Composition>>(env, thisObj);
    auto composition = thisHandle->value;
    return env->NewStringUTF(composition->composition_kind().c_str());
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getChildrenNative
 * Signature: ()[Lio/opentimeline/opentimelineio/SerializableObject/Retainer;
 */
JNIEXPORT jobjectArray JNICALL
Java_io_opentimeline_opentimelineio_Composition_getChildrenNative(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Composition>>(env, thisObj);
    auto composition = thisHandle->value;
    const std::vector<SerializableObject::Retainer<Composable>> &
            result = composition->children();
    return composableRetainerVectorToArray(
            env,
            *(new std::vector<
                    SerializableObject::Retainer<Composable>>(
                    result)));
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    clearChildren
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_Composition_clearChildren(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Composition>>(env, thisObj);
    auto composition = thisHandle->value;
    composition->clear_children();
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    setChildrenNative
 * Signature: ([Lio/opentimeline/opentimelineio/Composable;)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_Composition_setChildrenNative(
        JNIEnv *env,
        jobject thisObj,
        jobjectArray composableArray) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Composition>>(env, thisObj);
    auto composition = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto children = composableVectorFromArray(env, composableArray);
    composition->set_children(children, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    insertChild
 * Signature: (ILio/opentimeline/opentimelineio/Composable;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Composition_insertChild(
        JNIEnv *env,
        jobject thisObj,
        jint index,
        jobject composableChild) {
    if (composableChild == nullptr) {
        throwNullPointerException(env, "");
        return false;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Composition>>(env, thisObj);
    auto composition = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto childHandle = getHandle<SerializableObject::Retainer<Composable>>(env, composableChild);
    auto child = childHandle->value;
    bool result = composition->insert_child(index, child, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return result;
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    setChild
 * Signature: (ILio/opentimeline/opentimelineio/Composable;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Composition_setChild(
        JNIEnv *env,
        jobject thisObj,
        jint index,
        jobject composableChild) {
    if (composableChild == nullptr) {
        throwNullPointerException(env, "");
        return false;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Composition>>(env, thisObj);
    auto composition = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto childHandle = getHandle<SerializableObject::Retainer<Composable>>(env, composableChild);
    auto child = childHandle->value;
    bool result = composition->set_child(index, child, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return result;
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    removeChild
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Composition_removeChild(
        JNIEnv *env, jobject thisObj, jint index) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composition>>(env, thisObj);
    auto composition = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    bool result = composition->remove_child(index, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return result;
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    appendChild
 * Signature: (Lio/opentimeline/opentimelineio/Composable;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_Composition_appendChild(
        JNIEnv *env,
        jobject thisObj,
        jobject composableChild) {
    if (composableChild == nullptr) {
        throwNullPointerException(env, "");
        return false;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composition>>(env, thisObj);
    auto composition = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto childHandle = getHandle<SerializableObject::Retainer<OTIO_NS::Composable>>
            (env, composableChild);
    auto child = childHandle->value;
    bool result = composition->append_child(child, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return result;
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    isParentOf
 * Signature: (Lio/opentimeline/opentimelineio/Composable;)Z
 */
JNIEXPORT jboolean JNICALL
Java_io_opentimeline_opentimelineio_Composition_isParentOf(
        JNIEnv *env, jobject thisObj, jobject composableChild) {
    if (composableChild == nullptr) {
        throwNullPointerException(env, "");
        return false;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composition>>(env, thisObj);
    auto composition = thisHandle->value;
    auto childHandle = getHandle<SerializableObject::Retainer<OTIO_NS::Composable>>
            (env, composableChild);
    auto child = childHandle->value;
    return composition->is_parent_of(child);
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getHandlesOfChild
 * Signature: (Lio/opentimeline/opentimelineio/Composable;)Lio/opentimeline/util/Pair;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_getHandlesOfChild(
        JNIEnv *env,
        jobject thisObj,
        jobject composableChild) {
    if (composableChild == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<Composition>>(env, thisObj);
    auto composition = thisHandle->value;
    auto childHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composable>>
                    (env, thisObj);
    auto child = childHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result = composition->handles_of_child(child, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);

    jobject first = (result.first != OTIO_NS::nullopt)
                    ? rationalTimeToJObject(env, result.first.value())
                    : nullptr;
    jobject second = (result.second != OTIO_NS::nullopt)
                     ? rationalTimeToJObject(env, result.second.value())
                     : nullptr;

    jclass pairClass = env->FindClass("io/opentimeline/util/Pair");
    jmethodID pairInit = env->GetMethodID(
            pairClass, "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
    jobject pairObject = env->NewObject(pairClass, pairInit, first, second);
    return pairObject;
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getRangeOfChildAtIndex
 * Signature: (I)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_getRangeOfChildAtIndex(
        JNIEnv *env, jobject thisObj, jint index) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composition>>(env, thisObj);
    auto composition = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result = composition->range_of_child_at_index(index, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return timeRangeToJObject(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getTrimmedRangeOfChildAtIndex
 * Signature: (I)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_getTrimmedRangeOfChildAtIndex(
        JNIEnv *env, jobject thisObj, jint index) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composition>>
                    (env, thisObj);
    auto composition = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result =
            composition->trimmed_range_of_child_at_index(index, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return timeRangeToJObject(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getRangeOfChild
 * Signature: (Lio/opentimeline/opentimelineio/Composable;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_getRangeOfChild(
        JNIEnv *env,
        jobject thisObj,
        jobject composableChild) {
    if (composableChild == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composition>>
                    (env, thisObj);
    auto composition = thisHandle->value;
    auto childHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composable>>
                    (env, composableChild);
    auto child = childHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result = composition->range_of_child(child, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return timeRangeToJObject(env, result);
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getTrimmedRangeOfChild
 * Signature: (Lio/opentimeline/opentimelineio/Composable;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_getTrimmedRangeOfChild(
        JNIEnv *env,
        jobject thisObj,
        jobject composableChild) {
    if (composableChild == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composition>>
                    (env, thisObj);
    auto composition = thisHandle->value;
    auto childHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composable>>
                    (env, composableChild);
    auto child = childHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result =
            composition->trimmed_range_of_child(child, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    jobject resultObj = nullptr;
    if (result != OTIO_NS::nullopt)
        resultObj = timeRangeToJObject(env, result.value());
    return resultObj;
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    trimChildRange
 * Signature: (Lio/opentimeline/opentime/TimeRange;)Lio/opentimeline/opentime/TimeRange;
 */
JNIEXPORT jobject JNICALL
Java_io_opentimeline_opentimelineio_Composition_trimChildRange(
        JNIEnv *env, jobject thisObj, jobject timeRangeObj) {
    if (timeRangeObj == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composition>>
                    (env, thisObj);
    auto composition = thisHandle->value;
    auto timeRange = timeRangeFromJObject(env, timeRangeObj);
    auto result = composition->trim_child_range(timeRange);
    jobject resultObj = nullptr;
    if (result != OTIO_NS::nullopt)
        resultObj = timeRangeToJObject(env, result.value());
    return resultObj;
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    hasChild
 * Signature: (Lio/opentimeline/opentimelineio/Composable;)Z
 */
JNIEXPORT jboolean JNICALL
Java_io_opentimeline_opentimelineio_Composition_hasChild(
        JNIEnv *env, jobject thisObj, jobject composableChild) {
    if (composableChild == nullptr) {
        throwNullPointerException(env, "");
        return false;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composition>>
                    (env, thisObj);
    auto composition = thisHandle->value;
    auto childHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composable>>
                    (env, thisObj);
    auto child = childHandle->value;
    return composition->has_child(child);
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    getRangeOfAllChildren
 * Signature: ()Ljava/util/HashMap;
 */
JNIEXPORT jobject JNICALL Java_io_opentimeline_opentimelineio_Composition_getRangeOfAllChildren(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<OTIO_NS::Composition>>
                    (env, thisObj);
    auto composition = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result = composition->range_of_all_children(&errorStatus);
    processOTIOErrorStatus(env,errorStatus);

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
        setHandle(env, composableObject, first);

        jobject tr = timeRangeToJObject(env, second);

        env->CallObjectMethod(hashMapObj, hashMapPut, composableObject, tr);
    }
    return hashMapObj;
}

/*
 * Class:     io_opentimeline_opentimelineio_Composition
 * Method:    childrenIfNative
 * Signature: (Ljava/lang/Class;Lio/opentimeline/opentime/TimeRange;Z)[Lio/opentimeline/opentimelineio/Composable;
 */
 JNIEXPORT jobjectArray JNICALL
 Java_io_opentimeline_opentimelineio_Composition_childrenIfNative(
         JNIEnv *env, jobject thisObj, jclass descendedFromClass, jobject searchRangeTimeRangeOptional, jboolean shallowSearch){
     if (searchRangeTimeRangeOptional == nullptr) {
         throwNullPointerException(env, "");
         return nullptr;
     }
     return childrenIfWrapperUtil<Composition>(env, thisObj, descendedFromClass, searchRangeTimeRangeOptional, shallowSearch);
 }
