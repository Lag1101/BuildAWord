package com.android.luckybug.buildaword.Logic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by vasiliy.lomanov on 12.11.2014. Have a lot of fun!(c)
 */
public class Dictionary {

    private final SQLiteDatabase db;

    public Dictionary(Context context) {

        SQLiteAssetHelper dbOpenHelper = new SQLiteAssetHelper(context, "dictionary.db", null, 1);

        db = dbOpenHelper.getReadableDatabase();
    }

    public boolean contains(String word) {
        Cursor c = db.rawQuery("SELECT word FROM words WHERE word='" + word.toLowerCase() + "'", null);

        c.moveToFirst();
        int count = c.getCount();

        return count > 0;
    }
}
