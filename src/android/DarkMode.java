package com.openmove.darkmode;

import org.apache.cordova.*;
import org.json.JSONArray;

import android.util.Log;
import android.content.res.Configuration;
import android.provider.Settings;

public class DarkMode extends CordovaPlugin {
    String TAG = "DarkModePlugin";

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext){
        if (action.equals("darkmode")) {

            int a = cordova.getActivity().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch(a) {
            case Configuration.UI_MODE_NIGHT_NO:
                Log.d(TAG, "Dark Mode: off");
		callbackContext.success("false");
                break;
            // Night mode is not active, we're in day time
            case Configuration.UI_MODE_NIGHT_YES:
                Log.d(TAG, "Dark Mode: on");
		callbackContext.success("true");
                break;
            // Night mode is active, we're at night!
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                // We don't know what mode we're in, assume notnight
                Log.d(TAG, "Dark Mode: undefined");
		callbackContext.success("false");
                break;
        	}
            return true;

        } else if (action.equals("inversion")) {

            boolean isInversionEnabled =  false;
            int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(cordova.getActivity().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "Error finding setting ACCESSIBILITY_DISPLAY_INVERSION_ENABLED: " + e.getMessage());
            Log.d(TAG, "Checking negative color enabled status");
            final String SEM_HIGH_CONTRAST = "high_contrast";
            accessibilityEnabled = Settings.System.getInt(cordova.getActivity().getContentResolver(), SEM_HIGH_CONTRAST, 0);
        }
        if (accessibilityEnabled == 1) {
            Log.d(TAG, "inversion  or negative colour is enabled");
            isInversionEnabled = true;
        } else {
            Log.d(TAG, "inversion  or negative colour is disabled");
        }
            callbackContext.success(""+isInversionEnabled);

            return true;

        } else {
            return false;
        }
    }
}

