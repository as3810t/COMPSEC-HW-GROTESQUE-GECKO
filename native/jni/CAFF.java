package hu.grotesque_gecko.caff_parser;

import java.nio.ByteBuffer;
import java.io.Closeable;

public class CAFF implements Closeable {
    static {
        System.loadLibrary("caffparser");
        System.loadLibrary("caffparserjava");
    }

    private boolean valid;
    private final long caffPtr;
    private ByteBuffer preview;

    private CAFF(final ByteBuffer buffer) {
        this.caffPtr = parseCaff(buffer);
        this.valid = true;
        this.preview = null;
    }

    public static CAFF from(final ByteBuffer buffer) {
        if(!buffer.isDirect()) {
            throw new IllegalArgumentException("The buffer must be direct!");
        }

        return new CAFF(buffer);
    }

    public ByteBuffer generatePreview() {
        if(!valid) {
            throw new IllegalStateException("Resource was already freed");
        }

        if(preview == null) {
            preview = previewCaff(caffPtr);
        }
        return preview;
    }

    @Override
    public void close() {
        if(!valid) {
            throw new IllegalStateException("Resource was already freed");
        }

        freeCaff(caffPtr);
        valid = false;
    }

    @Override
    protected void finalize() {
        if(valid) {
            close();
        }
    }

    private native long parseCaff(final ByteBuffer buffer);
    private native void freeCaff(final long caff);
    private native ByteBuffer previewCaff(final long caff);
}