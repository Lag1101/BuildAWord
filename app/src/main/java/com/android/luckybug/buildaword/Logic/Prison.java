package com.android.luckybug.buildaword.Logic;

/**
 * Created by vasiliy.lomanov on 12.11.2014.
 */
public class Prison {

    static String alphabet = "абвгдеёжзийклмнопрстуфхцчъыьэюя";
    static final int size = 5;

    char cells[][] = new char[size][size];

    public Prison() {
        for( int y = 0; y < size; y++ ) {
            for( int x = 0; x < size; x++ ) {
                cells[y][x] = alphabet.charAt( (int)(Math.random() * alphabet.length()) );
            }
        }
    }
}
