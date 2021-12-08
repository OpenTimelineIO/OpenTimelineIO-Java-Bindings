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

public class StackTest {

    @Test
    public void testConstructor() {
        Stack stack = new Stack.StackBuilder()
                .setName("test")
                .build();
        assertEquals(stack.getName(), "test");
    }

    @Test
    public void testSerialize() throws OpenTimelineIOException {
        Stack stack = new Stack.StackBuilder()
                .setName("test")
                .build();
        Clip clip = new Clip.ClipBuilder()
                .setName("testClip")
                .build();
        assertTrue(stack.appendChild(clip));

        String encoded = stack.toJSONString();
        SerializableObject decoded = SerializableObject.fromJSONString(encoded);
        assertEquals(decoded, stack);
        Stack decodedStack = (Stack) decoded;
        assertNotNull(decodedStack.getChildren().get(0).parent());
    }

    @Test
    public void testTrimRangeChild() {

        Track track = new Track.TrackBuilder()
                .setName("foo")
                .build();
        Stack stack = new Stack.StackBuilder()
                .setName("foo")
                .build();

        Composition[] co = {track, stack};

        for (Composition st : co) {
            st.setSourceRange(
                    new TimeRange(
                            new RationalTime(100, 24),
                            new RationalTime(50, 24)));
            TimeRange r = new TimeRange(
                    new RationalTime(110, 24),
                    new RationalTime(30, 24));
            assertEquals(st.trimChildRange(r), r);
            r = new TimeRange(
                    new RationalTime(0, 24),
                    new RationalTime(30, 24));
            assertNull(st.trimChildRange(r));
            r = new TimeRange(
                    new RationalTime(1000, 24),
                    new RationalTime(30, 24));
            assertNull(st.trimChildRange(r));
            r = new TimeRange(
                    new RationalTime(90, 24),
                    new RationalTime(30, 24));
            assertEquals(st.trimChildRange(r),
                    new TimeRange(
                            new RationalTime(100, 24),
                            new RationalTime(20, 24)));
            r = new TimeRange(
                    new RationalTime(110, 24),
                    new RationalTime(50, 24));
            assertEquals(st.trimChildRange(r),
                    new TimeRange(
                            new RationalTime(110, 24),
                            new RationalTime(40, 24)));
            r = new TimeRange(
                    new RationalTime(90, 24),
                    new RationalTime(1000, 24));
            assertEquals(st.trimChildRange(r), st.getSourceRange());
        }

    }

    @Test
    public void testRangeOfChild() throws ChildAlreadyParentedException, CannotComputeAvailableRangeException, NotAChildException, ObjectWithoutDurationException {
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
        ArrayList<Composable> children = new ArrayList<>();
        children.add(clip1);
        children.add(clip2);
        children.add(clip3);
        Stack stack = new Stack.StackBuilder()
                .setName("foo")
                .build();
        stack.setChildren(children);

        assertEquals(stack.getDuration(), new RationalTime(50, 24));

        assertEquals(
                stack.rangeOfChildAtIndex(0)
                        .getStartTime(), new RationalTime());
        assertEquals(
                stack.rangeOfChildAtIndex(1)
                        .getStartTime(), new RationalTime());
        assertEquals(
                stack.rangeOfChildAtIndex(2)
                        .getStartTime(), new RationalTime());

        assertEquals(
                stack.rangeOfChildAtIndex(0)
                        .getDuration(), new RationalTime(50, 24));
        assertEquals(
                stack.rangeOfChildAtIndex(1)
                        .getDuration(), new RationalTime(50, 24));
        assertEquals(
                stack.rangeOfChildAtIndex(2)
                        .getDuration(), new RationalTime(50, 24));

        assertEquals(stack.rangeOfChildAtIndex(2)
                , stack.getRangeOfChild(
                        stack.getChildren().get(2)));
    }

