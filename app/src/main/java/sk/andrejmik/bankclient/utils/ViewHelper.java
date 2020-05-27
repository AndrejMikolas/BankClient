package sk.andrejmik.bankclient.utils;

import com.google.android.material.snackbar.Snackbar;

public class ViewHelper
{
    /**
     * Show or dismiss snackbar provided in param
     *
     * @param snackbar: Snackbar instance to be controlled
     * @param visible:  if snackbar will be shown or dismissed
     */
    public static void controlSnack(Snackbar snackbar, Boolean visible)
    {
        if (visible)
        {
            snackbar.show();
        }
        else
        {
            snackbar.dismiss();
        }
    }
}
