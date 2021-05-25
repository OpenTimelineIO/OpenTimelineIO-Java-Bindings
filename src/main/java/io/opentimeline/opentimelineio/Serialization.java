// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio;

import io.opentimeline.opentimelineio.exception.OpenTimelineIOException;

public class Serialization {

    /**
     * Serialize any OTIO object contained in an Any to a String.
     *
     * @param value       Any to be serialized
     * @param indent      number of spaces to use for indentation in JSON
     * @return serialized OTIO object
     */
    public String serializeJSONToString(Any value, int indent) throws OpenTimelineIOException {
        return serializeJSONToStringNative(value, indent);
    }

    /**
     * Serialize any OTIO object contained in an Any to a String with a default indent of 4.
     *
     * @param value       Any to be serialized
     * @return serialized OTIO object
     */
    public String serializeJSONToString(Any value) throws OpenTimelineIOException {
        return serializeJSONToStringNative(value, 4);
    }

    private native String serializeJSONToStringNative(
            Any value, int indent) throws OpenTimelineIOException ;

    /**
     * Serialize any OTIO object contained in an Any to a file.
     *
     * @param value       Any to be serialized
     * @param fileName    path to file
     * @param indent      number of spaces to use for indentation in JSON
     * @return was the object serialized and was the file created successfully?
     */
    public boolean serializeJSONToFile(
            Any value, String fileName, int indent) throws OpenTimelineIOException {
        return serializeJSONToFileNative(value, fileName, indent);
    }

    /**
     * Serialize any OTIO object contained in an Any to a file with a default indent of 4.
     *
     * @param value       Any to be serialized
     * @param fileName    path to file
     * @return was the object serialized and was the file created successfully?
     */
    public boolean serializeJSONToFile(
            Any value, String fileName) throws OpenTimelineIOException {
        return serializeJSONToFileNative(value, fileName, 4);
    }

    private native boolean serializeJSONToFileNative(
            Any value, String fileName, int indent) throws OpenTimelineIOException ;

}
