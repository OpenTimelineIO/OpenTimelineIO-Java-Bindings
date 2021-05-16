// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio.exception;

public class JSONParseException extends OpenTimelineIOException {
    public JSONParseException(String errorMessage, long lineNumber, long columnNumber) {
        super("An OpenTimelineIO call failed with: JSON parse error on input string: " +
                errorMessage + "(line " + lineNumber + ", column " + columnNumber + ")");
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.errorMessage = errorMessage;
    }

    public String getParseError() {
        return errorMessage;
    }

    public long getColumnNumber() {
        return columnNumber;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    String errorMessage;
    long lineNumber;
    long columnNumber;
}