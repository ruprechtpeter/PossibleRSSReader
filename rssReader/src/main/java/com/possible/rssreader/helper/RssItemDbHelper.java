package com.possible.rssreader.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rupi on 2017. 09. 11..
 */

public class RssItemDbHelper extends SQLiteOpenHelper {

    private static final String TAG = RssItemDbHelper.class.getName();
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

    private boolean findLink(String link) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + RssItemEntry.TABLE_NAME + " WHERE " + RssItemEntry.COLUMN_NAME_RSSLINK + " = \"" + link + "\"";
        Log.e(TAG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        return c.getCount() !=0;
    }

    public long saveLink(String link) {
        if (!findLink(link)) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(RssItemEntry.COLUMN_NAME_RSSLINK, link);

            long id = db.insert(RssItemEntry.TABLE_NAME, null, values);

            return id;
        } else {
            return -1;
        }
    }

    public List<String> loadLink() {
        List<String> links = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + RssItemEntry.TABLE_NAME;
        Log.e(TAG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                links.add(c.getString(c.getColumnIndex(RssItemEntry.COLUMN_NAME_RSSLINK)));
            } while (c.moveToNext());
        }

        return links;
    }

    public int deleteLink(String link) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(RssItemEntry.TABLE_NAME, RssItemEntry.COLUMN_NAME_RSSLINK + " = ?", new String[] { String.valueOf(link) });
    }


}
