package com.alensw.io;

import java.io.FileNotFoundException;

/* JADX INFO: loaded from: classes.dex */
public class FileOpenCancelledException extends FileNotFoundException {
    public FileOpenCancelledException() {
        super("cancelled");
    }
}
