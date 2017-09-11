package com.possible.rssreader.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Rupi on 2017. 09. 11..
 */

public class RssItemDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RssItem.db";

    public static class RssItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "rssitem";
        public static final String COLUMN_NAME_RSSLINK = "rsslink";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RssItemEntry.TABLE_NAME + " (" +
                    RssItemEntry._ID + " INTEGER PRIMARY KEY," +
                    RssItemEntry.COLUMN_NAME_RSSLINK + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RssItemEntry.TABLE_NAME;

    public RssItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
