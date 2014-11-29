package com.android.luckybug.buildaword.Conrtol.Bonus;

import android.view.View;
import android.widget.TextView;

import com.android.luckybug.buildaword.Conrtol.Cell.Cell;
import com.android.luckybug.buildaword.Logic.Game;

/**
 * Created by luckybug on 29.11.14.
 */
class Time extends BasicBonus {

    @Override
    void apply(Game game) {

        super.apply(null);
    }
    public Time(View image, TextView cost) {
        super(image, cost);
    }
}