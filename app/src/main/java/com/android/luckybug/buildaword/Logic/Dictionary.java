package com.android.luckybug.buildaword.Logic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;

import com.android.luckybug.buildaword.R;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by vasiliy.lomanov on 12.11.2014.
 */
public class Dictionary {

    SQLiteAssetHelper dbOpenHelper;
    SQLiteDatabase db;

    public Dictionary(Context context) {

        dbOpenHelper = new SQLiteAssetHelper(context, "dictionary.db", null, 1);

        db = dbOpenHelper.getReadableDatabase();
    }

    public boolean contains(String word) {
        Cursor c = db.rawQuery("SELECT word FROM words WHERE word='" + word.toLowerCase() + "'", null);

        c.moveToFirst();
        int count = c.getCount();

        return count > 0;
    }
}
