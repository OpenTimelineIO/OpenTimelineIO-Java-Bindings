// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

#include <exceptions.h>
#include <handle.h>
#include <io_opentimeline_opentimelineio_SerializableCollection.h>
#include <opentimelineio/serializableCollection.h>
#include <opentimelineio/version.h>
#include <utilities.h>

using namespace opentimelineio::OPENTIMELINEIO_VERSION;

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    initialize
 * Signature: (Ljava/lang/String;[Lio/opentimeline/opentimelineio/SerializableObject;Lio/opentimeline/opentimelineio/AnyDictionary;)V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_SerializableCollection_initialize(
        JNIEnv *env,
        jobject thisObj,
        jstring name,
        jobjectArray childrenArray,
        jobject metadata) {
    if (name == nullptr || childrenArray == nullptr || metadata == nullptr) {
        throwNullPointerException(env, "");
        return;
    }
    std::string nameStr = env->GetStringUTFChars(name, 0);
    auto children = serializableObjectVectorFromArray(env, childrenArray);
    auto metadataHandle = getHandle<AnyDictionary>(env, metadata);
    auto serializableCollection = new SerializableCollection(
            nameStr, children, *metadataHandle);
    auto serializableCollectionManager =
            new SerializableObject::Retainer<SerializableCollection>(serializableCollection);
    setHandle(env, thisObj, serializableCollectionManager);
}

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    getChildrenNative
 * Signature: ()[Lio/opentimeline/opentimelineio/SerializableObject/Retainer;
 */
JNIEXPORT jobjectArray JNICALL
Java_io_opentimeline_opentimelineio_SerializableCollection_getChildrenNative(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<SerializableCollection>>(env, thisObj);
    auto serializableCollection = thisHandle->value;
    std::vector<
            SerializableObject::Retainer<SerializableObject>>
            children = serializableCollection->children();
    return serializableObjectRetainerVectorToArray(
            env,
            *(new std::vector<
                    SerializableObject::Retainer<SerializableObject>>(
                    children)));
}

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    setChildrenNative
 * Signature: ([Lio/opentimeline/opentimelineio/SerializableObject;)V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_SerializableCollection_setChildrenNative(
        JNIEnv *env, jobject thisObj, jobjectArray childrenArray) {
    if (childrenArray == nullptr) {
        throwNullPointerException(env, "");
        return;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<SerializableCollection>>(env, thisObj);
    auto serializableCollection = thisHandle->value;
    auto children = serializableObjectVectorFromArray(env, childrenArray);
    serializableCollection->set_children(children);
}

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    clearChildren
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_io_opentimeline_opentimelineio_SerializableCollection_clearChildren(
        JNIEnv *env, jobject thisObj) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<SerializableCollection>>(env, thisObj);
    auto serializableCollection = thisHandle->value;
    serializableCollection->clear_children();
}

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    setChild
 * Signature: (ILio/opentimeline/opentimelineio/SerializableObject;)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_SerializableCollection_setChild(
        JNIEnv *env,
        jobject thisObj,
        jint index,
        jobject childObj) {
    if (childObj == nullptr) {
        throwNullPointerException(env, "");
        return false;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<SerializableCollection>>(env, thisObj);
    auto serializableCollection = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto childHandle =
            getHandle<SerializableObject::Retainer<SerializableObject>>(env, thisObj);
    auto child = childHandle->value;
    auto result = serializableCollection->set_child(index, child, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return result;
}

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    insertChild
 * Signature: (ILio/opentimeline/opentimelineio/SerializableObject;)V
 */
JNIEXPORT void JNICALL Java_io_opentimeline_opentimelineio_SerializableCollection_insertChild
        (JNIEnv *env, jobject thisObj, jint index, jobject childObj) {
    if (childObj == nullptr) {
        throwNullPointerException(env, "");
        return;
    }
    auto thisHandle =
            getHandle<SerializableObject::Retainer<SerializableCollection>>(env, thisObj);
    auto serializableCollection = thisHandle->value;
    auto childHandle =
            getHandle<SerializableObject::Retainer<SerializableObject>>(env, thisObj);
    auto child = childHandle->value;
    serializableCollection->insert_child(index, child);
}

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    removeChild
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_io_opentimeline_opentimelineio_SerializableCollection_removeChild(
        JNIEnv *env, jobject thisObj, jint index) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<SerializableCollection>>(env, thisObj);
    auto serializableCollection = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    auto result = serializableCollection->remove_child(index, &errorStatus);
    processOTIOErrorStatus(env, errorStatus);
    return result;
}

/*
 * Class:     io_opentimeline_opentimelineio_SerializableCollection
 * Method:    clipIfNative
 * Signature: (Ljava/util/Optional;Z)[Lio/opentimeline/opentimelineio/Clip;
 */
JNIEXPORT jobjectArray JNICALL Java_io_opentimeline_opentimelineio_SerializableCollection_clipIfNative
        (JNIEnv *env, jobject thisObj, jobject searchRangeTimeRangeOptional, jboolean shallowSearch){
    if (searchRangeTimeRangeOptional == nullptr) {
        throwNullPointerException(env, "");
        return nullptr;
    }
    return getClipIfResult<SerializableCollection>(env, thisObj, searchRangeTimeRangeOptional, shallowSearch);
}
