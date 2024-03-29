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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CompositionTest {

    @Test
    public void testConstructor() throws ChildAlreadyParentedException {
        Item item = new Item.ItemBuilder().build();
        Composition composition = new Composition.CompositionBuilder()
                .setName("test")
                .build();
        assertTrue(composition.appendChild(item));
        assertEquals(composition.getName(), "test");
        assertTrue(composition.getChildren().get(0).isEquivalentTo(item));
        assertEquals(composition.getCompositionKind(), "Composition");
        try {
            item.close();
            composition.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStr() {
        Composition composition = new Composition.CompositionBuilder()
                .build();
        assertEquals(composition.toString(),
                "io.opentimeline.opentimelineio.Composition(" +
                        "name=, " +
                        "children=[], " +
                        "sourceRange=null, " +
                        "metadata=io.opentimeline.opentimelineio.AnyDictionary{})");
        try {
            composition.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEquality() throws ChildAlreadyParentedException {
        Composition c0 = new Composition.CompositionBuilder().build();
        Composition c00 = new Composition.CompositionBuilder().build();
        assertTrue(c0.isEquivalentTo(c00));

        Item a = new Item.ItemBuilder()
                .setName("A")
                .build();
        Item b = new Item.ItemBuilder()
                .setName("B")
                .build();
        Item c = new Item.ItemBuilder()
                .setName("C")
                .build();

        Composition co1 = new Composition.CompositionBuilder().build();
        assertTrue(co1.appendChild(a));
        assertTrue(co1.appendChild(b));
        assertTrue(co1.appendChild(c));

        Item x = new Item.ItemBuilder()
                .setName("X")
                .build();
        Item y = new Item.ItemBuilder()
                .setName("Y")
                .build();
        Item z = new Item.ItemBuilder()
                .setName("Z")
                .build();

        Composition co2 = new Composition.CompositionBuilder().build();
        assertTrue(co2.appendChild(x));
        assertTrue(co2.appendChild(y));
        assertTrue(co2.appendChild(z));

        Item a2 = new Item.ItemBuilder()
                .setName("A")
                .build();
        Item b2 = new Item.ItemBuilder()
                .setName("B")
                .build();
        Item c2 = new Item.ItemBuilder()
                .setName("C")
                .build();

        Composition co3 = new Composition.CompositionBuilder().build();
        assertTrue(co2.appendChild(a2));
        assertTrue(co2.appendChild(b2));
        assertTrue(co2.appendChild(c2));

        assertNotEquals(co1.getNativeManager().nativeHandle, co2.getNativeManager().nativeHandle);
        assertFalse(co1.isEquivalentTo(co2));

        assertNotEquals(co1.getNativeManager().nativeHandle, co3.getNativeManager().nativeHandle);
        assertFalse(co1.isEquivalentTo(co3));

        try {
            c0.close();
            c00.close();
            c.close();
            b.close();
            a.close();
            co1.close();
            z.close();
            y.close();
            x.close();
            co2.close();
            c2.close();
            b2.close();
            a2.close();
            co3.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReplacingChildren() throws ChildAlreadyParentedException {
        Item a = new Item.ItemBuilder()
                .setName("A")
                .build();
        Item b = new Item.ItemBuilder()
                .setName("B")
                .build();
        Item c = new Item.ItemBuilder()
                .setName("C")
                .build();
        Composition co = new Composition.CompositionBuilder().build();
        assertTrue(co.appendChild(a));
        assertTrue(co.appendChild(b));
        List<Composable> children = co.getChildren();
        assertEquals(children.size(), 2);
        assertTrue(children.get(0).isEquivalentTo(a));
        assertTrue(children.get(1).isEquivalentTo(b));
        assertTrue(co.removeChild(1));
        children = co.getChildren();
        assertEquals(children.size(), 1);
        assertTrue(co.setChild(0, c));
        children = co.getChildren();
        assertEquals(children.size(), 1);
        assertTrue(children.get(0).isEquivalentTo(c));

        co.clearChildren();

        children = new ArrayList<>();
        children.add(a);
        children.add(b);
        co.setChildren(children);
        children = co.getChildren();
        assertEquals(children.size(), 2);
        assertTrue(children.get(0).isEquivalentTo(a));
        assertTrue(children.get(1).isEquivalentTo(b));

        co.clearChildren();//need to clear children because setChildren does not clear parent of previous children

        children = co.getChildren();
        assertEquals(children.size(), 0);
        children = new ArrayList<>();
        children.add(c);
        children.add(b);
        children.add(a);
        co.setChildren(children);
        children = co.getChildren();
        assertEquals(children.size(), 3);
        assertTrue(children.get(0).isEquivalentTo(c));
        assertTrue(children.get(1).isEquivalentTo(b));
        assertTrue(children.get(2).isEquivalentTo(a));

        try {
            a.close();
            b.close();
            c.close();
            co.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsParentOf() throws NotAChildException, ChildAlreadyParentedException{
        Composition co = new Composition.CompositionBuilder().build();
        Composition co2 = new Composition.CompositionBuilder().build();
        assertFalse(co.isParentOf(co2));
        assertTrue(co.appendChild(co2));
        assertTrue(co.isParentOf(co2));
        try {
            co.close();
            co2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParentManip() throws ChildAlreadyParentedException {
        Item it = new Item.ItemBuilder().build();
        Composition co = new Composition.CompositionBuilder().build();
        assertTrue(co.appendChild(it));
        assertTrue(it.parent().isEquivalentTo(co));
        try {
            it.close();
            co.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMoveChild() throws ChildAlreadyParentedException {
        Item it = new Item.ItemBuilder().build();
        Composition co = new Composition.CompositionBuilder().build();
        assertTrue(co.appendChild(it));
        assertTrue(it.parent().isEquivalentTo(co));

        Composition co2 = new Composition.CompositionBuilder().build();
        Exception exception = assertThrows(ChildAlreadyParentedException.class, () -> {
            co2.appendChild(it);
        });
        assertTrue(exception.getMessage().equals("child already has a parent"));

        assertTrue(co.removeChild(0));
        assertTrue(co2.appendChild(it));
        assertTrue(it.parent().isEquivalentTo(co2));
        try {
            it.close();
            co.close();
            co2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEachChildRecursion() throws ChildAlreadyParentedException, NotAChildException, ObjectWithoutDurationException, CannotComputeAvailableRangeException {
        Timeline tl = new Timeline.TimelineBuilder()
                .setName("TL")
                .build();
        Track tr1 = new Track.TrackBuilder()
                .setName("tr1")
                .build();
        Clip c1 = new Clip.ClipBuilder()
                .setName("c1")
                .build();
        Clip c2 = new Clip.ClipBuilder()
                .setName("c2")
                .build();
        Clip c3 = new Clip.ClipBuilder()
                .setName("c3")
                .build();
        assertTrue(tr1.appendChild(c1));
        assertTrue(tr1.appendChild(c2));
        assertTrue(tr1.appendChild(c3));

        Track tr2 = new Track.TrackBuilder()
                .setName("tr2")
                .build();
        Clip c4 = new Clip.ClipBuilder()
                .setName("c4")
                .build();
        Clip c5 = new Clip.ClipBuilder()
                .setName("c5")
                .build();
        assertTrue(tr2.appendChild(c4));
        assertTrue(tr2.appendChild(c5));

        Stack tlStack = new Stack.StackBuilder().build();
        assertTrue(tlStack.appendChild(tr1));
        assertTrue(tlStack.appendChild(tr2));
        tl.setTracks(tlStack);

        Stack st = new Stack.StackBuilder()
                .setName("st")
                .build();
        assertTrue(tr2.appendChild(st));
        Clip c6 = new Clip.ClipBuilder()
                .setName("c6")
                .build();
        assertTrue(st.appendChild(c6));

        Track tr3 = new Track.TrackBuilder()
                .setName("tr3")
                .build();
        Clip c7 = new Clip.ClipBuilder()
                .setName("c7")
                .build();
        Clip c8 = new Clip.ClipBuilder()
                .setName("c8")
                .build();
        assertTrue(tr3.appendChild(c7));
        assertTrue(tr3.appendChild(c8));
        assertTrue(st.appendChild(tr3));

        tlStack = tl.getTracks();
        assertEquals(tlStack.getChildren().size(), 2);
        assertEquals(tr1.getChildren().size(), 3);
        assertEquals(tr2.getChildren().size(), 3);
        assertEquals(st.getChildren().size(), 2);
        assertEquals(tr3.getChildren().size(), 2);

        List<Clip> clips = tl.eachClip().collect(Collectors.toList());
        List<Clip> clipsCompare = Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8);
        assertEquals(clips, clipsCompare);

        List<Track> allTracks = tl.eachChild(Track.class).collect(Collectors.toList());
        List<Track> tracksCompare = Arrays.asList(tr1, tr2, tr3);
        assertEquals(allTracks, tracksCompare);

        List<Stack> allStacks = tl.eachChild(Stack.class).collect(Collectors.toList());
        List<Stack> stacksCompare = Collections.singletonList(st);
        assertEquals(allStacks, stacksCompare);

        List<Composable> allChildren = tl.eachChild(null, Composable.class).collect(Collectors.toList());
        List<Composable> childrenCompare = Arrays.asList(tr1, c1, c2, c3, tr2, c4, c5, st, c6, tr3, c7, c8);
        assertEquals(allChildren, childrenCompare);

        try {
            tl.close();
            c1.close();
            c2.close();
            c3.close();
            c4.close();
            c5.close();
            c6.close();
            c7.close();
            c8.close();
            st.close();
            tr1.close();
            tr2.close();
            tr3.close();
            tlStack.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveActuallyRemoves() throws ChildAlreadyParentedException {
        Track track = new Track.TrackBuilder().build();
        Clip clip = new Clip.ClipBuilder().build();
        assertTrue(track.appendChild(clip));
        assertTrue(track.getChildren().get(0).isEquivalentTo(clip));
        // delete by index
        assertTrue(track.removeChild(0));
        assertEquals(track.getChildren().size(), 0);
        assertTrue(track.appendChild(clip));
        Clip clip2 = new Clip.ClipBuilder()
                .setName("test")
                .build();
        assertTrue(track.setChild(0, clip2));
        assertTrue(track.getChildren().get(0).isEquivalentTo(clip2));
        assertFalse(track.getChildren().get(0).isEquivalentTo(clip));
        try {
            track.close();
            clip.close();
            clip2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChildrenIfNullTimeRange() throws Exception{
        try(Composition composition = new Composition.CompositionBuilder().build();)
        {
            assertThrows(NullPointerException.class,
                    ()->{composition.childrenIf(Composable.class, null, false);});
        }
    }
}
