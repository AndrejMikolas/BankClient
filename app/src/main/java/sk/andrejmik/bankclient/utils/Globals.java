package sk.andrejmik.bankclient.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Globals
{
    public static final Gson GSON = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
}
