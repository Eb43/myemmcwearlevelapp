package com.example.myemmcwearlevelapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EmmcWearLevel {

    private static final String TAG = "EmmcWearLevel";
    private static final String EXT_CSD_PATH = "/sys/class/mmc_host/mmc0/mmc0:0001/ext_csd";

    public static String checkWearLevel() {
        Process process = null;
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        try {
            // Execute the command with root access
            process = Runtime.getRuntime().exec("su -c cat " + EXT_CSD_PATH);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Read the ext_csd file as a byte array
            char[] extCsd = new char[512];
            reader.read(extCsd, 0, 512);

            // Extract the specific bytes
            int preEolInfo = extCsd[268];
            int lifeTimeA = extCsd[269];
            int lifeTimeB = extCsd[270];

            // Interpret and log the results
            String preEolStatus = getPreEolStatus(preEolInfo);
            String lifeTimeAStatus = getLifeTimeEstimation(lifeTimeA);
            String lifeTimeBStatus = getLifeTimeEstimation(lifeTimeB);

            result.append("Pre-EOL Info: ").append(preEolStatus).append("\n");
            result.append("Device Life Time Estimation Type A: ").append(lifeTimeAStatus).append("\n");
            result.append("Device Life Time Estimation Type B: ").append(lifeTimeBStatus).append("\n");

            Log.i(TAG, result.toString());

        } catch (IOException e) {
            Log.e(TAG, "Failed to read ext_csd file", e);
            return "Failed to read eMMC wear level.";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Failed to close BufferedReader", e);
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result.toString();
    }

    private static String getPreEolStatus(int preEolInfo) {
        switch (preEolInfo) {
            case 0x01:
                return "Normal";
            case 0x02:
                return "Warning";
            case 0x03:
                return "Urgent";
            default:
                return "Unknown";
        }
    }

    private static String getLifeTimeEstimation(int lifeTime) {
        switch (lifeTime) {
            case 0x01:
                return "0% - 10% used";
            case 0x02:
                return "10% - 20% used";
            case 0x03:
                return "20% - 30% used";
            case 0x04:
                return "30% - 40% used";
            case 0x05:
                return "40% - 50% used";
            case 0x06:
                return "50% - 60% used";
            case 0x07:
                return "60% - 70% used";
            case 0x08:
                return "70% - 80% used";
            case 0x09:
                return "80% - 90% used";
            case 0x0A:
                return "90% - 100% used";
            default:
                return "Unknown";
        }
    }
}