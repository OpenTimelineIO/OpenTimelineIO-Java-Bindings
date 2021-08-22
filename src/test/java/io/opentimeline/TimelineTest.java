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
    public void testChildrenIfComposableEquality(){
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack stack = new Stack.StackBuilder().build();
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
                                new RationalTime(0, 2),
                                new RationalTime(50, 15)))
                        .setTargetURL("/var/tmp/test.mov")
                        .build();
                Clip C1 = new Clip.ClipBuilder()
                        .setName("test clip1")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(5, 24)).build())
                        .build();
                Clip C2 = new Clip.ClipBuilder()
                        .setName("test clip2")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(5, 24)).build())
                        .build();
                )
        {
            assertTrue(V1.appendChild(C1));
            assertTrue(V2.appendChild(C2));
            assertTrue(stack.appendChild(V1));
            assertTrue(stack.appendChild(V2));
            timeline.setTracks(stack);
            List<Composable> composableChildrenList = Arrays.asList(V1,C1,V2,C2);
            TimeRange search_range = new TimeRange(
                    new RationalTime(0, 1),
                    new RationalTime(40, 1));
            List<Composable> result = timeline.childrenIf(Composable.class,search_range, false);
            assertEquals(composableChildrenList.size(), result.size());
            for(int i = 0; i < composableChildrenList.size(); i++){
                assertTrue((result.get(i)).isEquivalentTo(composableChildrenList.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testChildrenIfClipEquality(){
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack stack = new Stack.StackBuilder().build();
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
                                new RationalTime(0, 2),
                                new RationalTime(50, 15)))
                        .setTargetURL("/var/tmp/test.mov")
                        .build();
                Clip C1 = new Clip.ClipBuilder()
                        .setName("test clip1")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(5, 24)).build())
                        .build();
                Clip C2 = new Clip.ClipBuilder()
                        .setName("test clip2")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(5, 24)).build())
                        .build();
        )
        {
            assertTrue(V1.appendChild(C1));
            assertTrue(V2.appendChild(C2));
            assertTrue(stack.appendChild(V1));
            assertTrue(stack.appendChild(V2));
            timeline.setTracks(stack);
            List<Clip> clipChildrenList = Arrays.asList(C1, C2);
            TimeRange search_range = new TimeRange(
                    new RationalTime(0, 1),
                    new RationalTime(40, 1));
            List<Clip> result = timeline.childrenIf(Clip.class,search_range, false);
            assertEquals(clipChildrenList.size(), result.size());
            for(int i = 0; i < clipChildrenList.size(); i++){
                assertTrue((result.get(i)).isEquivalentTo(clipChildrenList.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testChildrenIfTrackEquality(){
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack stack = new Stack.StackBuilder().build();
                Track V1 = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Track V2 = new Track.TrackBuilder()
                        .setName("V2")
                        .setKind(Track.Kind.video)
                        .build();
        )
        {
            assertTrue(stack.appendChild(V1));
            assertTrue(stack.appendChild(V2));
            timeline.setTracks(stack);
            List<Track> trackChildrenList = Arrays.asList(V1, V2);
            TimeRange search_range = new TimeRange(
                    new RationalTime(0, 1),
                    new RationalTime(40, 1));
            List<Track> result = timeline.childrenIf(Track.class,search_range, false);
            assertEquals(trackChildrenList.size(), result.size());
            for(int i = 0; i < trackChildrenList.size(); i++){
                assertTrue((result.get(i)).isEquivalentTo(trackChildrenList.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Test
    public void testChildrenIfGapEquality(){
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack stack = new Stack.StackBuilder().build();
                Track V1 = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Track V2 = new Track.TrackBuilder()
                        .setName("V2")
                        .setKind(Track.Kind.video)
                        .build();
                Gap G1 = new Gap.GapBuilder().build();
                Gap G2 = new Gap.GapBuilder().build();

        )
        {
            assertTrue(V1.appendChild(G1));
            assertTrue(V2.appendChild(G2));
            assertTrue(stack.appendChild(V1));
            assertTrue(stack.appendChild(V2));
            timeline.setTracks(stack);
            List<Gap> gapChildrenList = Arrays.asList(G1, G2);
            TimeRange search_range = new TimeRange(
                    new RationalTime(0, 1),
                    new RationalTime(40, 1));
            List<Gap> result = timeline.childrenIf(Gap.class,search_range, false);
            assertEquals(gapChildrenList.size(), result.size());
            for(int i = 0; i < gapChildrenList.size(); i++){
                assertTrue((result.get(i)).isEquivalentTo(gapChildrenList.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testChildrenIfStackEquality(){
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack topLevelStack = new Stack.StackBuilder().build();
                Track V1 = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Stack stackChild1 = new Stack.StackBuilder().build();
                Stack stackChild2 = new Stack.StackBuilder().build();
                Track V2 = new Track.TrackBuilder()
                        .setName("V2")
                        .setKind(Track.Kind.video)
                        .build();
                Track V3 = new Track.TrackBuilder()
                        .setName("V3")
                        .setKind(Track.Kind.video)
                        .build();

        )
        {
            //nested stack within track
            assertTrue(V2.appendChild(stackChild1));
            assertTrue(V3.appendChild(stackChild2));
            assertTrue(topLevelStack.appendChild(V1));
            assertTrue(topLevelStack.appendChild(V2));
            assertTrue(topLevelStack.appendChild(V3));
            timeline.setTracks(topLevelStack);
            List<Stack> stackChildrenList = Arrays.asList(stackChild1, stackChild2);
            TimeRange search_range = new TimeRange(
                    new RationalTime(0, 1),
                    new RationalTime(40, 1));
            List<Stack> result = timeline.childrenIf(Stack.class,search_range, false);
            assertEquals(stackChildrenList.size(), result.size());
            for(int i = 0; i < stackChildrenList.size(); i++){
                assertTrue((result.get(i)).isEquivalentTo(stackChildrenList.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testChildrenIfCompositionEquality(){
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack topLevelStack = new Stack.StackBuilder().build();
                Track V1 = new Track.TrackBuilder()
                        .setName("V1")
                        .setKind(Track.Kind.video)
                        .build();
                Stack stackChild1 = new Stack.StackBuilder().build();
                Track V2 = new Track.TrackBuilder()
                        .setName("V2")
                        .setKind(Track.Kind.video)
                        .build();

        )
        {
            assertTrue(V2.appendChild(stackChild1));
            assertTrue(topLevelStack.appendChild(V1));
            assertTrue(topLevelStack.appendChild(V2));
            timeline.setTracks(topLevelStack);
            List<Composition> compositionChildrenList = Arrays.asList(V1, V2, stackChild1);
            TimeRange search_range = new TimeRange(
                    new RationalTime(0, 1),
                    new RationalTime(40, 1));
            List<Composition> result = timeline.childrenIf(Composition.class,search_range, false);
            assertEquals(compositionChildrenList.size(), result.size());
            for(int i = 0; i < compositionChildrenList.size(); i++){
                assertTrue((result.get(i)).isEquivalentTo(compositionChildrenList.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testChildrenIfItemEquality(){
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack stack = new Stack.StackBuilder().build();
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
                                new RationalTime(0, 2),
                                new RationalTime(50, 15)))
                        .setTargetURL("/var/tmp/test.mov")
                        .build();
                Clip C1 = new Clip.ClipBuilder()
                        .setName("test clip1")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(5, 24)).build())
                        .build();
                Clip C2 = new Clip.ClipBuilder()
                        .setName("test clip2")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(5, 24)).build())
                        .build();
                Gap G1 = new Gap.GapBuilder().build();

        )
        {
            assertTrue(V1.appendChild(C1));
            assertTrue(V1.appendChild(G1));
            assertTrue(V2.appendChild(C2));
            assertTrue(stack.appendChild(V1));
            assertTrue(stack.appendChild(V2));
            timeline.setTracks(stack);
            List<Item> itemChildrenList = Arrays.asList(V1, C1, G1, V2, C2);
            TimeRange search_range = new TimeRange(
                    new RationalTime(0, 1),
                    new RationalTime(40, 1));
            List<Item> result = timeline.childrenIf(Item.class,search_range, false);
            assertEquals(itemChildrenList.size(), result.size());
            for(int i = 0; i < itemChildrenList.size(); i++){
                assertTrue((result.get(i)).isEquivalentTo(itemChildrenList.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testChildrenIfTransitionEquality(){
        try(
                Timeline timeline = new Timeline.TimelineBuilder().build();
                Stack stack = new Stack.StackBuilder().build();
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
                                new RationalTime(0, 2),
                                new RationalTime(50, 15)))
                        .setTargetURL("/var/tmp/test.mov")
                        .build();
                Clip C1 = new Clip.ClipBuilder()
                        .setName("test clip1")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(5, 24)).build())
                        .build();
                Clip C2 = new Clip.ClipBuilder()
                        .setName("test clip2")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(4, 10)).build())
                        .build();
                Clip C3 = new Clip.ClipBuilder()
                        .setName("test clip3")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(2, 20)).build())
                        .build();
                Clip C4 = new Clip.ClipBuilder()
                        .setName("test clip4")
                        .setMediaReference(mr)
                        .setSourceRange(new TimeRange.TimeRangeBuilder().setDuration(new RationalTime(3, 15)).build())
                        .build();
                Transition transition1 = new Transition.TransitionBuilder().build();
                Transition transition2 = new Transition.TransitionBuilder().build();


        )
        {
            assertTrue(V1.appendChild(C1));
            assertTrue(V1.appendChild(C2));
            assertTrue(V2.appendChild(C3));
            assertTrue(V2.appendChild(C4));
            assertTrue(V1.appendChild(transition1));
            assertTrue(V2.appendChild(transition2));
            assertTrue(stack.appendChild(V1));
            assertTrue(stack.appendChild(V2));
            timeline.setTracks(stack);
            List<Transition> transitionsChildrenList = Arrays.asList(transition1, transition2);
            TimeRange search_range = new TimeRange(
                    new RationalTime(0, 1),
                    new RationalTime(40, 1));
            List<Transition> result = timeline.childrenIf(Transition.class,search_range, false);
            assertEquals(transitionsChildrenList.size(), result.size());
            for(int i = 0; i < transitionsChildrenList.size(); i++){
                assertTrue((result.get(i)).isEquivalentTo(transitionsChildrenList.get(i)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testChildrenIfNullTimeRange(){
        try(Timeline timeline = new Timeline.TimelineBuilder().build();)
        {
            assertThrows(NullPointerException.class,
                    ()->{timeline.childrenIf(Composable.class, null, false);});
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
