package com.android.luckybug.buildaword.Logic;

import android.os.CountDownTimer;

import com.android.luckybug.buildaword.Conrtol.Cell.Cell;
import com.android.luckybug.buildaword.Conrtol.Prison;

/**
 * Created by luckybug on 30.11.14.
 */
public class Game {
    private Prison prison;
    private Player me, enemy;

    private int msPerTurn = 15*1000;
    private Cell.Owner turn = Cell.Owner.me;

    public CountDownTimer timer;

    public Game() {

    }
}
