package com.alensw.image;

import android.os.Handler;
import android.os.SystemClock;
import com.alensw.concurrent.CancellableCallable;
import com.alensw.concurrent.WorkQueue;

class GifPlayback implements CancellableCallable {
    private final GifMovie owner;
    private final Handler handler;
    private final WorkQueue available = new WorkQueue();
    private volatile boolean stopped;

    GifPlayback(GifMovie owner, Handler handler) {
        this.owner = owner;
        this.handler = handler;
    }

    @Override public synchronized void a() {
        stopped = true;
        handler.removeCallbacksAndMessages(this);
        available.a();
        notifyAll();
    }

    @Override public void b() {}

    synchronized void recycle(GifFrameBuffer frame) {
        if (!stopped) {
            available.a(frame);
            notifyAll();
        }
    }

    void present(GifFrameBuffer frame) {
        if (stopped) return;
        if (owner.present(this, frame.number, frame.buffer, frame.handle)) recycle(frame);
        else a();
    }

    private synchronized GifFrameBuffer nextBuffer() throws InterruptedException {
        while (!stopped) {
            GifFrameBuffer frame = (GifFrameBuffer) available.b();
            if (frame != null) return frame;
            wait();
        }
        return null;
    }

    private synchronized boolean publish(GifFrameBuffer frame, boolean last, long deadline) throws InterruptedException {
        if (stopped || !handler.postAtTime(frame, this, SystemClock.uptimeMillis()) || last) return false;
        long remaining;
        while (!stopped && (remaining = deadline - SystemClock.uptimeMillis()) > 0) wait(remaining);
        return !stopped;
    }

    @Override public Void call() {
        int frameNumber = 0;
        int repeats = Picture.gifPlaybackRepeatCount(owner.repeatCount);
        try {
            for (GifFrameBuffer frame; (frame = nextBuffer()) != null;) {
                long start = SystemClock.uptimeMillis();
                frame.number = frameNumber;
                int delay = owner.decode(frameNumber, frame.buffer, frame.handle);
                if (delay <= 0) break;
                boolean endOfPass = ++frameNumber == owner.frameCount;
                if (!publish(frame, endOfPass && repeats == 0, Math.max(start + delay, SystemClock.uptimeMillis() + 10))) break;
                if (endOfPass) {
                    frameNumber = 0;
                    if (repeats > 0) repeats--;
                }
            }
        } catch (InterruptedException interrupted) {
            a();
            Thread.currentThread().interrupt();
        }
        return null;
    }
}
