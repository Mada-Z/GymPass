package com.example.gympass;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String sharedPref = "session";
    String sessionKey = "sessionUser";

    public SessionManagement (Context context){
        sharedPreferences = context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession (String email) {

        editor.putString(sessionKey, email).commit();

    }

    public String getSession(){

        return sharedPreferences.getString(sessionKey, "");
    }

}
