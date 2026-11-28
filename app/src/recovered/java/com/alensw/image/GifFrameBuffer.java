package com.alensw.image;

import com.alensw.concurrent.WorkItem;

class GifFrameBuffer extends WorkItem implements Runnable {
    final int buffer;
    final int handle;
    private final GifPlayback playback;
    int number;

    GifFrameBuffer(GifPlayback playback, int buffer, int handle) {
        this.playback = playback;
        this.buffer = buffer;
        this.handle = handle;
    }

    @Override public boolean a(int buffer, int handle, int unused, Object... args) {
        return this.buffer == buffer && this.handle == handle;
    }

    @Override public void run() {
        playback.present(this);
    }
}
