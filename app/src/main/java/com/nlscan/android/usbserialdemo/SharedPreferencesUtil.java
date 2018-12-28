package com.nlscan.android.usbserialdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author Alan
 * @Company nlscan
 * @date 2018/3/12 14:32
 * @Description:
 */
public class SharedPreferencesUtil {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static final String LOGCAT_TAG = "BarcodeScannerTag";

    private SharedPreferencesUtil() {

    }

    public static SharedPreferences getInstance(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences("urlPrefFile", Context.MODE_PRIVATE);
        }
        return preferences;
    }

    public static void writeStrPrefNode(String name, String value) {
        Log.d(LOGCAT_TAG, "writeStrPrefNode name:" + name + " value:" + value);
        editor = preferences.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static String readStrPrefNode(String name) {
        Log.d(LOGCAT_TAG, "readStrPrefNode name:" + preferences.getString(name, ""));
        return preferences.getString(name, "");
    }

    public static void writeBooleanPrefNode(String name, Boolean value) {
        Log.d(LOGCAT_TAG, "writeBooleanPrefNode name:" + name + " value:" + value);
        editor = preferences.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static Boolean readBooleanPrefNode(String name) {
        Log.d(LOGCAT_TAG, "readBooleanPrefNode name:" + preferences.getBoolean(name, false));
        return preferences.getBoolean(name, false);
    }

    public static void writeIntPrefNode(String name, int value) {
        Log.d(LOGCAT_TAG, "writeIntegerPrefNode name:" + name + " value:" + value);
        editor = preferences.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public static int readIntPrefNode(String name) {
        Log.d(LOGCAT_TAG, "readIntegerPrefNode name:" + preferences.getInt(name, 0));
        return preferences.getInt(name, 0);
    }
}