    @Test
    public void testRangeOfChildWithDuration() throws NotAChildException, ChildAlreadyParentedException, ObjectWithoutDurationException, CannotComputeAvailableRangeException, InvalidTimeRangeException {

        TimeRange stSourceRange = new TimeRange(
                new RationalTime(5, 24),
                new RationalTime(5, 24));

        Stack st = new Stack.StackBuilder()
                .setName("foo")
                .setSourceRange(stSourceRange)
                .build();
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
        ArrayList<Composable> children = new ArrayList<>();
        children.add(clip1);
        children.add(clip2);
        children.add(clip3);
        st.setChildren(children);

        // getRangeOfChild always returns the pre-trimmed range
        // To get post-trim range call getTrimmedRangeOfChild
        assertEquals(st.getRangeOfChild(st.getChildren().get(0))
                , new TimeRange(
                        new RationalTime(0, 24),
                        new RationalTime(50, 24)));

        assertEquals(st.getTransformedTime(
                new RationalTime(25, 24),
                (Item) st.getChildren().get(0)), new RationalTime(125, 24));
        assertEquals(
                (((Clip) st.getChildren().get(0)).getTransformedTime(
                        new RationalTime(125, 24),
                        st)), new RationalTime(25, 24));

        assertEquals(st.trimmedRangeOfChildAtIndex(0), st.getSourceRange());

        assertEquals(((Clip) st.getChildren().get(0))
                        .getTrimmedRangeInParent(),
                st.getTrimmedRangeOfChild(st.getChildren().get(0)));

        // same test but via iteration
        for (int i = 0; i < st.getChildren().size(); i++) {
            assertEquals(
                    ((Clip) st.getChildren().get(i))
                            .getTrimmedRangeInParent(),
                    st.getTrimmedRangeOfChild(st.getChildren().get(i)));
        }
    }

    @Test
    public void testTransformedTime() throws ChildAlreadyParentedException, NotAChildException, ObjectWithoutDurationException, CannotComputeAvailableRangeException{

        TimeRange stSourceRange = new TimeRange(
                new RationalTime(5, 24),
                new RationalTime(5, 24));

        Stack st = new Stack.StackBuilder()
                .setName("foo")
                .setSourceRange(stSourceRange)
                .build();
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
        List<Composable> children = new ArrayList<>();
        children.add(clip1);
        children.add(clip2);
        children.add(clip3);
        st.setChildren(children);
        children = st.getChildren();
        assertEquals(children.get(0).getName(), "clip1");
        assertEquals(children.get(1).getName(), "clip2");
        assertEquals(children.get(2).getName(), "clip3");

        RationalTime testTime = new RationalTime(0, 24);
        assertEquals(
                st.getTransformedTime(testTime, clip1),
                new RationalTime(100, 24));

        // ensure that transformed_time does not edit in place
        assertEquals(testTime, new RationalTime(0, 24));

        assertEquals(
                st.getTransformedTime(new RationalTime(0, 24), clip2),
                new RationalTime(101, 24));
        assertEquals(
                st.getTransformedTime(new RationalTime(0, 24), clip3),
                new RationalTime(102, 24));

        assertEquals(
                st.getTransformedTime(new RationalTime(50, 24), clip1),
                new RationalTime(150, 24));
        assertEquals(
                st.getTransformedTime(new RationalTime(50, 24), clip2),
                new RationalTime(151, 24));
        assertEquals(
                st.getTransformedTime(new RationalTime(50, 24), clip3),
                new RationalTime(152, 24));

        assertEquals(
                clip1.getTransformedTime(new RationalTime(100, 24), st),
                new RationalTime(0, 24));
        assertEquals(
                clip2.getTransformedTime(new RationalTime(101, 24), st),
                new RationalTime(0, 24));
        assertEquals(
                clip3.getTransformedTime(new RationalTime(102, 24), st),
                new RationalTime(0, 24));

        assertEquals(
                clip1.getTransformedTime(new RationalTime(150, 24), st),
                new RationalTime(50, 24));
        assertEquals(
                clip2.getTransformedTime(new RationalTime(151, 24), st),
                new RationalTime(50, 24));
        assertEquals(
                clip3.getTransformedTime(new RationalTime(152, 24), st),
                new RationalTime(50, 24));
    }

    @Test
    public void testClipIfNullTimeRange() throws Exception{
        try(Stack stack = new Stack.StackBuilder().build();)
        {
            assertThrows(NullPointerException.class,
                    ()->{stack.clipIf(null, false);});
        }
    }

    @Test
    public void testClipIfEquality() throws Exception{
        try(
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
            List<Clip> clipChildrenList = Arrays.asList(C1, C2);
            TimeRange search_range = new TimeRange(
                    new RationalTime(0, 1),
                    new RationalTime(40, 1));
            List<Clip> result = stack.clipIf(search_range, false);
            assertEquals(clipChildrenList.size(), result.size());
            for(int i = 0; i < clipChildrenList.size(); i++){
                assertTrue((result.get(i)).isEquivalentTo(clipChildrenList.get(i)));
            }
        }
    }
}
