package org.apache.http;

/* JADX INFO: loaded from: classes.dex */
public interface Header {
    HeaderElement[] getElements() throws ParseException;

    String getName();

    String getValue();
}
