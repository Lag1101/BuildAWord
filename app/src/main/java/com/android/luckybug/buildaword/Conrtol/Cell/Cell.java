package com.android.luckybug.buildaword.Conrtol.Cell;

/**
 * Created by vasiliy.lomanov on 14.11.2014. Have a lot of fun!(c)
 */
public interface Cell {
    public interface Callback {
        public void callback(Cell cell);
    }
    public void setOff();
    public boolean pushed();
    public void onClick(final Callback listener);
    public String getText();
    public void setText(String text);
}
