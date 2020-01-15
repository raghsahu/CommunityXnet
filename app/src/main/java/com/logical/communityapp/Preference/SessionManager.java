package com.logical.communityapp.Preference;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String MyPREFERENCES = "MyPrefss";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_MOBILE = "mobile";
    public static final String QUIZ_ID = "quiz_id";
    public static final String EVENT_ID = "event_id";
    public static final String KEY_TYPE = "type";
    public static final String COUS_KEY = "cous";
    private static final String IS_SKIPPED = "IsSlipped";
    private static final String Image_NAME = "image_name";
    private static final String IS_PRACTICE_RULES = "IsPracticeRules";
    private static final String Token_Id = "token";


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(MyPREFERENCES, PRIVATE_MODE);
        editor = pref.edit();
        editor = pref.edit();

    }


    public void setImage_NAME(String strName){
        editor.putString(Image_NAME, strName);
        editor.commit();
    }

    public String getImage_NAME() {
        return pref.getString(Image_NAME, null);
    }

    public void serverLogin(String strName, String strType, String strOPBal) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, strName);
        editor.putString(KEY_TYPE, strType);
        //editor.putString(KEY_OP_BAL, strOPBal);
        editor.commit();
    }

    public void serverEmailLogin(String strName, String strMobile, String strCoustId) {
//        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, strName);
        editor.putString(KEY_MOBILE, strMobile);
        editor.putString(COUS_KEY, strCoustId);
        editor.commit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(IS_LOGIN, isLoggedIn);
        editor.commit();
    }
    public void setIsPracticeRules(boolean isPracticeRules) {
        editor.putBoolean(IS_PRACTICE_RULES, isPracticeRules);
        editor.commit();
    }

    public void setSkipped(boolean isLoggedIn) {
        editor.putBoolean(IS_SKIPPED, isLoggedIn);
        editor.commit();
    }

    // Get Skipped State
    public boolean isSkipped() {
        return pref.getBoolean(IS_SKIPPED, false);
    }

    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
    public boolean isPracticeRules() {
        return pref.getBoolean(IS_PRACTICE_RULES, false);
    }

    // Clearing all data from Shared Preferences
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
    public String getQuizId() {
        return pref.getString(QUIZ_ID, null);
    }
    public void setQuizId(String quizId) {
        editor.putString(QUIZ_ID, quizId);
        editor.commit();
    }


    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public String getMobile() {
        return pref.getString(KEY_MOBILE, null);
    }

    public String getEventId() {
        return pref.getString(EVENT_ID, null);
    }
    public void setEventId(String eventId) {
        editor.putString(EVENT_ID, eventId);
        editor.commit();
    }

    public String getType() {
        return pref.getString(KEY_TYPE, null);
    }



    public String getCoustId() {
        return pref.getString(COUS_KEY, null);
    }


    public void saveToken(String token) {

        editor.putString(Token_Id, token);
        editor.apply();
        editor.commit();
    }


    public String getTokenId() {
        return pref.getString(Token_Id, "");
    }


}
