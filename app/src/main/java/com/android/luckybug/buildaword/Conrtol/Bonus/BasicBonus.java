package com.android.luckybug.buildaword.Conrtol.Bonus;

import android.view.View;
import android.widget.TextView;

import com.android.luckybug.buildaword.Conrtol.Cell.Cell;

/**
 * Created by luckybug on 29.11.14.
 */
public abstract class BasicBonus {

    public enum Type{
        Time,
        Transform,
        Capture
    }

    View imageView;
    TextView costView;
    int cost;

    static public BasicBonus creator(Type type, View image, TextView cost) {
        switch (type) {
            case Time: return new Time(image, cost);
            case Transform: return new Transform(image, cost);
            case Capture: return new Capture(image, cost);
            default: throw new NoClassDefFoundError();
        }
    }

    abstract void apply(Cell cell);

    public void setEnabled(boolean enable) {
        imageView.setEnabled(enable);
        costView.setEnabled(enable);
    }

    public void setCost(int cost) {
        this.cost = cost;
        costView.setText(Integer.toString(cost));
    }
    public int getCost() {
        return cost;
    }

    public BasicBonus(View image, TextView cost) {
        imageView = image;
        costView = cost;
    }
}