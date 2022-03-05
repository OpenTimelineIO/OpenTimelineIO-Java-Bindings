// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio;

import io.opentimeline.OTIONative;
import io.opentimeline.opentime.RationalTime;
import io.opentimeline.opentime.TimeRange;
import io.opentimeline.opentimelineio.exception.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An Item is a Composable that can be part of a Composition or Timeline.
 * More specifically, it is a Composable that has meaningful duration.
 * Can also hold effects and markers.
 */
public class Item extends Composable {

    protected Item() {
    }

    Item(OTIONative otioNative) {
        this.nativeManager = otioNative;
    }

    public Item(
            String name,
            TimeRange sourceRange,
            AnyDictionary metadata,
            List<Effect> effects,
            List<Marker> markers,
            boolean enabled) {
        this.initObject(
                name,
                sourceRange,
                metadata,
                effects,
                markers,
                enabled);
    }

    @Deprecated
    public Item(
            String name,
            TimeRange sourceRange,
            AnyDictionary metadata,
            List<Effect> effects,
            List<Marker> markers) {
        this.initObject(
                name,
                sourceRange,
                metadata,
                effects,
                markers,
                true);
    }

    public Item(Item.ItemBuilder builder) {
        this.initObject(
                builder.name,
                builder.sourceRange,
                builder.metadata,
                builder.effects,
                builder.markers,
                builder.enabled);
    }

    private void initObject(String name,
                            TimeRange sourceRange,
                            AnyDictionary metadata,
                            List<Effect> effects,
                            List<Marker> markers,
                            boolean enabled) {
        Effect[] effectsArray = new Effect[effects.size()];
        effectsArray = effects.toArray(effectsArray);
        Marker[] markersArray = new Marker[markers.size()];
        markersArray = markers.toArray(markersArray);
        this.initialize(
                name,
                sourceRange,
                metadata,
                effectsArray,
                markersArray,
                enabled);
        this.nativeManager.className = this.getClass().getCanonicalName();
    }

    private native void initialize(String name,
                                   TimeRange sourceRange,
                                   AnyDictionary metadata,
                                   Effect[] effects,
                                   Marker[] markers,
                                   boolean enabled);

    public static class ItemBuilder {
        private String name = "";
        private TimeRange sourceRange = null;
        private AnyDictionary metadata = new AnyDictionary();
        private List<Effect> effects = new ArrayList<>();
        private List<Marker> markers = new ArrayList<>();
        private boolean enabled = true;

        public ItemBuilder() {
        }

        public Item.ItemBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Item.ItemBuilder setSourceRange(TimeRange sourceRange) {
            this.sourceRange = sourceRange;
            return this;
        }

        public Item.ItemBuilder setMetadata(AnyDictionary metadata) {
            this.metadata = metadata;
            return this;
        }

        public Item.ItemBuilder setEffects(List<Effect> effects) {
            this.effects = effects;
            return this;
        }

        public Item.ItemBuilder setMarkers(List<Marker> markers) {
            this.markers = markers;
            return this;
        }

        public Item.ItemBuilder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }

    /**
     * @return visibility of the Item. By default true.
     */
    public native boolean isVisible();

    public native boolean isOverlapping();

    /**
     * @return true if an Item contributes to compositions. Analogous to Mute in various NLEs.
     */
    public native boolean isEnabled();

    public native void setEnabled(boolean enabled);

    public native TimeRange getSourceRange();

    public native void setSourceRange(TimeRange sourceRange);

    public List<Effect> getEffects() {
        return Arrays.asList(getEffectsNative());
    }

    private native Effect[] getEffectsNative();

    public List<Marker> getMarkers() {
        return Arrays.asList(getMarkersNative());
    }

    private native Marker[] getMarkersNative();

    /**
     * Convience wrapper for the trimmed_range.duration of the item.
     *
     * @return getTrimmedRange().duration
     */
    public native RationalTime getDuration() throws UnsupportedOperationException, CannotComputeAvailableRangeException;

    /**
     * Implemented by child classes, available range of media.
     *
     * @return available range of media
     */
    public native TimeRange getAvailableRange() throws UnsupportedOperationException, CannotComputeAvailableRangeException;

    /**
     * The range after applying the source range.
     *
     * @return range after applying the source range.
     */
    public native TimeRange getTrimmedRange() throws UnsupportedOperationException, CannotComputeAvailableRangeException;

    /**
     * The range of this item's media visible to its parent.
     * Includes handles revealed by adjacent transitions (if any).
     * This will always be larger or equal to trimmedRange.
     *
     * @return range of this item's media visible to its parent.
     */
    public native TimeRange getVisibleRange() throws UnsupportedOperationException, CannotComputeAvailableRangeException, NotAChildException;

    /**
     * Find and return the trimmed range of this item in the parent.
     *
     * @return trimmed range in parent.
     */
    public native TimeRange getTrimmedRangeInParent() throws NotAChildException, UnsupportedOperationException, IndexOutOfBoundsException, ObjectWithoutDurationException, CannotComputeAvailableRangeException, InvalidTimeRangeException;

    /**
     * Find and return the untrimmed range of this item in the parent.
     *
     * @return untrimmed range of this item in the parent
     */
    public native TimeRange getRangeInParent() throws NotAChildException, UnsupportedOperationException, IndexOutOfBoundsException, ObjectWithoutDurationException, CannotComputeAvailableRangeException;

    /**
     * Converts time t in the coordinate system of self to coordinate
     * system of toItem.
     * Note that this and toItem must be part of the same timeline (they must
     * have a common ancestor).
     * Example:
     * 0                      20
     * [------t----D----------]
     * [--A-][t----B---][--C--]
     * 100    101    110
     * 101 in B = 6 in D
     * t = t argument
     *
     * @param time        RationalTime to be transformed
     * @param toItem      the Item in whose coordinate the time is to be transformed
     * @return time in the coordinate system of self to coordinate system of toItem
     */
    public native RationalTime getTransformedTime(
            RationalTime time,
            Item toItem) throws NotAChildException, UnsupportedOperationException, IndexOutOfBoundsException, ObjectWithoutDurationException, CannotComputeAvailableRangeException;

    /**
     * Transforms timeRange to the range of child or this toItem.
     *
     * @param timeRange   timeRange to be transformed
     * @param toItem      the Item in whose coordinate the time is to be transformed
     * @return timeRange in coordinate of toItem.
     */
    public native TimeRange getTransformedTimeRange(
            TimeRange timeRange,
            Item toItem) throws NotAChildException, UnsupportedOperationException, IndexOutOfBoundsException, ObjectWithoutDurationException, CannotComputeAvailableRangeException;

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() +
                "(" +
                "name=" + this.getName() +
                ", sourceRange=" + this.getSourceRange().toString() +
                ", effects=[" + this.getEffects()
                .stream().map(Objects::toString).collect(Collectors.joining(", ")) + "]" +
                ", markers=[" + this.getMarkers()
                .stream().map(Objects::toString).collect(Collectors.joining(", ")) + "]" +
                ", enabled=" + this.isEnabled() +
                ", metadata=" + this.getMetadata().toString() +
                ")";
    }
}
