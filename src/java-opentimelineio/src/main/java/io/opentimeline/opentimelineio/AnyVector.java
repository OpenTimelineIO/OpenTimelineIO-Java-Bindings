package io.opentimeline.opentimelineio;

import io.opentimeline.OTIONative;

public class AnyVector extends OTIONative {

    public AnyVector() {
        this.initObject();
    }

    public AnyVector(long nativeHandle) {
        this.nativeHandle = nativeHandle;
    }

    private void initObject() {
        this.className = this.getClass().getCanonicalName();
        this.initialize();
    }

    private native void initialize();

    public class Iterator extends OTIONative {
        private Iterator(AnyVector anyVector) {
            this.initObject(anyVector);
        }

        public Iterator(long nativeHandle) {
            this.nativeHandle = nativeHandle;
        }

        private void initObject(AnyVector anyVector) {
            this.className = this.getClass().getCanonicalName();
            this.initialize(anyVector);
        }

        private native void initialize(AnyVector anyVector);

        public boolean hasNext() {
            return hasNextNative(AnyVector.this);
        }

        public boolean hasPrevious() {
            return hasPreviousNative(AnyVector.this);
        }

        public Any next() {
            return nextNative(AnyVector.this);
        }

        public Any previous() {
            return previousNative(AnyVector.this);
        }

        private native Any nextNative(AnyVector anyVector);

        private native Any previousNative(AnyVector anyVector);

        private native boolean hasNextNative(AnyVector anyVector);

        private native boolean hasPreviousNative(AnyVector anyVector);

        private native void dispose();

        @Override
        public void close() throws Exception {
            dispose();
        }
    }

    public AnyVector.Iterator iterator() {
        return new AnyVector.Iterator(this);
    }

    public native Any get(int index);

    public native void add(Any any);

    public native void add(int index, Any any);

    public native void clear();

    public native void ensureCapacity(int minCapacity);

    public native int size();

    public boolean isEmpty() {
        return size() == 0;
    }

    public native void remove(int index);

    public native void trimToSize();

    private native void dispose();

    @Override
    public void close() throws Exception {
        dispose();
    }
}
