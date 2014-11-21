package com.android.luckybug.buildaword.Logic;

import com.android.luckybug.buildaword.Conrtol.Cell.Cell;

/**
 * Created by vasiliy.lomanov on 21.11.2014. Have a lot of fun!(c)
 */
public class Player {

    private int points;
    private Cell.Owner own;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Cell.Owner getOwn() {
        return own;
    }

    public void setOwn(Cell.Owner own) {
        this.own = own;
    }

    public Player(int points, Cell.Owner own) {
        setPoints(points);
        this.setOwn(own);
    }
}
