// SPDX-License-Identifier: Apache-2.0
// Copyright Contributors to the OpenTimelineIO Project.

package io.opentimeline.opentimelineio;

import io.opentimeline.OTIONative;
import io.opentimeline.opentime.TimeRange;
import io.opentimeline.opentimelineio.exception.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A kind of composition which can hold any serializable object.
 * This composition approximates the concept of a `bin` - a collection of
 * SerializableObjects that do not have any compositing meaning, but can
 * serialize to/from OTIO correctly, with metadata and a named collection.
 */
public class SerializableCollection extends SerializableObjectWithMetadata {

    protected SerializableCollection() {
    }

    SerializableCollection(OTIONative otioNative) {
        this.nativeManager = otioNative;
    }

    public SerializableCollection(
            String name,
            List<SerializableObject> children,
            AnyDictionary metadata) {
        this.initObject(name, children, metadata);
    }

    public SerializableCollection(SerializableCollectionBuilder serializableCollectionBuilder) {
        this.initObject(
                serializableCollectionBuilder.name,
                serializableCollectionBuilder.children,
                serializableCollectionBuilder.metadata);
    }

    private void initObject(String name, List<SerializableObject> children, AnyDictionary metadata) {
        SerializableObject[] serializableObjects = new SerializableObject[children.size()];
        serializableObjects = children.toArray(serializableObjects);
        this.initialize(name, serializableObjects, metadata);
        this.nativeManager.className = this.getClass().getCanonicalName();
    }

    private native void initialize(String name, SerializableObject[] children, AnyDictionary metadata);

    public static class SerializableCollectionBuilder {
        private String name = "";
        private List<SerializableObject> children = new ArrayList<>();
        private AnyDictionary metadata = new AnyDictionary();

        public SerializableCollectionBuilder() {
        }

        public SerializableCollection.SerializableCollectionBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public SerializableCollection.SerializableCollectionBuilder setMetadata(AnyDictionary metadata) {
            this.metadata = metadata;
            return this;
        }

        public SerializableCollection.SerializableCollectionBuilder setChildren(
                List<SerializableObject> children) {
            this.children = children;
            return this;
        }

        public SerializableCollection build() {
            return new SerializableCollection(this);
        }
    }

    public List<SerializableObject> getChildren() {
        return Arrays.asList(getChildrenNative());
    }

    private native SerializableObject[] getChildrenNative();

    public void setChildren(List<SerializableObject> children) {
        setChildrenNative(children.toArray(new SerializableObject[0]));
    }

    private native void setChildrenNative(SerializableObject[] children);

    public native void clearChildren();

    public native boolean setChild(int index, SerializableObject child) throws IndexOutOfBoundsException;

    public native void insertChild(int index, SerializableObject child);

    public native boolean removeChild(int index) throws IndexOutOfBoundsException;

    public <T extends Composable> Stream<T> eachChild(
            TimeRange searchRange, Class<T> descendedFrom) throws NotAChildException, ObjectWithoutDurationException, CannotComputeAvailableRangeException {
        List<SerializableObject> children = this.getChildren();
        Stream<T> resultStream;
        try {
            resultStream = children.stream()
                    .flatMap(element -> {
                                Stream<T> currentElementStream = Stream.empty();
                                if (descendedFrom.isAssignableFrom(element.getClass()))
                                    currentElementStream = Stream.concat(Stream.of(descendedFrom.cast(element)), currentElementStream);
                                Stream<T> nestedStream = Stream.empty();
                                if (element instanceof Composition) {
                                    try {
                                        nestedStream = ((Composition) element).eachChild(
                                                searchRange,
                                                descendedFrom,
                                                false);
                                    } catch (Exception e){
                                        throw new RuntimeException(e);
                                    }
                                }
                                return Stream.concat(currentElementStream, nestedStream);
                            }
                    );
        } catch (RuntimeException e){
            if (e.getCause() instanceof NotAChildException)
                throw (NotAChildException) e.getCause();
            else if (e.getCause() instanceof ObjectWithoutDurationException)
                throw (ObjectWithoutDurationException) e.getCause();
            else if (e.getCause() instanceof CannotComputeAvailableRangeException)
                throw (CannotComputeAvailableRangeException) e.getCause();
            else throw e;
        }
        return resultStream;
    }

    public Stream<Clip> eachClip(TimeRange searchRange) throws NotAChildException, ObjectWithoutDurationException, CannotComputeAvailableRangeException {
        return this.eachChild(searchRange, Clip.class);
    }

    public List<Clip> clipIf(TimeRange search_range, boolean shallow_search){
        return Arrays.asList(clipIfNative(search_range, shallow_search));
    }

    public native Clip[] clipIfNative(TimeRange search_range, boolean shallow_search);

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() +
                "(" +
                "name=" + this.getName() +
                ", children=[" + this.getChildren()
                .stream().map(Objects::toString).collect(Collectors.joining(", ")) + "]" +
                ", metadata=" + this.getMetadata().toString() +
                ")";
    }
}
