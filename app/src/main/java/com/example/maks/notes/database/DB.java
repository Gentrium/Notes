package com.example.maks.notes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DB {
    private static String NOTE_DB = "note_db.sqlite";

    private static DaoMaster.DevOpenHelper helper = null;
    private static DaoMaster master = null;
    private static SQLiteDatabase database = null;

    public DB(Context context) {
        Log.i("DB", NOTE_DB);
        helper = new DaoMaster.DevOpenHelper(context, NOTE_DB, null);
        database = helper.getWritableDatabase();
        master = new DaoMaster(database);
    }

    /*
     * класс для работы с daoMaster
     */
    public static synchronized DaoMaster getDaoMaster() {
        if (master == null) {
            master = new DaoMaster(getDBInstance());
        }
        return master;
    }

    /*
     * метод для доступа к бд
     */
    public static synchronized SQLiteDatabase getDBInstance() {
        if (database == null) {
            database = helper.getWritableDatabase();
        }
        return database;
    }

}
