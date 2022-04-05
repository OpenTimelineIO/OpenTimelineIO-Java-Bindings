// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

#include <jni.h>

#include <exceptions.h>
#include <handle.h>
#include <opentime/rationalTime.h>
#include <opentime/timeRange.h>
#include <opentime/timeTransform.h>
#include <opentime/version.h>
#include <opentimelineio/anyDictionary.h>
#include <opentimelineio/composable.h>
#include <opentimelineio/effect.h>
#include <opentimelineio/marker.h>
#include <opentimelineio/mediaReference.h>
#include <opentimelineio/serializableObject.h>
#include <opentimelineio/stack.h>
#include <opentimelineio/track.h>
#include <opentimelineio/version.h>
#include <opentimelineio/clip.h>
#include <opentimelineio/gap.h>
#include <opentimelineio/transition.h>
#include <opentimelineio/composition.h>
#include <cstring>
#include <unordered_map>
#include <memory>
#include <iostream>

#ifndef _UTILITIES_H_INCLUDED_
#define _UTILITIES_H_INCLUDED_

using namespace opentimelineio::OPENTIMELINEIO_VERSION;

inline void registerObjectToOTIOFactory(JNIEnv *env, jobject otioObject) {

    if (otioObject == nullptr) {
        throwNullPointerException(env, "");
        return;
    }

    jclass otioFactoryClass = env->FindClass("io/opentimeline/OTIOFactory");

    jmethodID registerObject = env->GetMethodID(otioFactoryClass, "registerObject",
                                                "(Lio/opentimeline/OTIOObject;)V");
    jmethodID getFactoryInstance = env->GetStaticMethodID(otioFactoryClass, "getInstance",
                                                          "()Lio/opentimeline/OTIOFactory;");
    jobject factoryInstance = env->CallStaticObjectMethod(otioFactoryClass, getFactoryInstance);

    env->CallVoidMethod(factoryInstance, registerObject, otioObject);
}

inline std::vector<SerializableObject *>
serializableObjectVectorFromArray(JNIEnv *env, jobjectArray array) {
    int arrayLength = env->GetArrayLength(array);
    std::vector<SerializableObject *> objectVector;
    objectVector.reserve(arrayLength);
    for (int i = 0; i < arrayLength; ++i) {
        jobject element = env->GetObjectArrayElement(array, i);
        auto elementHandle =
                getHandle<SerializableObject::Retainer<SerializableObject>>(env, element);
        auto serializableObject = elementHandle->value;
        objectVector.push_back(serializableObject);
    }
    return objectVector;
}

inline std::vector<Effect *>
effectVectorFromArray(JNIEnv *env, jobjectArray array) {
    int arrayLength = env->GetArrayLength(array);
    std::vector<Effect *> objectVector;
    objectVector.reserve(arrayLength);
    for (int i = 0; i < arrayLength; ++i) {
        jobject element = env->GetObjectArrayElement(array, i);
        auto elementHandle =
                getHandle<SerializableObject::Retainer<Effect>>(env, element);
        auto effect = elementHandle->value;
        objectVector.push_back(effect);
    }
    return objectVector;
}

inline std::vector<Marker *>
markerVectorFromArray(JNIEnv *env, jobjectArray array) {
    int arrayLength = env->GetArrayLength(array);
    std::vector<Marker *> objectVector;
    objectVector.reserve(arrayLength);
    for (int i = 0; i < arrayLength; ++i) {
        jobject element = env->GetObjectArrayElement(array, i);
        auto elementHandle =
                getHandle<SerializableObject::Retainer<Marker>>(env, element);
        auto marker = elementHandle->value;
        objectVector.push_back(marker);
    }
    return objectVector;
}

inline std::vector<Composable *>
composableVectorFromArray(JNIEnv *env, jobjectArray array) {
    int arrayLength = env->GetArrayLength(array);
    std::vector<Composable *> objectVector;
    objectVector.reserve(arrayLength);
    for (int i = 0; i < arrayLength; ++i) {
        jobject element = env->GetObjectArrayElement(array, i);
        auto elementHandle =
                getHandle<SerializableObject::Retainer<Composable>>(env, element);
        auto composable = elementHandle->value;
        objectVector.push_back(composable);
    }
    return objectVector;
}

inline std::vector<Track *>
trackVectorFromArray(JNIEnv *env, jobjectArray array) {
    int arrayLength = env->GetArrayLength(array);
    std::vector<Track *> objectVector;
    objectVector.reserve(arrayLength);
    for (int i = 0; i < arrayLength; ++i) {
        jobject element = env->GetObjectArrayElement(array, i);
        auto elementHandle =
                getHandle<SerializableObject::Retainer<Track>>(env, element);
        auto track = elementHandle->value;
        objectVector.push_back(track);
    }
    return objectVector;
}

