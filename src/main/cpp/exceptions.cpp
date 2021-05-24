#include "exceptions.h"
#include "utilities.h"

inline jint throwJavaException(JNIEnv *env, const char *className, std::string &msg) {
    jclass exClass = env->FindClass(className);
    return env->ThrowNew(exClass, msg.c_str());
}

jint processOpenTimeErrorStatus(JNIEnv *env, opentime::ErrorStatus &errorStatus) {
    switch (errorStatus.outcome) {
        case opentime::ErrorStatus::Outcome::INVALID_TIMECODE_RATE:
            return throwJavaException(env, "io/opentimeline/opentime/exception/InvalidTimecodeRateException",
                                      errorStatus.details);
        case opentime::ErrorStatus::Outcome::INVALID_TIMECODE_STRING:
            return throwJavaException(env, "io/opentimeline/opentime/exception/InvalidTimecodeStringException",
                                      errorStatus.details);
        case opentime::ErrorStatus::Outcome::INVALID_TIME_STRING:
            return throwJavaException(env, "io/opentimeline/opentime/exception/InvalidTimestringException",
                                      errorStatus.details);
        case opentime::ErrorStatus::Outcome::INVALID_RATE_FOR_DROP_FRAME_TIMECODE:
            return throwJavaException(env,
                                      "io/opentimeline/opentime/exception/InvalidRateForDropFrameTimecodeException",
                                      errorStatus.details);
        case opentime::ErrorStatus::Outcome::NON_DROPFRAME_RATE:
            return throwJavaException(env, "io/opentimeline/opentime/exception/NonDropframeRateException",
                                      errorStatus.details);
        case opentime::ErrorStatus::Outcome::TIMECODE_RATE_MISMATCH:
            return throwJavaException(env, "io/opentimeline/opentime/exception/TimecodeRateMismatchException",
                                      errorStatus.details);
        case opentime::ErrorStatus::Outcome::NEGATIVE_VALUE: {
            return throwJavaException(env, "io/opentimeline/opentime/exception/NegativeValueException",
                                      errorStatus.details);
        }
        case opentime::ErrorStatus::Outcome::OK:
            return 0;
        default: {
            return throwJavaException(env, "io/opentimeline/opentime/exception/OpentimeException",
                                      errorStatus.details);
        }
    }
}

jint processOTIOErrorStatus(JNIEnv *env, OTIO_NS::ErrorStatus &errorStatus) {
    switch (errorStatus.outcome) {
        case OTIO_NS::ErrorStatus::Outcome::NOT_IMPLEMENTED: {
            return throwJavaException(env, "java/lang/UnsupportedOperationException",
                                      errorStatus.full_description);
        }
        case OTIO_NS::ErrorStatus::Outcome::TYPE_MISMATCH: {
            return throwJavaException(env, "io/opentimeline/opentimelineio/exception/TypeMismatchException",
                                      errorStatus.full_description);
        }
        case OTIO_NS::ErrorStatus::Outcome::FILE_OPEN_FAILED:
        case OTIO_NS::ErrorStatus::Outcome::FILE_WRITE_FAILED: {
            return throwJavaException(env, "java/io/IOException",
                                      errorStatus.full_description);
        }
        case OTIO_NS::ErrorStatus::Outcome::INTERNAL_ERROR:
        case OTIO_NS::ErrorStatus::Outcome::KEY_NOT_FOUND: {
            return throwJavaException(env, "java/lang/RuntimeException",
                                      errorStatus.full_description);
        }
        case OTIO_NS::ErrorStatus::Outcome::ILLEGAL_INDEX: {
            return throwJavaException(env, "java/lang/IndexOutOfBoundsException",
                                      errorStatus.full_description);
        }
        case OTIO_NS::ErrorStatus::Outcome::MALFORMED_SCHEMA: {
            return throwJavaException(env,
                                      "io/opentimeline/opentimelineio/exception/MalformedSchemaException",
                                      errorStatus.full_description);
        }
        case OTIO_NS::ErrorStatus::Outcome::JSON_PARSE_ERROR: {
            return throwJavaException(env, "io/opentimeline/opentimelineio/exception/JSONParseException",
                                      errorStatus.full_description);
        }
        case OTIO_NS::ErrorStatus::Outcome::CANNOT_COMPUTE_AVAILABLE_RANGE: {
            return throwJavaException(env,
                                      "io/opentimeline/opentimelineio/exception/CannotComputeAvailableRangeException",
                                      errorStatus.full_description);
        }
        case OTIO_NS::ErrorStatus::Outcome::INVALID_TIME_RANGE: {
            return throwJavaException(env,
                                      "io/opentimeline/opentimelineio/exception/InvalidTimeRangeException",
                                      errorStatus.full_description);
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
            return throwJavaException(env,
                                      "io/opentimeline/opentimelineio/exception/TransitionTrimException",
                                      errorStatus.full_description);
        }
        case OTIO_NS::ErrorStatus::Outcome::CHILD_ALREADY_PARENTED: {
            return throwJavaException(env,
                                      "io/opentimeline/opentimelineio/exception/ChildAlreadyParentedException",
                                      errorStatus.full_description);
        }
        case OTIO_NS::ErrorStatus::Outcome::NOT_A_CHILD_OF:
        case OTIO_NS::ErrorStatus::Outcome::NOT_A_CHILD:
        case OTIO_NS::ErrorStatus::Outcome::NOT_DESCENDED_FROM: {
            return throwJavaException(env,
                                      "io/opentimeline/opentimelineio/exception/NotAChildException",
                                      errorStatus.full_description);
        }
        case OTIO_NS::ErrorStatus::Outcome::OK:
            return 0;
        case OTIO_NS::ErrorStatus::Outcome::SCHEMA_ALREADY_REGISTERED:
        case OTIO_NS::ErrorStatus::Outcome::SCHEMA_NOT_REGISTERED:
        case OTIO_NS::ErrorStatus::Outcome::SCHEMA_VERSION_UNSUPPORTED:
        case OTIO_NS::ErrorStatus::Outcome::NOT_AN_ITEM:
        case OTIO_NS::ErrorStatus::Outcome::UNRESOLVED_OBJECT_REFERENCE:
        case OTIO_NS::ErrorStatus::Outcome::DUPLICATE_OBJECT_REFERENCE: {
            return throwJavaException(env,
                                      "io/opentimeline/opentimelineio/exception/OpenTimelineIOException",
                                      errorStatus.full_description);
        }
        default: {
            return throwJavaException(env,
                                      "io/opentimeline/opentimelineio/exception/OpenTimelineIOException",
                                      errorStatus.full_description);
        }
    }
}