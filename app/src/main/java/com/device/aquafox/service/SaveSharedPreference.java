package com.device.aquafox.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.device.aquafox.data.Session;


public class SaveSharedPreference {

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Set the Login Status
     * @param context
     * @param loggedIn
     */
    public static void setLoggedIn(Context context, boolean loggedIn, Session session) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(PreferencesUtility.LOGGED_IN_PREF, loggedIn);
        if(loggedIn) {
            editor.putString(PreferencesUtility.TOKEN, session.getToken());

            editor.putString(PreferencesUtility.DATA_CODIGO, String.valueOf(session.getCodigo()));
            editor.putString(PreferencesUtility.DATA_NOMBRE, session.getNombre());
            editor.putString(PreferencesUtility.DATA_APELLIDO, session.getApellido());
            editor.putString(PreferencesUtility.DATA_EMAIL, session.getEmail());
            editor.putString(PreferencesUtility.DATA_TIPODOC, session.getTipodoc());
            editor.putString(PreferencesUtility.DATA_NUMDOC, session.getNumdoc());
            editor.putString(PreferencesUtility.DATA_TELEFONO, session.getTelefono());

        } else {
            //Clean SharedPreferences
            editor.putString(PreferencesUtility.TOKEN, null);
        }

        editor.apply();
    }

    /**
     * Get the Login Status
     * @param context
     * @return boolean: login status
     */
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(PreferencesUtility.LOGGED_IN_PREF, false);
    }
    public static String getToken(Context context) {
        return getPreferences(context).getString(PreferencesUtility.TOKEN, null);
    }
    public static String getValue(Context context, String key) {
        return getPreferences(context).getString(key, null);
    }

}