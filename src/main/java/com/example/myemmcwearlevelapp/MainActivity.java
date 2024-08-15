package com.example.myemmcwearlevelapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView wearLevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wearLevelTextView = findViewById(R.id.wearLevelTextView);

        // Ensure the app has root access before proceeding
        if (isDeviceRooted()) {
            String wearLevelInfo = EmmcWearLevel.checkWearLevel();
            wearLevelTextView.setText(wearLevelInfo);
        } else {
            Log.e(TAG, "This app requires root access to function properly.");
            wearLevelTextView.setText("Root access required to read eMMC wear level.");
        }
    }

    // Helper method to check if the device is rooted
    private boolean isDeviceRooted() {
        String[] paths = {
                "/system/bin/su",
                "/system/xbin/su",
                "/sbin/su",
                "/su/bin/su",
                "/system/su",
                "/system/bin/.ext/.su",
                "/system/usr/we-need-root/su-backup",
                "/system/xbin/mu"
        };
        for (String path : paths) {
            if (new java.io.File(path).exists()) return true;
        }
        return false;
    }
}