// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio.exception;

import io.opentimeline.opentimelineio.SerializableObject;

public class ChildAlreadyParentedException extends OpenTimelineIOException {
    public ChildAlreadyParentedException(String message) {
        super(message);
    }
}