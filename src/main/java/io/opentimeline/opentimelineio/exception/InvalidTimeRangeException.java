// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio.exception;

public class InvalidTimeRangeException extends OpenTimelineIOException {
    public InvalidTimeRangeException(String message) {
        super("An OpenTimelineIO call failed with: " + message);
    }
}