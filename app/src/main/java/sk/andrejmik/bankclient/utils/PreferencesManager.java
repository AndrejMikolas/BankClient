package sk.andrejmik.bankclient.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

import sk.andrejmik.bankclient.BankClient;

/**
 * Helper class for easy access to SharedPreferences
 */
public class PreferencesManager
{
    public static final String PREFERENCES_KEY_SERVER_ADDRESS = "server";
    public static final String PREFERENCES_KEY_SERVER_PORT = "port";
    private static final Map<String, String> PREFERENCES_DEFAULTS = new HashMap<String, String>()
    {{
        put(PREFERENCES_KEY_SERVER_ADDRESS, "http://127.0.0.1");
        put(PREFERENCES_KEY_SERVER_PORT, "8080");
    }};
    
    //region INTERNAL METHODS
    private static SharedPreferences getSharedPreferences()
    {
        return PreferenceManager.getDefaultSharedPreferences(BankClient.getContext());
    }
    
    private static SharedPreferences.Editor getEditor()
    {
        SharedPreferences prefs = getSharedPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        //editor.clear();
        return editor;
    }
    //endregion
    
    public static void putString(String key, String value)
    {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.apply();
    }
    
    public static String getString(String key)
    {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getString(key, PREFERENCES_DEFAULTS.get(key));
    }
}