std::map<std::type_info const *, std::string> getAnyType();

std::string getAnyType(const std::type_info &typeInfo);

inline std::string getSerializableObjectJavaClassFromNative(OTIO_NS::SerializableObject *serializableObject) {
    static std::once_flag classFlag;
    static std::unique_ptr<std::map<std::string, std::string>> class_dispatch_table;
    std::call_once(classFlag, []() {
        class_dispatch_table = std::unique_ptr<std::map<std::string, std::string>>(
                new std::map<std::string, std::string>());
        (*class_dispatch_table)["Clip"] = "io/opentimeline/opentimelineio/Clip";
        (*class_dispatch_table)["Composable"] = "io/opentimeline/opentimelineio/Composable";
        (*class_dispatch_table)["Composition"] = "io/opentimeline/opentimelineio/Composition";
        (*class_dispatch_table)["Effect"] = "io/opentimeline/opentimelineio/Effect";
        (*class_dispatch_table)["ExternalReference"] = "io/opentimeline/opentimelineio/ExternalReference";
        (*class_dispatch_table)["FreezeFrame"] = "io/opentimeline/opentimelineio/FreezeFrame";
        (*class_dispatch_table)["Gap"] = "io/opentimeline/opentimelineio/Gap";
        (*class_dispatch_table)["GeneratorReference"] = "io/opentimeline/opentimelineio/GeneratorReference";
        (*class_dispatch_table)["ImageSequenceReference"] = "io/opentimeline/opentimelineio/ImageSequenceReference";
        (*class_dispatch_table)["Item"] = "io/opentimeline/opentimelineio/Item";
        (*class_dispatch_table)["LinearTimeWarp"] = "io/opentimeline/opentimelineio/LinearTimeWarp";
        (*class_dispatch_table)["Marker"] = "io/opentimeline/opentimelineio/Marker";
        (*class_dispatch_table)["MediaReference"] = "io/opentimeline/opentimelineio/MediaReference";
        (*class_dispatch_table)["MissingReference"] = "io/opentimeline/opentimelineio/MissingReference";
        (*class_dispatch_table)["SerializableCollection"] = "io/opentimeline/opentimelineio/SerializableCollection";
        (*class_dispatch_table)["SerializableObject"] = "io/opentimeline/opentimelineio/SerializableObject";
        (*class_dispatch_table)["SerializableObjectWithMetadata"] = "io/opentimeline/opentimelineio/SerializableObjectWithMetadata";
        (*class_dispatch_table)["Stack"] = "io/opentimeline/opentimelineio/Stack";
        (*class_dispatch_table)["TimeEffect"] = "io/opentimeline/opentimelineio/TimeEffect";
        (*class_dispatch_table)["Timeline"] = "io/opentimeline/opentimelineio/Timeline";
        (*class_dispatch_table)["Track"] = "io/opentimeline/opentimelineio/Track";
        (*class_dispatch_table)["Transition"] = "io/opentimeline/opentimelineio/Transition";
        (*class_dispatch_table)["UnknownSchema"] = "io/opentimeline/opentimelineio/UnknownSchema";
    });

    return (*class_dispatch_table)[serializableObject->schema_name()];
}

/* this deepcopies any */
inline jobject
anyFromNative(JNIEnv *env, any *native) {
    if (native == nullptr)return nullptr;
    jclass cls = env->FindClass("io/opentimeline/opentimelineio/Any");
    if (cls == NULL) return NULL;

//    std::string anyType = _type_dispatch_table[&native->type()];
//    std::string anyType = getAnyType()[native->type()];
    std::string anyType = getAnyType(native->type());
    // Get the Method ID of the constructor which takes an otioNative
    jmethodID anyInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == anyInit) return NULL;

    auto newAny = new any(*native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(newAny));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.Any";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, anyInit, otioNative);

    jfieldID anyTypeStringID = env->GetFieldID(cls, "anyTypeClass", "Ljava/lang/String;");
    jstring anyTypeString = env->NewStringUTF(anyType.c_str());
    env->SetObjectField(newObj, anyTypeStringID, anyTypeString);

    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

