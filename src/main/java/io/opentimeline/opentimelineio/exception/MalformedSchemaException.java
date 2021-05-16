// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio.exception;

public class MalformedSchemaException extends OpenTimelineIOException {
    public MalformedSchemaException(String schemaVersion) {
        super("An OpenTimelineIO call failed with: Badly formed schema version string " + schemaVersion);
        this.schemaVersion = schemaVersion;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    String schemaVersion;
}