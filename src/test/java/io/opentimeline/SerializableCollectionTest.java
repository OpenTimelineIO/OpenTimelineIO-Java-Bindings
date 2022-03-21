// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline;

import io.opentimeline.opentime.RationalTime;
import io.opentimeline.opentime.TimeRange;
import io.opentimeline.opentimelineio.*;
import io.opentimeline.opentimelineio.exception.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SerializableCollectionTest {

    Clip clip;
    MissingReference missingReference;
    AnyDictionary metadata;
    SerializableCollection sc;
    ArrayList<SerializableObject> children;

    @BeforeEach
    public void setUp() {
        clip = new Clip.ClipBuilder()
                .setName("testClip")
                .build();
        missingReference = new MissingReference.MissingReferenceBuilder().build();
        children = new ArrayList<>();
        children.add(clip);
        children.add(missingReference);
        metadata = new AnyDictionary();
        metadata.put("foo", new Any("bar"));
        sc = new SerializableCollection.SerializableCollectionBuilder()
                .setName("test")
                .setChildren(children)
                .build();
    }

    @Test
    public void testConstructor() {
        assertEquals(sc.getName(), "test");
        List<SerializableObject> ch = sc.getChildren();
        assertEquals(ch.size(), children.size());
        for (int i = 0; i < ch.size(); i++) {
            SerializableObject child = children.get(i);
            SerializableObject childCompare = ch.get(i);
            assertTrue(child.isEquivalentTo(childCompare));
        }
    }

    @Test
    public void testStr() {
        assertEquals(sc.toString(),
                "io.opentimeline.opentimelineio.SerializableCollection(" +
                        "name=test, " +
                        "children=[" +
                        "io.opentimeline.opentimelineio.Clip(name=testClip, " +
                        "mediaReference=io.opentimeline.opentimelineio.MissingReference(name=, " +
                        "availableRange=null, " +
                        "metadata=io.opentimeline.opentimelineio.AnyDictionary{}), " +
                        "sourceRange=null, " +
                        "metadata=io.opentimeline.opentimelineio.AnyDictionary{}), " +
                        "io.opentimeline.opentimelineio.MissingReference(name=, " +
                        "availableRange=null, " +
                        "metadata=io.opentimeline.opentimelineio.AnyDictionary{})], " +
                        "metadata=io.opentimeline.opentimelineio.AnyDictionary{})");
    }

    @Test
    public void testSerialize() throws OpenTimelineIOException {
        Any scAny = new Any(sc);
        Serialization serialization = new Serialization();
        String encoded = serialization.serializeJSONToString(scAny);
        SerializableObject decoded = SerializableObject.fromJSONString(encoded);
        assertTrue(decoded.isEquivalentTo(sc));
        try {
            scAny.close();
            decoded.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClipIfNullTimeRange() throws Exception{
        try(SerializableCollection sc = new SerializableCollection.SerializableCollectionBuilder().build();)
        {
            assertThrows(NullPointerException.class,
                    ()->{sc.clipIf(null, false);});
        }
    }

    @Test
    public void testClipIfEquality() throws Exception{
        try(
                SerializableCollection sc = new SerializableCollection.SerializableCollectionBuilder().build();
                Track track = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                ExternalReference mr = new ExternalReference.ExternalReferenceBuilder()
                        .setAvailableRange(TimeRange.rangeFromStartEndTime(
                                new RationalTime(0, 24),
                                new RationalTime(50, 24)))
                        .setTargetURL("/var/tmp/test.mov")
                        .build();
                Clip C1 = new Clip.ClipBuilder()
                        .setName("test clip1")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(10, 24)).build())
                        .build();
                Clip C2 = new Clip.ClipBuilder()
                        .setName("test clip2")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(10, 24)).build())
                        .build();
                Clip C3 = new Clip.ClipBuilder()
                        .setName("test clip3")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(10, 24)).build())
                        .build();
                Clip C4 = new Clip.ClipBuilder()
                        .setName("test clip4")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(10, 24)).build())
                        .build();
        )
        {
            assertTrue(track.appendChild(C1));
            assertTrue(track.appendChild(C2));
            assertTrue(track.appendChild(C3));
            assertTrue(track.appendChild(C4));
            List<SerializableObject> children = new ArrayList<SerializableObject>();
            assertTrue(children.add(track));
            sc.setChildren(children);

            //testing full time range
            List<Clip> clip_fullTimeRange = Arrays.asList(C1, C2, C3, C4);
            List<Clip> result_fullTimeRange = sc.clipIf(Optional.empty(), false);
            assertEquals(clip_fullTimeRange.size(), result_fullTimeRange.size());
            for(int i = 0; i < clip_fullTimeRange.size(); i++){
                assertTrue((result_fullTimeRange.get(i)).isEquivalentTo(clip_fullTimeRange.get(i)));
            }

            //testing trimmed time range
            TimeRange search_range = new TimeRange(new RationalTime(0, 24), new RationalTime(20, 24));
            List<Clip> clip_trimmedTimeRange = Arrays.asList(C1, C2);
            List<Clip> result_trimmedTimeRange = sc.clipIf(Optional.of(search_range), false);
            assertEquals(clip_trimmedTimeRange.size(), result_trimmedTimeRange.size());
            for(int i = 0; i < clip_trimmedTimeRange.size(); i++){
                assertTrue((result_trimmedTimeRange.get(i)).isEquivalentTo(clip_trimmedTimeRange.get(i)));
            }
        }

    }

    @AfterEach
    public void tearDown() {
        try {
            clip.getNativeManager().close();
            missingReference.getNativeManager().close();
            metadata.getNativeManager().close();
            sc.getNativeManager().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