/* this deepcopies anyDictionary */
inline jobject
anyDictionaryFromNative(JNIEnv *env, OTIO_NS::AnyDictionary *native) {
    if (native == nullptr)return nullptr;
    jclass cls = env->FindClass("io/opentimeline/opentimelineio/AnyDictionary");
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID dictInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == dictInit) return NULL;

    auto newDict = new AnyDictionary(*native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(newDict));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.AnyDictionary";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, dictInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

/* this deepcopies anyDictionary::iterator */
inline jobject
anyDictionaryIteratorFromNative(
        JNIEnv *env, AnyDictionary::iterator *native) {
    if (native == nullptr)return nullptr;
    jclass cls =
            env->FindClass("io/opentimeline/opentimelineio/AnyDictionary$Iterator");
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID itInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == itInit) return NULL;

    auto newIt = new AnyDictionary::iterator(*native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(newIt));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.AnyDictionary.Iterator";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, itInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

/* this deepcopies anyVector */
inline jobject
anyVectorFromNative(JNIEnv *env, AnyVector *native) {
    if (native == nullptr)return nullptr;
    jclass cls = env->FindClass("io/opentimeline/opentimelineio/AnyVector");
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID vecInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == vecInit) return NULL;

    auto newVec = new AnyVector(*native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(newVec));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.AnyVector";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, vecInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

/* this deepcopies anyVector::iterator */
inline jobject
anyVectorIteratorFromNative(JNIEnv *env, AnyVector::iterator *native) {
    if (native == nullptr)return nullptr;
    jclass cls =
            env->FindClass("io/opentimeline/opentimelineio/AnyVector$Iterator");
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID itInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == itInit) return NULL;

    auto newIt = new AnyVector::iterator(*native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(newIt));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.AnyVector.Iterator";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, itInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

/* Following functions create new Retainer<T> objects thereby increasing the reference count */

inline jobject
serializableObjectFromNative(JNIEnv *env, SerializableObject *native) {
    if (native == nullptr)return nullptr;
    std::string javaCls = getSerializableObjectJavaClassFromNative(native);
    jclass cls =
            env->FindClass(javaCls.c_str());
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID soInit =
            env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == soInit) return NULL;

    auto serializableObjectManager =
            new SerializableObject::Retainer<SerializableObject>(native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(serializableObjectManager));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.SerializableObject";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, soInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

inline jobject
effectFromNative(JNIEnv *env, Effect *native) {
    if (native == nullptr)return nullptr;
    std::string javaCls = getSerializableObjectJavaClassFromNative(native);
    jclass cls =
            env->FindClass(javaCls.c_str());
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID effectInit =
            env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == effectInit) return NULL;

    auto effectManager =
            new SerializableObject::Retainer<Effect>(native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(effectManager));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.Effect";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, effectInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

inline jobject
markerFromNative(JNIEnv *env, Marker *native) {
    if (native == nullptr)return nullptr;
    std::string javaCls = getSerializableObjectJavaClassFromNative(native);
    jclass cls =
            env->FindClass(javaCls.c_str());
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID markerInit =
            env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == markerInit) return NULL;

    auto effectManager =
            new SerializableObject::Retainer<Marker>(native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(effectManager));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.Marker";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, markerInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

inline jobject
composableFromNative(JNIEnv *env, OTIO_NS::Composable *native) {
    if (native == nullptr)return nullptr;
    std::string javaCls = getSerializableObjectJavaClassFromNative(native);
    jclass cls =
            env->FindClass(javaCls.c_str());
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNatove
    jmethodID composableInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == composableInit) return NULL;

    auto composableManager =
            new SerializableObject::Retainer<Composable>(native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(composableManager));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.Composable";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, composableInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

inline jobject
compositionFromNative(JNIEnv *env, OTIO_NS::Composition *native) {
    if (native == nullptr)return nullptr;
    std::string javaCls = getSerializableObjectJavaClassFromNative(native);
    jclass cls =
            env->FindClass(javaCls.c_str());
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID compositionInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == compositionInit) return NULL;

    auto compositionManager =
            new SerializableObject::Retainer<Composable>(native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(compositionManager));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.Composition";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, compositionInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

inline jobject
mediaReferenceFromNative(JNIEnv *env, OTIO_NS::MediaReference *native) {
    if (native == nullptr)return nullptr;
    std::string javaCls = getSerializableObjectJavaClassFromNative(native);
    jclass cls =
            env->FindClass(javaCls.c_str());
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID mrInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == mrInit) return NULL;

    auto mrManager =
            new SerializableObject::Retainer<MediaReference>(native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(mrManager));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.MediaReference";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, mrInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

