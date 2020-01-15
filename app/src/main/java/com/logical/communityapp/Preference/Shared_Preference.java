package com.logical.communityapp.Preference;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;

public class Shared_Preference {
    public static final String SHARED_PREFERENCE_NAME = "EXPENSEMGT";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String MOBILE = "mobile";
    public static final String PROFILEJSONDATA = "jsondata";
    public static final String LIVEGAMEJSONDATA = "livejsondata";
    public static final String IMAGE = "image";
    public static final String User_Id = "user_id";


    Context context;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor2;

    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_NAME = "user_fullname";


    public static void setName(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, value);
        editor.commit();
    }

    public static void setEmail(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL, value);
        editor.commit();
    }

    public static String getName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(NAME, "");
    }

    public static String getEmail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(EMAIL, "");
    }

    public static void setJsondata(Context context, JSONArray headname) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROFILEJSONDATA, headname.toString());
        editor.commit();
    }

    public static String getJsondata(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(PROFILEJSONDATA, null);
    }
    //*************************************
    public static void setLivegamejsondata(Context context, JSONArray headname) {
        if (context!=null){
            SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(LIVEGAMEJSONDATA, headname.toString());
            editor.commit();
        }

    }

    public static String getLivegamejsondata(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(LIVEGAMEJSONDATA, null);
    }

    //***************************************
    public static void setUser_Id(Context context, String headname) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(User_Id, headname);
        editor.commit();
    }

    public static String getUser_Id(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(User_Id, "");
    }

    //***************************************
    public static void setImage(Context context, String headname) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(IMAGE, headname);
        editor.commit();
    }

    public static String getImage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
        return preferences.getString(IMAGE, "");
    }



    public void cleardatetime() {
        editor2.clear();
        editor2.commit();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(IS_LOGIN, false);
    }

}
