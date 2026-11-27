package com.alensw.b.h;

import android.os.Handler;
import android.os.SystemClock;

class g implements com.alensw.b.k.k {
    private final d owner;
    private final Handler handler;
    private final com.alensw.b.k.f available = new com.alensw.b.k.f();
    private volatile boolean stopped;

    g(d owner, Handler handler) {
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

    synchronized void recycle(f frame) {
        if (!stopped) {
            available.a(frame);
            notifyAll();
        }
    }

    void present(f frame) {
        if (stopped) return;
        if (owner.present(this, frame.number, frame.buffer, frame.handle)) recycle(frame);
        else a();
    }

    private synchronized f nextBuffer() throws InterruptedException {
        while (!stopped) {
            f frame = (f) available.b();
            if (frame != null) return frame;
            wait();
        }
        return null;
    }

    private synchronized boolean publish(f frame, boolean last, long deadline) throws InterruptedException {
        if (stopped || !handler.postAtTime(frame, this, SystemClock.uptimeMillis()) || last) return false;
        long remaining;
        while (!stopped && (remaining = deadline - SystemClock.uptimeMillis()) > 0) wait(remaining);
        return !stopped;
    }

    @Override public Void call() {
        int frameNumber = 0;
        int repeats = owner.repeatCount;
        try {
            for (f frame; (frame = nextBuffer()) != null;) {
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