inline jobject
stackFromNative(JNIEnv *env, OTIO_NS::Stack *native) {
    if (native == nullptr)return nullptr;
    std::string javaCls = getSerializableObjectJavaClassFromNative(native);
    jclass cls =
            env->FindClass(javaCls.c_str());
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID stackInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == stackInit) return NULL;

    auto stackManager =
            new SerializableObject::Retainer<Stack>(native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(stackManager));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.Stack";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an int argument
    jobject newObj = env->NewObject(cls, stackInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

inline jobject
clipFromNative(JNIEnv *env, OTIO_NS::Clip *native) {
    if (native == nullptr)return nullptr;
    std::string javaCls = getSerializableObjectJavaClassFromNative(native);
    jclass cls =
            env->FindClass(javaCls.c_str());
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID clipInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == clipInit) return NULL;

    auto clipManager =
            new SerializableObject::Retainer<Clip>(native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(clipManager));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.Clip";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, clipInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

inline jobject
gapFromNative(JNIEnv *env, OTIO_NS::Gap *native) {
    if (native == nullptr)return nullptr;
    std::string javaCls = getSerializableObjectJavaClassFromNative(native);
    jclass cls =
            env->FindClass(javaCls.c_str());
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID gapInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == gapInit) return NULL;

    auto gapManager =
            new SerializableObject::Retainer<Gap>(native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(gapManager));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.Gap";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, gapInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

inline jobject
trackFromNative(JNIEnv *env, OTIO_NS::Track *native) {
    if (native == nullptr)return nullptr;
    std::string javaCls = getSerializableObjectJavaClassFromNative(native);
    jclass cls =
            env->FindClass(javaCls.c_str());
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID trackInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == trackInit) return NULL;

    auto trackManager =
            new SerializableObject::Retainer<Track>(native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(trackManager));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.Track";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, trackInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

inline jobject
transitionFromNative(JNIEnv *env, OTIO_NS::Transition *native) {
    if (native == nullptr)return nullptr;
    std::string javaCls = getSerializableObjectJavaClassFromNative(native);
    jclass cls =
            env->FindClass(javaCls.c_str());
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID transitionInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == transitionInit) return NULL;

    auto transitionManager =
            new SerializableObject::Retainer<Transition>(native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(transitionManager));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.Transition";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, transitionInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

inline jobject
itemFromNative(JNIEnv *env, OTIO_NS::Item *native) {
    if (native == nullptr)return nullptr;
    std::string javaCls = getSerializableObjectJavaClassFromNative(native);
    jclass cls =
            env->FindClass(javaCls.c_str());
    if (cls == NULL) return NULL;

    // Get the Method ID of the constructor which takes an otioNative
    jmethodID itemInit = env->GetMethodID(cls, "<init>", "(Lio/opentimeline/OTIONative;)V");
    if (NULL == itemInit) return NULL;

    auto itemManager =
            new SerializableObject::Retainer<Item>(native);
    jclass otioNativeClass = env->FindClass("io/opentimeline/OTIONative");
    jfieldID classNameID =
            env->GetFieldID(otioNativeClass, "className", "Ljava/lang/String;");
    jmethodID otioNativeInit =
            env->GetMethodID(otioNativeClass, "<init>", "(J)V");
    jobject otioNative = env->NewObject(
            otioNativeClass,
            otioNativeInit,
            reinterpret_cast<jlong>(itemManager));
    std::string classNameStr =
            "io.opentimeline.opentimelineio.Item";
    jstring className = env->NewStringUTF(classNameStr.c_str());
    env->SetObjectField(otioNative, classNameID, className);

    // Call back constructor to allocate a new instance, with an otioNative argument
    jobject newObj = env->NewObject(cls, itemInit, otioNative);
    registerObjectToOTIOFactory(env, newObj);
    return newObj;
}

inline jobjectArray
serializableObjectRetainerVectorToArray(
        JNIEnv *env,
        std::vector<
                OTIO_NS::SerializableObject::Retainer<OTIO_NS::SerializableObject>> &v) {
    jclass serializableObjectClass = env->FindClass(
            "io/opentimeline/opentimelineio/SerializableObject");
    jobjectArray result =
            env->NewObjectArray((jsize)v.size(), serializableObjectClass, nullptr);
    for (int i = 0; i < v.size(); i++) {
        auto newObj = serializableObjectFromNative(env, v[i]);
        registerObjectToOTIOFactory(env, newObj);
        env->SetObjectArrayElement(
                result, i, newObj);
    }
    return result;
}

inline jobjectArray
effectRetainerVectorToArray(
        JNIEnv *env,
        std::vector<OTIO_NS::SerializableObject::Retainer<OTIO_NS::Effect>> &v) {
    jclass effectClass = env->FindClass(
            "io/opentimeline/opentimelineio/Effect");
    jobjectArray result =
            env->NewObjectArray((jsize)v.size(), effectClass, nullptr);
    for (int i = 0; i < v.size(); i++) {
        auto newObj = effectFromNative(env, v[i]);
        registerObjectToOTIOFactory(env, newObj);
        env->SetObjectArrayElement(
                result, i, newObj);
    }
    return result;
}

inline jobjectArray
markerRetainerVectorToArray(
        JNIEnv *env,
        std::vector<OTIO_NS::SerializableObject::Retainer<OTIO_NS::Marker>> &v) {
    jclass markerClass = env->FindClass(
            "io/opentimeline/opentimelineio/Marker");
    jobjectArray result =
            env->NewObjectArray((jsize)v.size(), markerClass, nullptr);
    for (int i = 0; i < v.size(); i++) {
        auto newObj = markerFromNative(env, v[i]);
        registerObjectToOTIOFactory(env, newObj);
        env->SetObjectArrayElement(
                result, i, newObj);
    }
    return result;
}

inline jobjectArray
composableRetainerVectorToArray(
        JNIEnv *env,
        std::vector<OTIO_NS::SerializableObject::Retainer<OTIO_NS::Composable>> &v) {
    jclass composableClass = env->FindClass(
            "io/opentimeline/opentimelineio/Composable");
    jobjectArray result =
            env->NewObjectArray((jsize)v.size(), composableClass, nullptr);
    for (int i = 0; i < v.size(); i++) {
        auto newObj = composableFromNative(env, v[i]);
        registerObjectToOTIOFactory(env, newObj);
        env->SetObjectArrayElement(
                result, i, newObj);
    }
    return result;
}

inline jobjectArray
clipRetainerVectorToArray(
        JNIEnv *env,
        std::vector<OTIO_NS::SerializableObject::Retainer<OTIO_NS::Clip>> &v) {
    jclass clipClass = env->FindClass(
            "io/opentimeline/opentimelineio/Clip");
    jobjectArray result =
            env->NewObjectArray((jsize)v.size(), clipClass, nullptr);
    for (int i = 0; i < v.size(); i++) {
        auto newObj = clipFromNative(env, v[i]);
        registerObjectToOTIOFactory(env, newObj);
        env->SetObjectArrayElement(
                result, i, newObj);
    }
    return result;
}


inline jobjectArray
gapRetainerVectorToArray(
        JNIEnv *env,
        std::vector<OTIO_NS::SerializableObject::Retainer<OTIO_NS::Gap>> &v) {
    jclass gapClass = env->FindClass(
            "io/opentimeline/opentimelineio/Gap");
    jobjectArray result =
            env->NewObjectArray((jsize)v.size(), gapClass, nullptr);
    for (int i = 0; i < v.size(); i++) {
        auto newObj = gapFromNative(env, v[i]);
        registerObjectToOTIOFactory(env, newObj);
        env->SetObjectArrayElement(
                result, i, newObj);
    }
    return result;
}


inline jobjectArray
trackRetainerVectorToArray(
        JNIEnv *env,
        std::vector<OTIO_NS::SerializableObject::Retainer<OTIO_NS::Track>> &v) {
    jclass trackClass = env->FindClass(
            "io/opentimeline/opentimelineio/Track");
    jobjectArray result =
            env->NewObjectArray((jsize)v.size(), trackClass, nullptr);
    for (int i = 0; i < v.size(); i++) {
        auto newObj = trackFromNative(env, v[i]);
        registerObjectToOTIOFactory(env, newObj);
        env->SetObjectArrayElement(
                result, i, newObj);
    }
    return result;
}

inline jobjectArray
stackRetainerVectorToArray(
        JNIEnv *env,
        std::vector<OTIO_NS::SerializableObject::Retainer<OTIO_NS::Stack>> &v) {
    jclass stackClass = env->FindClass(
            "io/opentimeline/opentimelineio/Stack");
    jobjectArray result =
            env->NewObjectArray((jsize)v.size(), stackClass, nullptr);
    for (int i = 0; i < v.size(); i++) {
        auto newObj = stackFromNative(env, v[i]);
        registerObjectToOTIOFactory(env, newObj);
        env->SetObjectArrayElement(
                result, i, newObj);
    }
    return result;
}

inline jobjectArray
transitionRetainerVectorToArray(
        JNIEnv *env,
        std::vector<OTIO_NS::SerializableObject::Retainer<OTIO_NS::Transition>> &v) {
    jclass transitionClass = env->FindClass(
            "io/opentimeline/opentimelineio/Transition");
    jobjectArray result =
            env->NewObjectArray((jsize)v.size(), transitionClass, nullptr);
    for (int i = 0; i < v.size(); i++) {
        auto newObj = transitionFromNative(env, v[i]);
        registerObjectToOTIOFactory(env, newObj);
        env->SetObjectArrayElement(
                result, i, newObj);
    }
    return result;
}

inline jobjectArray
compositionRetainerVectorToArray(
        JNIEnv *env,
        std::vector<OTIO_NS::SerializableObject::Retainer<OTIO_NS::Composition>> &v) {
    jclass compositionClass = env->FindClass(
            "io/opentimeline/opentimelineio/Composition");
    jobjectArray result =
            env->NewObjectArray((jsize)v.size(), compositionClass, nullptr);
    for (int i = 0; i < v.size(); i++) {
        auto newObj = compositionFromNative(env, v[i]);
        registerObjectToOTIOFactory(env, newObj);
        env->SetObjectArrayElement(
                result, i, newObj);
    }
    return result;
}

inline jobjectArray
itemRetainerVectorToArray(
        JNIEnv *env,
        std::vector<OTIO_NS::SerializableObject::Retainer<OTIO_NS::Item>> &v) {
    jclass itemClass = env->FindClass(
            "io/opentimeline/opentimelineio/Item");
    jobjectArray result =
            env->NewObjectArray((jsize)v.size(), itemClass, nullptr);
    for (int i = 0; i < v.size(); i++) {
        auto newObj = itemFromNative(env, v[i]);
        registerObjectToOTIOFactory(env, newObj);
        env->SetObjectArrayElement(
                result, i, newObj);
    }
    return result;
}

inline jobjectArray
trackVectorToArray(JNIEnv *env, std::vector<OTIO_NS::Track *> &v) {
    jclass trackClass = env->FindClass("io/opentimeline/opentimelineio/Track");
    jobjectArray result = env->NewObjectArray((jsize)v.size(), trackClass, nullptr);
    for (int i = 0; i < v.size(); i++) {
        auto newObj = trackFromNative(env, v[i]);
        registerObjectToOTIOFactory(env, newObj);
        env->SetObjectArrayElement(result, i, newObj);
    }
    return result;
}

inline opentime::RationalTime
rationalTimeFromJObject(JNIEnv *env, jobject rtObject) {
    jclass rtClass = env->FindClass("io/opentimeline/opentime/RationalTime");
    jmethodID getValue = env->GetMethodID(rtClass, "getValue", "()D");
    jmethodID getRate = env->GetMethodID(rtClass, "getRate", "()D");
    double value = env->CallDoubleMethod(rtObject, getValue);
    double rate = env->CallDoubleMethod(rtObject, getRate);
    opentime::RationalTime rt(value, rate);
    return rt;
}

inline opentime::TimeRange
timeRangeFromJObject(JNIEnv *env, jobject trObject) {
    jclass trClass = env->FindClass("io/opentimeline/opentime/TimeRange");
    jmethodID getStartTime = env->GetMethodID(
            trClass, "getStartTime", "()Lio/opentimeline/opentime/RationalTime;");
    jmethodID getDuration = env->GetMethodID(
            trClass, "getDuration", "()Lio/opentimeline/opentime/RationalTime;");
    jobject startTime = env->CallObjectMethod(trObject, getStartTime);
    jobject duration = env->CallObjectMethod(trObject, getDuration);

    jclass rtClass = env->FindClass("io/opentimeline/opentime/RationalTime");
    jmethodID getValue = env->GetMethodID(rtClass, "getValue", "()D");
    jmethodID getRate = env->GetMethodID(rtClass, "getRate", "()D");

    double startTimeValue = env->CallDoubleMethod(startTime, getValue);
    double startTimeRate = env->CallDoubleMethod(startTime, getRate);
    double durationValue = env->CallDoubleMethod(duration, getValue);
    double durationRate = env->CallDoubleMethod(duration, getRate);

    opentime::TimeRange tr(
            opentime::RationalTime(startTimeValue, startTimeRate),
            opentime::RationalTime(durationValue, durationRate));

    return tr;
}

inline opentime::TimeTransform
timeTransformFromJObject(JNIEnv *env, jobject txObject) {
    jclass trClass = env->FindClass("io/opentimeline/opentime/TimeTransform");
    jmethodID getOffset = env->GetMethodID(
            trClass, "getOffset", "()Lio/opentimeline/opentime/RationalTime;");
    jmethodID getScale = env->GetMethodID(trClass, "getScale", "()D");
    jmethodID getRate = env->GetMethodID(trClass, "getRate", "()D");
    jobject offset = env->CallObjectMethod(txObject, getOffset);
    double scale = env->CallDoubleMethod(txObject, getScale);
    double rate = env->CallDoubleMethod(txObject, getRate);

    jclass rtClass = env->FindClass("io/opentimeline/opentime/RationalTime");
    jmethodID getRationalTimeValue =
            env->GetMethodID(rtClass, "getValue", "()D");
    jmethodID getRationalTimeRate = env->GetMethodID(rtClass, "getRate", "()D");

    double offsetValue = env->CallDoubleMethod(offset, getRationalTimeValue);
    double offsetRate = env->CallDoubleMethod(offset, getRationalTimeRate);

    opentime::TimeTransform timeTransform(
            opentime::RationalTime(offsetValue, offsetRate), scale, rate);
    return timeTransform;
}

inline jobject
rationalTimeToJObject(JNIEnv *env, opentime::RationalTime rationalTime) {
    jclass rtClass = env->FindClass("io/opentimeline/opentime/RationalTime");
    jmethodID rtInit = env->GetMethodID(rtClass, "<init>", "(DD)V");
    jobject rt = env->NewObject(
            rtClass, rtInit, rationalTime.value(), rationalTime.rate());
    return rt;
}

inline jobject
timeRangeToJObject(JNIEnv *env, opentime::TimeRange timeRange) {
    jclass trClass = env->FindClass("io/opentimeline/opentime/TimeRange");
    jmethodID trInit = env->GetMethodID(
            trClass,
            "<init>",
            "(Lio/opentimeline/opentime/RationalTime;Lio/opentimeline/opentime/RationalTime;)V");
    jobject startTime = rationalTimeToJObject(env, timeRange.start_time());
    jobject duration = rationalTimeToJObject(env, timeRange.duration());
    jobject tr = env->NewObject(trClass, trInit, startTime, duration);
    return tr;
}

inline jobject
timeTransformToJObject(JNIEnv *env, opentime::TimeTransform timeTransform) {
    jclass txClass = env->FindClass("io/opentimeline/opentime/TimeTransform");
    jmethodID txInit = env->GetMethodID(
            txClass, "<init>", "(Lio/opentimeline/opentime/RationalTime;DD)V");
    jobject offset = rationalTimeToJObject(env, timeTransform.offset());
    jobject tx = env->NewObject(
            txClass, txInit, offset, timeTransform.scale(), timeTransform.rate());
    return tx;
}

template<typename T>
inline jobjectArray
getChildrenIfResult(std::string clsNameString,
               JNIEnv *env,
               T *baseClass,
               OTIO_NS::ErrorStatus errorStatus,
               nonstd::optional_lite::optional<TimeRange> searchRange,
               jboolean shallowSearch) {
    static std::unordered_map<std::string, std::function<jobjectArray()>> childrenIf_dispatch_table;
    (childrenIf_dispatch_table)["io.opentimeline.opentimelineio.Clip"] = [&](){
        auto result = baseClass->template children_if<Clip>(&errorStatus, searchRange, shallowSearch);
        return clipRetainerVectorToArray(env,*(new std::vector<SerializableObject::Retainer<Clip>>(result)));
    };
    (childrenIf_dispatch_table)["io.opentimeline.opentimelineio.Composable"] = [&](){
        auto result = baseClass->template children_if<Composable>(&errorStatus, searchRange, shallowSearch);
        return composableRetainerVectorToArray(env,*(new std::vector<SerializableObject::Retainer<Composable>>(result)));
    };
    (childrenIf_dispatch_table)["io.opentimeline.opentimelineio.Composition"] = [&](){
        auto result = baseClass->template children_if<Composition>(&errorStatus, searchRange, shallowSearch);
        return compositionRetainerVectorToArray(env,*(new std::vector<SerializableObject::Retainer<Composition>>(result)));
    };
    (childrenIf_dispatch_table)["io.opentimeline.opentimelineio.Gap"] = [&](){
        auto result = baseClass->template children_if<Gap>(&errorStatus, searchRange, shallowSearch);
        return gapRetainerVectorToArray(env,*(new std::vector<SerializableObject::Retainer<Gap>>(result)));
    };
    (childrenIf_dispatch_table)["io.opentimeline.opentimelineio.Item"] = [&](){
        auto result = baseClass->template children_if<Item>(&errorStatus, searchRange, shallowSearch);
        return itemRetainerVectorToArray(env,*(new std::vector<SerializableObject::Retainer<Item>>(result)));
    };
    (childrenIf_dispatch_table)["io.opentimeline.opentimelineio.Stack"] = [&](){
        auto result = baseClass->template children_if<Stack>(&errorStatus, searchRange, shallowSearch);
        return stackRetainerVectorToArray(env,*(new std::vector<SerializableObject::Retainer<Stack>>(result)));
    };
    (childrenIf_dispatch_table)["io.opentimeline.opentimelineio.Track"] = [&](){
        auto result = baseClass->template children_if<Track>(&errorStatus, searchRange, shallowSearch);
        return trackRetainerVectorToArray(env,*(new std::vector<SerializableObject::Retainer<Track>>(result)));
    };
    (childrenIf_dispatch_table)["io.opentimeline.opentimelineio.Transition"] = [&](){
        auto result = baseClass->template children_if<Transition>(&errorStatus, searchRange, shallowSearch);
        return transitionRetainerVectorToArray(env,*(new std::vector<SerializableObject::Retainer<Transition>>(result)));
    };
    return (childrenIf_dispatch_table)[clsNameString]();
}

template<typename T>
inline jobjectArray
childrenIfWrapperUtil(JNIEnv *env, jobject thisObj, jclass descendedFromCLass, jobject searchRangeTimeRangeOptional, jboolean shallowSearch) {
    auto thisHandle =
            getHandle<SerializableObject::Retainer<T>>(env, thisObj);
    auto baseClass = thisHandle->value;
    jclass cls = env->GetObjectClass(descendedFromCLass);
    jmethodID getNameID = env->GetMethodID(cls, "getName", "()Ljava/lang/String;");
    auto clsName = (jstring)env->CallObjectMethod(descendedFromCLass, getNameID);
    const char* clsNameString = env->GetStringUTFChars(clsName, NULL);
    auto errorStatus = OTIO_NS::ErrorStatus();

    jclass optionalClass = env->GetObjectClass(searchRangeTimeRangeOptional);
    jmethodID getMethodID = env->GetMethodID(optionalClass, "get", "()Ljava/lang/Object;");
    jmethodID isPresentID = env->GetMethodID(optionalClass, "isPresent", "()Z");
    jboolean ifPresent = env->CallBooleanMethod(searchRangeTimeRangeOptional, isPresentID);
    optional<TimeRange> searchRange = nullopt;
    if (ifPresent){
        jobject searchRangeJObject = env->CallObjectMethod(searchRangeTimeRangeOptional, getMethodID);
        searchRange = timeRangeFromJObject(env, searchRangeJObject);

    }
    jobjectArray descendedFromClassChildren = getChildrenIfResult<T>(clsNameString, env, baseClass, errorStatus, searchRange, shallowSearch);
    processOTIOErrorStatus(env, errorStatus);
    env->ReleaseStringUTFChars(clsName, clsNameString);
    return descendedFromClassChildren;
}

template <typename T>
inline jobjectArray
getClipIfResult(JNIEnv *env, jobject thisObj, jobject searchRangeTimeRangeOptional, jboolean shallowSearch){
    auto thisHandle =
            getHandle<SerializableObject::Retainer<T>>(env, thisObj);
    auto baseClass = thisHandle->value;
    auto errorStatus = OTIO_NS::ErrorStatus();
    jclass optionalClass = env->GetObjectClass(searchRangeTimeRangeOptional);
    jmethodID getMethodID = env->GetMethodID(optionalClass, "get", "()Ljava/lang/Object;");
    jmethodID isPresentID = env->GetMethodID(optionalClass, "isPresent", "()Z");
    jboolean ifPresent = env->CallBooleanMethod(searchRangeTimeRangeOptional, isPresentID);
    optional<TimeRange> searchRange = nullopt;
    if (ifPresent){
        jobject searchRangeJObject = env->CallObjectMethod(searchRangeTimeRangeOptional, getMethodID);
        searchRange = timeRangeFromJObject(env, searchRangeJObject);

    }
    auto result = baseClass->clip_if(&errorStatus, searchRange, shallowSearch);
    processOTIOErrorStatus(env, errorStatus);
    return clipRetainerVectorToArray(env, *(new std::vector<SerializableObject::Retainer<Clip>>(result)));
}

#endif