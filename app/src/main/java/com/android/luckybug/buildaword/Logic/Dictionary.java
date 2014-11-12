package com.android.luckybug.buildaword.Logic;

import android.content.Context;

import com.android.luckybug.buildaword.R;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by vasiliy.lomanov on 12.11.2014.
 */
public class Dictionary {

    HashSet<String> words;

    public Dictionary(Context context) {
        words = new HashSet<String>();
        words.addAll(Arrays.asList(context.getResources().getStringArray(R.array.words)));
    }

    public boolean isIn(String word) {
        return words.contains(word);
    }
}
