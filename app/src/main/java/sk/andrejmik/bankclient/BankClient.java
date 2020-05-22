package sk.andrejmik.bankclient;

import android.app.Application;
import android.content.Context;

public class BankClient extends Application
{
    private static Application appInstance = null;
    
    public static Context getContext()
    {
        if (appInstance != null)
        {
            return appInstance.getApplicationContext();
        }
        return null;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        appInstance = this;
    }
}
