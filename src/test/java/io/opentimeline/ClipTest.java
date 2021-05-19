// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline;

import io.opentimeline.opentime.RationalTime;
import io.opentimeline.opentime.TimeRange;
import io.opentimeline.opentimelineio.*;
import io.opentimeline.opentimelineio.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClipTest {

    @Test
    public void testConstructor() throws Exception {
        String name = "test";
        RationalTime rt = new RationalTime(5, 24);
        TimeRange tr = new TimeRange(rt, rt);
        ExternalReference mr = new ExternalReference.ExternalReferenceBuilder()
                .setTargetURL("/var/tmp/test.mov")
                .setAvailableRange(new TimeRange(rt, new RationalTime(10, 24)))
                .build();
        Clip clip = new Clip.ClipBuilder()
                .setName(name)
                .setMediaReference(mr)
                .setSourceRange(tr)
                .build();

        assertEquals(clip.getName(), name);
        assertTrue(clip.getSourceRange().equals(tr));
        assertTrue(clip.getMediaReference().isEquivalentTo(mr));

        Any clipAny = new Any(clip);
        Serialization serialization = new Serialization();
        Deserialization deserialization = new Deserialization();
        String encoded = serialization.serializeJSONToString(clipAny);
        Any destination = new Any(new SerializableObject());
        assertTrue(deserialization.deserializeJSONFromString(encoded, destination));
        assertTrue(clip.isEquivalentTo(destination.safelyCastSerializableObject()));

        try {
            clip.close();
            mr.close();
            destination.close();
            clipAny.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStr() {
        Clip clip = new Clip.ClipBuilder()
                .setName("test_clip")
                .build();
        assertEquals(clip.toString(),
                "io.opentimeline.opentimelineio.Clip(" +
                        "name=test_clip, " +
                        "mediaReference=io.opentimeline.opentimelineio.MissingReference(name=, " +
                        "availableRange=null, " +
                        "metadata=io.opentimeline.opentimelineio.AnyDictionary{}), " +
                        "sourceRange=null, " +
                        "metadata=io.opentimeline.opentimelineio.AnyDictionary{})");
        try {
            clip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRanges() throws CannotComputeAvailableRangeException {
        TimeRange tr = new TimeRange(
                new RationalTime(86400, 24),
                new RationalTime(200, 24));

        Clip clip = new Clip.ClipBuilder()
                .setName("test_clip")
                .setMediaReference(
                        new ExternalReference(
                                "var/tmp/foo.mov",
                                tr, new AnyDictionary()))
                .build();

        assertTrue(clip.getDuration().equals(clip.getTrimmedRange().getDuration()));
        assertTrue(clip.getDuration().equals(tr.getDuration()));
        assertTrue(clip.getTrimmedRange().equals(tr));
        assertTrue(clip.getAvailableRange().equals(tr));
        try {
            clip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRefDefault() {
        Clip clip = new Clip.ClipBuilder().build();
        MissingReference missingReference = new MissingReference.MissingReferenceBuilder().build();
        assertTrue(clip.getMediaReference().isEquivalentTo(missingReference));
        ExternalReference externalReference = new ExternalReference.ExternalReferenceBuilder().build();
        clip.setMediaReference(externalReference);
        assertTrue(clip.getMediaReference().isEquivalentTo(externalReference));
        try {
            clip.close();
            missingReference.close();
            externalReference.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
