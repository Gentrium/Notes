package com.example.maks.notes;

import android.app.Application;

import com.backendless.Backendless;
import com.example.maks.notes.constants.Constants;
import com.example.maks.notes.database.DB;

public class NotesApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        String appVersion = "v1";
        Backendless.initApp( this, Constants.APP_ID, Constants.SECRET_KEY, appVersion );
        new DB(getApplicationContext());
    }
}
