package com.openmove.darkmode;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
import android.content.res.Configuration;
import android.provider.Settings;

import java.util.HashMap;
import java.util.Map;

public class DarkMode extends CordovaPlugin {
    private static final String TAG = "DarkModePlugin";
    private static final String SEM_HIGH_CONTRAST = "high_contrast";
    private static final String DARKMODE_SETTING = "darkmode";
    private static final String INVERSION_SETTING = "inversion";
    private static final String REGISTER_SETTING = "register";
    private static final String UNREGISTER_SETTING = "unregister";
    private final Map<String, CallbackContext> callbackContexts = new HashMap<>();
    private Boolean lastDarkMode = null;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) {
        Log.e(TAG, action);
        if (action.equals(DARKMODE_SETTING)) {
            callbackContext.success(isDarkModeEnabled() ? "true" : "false");
            return true;
        } else if (action.equals(INVERSION_SETTING)) {
            callbackContext.success(isInversionEnabled() ? "true" : "false");
            return true;
        } else if (action.equals(REGISTER_SETTING)) {
            String setting;
            try {
                setting = data.getString(0);
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse name of setting to register callback", e);
                callbackContext.error("Failed to parse name of setting to register callback");
                return true;
            }
            if (callbackContexts.containsKey(setting)) {
                Log.e(TAG, "There is already a callback registered for " + setting);
                callbackContext.error("There is already a callback registered for " + setting);
                return true;
            }
            final String initialValue;
            switch (setting) {
                case DARKMODE_SETTING:
                    initialValue = isDarkModeEnabled() ? "true" : "false";
                    break;
                case INVERSION_SETTING:
                    // TODO: How can the plugin get notified with system configuration changes?
                    Log.e(TAG, "Callbacks for setting " + setting + " are not currently supported");
                    callbackContext.error("Callbacks for setting " + setting + " are not currently supported");
                    return false;
                default: {
                    Log.e(TAG, "Cannot register callback for invalid setting " + setting);
                    callbackContext.error("Cannot register callback for invalid setting " + setting);
                    return true;
                }
            }
            callbackContexts.put(setting, callbackContext);
            // Return with success and keep the callback alive
            final PluginResult result = new PluginResult(PluginResult.Status.OK, initialValue);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
            return true;
        } else if (action.equals(UNREGISTER_SETTING)) {
            String setting;
            try {
                setting = data.getString(0);
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse name of setting to register callback", e);
                callbackContext.error("Failed to parse name of setting to register callback");
                return true;
            }
            final CallbackContext context = callbackContexts.remove(setting);
            if (context != null) {
                final PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(false);
                context.sendPluginResult(result);
            }
            callbackContext.success();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        final Boolean darkMode = (newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        if (!darkMode.equals(lastDarkMode)) {
            lastDarkMode = darkMode;
            final CallbackContext context = callbackContexts.get(DARKMODE_SETTING);
            if (context != null) {
                final PluginResult result = new PluginResult(PluginResult.Status.OK, darkMode ? "true" : "false");
                result.setKeepCallback(true);
                context.sendPluginResult(result);
            }
        }
    }

    private boolean isDarkModeEnabled() {
        return (cordova.getActivity().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    private boolean isInversionEnabled() {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(cordova.getActivity().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "Error finding setting ACCESSIBILITY_DISPLAY_INVERSION_ENABLED: " + e.getMessage());
            Log.d(TAG, "Checking negative color enabled status");
            accessibilityEnabled = Settings.System.getInt(cordova.getActivity().getContentResolver(), SEM_HIGH_CONTRAST, 0);
        }
        return accessibilityEnabled == 1;
    }
}

