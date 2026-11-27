package com.alensw.b.h;

class f extends com.alensw.b.k.e implements Runnable {
    final int buffer;
    final int handle;
    private final g playback;
    int number;

    f(g playback, int buffer, int handle) {
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
