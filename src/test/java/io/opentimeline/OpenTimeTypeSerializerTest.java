// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline;

import io.opentimeline.opentime.RationalTime;
import io.opentimeline.opentime.TimeRange;
import io.opentimeline.opentime.TimeTransform;
import io.opentimeline.opentimelineio.*;
import io.opentimeline.opentimelineio.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OpenTimeTypeSerializerTest {

    @Test
    public void testSerializeTime() throws OpenTimelineIOException {
        RationalTime rt = new RationalTime(15, 24);
        Any rtAny = new Any(rt);
        Serialization serialization = new Serialization();
        Deserialization deserialization = new Deserialization();
        String encoded = serialization.serializeJSONToString(rtAny);
        Any destination = new Any(new RationalTime());
        assertTrue(deserialization.deserializeJSONFromString(encoded, destination));
        assertTrue(destination.safelyCastRationalTime().equals(rt));
        try {
            destination.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RationalTime rtDur = new RationalTime(10, 20);
        TimeRange tr = new TimeRange(rt, rtDur);
        Any trAny = new Any(tr);
        encoded = serialization.serializeJSONToString(trAny);
        destination = new Any(new TimeRange());
        assertTrue(deserialization.deserializeJSONFromString(encoded, destination));
        assertTrue(destination.safelyCastTimeRange().equals(tr));
        try {
            destination.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TimeTransform tt = new TimeTransform.TimeTransformBuilder()
                .setOffset(rt)
                .setScale(1.5)
                .build();
        Any ttAny = new Any(tt);
        encoded = serialization.serializeJSONToString(ttAny);
        destination = new Any(new TimeTransform());
        assertTrue(deserialization.deserializeJSONFromString(encoded, destination));
        assertTrue(destination.safelyCastTimeTransform().equals(tt));
        try {
            rtAny.close();
            destination.close();
            trAny.close();
            ttAny.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
