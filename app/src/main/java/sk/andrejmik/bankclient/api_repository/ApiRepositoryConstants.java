package sk.andrejmik.bankclient.api_repository;

import okhttp3.MediaType;
import sk.andrejmik.bankclient.utils.PreferencesManager;

class ApiRepositoryConstants
{
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    
    static String getServerAddress()
    {
        return String.format("%s:%s", PreferencesManager.getString(PreferencesManager.PREFERENCES_KEY_SERVER_ADDRESS),
                             PreferencesManager.getString(PreferencesManager.PREFERENCES_KEY_SERVER_PORT));
    }
}
