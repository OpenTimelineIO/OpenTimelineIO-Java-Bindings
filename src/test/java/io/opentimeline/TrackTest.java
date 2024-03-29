// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline;

import io.opentimeline.opentime.RationalTime;
import io.opentimeline.opentime.TimeRange;
import io.opentimeline.opentimelineio.*;
import io.opentimeline.opentimelineio.exception.*;
import io.opentimeline.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

public class TrackTest {

    @Test
    public void testSerialize() throws OpenTimelineIOException {
        Track track = new Track.TrackBuilder()
                .setName("foo")
                .build();
        String encoded = track.toJSONString();
        SerializableObject decoded = SerializableObject.fromJSONString(encoded);
        assertEquals(decoded, track);
        try {
            track.close();
            decoded.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInstancing() throws ChildAlreadyParentedException, CannotComputeAvailableRangeException {
        RationalTime length = new RationalTime(5, 1);
        TimeRange tr = new TimeRange(new RationalTime(), length);
        Item it = new Item.ItemBuilder()
                .setSourceRange(tr)
                .build();
        Track sq = new Track.TrackBuilder().build();
        assertTrue(sq.appendChild(it));
        assertEquals(sq.rangeOfChildAtIndex(0), tr);

        Exception exception = assertThrows(ChildAlreadyParentedException.class, () -> {
            assertFalse(sq.appendChild(it));
        });
        assertTrue(exception.getMessage().equals("child already has a parent"));
//        sq.clearChildren();

//        List<Composable> children = new ArrayList<>();
//        children.add(it);
//        children.add(it);
//        children.add(it);
//        sq.setChildren(children, errorStatus);
//        assertEquals(errorStatus.getOutcome(), ErrorStatus.Outcome.CHILD_ALREADY_PARENTED);
        try {
            it.close();
            sq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteParentContainer() throws ChildAlreadyParentedException {
        Item it = new Item.ItemBuilder().build();
        Track sq = new Track.TrackBuilder().build();
        assertTrue(sq.appendChild(it));
        try {
            sq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNull(it.parent());
        try {
            it.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTransactional() throws OpenTimelineIOException {
        Item it = new Item.ItemBuilder().build();
        Track trackA = new Track.TrackBuilder().build();
        Track trackB = new Track.TrackBuilder().build();

        assertTrue(trackA.appendChild((Composable) (it.deepCopy())));
        assertTrue(trackA.appendChild((Composable) (it.deepCopy())));
        assertTrue(trackA.appendChild((Composable) (it.deepCopy())));
        assertEquals(trackA.getChildren().size(), 3);

        assertTrue(trackB.appendChild((Composable) (it.deepCopy())));
        assertTrue(trackB.appendChild((Composable) (it.deepCopy())));
        assertTrue(trackB.appendChild((Composable) (it.deepCopy())));
        assertEquals(trackB.getChildren().size(), 3);

        List<Composable> children = new ArrayList<>();
        children.add((Composable) (it.deepCopy()));
        children.add((Composable) (it.deepCopy()));
        children.add((Composable) (it.deepCopy()));
        children.add((Composable) (it.deepCopy()));
        children.add(trackB.getChildren().get(0));
        Exception exception = assertThrows(ChildAlreadyParentedException.class, () -> {
            trackA.setChildren(children);
        });
        assertTrue(exception.getMessage().equals("child already has a parent"));
        assertEquals(trackA.getChildren().size(), 3);
        try {
            it.close();
            trackA.close();
            trackB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRange() throws OpenTimelineIOException {
        RationalTime length = new RationalTime(5, 1);
        TimeRange tr = new TimeRange(new RationalTime(), length);
        Item it = new Item.ItemBuilder()
                .setSourceRange(tr)
                .build();
        Track sq = new Track.TrackBuilder().build();
        assertTrue(sq.appendChild(it));
        assertEquals(sq.rangeOfChildAtIndex(0), tr);
        // It is an error to add an item to composition if it is already in
        // another composition.  This clears out the old test composition
        // (and also clears out its parent pointers).
        sq.clearChildren();
        assertTrue(sq.appendChild(it));
        assertTrue(sq.appendChild(((Composable) it.deepCopy())));
        assertTrue(sq.appendChild(((Composable) it.deepCopy())));
        assertTrue(sq.appendChild(((Composable) it.deepCopy())));

        assertEquals(sq.rangeOfChildAtIndex(1),
                new TimeRange(
                        new RationalTime(5, 1),
                        new RationalTime(5, 1)));
        assertEquals(sq.rangeOfChildAtIndex(0),
                new TimeRange(
                        new RationalTime(0, 1),
                        new RationalTime(5, 1)));
        assertEquals(sq.rangeOfChildAtIndex(-1),
                new TimeRange(
                        new RationalTime(15, 1),
                        new RationalTime(5, 1)));
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            sq.rangeOfChildAtIndex(11);
        });

        assertEquals(sq.getDuration(), length.add(length).add(length).add(length));

        // add a transition to either side
        RationalTime inOffset = new RationalTime(10, 24);
        RationalTime outOffset = new RationalTime(12, 24);
        TimeRange rangeOfItem = sq.rangeOfChildAtIndex(3);
        Transition trx = new Transition.TransitionBuilder()
                .setInOffset(inOffset)
                .setOutOffset(outOffset)
                .build();
        assertTrue(sq.insertChild(0, ((Composable) trx.deepCopy())));
        assertTrue(sq.insertChild(3, ((Composable) trx.deepCopy())));
        assertTrue(sq.appendChild(((Composable) trx.deepCopy())));

        // range of Transition
        assertEquals(sq.rangeOfChildAtIndex(3),
                new TimeRange(
                        new RationalTime(230, 24),
                        new RationalTime(22, 24)));
        assertEquals(sq.rangeOfChildAtIndex(-1),
                new TimeRange(
                        new RationalTime(470, 24),
                        new RationalTime(22, 24)));

        // range of item is not altered by insertion of transitions
        assertEquals(sq.rangeOfChildAtIndex(5), rangeOfItem);

        // inOffset and outOffset for the beginning and ending
        assertEquals(sq.getDuration(),
                inOffset.add(length).add(length).add(length).add(length).add(outOffset));
        try {
            it.close();
            sq.close();
            trx.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRangeOfChild() throws ChildAlreadyParentedException, CannotComputeAvailableRangeException, NotAChildException, ObjectWithoutDurationException, InvalidTimeRangeException {
        Clip clip1 = new Clip.ClipBuilder()
                .setName("clip1")
                .setSourceRange(
                        new TimeRange(
                                new RationalTime(100, 24),
                                new RationalTime(50, 24)
                        ))
                .build();
        Clip clip2 = new Clip.ClipBuilder()
                .setName("clip2")
                .setSourceRange(
                        new TimeRange(
                                new RationalTime(101, 24),
                                new RationalTime(50, 24)
                        ))
                .build();
        Clip clip3 = new Clip.ClipBuilder()
                .setName("clip3")
                .setSourceRange(
                        new TimeRange(
                                new RationalTime(102, 24),
                                new RationalTime(50, 24)
                        ))
                .build();
        Track sq = new Track.TrackBuilder()
                .setName("foo")
                .build();
        assertTrue(sq.appendChild(clip1));
        assertTrue(sq.appendChild(clip2));
        assertTrue(sq.appendChild(clip3));

        // track should be as long as the children summed up
        assertEquals(sq.getDuration(), new RationalTime(150, 24));

        // sequenced items should all land end to end
        assertEquals(sq.rangeOfChildAtIndex(0).getStartTime()
                , new RationalTime());
        assertEquals(sq.rangeOfChildAtIndex(1).getStartTime()
                , new RationalTime(50, 24));
        assertEquals(sq.rangeOfChildAtIndex(2).getStartTime()
                , new RationalTime(100, 24));
        assertEquals(sq.getRangeOfChild(sq.getChildren().get(2)),
                sq.rangeOfChildAtIndex(2));

        assertEquals(sq.rangeOfChildAtIndex(0).getDuration()
                , new RationalTime(50, 24));
        assertEquals(sq.rangeOfChildAtIndex(1).getDuration()
                , new RationalTime(50, 24));
        assertEquals(sq.rangeOfChildAtIndex(2).getDuration()
                , new RationalTime(50, 24));

        // should trim 5 frames off the front and 5 frames off the back
        TimeRange sqSourceRange = new TimeRange(
                new RationalTime(5, 24),
                new RationalTime(140, 24));
        sq.setSourceRange(sqSourceRange);
        assertEquals(sq.trimmedRangeOfChildAtIndex(0),
                new TimeRange(
                        new RationalTime(5, 24),
                        new RationalTime(45, 24)));
        assertEquals(sq.trimmedRangeOfChildAtIndex(1),
                sq.rangeOfChildAtIndex(1));
        assertEquals(sq.trimmedRangeOfChildAtIndex(2),
                new TimeRange(
                        new RationalTime(100, 24),
                        new RationalTime(45, 24)));

        // get the trimmed range in parent
        assertEquals(((Clip) sq.getChildren().get(0)).getTrimmedRangeInParent(),
                sq.getTrimmedRangeOfChild(sq.getChildren().get(0)));

        // same test but via iteration
        for (int i = 0; i < sq.getChildren().size(); i++) {
            assertEquals(((Clip) sq.getChildren().get(i)).getTrimmedRangeInParent(),
                    sq.getTrimmedRangeOfChild(sq.getChildren().get(i)));
        }
        try {
            clip1.close();
            clip2.close();
            clip3.close();
            sq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRangeTrimmedOut() throws ChildAlreadyParentedException, CannotComputeAvailableRangeException {
        Clip clip1 = new Clip.ClipBuilder()
                .setName("clip1")
                .setSourceRange(
                        new TimeRange(
                                new RationalTime(100, 24),
                                new RationalTime(50, 24)
                        ))
                .build();
        Clip clip2 = new Clip.ClipBuilder()
                .setName("clip2")
                .setSourceRange(
                        new TimeRange(
                                new RationalTime(101, 24),
                                new RationalTime(50, 24)
                        ))
                .build();

        Track track = new Track.TrackBuilder()
                .setName("foo")
                .build();
        assertTrue(track.appendChild(clip1));
        assertTrue(track.appendChild(clip2));
        // should trim out clip 1
        track.setSourceRange(new TimeRange(
                new RationalTime(60, 24),
                new RationalTime(10, 24)));
        Exception exception = assertThrows(InvalidTimeRangeException.class, () -> {
            track.trimmedRangeOfChildAtIndex(0);
        });
        assertTrue(exception.getMessage().equals("computed time range would be invalid"));

        TimeRange notNothing = track.trimmedRangeOfChildAtIndex(1);
        assertEquals(notNothing, track.getSourceRange());

        // should trim out second clip
        track.setSourceRange(new TimeRange(
                new RationalTime(0, 24),
                new RationalTime(10, 24)));
        exception = assertThrows(InvalidTimeRangeException.class, () -> {
            track.trimmedRangeOfChildAtIndex(1);
        });
        assertTrue(exception.getMessage().equals("computed time range would be invalid"));

        notNothing = track.trimmedRangeOfChildAtIndex(0);
        assertEquals(notNothing, track.getSourceRange());
        try {
            clip1.close();
            clip2.close();
            track.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRangeNested() throws OpenTimelineIOException {
        Clip clip1 = new Clip.ClipBuilder()
                .setName("clip1")
                .setSourceRange(
                        new TimeRange(
                                new RationalTime(100, 24),
                                new RationalTime(50, 24)
                        ))
                .build();
        Clip clip2 = new Clip.ClipBuilder()
                .setName("clip2")
                .setSourceRange(
                        new TimeRange(
                                new RationalTime(101, 24),
                                new RationalTime(50, 24)
                        ))
                .build();
        Clip clip3 = new Clip.ClipBuilder()
                .setName("clip3")
                .setSourceRange(
                        new TimeRange(
                                new RationalTime(102, 24),
                                new RationalTime(50, 24)
                        ))
                .build();
        Track track = new Track.TrackBuilder()
                .setName("inner")
                .build();
        assertTrue(track.appendChild(clip1));
        assertTrue(track.appendChild(clip2));
        assertTrue(track.appendChild(clip3));

        assertEquals(track.getChildren().size(), 3);

        // make a nested track with 3 sub-tracks, each with 3 clips
        Track outerTrack = new Track.TrackBuilder()
                .setName("outer")
                .build();
        assertTrue(outerTrack.appendChild((Composable) (track.deepCopy())));
        assertTrue(outerTrack.appendChild((Composable) (track.deepCopy())));
        assertTrue(outerTrack.appendChild((Composable) (track.deepCopy())));

        // make one long track with 9 clips
        Track longTrack = new Track.TrackBuilder()
                .setName("long")
                .build();
        for (int i = 0; i < 3; i++) {
            List<Composable> children = track.getChildren();
            for (int j = 0; j < children.size(); j++) {
                assertTrue(longTrack.appendChild(
                        (Composable) (children.get(j).deepCopy())));
            }
        }

        // the original track's children should have been copied
        Exception exception = assertThrows(NotAChildException.class, () -> {
            outerTrack.getRangeOfChild(track.getChildren().get(1));
        });
        assertTrue(exception.getMessage().equals("item is not a descendent of specified object"));

        exception = assertThrows(NotAChildException.class, () -> {
            longTrack.getRangeOfChild(track.getChildren().get(1));
        });
        assertTrue(exception.getMessage().equals("item is not a descendent of specified object"));

        // the nested and long tracks should be the same length
        assertEquals(outerTrack.getDuration(), longTrack.getDuration());

        // the 9 clips within both compositions should have the same
        // overall timing, even though the nesting is different.
        List<Composable> outerTrackClips = new ArrayList<>();
        List<Composable> outerTrackChildren = outerTrack.getChildren();
        Track outerTrackChild1 = (Track) (outerTrackChildren.get(0));
        Track outerTrackChild2 = (Track) (outerTrackChildren.get(1));
        Track outerTrackChild3 = (Track) (outerTrackChildren.get(2));
        outerTrackClips.addAll(outerTrackChild1.getChildren());
        outerTrackClips.addAll(outerTrackChild2.getChildren());
        outerTrackClips.addAll(outerTrackChild3.getChildren());
        List<Composable> longTrackClips = longTrack.getChildren();
        assertEquals(longTrackClips.size(), outerTrackClips.size());
        for (int i = 0; i < longTrackClips.size(); i++) {
            assertEquals(outerTrack.getRangeOfChild(outerTrackClips.get(i)),
                    longTrack.getRangeOfChild(longTrackClips.get(i)));
        }

        // using eachClip Stream
        ArrayList<TimeRange> list1 = new ArrayList<>();
        outerTrack.eachClip(null).forEach(clip -> {
            try {
                list1.add(outerTrack.getRangeOfChild(clip));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        ArrayList<TimeRange> list2 = new ArrayList<>();
        longTrack.eachClip(null).forEach(clip -> {
            try {
                list2.add(longTrack.getRangeOfChild(clip));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        assertEquals(list1, list2);
        try {
            clip1.close();
            clip2.close();
            clip3.close();
            track.close();
            outerTrack.close();
            longTrack.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetItem() throws ChildAlreadyParentedException {
        Track seq = new Track.TrackBuilder().build();
        Clip it = new Clip.ClipBuilder().build();
        Clip it2 = new Clip.ClipBuilder().build();
        assertTrue(seq.appendChild(it));
        assertEquals(seq.getChildren().size(), 1);
        assertTrue(seq.setChild(0, it2));
        assertEquals(seq.getChildren().size(), 1);
        try {
            seq.close();
            it.close();
            it2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTransformedTime() throws ChildAlreadyParentedException, NotAChildException, ObjectWithoutDurationException, CannotComputeAvailableRangeException {
        Clip clip1 = new Clip.ClipBuilder()
                .setName("clip1")
                .setSourceRange(
                        new TimeRange(
                                new RationalTime(100, 24),
                                new RationalTime(50, 24)
                        ))
                .build();
        Clip clip2 = new Clip.ClipBuilder()
                .setName("clip2")
                .setSourceRange(
                        new TimeRange(
                                new RationalTime(101, 24),
                                new RationalTime(50, 24)
                        ))
                .build();
        Clip clip3 = new Clip.ClipBuilder()
                .setName("clip3")
                .setSourceRange(
                        new TimeRange(
                                new RationalTime(102, 24),
                                new RationalTime(50, 24)
                        ))
                .build();
        Track sq = new Track.TrackBuilder()
                .setName("foo")
                .build();
        assertTrue(sq.appendChild(clip1));
        assertTrue(sq.appendChild(clip2));
        assertTrue(sq.appendChild(clip3));

        Gap fl = new Gap.GapBuilder()
                .setName("GAP")
                .setSourceRange(new TimeRange(
                        new RationalTime(0, 24),
                        new RationalTime(50, 24)))
                .build();
        assertFalse(fl.isVisible());
        List<Composable> sqChildren = sq.getChildren();
        clip1 = ((Clip) sqChildren.get(0));
        clip2 = ((Clip) sqChildren.get(1));
        clip3 = ((Clip) sqChildren.get(2));
        assertEquals(clip1.getName(), "clip1");
        assertEquals(clip2.getName(), "clip2");
        assertEquals(clip3.getName(), "clip3");

        assertEquals(sq.getTransformedTime(new RationalTime(0, 24), clip1),
                new RationalTime(100, 24));
        assertEquals(sq.getTransformedTime(new RationalTime(0, 24), clip2),
                new RationalTime(51, 24));
        assertEquals(sq.getTransformedTime(new RationalTime(0, 24), clip3),
                new RationalTime(2, 24));

        assertEquals(sq.getTransformedTime(new RationalTime(50, 24), clip1),
                new RationalTime(150, 24));
        assertEquals(sq.getTransformedTime(new RationalTime(50, 24), clip2),
                new RationalTime(101, 24));
        assertEquals(sq.getTransformedTime(new RationalTime(50, 24), clip3),
                new RationalTime(52, 24));

        assertEquals(clip1.getTransformedTime(new RationalTime(100, 24), sq),
                new RationalTime(0, 24));
        assertEquals(clip2.getTransformedTime(new RationalTime(101, 24), sq),
                new RationalTime(50, 24));
        assertEquals(clip3.getTransformedTime(new RationalTime(102, 24), sq),
                new RationalTime(100, 24));

        assertEquals(clip1.getTransformedTime(new RationalTime(150, 24), sq),
                new RationalTime(50, 24));
        assertEquals(clip2.getTransformedTime(new RationalTime(151, 24), sq),
                new RationalTime(100, 24));
        assertEquals(clip3.getTransformedTime(new RationalTime(152, 24), sq),
                new RationalTime(150, 24));
        try {
            clip1.close();
            clip2.close();
            clip3.close();
            sq.close();
            fl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNeighborsOfSimple() throws OpenTimelineIOException {
        Track seq = new Track.TrackBuilder().build();
        Transition trans = new Transition.TransitionBuilder()
                .setInOffset(new RationalTime(10, 24))
                .setOutOffset(new RationalTime(10, 24))
                .build();
        assertTrue(seq.appendChild(trans));
        // neighbors of first transition
        Pair<Composable, Composable> neighbors = seq.getNeighborsOf(
                seq.getChildren().get(0), Track.NeighborGapPolicy.never);
        assertEquals(neighbors, new Pair<Composable, Composable>(null, null));
        // test with neighbor filling policy on
        neighbors = seq.getNeighborsOf(
                seq.getChildren().get(0), Track.NeighborGapPolicy.around_transitions);
        Gap fill = new Gap.GapBuilder()
                .setSourceRange(new TimeRange(
                        new RationalTime(0, trans.getInOffset().getRate()),
                        trans.getInOffset()))
                .build();
        assertEquals(neighbors, new Pair<Composable, Composable>(fill, (Composable) fill.deepCopy()));
        try {
            seq.close();
            trans.close();
            fill.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNeighborsOfNoExpand() throws ChildAlreadyParentedException, NotAChildException {
        Track seq = new Track.TrackBuilder().build();
        Clip clip = new Clip.ClipBuilder().build();
        assertTrue(seq.appendChild(clip));

        Pair<Composable, Composable> neighbors = seq.getNeighborsOf(
                seq.getChildren().get(0));
        assertEquals(neighbors, new Pair<Composable, Composable>(null, null));
        assertNull(neighbors.getFirst());
        assertNull(neighbors.getSecond());
        try {
            seq.close();
            clip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNeighborsOfFromData() throws OpenTimelineIOException {
        String projectRootDir = System.getProperty("user.dir");
        String sampleDataDir = projectRootDir + File.separator +
                "src" + File.separator + "test" + File.separator + "sample_data";
        String genRefTest = sampleDataDir + File.separator + "transition_test.otio";
        File file = new File(genRefTest);
        assertTrue(file.exists());
        Timeline timeline = (Timeline) SerializableObject.fromJSONFile(genRefTest);
        Stack stack = timeline.getTracks();
        List<Composable> stackChildren = stack.getChildren();
        Track track = (Track) stackChildren.get(0);
        Pair<Composable, Composable> neighbors = track.getNeighborsOf(
                track.getChildren().get(0), Track.NeighborGapPolicy.never);
        assertEquals(neighbors, new Pair<Composable, Composable>(null, track.getChildren().get(1)));
        Gap fill = new Gap.GapBuilder()
                .setSourceRange(new TimeRange(
                        new RationalTime(0, ((Transition) track.getChildren().get(0)).getInOffset().getRate()),
                        ((Transition) track.getChildren().get(0)).getInOffset()))
                .build();
        neighbors = track.getNeighborsOf(
                track.getChildren().get(0), Track.NeighborGapPolicy.around_transitions);
        assertEquals(neighbors, new Pair<Composable, Composable>(fill, track.getChildren().get(1)));
        // neighbor around second transition
        neighbors = track.getNeighborsOf(
                track.getChildren().get(2), Track.NeighborGapPolicy.never);
        assertEquals(neighbors, new Pair<>(track.getChildren().get(1), track.getChildren().get(3)));
        // no change w/ different policy
        neighbors = track.getNeighborsOf(
                track.getChildren().get(2), Track.NeighborGapPolicy.around_transitions);
        assertEquals(neighbors, new Pair<>(track.getChildren().get(1), track.getChildren().get(3)));
        // neighbor around third transition
        neighbors = track.getNeighborsOf(
                track.getChildren().get(5), Track.NeighborGapPolicy.around_transitions);
        assertEquals(neighbors, new Pair<Composable, Composable>(track.getChildren().get(4), null));

        try {
            fill.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fill = new Gap.GapBuilder()
                .setSourceRange(new TimeRange(
                        new RationalTime(0, ((Transition) track.getChildren().get(5)).getInOffset().getRate()),
                        ((Transition) track.getChildren().get(5)).getInOffset()))
                .build();

        neighbors = track.getNeighborsOf(
                track.getChildren().get(5), Track.NeighborGapPolicy.around_transitions);
        assertEquals(neighbors, new Pair<Composable, Composable>(track.getChildren().get(4), null));
        try {
            timeline.close();
            stack.close();
            track.close();
            fill.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRangeOfAllChildren() throws OpenTimelineIOException {
        String projectRootDir = System.getProperty("user.dir");
        String sampleDataDir = projectRootDir + File.separator +
                "src" + File.separator + "test" + File.separator + "sample_data";
        String genRefTest = sampleDataDir + File.separator + "transition_test.otio";
        File file = new File(genRefTest);
        assertTrue(file.exists());
        Timeline timeline = (Timeline) SerializableObject.fromJSONFile(genRefTest);
        Stack stack = timeline.getTracks();
        List<Composable> stackChildren = stack.getChildren();
        Track track = (Track) stackChildren.get(0);
        HashMap<Composable, TimeRange> rangeOfAllChildren = track.getRangeOfAllChildren();
        List<Composable> trackChildren = track.getChildren();
        ArrayList<Composable> trackClips = new ArrayList<>();
        for (Composable trackChild : trackChildren) {
            if (trackChild instanceof Clip)
                trackClips.add(trackChild);
        }
        assertEquals(rangeOfAllChildren.get(trackClips.get(0)).getStartTime().getValue(), 0);
        assertEquals(rangeOfAllChildren.get(trackClips.get(1)).getStartTime(),
                rangeOfAllChildren.get(trackClips.get(0)).getDuration());

        for (Composable stackChild : stackChildren) {
            Track t = (Track) stackChild;
            List<Composable> tChildren = t.getChildren();
            for (Composable tChild : tChildren) {
                if (tChild instanceof Clip) {
                    assertEquals(((Clip) tChild).getRangeInParent(), rangeOfAllChildren.get(tChild));
                } else if (tChild instanceof Transition) {
                    assertEquals(((Transition) tChild).getRangeInParent(), rangeOfAllChildren.get(tChild));
                }
            }
        }
        try {
            timeline.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        track = new Track.TrackBuilder().build();
        rangeOfAllChildren = track.getRangeOfAllChildren();
        assertEquals(rangeOfAllChildren.size(), 0);
        try {
            track.close();
            stack.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClipIfNullTimeRange() throws Exception{
        try(Track track = new Track.TrackBuilder().build();)
        {
            assertThrows(NullPointerException.class,
                    ()->{track.clipIf(null, false);});
        }
    }

    @Test
    public void testClipIfEquality() throws Exception{
        try(
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

            //testing full time range
            List<Clip> clip_fullTimeRange = Arrays.asList(C1, C2, C3, C4);
            List<Clip> result_fullTimeRange = track.childrenIf(Clip.class, Optional.empty(), false);
            assertEquals(clip_fullTimeRange.size(), result_fullTimeRange.size());
            for(int i = 0; i < clip_fullTimeRange.size(); i++){
                assertTrue((result_fullTimeRange.get(i)).isEquivalentTo(clip_fullTimeRange.get(i)));
            }

            //testing trimmed time range
            TimeRange search_range = new TimeRange(new RationalTime(0, 24), new RationalTime(20, 24));
            List<Clip> clip_trimmedTimeRange = Arrays.asList(C1, C2);
            List<Clip> result_trimmedTimeRange = track.childrenIf(Clip.class ,Optional.of(search_range), false);
            assertEquals(clip_trimmedTimeRange.size(), result_trimmedTimeRange.size());
            for(int i = 0; i < clip_trimmedTimeRange.size(); i++){
                assertTrue((result_trimmedTimeRange.get(i)).isEquivalentTo(clip_trimmedTimeRange.get(i)));
            }
        }
    }

    @Test
    public void testChildrenIfComposableEquality() throws Exception {
        try (
                Track track = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Track track2 = new Track.TrackBuilder()
                        .setName("V2")
                        .setKind(Track.Kind.video)
                        .build();
                ExternalReference mr = new ExternalReference.ExternalReferenceBuilder()
                        .setAvailableRange(TimeRange.rangeFromStartEndTime(
                                new RationalTime(0, 24),
                                new RationalTime(48, 24)))
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
        ) {
            assertTrue(track.appendChild(track2));
            assertTrue(track2.appendChild(C1));
            assertTrue(track2.appendChild(C2));
            assertTrue(track2.appendChild(C3));
            //testing full time range
            List<Composable> composable_fullTimeRange = Arrays.asList(track2, C1, C2, C3);
            List<Composable> result_fullTimeRange = track.childrenIf(Composable.class, Optional.empty(), false);
            assertEquals(composable_fullTimeRange.size(), result_fullTimeRange.size());
            for(int i = 0; i < composable_fullTimeRange.size(); i++){
                assertTrue((result_fullTimeRange.get(i)).isEquivalentTo(composable_fullTimeRange.get(i)));
            }

            //testing trimmed time range
            TimeRange search_range = new TimeRange(new RationalTime(0, 24), new RationalTime(20, 24));
            List<Composable> composable_trimmedTimeRange = Arrays.asList(track2, C1, C2);
            List<Composable> result_trimmedTimeRange = track.childrenIf(Composable.class ,Optional.of(search_range), false);
            assertEquals(composable_trimmedTimeRange.size(), result_trimmedTimeRange.size());
            for(int i = 0; i < composable_trimmedTimeRange.size(); i++){
                assertTrue((result_trimmedTimeRange.get(i)).isEquivalentTo(composable_trimmedTimeRange.get(i)));
            }
        }

    }

    @Test
    public void testChildrenIfNullTimeRange() throws Exception{
        try(Track track = new Track.TrackBuilder().build();)
        {
            assertThrows(NullPointerException.class,
                    ()->{track.childrenIf(Composable.class, null, false);});
        }
    }
}
