package com.github.universal.init.d;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Main extends Activity {

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEdit;
    private BootReceiver bootReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefsEdit = prefs.edit();
        bootReceiver = new BootReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bootReceiver.run("whoami");
        if (!prefs.getBoolean("enabled", false)) {
            Toast.makeText(this, getResources().getText(R.string.app_name) + " enabled", Toast.LENGTH_SHORT).show();
            prefsEdit.putBoolean("enabled", true);
        } else {
            Toast.makeText(this, getResources().getText(R.string.app_name) + " disabled", Toast.LENGTH_SHORT).show();
            prefsEdit.putBoolean("enabled", false);
        }
        prefsEdit.apply();
        finish();
    }
}
