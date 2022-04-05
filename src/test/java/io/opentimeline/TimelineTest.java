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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TimelineTest {

    @Test
    public void testInit() {
        RationalTime rt = new RationalTime(12, 24);
        Timeline tl = new Timeline.TimelineBuilder()
                .setName("test_timeline")
                .setGlobalStartTime(rt)
                .build();
        assertEquals(tl.getName(), "test_timeline");
        assertEquals(tl.getGlobalStartTime(), rt);
        try {
            tl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testStr() {
        RationalTime rt = new RationalTime(12, 24);
        Timeline tl = new Timeline.TimelineBuilder()
                .setName("test_timeline")
                .setGlobalStartTime(rt)
                .build();
        assertEquals(tl.toString(),
                "io.opentimeline.opentimelineio.Timeline(" +
                        "name=test_timeline, " +
                        "tracks=io.opentimeline.opentimelineio.Stack(" +
                        "name=tracks, " +
                        "children=[], " +
                        "sourceRange=null, " +
                        "metadata=io.opentimeline.opentimelineio.AnyDictionary{}))");
        try {
            tl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMetadata() throws OpenTimelineIOException {
        RationalTime rt = new RationalTime(12, 24);
        AnyDictionary metadata = new AnyDictionary();
        metadata.put("foo", new Any("bar"));
        Timeline tl = new Timeline.TimelineBuilder()
                .setName("test_timeline")
                .setGlobalStartTime(rt)
                .setMetadata(metadata)
                .build();
        assertEquals(tl.getMetadata().get("foo").safelyCastString(), "bar");

        String encoded = tl.toJSONString();
        Timeline decoded = (Timeline) SerializableObject.fromJSONString(encoded);
        assertEquals(decoded, tl);
        assertEquals(tl.getMetadata(), decoded.getMetadata());
        try {
            tl.close();
            decoded.close();
            metadata.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRange() throws ChildAlreadyParentedException, CannotComputeAvailableRangeException, NotAChildException, ObjectWithoutDurationException{
        Track track = new Track.TrackBuilder().build();
        Stack stack = new Stack.StackBuilder().build();
        RationalTime rt = new RationalTime(5, 24);

        assertTrue(stack.appendChild(track));
        Timeline timeline = new Timeline.TimelineBuilder().build();
        timeline.setTracks(stack);

        ExternalReference mr = new ExternalReference.ExternalReferenceBuilder()
                .setAvailableRange(TimeRange.rangeFromStartEndTime(
                        new RationalTime(5, 24),
                        new RationalTime(15, 24)))
                .setTargetURL("/var/tmp/test.mov")
                .build();
        Clip cl = new Clip.ClipBuilder()
                .setName("test clip1")
                .setMediaReference(mr)
                .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(rt).build())
                .build();
        Clip cl2 = new Clip.ClipBuilder()
                .setName("test clip2")
                .setMediaReference(mr)
                .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(rt).build())
                .build();
        Clip cl3 = new Clip.ClipBuilder()
                .setName("test clip3")
                .setMediaReference(mr)
                .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(rt).build())
                .build();
        assertTrue(track.appendChild(cl));
        assertTrue(track.appendChild(cl2));
        assertTrue(track.appendChild(cl3));

        assertTrue(timeline.getDuration().equals(rt.add(rt).add(rt)));
        assertEquals(timeline.getRangeOfChild(cl),
                ((Track) timeline.getTracks().getChildren().get(0))
                        .getRangeOfChildAtIndex(0));
        try {
            timeline.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerialize() throws OpenTimelineIOException {
        Clip clip = new Clip.ClipBuilder()
                .setName("test_clip")
                .setMediaReference(new MissingReference.MissingReferenceBuilder().build())
                .build();
        Track track = new Track.TrackBuilder().build();
        Stack stack = new Stack.StackBuilder().build();
        assertTrue(stack.appendChild(track));
        Timeline timeline = new Timeline.TimelineBuilder().build();
        timeline.setTracks(stack);
        assertTrue(track.appendChild(clip));
        String encoded = timeline.toJSONString();
        Timeline decoded = (Timeline) SerializableObject.fromJSONString(encoded);
        assertEquals(decoded, timeline);
        String encoded2 = decoded.toJSONString();
        assertEquals(encoded, encoded2);
        try {
            timeline.close();
            decoded.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerializeOfSubclasses() throws OpenTimelineIOException {
        Clip clip1 = new Clip.ClipBuilder()
                .setName("Test Clip")
                .setMediaReference(new ExternalReference.ExternalReferenceBuilder()
                        .setTargetURL("/tmp/foo.mov")
                        .build())
                .build();
        Track track = new Track.TrackBuilder().build();
        Stack stack = new Stack.StackBuilder().build();
        assertTrue(stack.appendChild(track));
        Timeline tl1 = new Timeline.TimelineBuilder()
                .setName("Testing Serialization")
                .build();
        tl1.setTracks(stack);
        assertTrue(track.appendChild(clip1));
        String serialized = tl1.toJSONString();
        assertNotNull(serialized);
        Timeline tl2 = (Timeline) SerializableObject.fromJSONString(serialized);
        assertEquals(tl2, tl1);
        assertEquals(tl1.getName(), tl2.getName());
        assertEquals(tl1.getTracks().getChildren().size(), 1);
        assertEquals(tl2.getTracks().getChildren().size(), 1);
        Track track1 = (Track) tl1.getTracks().getChildren().get(0);
        Track track2 = (Track) tl2.getTracks().getChildren().get(0);
        assertEquals(track1, track2);
        assertEquals(track1.getChildren().size(), 1);
        assertEquals(track2.getChildren().size(), 1);
        Clip clip2 = (Clip) ((Track) tl2.getTracks().getChildren().get(0)).getChildren().get(0);
        assertEquals(clip1.getName(), clip2.getName());
        assertEquals(clip1.getMediaReference(), clip2.getMediaReference());
        assertEquals(((ExternalReference) clip1.getMediaReference()).getTargetURL(),
                ((ExternalReference) clip2.getMediaReference()).getTargetURL());

        try {
            tl1.close();
            tl2.close();
            clip1.close();
            clip2.close();
            track.close();
            stack.close();
            track1.close();
            track2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTracks() throws ChildAlreadyParentedException {
        Track V1 = new Track.TrackBuilder()
                .setName("V1")
                .setKind(Track.Kind.video)
                .build();
        Track V2 = new Track.TrackBuilder()
                .setName("V2")
                .setKind(Track.Kind.video)
                .build();
        Track A1 = new Track.TrackBuilder()
                .setName("A1")
                .setKind(Track.Kind.audio)
                .build();
        Track A2 = new Track.TrackBuilder()
                .setName("A2")
                .setKind(Track.Kind.audio)
                .build();
        Stack stack = new Stack.StackBuilder().build();
        assertTrue(stack.appendChild(V1));
        assertTrue(stack.appendChild(V2));
        assertTrue(stack.appendChild(A1));
        assertTrue(stack.appendChild(A2));
        Timeline tl = new Timeline.TimelineBuilder().build();
        tl.setTracks(stack);
        ArrayList<String> videoTrackNames = new ArrayList<>();
        List<Track> videoTracks = tl.getVideoTracks();
        for (Track t : videoTracks) {
            videoTrackNames.add(t.getName());
        }
        assertEquals(videoTrackNames, new ArrayList<>(Arrays.asList("V1", "V2")));
        ArrayList<String> audioTrackNames = new ArrayList<>();
        List<Track> audioTracks = tl.getAudioTracks();
        for (Track t : audioTracks) {
            audioTrackNames.add(t.getName());
        }
        assertEquals(audioTrackNames, new ArrayList<>(Arrays.asList("A1", "A2")));
        try {
            tl.close();
            A1.close();
            A2.close();
            V1.close();
            V2.close();
            stack.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChildrenIfComposableEquality() throws Exception{
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack stack = new Stack.StackBuilder().build();
                Track V1 = new Track.TrackBuilder()
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
            assertTrue(V1.appendChild(C1));
            assertTrue(V1.appendChild(C2));
            assertTrue(V1.appendChild(C3));
            assertTrue(V1.appendChild(C4));
            assertTrue(stack.appendChild(V1));
            timeline.setTracks(stack);

            //testing full time range
            List<Composable> composable_fullTimeRange = Arrays.asList(V1, C1, C2, C3, C4);
            List<Composable> result_fullTimeRange = timeline.childrenIf(Composable.class, Optional.empty(), false);
            assertEquals(composable_fullTimeRange.size(), result_fullTimeRange.size());
            for(int i = 0; i < composable_fullTimeRange.size(); i++){
                assertTrue((result_fullTimeRange.get(i)).isEquivalentTo(composable_fullTimeRange.get(i)));
            }

            //testing trimmed time range
            TimeRange search_range = new TimeRange(new RationalTime(0, 24), new RationalTime(20, 24));
            List<Composable> composable_trimmedTimeRange = Arrays.asList(V1, C1, C2);
            List<Composable> result_trimmedTimeRange = timeline.childrenIf(Composable.class ,Optional.of(search_range), false);
            assertEquals(composable_trimmedTimeRange.size(), result_trimmedTimeRange.size());
            for(int i = 0; i < composable_trimmedTimeRange.size(); i++){
                assertTrue((result_trimmedTimeRange.get(i)).isEquivalentTo(composable_trimmedTimeRange.get(i)));
            }
        }
    }

    @Test
    public void testChildrenIfClipEquality() throws Exception{
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack stack = new Stack.StackBuilder().build();
                Track V1 = new Track.TrackBuilder()
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
            assertTrue(V1.appendChild(C1));
            assertTrue(V1.appendChild(C2));
            assertTrue(V1.appendChild(C3));
            assertTrue(V1.appendChild(C4));
            assertTrue(stack.appendChild(V1));
            timeline.setTracks(stack);

            //testing full time range
            List<Clip> clips_fullTimeRange = Arrays.asList(C1, C2, C3, C4);
            List<Clip> result_fullTimeRange = timeline.childrenIf(Clip.class, Optional.empty(), false);
            assertEquals(clips_fullTimeRange.size(), result_fullTimeRange.size());
            for(int i = 0; i < clips_fullTimeRange.size(); i++){
                assertTrue((result_fullTimeRange.get(i)).isEquivalentTo(clips_fullTimeRange.get(i)));
            }

            //testing trimmed time range
            TimeRange search_range = new TimeRange(new RationalTime(0, 24), new RationalTime(20, 24));
            List<Clip> clips_trimmedTimeRange = Arrays.asList(C1, C2);
            List<Clip> result_trimmedTimeRange = timeline.childrenIf(Clip.class, Optional.of(search_range), false);
            assertEquals(clips_trimmedTimeRange.size(), result_trimmedTimeRange.size());
            for(int i = 0; i < clips_trimmedTimeRange.size(); i++){
                assertTrue((result_trimmedTimeRange.get(i)).isEquivalentTo(clips_trimmedTimeRange.get(i)));
            }
        }

    }

    @Test
    public void testChildrenIfTrackEquality() throws Exception{
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack topLevelStack = new Stack.StackBuilder().build();
                Track topLevelTrack = new Track.TrackBuilder()
                        .setName("Top_track")
                        .setKind(Track.Kind.video)
                        .build();
                Track V1 = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Track V2 = new Track.TrackBuilder()
                        .setName("V2")
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
            assertTrue(V1.appendChild(C1));
            assertTrue(V1.appendChild(C2));
            assertTrue(V1.appendChild(C3));
            assertTrue(V2.appendChild(C4));
            assertTrue(topLevelTrack.appendChild(V1));
            assertTrue(topLevelTrack.appendChild(V2));
            assertTrue(topLevelStack.appendChild(topLevelTrack));
            timeline.setTracks(topLevelStack);

            //testing full time range
            List<Track> track_fullTimeRange = Arrays.asList(topLevelTrack, V1, V2);
            List<Track> result_fullTimeRange = timeline.childrenIf(Track.class, Optional.empty(), false);
            assertEquals(track_fullTimeRange.size(), result_fullTimeRange.size());
            for(int i = 0; i < track_fullTimeRange.size(); i++){
                assertTrue((result_fullTimeRange.get(i)).isEquivalentTo(track_fullTimeRange.get(i)));
            }

            //testing trimmed time range
            TimeRange search_range = new TimeRange(new RationalTime(0, 24), new RationalTime(20, 24));
            List<Track> track_trimmedTimeRange = Arrays.asList(topLevelTrack, V1);
            List<Track> result_trimmedTimeRange = timeline.childrenIf(Track.class, Optional.of(search_range), false);
            assertEquals(track_trimmedTimeRange.size(), result_trimmedTimeRange.size());
            for(int i = 0; i < track_trimmedTimeRange.size(); i++){
                assertTrue((result_trimmedTimeRange.get(i)).isEquivalentTo(track_trimmedTimeRange.get(i)));
            }
        }
    }
    @Test
    public void testChildrenIfGapEquality() throws Exception{
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack stack = new Stack.StackBuilder().build();
                Track V1 = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Gap G1 = new Gap.GapBuilder()
                        .setName("test gap1")
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(10, 24)).build())
                        .build();
                Gap G2 = new Gap.GapBuilder()
                        .setName("test gap2")
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(10, 24)).build())
                        .build();
                Gap G3 = new Gap.GapBuilder()
                        .setName("test gap2")
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(10, 24)).build())
                        .build();
                Gap G4 = new Gap.GapBuilder()
                        .setName("test gap2")
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(10, 24)).build())
                        .build();

        )
        {
            assertTrue(V1.appendChild(G1));
            assertTrue(V1.appendChild(G2));
            assertTrue(V1.appendChild(G3));
            assertTrue(V1.appendChild(G4));
            assertTrue(stack.appendChild(V1));
            timeline.setTracks(stack);

            //testing full time range
            List<Gap> gap_fullTimeRange = Arrays.asList(G1, G2, G3, G4);
            List<Gap> result_fullTimeRange = timeline.childrenIf(Gap.class, Optional.empty(), false);
            assertEquals(gap_fullTimeRange.size(), result_fullTimeRange.size());
            for(int i = 0; i < gap_fullTimeRange.size(); i++){
                assertTrue((result_fullTimeRange.get(i)).isEquivalentTo(gap_fullTimeRange.get(i)));
            }

            //testing trimmed time range
            TimeRange search_range = new TimeRange(new RationalTime(0, 24), new RationalTime(20, 24));
            List<Gap> gap_trimmedTimeRange = Arrays.asList(G1, G2);
            List<Gap> result_trimmedTimeRange = timeline.childrenIf(Gap.class, Optional.of(search_range), false);
            assertEquals(gap_trimmedTimeRange.size(), result_trimmedTimeRange.size());
            for(int i = 0; i < gap_trimmedTimeRange.size(); i++){
                assertTrue((result_trimmedTimeRange.get(i)).isEquivalentTo(gap_trimmedTimeRange.get(i)));
            }
        }
    }

    @Test
    public void testChildrenIfStackEquality() throws Exception{
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack topLevelStack = new Stack.StackBuilder().build();
                Track topLevelTrack = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Stack S1 = new Stack.StackBuilder().build();
                Stack S2 = new Stack.StackBuilder().build();
                Track V1 = new Track.TrackBuilder()
                        .setName("V2")
                        .setKind(Track.Kind.video)
                        .build();
                Track V2 = new Track.TrackBuilder()
                        .setName("V3")
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


        )
        {
            assertTrue(V1.appendChild(C1));
            assertTrue(V2.appendChild(C2));
            assertTrue(S1.appendChild(V1));
            assertTrue(S2.appendChild(V2));
            assertTrue(topLevelTrack.appendChild(S1));
            assertTrue(topLevelTrack.appendChild(S2));
            assertTrue(topLevelStack.appendChild(topLevelTrack));
            timeline.setTracks(topLevelStack);

            //testing full time range
            List<Stack> stack_fullTimeRange = Arrays.asList(S1, S2);
            List<Stack> result_fullTimeRange = timeline.childrenIf(Stack.class, Optional.empty(), false);
            assertEquals(stack_fullTimeRange.size(), result_fullTimeRange.size());
            for(int i = 0; i < stack_fullTimeRange.size(); i++){
                assertTrue((result_fullTimeRange.get(i)).isEquivalentTo(stack_fullTimeRange.get(i)));
            }

            //testing trimmed time range
            TimeRange search_range = new TimeRange(new RationalTime(0, 24), new RationalTime(10, 24));
            List<Stack> stack_trimmedTimeRange = Arrays.asList(S1);
            List<Stack> result_trimmedTimeRange = timeline.childrenIf(Stack.class, Optional.of(search_range), false);
            assertEquals(stack_trimmedTimeRange.size(), result_trimmedTimeRange.size());
            for(int i = 0; i < stack_trimmedTimeRange.size(); i++){
                assertTrue((result_trimmedTimeRange.get(i)).isEquivalentTo(stack_trimmedTimeRange.get(i)));
            }


        }
    }

    @Test
    public void testChildrenIfCompositionEquality() throws Exception{
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack topLevelStack = new Stack.StackBuilder().build();
                Track topLevelTrack = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Stack S1 = new Stack.StackBuilder().build();
                Stack S2 = new Stack.StackBuilder().build();
                Track V1 = new Track.TrackBuilder()
                        .setName("V2")
                        .setKind(Track.Kind.video)
                        .build();
                Track V2 = new Track.TrackBuilder()
                        .setName("V3")
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

        )
        {
            assertTrue(V1.appendChild(C1));
            assertTrue(V2.appendChild(C2));
            assertTrue(S1.appendChild(V1));
            assertTrue(S2.appendChild(V2));
            assertTrue(topLevelTrack.appendChild(S1));
            assertTrue(topLevelTrack.appendChild(S2));
            assertTrue(topLevelStack.appendChild(topLevelTrack));
            timeline.setTracks(topLevelStack);

            //testing full time range
            List<Composition> composition_fullTimeRange = Arrays.asList(topLevelTrack, S1, V1, S2, V2);
            List<Composition> result_fullTimeRange = timeline.childrenIf(Composition.class, Optional.empty(), false);
            assertEquals(composition_fullTimeRange.size(), result_fullTimeRange.size());
            for(int i = 0; i < composition_fullTimeRange.size(); i++){
                assertTrue((result_fullTimeRange.get(i)).isEquivalentTo(composition_fullTimeRange.get(i)));
            }

            //testing trimmed time range
            TimeRange search_range = new TimeRange(new RationalTime(0, 24), new RationalTime(10, 24));
            List<Composition> composition_trimmedTimeRange = Arrays.asList(topLevelTrack, S1, V1);
            List<Composition> result_trimmedTimeRange = timeline.childrenIf(Composition.class, Optional.of(search_range), false);
            assertEquals(composition_trimmedTimeRange.size(), result_trimmedTimeRange.size());
            for(int i = 0; i < composition_trimmedTimeRange.size(); i++){
                assertTrue((result_trimmedTimeRange.get(i)).isEquivalentTo(composition_trimmedTimeRange.get(i)));
            }

        }

    }

    @Test
    public void testChildrenIfItemEquality() throws Exception{
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack topLevelStack = new Stack.StackBuilder().build();
                Track topLevelTrack = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Track V1 = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Track V2 = new Track.TrackBuilder()
                        .setName("V2")
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
                Gap G1 = new Gap.GapBuilder()
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(10, 24)).build())
                        .build();

        )
        {
            assertTrue(V1.appendChild(C1));
            assertTrue(V1.appendChild(G1));
            assertTrue(V2.appendChild(C2));
            assertTrue(topLevelTrack.appendChild(V1));
            assertTrue(topLevelTrack.appendChild(V2));
            assertTrue(topLevelStack.appendChild(topLevelTrack));
            timeline.setTracks(topLevelStack);

            //testing full time range
            List<Item> item_fullTimeRange = Arrays.asList(topLevelTrack, V1, C1, G1, V2, C2);
            List<Item> result_fullTimeRange = timeline.childrenIf(Item.class, Optional.empty(), false);
            assertEquals(item_fullTimeRange.size(), result_fullTimeRange.size());
            for(int i = 0; i < item_fullTimeRange.size(); i++){
                assertTrue((result_fullTimeRange.get(i)).isEquivalentTo(item_fullTimeRange.get(i)));
            }

            //testing trimmed time range
            TimeRange search_range = new TimeRange(new RationalTime(0, 24), new RationalTime(20, 24));
            List<Item> item_trimmedTimeRange = Arrays.asList(topLevelTrack, V1, C1, G1);
            List<Item> result_trimmedTimeRange = timeline.childrenIf(Item.class, Optional.of(search_range), false);
            assertEquals(item_trimmedTimeRange.size(), result_trimmedTimeRange.size());
            for(int i = 0; i < item_trimmedTimeRange.size(); i++){
                assertTrue((result_trimmedTimeRange.get(i)).isEquivalentTo(item_trimmedTimeRange.get(i)));
            }


        }

    }

    @Test
    public void testChildrenIfTransitionEquality() throws Exception{
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack topLevelStack = new Stack.StackBuilder().build();
                Track topLevelTrack = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Track V1 = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Track V2 = new Track.TrackBuilder()
                        .setName("V2")
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
                Transition transition1 = new Transition.TransitionBuilder().build();
                Transition transition2 = new Transition.TransitionBuilder().build();


        )
        {
            assertTrue(V1.appendChild(transition1));
            assertTrue(V1.appendChild(C1));
            assertTrue(V2.appendChild(transition2));
            assertTrue(V2.appendChild(C2));
            assertTrue(topLevelTrack.appendChild(V1));
            assertTrue(topLevelTrack.appendChild(V2));
            assertTrue(topLevelStack.appendChild(topLevelTrack));
            timeline.setTracks(topLevelStack);

            //testing full time range
            List<Transition> transition_fullTimeRange = Arrays.asList(transition1, transition2);
            List<Transition> result_fullTimeRange = timeline.childrenIf(Transition.class, Optional.empty(), false);
            assertEquals(transition_fullTimeRange.size(), result_fullTimeRange.size());
            for(int i = 0; i < transition_fullTimeRange.size(); i++){
                assertTrue((result_fullTimeRange.get(i)).isEquivalentTo(transition_fullTimeRange.get(i)));
            }

            //testing trimmed time range
            TimeRange search_range = new TimeRange(new RationalTime(0, 24), new RationalTime(10, 24));
            List<Transition> transition_trimmedTimeRange = Arrays.asList(transition1);
            List<Transition> result_trimmedTimeRange = timeline.childrenIf(Transition.class, Optional.of(search_range), false);
            assertEquals(transition_trimmedTimeRange.size(), result_trimmedTimeRange.size());
            for(int i = 0; i < transition_trimmedTimeRange.size(); i++){
                assertTrue((result_trimmedTimeRange.get(i)).isEquivalentTo(transition_trimmedTimeRange.get(i)));
            }
        }
    }

    @Test
    public void testChildrenIfNullTimeRange() throws Exception{
        try(Timeline timeline = new Timeline.TimelineBuilder().build();)
        {
            assertThrows(NullPointerException.class,
                    ()->{timeline.childrenIf(Composable.class, null, false);});
        }
    }

    @Test
    public void testClipIfNullTimeRange() throws Exception{
        try(Timeline timeline = new Timeline.TimelineBuilder().build();)
        {
            assertThrows(NullPointerException.class,
                    ()->{timeline.clipIf(null, false);});
        }
    }

    @Test
    public void testClipIfEquality() throws Exception{
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack stack = new Stack.StackBuilder().build();
                Track V1 = new Track.TrackBuilder()
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
            assertTrue(V1.appendChild(C1));
            assertTrue(V1.appendChild(C2));
            assertTrue(V1.appendChild(C3));
            assertTrue(V1.appendChild(C4));
            assertTrue(stack.appendChild(V1));
            timeline.setTracks(stack);

            //testing full time range
            List<Clip> clips_fullTimeRange = Arrays.asList(C1, C2, C3, C4);
            List<Clip> result_fullTimeRange = timeline.clipIf(Optional.empty(), false);
            assertEquals(clips_fullTimeRange.size(), result_fullTimeRange.size());
            for(int i = 0; i < clips_fullTimeRange.size(); i++){
                assertTrue((result_fullTimeRange.get(i)).isEquivalentTo(clips_fullTimeRange.get(i)));
            }

            //testing trimmed time range
            TimeRange search_range = new TimeRange(new RationalTime(0, 24), new RationalTime(20, 24));
            List<Clip> clips_trimmedTimeRange = Arrays.asList(C1, C2);
            List<Clip> result_trimmedTimeRange = timeline.clipIf(Optional.of(search_range), false);
            assertEquals(clips_trimmedTimeRange.size(), result_trimmedTimeRange.size());
            for(int i = 0; i < clips_trimmedTimeRange.size(); i++){
                assertTrue((result_trimmedTimeRange.get(i)).isEquivalentTo(clips_trimmedTimeRange.get(i)));
            }
        }

    }

}
