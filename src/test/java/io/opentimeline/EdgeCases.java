// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline;

import io.opentimeline.opentime.RationalTime;
import io.opentimeline.opentime.TimeRange;
import io.opentimeline.opentimelineio.*;
import io.opentimeline.opentimelineio.exception.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeCases {

    @Test
    public void testEmptyCompositions() throws CannotComputeAvailableRangeException {
        Timeline timeline = new Timeline.TimelineBuilder().build();
        Stack stack = timeline.getTracks();
        List<Composable> tracks = stack.getChildren();
        assertEquals(tracks.size(), 0);
        assertEquals(stack.getDuration(), new RationalTime(0, 24));
        try {
            timeline.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIteratingOverDupes() throws Exception {
        Timeline timeline = new Timeline.TimelineBuilder().build();
        Track track = new Track.TrackBuilder().build();
        Clip clip = new Clip.ClipBuilder()
                .setName("Dupe")
                .setSourceRange(new TimeRange(
                        new RationalTime(10, 30),
                        new RationalTime(15, 30)))
                .build();
        for (int i = 0; i < 10; i++) {
            Clip dupe = (Clip) clip.deepCopy();
            assertTrue(track.appendChild(dupe));
        }
        Stack stack = new Stack.StackBuilder().build();
        assertTrue(stack.appendChild(track));
        timeline.setTracks(stack);
        assertEquals(track.getChildren().size(), 10);
        assertEquals(track.getTrimmedRange(),
                new TimeRange(
                        new RationalTime(0, 30),
                        new RationalTime(150, 30)));

        List<Composable> trackChildren = track.getChildren();
        TimeRange previous = null;
        // test normal iteration
        for (Composable child : trackChildren) {
            Clip childClip = (Clip) child;
            assertEquals(track.getRangeOfChild(childClip),
                    childClip.getRangeInParent());
            assertNotEquals(childClip.getRangeInParent(), previous);
            previous = childClip.getRangeInParent();
        }

        previous = null;
        // compare to iteration by index
        for (int i = 0; i < trackChildren.size(); i++) {
            Clip childClip = (Clip) trackChildren.get(i);
            assertEquals(track.getRangeOfChild(childClip),
                    track.rangeOfChildAtIndex(i));
            assertEquals(track.getRangeOfChild(childClip),
                    childClip.getRangeInParent());
            assertNotEquals(childClip.getRangeInParent(), previous);
            previous = childClip.getRangeInParent();
        }
    }

}
