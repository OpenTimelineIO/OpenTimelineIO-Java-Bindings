// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio.exception;

public class JSONParseException extends OpenTimelineIOException {
    public JSONParseException(String errorMessage) {
        super("An OpenTimelineIO call failed with: JSON parse error on input string: " + errorMessage);
    }
}