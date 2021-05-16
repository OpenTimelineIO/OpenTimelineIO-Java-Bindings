// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio.exception;

import io.opentimeline.opentimelineio.SerializableObject;

public class NotAChildException extends OpenTimelineIOException {
    public NotAChildException(String message) {
        super("An OpenTimelineIO call failed with: " + message);
    }
}