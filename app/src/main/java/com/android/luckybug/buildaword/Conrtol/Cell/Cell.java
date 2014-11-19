package com.android.luckybug.buildaword.Conrtol.Cell;

/**
 * Created by vasiliy.lomanov on 14.11.2014. Have a lot of fun!(c)
 */
public abstract class Cell {
    public static enum Owner {me, enemy, nobody}
    public interface Callback {
        public void callback(Cell cell);
    }
    public abstract void engage(Owner newOwner);
    public abstract Owner getOwner();
    public abstract void setEnable(boolean enable);
    public abstract void setOff();
    public abstract boolean pushed();
    public abstract void onClick(final Callback listener);
    public abstract String getText();
    public abstract void setText(String text);
    public abstract void setOwner(Owner own);

    public abstract int getPints();
    public abstract void setPoints(int points);

    protected Owner owner = Owner.nobody;
    protected int points = 1;
}
