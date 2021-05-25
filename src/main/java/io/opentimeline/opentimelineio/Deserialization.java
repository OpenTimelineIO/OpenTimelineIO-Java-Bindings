// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio;

import io.opentimeline.opentimelineio.exception.OpenTimelineIOException;

public class Deserialization {

    /**
     * Deserialize an OTIO JSON String and get the result in an Any object.
     *
     * @param input       JSON String
     * @param destination JSON will be deserialized into this object
     * @return was the JSON deserialized successfully?
     */
    public native boolean deserializeJSONFromString(
            String input, Any destination) throws OpenTimelineIOException;

    /**
     * Deserialize an OTIO JSON file and get the result in an Any object.
     *
     * @param fileName    path to JSON file
     * @param destination JSON will be deserialized into this object
     * @return was the JSON deserialized successfully?
     */
    public native boolean deserializeJSONFromFile(
            String fileName, Any destination) throws OpenTimelineIOException;

}
