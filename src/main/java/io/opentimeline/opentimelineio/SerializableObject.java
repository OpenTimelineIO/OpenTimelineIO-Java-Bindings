// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio;

import io.opentimeline.OTIONative;
import io.opentimeline.OTIOObject;
import io.opentimeline.opentimelineio.exception.*;

/**
 * Base object for things that can be [de]serialized to/from .otio files.
 */
public class SerializableObject extends OTIOObject {

    public SerializableObject() {
        this.initObject();
    }

    SerializableObject(OTIONative otioNative) {
        this.nativeManager = otioNative;
    }

    private void initObject() {
        if (this.getClass().getCanonicalName().equals("io.opentimeline.opentimelineio.SerializableObject"))
            this.initialize();
        if (this.nativeManager != null)
            this.nativeManager.className = this.getClass().getCanonicalName();
    }

    private native void initialize();

    public native boolean toJSONFile(String fileName) throws Exception;

    public native boolean toJSONFile(String fileName, int indent) throws Exception;

    public native String toJSONString() throws Exception;

    public native String toJSONString(int indent) throws Exception;

    public static native SerializableObject fromJSONFile(String fileName) throws Exception;

    public static native SerializableObject fromJSONString(String input) throws Exception;

    /**
     * Returns true if the contents of self and other match.
     *
     * @param serializableObject other SerializableObject
     * @return true if the contents of both match, otherwise false
     */
    public native boolean isEquivalentTo(SerializableObject serializableObject);

    /**
     * Create a deepcopy of the SerializableObject
     *
     * @return a deepcopy of the object
     */
    public native SerializableObject deepCopy() throws Exception;

    public native AnyDictionary dynamicFields();

    /**
     * In general, SerializableObject will have a known schema
     * but UnknownSchema subclass will redefine this property to be True
     *
     * @return true if schema is unknown, otherwise false
     */
    public native boolean isUnknownSchema();

    public native String schemaName();

    public native int schemaVersion();

    public native int currentRefCount();

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SerializableObject))
            return false;
        return this.isEquivalentTo((SerializableObject) obj);
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "()";
    }
}
