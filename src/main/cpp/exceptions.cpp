#include "exceptions.h"
#include "utilities.h"

jint processOpenTimeErrorStatus(JNIEnv *env, opentime::ErrorStatus &errorStatus) {
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

jint processOTIOErrorStatus(JNIEnv *env, OTIO_NS::ErrorStatus &errorStatus) {
    std::string otio_msg = "An OpenTimelineIO call failed with: ";
    switch (errorStatus.outcome) {
        case OTIO_NS::ErrorStatus::Outcome::NOT_IMPLEMENTED: {
            const char *className = "java/lang/UnsupportedOperationException";
            jclass exClass = env->FindClass(className);
            otio_msg = otio_msg + errorStatus.full_description;
            return env->ThrowNew(exClass, otio_msg.c_str());
        }
        case OTIO_NS::ErrorStatus::Outcome::TYPE_MISMATCH: {
            const char *className = "java/lang/IllegalArgumentException";
            jclass exClass = env->FindClass(className);
            otio_msg = otio_msg + errorStatus.full_description;
            return env->ThrowNew(exClass, otio_msg.c_str());
        }
        case OTIO_NS::ErrorStatus::Outcome::FILE_OPEN_FAILED:
        case OTIO_NS::ErrorStatus::Outcome::FILE_WRITE_FAILED: {
            const char *className = "java/io/IOException";
            jclass exClass = env->FindClass(className);
            otio_msg = otio_msg + errorStatus.full_description;
            return env->ThrowNew(exClass, otio_msg.c_str());
        }
        case OTIO_NS::ErrorStatus::Outcome::INTERNAL_ERROR:
        case OTIO_NS::ErrorStatus::Outcome::KEY_NOT_FOUND: {
            const char *className = "java/lang/RuntimeException";
            jclass exClass = env->FindClass(className);
            otio_msg = otio_msg + errorStatus.full_description;
            return env->ThrowNew(exClass, otio_msg.c_str());
        }
        case OTIO_NS::ErrorStatus::Outcome::ILLEGAL_INDEX: {
            const char *className = "java/lang/IndexOutOfBoundsException";
            jclass exClass = env->FindClass(className);
            otio_msg = otio_msg + errorStatus.full_description;
            return env->ThrowNew(exClass, otio_msg.c_str());
        }
        case OTIO_NS::ErrorStatus::Outcome::MALFORMED_SCHEMA: {
            const char *className = "io/opentimeline/opentimelineio/exception/MalformedSchemaException";
            jclass exClass = env->FindClass(className);
            size_t firstIndex = errorStatus.details.find_first_of('\'');
            size_t lastIndex = errorStatus.details.find_last_of('\'');
            std::string version = errorStatus.details.substr(firstIndex + 1, lastIndex - firstIndex - 1);
            return env->ThrowNew(exClass, version.c_str());
        }
        case OTIO_NS::ErrorStatus::Outcome::JSON_PARSE_ERROR: {
            const char *className = "io/opentimeline/opentimelineio/exception/JSONParseException";
            jclass exClass = env->FindClass(className);
            return env->ThrowNew(exClass, errorStatus.full_description.c_str());
        }
        case OTIO_NS::ErrorStatus::Outcome::CANNOT_COMPUTE_AVAILABLE_RANGE: {
            const char *className = "io/opentimeline/opentimelineio/exception/CannotComputeAvailableRangeException";
            jclass exClass = env->FindClass(className);
            return env->ThrowNew(exClass, errorStatus.full_description.c_str());
        }
        case OTIO_NS::ErrorStatus::Outcome::INVALID_TIME_RANGE: {
            const char *className = "io/opentimeline/opentimelineio/exception/InvalidTimeRangeException";
            jclass exClass = env->FindClass(className);
            return env->ThrowNew(exClass, errorStatus.full_description.c_str());
        }
        case OTIO_NS::ErrorStatus::Outcome::OBJECT_WITHOUT_DURATION: {
            const char *className = "io/opentimeline/opentimelineio/exception/ObjectWithoutDurationException";
            jclass exClass = env->FindClass(className);
            jmethodID exceptionCtor = env->GetMethodID(exClass, "<init>",
                                                       "(Ljava/lang/String;Lio/opentimeline/opentimelineio/SerializableObject;)V");
            jobject exObject = env->NewObject(exClass, exceptionCtor, errorStatus.details.c_str(),
                                              serializableObjectFromNative(env,
                                                                           const_cast<SerializableObject *>(errorStatus.object_details)));
            return env->Throw((jthrowable) exObject);
        }
        case OTIO_NS::ErrorStatus::Outcome::CANNOT_TRIM_TRANSITION: {
            const char *className = "io/opentimeline/opentimelineio/exception/TransitionTrimException";
            jclass exClass = env->FindClass(className);
            return env->ThrowNew(exClass, errorStatus.full_description.c_str());
        }
        case OTIO_NS::ErrorStatus::Outcome::CHILD_ALREADY_PARENTED: {
            const char *className = "io/opentimeline/opentimelineio/exception/ChildAlreadyParentedException";
            jclass exClass = env->FindClass(className);
            return env->ThrowNew(exClass, errorStatus.full_description.c_str());
        }
        case OTIO_NS::ErrorStatus::Outcome::NOT_A_CHILD_OF:
        case OTIO_NS::ErrorStatus::Outcome::NOT_A_CHILD:
        case OTIO_NS::ErrorStatus::Outcome::NOT_DESCENDED_FROM: {
            const char *className = "io/opentimeline/opentimelineio/exception/NotAChildException";
            jclass exClass = env->FindClass(className);
            return env->ThrowNew(exClass, errorStatus.full_description.c_str());
        }
        case OTIO_NS::ErrorStatus::Outcome::OK:
            return 0;
        case OTIO_NS::ErrorStatus::Outcome::SCHEMA_ALREADY_REGISTERED:
        case OTIO_NS::ErrorStatus::Outcome::SCHEMA_NOT_REGISTERED:
        case OTIO_NS::ErrorStatus::Outcome::SCHEMA_VERSION_UNSUPPORTED:
        case OTIO_NS::ErrorStatus::Outcome::NOT_AN_ITEM:
        case OTIO_NS::ErrorStatus::Outcome::UNRESOLVED_OBJECT_REFERENCE:
        case OTIO_NS::ErrorStatus::Outcome::DUPLICATE_OBJECT_REFERENCE: {
            const char *className = "io/opentimeline/opentimelineio/exception/OpenTimelineIOException";
            jclass exClass = env->FindClass(className);
            otio_msg = otio_msg + errorStatus.full_description;
            return env->ThrowNew(exClass, otio_msg.c_str());
        }
        default: {
            const char *className = "io/opentimeline/opentimelineio/exception/OpenTimelineIOException";
            jclass exClass = env->FindClass(className);
            otio_msg = otio_msg + "an unknown Exception.";
            return env->ThrowNew(exClass, otio_msg.c_str());
        }
    }
}