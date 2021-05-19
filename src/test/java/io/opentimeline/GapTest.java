// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline;

import io.opentimeline.opentimelineio.Gap;
import io.opentimeline.opentimelineio.SerializableObject;
import io.opentimeline.opentimelineio.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GapTest {

    @Test
    public void testSerialize() throws Exception {
        Gap gap = new Gap.GapBuilder().build();
        String encoded = gap.toJSONString();
        Gap decoded = (Gap) SerializableObject.fromJSONString(encoded);
        assertEquals(gap, decoded);
        try {
            gap.close();
            decoded.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStr() {
        Gap gap = new Gap.GapBuilder().build();
        assertEquals(gap.toString(),
                "io.opentimeline.opentimelineio.Gap(" +
                        "name=, " +
                        "sourceRange=io.opentimeline.opentime.TimeRange(" +
                        "startTime=io.opentimeline.opentime.RationalTime(value=0.0, rate=1.0), " +
                        "duration=io.opentimeline.opentime.RationalTime(value=0.0, rate=1.0)), " +
                        "effects=[], " +
                        "markers=[], " +
                        "metadata=io.opentimeline.opentimelineio.AnyDictionary{})");
        try {
            gap.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
