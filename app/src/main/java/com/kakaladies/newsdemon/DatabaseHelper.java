package com.kakaladies.newsdemon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "ARTICLES";
    public static final String TABLE_NAME2 = "FAVOURITES";
    public static final String COL_HEADLINE = "HEADLINE";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_URL = "URL";
    public static final String COL_PUBLISHED = "PUBLISHED";

    public static final String COL_ID = "_id";
    protected static final String DATABASE_NAME = "KakaLadies";
    protected static final int VERSION_NUM = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_HEADLINE + " text, "
                + COL_DESCRIPTION + " text, "
                + COL_URL + " text, "
                + COL_PUBLISHED + " text);");

        db.execSQL("CREATE TABLE " + TABLE_NAME2
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_HEADLINE + " text, "
                + COL_DESCRIPTION + " text, "
                + COL_URL + " text, "
                + COL_PUBLISHED + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}