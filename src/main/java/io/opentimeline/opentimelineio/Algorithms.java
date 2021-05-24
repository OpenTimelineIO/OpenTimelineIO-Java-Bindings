// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio;

import io.opentimeline.opentime.TimeRange;
import io.opentimeline.opentimelineio.exception.OpenTimelineIOException;

import java.util.List;

public class Algorithms {

    /**
     * Flatten a Stack, into a single Track.
     * Note that the 1st Track is the bottom one, and the last is the top.
     *
     * @param inStack     stack to flatten
     * @return flattened track
     */
    public native Track flattenStack(Stack inStack) throws OpenTimelineIOException;

    /**
     * Flatten a Stack, into a single Track.
     * Note that the 1st Track is the bottom one, and the last is the top.
     *
     * @param tracks      list of tracks to flatten
     * @return flattened track
     */
    public Track flattenStack(List<Track> tracks) throws OpenTimelineIOException{
        Track[] trackArray = new Track[tracks.size()];
        trackArray = tracks.toArray(trackArray);
        return flattenStackNative(trackArray);
    }

    private native Track flattenStackNative(Track[] tracks) throws OpenTimelineIOException;

    /**
     * Returns a new track that is a copy of the inTrack, but with items
     * outside the trimRange removed and items on the ends trimmed to the
     * trimRange. Note that the track is never expanded, only shortened.
     * Please note that you could do nearly the same thing non-destructively by
     * just setting the Track's source_range but sometimes you want to really cut
     * away the stuff outside and that's what this function is meant for.
     *
     * @param inTrack     track to be trimmed
     * @param trimRange   this is the range, which the track will be trimmed to
     * @return trimmed track
     */
    public native Track trackTrimmedToRange(Track inTrack, TimeRange trimRange) throws OpenTimelineIOException;

    /**
     * Returns a new timeline that is a copy of the inTimeline, but with items
     * outside the trimRange removed and items on the ends trimmed to the
     * trimRange. Note that the timeline is never expanded, only shortened.
     * Please note that you could do nearly the same thing non-destructively by
     * just setting the Track's sourceRange but sometimes you want to really cut
     * away the stuff outside and that's what this function is meant for.
     *
     * @param inTimeline  timeline to be trimmed
     * @param trimRange   this is the range, which the timeline will be trimmed to
     * @return trimmed timeline
     */
    public Timeline timelineTrimmedToRange(Timeline inTimeline, TimeRange trimRange) throws OpenTimelineIOException {
        Timeline newTimeline = (Timeline) inTimeline.deepCopy();
        Stack stack = inTimeline.getTracks();
        Stack newStack = newTimeline.getTracks();
        List<Composable> tracks = stack.getChildren();
        for (int i = 0; i < tracks.size(); i++) {
            Track trimmedTrack = this.trackTrimmedToRange((Track) tracks.get(i), trimRange);
            newStack.setChild(i, trimmedTrack);
        }
        return newTimeline;
    }
}
