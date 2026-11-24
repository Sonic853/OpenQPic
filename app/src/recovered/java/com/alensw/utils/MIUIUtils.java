package com.alensw.utils;

import android.text.TextUtils;

public class MIUIUtils {
   public static String getSystemProperty(String str) {
        java.io.BufferedReader bufferedReader = null;
        try {
            bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(Runtime.getRuntime().exec("getprop " + str).getInputStream()), 1024);
            String line = bufferedReader.readLine();
            bufferedReader.close();
            try {
                bufferedReader.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return line;
        } catch (java.io.IOException e2) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (java.io.IOException e3) {
                    e3.printStackTrace();
                }
            }
            return null;
        } catch (Throwable th) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (java.io.IOException e4) {
                    e4.printStackTrace();
                }
            }
            throw th;
        }
    }

   public static boolean isMIUI() {
      return TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name")) ^ true;
   }
}
