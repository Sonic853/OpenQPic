package com.alensw.b.e;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/* JADX INFO: loaded from: classes.dex */
final class c implements X509TrustManager {
    c() {
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
    }

    @Override // javax.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
