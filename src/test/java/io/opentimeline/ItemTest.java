// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline;

import io.opentimeline.opentime.RationalTime;
import io.opentimeline.opentime.TimeRange;
import io.opentimeline.opentimelineio.*;
import io.opentimeline.opentimelineio.exception.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    public void testConstructor() throws OpenTimelineIOException {
        TimeRange tr = new TimeRange(
                new RationalTime(0, 1),
                new RationalTime(10, 1));
        Item it = new Item.ItemBuilder()
                .setName("foo")
                .setSourceRange(tr)
                .build();
        assertEquals(it.getSourceRange(), tr);
        assertEquals(it.getName(), "foo");
        String encoded = it.toJSONString();
        Item decoded = (Item) SerializableObject.fromJSONString(encoded);
        assertEquals(it, decoded);
        try {
            it.close();
            decoded.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStr() {
        TimeRange tr = new TimeRange(
                new RationalTime(0, 1),
                new RationalTime(10, 1));
        Item it = new Item.ItemBuilder()
                .setName("foo")
                .setSourceRange(tr)
                .build();
        assertEquals(it.toString(),
                "io.opentimeline.opentimelineio.Item(" +
                        "name=foo, " +
                        "sourceRange=io.opentimeline.opentime.TimeRange(" +
                        "startTime=io.opentimeline.opentime.RationalTime(value=0.0, rate=1.0), " +
                        "duration=io.opentimeline.opentime.RationalTime(value=10.0, rate=1.0)), " +
                        "effects=[], " +
                        "markers=[], " +
                        "enabled=true, " +
                        "metadata=io.opentimeline.opentimelineio.AnyDictionary{})");
        try {
            it.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCopyArguments() {
        // make sure all the arguments are copied and not referenced
        TimeRange tr = new TimeRange(
                new RationalTime(0, 1),
                new RationalTime(10, 1));
        String name = "foobar";
        ArrayList<Effect> effects = new ArrayList<>();
        ArrayList<Marker> markers = new ArrayList<>();
        AnyDictionary metadata = new AnyDictionary();
        Item it = new Item.ItemBuilder()
                .setName(name)
                .setSourceRange(tr)
                .setEffects(effects)
                .setMarkers(markers)
                .setMetadata(metadata)
                .build();
        name = "foobaz";
        assertNotEquals(name, it.getName());

        tr = new TimeRange(
                new RationalTime(1, tr.getStartTime().getRate()),
                tr.getDuration());
        assertNotEquals(it.getSourceRange().getStartTime(), tr.getStartTime());
        markers.add(new Marker.MarkerBuilder().build());
        assertNotEquals(markers, it.getMarkers());
        metadata.put("foo", new Any("bar"));
        assertNotEquals(metadata, it.getMetadata());
        try {
            it.close();
            metadata.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDuration() throws CannotComputeAvailableRangeException {
        TimeRange tr = new TimeRange(
                new RationalTime(0, 1),
                new RationalTime(10, 1));
        Item it = new Item.ItemBuilder()
                .setSourceRange(tr)
                .build();
        assertEquals(it.getDuration(), tr.getDuration());
        try {
            it.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAvailableRange() {
        Item it = new Item.ItemBuilder().build();
        Exception exception = assertThrows(UnsupportedOperationException.class, it::getAvailableRange);
        assertEquals(exception.getMessage(), "method not implemented for this class");
        try {
            it.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDurationAndSourceRange() throws CannotComputeAvailableRangeException {
        Item it = new Item.ItemBuilder().build();
        Exception exception = assertThrows(UnsupportedOperationException.class, it::getDuration);
        assertEquals(exception.getMessage(), "method not implemented for this class");

        assertNull(it.getSourceRange());
        TimeRange tr = new TimeRange(
                new RationalTime(1, 1),
                new RationalTime(10, 1));
        Item it2 = new Item.ItemBuilder()
                .setSourceRange(tr)
                .build();
        assertEquals(tr, it2.getSourceRange());
        assertEquals(it2.getDuration(), tr.getDuration());
        try {
            it.close();
            it2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTrimmedRange() throws CannotComputeAvailableRangeException {
        Item it = new Item.ItemBuilder().build();
        Exception exception = assertThrows(UnsupportedOperationException.class, it::getTrimmedRange);
        assertEquals(exception.getMessage(), "method not implemented for this class");

        TimeRange tr = new TimeRange(
                new RationalTime(1, 1),
                new RationalTime(10, 1));
        Item it2 = new Item.ItemBuilder()
                .setSourceRange(tr)
                .build();
        assertEquals(it2.getTrimmedRange(), tr);
        try {
            it.close();
            it2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerialize() throws OpenTimelineIOException {
        TimeRange tr = new TimeRange(
                new RationalTime(0, 1),
                new RationalTime(10, 1));
        Item it = new Item.ItemBuilder()
                .setSourceRange(tr)
                .build();
        String encoded = it.toJSONString();
        Item decoded = (Item) SerializableObject.fromJSONString(encoded);
        assertEquals(it, decoded);
        try {
            it.close();
            decoded.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMetadata() throws OpenTimelineIOException {
        TimeRange tr = new TimeRange(new RationalTime(10, 1));
        AnyDictionary metadata = new AnyDictionary();
        metadata.put("foo", new Any("bar"));
        Item it = new Item.ItemBuilder()
                .setSourceRange(tr)
                .setMetadata(metadata)
                .build();
        String encoded = it.toJSONString();
        Item decoded = (Item) SerializableObject.fromJSONString(encoded);
        assertEquals(it, decoded);
        assertEquals(it.getMetadata().size(),
                decoded.getMetadata().size());
        assertEquals(it.getMetadata().get("foo").safelyCastString(),
                decoded.getMetadata().get("foo").safelyCastString());
        try {
            it.close();
            metadata.close();
            decoded.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEnabled() {
        TimeRange tr = new TimeRange.TimeRangeBuilder()
                .setDuration(new RationalTime(10, 1))
                .build();
        Item item = new Item.ItemBuilder()
                .setSourceRange(tr)
                .build();
        assertTrue(item.isEnabled());

        item.setEnabled(false);
        assertFalse(item.isEnabled());
    }

    @Test
    public void testAddEffect() throws OpenTimelineIOException {
        TimeRange tr = new TimeRange(new RationalTime(10, 1));
        AnyDictionary metadata = new AnyDictionary();
        metadata.put("amount", new Any("100"));
        Effect effect = new Effect.EffectBuilder()
                .setEffectName("blur")
                .setMetadata(metadata)
                .build();
        ArrayList<Effect> effects = new ArrayList<>();
        effects.add(effect);
        Item it = new Item.ItemBuilder()
                .setSourceRange(tr)
                .setEffects(effects)
                .build();
        String encoded = it.toJSONString();
        Item decoded = (Item) SerializableObject.fromJSONString(encoded);
        assertEquals(it, decoded);
        assertEquals(it.getEffects(), decoded.getEffects());
        try {
            metadata.close();
            it.close();
            effect.close();
            decoded.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddMarker() throws OpenTimelineIOException {
        TimeRange tr = new TimeRange(new RationalTime(10, 1));
        AnyDictionary metadata = new AnyDictionary();
        metadata.put("some stuff to mark", new Any("100"));
        Marker marker = new Marker.MarkerBuilder()
                .setName("test_marker")
                .setMarkedRange(tr)
                .setMetadata(metadata)
                .build();
        ArrayList<Marker> markers = new ArrayList<>();
        markers.add(marker);
        Item it = new Item.ItemBuilder()
                .setSourceRange(tr)
                .setMarkers(markers)
                .build();
        String encoded = it.toJSONString();
        Item decoded = (Item) SerializableObject.fromJSONString(encoded);
        assertEquals(it, decoded);
        assertEquals(it.getMarkers(), decoded.getMarkers());
        try {
            metadata.close();
            it.close();
            marker.close();
            decoded.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVisibleRange() throws ChildAlreadyParentedException, CannotComputeAvailableRangeException, NotAChildException {
        Clip A = new Clip.ClipBuilder()
                .setName("A")
                .setSourceRange(new TimeRange(
                        new RationalTime(1, 30),
                        new RationalTime(50, 30)))
                .build();
        Clip B = new Clip.ClipBuilder()
                .setName("B")
                .setSourceRange(new TimeRange(
                        new RationalTime(100, 30),
                        new RationalTime(50, 30)))
                .build();
        Clip C = new Clip.ClipBuilder()
                .setName("C")
                .setSourceRange(new TimeRange(
                        new RationalTime(50, 30),
                        new RationalTime(50, 30)))
                .build();
        Clip D = new Clip.ClipBuilder()
                .setName("D")
                .setSourceRange(new TimeRange(
                        new RationalTime(1, 30),
                        new RationalTime(50, 30)))
                .build();
        Transition transition1 = new Transition.TransitionBuilder()
                .setInOffset(new RationalTime(7, 30))
                .setOutOffset(new RationalTime(10, 30))
                .build();
        Transition transition2 = new Transition.TransitionBuilder()
                .setInOffset(new RationalTime(17, 30))
                .setOutOffset(new RationalTime(15, 30))
                .build();
        Track track = new Track.TrackBuilder()
                .setName("V1")
                .build();
        assertTrue(track.appendChild(A));
        assertTrue(track.appendChild(transition1));
        assertTrue(track.appendChild(B));
        assertTrue(track.appendChild(transition2));
        assertTrue(track.appendChild(C));
        assertTrue(track.appendChild(D));
        Stack stack = new Stack.StackBuilder()
                .build();
        assertTrue(stack.appendChild(track));
        Timeline timeline = new Timeline.TimelineBuilder().build();
        timeline.setTracks(stack);

        List<Composable> trackChildren = track.getChildren();
        ArrayList<String> clipNames = new ArrayList<>();
        ArrayList<TimeRange> clipTrimmedRanges = new ArrayList<>();
        ArrayList<TimeRange> clipVisibleRanges = new ArrayList<>();
        for (Composable composable : trackChildren) {
            if (composable instanceof Clip) {
                clipNames.add(composable.getName());
                clipTrimmedRanges.add(((Clip) composable).getTrimmedRange());
                clipVisibleRanges.add(((Clip) composable).getVisibleRange());
            }
        }
        assertEquals(clipNames, new ArrayList<>(Arrays.asList("A", "B", "C", "D")));
        assertEquals(clipTrimmedRanges, new ArrayList<>(Arrays.asList(
                new TimeRange(
                        new RationalTime(1, 30),
                        new RationalTime(50, 30)),
                new TimeRange(
                        new RationalTime(100, 30),
                        new RationalTime(50, 30)),
                new TimeRange(
                        new RationalTime(50, 30),
                        new RationalTime(50, 30)),
                new TimeRange(
                        new RationalTime(1, 30),
                        new RationalTime(50, 30))
        )));
        assertEquals(clipVisibleRanges, new ArrayList<>(Arrays.asList(
                new TimeRange(
                        new RationalTime(1, 30),
                        new RationalTime(50 + 10, 30)),
                new TimeRange(
                        new RationalTime(100 - 7, 30),
                        new RationalTime(50 + 15 + 7, 30)),
                new TimeRange(
                        new RationalTime(33, 30),
                        new RationalTime(50 + 17, 30)),
                new TimeRange(
                        new RationalTime(1, 30),
                        new RationalTime(50, 30))
        )));
        try {
            timeline.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
