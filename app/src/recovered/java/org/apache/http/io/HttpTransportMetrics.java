package org.apache.http.io;

/* JADX INFO: loaded from: classes.dex */
public interface HttpTransportMetrics {
    long getBytesTransferred();

    void reset();
}
