package com.github.universal.init.d;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.util.Arrays;

public class BootReceiver extends BroadcastReceiver {

    private SharedPreferences prefs;

    @Override
    public void onReceive(Context context, Intent intent) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (prefs.getBoolean("enabled", false)) {
            init();
        }
    }

    private void init() {
        File dir = new File("/system/etc/init.d");

        if (!dir.exists()) {
            run("mount -o remount,rw /system");
            run("mkdir /system/etc/init.d");
            run("chmod 755 /system/etc/init.d");
            run("mount -o remount,ro /system");
        } else if (!dir.canRead()) {
            run("mount -o remount,rw /system");
            run("chmod 755 /system/etc/init.d");
            run("mount -o remount,ro /system");
        }

        // Get the list of init.d scripts
        File[] filelist = dir.listFiles();
        if (filelist != null) {
            Arrays.sort(filelist);
            for (File file : filelist) {
                // Execute each script one after the other in the loop
                run("sh " + file.toString());
            }
        }
    }

    public void run(String string) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", string});
            process.waitFor();
        } catch (Exception e) {
            Log.d("init.d", "Failed to run command " + string);
        }
    }
}
