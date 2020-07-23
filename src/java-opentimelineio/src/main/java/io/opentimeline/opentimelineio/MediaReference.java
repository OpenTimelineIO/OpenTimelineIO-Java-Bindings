package io.opentimeline.opentimelineio;

import io.opentimeline.opentime.TimeRange;

public class MediaReference extends SerializableObjectWithMetadata {

    protected MediaReference() {
    }

    public MediaReference(long nativeHandle) {
        this.nativeHandle = nativeHandle;
    }

    public MediaReference(String name, TimeRange availableRange, AnyDictionary metadata) {
        this.initObject(name, availableRange, metadata);
    }

    public MediaReference(MediaReference.MediaReferenceBuilder mediaReferenceBuilder) {
        this.initObject(
                mediaReferenceBuilder.name,
                mediaReferenceBuilder.availableRange,
                mediaReferenceBuilder.metadata);
    }

    private void initObject(String name, TimeRange availableRange, AnyDictionary metadata) {
        this.className = this.getClass().getCanonicalName();
        this.initialize(name, availableRange, metadata);
    }

    private native void initialize(String name, TimeRange availableRange, AnyDictionary metadata);

    public static class MediaReferenceBuilder {
        private String name = "";
        private TimeRange availableRange = null;
        private AnyDictionary metadata = new AnyDictionary();

        public MediaReferenceBuilder() {
        }

        public MediaReference.MediaReferenceBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public MediaReference.MediaReferenceBuilder setAvailableRange(TimeRange availableRange) {
            this.availableRange = availableRange;
            return this;
        }


        public MediaReference.MediaReferenceBuilder setMetadata(AnyDictionary metadata) {
            this.metadata = metadata;
            return this;
        }

        public MediaReference build() {
            return new MediaReference(this);
        }
    }

    public native TimeRange getAvailableRange();

    public native void setAvailableRange(TimeRange availableRange);

    public native boolean isMissingReference();
}
