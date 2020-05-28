package sk.andrejmik.bankclient.api_repository;

import okhttp3.MediaType;
import sk.andrejmik.bankclient.utils.PreferencesManager;

/**
 * Constants class for API repository
 */
class ApiRepositoryConstants
{
    /* JSON media type for POST request */
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    
    /**
     * Method for getting server address stored in shared preferences. If not saved, returns https://127.0.0.1:8080
     *
     * @return Server address with port stored in shared preferences formatted
     */
    static String getServerAddress()
    {
        return String.format("%s:%s", PreferencesManager.getString(PreferencesManager.PREFERENCES_KEY_SERVER_ADDRESS),
                             PreferencesManager.getString(PreferencesManager.PREFERENCES_KEY_SERVER_PORT));
    }
}
