// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio.exception;

import io.opentimeline.opentimelineio.SerializableObject;

public class ObjectWithoutDurationException extends OpenTimelineIOException {
    public ObjectWithoutDurationException(String message, SerializableObject objectDetails) {
        super(message);
        this.objectDetails = objectDetails;
    }

    public SerializableObject getObjectDetails() {
        return objectDetails;
    }

    SerializableObject objectDetails;
}