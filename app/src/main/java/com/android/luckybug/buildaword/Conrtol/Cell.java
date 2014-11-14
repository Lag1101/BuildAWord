package com.android.luckybug.buildaword.Conrtol;

/**
 * Created by vasiliy.lomanov on 14.11.2014.
 */
public interface Cell {
    public interface Callback {
        public void callback(Cell cell);
    }
    public boolean pushed();
    public void onClick(final Callback listener);
    public String getText();
    public void setText(String text);
}
