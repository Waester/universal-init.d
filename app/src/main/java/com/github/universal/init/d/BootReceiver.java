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
        File dir = new File("/data/init.d");

        if (!dir.exists()) {
            run("mkdir /data/init.d");
            run("chmod 755 /data/init.d");
        } else if (!dir.canRead()) {
            run("chmod 755 /data/init.d");
        }

        // Get the list of init.d scripts
        File[] filelist = dir.listFiles();
        if (filelist != null) {
            Arrays.sort(filelist);
            for (File file : filelist) {
                if (file.canExecute()) {
                    // Execute each script in alphabetical order
                    run("sh " + file.toString());
                }
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
