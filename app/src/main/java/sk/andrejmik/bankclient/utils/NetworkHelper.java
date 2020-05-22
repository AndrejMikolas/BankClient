package sk.andrejmik.bankclient.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import sk.andrejmik.bankclient.BankClient;

public class NetworkHelper
{
    public static boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) BankClient.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
